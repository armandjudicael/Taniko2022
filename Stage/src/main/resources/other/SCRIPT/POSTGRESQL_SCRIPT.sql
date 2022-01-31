create database "TANIKO_POSTGRESQL";
/*  CREATION DES TYPE ENUMERER  */
CREATE TYPE modifType as ENUM ('AUGMENTATION','DIMINUTION');
CREATE TYPE personSexe as ENUM ('Masculin','Feminin');
CREATE TYPE matrimonialSituation as ENUM  ('Célibataire','Marié','Veuve');
CREATE TYPE matrimonialRegime as ENUM ('séparation des biens','Droit commun');
CREATE TYPE procedureType as ENUM ('A','P','C','autre');
CREATE TYPE observationType as ENUM ('CONNEXE','SANS_EMPIETEMENT','AUTRE','');
CREATE TYPE folderStatusType AS ENUM ('EN_COURS','SUSPENDU','REJETER','TERMINER','');
CREATE TYPE userType AS ENUM ('administrateur','standard');
CREATE TYPE infoType AS ENUM ('jtr','ordonnance','reperage','arrete','decision','autre','') ;
CREATE TYPE avisType AS ENUM ('CHARGE','INFORMATION','AUTRE');
CREATE TYPE privType AS ENUM ('INSCRIPTION','RADIATION');
CREATE TYPE privilegeName AS ENUM ('LEGALE','CONVENTIONNELLE','JUDICIAIRE','SIMPLE','EMPHYTEOTIQUE','AMIABLE');
CREATE TYPE operationType AS ENUM ('FUSION','MORCELEMENT');
create type servitudeType as ENUM ('CONVENTIONNELE','JUDICIAIRE');
create type privOperation as enum ('TRANSFERT','CESSION');
create type typePrivilege as enum ('HYPOTHEQUE','BAIL');
/* PERSONNE PHYSIQUE */
create table personne_morale(
                                idDmd BIGSERIAL NOT NULL primary key,
                                numTelDmd varchar(255) unique default null,
                                emailDmd varchar(255) unique default null,
                                adresseDmd text default null,
                                nomDmd text NOT NULL,
                                typeDmd VARCHAR(255),
                                capital double precision default null,
                                nif varchar(255) default null,
                                rcs varchar(255) default null,
                                statistique varchar(255) default null,
                                nationalite  VARCHAR(255) default null
);
/* PERSONNE PHYSIQUE */
create table personne_physique(
                                  idDmdPhysique bigserial primary key,
                                  lotDmd      text  default null,
                                  parcelleDmd text  default null,
                                  lieuDeNaissanceDmd text default null,
                                  dateDeNaissanceDmd DATE default null,
                                  pere text default null,
                                  mere text default null,
                                  sexe personSexe default 'Masculin',
                                  situationDeFamille matrimonialSituation default 'Célibataire',
                                  cin varchar(255) default null ,
                                  lieuDelivrance text default null,
                                  dateDelivrance date default null,
                                  profession text default null,
                                  CONSTRAINT fk_demandeur_physique foreign key (idDmdPhysique) references personne_morale(idDmd) ON DELETE CASCADE
);
/* TABLE MARIAGE */
create table mariage(
                        idDemandeur int not null ,
                        idConjoint int not null ,
                        dateMariage DATE not null ,
                        lieuMariage text not null ,
                        regime matrimonialRegime default 'Droit commun',
                        CONSTRAINT fk_demandeur_mariage foreign key (idDemandeur) references personne_physique(idDmdPhysique) ON DELETE CASCADE,
                        CONSTRAINT fk_conjoint_mariage foreign key (idConjoint) references personne_physique(idDmdPhysique) ON DELETE CASCADE,
                        constraint pk_mariage primary key (idDemandeur,idConjoint)
);
/* PROPRIETE */
create table propriete(
                          idPropriete BIGSERIAL primary key NOT NULL ,
                          region text not null,
                          district text not null,
                          commune text not null,
                          quartier text not null,
                          parcelle varchar(100) not null,
                          superficie text not null,
                          numTitre text default null,
                          consistance text default null,
                          dateCreation DATE default null,
                          numMorcelement varchar(100) default null,
                          nomPropriete text default null,
                          numDepot varchar(100) default null,
                          numVolume varchar(100) default null,
                          limite text default null
);

