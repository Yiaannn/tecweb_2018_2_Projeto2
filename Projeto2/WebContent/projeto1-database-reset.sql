DROP DATABASE IF EXISTS tecwebprojeto1;
CREATE DATABASE tecwebprojeto1;

USE tecwebprojeto1;

CREATE TABLE User (
  id_user  INT  NOT NULL  AUTO_INCREMENT,
  login_name  VARCHAR(32)  NOT NULL,
  display_name  VARCHAR(32),
  pass_hash CHAR(64),
  PRIMARY KEY (id_user)
);

CREATE TABLE Note (
	id_notes INT NOT NULL AUTO_INCREMENT,
    message_body TEXT,
    creation_date DATE NOT NULL,
    expiry_date DATE,
    priority_level INT NOT NULL,
    is_active BOOLEAN NOT NULL,
    id_owner INT NOT NULL,
    PRIMARY KEY (id_notes),
    CONSTRAINT fkCor FOREIGN KEY (id_owner)
        REFERENCES User (id_user)
);