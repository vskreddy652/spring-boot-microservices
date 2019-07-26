CREATE SCHEMA IF NOT EXISTS technology_service;
USE technology_service;

CREATE TABLE IF NOT EXISTS skill (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  created_date datetime NOT NULL,
  updated_date datetime NOT NULL,
  name varchar(255) NOT NULL,
  prerequisites varchar(255) NOT NULL,
  toc varchar(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

SHOW DATABASES;