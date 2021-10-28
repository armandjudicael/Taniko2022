package controller.formController.other;

import Model.Enum.*;
import View.Model.ViewObject.AffaireForView;
import View.Model.ViewObject.ProcedureForTableview;
import com.jfoenix.controls.JFXComboBox;
import controller.viewController.AffairViewController;
import dao.DaoFactory;
import dao.DbOperation;
import Model.Other.MainService;
import Model.Pojo.*;
import View.Dialog.Other.Notification;
import View.Helper.Attachement.AttachementCheckerButton;
import View.Helper.Attachement.AttachementCreatorButton;
import View.Helper.Attachement.AttachementRemoverButton;
import View.Model.ViewObject.ConnexAffairForView;
import com.jfoenix.controls.JFXButton;
import controller.formController.demandeurController.MainDemandeurFormController;
import controller.formController.demandeurController.NouveauDemandeurController;
import controller.formController.demandeurController.RechercherDemandeurController;
import controller.viewController.LoginController;
import dao.DemandeurMoraleDao;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.TilePane;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static Model.Pojo.Affaire.string2TypeDemande;
import static dao.DemandeurMoraleDao.*;

public class PieceJointeFormController implements Initializable{

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        initBtnAction();
    }

    private void initBtnAction(){
        ObservableList<Node> children = pjTilepane.getChildren();
        new AttachementCreatorButton(newPieceBtn,children,false);
        new AttachementRemoverButton(delPieceBtn,children,false);
        new AttachementCheckerButton(checkBtn,children);
        delPieceBtn.disableProperty().bind(Bindings.isEmpty(children));
        checkBtn.disableProperty().bind(Bindings.isEmpty(children));
        pjPrevBtn.setOnAction(event -> MainAffaireFormController.updateLabelAndShowPane(pjPrevBtn));
        saveBtn.setOnAction(event ->{
            MainService.getInstance().launch(new Task<Void>() {
                @Override protected Void call() throws Exception{
                    saveAffaire();
                    return null;
                }
            });
        });

    }

    private void saveAllAttachement(Affaire affaire, ObservableList<Node> children) {
        int[] all = DaoFactory.getPieceJointeDao().createAll(AttachementCreatorButton.getAttachementList(), affaire);
        if (children.size() == all.length){
                String message = " Piece jointe enregistré avdec succès ";
                Notification.getInstance(message, NotifType.SUCCESS).showNotif();
        }else {
            String message = " Echec de l'enregistrement des pieces jointes ";
            Notification.getInstance(message,NotifType.WARNING).showNotif();
        }
    }

    private void saveAffaire(){
            PersonneMorale personneMorale = initDemandeur();
            Timestamp dateFormulation = Timestamp.valueOf(LocalDateTime.of(affaireFormInstance.getDateFormulation().getValue(), LocalTime.now()));
            String numeroAffaire = affaireFormInstance.getNumeroAffaire().getText()+affaireFormInstance.getNumAndDateAffLabel().getText();
            User redacteur = initRedateur();
            Terrain terrain = initTerrain();
            AffaireForView affairForView = new AffaireForView(numeroAffaire,dateFormulation,string2TypeDemande(affaireFormInstance.getTypeDemande().getValue()), redacteur, AffaireStatus.RUNNING, personneMorale, terrain, new ProcedureForTableview(ProcedureStatus.NONE,new SimpleStringProperty("Auccune procedure")));
            // ENREGISTRER AFFAIRE
            if (DaoFactory.getAffaireDao().create(affairForView) != 0){
                int idAffaire = DaoFactory.getAffaireDao().getAffaireIdByNum(affairForView.getNumero());
                affairForView.setId(idAffaire);
                Notification.getInstance(" L'affaire N° "+ numeroAffaire +" est enregistrer avec succès ", NotifType.SUCCESS).showNotif();
                // MIS A JOUR DE L'AFFICHAGE
                Platform.runLater(() -> AffairViewController.getInstance().getTableView().getItems().add(affairForView));
                // INSERTION DANS LA TABLE DE DISPATCH
                if (redacteur!=null) DbOperation.insertOnDispatchTable(redacteur,affairForView);
                // ENREGISTREMENT DES PIECES JOINTES
                if (!pjTilepane.getChildren().isEmpty()) saveAllAttachement(affairForView,pjTilepane.getChildren());
            }
    }

    private PersonneMorale initDemandeur(){
        Tab searchTab = mainDemandeurFormInstance.getSearchTab();
        Tab newTab = mainDemandeurFormInstance.getNewTab();
        ToggleButton moraleToogle = mainDemandeurFormInstance.getPersonneMorale();
        if (newTab.isSelected()){
            if (moraleToogle.isSelected()) return initDemandeurMorale();
            else return initDemandeurPhysique();
        }else if (searchTab.isSelected()) return  searchDemandeurFormInstance.getDemandeurCombobox().getValue();
        return null;
    }

    private PersonneMorale initDemandeurMorale() {
        PersonneMorale dm = createDemandeurMorale();
        PersonnePhysique rep = createRepresentant();
        // INSERTION DU DEMANDEUR MORALE
        if (DaoFactory.getDemandeurMoraleDao().create(dm) == 1){
            // RECUPERATION DE L'IDENTIFIANT DU PERSONNE MORALE
            dm.setId(getLasInsertId(dm));
            // INSERTION DU REPRESENTANT
            if (DaoFactory.getDemandeurMoraleDao().create(rep) == 1){
                // RECUPERATION DE L'IDENTIFIANT DU REPRESENTANT
                rep.setId(getLasInsertId(rep));
                // INSERTION DANS LA TABLE REPRESENTANT
                if (DbOperation.insertOnTableRepresentant(rep,dm) == 1){
                   return dm;
                }
            }
        }
        return dm;
    }

    private PersonnePhysique initDemandeurPhysique(){
        PersonnePhysique ph = createDemandeurPhysique();
        /*
        * ON FAIT DEUX INSERTION POUR LES PRESONNES PHYSIQUE CAR ON A UNE RELATION D'HERITAGE
        * - UNE DANS LA TABLE " personnne morale " pour enregister le nom , adresse
        * - UNE DANS LA TABLE " personne physique " pour enregister le situation matrimoniale ,n ect
        * */
        // INSERTION DEs DONNES DANS LA TABLE PERSONNE MORALE
        if (DaoFactory.getDemandeurMoraleDao().create(ph) == 1){
            // RECUPERATION DE LA L'IDENTIFIANT DU DEMANDEUR INSERER
            ph.setId(getLasInsertId(ph));
            // INSERTION DANS LA TABLE PERSONNE PHYSIQUE
            DaoFactory.getDemandeurPhysiqueDao().create(ph);
        }
        if(newDemandeurFormInstance.getMarie().isSelected()){
            PersonnePhysique conjoint = createConjoint();
            // INSERTION DEs DONNES DANS LA TABLE PERSONNE MORALE
            if (DaoFactory.getDemandeurMoraleDao().create(ph) == 1){
                // RECUPERATION DE LA L'IDENTIFIANT DU DEMANDEUR INSERER
                conjoint.setId(getLasInsertId(conjoint));
                // INSERTION DANS LA TABLE PERSONNE PHYSIQUE
                if (DaoFactory.getDemandeurPhysiqueDao().create(ph) == 1){
                    // MARIAGE
                    Mariage mariage = createMariage();
                    mariage.setDemandeur(ph);
                    mariage.setConjoint(conjoint);
                    // INSERTION DANS LA TABLE MARIAGE
                     if (DbOperation.insertOnTableMariage(mariage) == 1) return ph;
                }
            }
        }
        return ph;
    }

    private int getLasInsertId(PersonneMorale dp){
        if (dp.getNumTel().isBlank() && dp.getNumTel()!=null) return DaoFactory.getDemandeurMoraleDao().getDemandeurIdBy(dp.getNumTel(),ParamerterType.NUMTEL);
        else if (dp.getEmail().isBlank() && dp.getEmail()!=null) return DaoFactory.getDemandeurMoraleDao().getDemandeurIdBy(dp.getEmail(),ParamerterType.EMAIL);;
        return DaoFactory.getDemandeurMoraleDao().getDemandeurIdBy(dp.getNom(),ParamerterType.NAME);
    }

    private PersonnePhysique createDemandeurPhysique(){
        String name = newDemandeurFormInstance.getNomDmd().getText();
        String firstName = newDemandeurFormInstance.getPrenomDmd().getText()!=null ? newDemandeurFormInstance.getPrenomDmd().getText() : "";
        Timestamp dateNaissance = Timestamp.valueOf(LocalDateTime.of(newDemandeurFormInstance.getDateDeNaissanceDmd().getValue(),null));
        String lieuDeNaissance = newDemandeurFormInstance.getLieuDeNaissanceDmd().getText();
        String sexe = initSex(true);
        String adresse = newDemandeurFormInstance.getAdresse().getText();
        String parcelle = newDemandeurFormInstance.getDemandeurParcelle1()+" / "+newDemandeurFormInstance.getDemandeurParcelle2();
        String lot = newDemandeurFormInstance.getLot().getText();
        String profession = newDemandeurFormInstance.getProffession().getText();
        String email = newDemandeurFormInstance.getEmail().getText();
        String numTel = newDemandeurFormInstance.getTelephone().getText();
        String situationMatrimoniale = initSituationMatrimoniale();
        String nationalite = newDemandeurFormInstance.getNationalite().getText();
        String nomDuPere = newDemandeurFormInstance.getNomPere().getText();
        String nomDuMere = newDemandeurFormInstance.getNomMere().getText();
        return new PersonnePhysique(name+firstName,adresse,numTel,email,TypeDemandeur.PERSONNE_PHYSIQUE,nationalite,parcelle,lot,dateNaissance,lieuDeNaissance,sexe,nomDuPere,nomDuMere,situationMatrimoniale,profession);
    }

    private String initSex(Boolean isApplicant){
        Toggle selectedToggle = newDemandeurFormInstance.getSexe().getSelectedToggle();
        boolean equals = selectedToggle.equals(newDemandeurFormInstance.getMasculin());
        if (isApplicant){
            if (equals) return  "Masculin";
            else return  "Féminin";
        }else {
            if (!equals) return  "Masculin";
            else return  "Féminin";
        }
    }

    private String initSituationMatrimoniale(){
        if (newDemandeurFormInstance.getMarie().isSelected()){
            createMariage();
           return "Marié";
        }
        else if (newDemandeurFormInstance.getCelibataire().isSelected()) return "célibataire";
        else return "veuve";
    }

    private PersonnePhysique createConjoint(){
        // CONJOINT
        String nomConjoint = newDemandeurFormInstance.getNomConjoint().getText();
        String prenomConjoint = newDemandeurFormInstance.getPrenomConjoint().getText();
        String lieuDeNaissanceConjoint = newDemandeurFormInstance.getLieuDeNaissanceConjoint().getText();
        LocalDate value = newDemandeurFormInstance.getDateNaissanceConjoint().getValue();
        Timestamp dateNaissanceConjoint = Timestamp.valueOf(LocalDateTime.of(value, null));
        return new PersonnePhysique(nomConjoint+prenomConjoint, "", "", "",TypeDemandeur.PERSONNE_PHYSIQUE, "", "", "", dateNaissanceConjoint, lieuDeNaissanceConjoint, initSex(false), "", "", "Marié","");
    }

    private Mariage createMariage(){
        // MARIAGE
        String lieuMariage = newDemandeurFormInstance.getLieuMariage().getText();
        LocalDate value1 = newDemandeurFormInstance.getDateMariage().getValue();
        Timestamp dateMariage = Timestamp.valueOf(LocalDateTime.of(value1,null));
        String regimeMatrimoniale = newDemandeurFormInstance.getRegimeMatrimoniale().getValue();
        return new Mariage(dateMariage,lieuMariage,Mariage.String2RegimeMatrimoniale(regimeMatrimoniale));
    }

    private PersonneMorale createDemandeurMorale(){
        String raisonSocial = newDemandeurFormInstance.getRaisonSocial().getText();
        String siegeSocial = newDemandeurFormInstance.getSiegeSocial().getText();
        String telephone = newDemandeurFormInstance.getTelPersonneMorale().getText();
        String email = newDemandeurFormInstance.getEmailPersonneMorale().getText();
        String nationalite = newDemandeurFormInstance.getNationnalitéMorale().getText();
        return new PersonneMorale(siegeSocial,telephone,email,raisonSocial,TypeDemandeur.PERSONNE_MORALE,nationalite);
    }

    private PersonnePhysique createRepresentant(){
        String nomRepresentant = newDemandeurFormInstance.getNomRepresentant().getText();
        String prenomRepresentant = newDemandeurFormInstance.getPrenomRepresentant().getText();
        String professionRepresentant = newDemandeurFormInstance.getProffession().getText();
        String adresseRepresentant = newDemandeurFormInstance.getAdresseRepresentant().getText();
        PersonnePhysique ps = new PersonnePhysique();
        ps.setNom(nomRepresentant+prenomRepresentant);
        ps.setProfession(professionRepresentant);
        ps.setAdresse(adresseRepresentant);
        return ps;
    }

    private User initRedateur(){
        if (affaireFormInstance.getMoiMemeRadio().isSelected()){
            return LoginController.getConnectedUser();
        }else if (affaireFormInstance.getRechercherRedacteurRadio().isSelected()){
            User value = affaireFormInstance.getRedactorCombobox().getValue();
            if (value!=null)
                return value;
        }
        return null;
    }

    public void resetAffairForm(){
//        this.typeDemande.setValue("ACQUISITION");
//        this.affairePanel.toFront();
//        this.numero.clear();
//        this.dateFormulation.setValue(LocalDate.now());
//        this.sise.clear();
//        this.parcelleTerrain.clear();
//        this.parcelleTerrain1.clear();
//        this.hectare.clear();
//        this.are.clear();
//        this.centiAre.clear();
//        this.demandeurParcelle2.clear();
//        this.demandeurParcelle1.clear();
//        this.nom.clear();
//        this.prenom.clear();
//        this.adresse.clear();
//        this.lot.clear();
//
//        if (insererPropriete.isSelected()){
//            nomPropriete.clear();
//            numeroTitre.clear();
//            numeroMorcellement.clear();
//            dateCreation.setValue(LocalDate.now());
//        }else if (rechercherPropriete.isSelected()){
//            titleCombobox.setValue(null);
//        }
    }

    private Titre initTitre(){
        if (terrainFormInstance.getRadioDependant().isSelected()){
            if (terrainFormInstance.getRechercherPropriete().isSelected()) {
                Titre titre = terrainFormInstance.getTitleCombobox().getValue();
                return titre;
            }else{
                Titre titre = new Titre();
                titre.setNomPropriete(terrainFormInstance.getNomPropriete().getText());
                titre.setNumero(terrainFormInstance.getNumeroTitre()+"-BA");
                titre.setNumMorcelement(terrainFormInstance.getNumeroMorcellement().getText());
                Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(terrainFormInstance.getDateCreation().getValue(), LocalTime.now()));
                titre.setDate(timestamp);
                if (DaoFactory.getTitreDao().create(titre) != 0) {
                    int id = DaoFactory.getTitreDao().findByNum(titre.getNumero());
                    titre.setId(id);
                    Notification.getInstance("Titre enregistré ave succés", NotifType.SUCCESS).showNotif();
                }
                return titre;

            }

        }
        return null;
    }

    private Terrain initTerrain(){
        if (affaireFormInstance.getConnexeRadio().isSelected()){
            ConnexAffairForView value = affaireFormInstance.getConnexeCombobox().getValue();
            Terrain terrain1 = DaoFactory.getTerrainDao().findById(value.getIdTerrain());
            if (terrain1!=null)
                return terrain1;
        }else{
            Terrain terrain = createTerrain();
            if (DaoFactory.getTerrainDao().create(terrain) == 1 ){
                int idTerrain = DaoFactory.getTerrainDao().getIdTerrainBy(terrain.getSuperficie(),
                        terrain.getParcelle(),terrain.getQuartier());
                terrain.setIdTerrain(idTerrain);
                if (terrain.getTitreDependant()!=null)
                    DbOperation.insertOnTerrrainTitre(terrain,TableName.DEPEND_TERRRAIN_TITRE);
                return terrain;
            }
        }
        return null;
    }

    private Terrain createTerrain(){
        String superficie = terrainFormInstance.getSuperficieLabel().getText();
        String parcelleTerrain = terrainFormInstance.getParcelleTerrain().getText()+ "/" + terrainFormInstance.getParcelleTerrain1().getText();
        String quartierTerrain = terrainFormInstance.getSise().getText();
        String district = terrainFormInstance.getDistrict().getText();
        String commune = terrainFormInstance.getCommune().getText();
        String region = terrainFormInstance.getRegion().getText();
        return new Terrain(superficie,parcelleTerrain,district,commune,quartierTerrain,region,initTitre());
    }

    @FXML private JFXButton checkBtn;
    @FXML private TilePane pjTilepane;
    @FXML private JFXButton newPieceBtn;
    @FXML private JFXButton delPieceBtn;
    @FXML private JFXButton saveBtn;
    @FXML private JFXButton pjPrevBtn;

    private final static MainDemandeurFormController mainDemandeurFormInstance = MainDemandeurFormController.getInstance();
    private final static  TerrainFromController terrainFormInstance = TerrainFromController.getInstance();
    private final static  AffaireFormController affaireFormInstance = AffaireFormController.getInstance();
    private final static NouveauDemandeurController newDemandeurFormInstance = NouveauDemandeurController.getInstance();
    private final static RechercherDemandeurController searchDemandeurFormInstance = RechercherDemandeurController.getInstance();
}
