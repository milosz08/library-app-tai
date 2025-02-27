-- liquibase formatted sql
-- changeset milosz08:7

DROP TABLE addresses;

CREATE TABLE IF NOT EXISTS addresses
(
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,

  street VARCHAR(100) NOT NULL,
  building_number VARCHAR(10) NOT NULL,
  apartment_number VARCHAR(10) DEFAULT NULL,
  city VARCHAR(100) NOT NULL,
  user_id BIGINT UNSIGNED NOT NULL,

  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
  PRIMARY KEY (id)
)
ENGINE=InnoDB;

-- password: Test123@
INSERT INTO users(id, first_name, last_name, email, password, active, role_id)
VALUES (3, 'Julia', 'Testowa', 'julia@testowa.com', '$2a$10$Crgw5lBRDf7j4uC1zHis8ufRm8M.OSutRKmPuMS1bTVGn8lMSL8zC', TRUE, 1);

INSERT INTO addresses(id, street, building_number, apartment_number, city, user_id)
VALUES (1, 'Akademicka', '20A', NULL, 'Gliwice', 3);
