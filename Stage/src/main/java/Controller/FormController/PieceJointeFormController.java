package Controller.FormController;

import DAO.DaoFactory;
import Model.Other.MainService;
import Model.Pojo.*;
import View.Dialog.Other.FileChooserDialog;
import View.Model.PieceJointeForView;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.TilePane;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PieceJointeFormController implements Initializable{
    private static List<File> pieceJointeFiles;
    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        initBtnAction();
    }

    private void initBtnAction(){
        newPieceBtn.setOnAction(this::showFileChooserAndCreateAttachement);
        delPieceBtn.setOnAction(this::deletePieceJointe);
        ObservableList<Node> children = pjTilepane.getChildren();
        delPieceBtn.disableProperty().bind(Bindings.isEmpty(children));
        pjPrevBtn.setOnAction(event -> MainAffaireFormController.updateLabelAndShowPane(pjPrevBtn));
        saveBtn.setOnAction(event -> {

            MainService.getInstance().launch(new Task<Void>() {

                @Override protected Void call() throws Exception{
                    Affaire affaire = new Affaire();
                    affaire.setId(16);
                    int[] all = DaoFactory.getPieceJointeDao().createAll(pjTilepane.getChildren(),affaire);
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

    private void deletePieceJointe(ActionEvent event) {
        ObservableList<Node> children = pjTilepane.getChildren();
        List<Node> collect = children.stream().filter(node -> {
            PieceJointeForView pieceJointeForView = (PieceJointeForView) node;
            boolean selected = pieceJointeForView.getPieceCheckbox().isSelected();
            if (selected) return true;
            else return false;
        }).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            children.removeAll(collect);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(" Veuillez cochez le(s) pièce(s) jointe à supprimer ");
            alert.showAndWait();
        }
    }

    private PieceJointe createAttachementBy(File file) throws FileNotFoundException{
        String name = file.getName();
        String[] split = name.split("\\.");
        String description = split[0];
        String extension = split[1];
        String size = calculateFileSize(file);
        return new PieceJointe(description,extension,size,new FileInputStream(file));
    }

    public String calculateFileSize(File file){
        long length = FileUtils.sizeOf(file);
        long kilo = length / FileUtils.ONE_KB;
        if (kilo<FileUtils.ONE_KB) return kilo+" Kb";
        return  kilo/FileUtils.ONE_MB+" Mb";
        //        Dialog dialog = new Dialog();
//        DialogPane dialogPane = new DialogPane();
//        dialogPane.setContent(new Node() {});
//        dialog.setDialogPane(dialogPane);
//        dialog.initStyle(StageStyle.UNDECORATED);
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
            if (pieceJointeFiles.size()>10){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(" le nombre de limite de fichier selectionné a importer simultanement est de 10");
                alert.showAndWait();
                return;
            }
            pieceJointeFiles.forEach(file -> {
                try {
                    pjTilepane.getChildren().add(new PieceJointeForView(createAttachementBy(file), false));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML private TilePane pjTilepane;
    @FXML private JFXButton newPieceBtn;
    @FXML private JFXButton delPieceBtn;
    @FXML private JFXButton saveBtn;
    @FXML private JFXButton pjPrevBtn;

}
