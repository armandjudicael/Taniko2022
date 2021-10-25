drop database if exists TANIKO_1_1;
create database TANIKO_1_1;
use TANIKO_1_1;

create table demandeur_morale(
    idDmd int auto_increment NOT NULL primary key,
    numTelDmd varchar(255) default null,
    emailDmd varchar(255) default null,
    adresseDmd text default null,
    nomDmd text NOT NULL,
    typeDmd enum('morale','physique') default 'physique',
    nationalite  VARCHAR(255) not null
);

create table demandeur_physique(
  idDmdPhysique int auto_increment primary key,
  prenomDmd   varchar(255) default null,
  lotDmd      varchar(50)  default null,
  parcelleDmd varchar(50)  default null,
  lieuDeNaissanceDmd varchar(255) default null,
  dateDeNaissanceDmd timestamp default null,
  pere varchar(255) default null,
  mere varchar(255) default null,
  sexe enum('Masculin','Féminin') default 'Masculin',
  situationDeFamille enum('célibataire','Marié','Veuve') default 'Célibataire',
  CONSTRAINT fk_demandeur_physique foreign key (idDmdPhysique) references demandeur_physique(idDmdPhysique) ON DELETE CASCADE
);

create table mariage(
  idHomme int not null ,
  idFemme int not null ,
  dateMariage timestamp not null ,
  lieuMariage varchar(255) not null ,
  regime enum('séparation des biens','Droit commun'),
  CONSTRAINT fk_homme_mariage foreign key (idHomme) references demandeur_physique(idDmdPhysique) ON DELETE CASCADE,
  CONSTRAINT fk_femme_mariage foreign key (idFemme) references demandeur_physique(idDmdPhysique) ON DELETE CASCADE
);

create table utilisateur(
  idUtilisateur   int auto_increment primary key,
  nom_utilisateur varchar(255)                        not null,
  mot_de_passe    varchar(255)                        not null,
  nom             varchar(255)                        not null,
  prenom          varchar(255)                        not null,
  photo           longblob     default         null,
  type            enum ('administrateur','standard') default 'standard',
  fonction        text   default      null,
  userStatus int default 0,
  constraint nom_utilisateur unique (nom_utilisateur)
);

create table terrain(
idTerrain int auto_increment primary key,
region varchar(255) not null,
district varchar(255) not null,
commune varchar(255) not null,
quartier varchar(255) not null,
parcelle varchar(50) not null,
superficie text not null
);

create table titre(
  idTitre int not null auto_increment primary key,
  numTitre varchar(255) not null,
  dateCreation timestamp not null ,
  numMorcelement varchar(255) not null
);

create table terrain_aboutir_titre(
  titreId int not null,
  terrainId int not null,
  CONSTRAINT fk_terrain_aboutir_titre foreign key (terrainId) references terrain(idTerrain) ON DELETE CASCADE ,
  CONSTRAINT fk_titre_aboutir_terrain foreign key (titreId) references titre(idTitre) ON DELETE CASCADE
);

create table terrain_depend_titre(
  titreId int not null ,
  terrainId int not null,
  CONSTRAINT fk_terrain_depend_titre foreign key (terrainId) references terrain(idTerrain) ON DELETE CASCADE,
  CONSTRAINT fk_titre_depend_terrain foreign key (titreId) references titre(idTitre) ON DELETE CASCADE
);

create table `_procedure` (
  idProcedure int not null auto_increment primary key,
  nomProcedure longtext not null,
  typeProcedure enum('A','P') not null
);

create table affaire(
  idAffaire           int auto_increment primary key,
  numAffaire          varchar(255)                                             not null,
  typeAffaire         enum('ACQUISITION','PRESCRIPTION_ACQUISITIVE','AFFECTATION') not null ,
  dateFormulation     timestamp  not null,
  observation         enum ('CONNEXE','SANS_EMPIETEMENT')  default 'SANS_EMPIETEMENT'         ,
  situation           ENUM('EN_COURS','SUSPENDU','REJETER','TERMINER') default 'EN_COURS',
  description         text default null,
  demandeurId         int  not null,
  terrainId int not null,
  constraint numero_affaire unique (numAffaire),
  constraint fk_affaire_et_demandeur foreign key (demandeurId) references demandeur_morale(idDmd),
  constraint fk_affaire_et_terrain foreign key (terrainId) references terrain(idTerrain) on DELETE CASCADE
);

create table ordonnance(
    idOrdonnance int not null auto_increment primary key,
    numOrdonnance varchar(100) not null ,
    dateOdronnance timestamp not null ,
    affaireId int not null ,
    constraint fk_ordonnance_affaire foreign key (affaireId) references affaire(idAffaire)
);

create table reperage(
    idReperage int not null  auto_increment primary key ,
    numReperage varchar(100) not null ,
    dateReperage timestamp not null ,
    affaireId int not null ,
    constraint fk_reperage_affaire foreign key (affaireId) references affaire(idAffaire)
);

create table journal_de_tresorerie(
    idJtr int not null auto_increment primary key,
    numJtr varchar(100) not null ,
    dateJtr timestamp not null ,
    affaireId int not null ,
    constraint fk_journal_de_tresorerie foreign key (affaireId) references affaire(idAffaire)
);

create table procedure_concerner_affaire(
  procedureId int not null,
  affaireId int not null,
  dateDepart timestamp default '1999-07-30',
  dateArrive timestamp default '1999-07-30',
  numArrive varchar(10) default '',
  numDepart varchar(10) default '',
  CONSTRAINT primary key (procedureId,affaireId),
  CONSTRAINT foreign key (procedureId) references `_procedure` (idProcedure) on DELETE CASCADE,
  CONSTRAINT foreign key (affaireId) references affaire(idAffaire) on DELETE CASCADE
);

create table affaire_procedure(
  procedureId int not null,
  affaireId int not null,
  CONSTRAINT foreign key (procedureId) references `_procedure` (idProcedure) on DELETE CASCADE,
  CONSTRAINT foreign key (affaireId) references affaire(idAffaire) on DELETE CASCADE
);

create table dispatch(
  utilisateurId int  not null,
  affaireId     int  not null,
  dateDispatch timestamp not null,
  CONSTRAINT primary key (utilisateurId,affaireId,dateDispatch),
  CONSTRAINT foreign key (utilisateurId) references utilisateur (idUtilisateur) on DELETE CASCADE,
  CONSTRAINT foreign key (affaireId) references affaire(idAffaire) on DELETE CASCADE
);

create table pieceJointe(
  idPiece int not null primary key,
  descriptionPiece varchar(255) not null,
  valeurPiece longblob not null,
  extensionPiece varchar(50) not null,
  affaireId int not null,
  CONSTRAINT fk_affaire_piece_jointe FOREIGN KEY  (affaireId) references affaire(idAffaire) on delete cascade
);