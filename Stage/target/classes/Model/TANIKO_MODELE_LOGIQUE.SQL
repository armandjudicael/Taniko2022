-- -----------------------------------------------------------------------------
--             Génération d'une base de données pour
--                           PostgreSQL
--                        (1/10/2021 20:57:07)
-- -----------------------------------------------------------------------------
--      Nom de la base : TANIKO_MODELE_LOGIQUE
--      Projet : TANIKO_WS
--      Auteur : Taniko
--      Date de dernière modification : 1/10/2021 20:55:44
-- -----------------------------------------------------------------------------

drop database TANIKO_MODELE_LOGIQUE;
-- -----------------------------------------------------------------------------
--       CREATION DE LA BASE 
-- -----------------------------------------------------------------------------

CREATE DATABASE TANIKO_MODELE_LOGIQUE;

-- -----------------------------------------------------------------------------
--       TABLE : NOTIFICATION
-- -----------------------------------------------------------------------------

CREATE TABLE NOTIFICATION
   (
    IDNOTIFICATION DB_TEXT, 32 NOT NULL  ,
    MESSAGE DB_TEXT, 255 NOT NULL  ,
    STATUSNOTIFICATION DB_TEXT, 32 NOT NULL  
,   CONSTRAINT PK_NOTIFICATION PRIMARY KEY (IDNOTIFICATION)
   );

-- -----------------------------------------------------------------------------
--       TABLE : UTILISATEUR
-- -----------------------------------------------------------------------------

CREATE TABLE UTILISATEUR
   (
    IDUTILISATEUR DB_LONG(4) NOT NULL  ,
    NOM DB_TEXT, 255 NOT NULL  ,
    PRENOM DB_TEXT, 255 NULL  ,
    NOMUTILISATEUR DB_TEXT, 255 NOT NULL  ,
    MOTDEPASSE DB_TEXT, 255 NOT NULL  ,
    FONCTION DB_TEXT, 255 NOT NULL  ,
    STATUS DB_INTEGER(1) NULL  ,
    PHOTO DB_INTEGER(255) NULL  ,
    TYPEUTILISATEUR DB_TEXT, 255 NOT NULL  
,   CONSTRAINT PK_UTILISATEUR PRIMARY KEY (IDUTILISATEUR)
   );

-- -----------------------------------------------------------------------------
--       TABLE : AFFAIRE
-- -----------------------------------------------------------------------------

CREATE TABLE AFFAIRE
   (
    IDAFFAIRE DB_INTEGER(2) NOT NULL  ,
    NUMEROREPERAGE DB_TEXT, 32 NOT NULL  ,
    NUMEROJTR DB_TEXT, 32 NOT NULL  ,
    NUMORD DB_TEXT, 32 NOT NULL  ,
    IDTERRAIN DB_TEXT, 32 NOT NULL  ,
    IDDMD DB_INTEGER(2) NOT NULL  ,
    NUMAFFAIRE DB_TEXT, 255 NOT NULL  ,
    DATEFORMULATION DB_DATE(12) NOT NULL  ,
    OBSERVATION DB_TEXT, 255 NOT NULL  ,
    TYPEAFFAIRE DB_TEXT, 255 NOT NULL  ,
    STATUSAFFAIRE DB_TEXT, 255 NOT NULL  ,
    DESCRIPTION DB_TEXT, 255 NULL  
,   CONSTRAINT PK_AFFAIRE PRIMARY KEY (IDAFFAIRE)
   );

-- -----------------------------------------------------------------------------
--       INDEX DE LA TABLE AFFAIRE
-- -----------------------------------------------------------------------------

CREATE UNIQUE INDEX I_FK_AFFAIRE_REPERAGE
     ON AFFAIRE (NUMEROREPERAGE)
    ;

CREATE UNIQUE INDEX I_FK_AFFAIRE_JOURNALDETRESORER
     ON AFFAIRE (NUMEROJTR)
    ;

CREATE UNIQUE INDEX I_FK_AFFAIRE_ORDONNANCE
     ON AFFAIRE (NUMORD)
    ;

CREATE  INDEX I_FK_AFFAIRE_TERRAIN
     ON AFFAIRE (IDTERRAIN)
    ;

CREATE  INDEX I_FK_AFFAIRE_DEMANDEUR
     ON AFFAIRE (IDDMD)
    ;

-- -----------------------------------------------------------------------------
--       TABLE : PROCEDURE
-- -----------------------------------------------------------------------------

CREATE TABLE PROCEDURE
   (
    IDPROCEDURE DB_INTEGER(1) NOT NULL  ,
    NOMPROCEDURE DB_TEXT, 255 NOT NULL  ,
    TYPEPROCEDURE DB_TEXT, 32 NOT NULL  
,   CONSTRAINT PK_PROCEDURE PRIMARY KEY (IDPROCEDURE)
   );

