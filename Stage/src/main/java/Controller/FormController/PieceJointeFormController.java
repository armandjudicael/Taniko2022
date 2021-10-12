package Controller.FormController;

import DAO.DaoFactory;
import Model.Other.MainService;
import Model.Pojo.*;
import View.Helper.Attachement.AttachementCreatorButton;
import View.Helper.Attachement.AttachementRemoverButton;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.TilePane;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PieceJointeFormController implements Initializable{

    private static List<File> pieceJointeFiles;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        initBtnAction();
    }
    private void initBtnAction(){
        ObservableList<Node> children = pjTilepane.getChildren();
        new AttachementCreatorButton(newPieceBtn,children,false);
        new AttachementRemoverButton(delPieceBtn,children,false);
        delPieceBtn.disableProperty().bind(Bindings.isEmpty(children));
        pjPrevBtn.setOnAction(event -> MainAffaireFormController.updateLabelAndShowPane(pjPrevBtn));
        saveBtn.setOnAction(event -> {
            MainService.getInstance().launch(new Task<Void>() {
                @Override protected Void call() throws Exception{
                    Affaire affaire = new Affaire();
                    affaire.setId(16);
                    int[] all = DaoFactory.getPieceJointeDao().createAll(AttachementCreatorButton.getAttachementList(),affaire);
                    if (children.size() == all.length){
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION," Piece jointe enregistré avdec succès ");
                            alert.showAndWait();
                        });
                    }else {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR," Echec de l'enregistrement des pieces jointes ");
                            alert.showAndWait();
                        });
                    }
                    return null;
                }
            });
        });
    }


//    private void sauvegarderAffaire(){
//
//        Demandeur demandeur = new Demandeur(nom.getText(),prenom.getText(),adresse.getText(), demandeurParcelle1.getText() + "/" + demandeurParcelle1.getText(), lot.getText());
//        // enregistrement du demandeur
//        if (DaoFactory.getDemandeurDao().create(demandeur) != 0) {
//
//            int idDemandeur = DaoFactory.getDemandeurDao().getIdDemandeurByFullName(nom.getText(),prenom.getText());
//            demandeur.setId(idDemandeur);
//
//            Timestamp dateFormulation = Timestamp.valueOf(LocalDateTime.of(MainAffaireForm.this.dateFormulation.getValue(), LocalTime.now()));
//
//            String numeroAffaire = numero.getText()+numLabel.getText();
//
//            User redacteur= initRedateur();
//
//            Terrain terrain = initTerrain();
//
//            AffaireForView affairForView = new AffaireForView(numeroAffaire,
//                    dateFormulation,
//                    string2TypeDemande(typeDemande.getValue()),
//                    redacteur ,
//                    status,
//                    demandeur,
//                    terrain,
//                    new ProcedureForTableview(ProcedureStatus.NONE, new SimpleStringProperty("Auccune procedure")));
//
//            // ENREGISTRER AFFAIRE
//            if (DaoFactory.getAffaireDao().create(affairForView) != 0){
//
//                int idAffaire = DaoFactory.getAffaireDao().getAffaireIdByNum(affairForView.getNumero());
//                affairForView.setId(idAffaire);
//
//                // INSERTION DANS LA TABLE DE DISPATCH
//                if (redacteur!=null){
//                    DbOperation.insertOnDispatchTable(redacteur,affairForView);
//                }
//
//                Platform.runLater(() -> {
//                    // mis a jour de l'affichage
//                    AffairViewController.getInstance().getTableView().getItems().add(affairForView);
//                    Notification.getInstance(" L'affaire N° "+ numeroAffaire +" est enregistrer avec succès ", NotifType.SUCCESS).show();
//                });
//
//            }
//        }
//    }
//    private User initRedateur(){
//        if (moiMemeRadio.isSelected()){
//            return LoginController.getConnectedUser();
//        }else if (rechercherRedacteurRadio.isSelected()){
//            User value = redactorCombobox.getValue();
//            if (value!=null)
//                return value;
//        }
//        return null;
//    }
//    public void resetAffairForm(){
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
//
//    }
//    private Terrain initTerrain(){
//        if (connexeRadio.isSelected()){
//            ConnexAffairForView value = connexeCombobox.getValue();
//            Terrain terrain1 = DaoFactory.getTerrainDao().findById(value.getIdTerrain());
//            if (terrain1!=null)
//                return terrain1;
//        }else{
//
//            TerrainFromController instance = TerrainFromController.getInstance();
//            Titre titre = initTitre();
//            String superficie = instance.getSuperficieLabel().getText();
//            String parcelleTerrain = instance.getParcelleTerrain().getText()+ "/" +instance.getParcelleTerrain1().getText();
//            String quartierTerrain = instance.getSise().getText();
//
//            Terrain terrain = new Terrain(
//                    superficie,
//                    parcelleTerrain,
//                    instance.getDistrict().getText(),
//                    instance.getCommune().getText(),
//                    quartierTerrain,
//                    instance.getRegion().getText(),
//                    titre
//            );
//
//            if (DaoFactory.getTerrainDao().create(terrain) == 1 ){
//                int idTerrain = DaoFactory.getTerrainDao().getIdTerrainBy(superficie,parcelleTerrain,quartierTerrain);
//                terrain.setIdTerrain(idTerrain);
//                if (titre!=null)
//                    DbOperation.insertOnTerrrainTitre(terrain, TableName.DEPEND_TERRRAIN_TITRE);
//                return terrain;
//            }
//        }
//
//        return null;
//    }

    @FXML private TilePane pjTilepane;
    @FXML private JFXButton newPieceBtn;
    @FXML private JFXButton delPieceBtn;
    @FXML private JFXButton saveBtn;
    @FXML private JFXButton pjPrevBtn;
}
