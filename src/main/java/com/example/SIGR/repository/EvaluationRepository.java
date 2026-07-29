package com.example.SIGR.repository;

import com.example.SIGR.dto.response.CartographieRisqueDetailResponse;
import com.example.SIGR.entity.Evaluation;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, String> {

    // ================= EXISTANT =================

    Optional<Evaluation> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByRisque_Code(String codeRisque);

    long countByRisque_Processus_Unite_Code(String codeUnite);

    // ================= CARTOGRAPHIE DETAILLEE =================
    @Query("""
    SELECT new com.example.SIGR.dto.response.CartographieRisqueDetailResponse(

        r.code,
        r.libelle,
        r.typeRisque,
        r.statut,
        r.dateIdentification,

        p.code,
        p.libelle,
        ua.libelle,

        r.causeProbable,
        r.consequenceProbable,

        e.impactInherent,
        e.probabiliteInherente,
        CAST((e.impactInherent * e.probabiliteInherente) AS integer),

        e.protection,
        e.prevention,
        CAST((e.impactInherent - e.protection) AS integer),
        CAST((e.probabiliteInherente - e.prevention) AS integer),
        CAST(((e.impactInherent - e.protection) * (e.probabiliteInherente - e.prevention)) AS integer),

        CASE
            WHEN e.rangPriorite = 1 THEN '1-Zone d''observation (Actions non nécessaires - Réévaluation périodique)'
            WHEN e.rangPriorite = 2 THEN '2- Zone d''amélioration (Actions de remédiation à moyen terme)'
            WHEN e.rangPriorite = 3 THEN '3 -Zone d''audit et de traitement des risques prioritaires (Actions de remédiation immédiates)'
            ELSE 'Zone d''observation (Actions non nécessaires - Réévaluation périodique)'
        END,

        e.controleExistants,
        e.controleInexistants,
        e.dejaSurvenu,

        e.recommandation,
        e.dateDebut,
        e.dateFin,
        r.bonnesPratiques,

        ag.nom

    )
    FROM Evaluation e
    JOIN e.risque r
    JOIN r.processus p
    JOIN p.unite ua
    LEFT JOIN e.evaluePar ag
    WHERE (:annee IS NULL OR YEAR(r.dateIdentification) = :annee)
""")

    List<CartographieRisqueDetailResponse> findCartographieRisquesDetail(@Param("annee") Integer annee);

    @Query("""
    SELECT new com.example.SIGR.dto.response.CartographieRisqueDetailResponse(

        r.code,
        r.libelle,
        r.typeRisque,
        r.statut,
        r.dateIdentification,

        p.code,
        p.libelle,
        ua.libelle,

        r.causeProbable,
        r.consequenceProbable,

        e.impactInherent,
        e.probabiliteInherente,
        CAST((e.impactInherent * e.probabiliteInherente) AS integer),

        e.protection,
        e.prevention,
        CAST((e.impactInherent - e.protection) AS integer),
        CAST((e.probabiliteInherente - e.prevention) AS integer),
        CAST(((e.impactInherent - e.protection) * (e.probabiliteInherente - e.prevention)) AS integer),

        CASE
            WHEN e.rangPriorite = 1 THEN '1-Zone d''observation (Actions non nécessaires - Réévaluation périodique)'
            WHEN e.rangPriorite = 2 THEN '2- Zone d''amélioration (Actions de remédiation à moyen terme)'
            WHEN e.rangPriorite = 3 THEN '3 -Zone d''audit et de traitement des risques prioritaires (Actions de remédiation immédiates)'
            ELSE 'Zone d''observation (Actions non nécessaires - Réévaluation périodique)'
        END,

        e.controleExistants,
        e.controleInexistants,
        e.dejaSurvenu,

        e.recommandation,
        e.dateDebut,
        e.dateFin,
        r.bonnesPratiques,

        ag.nom

    )
    FROM Evaluation e
    JOIN e.risque r
    JOIN r.processus p
    JOIN p.unite ua
    LEFT JOIN e.evaluePar ag
    WHERE ua.code = :codeUnite
    AND (:annee IS NULL OR YEAR(r.dateIdentification) = :annee)
""")

    List<CartographieRisqueDetailResponse> findCartographieRisquesDetailByUnite(
            @Param("codeUnite") String codeUnite,
            @Param("annee") Integer annee
    );

    // ================= OPTIONNEL =================

    @Query("""
        SELECT e
        FROM Evaluation e
        JOIN FETCH e.risque r
        JOIN FETCH r.processus p
        JOIN FETCH p.unite ua
        WHERE r.cartographie.id = :idCartographie
    """)
    List<Evaluation> findByCartographie(@Param("idCartographie") String idCartographie);
}