-- -----------------------------------------------------------------------------
--       TABLE : DEMANDEUR
-- -----------------------------------------------------------------------------

CREATE TABLE DEMANDEUR
   (
    IDDMD DB_INTEGER(2) NOT NULL  ,
    NOMDMD DB_TEXT, 255 NOT NULL  ,
    PRENOMDMD DB_TEXT, 255 NULL  ,
    ADRESSEDMD DB_TEXT, 255 NULL  ,
    PARCELLEDMD DB_TEXT, 255 NULL  ,
    LOTDMD DB_TEXT, 255 NULL  ,
    NOMINSTITUTION DB_TEXT, 255 NULL  ,
    EMAIL DB_TEXT, 255 NULL  ,
    NUMTEL DB_TEXT, 255 NULL  ,
    PROFESSION DB_TEXT, 255 NULL  
,   CONSTRAINT PK_DEMANDEUR PRIMARY KEY (IDDMD)
   );

-- -----------------------------------------------------------------------------
--       TABLE : TERRAIN
-- -----------------------------------------------------------------------------

CREATE TABLE TERRAIN
   (
    IDTERRAIN DB_TEXT, 32 NOT NULL  ,
    QUARTIER DB_TEXT, 255 NOT NULL  ,
    COMMUNE DB_TEXT, 255 NOT NULL  ,
    DISTRICT DB_TEXT, 255 NOT NULL  ,
    PARCELLE DB_TEXT, 255 NULL  ,
    REGION DB_TEXT, 255 NOT NULL  ,
    SUPERFICIE DB_TEXT, 255 NOT NULL  
,   CONSTRAINT PK_TERRAIN PRIMARY KEY (IDTERRAIN)
   );

-- -----------------------------------------------------------------------------
--       TABLE : ORDONNANCE
-- -----------------------------------------------------------------------------

CREATE TABLE ORDONNANCE
   (
    NUMORD DB_TEXT, 32 NOT NULL  ,
    DATEORD DB_TEXT, 32 NULL  
,   CONSTRAINT PK_ORDONNANCE PRIMARY KEY (NUMORD)
   );

-- -----------------------------------------------------------------------------
--       TABLE : TITRE
-- -----------------------------------------------------------------------------

CREATE TABLE TITRE
   (
    IDTITRE DB_TEXT, 32 NOT NULL  ,
    IDTERRAIN DB_TEXT, 32 NOT NULL  ,
    NUMEROTITRE DB_TEXT, 255 NOT NULL  ,
    DATECREATION DB_DATE(12) NOT NULL  ,
    NUMEROMORCELEMENT DB_TEXT, 255 NULL  
,   CONSTRAINT PK_TITRE PRIMARY KEY (IDTITRE)
   );

-- -----------------------------------------------------------------------------
--       INDEX DE LA TABLE TITRE
-- -----------------------------------------------------------------------------

CREATE UNIQUE INDEX I_FK_TITRE_TERRAIN
     ON TITRE (IDTERRAIN)
    ;

-- -----------------------------------------------------------------------------
--       TABLE : REPERAGE
-- -----------------------------------------------------------------------------

CREATE TABLE REPERAGE
   (
    NUMEROREPERAGE DB_TEXT, 32 NOT NULL  ,
    DATEREPERAGE DB_TEXT, 32 NULL  
,   CONSTRAINT PK_REPERAGE PRIMARY KEY (NUMEROREPERAGE)
   );

-- -----------------------------------------------------------------------------
--       TABLE : JOURNALDETRESORERIEDERECETTE
-- -----------------------------------------------------------------------------

CREATE TABLE JOURNALDETRESORERIEDERECETTE
   (
    NUMEROJTR DB_TEXT, 32 NOT NULL  ,
    DATEJTR DB_TEXT, 32 NULL  
,   CONSTRAINT PK_JOURNALDETRESORERIEDERECETTE PRIMARY KEY (NUMEROJTR)
   );

-- -----------------------------------------------------------------------------
--       TABLE : PIECEJOINTE
-- -----------------------------------------------------------------------------

CREATE TABLE PIECEJOINTE
   (
    IDPIECE DB_LONG(4) NOT NULL  ,
    IDAFFAIRE DB_INTEGER(2) NOT NULL  ,
    NOMPIECEJOINTE DB_TEXT, 255 NOT NULL  ,
    EXTENSION DB_TEXT, 255 NOT NULL  ,
    VALEUR DB_INTEGER(255) NOT NULL  
,   CONSTRAINT PK_PIECEJOINTE PRIMARY KEY (IDPIECE)
   );

