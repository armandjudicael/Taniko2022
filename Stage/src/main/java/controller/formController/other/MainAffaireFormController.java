package controller.formController.other;

import com.jfoenix.controls.JFXProgressBar;
import controller.formController.demandeurController.MainDemandeurFormController;
import controller.formController.demandeurController.NouveauDemandeurController;
import controller.formController.demandeurController.RechercherDemandeurController;
import view.Dialog.FormDialog.MainAffaireForm;
import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;

public class MainAffaireFormController implements Initializable{
    @Override public void initialize(URL url, ResourceBundle resourceBundle){
       mainAffaireFormController = this;
       closeBtn.setOnAction(event -> MainAffaireForm.getInstance().close());
       affaireFormPanel.toFront();
    }
    private void executeUpdate(Node node, Label newLabel, Label oldLabel){
        new FadeIn(node).play();
        node.toFront();
        if (oldLabel!=null){
            oldLabel.getStyleClass().removeAll("navLabel");
        }
        newLabel.getStyleClass().add("navLabel");
    }
    public static void updateLabelAndShowPane(JFXButton button){
        if (button!=null){
            String buttonId = button.getId();
            switch (buttonId){
                case "affaireNextBtn" :{
                    mainAffaireFormController.executeUpdate(mainAffaireFormController.demandeurFormPanel
                            , mainAffaireFormController.demandeurLabel, mainAffaireFormController.affaireLabel);}break;
                case "dmdNextBtn" :{
                    mainAffaireFormController.executeUpdate(mainAffaireFormController.terrainFormPanel
                            , mainAffaireFormController.terrainLabel,mainAffaireFormController.demandeurLabel);}break;
                case "terrainNextBtn":{
                    mainAffaireFormController.executeUpdate(mainAffaireFormController.piecejointeFormPanel
                            , mainAffaireFormController.pieceJointeLabel, mainAffaireFormController.terrainLabel);}break;
                case "pjPrevBtn" :{
                    mainAffaireFormController.executeUpdate(mainAffaireFormController.terrainFormPanel
                            ,mainAffaireFormController.terrainLabel,mainAffaireFormController.pieceJointeLabel);}break;
                case "terrainPrevBtn" : {
                    mainAffaireFormController.executeUpdate(mainAffaireFormController.demandeurFormPanel
                            ,mainAffaireFormController.demandeurLabel,mainAffaireFormController.terrainLabel);}break;
                case "dmdPrevBtn":{
                    mainAffaireFormController.executeUpdate(mainAffaireFormController.affaireFormPanel
                            ,mainAffaireFormController.affaireLabel,mainAffaireFormController.demandeurLabel);}break;
                default:{
                    mainAffaireFormController.executeUpdate(mainAffaireFormController.affaireFormPanel
                            ,mainAffaireFormController.affaireLabel,mainAffaireFormController.pieceJointeLabel);
                }
            }
        }else {
            mainAffaireFormController.executeUpdate(mainAffaireFormController.affaireFormPanel
                    ,mainAffaireFormController.affaireLabel,mainAffaireFormController.pieceJointeLabel);
        }
    }
    public static TerrainFromController getTerrainFromInstance() {
        if(terrainFromInstance == null) terrainFromInstance = TerrainFromController.getInstance();
        return terrainFromInstance;
    }
    public static NouveauDemandeurController getNewDemandeurFormInstance() {
        if (newDemandeurFormInstance == null) newDemandeurFormInstance = NouveauDemandeurController.getInstance();
        return newDemandeurFormInstance;
    }
    public static RechercherDemandeurController getSearchDmdFormInstance() {
        if (searchDmdFormInstance == null) searchDmdFormInstance = RechercherDemandeurController.getInstance();
        return searchDmdFormInstance;
    }
    public static AffaireFormController getAffFormInstance() {
        if (affFormInstance ==null) affFormInstance = AffaireFormController.getInstance();
        return affFormInstance;
    }
    public static MainDemandeurFormController getMainDmdFormInstance() {
        if (mainDmdFormInstance == null) mainDmdFormInstance = MainDemandeurFormController.getInstance();
        return mainDmdFormInstance;
    }
    public static MainAffaireFormController getInstance() {
        return mainAffaireFormController;
    }
    private static TerrainFromController terrainFromInstance;
    private static NouveauDemandeurController newDemandeurFormInstance;
    private static RechercherDemandeurController searchDmdFormInstance;
    private static AffaireFormController affFormInstance;
    private static MainDemandeurFormController mainDmdFormInstance;
    private static MainAffaireFormController mainAffaireFormController;
    // Label
    @FXML private Label affaireLabel;
    @FXML private Label demandeurLabel;
    @FXML private Label terrainLabel;
    @FXML private Label pieceJointeLabel;
    @FXML private JFXButton closeBtn;
    // pane
    @FXML private AnchorPane affaireFormPanel;
    @FXML private AnchorPane demandeurFormPanel;
    @FXML private AnchorPane terrainFormPanel;
    @FXML private AnchorPane piecejointeFormPanel;
    @FXML private JFXProgressBar checkProgressBar;
}
