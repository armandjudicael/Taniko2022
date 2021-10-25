package controller.formController;

import DAO.DaoFactory;
import DAO.DbOperation;
import Model.Enum.NotifType;
import Model.Enum.TableName;
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
import controller.viewController.AffairViewController;
import controller.viewController.LoginController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.TilePane;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

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
                    Affaire affaire = new Affaire();
                    affaire.setId(16);
                    int[] all = DaoFactory.getPieceJointeDao().createAll(AttachementCreatorButton.getAttachementList(),affaire);
                    if (children.size() == all.length){
                            String message = " Piece jointe enregistré avdec succès ";
                            Notification.getInstance(message, NotifType.SUCCESS).showNotif();
                    }else {
                        String message = " Echec de l'enregistrement des pieces jointes ";
                        Notification.getInstance(message,NotifType.WARNING).showNotif();
                    }
                    return null;
                }
            });
        });
    }


    private void sauvegarderAffaire(){
        Demandeur demandeur = new Demandeur(nom.getText(),prenom.getText(),adresse.getText(), demandeurParcelle1.getText() + "/" + demandeurParcelle1.getText(), lot.getText());
        // enregistrement du demandeur
        if (DaoFactory.getDemandeurDao().create(demandeur) != 0) {
            int idDemandeur = DaoFactory.getDemandeurDao().getIdDemandeurByFullName(nom.getText(),prenom.getText());
            demandeur.setId(idDemandeur);
            Timestamp dateFormulation = Timestamp.valueOf(LocalDateTime.of(MainAffaireForm.this.dateFormulation.getValue(), LocalTime.now()));
            String numeroAffaire = numero.getText()+numLabel.getText();

            User redacteur= initRedateur();
            Terrain terrain = initTerrain();
            AffaireForView affairForView = new AffaireForView(numeroAffaire,
                    dateFormulation,
                    string2TypeDemande(typeDemande.getValue()),
                    redacteur ,
                    status,
                    demandeur,
                    terrain,
                    new ProcedureForTableview(ProcedureStatus.NONE, new SimpleStringProperty("Auccune procedure")));
            // ENREGISTRER AFFAIRE
            if (DaoFactory.getAffaireDao().create(affairForView) != 0){
                int idAffaire = DaoFactory.getAffaireDao().getAffaireIdByNum(affairForView.getNumero());
                affairForView.setId(idAffaire);
                // INSERTION DANS LA TABLE DE DISPATCH
                if (redacteur!=null){
                    DbOperation.insertOnDispatchTable(redacteur,affairForView);
                }
                Platform.runLater(() -> {
                    // mis a jour de l'affichage
                    AffairViewController.getInstance().getTableView().getItems().add(affairForView);
                    Notification.getInstance(" L'affaire N° "+ numeroAffaire +" est enregistrer avec succès ", NotifType.SUCCESS).show();
                });

            }
        }
    }

    private PersonnePhysique initDemandeur(){

        Tab searchTab = mainDemandeurFormInstance.getSearchTab();
        Tab newTab = mainDemandeurFormInstance.getNewTab();
        ToggleButton moraleToogle = mainDemandeurFormInstance.getPersonneMorale();

        if (newTab.isSelected()){

            String name = newDemandeurFormInstance.getNomDmd().getText();
            String firstName = newDemandeurFormInstance.getAdresse().getText();
            String adresse = newDemandeurFormInstance.getAdresse().getText();
            String email = newDemandeurFormInstance.getEmail().getText();
            String numTel = newDemandeurFormInstance.getTelephone().getText();
            String nationnalite = newDemandeurFormInstance.getNationalite().getText();
            if (moraleToogle.isSelected()){
                String nomRepresentant = newDemandeurFormInstance.getNomRepresentant().getText();
                String prenomRepresentant = newDemandeurFormInstance.getPrenomRepresentant().getText();
                String prefessionRepresentant = newDemandeurFormInstance.getProffession().getText();
            }else {

            }

        }else if (searchTab.isSelected()){

        }

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

    private Titre initTitre() {
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
            Titre titre = initTitre();
            String superficie = terrainFormInstance.getSuperficieLabel().getText();
            String parcelleTerrain = terrainFormInstance.getParcelleTerrain().getText()+ "/" + terrainFormInstance.getParcelleTerrain1().getText();
            String quartierTerrain = terrainFormInstance.getSise().getText();
            Terrain terrain = new Terrain(
                    superficie,
                    parcelleTerrain,
                    terrainFormInstance.getDistrict().getText(),
                    terrainFormInstance.getCommune().getText(),
                    quartierTerrain,
                    terrainFormInstance.getRegion().getText(),
                    titre
            );
            if (DaoFactory.getTerrainDao().create(terrain) == 1 ){
                int idTerrain = DaoFactory.getTerrainDao().getIdTerrainBy(superficie,parcelleTerrain,quartierTerrain);
                terrain.setIdTerrain(idTerrain);
                if (titre!=null)
                    DbOperation.insertOnTerrrainTitre(terrain, TableName.DEPEND_TERRRAIN_TITRE);
                return terrain;
            }
        }
        return null;
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
