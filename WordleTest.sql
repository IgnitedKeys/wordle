DROP DATABASE IF EXISTS WordleTest;
CREATE DATABASE WordleTest;

USE WordleTest;

CREATE TABLE Game (
gameId INT PRIMARY KEY AUTO_INCREMENT,
answer CHAR(5) NOT NULL,
finished BIT NOT NULL
);

CREATE TABLE Round (
roundId INT PRIMARY KEY AUTO_INCREMENT,
gameId INT,
guess CHAR(5) NOT NULL,
result VARCHAR(10) NOT NULL,
FOREIGN KEY(gameId) REFERENCES Game(gameId)
);