-- -----------------------------------------------------------------------------
--       INDEX DE LA TABLE PIECEJOINTE
-- -----------------------------------------------------------------------------

CREATE  INDEX I_FK_PIECEJOINTE_AFFAIRE
     ON PIECEJOINTE (IDAFFAIRE)
    ;

-- -----------------------------------------------------------------------------
--       TABLE : DEPENDRE
-- -----------------------------------------------------------------------------

CREATE TABLE DEPENDRE
   (
    IDTERRAIN DB_TEXT, 32 NOT NULL  ,
    IDTITRE DB_TEXT, 32 NULL  
,   CONSTRAINT PK_DEPENDRE PRIMARY KEY (IDTERRAIN)
   );

-- -----------------------------------------------------------------------------
--       INDEX DE LA TABLE DEPENDRE
-- -----------------------------------------------------------------------------

CREATE  INDEX I_FK_DEPENDRE_TITRE
     ON DEPENDRE (IDTITRE)
    ;

-- -----------------------------------------------------------------------------
--       TABLE : ETRE_NOTIFIER
-- -----------------------------------------------------------------------------

CREATE TABLE ETRE_NOTIFIER
   (
    IDNOTIFICATION DB_TEXT, 32 NOT NULL  ,
    IDUTILISATEUR DB_LONG(4) NOT NULL  
,   CONSTRAINT PK_ETRE_NOTIFIER PRIMARY KEY (IDNOTIFICATION, IDUTILISATEUR)
   );

-- -----------------------------------------------------------------------------
--       INDEX DE LA TABLE ETRE_NOTIFIER
-- -----------------------------------------------------------------------------

CREATE  INDEX I_FK_ETRE_NOTIFIER_NOTIFICATIO
     ON ETRE_NOTIFIER (IDNOTIFICATION)
    ;

CREATE  INDEX I_FK_ETRE_NOTIFIER_UTILISATEUR
     ON ETRE_NOTIFIER (IDUTILISATEUR)
    ;

-- -----------------------------------------------------------------------------
--       TABLE : AVOIR_CONCERNER
-- -----------------------------------------------------------------------------

CREATE TABLE AVOIR_CONCERNER
   (
    IDPROCEDURE DB_INTEGER(1) NOT NULL  ,
    IDAFFAIRE DB_INTEGER(2) NULL  
,   CONSTRAINT PK_AVOIR_CONCERNER PRIMARY KEY (IDPROCEDURE)
   );

-- -----------------------------------------------------------------------------
--       INDEX DE LA TABLE AVOIR_CONCERNER
-- -----------------------------------------------------------------------------

CREATE  INDEX I_FK_AVOIR_CONCERNER_AFFAIRE
     ON AVOIR_CONCERNER (IDAFFAIRE)
    ;

-- -----------------------------------------------------------------------------
--       TABLE : ETRE_TRAITER
-- -----------------------------------------------------------------------------

CREATE TABLE ETRE_TRAITER
   (
    IDAFFAIRE DB_INTEGER(2) NOT NULL  ,
    IDUTILISATEUR DB_LONG(4) NOT NULL  ,
    DATEDSISPATCH DB_DATE(12) NOT NULL  
,   CONSTRAINT PK_ETRE_TRAITER PRIMARY KEY (IDAFFAIRE, IDUTILISATEUR)
   );

-- -----------------------------------------------------------------------------
--       INDEX DE LA TABLE ETRE_TRAITER
-- -----------------------------------------------------------------------------

CREATE  INDEX I_FK_ETRE_TRAITER_AFFAIRE
     ON ETRE_TRAITER (IDAFFAIRE)
    ;

CREATE  INDEX I_FK_ETRE_TRAITER_UTILISATEUR
     ON ETRE_TRAITER (IDUTILISATEUR)
    ;

-- -----------------------------------------------------------------------------
--       TABLE : ENVOYER
-- -----------------------------------------------------------------------------

CREATE TABLE ENVOYER
   (
    IDPROCEDURE DB_INTEGER(1) NOT NULL  ,
    IDAFFAIRE DB_INTEGER(2) NOT NULL  ,
    DATEDEPART DB_DATE(12) NULL  ,
    DATEARRIVE DB_DATE(12) NULL  ,
    NUMDEPART DB_TEXT, 255 NULL  ,
    NUMARRIVE DB_TEXT, 255 NULL  
,   CONSTRAINT PK_ENVOYER PRIMARY KEY (IDPROCEDURE, IDAFFAIRE)
   );

-- -----------------------------------------------------------------------------
--       INDEX DE LA TABLE ENVOYER
-- -----------------------------------------------------------------------------

