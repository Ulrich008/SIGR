-- Ajouter les colonnes pour la gestion de l'avis sur les risques
ALTER TABLE risque ADD COLUMN avis_risque VARCHAR(50);
ALTER TABLE risque ADD COLUMN motif VARCHAR(1000);
ALTER TABLE risque ADD COLUMN transmis BOOLEAN DEFAULT FALSE;

-- Mettre à jour les enregistrements existants avec une valeur par défaut
UPDATE risque SET avis_risque = 'EN_ATTENTE' WHERE avis_risque IS NULL;
UPDATE risque SET transmis = FALSE WHERE transmis IS NULL;
