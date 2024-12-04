-- liquibase formatted sql
-- changeset milosz08:4

CREATE TABLE IF NOT EXISTS logs(
	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,

	message VARCHAR(255) NOT NULL,
	level ENUM('INFO', 'ERROR') NOT NULL,
	executed_time DATETIME NOT NULL,

	PRIMARY KEY (id)
)
ENGINE=InnoDB;
