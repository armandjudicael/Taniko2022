package Controller.FormController;

import Controller.ViewController.AffairViewController;
import Controller.ViewController.LoginController;
import DAO.DaoFactory;
import DAO.DbOperation;
import Model.Enum.NotifType;
import Model.Enum.ProcedureStatus;
import Model.Enum.TableName;
import Model.Other.MainService;
import Model.Other.ProcedureForTableview;
import Model.Pojo.*;
import View.Dialog.FormDialog.MainAffaireForm;
import View.Dialog.Other.FileChooserDialog;
import View.Dialog.Other.Notification;
import View.Model.AffaireForView;
import View.Model.ConnexAffairForView;
import View.Model.PieceJointeForView;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.TilePane;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

import static Model.Pojo.Affaire.string2TypeDemande;

public class PieceJointeFormController implements Initializable{

    private static List<File> pieceJointeFiles;
    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        saveBtn.setOnAction(event -> {
            MainAffaireForm.getInstance().close();
//            MainService.getInstance().launch(new Task<Void>() {
//                @Override protected Void call() throws Exception {
//                    sauvegarderAffaire();
//                    return null;
//                }
//                @Override protected void succeeded() {
//                    resetAffairForm();
//                }
//            });
        });
    }

    private void initBtnAction(){
        newPieceBtn.setOnAction(this::showFileChooserAndCreateAttachement);
        delPieceBtn.setOnAction(event -> {});
    }

    private PieceJointe createAttachementBy(File file) throws FileNotFoundException {
        String name = file.getName();
        String[] split = name.split("\\.");
        String description = split[0];
        String extension = split[1];
        String size = calculateFileSize(file);
        return new PieceJointe(description,extension,size,new FileInputStream(file));
    }

    private String calculateFileSize(File file){
        long length = FileUtils.sizeOf(file);
        double mega = length/FileUtils.ONE_MB;
        return String.valueOf(mega)+" Mb";
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

    private void showFileChooserAndCreateAttachement(ActionEvent event) {
        pieceJointeFiles = FileChooserDialog.getInstance();
        if (pieceJointeFiles != null && !pieceJointeFiles.isEmpty()) {
            pieceJointeFiles.forEach(file -> {
                try {
                    PieceJointeTilePane.getChildren().add(new PieceJointeForView(createAttachementBy(file), false).getPane());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML private TilePane PieceJointeTilePane;
    @FXML private JFXButton newPieceBtn;
    @FXML private JFXButton delPieceBtn;
    @FXML private JFXButton saveBtn;
    @FXML private JFXButton back2TerrainBtn;
}
