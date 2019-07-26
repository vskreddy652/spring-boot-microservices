CREATE SCHEMA IF NOT EXISTS search_service;
USE search_service;

CREATE TABLE IF NOT EXISTS mentor_skills (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  created_date datetime NOT NULL,
  updated_date datetime NOT NULL,
  facilities_offered varchar(255) NOT NULL,
  mentor_id bigint(20) NOT NULL,
  self_rating float NOT NULL,
  skill_id bigint(20) NOT NULL,
  tarinings_delivered int(11) NOT NULL,
  years_of_experience float NOT NULL,
  fees float NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS mentor_calendar (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  created_date datetime NOT NULL,
  updated_date datetime NOT NULL,
  end_date varchar(255) NOT NULL,
  end_time varchar(255) NOT NULL,
  mentor_id bigint(20) NOT NULL,
  skill_id bigint(20) NOT NULL,
  start_date varchar(255) NOT NULL,
  start_time varchar(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;