/* PROPRIETE DEPENDANT PROPRIETE (Terrain dependant d'une properiete) */
create table propriete_depend_propriete(
                                           idTerrainMere int not null ,
                                           idTerrainDependant int not null,
                                           CONSTRAINT fk_terrain_depend_titre foreign key (idTerrainMere) references propriete (idPropriete) ON DELETE CASCADE,
                                           CONSTRAINT fk_titre_depend_terrain foreign key (idTerrainDependant) references propriete (idPropriete) ON DELETE CASCADE
    ,CONSTRAINT pk_propriete_dependant primary key (idTerrainMere,idTerrainDependant)
);

/* PROCEDURE CONCERNANT LES AFFAIRES */
create table _procedure(
                           idProcedure serial primary key,
                           nomProcedure TEXT not null,
                           typeProcedure procedureType DEFAULT 'A'
);
/* DOSSIER OU AFFAIRE */
create table dossier(
                        idDossier           bigserial primary key,
                        numeroDossier       varchar(255) default null,
                        nature              varchar(100),
                        dateFormulation     date  not null,
                        observation         observationType  default '',
                        situation           folderStatusType default 'EN_COURS',
                        proprieteId int not null,
                        titulaireId int not null,
                        constraint fk_proprietekey_dossier foreign key (proprieteId) references propriete (idPropriete) on DELETE CASCADE,
                        constraint fk_titulaireKey_dossier foreign key (titulaireId) references personne_morale(idDmd) on DELETE CASCADE
);
/* PROCEDURE CONCERNER AFFAIRE OU DOSSIER */
create table procedure_concerner_dossier(
                                            procedureId int not null,
                                            dossierId int not null,
                                            dateDepart timestamp default null,
                                            dateArrive timestamp default null,
                                            numArrive varchar(10) default  null,
                                            numDepart varchar(10) default null,
                                            CONSTRAINT pk_procedure_dossier primary key (procedureId,dossierId),
                                            CONSTRAINT fk_procedureKey_procedure_folder foreign key (procedureId) references _procedure (idProcedure) on DELETE CASCADE,
                                            CONSTRAINT fk_folderKey_procedure_folder foreign key (dossierId) references dossier(idDossier) on DELETE CASCADE
);

/* TABLE CONTENANT TOUTES LES DEMANDEURS*/
create table demandeur_dossier(
                                  dossierId int not null,
                                  demandeurId int not null ,
                                  constraint fk_dossier_demandeur foreign key (dossierId) references dossier(idDossier) on delete cascade ,
                                  constraint fk_demandeur_dossier foreign key (demandeurId) references personne_morale(idDmd) on delete cascade ,
                                  constraint pk_demandeurDossier primary key (dossierId,demandeurId)
);
/* UTILISATEUR */
create table utilisateur(
                            idUtilisateur   int not null primary key,
                            nom_utilisateur varchar(255)                        not null,
                            mot_de_passe    varchar(255)                        not null,
                            photo           bytea   default null,
                            type            userType default 'standard',
                            userStatus int default 0,
                            constraint nom_utilisateur unique (nom_utilisateur),
                            constraint fk_utilisateur_personne_morale foreign key (idUtilisateur) references personne_morale(idDmd)
);
/* TABLE DISPATCH */
create table dispatch(
                         utilisateurId int  not null,
                         dossierId     int  not null,
                         dateDispatch date not null,
                         CONSTRAINT pk_dispatch primary key (utilisateurId,dossierId,dateDispatch),
                         CONSTRAINT fk_userkey_dispatch foreign key (utilisateurId) references utilisateur (idUtilisateur) on DELETE CASCADE,
                         CONSTRAINT fk_dossierkey_dispach foreign key (dossierId) references dossier(idDossier) on DELETE CASCADE
);
/* PIECE JOINTE */
create table attachement(
                            idPieceJointe bigserial primary key,
                            descriptionPiece varchar(255) not null,
                            valeurPiece bytea not null,
                            extensionPiece varchar(50) not null,
                            taillePiece varchar(50) not null ,
                            dossierId int not null,
    /*
       0 : INCONNU
       1 : VALIDE
       2 : INVALIDE
     */
                            isValide boolean default false,
                            CONSTRAINT fk_dossier_piece_jointe FOREIGN KEY  (dossierId) references dossier(idDossier) on delete cascade
);
/* COMMENTAIRE SUR LE PIECE JOINTE */
create table attach_comment(
                               idComment int not null primary key,
                               libelle text default null,
                               pieceId int not null,
                               constraint fk_pieceKey_comment foreign key(pieceId) references attachement(idPieceJointe) on delete cascade
);
/*TABLE CONTENANT TOUT LES DOSSIER CONNEXES*/
create table dossier_connexe(
                                idDossierParent int not null,
                                idDossierFils int not null ,
                                constraint fk_dossier_connexe_Parent foreign key (idDossierParent) references dossier(idDossier) on delete cascade,
                                constraint fk_dossier_connexe_fils foreign key (idDossierFils) references dossier(idDossier) on delete cascade,
                                constraint pk_dossierConnexe primary key (idDossierParent,idDossierFils)
);

