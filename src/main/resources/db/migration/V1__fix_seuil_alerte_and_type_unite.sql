-- Migration pour corriger les erreurs de conversion de type de colonne

-- 1. Fixer la colonne seuil_alerte dans indicateur_performance
-- Supprimer la contrainte NOT NULL temporairement pour permettre la conversion
ALTER TABLE indicateur_performance ALTER COLUMN seuil_alerte DROP NOT NULL;

-- Convertir la colonne seuil_alerte de double vers date
-- Les valeurs existantes seront mises à NULL car la conversion n'est pas possible
ALTER TABLE indicateur_performance ALTER COLUMN seuil_alerte TYPE date USING NULL;

-- Rétablir la contrainte NOT NULL
ALTER TABLE indicateur_performance ALTER COLUMN seuil_alerte SET NOT NULL;

-- 2. Fixer la colonne seuil_alerte dans indicateur_performance_audit
ALTER TABLE indicateur_performance_audit ALTER COLUMN seuil_alerte DROP NOT NULL;
ALTER TABLE indicateur_performance_audit ALTER COLUMN seuil_alerte TYPE date USING NULL;
ALTER TABLE indicateur_performance_audit ALTER COLUMN seuil_alerte SET NOT NULL;

-- 3. Ajouter la colonne type_unite dans unite_mesure avec une valeur par défaut
ALTER TABLE unite_mesure ADD COLUMN IF NOT EXISTS type_unite varchar(20);
UPDATE unite_mesure SET type_unite = 'NUMERIQUE' WHERE type_unite IS NULL;
ALTER TABLE unite_mesure ALTER COLUMN type_unite SET NOT NULL;
ALTER TABLE unite_mesure ADD CONSTRAINT check_type_unite CHECK (type_unite IN ('NUMERIQUE', 'DATE'));

-- 4. Ajouter la colonne type_unite dans unite_mesure_audit
ALTER TABLE unite_mesure_audit ADD COLUMN IF NOT EXISTS type_unite varchar(20);
ALTER TABLE unite_mesure_audit ADD CONSTRAINT check_type_unite_audit CHECK (type_unite IN ('NUMERIQUE', 'DATE'));
