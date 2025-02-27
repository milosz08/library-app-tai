-- liquibase formatted sql
-- changeset milosz08:9

-- @formatter:off

INSERT INTO books(id, title, isbn, year, publisher, city, copies)
VALUES (1, 'Algorytmy i struktury danych: Od podstaw do zaawansowania', '978-83-123456-88-8', 2022, 'Wydawnictwo Naukowe PWN', 'Warszawa', 8),
       (2, 'Równania różniczkowe w praktyce inżynierskiej', '978-83-987654-45-3', 2021, 'Oficyna Matematyczna', 'Kraków', 20),
       (3, 'Sztuczna inteligencja: Teoria i zastosowania', '978-83-654321-77-2', 2023, 'Informatyka Polska', 'Poznań', 11),
       (4, 'Matematyka dyskretna: Kompendium wiedzy dla informatyków', '978-83-765432-11-4', 2020, 'Sigma Press', 'Wrocław', 5),
       (5, 'Uczenie maszynowe w praktyce: Projekty w Pythonie', '978-83-876543-90-6', 2022, 'DataScience Lab', 'Gdańsk', 7),
       (6, 'Teoria grafów i jej zastosowania w informatyce', '978-83-543210-56-7', 2019, 'Wydawnictwo Politechniczne', 'Łódź', 2),
       (7, 'Podstawy kryptografii: Od klasycznych szyfrów po kwantowe bezpieczeństwo', '978-83-213654-98-2', 2023, 'Wydawnictwo Krypton', 'Warszawa', 4);

-- @formatter:on

INSERT INTO authors(id, first_name, last_name, book_id)
VALUES (1, 'Piotr', 'Zieliński', 1),
       (2, 'Anna', 'Nowicka', 1),
       (3, 'Anna', 'Lewandowska', 2),
       (4, 'Marek', 'Nowicki', 3),
       (5, 'Tomasz', 'Piotrowski', 3),
       (6, 'Ewa', 'Kowalska', 3),
       (7, 'Joanna', 'Kowalczyk', 4),
       (8, 'Andrzej', 'Lisowski', 4),
       (9, 'Tomasz', 'Wiśniewski', 5),
       (10, 'Ewa', 'Kamińska', 6),
       (11, 'Piotr', 'Zawadzki', 6),
       (12, 'Krzysztof', 'Malicki', 7);

