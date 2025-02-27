-- liquibase formatted sql
-- changeset milosz08:3

INSERT INTO roles(id, name)
VALUES (1, 'CUSTOMER'),
       (2, 'EMPLOYER'),
       (3, 'ADMIN');

-- @formatter:off

-- password: Test123@
INSERT INTO users(id, first_name, last_name, email, password, active, role_id)
VALUES (1, 'Adam', 'Nowak', 'adam@nowak.com', '$2a$10$Crgw5lBRDf7j4uC1zHis8ufRm8M.OSutRKmPuMS1bTVGn8lMSL8zC', TRUE, 2),
       (2, 'Jan', 'Nowak', 'jan@nowak.com', '$2a$10$Crgw5lBRDf7j4uC1zHis8ufRm8M.OSutRKmPuMS1bTVGn8lMSL8zC', TRUE, 3)

-- @formatter:on
