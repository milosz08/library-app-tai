-- liquibase formatted sql
-- changeset milosz08:2

CREATE TABLE IF NOT EXISTS ota_tokens(
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,

  token VARCHAR(10) NOT NULL UNIQUE,
  expires DATETIME NOT NULL,
  used BOOL NOT NULL DEFAULT FALSE,
  type ENUM('ACTIVATE_ACCOUNT') NOT NULL,
  user_id BIGINT UNSIGNED NOT NULL,

  FOREIGN KEY (user_id) REFERENCES users(id),
  PRIMARY KEY (id)
)
ENGINE=InnoDB;


CREATE EVENT IF NOT EXISTS remove_expired_ota_token
  ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 12 HOUR
  DO
  DELETE FROM ota_tokens WHERE expires < NOW() AND used = 0
