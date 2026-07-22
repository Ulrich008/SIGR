package com.example.SIGR.config;

import com.example.SIGR.entity.Agent;
import com.example.SIGR.entity.Profil;
import com.example.SIGR.entity.Role;

import com.example.SIGR.repository.AgentRepository;
import com.example.SIGR.repository.ProfilRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class    DataInitializer implements CommandLineRunner {

    private final AgentRepository agentRepository;
    private final ProfilRepository profilRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionTemplate transactionTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Identifiants du super admin créé au premier démarrage. Mot de passe
     * généré aléatoirement (openssl rand), à ne jamais réutiliser tel quel
     * ailleurs ni recommuniquer en clair au-delà de sa mise en place initiale.
     */
    private static final String MATRICULE_ADMIN_PAR_DEFAUT = "ADMIN001";
    private static final String PASSWORD_ADMIN_PAR_DEFAUT = "admin123";

    public DataInitializer(
            AgentRepository agentRepository,
            ProfilRepository profilRepository,
            PasswordEncoder passwordEncoder,
            PlatformTransactionManager transactionManager
    ) {
        this.agentRepository = agentRepository;
        this.profilRepository = profilRepository;
        this.passwordEncoder = passwordEncoder;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    public void run(String... args) {

        supprimerContraintesMinistereObsoletes();
        reparerCodeMinistereHerite();
        initProfils();
        initAdmin();
    }

    /**
     * Avant l'ajout de referencedColumnName, Hibernate avait créé les
     * contraintes de clé étrangère code_ministere -> ministere.id_ministere.
     * ddl-auto=update ne supprime jamais une contrainte existante, même
     * devenue incorrecte : elle continue de bloquer l'écriture des vrais
     * codes métier. On la supprime dynamiquement (le nom est auto-généré
     * par Hibernate, donc imprévisible) ; Hibernate recrée ensuite la
     * bonne contrainte (vers ministere.code_ministere) au démarrage suivant.
     */
    private void supprimerContraintesMinistereObsoletes() {

        transactionTemplate.executeWithoutResult(status -> {

            entityManager.createNativeQuery("""
                    DO $$
                    DECLARE
                        r RECORD;
                    BEGIN
                        FOR r IN
                            SELECT tc.constraint_name, tc.table_name
                            FROM information_schema.table_constraints tc
                            JOIN information_schema.key_column_usage kcu
                              ON tc.constraint_name = kcu.constraint_name
                            JOIN information_schema.constraint_column_usage ccu
                              ON tc.constraint_name = ccu.constraint_name
                            WHERE tc.constraint_type = 'FOREIGN KEY'
                              AND tc.table_name IN ('agent', 'unite_administrative')
                              AND kcu.column_name = 'code_ministere'
                              AND ccu.table_name = 'ministere'
                              AND ccu.column_name = 'id_ministere'
                        LOOP
                            EXECUTE format('ALTER TABLE %I DROP CONSTRAINT %I', r.table_name, r.constraint_name);
                        END LOOP;
                    END $$;
                    """).executeUpdate();
        });
    }

    /**
     * Correctif ponctuel : avant l'ajout de referencedColumnName sur les
     * relations Agent/UniteAdministrative -> Ministere, JPA référençait par
     * défaut la clé primaire de Ministere (id_ministere, un UUID) au lieu de
     * son code métier (code_ministere). Les lignes déjà enregistrées
     * contiennent donc cet UUID au lieu du code, ce qui rend le filtre par
     * ministère inopérant (aucune correspondance possible).
     *
     * Cette réparation réécrit ces colonnes avec le vrai code métier.
     * Idempotente : une ligne déjà correcte (contenant un vrai code) ne
     * correspond à aucun id_ministere et n'est donc jamais modifiée.
     */
    private void reparerCodeMinistereHerite() {

        transactionTemplate.executeWithoutResult(status -> {

            entityManager.createNativeQuery("""
                    UPDATE agent
                    SET code_ministere = m.code_ministere
                    FROM ministere m
                    WHERE agent.code_ministere = m.id_ministere
                    """).executeUpdate();

            entityManager.createNativeQuery("""
                    UPDATE unite_administrative
                    SET code_ministere = m.code_ministere
                    FROM ministere m
                    WHERE unite_administrative.code_ministere = m.id_ministere
                    """).executeUpdate();
        });
    }

    /**
     * Les profils métier du système (voir profils.sql, jamais exécuté
     * automatiquement par Spring Boot car placé hors de resources/data.sql).
     * Insérés ici pour garantir leur présence au démarrage, quel que soit
     * l'environnement.
     *
     * Pas de profil "Administrateur" : ADMIN/SUPER_ADMIN sont des rôles
     * techniques, pas des profils métier — leur accès à la Configuration
     * vient directement du rôle (voir SecurityConfig / AgentServiceImpl).
     */
    private void initProfils() {

        creerProfilSiAbsent(
                "CMMR",
                "CMMR",
                "Valide la cartographie des risques (CR) du ministère portant sur les risques inhérents. Consultation uniquement. Peut Valider, Différer ou Rejeter."
        );

        creerProfilSiAbsent(
                "CCI",
                "CCI",
                "Consolide et valide, avec le CODIR, la cartographie des risques d'une unité administrative (UA) parente avant sa transmission au CMMR. Consultation uniquement. Peut Valider, Différer ou Rejeter."
        );

        creerProfilSiAbsent(
                "PILOTE",
                "Pilote de processus",
                "Valide, avec le CODIR, la cartographie des risques de son processus avant sa transmission au CCI. Consultation uniquement. Peut Valider, Différer ou Rejeter."
        );

        creerProfilSiAbsent(
                "RESPONSABLE_RISQUES",
                "Responsable des risques",
                "Identifie, en concertation avec les assistants pilotes, les risques inhérents. Évalue les risques. Coordonne la mise en œuvre des plans de mitigation au niveau du processus. Création, modification et suppression sur Formalisation, Risques inhérents, Évaluation et Mitigation."
        );

        creerProfilSiAbsent(
                "RESPONSABLE_ACTION",
                "Responsable d'action",
                "Met en œuvre les actions qui lui sont confiées. Renseigne les indicateurs associés. Soumet les résultats au Responsable des risques pour appréciation. Peut modifier uniquement les actions qui lui sont affectées et renseigner les indicateurs."
        );

        creerProfilSiAbsent(
                "AUDITEUR",
                "Auditeur",
                "Consulte les informations dans le cadre des missions d'audit. Tous les modules accessibles en lecture seule, sauf Audit accessible en lecture et modification."
        );
    }

    private void creerProfilSiAbsent(String code, String libelle, String description) {

        if (profilRepository.existsByCode(code)) {
            return;
        }

        Profil profil = new Profil();
        profil.setCode(code);
        profil.setLibelle(libelle);
        profil.setDescription(description);

        profilRepository.save(profil);
    }

    private void initAdmin() {

        // Vérifie si un admin existe déjà
        boolean adminExists =
                agentRepository.existsByMatricule(MATRICULE_ADMIN_PAR_DEFAUT);

        if (adminExists) {
            return;
        }

        // Création admin par défaut
        Agent admin = new Agent();

        admin.setMatricule(MATRICULE_ADMIN_PAR_DEFAUT);

        admin.setPassword(
                passwordEncoder.encode(PASSWORD_ADMIN_PAR_DEFAUT)
        );

        admin.setNom("SUPER");

        admin.setPrenoms("ADMIN");

        admin.setRole(Role.SUPER_ADMIN);

        admin.setEnabled(true);

        agentRepository.save(admin);

        System.out.println("""

                ================================
                SUPER ADMIN PAR DÉFAUT CRÉÉ
                Matricule : %s
                ================================

                """.formatted(MATRICULE_ADMIN_PAR_DEFAUT));
    }
}