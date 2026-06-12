-- Migration pour corriger le type des colonnes valeur_cible et valeur_obtenue
-- Permet de stocker à la fois des valeurs numériques et des dates

-- 1. Fixer la colonne valeur_cible dans indicateur_performance
ALTER TABLE indicateur_performance ALTER COLUMN valeur_cible TYPE varchar(50);

-- 2. Fixer la colonne valeur_obtenue dans indicateur_performance
ALTER TABLE indicateur_performance ALTER COLUMN valeur_obtenue TYPE varchar(50);

-- 3. Fixer la colonne valeur_cible dans indicateur_performance_audit
ALTER TABLE indicateur_performance_audit ALTER COLUMN valeur_cible TYPE varchar(50);

-- 4. Fixer la colonne valeur_obtenue dans indicateur_performance_audit
ALTER TABLE indicateur_performance_audit ALTER COLUMN valeur_obtenue TYPE varchar(50);
