-- liquibase formatted sql
-- changeset milosz08:5

ALTER TABLE ota_tokens
  MODIFY COLUMN type ENUM('ACTIVATE_ACCOUNT', 'CHANGE_FIRST_PASSWORD', 'RESET_PASSWORD') NOT NULL;
