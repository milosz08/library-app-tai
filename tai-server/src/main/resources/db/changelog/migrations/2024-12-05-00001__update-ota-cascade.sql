-- liquibase formatted sql
-- changeset milosz08:6

DROP TABLE ota_tokens;

CREATE TABLE IF NOT EXISTS ota_tokens(
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,

  token VARCHAR(10) NOT NULL UNIQUE,
  expires DATETIME NOT NULL,
  used BOOL NOT NULL DEFAULT FALSE,
  type ENUM('ACTIVATE_ACCOUNT', 'CHANGE_FIRST_PASSWORD', 'RESET_PASSWORD') NOT NULL,
  user_id BIGINT UNSIGNED NOT NULL,

  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
  PRIMARY KEY (id)
)
ENGINE=InnoDB;
