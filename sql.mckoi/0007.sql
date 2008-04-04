ALTER CREATE TABLE mitglied 
(
  id            INTEGER     default UNIQUEKEY('mitglied'), 
  externemitgliedsnummer INTEGER,
  anrede        VARCHAR(10),
  titel         VARCHAR(20),
  name          VARCHAR(40) NOT NULL, 
  vorname       VARCHAR(40) NOT NULL, 
  strasse       VARCHAR(40) NOT NULL, 
  plz           VARCHAR(10)  NOT NULL, 
  ort           VARCHAR(40) NOT NULL, 
  zahlungsweg   INTEGER,
  zahlungsrhytmus INTEGER,
  blz           VARCHAR(8),
  konto         VARCHAR(10),
  kontoinhaber  VARCHAR(27),
  geburtsdatum  DATE,
  geschlecht    CHAR(1),
  telefonprivat VARCHAR(15),
  telefondienstlich VARCHAR(15),
  email         VARCHAR(50),
  eintritt      DATE,
  beitragsgruppe INTEGER,
  zahlerid      INTEGER,
  austritt      DATE,
  kuendigung    DATE,
  vermerk1      VARCHAR(255),
  vermerk2      VARCHAR(255),
  eingabedatum  DATE,
  UNIQUE        (id), 
  UNIQUE        (externemitgliedsnummer),
  PRIMARY KEY   (id)
);
