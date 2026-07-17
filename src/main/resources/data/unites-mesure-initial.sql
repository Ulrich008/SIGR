-- Insertion des unités de mesure initiales

-- Unités de mesure numériques
INSERT INTO unite_mesure (id_unite_mesure, code_unite_mesure, libelle_unite_mesure, symbole, description, type_unite) VALUES
('1', 'POURCENTAGE', 'Pourcentage', '%', 'Pourcentage de progression', 'NUMERIQUE'),
('2', 'HEURE', 'Heure', 'h', 'Durée en heures', 'NUMERIQUE'),
('3', 'MINUTE', 'Minute', 'min', 'Durée en minutes', 'NUMERIQUE'),
('4', 'JOUR', 'Jour', 'j', 'Durée en jours', 'NUMERIQUE'),
('5', 'EURO', 'Euro', '€', 'Montant en euros', 'NUMERIQUE'),
('6', 'SCORE_10', 'Score sur 10', '/10', 'Note sur 10', 'NUMERIQUE'),
('7', 'SCORE_100', 'Score sur 100', '/100', 'Note sur 100', 'NUMERIQUE'),
('8', 'METRE_CUBE', 'Mètre cube', 'm³', 'Volume en mètre cube', 'NUMERIQUE'),
('9', 'KILOGRAMME', 'Kilogramme', 'kg', 'Masse en kilogramme', 'NUMERIQUE'),
('10', 'LITRE', 'Litre', 'L', 'Volume en litre', 'NUMERIQUE');

-- Unités de mesure de type date



-- Insertion des unités de mesure initiales

-- Unités de mesure numériques
INSERT INTO unite_mesure (
    id_unite_mesure,
    code_unite_mesure,
    libelle_unite_mesure,
    symbole,
    description,
    type_unite
) VALUES
('1', 'POURCENTAGE', 'Pourcentage', '%', 'Pourcentage de progression', 'NUMERIQUE'),
('2', 'HEURE', 'Heure', 'h', 'Durée en heures', 'NUMERIQUE'),
('3', 'MINUTE', 'Minute', 'min', 'Durée en minutes', 'NUMERIQUE'),
('4', 'JOUR', 'Jour', 'j', 'Durée en jours', 'NUMERIQUE'),
('5', 'EURO', 'Euro', '€', 'Montant en euros', 'NUMERIQUE'),
('6', 'SCORE_10', 'Score sur 10', '/10', 'Note sur 10', 'NUMERIQUE'),
('7', 'SCORE_100', 'Score sur 100', '/100', 'Note sur 100', 'NUMERIQUE'),
('8', 'METRE_CUBE', 'Mètre cube', 'm³', 'Volume en mètre cube', 'NUMERIQUE'),
('9', 'KILOGRAMME', 'Kilogramme', 'kg', 'Masse en kilogramme', 'NUMERIQUE'),
('10', 'LITRE', 'Litre', 'L', 'Volume en litre', 'NUMERIQUE'),
('11', 'NOMBRE', 'Nombre', '', 'Valeur numérique sans unité spécifique', 'NUMERIQUE');

-- Unités de mesure de type date
INSERT INTO unite_mesure (
    id_unite_mesure,
    code_unite_mesure,
    libelle_unite_mesure,
    symbole,
    description,
    type_unite
) VALUES
('12', 'DATE', 'Date', '', 'Valeur de type date', 'DATE');