drop database if exists TANIKO2022;
create database TANIKO2022;
use TANIKO2022;

# DEMANDEUR
create table personne_morale(
    idDmd int auto_increment NOT NULL primary key,
    numTelDmd varchar(255) unique default null,
    emailDmd varchar(255) unique default null,
    adresseDmd text default null,
    nomDmd text NOT NULL,
    typeDmd enum('MORALE_PRIVE','MORALE_PUBLIQUE','PHYSIQUE') default 'PHYSIQUE',
    capital double default null,
    nif varchar(255) default null,
    rcs varchar(255) default null,
    statistique varchar(255) default null,
    nationalite  VARCHAR(255) default null
);

create table personne_physique(
  idDmdPhysique int auto_increment primary key,
  lotDmd      varchar(50)  default null,
  parcelleDmd varchar(50)  default null,
  lieuDeNaissanceDmd varchar(255) default null,
  dateDeNaissanceDmd DATE default null,
  pere varchar(255) default null,
  mere varchar(255) default null,
  sexe enum('Masculin','Féminin') default 'Masculin',
  situationDeFamille enum('célibataire','Marié','Veuve') default 'Célibataire',
  cin varchar(255) default null ,
  lieuDelivrance varchar(255) default null,
  dateDelivrance date default null,
  profession varchar(255) default null,
  CONSTRAINT fk_demandeur_physique foreign key (idDmdPhysique) references personne_morale(idDmd) ON DELETE CASCADE
);

# MARIAGE
create table mariage(
  idDemandeur int not null ,
  idConjoint int not null ,
  dateMariage DATE not null ,
  lieuMariage varchar(255) not null ,
  regime enum('séparation des biens','Droit commun'),
  CONSTRAINT fk_demandeur_mariage foreign key (idDemandeur) references personne_physique(idDmdPhysique) ON DELETE CASCADE,
  CONSTRAINT fk_conjoint_mariage foreign key (idConjoint) references personne_physique(idDmdPhysique) ON DELETE CASCADE,
  constraint pk_mariage primary key (idDemandeur,idConjoint)
);

# UTILISATEUR
create table utilisateur(
  idUtilisateur   int not null primary key,
  nom_utilisateur varchar(255)                        not null,
  mot_de_passe    varchar(255)                        not null,
  photo           longblob     default         null,
  type            enum ('administrateur','standard') default 'standard',
  userStatus int default 0,
  constraint nom_utilisateur unique (nom_utilisateur),
  constraint fk_utilisateur_personne_morale foreign key (idUtilisateur) references personne_morale(idDmd)
);

# PROPRIETE
create table propriete(
idPropriete int auto_increment primary key,
region varchar(255) not null,
district varchar(255) not null,
commune varchar(255) not null,
quartier varchar(255) not null,
parcelle varchar(50) not null,
superficie text not null,
numTitre varchar(255) default null,
dateCreation DATE default null,
numMorcelement varchar(255) default null,
nomPropriete varchar(255) default null,
numDepot varchar(50) default null,
numVolume varchar(50) default null,
limite text default null
);

create table propriete_depend_propriete(
  idTerrainMere int not null ,
  idTerrainDependant int not null,
  CONSTRAINT fk_terrain_depend_titre foreign key (idTerrainMere) references propriete (idPropriete) ON DELETE CASCADE,
  CONSTRAINT fk_titre_depend_terrain foreign key (idTerrainDependant) references propriete (idPropriete) ON DELETE CASCADE
  ,CONSTRAINT pk_propriete_dependant primary key (idTerrainMere,idTerrainDependant)
                                       );

# PROCEDURE
create table `_procedure`(
  idProcedure int not null auto_increment primary key,
  nomProcedure longtext not null,
  # note :
  #  >  ' C ' pour les procedures dans la section conservation
  #  >  ' A ' pour acquisition
  #  >  ' P ' pour prescription acquisition
  typeProcedure enum('A','P','C') not null
);