CREATE  INDEX I_FK_ENVOYER_PROCEDURE
     ON ENVOYER (IDPROCEDURE)
    ;

CREATE  INDEX I_FK_ENVOYER_AFFAIRE
     ON ENVOYER (IDAFFAIRE)
    ;


-- -----------------------------------------------------------------------------
--       CREATION DES REFERENCES DE TABLE
-- -----------------------------------------------------------------------------


ALTER TABLE AFFAIRE ADD 
     CONSTRAINT FK_AFFAIRE_REPERAGE
          FOREIGN KEY (NUMEROREPERAGE)
               REFERENCES REPERAGE (NUMEROREPERAGE);

ALTER TABLE AFFAIRE ADD 
     CONSTRAINT FK_AFFAIRE_JOURNALDETRESORERIEDE
          FOREIGN KEY (NUMEROJTR)
               REFERENCES JOURNALDETRESORERIEDERECETTE (NUMEROJTR);

ALTER TABLE AFFAIRE ADD 
     CONSTRAINT FK_AFFAIRE_ORDONNANCE
          FOREIGN KEY (NUMORD)
               REFERENCES ORDONNANCE (NUMORD);

ALTER TABLE AFFAIRE ADD 
     CONSTRAINT FK_AFFAIRE_TERRAIN
          FOREIGN KEY (IDTERRAIN)
               REFERENCES TERRAIN (IDTERRAIN);

ALTER TABLE AFFAIRE ADD 
     CONSTRAINT FK_AFFAIRE_DEMANDEUR
          FOREIGN KEY (IDDMD)
               REFERENCES DEMANDEUR (IDDMD);

ALTER TABLE TITRE ADD 
     CONSTRAINT FK_TITRE_TERRAIN
          FOREIGN KEY (IDTERRAIN)
               REFERENCES TERRAIN (IDTERRAIN);

ALTER TABLE PIECEJOINTE ADD 
     CONSTRAINT FK_PIECEJOINTE_AFFAIRE
          FOREIGN KEY (IDAFFAIRE)
               REFERENCES AFFAIRE (IDAFFAIRE);

ALTER TABLE DEPENDRE ADD 
     CONSTRAINT FK_DEPENDRE_TERRAIN
          FOREIGN KEY (IDTERRAIN)
               REFERENCES TERRAIN (IDTERRAIN);

ALTER TABLE DEPENDRE ADD 
     CONSTRAINT FK_DEPENDRE_TITRE
          FOREIGN KEY (IDTITRE)
               REFERENCES TITRE (IDTITRE);

ALTER TABLE ETRE_NOTIFIER ADD 
     CONSTRAINT FK_ETRE_NOTIFIER_NOTIFICATION
          FOREIGN KEY (IDNOTIFICATION)
               REFERENCES NOTIFICATION (IDNOTIFICATION);

ALTER TABLE ETRE_NOTIFIER ADD 
     CONSTRAINT FK_ETRE_NOTIFIER_UTILISATEUR
          FOREIGN KEY (IDUTILISATEUR)
               REFERENCES UTILISATEUR (IDUTILISATEUR);

ALTER TABLE AVOIR_CONCERNER ADD 
     CONSTRAINT FK_AVOIR_CONCERNER_PROCEDURE
          FOREIGN KEY (IDPROCEDURE)
               REFERENCES PROCEDURE (IDPROCEDURE);

ALTER TABLE AVOIR_CONCERNER ADD 
     CONSTRAINT FK_AVOIR_CONCERNER_AFFAIRE
          FOREIGN KEY (IDAFFAIRE)
               REFERENCES AFFAIRE (IDAFFAIRE);

ALTER TABLE ETRE_TRAITER ADD 
     CONSTRAINT FK_ETRE_TRAITER_AFFAIRE
          FOREIGN KEY (IDAFFAIRE)
               REFERENCES AFFAIRE (IDAFFAIRE);

ALTER TABLE ETRE_TRAITER ADD 
     CONSTRAINT FK_ETRE_TRAITER_UTILISATEUR
          FOREIGN KEY (IDUTILISATEUR)
               REFERENCES UTILISATEUR (IDUTILISATEUR);

ALTER TABLE ENVOYER ADD 
     CONSTRAINT FK_ENVOYER_PROCEDURE
          FOREIGN KEY (IDPROCEDURE)
               REFERENCES PROCEDURE (IDPROCEDURE);

ALTER TABLE ENVOYER ADD 
     CONSTRAINT FK_ENVOYER_AFFAIRE
          FOREIGN KEY (IDAFFAIRE)
               REFERENCES AFFAIRE (IDAFFAIRE);


-- -----------------------------------------------------------------------------
--                FIN DE GENERATION
-- -----------------------------------------------------------------------------