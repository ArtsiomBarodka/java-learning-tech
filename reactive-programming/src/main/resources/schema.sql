DROP TABLE IF EXISTS sport;

CREATE TABLE IF NOT EXISTS sport (
  ID bigint not null AUTO_INCREMENT,
  NAME varchar(100) not null,
  PRIMARY KEY ( ID )
);