# DOSSIER
create table dossier(
  idDossier           int auto_increment primary key,
  numeroDossier       varchar(255) default null,
  nature              varchar(100),
  dateFormulation     timestamp  not null,
  observation         enum ('CONNEXE','SANS_EMPIETEMENT','')  default '',
  situation           enum('EN_COURS','SUSPENDU','REJETER','TERMINER','') default 'EN_COURS',
  proprieteId int not null,
  titulaireId int not null,
  constraint fk_proprietekey_dossier foreign key (proprieteId) references propriete (idPropriete) on DELETE CASCADE,
  constraint fk_titulaireKey_dossier foreign key (titulaireId) references personne_morale(idDmd) on DELETE CASCADE
);

# TABLE CONTENANT TOUTES LES DEMANDEURS
create table demandeurDossier(
                                 dossierId int not null,
                                 demandeurId int not null ,
                                 constraint fk_dossier_demandeur foreign key (dossierId) references dossier(idDossier) on delete cascade ,
                                 constraint fk_demandeur_dossier foreign key (demandeurId) references personne_morale(idDmd) on delete cascade ,
                                 constraint pk_demandeurDossier primary key (dossierId,demandeurId)
);

# TABLE CONTENANT TOUT LES DOSSIER CONNEXES
create table dossier_connexe(
    idDossierParent int not null,
    idDossierFils int not null ,
    constraint fk_dossier_connexe_Parent foreign key (idDossierParent) references dossier(idDossier) on delete cascade,
    constraint fk_dossier_connexe_fils foreign key (idDossierFils) references dossier(idDossier) on delete cascade,
    constraint pk_dossierConnexe primary key (idDossierParent,idDossierFils)
);

# TABLES CONTENANT LES PROCEDURES CONCERNANT LE DOSSIER
create table procedure_dossier(
     procedureId int not null,
     dossierId int not null,
     CONSTRAINT fk_prcd_aff_and_prcd foreign key (procedureId) references _procedure(idProcedure) on delete cascade
    ,CONSTRAINT fk_aff_prcd_and_aff foreign key (dossierId) references dossier(idDossier) on delete cascade,
     constraint pk_procedureDossier primary key (procedureId,dossierId)
);

# TABLES CONTENANT LES REPRESENTANT D'UNE PERSONNE MORALE
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
                # INFORMATION DU DOSSIER
                create table informationUtile(
                    idInfo int not null  auto_increment primary key,
                    numInfo varchar(100) not null,
                    dateInfo DATE not null,
                    montantJtr varchar(255) default null,
                    typeInfo enum('jtr','ordonnance','reperage','arrete','decision','autre')
                );

                create table dossier_info(
                    dossierId int not null,
                    infoId int not null,
                    constraint fk_ordonnance_affaire foreign key (dossierId) references dossier(idDossier) on delete cascade,
                    constraint fk_affaire_ordonnance foreign key (infoId) references informationUtile(idInfo) on delete cascade,
                    constraint pk_dossierOrdonnance primary key (dossierId,infoId)
                );

create table procedure_concerner_dossier(
  procedureId int not null,
  dossierId int not null,
  dateDepart DATETIME default null,
  dateArrive DATETIME default null,
  numArrive varchar(10) default  null,
  numDepart varchar(10) default null,
  CONSTRAINT primary key (procedureId,dossierId),
  CONSTRAINT foreign key (procedureId) references `_procedure` (idProcedure) on DELETE CASCADE,
  CONSTRAINT foreign key (dossierId) references dossier(idDossier) on DELETE CASCADE
);