/*TABLES CONTENANT LES PROCEDURES CONCERNANT LE DOSSIER*/
create table procedure_dossier(
                                 procedureId int not null,
                                 dossierId int not null,
                                 CONSTRAINT fk_prcd_aff_and_prcd foreign key (procedureId) references _procedure(idProcedure) on delete cascade
    ,CONSTRAINT fk_aff_prcd_and_aff foreign key (dossierId) references dossier(idDossier) on delete cascade,
                                 constraint pk_procedureDossier primary key (procedureId,dossierId)
);
/* TABLES CONTENANT LES REPRESENTANT D'UNE PERSONNE MORALE*/
create table representant_personne_morale(
                                             idPersonneMorale int not null,
                                             idPersonnePhysique int not null,
                                             dossierId int not null,
                                             numeroProcuration varchar(100) default null,
                                             dateProcuration date default  null,
                                             constraint fk_representant_morale foreign key (idPersonneMorale) references personne_morale(idDmd) on delete cascade ,
                                             constraint fk_representant_physique foreign key (idPersonnePhysique) references personne_physique(idDmdPhysique) on delete cascade,
                                             constraint fk_dossier_representant foreign key(dossierId) references dossier(idDossier) on delete cascade,
                                             constraint pk_representant_personne_morale primary key (idPersonneMorale,idPersonnePhysique,dossierId)
);
/* INFORMATION DU DOSSIER */
create table informationUtile(
                                 idInfo BIGSERIAL primary key,
                                 numInfo varchar(100) not null,
                                 dateInfo DATE not null,
                                 montantJtr DOUBLE PRECISION default null,
                                 typeInfo infoType default 'jtr'
);
create table dossier_info(
                             dossierId int not null,
                             infoId int not null,
                             constraint fk_ordonnance_affaire foreign key (dossierId) references dossier(idDossier) on delete cascade,
                             constraint fk_affaire_ordonnance foreign key (infoId) references informationUtile(idInfo) on delete cascade,
                             constraint pk_dossierOrdonnance primary key (dossierId,infoId)
);
/* AVIS SERVICE TECHNIQUE */
/*
  Les origine des charges sont multiple et depend de la nature de l'emplacement du propriete . Par exemple :
   - SRAT ( Service de l'ammenagement du territoire ) pour les propriete urbains
   - APIPA ( Autorité pour la Protection contre les Inondatios de la Plaine d'Antananarivo )
   - BPPA
   - MADARAILS
   - TOURISMES
   - Min agirculture
*/
create table avis(
                     idAvis bigserial primary key,
                     libeleAvis text default null,
/*
  TYPE AVIS :
      - Avis ( Inconstructible si terrain < 1 are )
      - InFormation ( SRAT )
*/
                     nomOrigine text default null,
                     typeAvis  avisType  default 'INFORMATION'
);
create table avis_origine(
    avisId int not null,
    origineId int not null,
    constraint fk_origineKey_origine_avis foreign key (origineId) references personne_morale(idDmd) on delete cascade,
    constraint fk_avisKey_origine_avis foreign key (avisId) references avis(idAvis) on delete cascade,
    constraint pk_avis_origine primary key (avisId,origineId)
);
create table avisDossier(
                            avisId int not null,
                            proprieteId int not null ,
                            dateAvis date default null,
                            numAvis varchar(255) not null,
                            reserveEmprise varchar(50)  default null,
                            constraint fk_charge_propriete foreign key (avisId) references avis(idAvis),
                            constraint fk_propriete_charge foreign key (proprieteId) references propriete(idPropriete),
                            constraint pk_avisDossier primary key (avisId,proprieteId)
);
/*
    DEBUT SECTION CONSERVATION
*/
/* PRENOTATION */
create table prenotation(
                            idPrenotation bigserial primary key,
                            nomPrenotation text not null
);
create table propriete_prenotation(
                                     prenotationId int not null,
                                     proprieteId int not null,
                                     constraint fk_propriete_prenotation foreign key (prenotationId) references prenotation(idPrenotation),
                                     constraint fk_prenotation_propriete foreign key (proprieteId) references propriete(idPropriete),
                                     constraint pk_proprietePrenotation primary key (prenotationId,proprieteId)
);
create table prenotation_dossier(
                                    prenotationId int not null,
                                    proprieteId int not null,
                                    dossierId int not null,
                                    date date not null,
                                    typePrenotationDossier privType  default 'INSCRIPTION',
                                    CONSTRAINT fk_proprietekey_prenotation_dossier foreign key (proprieteId) references propriete(idPropriete) on delete cascade ,
                                    constraint fk_folderkey_prenotation_dossier foreign key (dossierId) references dossier(idDossier) on delete cascade,
                                    constraint fk_prenotationkey_prenotation_dossier foreign key (prenotationId) references prenotation(idPrenotation) on delete cascade
                                   ,constraint pk_prenotation_dossier primary key (prenotationId,proprieteId)
);
create table prenotation_personne(
                                    personneId int not null,
                                    prenotationId int not null,
                                    CONSTRAINT fk_personneKey_prenotation_personne foreign key (personneId) references personne_morale(idDmd) on delete cascade ,
                                    constraint fk_prenotationKey_prenotation_personne foreign key (prenotationId) references prenotation(idPrenotation) on delete cascade
                                   ,constraint pk_prenotation_personne primary key (prenotationId,personneId)
);
/* HYPOTHEQUE ET BAIL */
create table privilege(
                           idPrivilege bigserial primary key,
                           proprieteId int not null,
                           montantPriv double precision not null,
                           numBorderauPriv varchar(50) default null,
                           numDepotPriv varchar(50) default null,
                           numVolumePriv varchar(50) default null,
                           typePriv   typePrivilege default 'HYPOTHEQUE',
                           dureBail   varchar(50) default null,
                           privNom privilegeName default 'SIMPLE',
                           constraint fk_proprieteKey_privilege  foreign key (proprieteId) references propriete(idPropriete) on delete cascade
);
/*  Personne concerné par le privilege,Bail ou hypotheque */
create table privilege_personne(
    personId int not null,
    privilegeId int not null
    ,constraint fk_personKey_privilege foreign key (personId) references personne_morale(idDmd)
    ,constraint fk_privilegeKey_priv_personne foreign key (privilegeId) references privilege(idPrivilege)
    ,constraint  pk_privilege_personne primary key (personId,privilegeId)
);
/*PRIVILEGE ET DOSSIER */
create table dossier_privilege(
                                   privilegeId int not null,
                                   dossierId int not null,
                                   datePriv date not null,
                                   typeDossierPriv privType default 'INSCRIPTION',
                                   CONSTRAINT fk_hypkey_dossierPriv foreign key (privilegeId) references privilege(idPrivilege) on delete cascade ,
                                   constraint fk_dkey_dossierPriv foreign key (dossierId) references dossier(idDossier) on delete cascade
    ,constraint pk_dossier_hypotheque primary key (privilegeId,dossierId)
);
/* TRANSFERT HYPOTHEQUE ET CESSION BAIL */
create table privilege_operation(
                                     privilegeId int not null,
                                     pmId int not null,
                                     dateOperation date not null,
                                     typeOperation privOperation default 'TRANSFERT',
                                     constraint fk_privKey_privOperation foreign key (privilegeId) references privilege(idPrivilege) on delete cascade ,
                                     constraint fk_personneMoraleKey_privOperation foreign key (pmId)  references personne_morale(idDmd) on delete cascade
    ,constraint pk_privOperation primary key (privilegeId,pmId)
);
/* DOSSIER PRIVILEGE HYPOTHEQUE */
create table dossier_privilege_operation(
                                             privilegeId int not null,
                                             pmId int not null,
                                             dossierId int not null,
                                             constraint fk_dossierKey_transfert_dossier foreign key (dossierId) references dossier(idDossier) on delete cascade ,
                                             constraint fk_hypothequeKey_transfert_dossier foreign key (privilegeId) references privilege(idPrivilege) on delete cascade ,
                                             constraint fk_personneMoraleKey_transfert_dossier foreign key (pmId)  references personne_morale(idDmd) on delete cascade
    ,constraint pk_transfert_hypotheque_dossier primary key (privilegeId,pmId,dossierId)
);
/* MUTATION */
create table mutation(
                         idMutation bigserial primary key ,
                         proprieteId int not null,
                         montantMutation varchar(100) default null,
                         typeMutation varchar(100) not null,
                         numBorderauMutation varchar(50) default null,
                         numVolumeMutation varchar(50) default null,
                         numDepotMutation varchar(50) default null,
                         constraint fk_propriete_mutation foreign key (proprieteId) references propriete(idPropriete) on delete cascade
);
create table mutation_person(
    mutationId int not null ,
    personId int not null ,
    constraint fk_personKey_mutation_person foreign key (personId) references personne_morale(idDmd),
    constraint fk_mutationKey_mutation_person foreign key (personId) references personne_morale(idDmd)
    ,constraint pk_mutation_person primary key (mutationId,personId)
);
create table dossier_mutation(
                                 mutationId int not null,
                                 dossierId int not null,
                                 constraint fk_mutation_dossierEtmutation foreign key (mutationId) references mutation(idMutation) on delete cascade,
                                 constraint fk_dossier_dossierEtmutation foreign key (dossierId) references dossier(idDossier) on delete cascade,
                                 constraint pk_dossier_mutation primary key (mutationId,dossierId)
);
/* MODIFICATION CONSISTANCE */
create table modification(
                             idModif int not null primary key ,
                             typeModif modifType default 'AUGMENTATION',
                             valeurModif varchar(100) not null,
                             dateModif date not null,
                             resultatModif varchar(100) not null,
                             proprieteId int not null,
                             constraint fk_modification foreign key (proprieteId) references propriete(idPropriete) on delete cascade
);
/* SERVITUDE DE PASSAGE */
create table servitude_passage(
                                  idServitude bigserial primary key ,
                                  typeServitude servitudeType default 'CONVENTIONNELE',
                                  proprieteId int not null,
                                  valeur varchar(100) not null,
                                  constraint fk_propriete_servitude foreign key (proprieteId) references propriete(idPropriete) on delete cascade
);
create table servitude_modif(
    servitudeId int not null,
    modifId int not null,
    constraint fk_modificationKey_servitude_modif foreign key (modifId) references modification(idModif) on delete cascade,
    constraint fk_servKey_servitude_modif foreign key (servitudeId) references servitude_passage(idServitude) on delete cascade,
    constraint pk_servitude_modif primary key (servitudeId,modifId)
);
create table dossier_servitude_passage(
                                          servitudeId int not null ,
                                          dossierId int not null ,
                                          constraint fk_dossierKey_servitude foreign key (dossierId) references dossier(idDossier) on delete cascade,
                                          constraint fk_servitudeKey_servitude foreign key (servitudeId) references servitude_passage(idServitude) on delete cascade,
                                          constraint pk_dossier_servitude primary key (servitudeId,dossierId)
);
/*
    FIN CONSERVATION
*/
/* TABLE CONTENANT LE TITULAIRE DE CHAQUE AFFAIRE */
create table titulaire_dossier(
                                  dossierId int not null,
                                  titulaireId int not null,
                                  constraint fk_dossier_titulaireDossier foreign key ( titulaireId) references personne_morale(idDmd) on delete cascade ,
                                  constraint fk_demandeur_titulaireDossier foreign key (dossierId) references dossier(idDossier) on delete cascade ,
                                  constraint pk_titulaireDossier primary key (titulaireId,dossierId)
);
/* PORTEUR DUPLICATA*/
create table porteur_duplicata(
                                  personneId int not null,
                                  proprieteId int not null,
                                  dateDuplicata date not null,
                                  constraint fk_dossier_titulaireDossier foreign key (personneId) references personne_physique(idDmdPhysique) on delete cascade ,
                                  constraint fk_demandeur_titulaireDossier foreign key (proprieteId) references propriete(idPropriete) on delete cascade ,
                                  constraint pk_porteur_duplicata primary key (personneId,proprieteId)
);
/* TERRAIN OPERATION */
create table propriete_operation(
                                   idProprieteMere int not null,
                                   idProprieteFils int not null,
                                   typeOperation operationType default 'MORCELEMENT',
                                   dateOperation date not null,
                                   idModifMere int not null,
                                   idModifFils int not null,
                                   dossierId int not null,
                                   constraint fk_dossier_operation foreign key (dossierId) references dossier(idDossier) on delete cascade,
                                   constraint fk_modif_mere foreign key (idModifMere) references modification(idModif) on delete cascade,
                                   constraint fk_modif_fils foreign key (idModifFils) references modification(idModif) on delete cascade,
                                   constraint fk_proprieteOperationMere foreign key (idProprieteMere) references propriete(idPropriete) on delete cascade,
                                   constraint fk_proprieteOperationFils foreign key (idProprieteFils) references propriete(idPropriete) on delete cascade,
                                   constraint pk_proprieteOperation primary key (idProprieteFils,idProprieteMere)
);