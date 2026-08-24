-- Insertion des profils du système SIGR
-- Ce script doit être exécuté après la création de la table profil

-- 1. Administrateur
INSERT INTO profil (id_profil, code, libelle, description, created_at, created_by)
VALUES (
    gen_random_uuid(),
    'ADMIN',
    'Administrateur',
    'Gère la configuration générale du système. Accès complet à la configuration (lecture et modification).',
    NOW(),
    'system'
) ON CONFLICT (code) DO NOTHING;

-- 2. CMMR (Comité de Maîtrise des Risques Ministériel)
INSERT INTO profil (id_profil, code, libelle, description, created_at, created_by)
VALUES (
    gen_random_uuid(),
    'CMMR',
    'CMMR',
    'Valide la cartographie des risques (CR) du ministère portant sur les risques inhérents. Consultation uniquement. Peut Valider, Différer ou Rejeter.',
    NOW(),
    'system'
) ON CONFLICT (code) DO NOTHING;

-- 3. CCI (Comité de Contrôle Interne)
INSERT INTO profil (id_profil, code, libelle, description, created_at, created_by)
VALUES (
    gen_random_uuid(),
    'CCI',
    'CCI',
    'Consolide et valide, avec le CODIR, la cartographie des risques d''une unité administrative (UA) parente avant sa transmission au CMMR. Consultation uniquement. Peut Valider, Différer ou Rejeter.',
    NOW(),
    'system'
) ON CONFLICT (code) DO NOTHING;

-- 4. Pilote de processus
INSERT INTO profil (id_profil, code, libelle, description, created_at, created_by)
VALUES (
    gen_random_uuid(),
    'PILOTE',
    'Pilote de processus',
    'Valide, avec le CODIR, la cartographie des risques de son processus avant sa transmission au CCI. Consultation uniquement. Peut Valider, Différer ou Rejeter.',
    NOW(),
    'system'
) ON CONFLICT (code) DO NOTHING;

-- 5. Manager Risque
INSERT INTO profil (id_profil, code, libelle, description, created_at, created_by)
VALUES (
    gen_random_uuid(),
    'MANAGER_RISQUE',
    'Manager Risque',
    'Identifie, en concertation avec les assistants pilotes, les risques inhérents. Évalue les risques. Coordonne la mise en œuvre des plans de mitigation au niveau du processus. Création, modification et suppression sur Formalisation, Risques inhérents, Évaluation, Mitigation et Cartographie. Accès global sur tous les processus.',
    NOW(),
    'system'
) ON CONFLICT (code) DO NOTHING;

-- 5bis. Responsable Risque
INSERT INTO profil (id_profil, code, libelle, description, created_at, created_by)
VALUES (
    gen_random_uuid(),
    'RESPONSABLE_RISQUES',
    'Responsable Risque',
    'Directeur général ou assimilé, porte la responsabilité de gestion des risques de son unité administrative. Consultation uniquement sur Formalisation, Évaluation, Mitigation et Audit ; appose son visa dans le circuit de validation des risques (Cartographie).',
    NOW(),
    'system'
) ON CONFLICT (code) DO NOTHING;

-- 5ter. Correspondant Risque
INSERT INTO profil (id_profil, code, libelle, description, created_at, created_by)
VALUES (
    gen_random_uuid(),
    'CORRESPONDANT_RISQUE',
    'Correspondant Risque',
    'Cadre au niveau d''un département opérationnel. Mêmes droits de création/modification que le Manager Risque sur Formalisation, Évaluation et Mitigation, mais cantonné aux données de sa propre unité administrative. Cartographie et Audit en lecture seule.',
    NOW(),
    'system'
) ON CONFLICT (code) DO NOTHING;

-- 6. Responsable d'action
INSERT INTO profil (id_profil, code, libelle, description, created_at, created_by)
VALUES (
    gen_random_uuid(),
    'RESPONSABLE_ACTION',
    'Responsable d''action',
    'Met en œuvre les actions qui lui sont confiées. Renseigne les indicateurs associés. Soumet les résultats au Responsable des risques pour appréciation. Peut modifier uniquement les actions qui lui sont affectées et renseigner les indicateurs.',
    NOW(),
    'system'
) ON CONFLICT (code) DO NOTHING;

-- 7. Auditeur
INSERT INTO profil (id_profil, code, libelle, description, created_at, created_by)
VALUES (
    gen_random_uuid(),
    'AUDITEUR',
    'Auditeur',
    'Consulte les informations dans le cadre des missions d''audit. Tous les modules accessibles en lecture seule, sauf Audit accessible en lecture et modification.',
    NOW(),
    'system'
) ON CONFLICT (code) DO NOTHING;

-- 8. Contrôleur Interne
INSERT INTO profil (id_profil, code, libelle, description, created_at, created_by)
VALUES (
    gen_random_uuid(),
    'CONTROLEUR_INTERNE',
    'Contrôleur Interne',
    'Réalise les contrôles de second niveau et les rapports de contrôle interne d''un processus. Évaluations, Mitigation, Audit et Cartographie définitive accessibles en lecture seule ; création, modification et suppression sur le module Contrôle Interne.',
    NOW(),
    'system'
) ON CONFLICT (code) DO NOTHING;