# AVIS SERVICE TECHNIQUE
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
idAvis int auto_increment primary key,
libeleAvis text default null,
origineId int not null,
/*
  TYPE AVIS :
      - Avis ( Inconstructible si terrain < 1 are )
      - InFormation ( SRAT )
*/
nomOrigine text default null,
typeAvis ENUM('CHARGE','INFORMATION','AUTRE') default 'INFORMATION',
constraint personneMorale_charge foreign key (origineId) references personne_morale(idDmd) on delete cascade
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
                    # PRENOTATION

                                create table prenotation(
                                    idPrenotation int auto_increment primary key,
                                    nomPrenotation text not null
                                );

                                create table proprietePrenotation(
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
                                                                    datePrenotation date not null,
                                                                    typePrenotationDossier enum ('INSCRIPTION','RADIATION') default 'INSCRIPTION',
                                                                    CONSTRAINT fk_hypotheque foreign key (proprieteId) references propriete(idPropriete) on delete cascade ,
                                                                    constraint fk_dossier foreign key (dossierId) references dossier(idDossier) on delete cascade
                                                                   ,constraint pk_prenotation_dossier primary key (prenotationId,proprieteId)
                                );

                    # BAIL
                    create table bail(
                      idBail int not null primary key,
                      typeBail enum('SIMPLE','EMPHYTEOTIQUE','AMIABLE') default 'SIMPLE',
                      dureBail varchar(50) not null,
                      montantRedevance varchar(255) not null,
                      personId int not null ,
                      constraint fk_personKey_bail foreign key (personId) references personne_morale(idDmd)
                    );

                    create table bail_propriete(
                        bailId int not null,
                        proprieteId int not null,
                        constraint foreign key (bailId) references bail(idBail) on delete cascade ,
                        constraint pk_bail_dossier primary key (bailId,proprieteId),
                        constraint fk_propriete_bail foreign key (proprieteId) references propriete(idPropriete) on delete cascade
                    );

                    create table dossier_bail(
                            bailId int not null ,
                            proprieteId int not null,
                            dossierId int not null ,
                            dateBail date not null,
                            typBailDossier enum ('INSCRIPTION','RADIATION') default 'INSCRIPTION',
                            constraint dossierkey_dossier_bail foreign key (dossierId) references dossier(idDossier) on delete cascade,
                            constraint proprietekey_dossier_bail foreign key (proprieteId) references propriete(idPropriete) on delete cascade,
                            constraint bailkey_dossier_bail foreign key (bailId) references bail(idBail) on delete cascade,
                            constraint pk_dossier_bail primary key (bailId,proprieteId,dossierId)
                    );

