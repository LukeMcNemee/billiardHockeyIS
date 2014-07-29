CREATE TABLE "TEAMS" (
    "ID" BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "NAME" VARCHAR(255),
    "COACH" VARCHAR(255)    
);

CREATE TABLE "PLAYERS" (
    "ID" BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "NAME" VARCHAR(255),
    "SURNAME" VARCHAR(255),
    "NUMBER"  SMALLINT,
    "TEAMID" BIGINT REFERENCES TEAMS(ID),
    "HOME" SMALLINT,
    "AWAY" SMALLINT,
    "POSITION" VARCHAR(255)    
);

CREATE TABLE "MATCHES" (
    "ID" BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "HOMETEAMID" BIGINT REFERENCES TEAMS(ID),
    "AWAYTEAMID" BIGINT REFERENCES TEAMS(ID),
    "DATE" DATE,
    "HOMEOWNGOALS" INTEGER,
    "AWAYOWNGOALS" INTEGER,
    "HOMEADVANTAGEGOALS" INTEGER,
    "AWAYADVANTAGEGOALS" INTEGER,
    "HOMECONTUMATIONGOALS" INTEGER,
    "AWAYCONTUMATIONGOALS" INTEGER,
    "HOMETECHNICALGOALS" INTEGER,
    "AWAYTECHNICALGOALS" INTEGER
);

CREATE TABLE "GOALS"(
    "PLAYERID" BIGINT REFERENCES PLAYERS(ID),
    "MATCHID" BIGINT REFERENCES MATCHES(ID),
    "AMOUNT" INTEGER
);

