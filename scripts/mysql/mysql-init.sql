-- Creates test user info and grants privileges.
CREATE USER 'reader'@'localhost' IDENTIFIED BY 'password';
GRANT SELECT, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'reader' IDENTIFIED BY 'password';
FLUSH PRIVILEGES;

CREATE USER 'inserter'@'localhost' IDENTIFIED BY 'password';
GRANT SELECT, UPDATE, INSERT, DELETE ON *.* TO 'inserter' IDENTIFIED BY 'password';
FLUSH PRIVILEGES;

-- Create the `example` database.
DROP
    DATABASE IF EXISTS `example_db`;
CREATE
    DATABASE `example_db` CHARSET = utf8;

-- Uses `example_db` database.
USE `example_db`;

-- Create test tables.
DROP TABLE IF EXISTS `xxx`;
CREATE TABLE `xxx`
(
    `sensor_id`   VARCHAR(200),
    `temperature` BIGINT,
    `status`   VARCHAR(50),
    `event_time` BIGINT,
    `ptime` TIMESTAMP(3)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `sensors`;
CREATE TABLE `sensors`
(
    `sensor_id`   VARCHAR(200),
    `temperature` BIGINT,
    `status`   VARCHAR(50),
    `event_time` BIGINT,
    `ptime` TIMESTAMP(3)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `cep_data`;
CREATE TABLE `cep_data`
(
  `sensor_id`   VARCHAR(200),
  `event_time` TIMESTAMP(3),
  `non_errors` BIGINT NOT NULL,
  `history`   VARCHAR(255),
  `min_temperature`   DOUBLE,
  `avg_temperature`   DOUBLE,
  `max_temperature`   DOUBLE,
  `elapsed` BIGINT
) ENGINE = InnoDB
DEFAULT CHARSET = utf8;