# HYPOTHEQUE
                                create table hypotheque(
                                                               idHypotheque int not null primary key,
                                                               proprieteId int not null,
                                                               dateInscriptionHyp date not null ,
                                                               dateRadiationHyp date default null,
                                                               montantHyp double not null,
                                                               numBorderauHyp varchar(50) default null,
                                                               typeHyp enum ('LEGALE','CONVENTIONNELLE','JUDICIAIRE'),
                                                               constraint fk_hypotheque_propriete  foreign key (proprieteId) references propriete(idPropriete) on delete cascade
                                );
                               # HYPOTHEQUE ET DOSSIER
                                create table dossier_hypotheque(
                                    hypothequeId int not null,
                                    dossierId int not null,
                                    datehyp date not null,
                                    typeDossierHyp enum ('INSCRIPTION','RADIATION') default 'INSCRIPTION',
                                    CONSTRAINT fk_hypotheque foreign key (hypothequeId) references hypotheque(idHypotheque) on delete cascade ,
                                    constraint fk_dossier foreign key (dossierId) references dossier(idDossier) on delete cascade
                                   ,constraint pk_dossier_hypotheque primary key (hypothequeId,dossierId)
                                );
                                # TRANSFERT HYPOTHEQUE
                                create table transfert_hypotheque(
                                    hypothequeId int not null,
                                    pmId int not null,
                                    dateTransfert date not null,
                                    constraint fk_hypothequeKey_transfert foreign key (hypothequeId) references hypotheque(idHypotheque) on delete cascade ,
                                    constraint fk_personneMoraleKey_transfert foreign key (pmId)  references personne_morale(idDmd) on delete cascade
                                    ,constraint pk_transfert_hypotheque primary key (hypothequeId,pmId)
                                );
                                # DOSSIER TRANSFERT HYPOTHEQUE
                                create table dossier_transfert_hypotheque(
                                    hypothequeId int not null,
                                    pmId int not null,
                                    dossierId int not null,
                                    constraint fk_dossierKey_transfert_dossier foreign key (dossierId) references dossier(idDossier) on delete cascade ,
                                    constraint fk_hypothequeKey_transfert_dossier foreign key (hypothequeId) references hypotheque(idHypotheque) on delete cascade ,
                                    constraint fk_personneMoraleKey_transfert_dossier foreign key (pmId)  references personne_morale(idDmd) on delete cascade
                                    ,constraint pk_transfert_hypotheque_dossier primary key (hypothequeId,pmId,dossierId)
                                );
                    # MUTATION
                                create table mutation(
                                                         idMutation int not null primary key ,
                                                         proprieteId int not null,
                                                         montantMutation varchar(100) not null,
                                                         typeMutation varchar(100),
                                                         numBorderauMutation varchar(50) default null,
                                                         numVolumeMutation varchar(50) default null,
                                                         numDepotMutation varchar(50) default null,
                                                         constraint fk_propriete_mutation foreign key (proprieteId) references propriete(idPropriete) on delete cascade
                                );

                                create table dossier_mutation(
                                  mutationId int not null,
                                  dossierId int not null,
                                  constraint fk_mutation_dossierEtmutation foreign key (mutationId) references mutation(idMutation) on delete cascade,
                                  constraint fk_dossier_dossierEtmutation foreign key (dossierId) references dossier(idDossier) on delete cascade
                                );

                    # SERVITUDE DE PASSAGE

                    create table servitude_passage(
                                                      idServitude int not null primary key ,
                                                      dossierId int not null,
                                                      proprieteId int not null,
                                                      valeur varchar(100) not null,
                                                      modifId int not null,
                                                      constraint fk_propriete_servitude foreign key (proprieteId) references propriete(idPropriete) on delete cascade,
                                                      constraint fk_modification_servitude foreign key (modifId) references modification(idModif) on delete cascade
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
# TABLE CONTENANT LE TITULAIRE DE CHAQUE AFFAIRE
create table titulaire_affaire(
    dossierId int not null,
    titulaireId int not null,
    constraint fk_dossier_titulaireDossier foreign key ( titulaireId) references personne_morale(idDmd) on delete cascade ,
    constraint fk_demandeur_titulaireDossier foreign key (dossierId) references dossier(idDossier) on delete cascade ,
    constraint pk_titulaireDossier primary key (titulaireId,dossierId)
);

# PORTEUR DUPLICATA
create table porteur_duplicata(
    personneId int not null,
    proprieteId int not null,
    dateDuplicata date not null,
    constraint fk_personneKey_porteur_duplicata foreign key (personneId) references personne_physique(idDmdPhysique) on delete cascade ,
    constraint fk_proprietekey_porteur_duplicata foreign key (proprieteId) references propriete(idPropriete) on delete cascade ,
    constraint pk_porteur_duplicata primary key (personneId,proprieteId)
);

# MODIFICATION CONSISTANCE
create table modification(
    idModif int not null primary key ,
    typeModif enum('AUGMENTATION','DIMINUTION') default 'AUGMENTATION',
    valeurModif varchar(100) not null,
    dateModif date not null,
    resultatModif varchar(100) not null,
    proprieteId int not null,
    constraint fk_modification foreign key (proprieteId) references propriete(idPropriete) on delete cascade
);

# TERRAIN OPERATION
create table proprieteOperation(
    idProprieteMere int not null,
    idProprieteFils int not null,
    typeOperation enum('FUSION','MORCELEMENT'),
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
