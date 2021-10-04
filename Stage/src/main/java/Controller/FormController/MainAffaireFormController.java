package Controller.FormController;

import View.Dialog.FormDialog.MainAffaireForm;
import View.helper.DialogCloserButton;
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
       new DialogCloserButton(closeBtn,MainAffaireForm.getInstance());
       mainAffaireFormController = this;
    }

    private void executeUpdate(Node node, Label newLabel, Label oldLabel){
        new FadeIn(node).play();
        node.toFront();
        if (oldLabel!=null)
           oldLabel.getStyleClass().removeAll("navLabel");
        newLabel.getStyleClass().add("navLabel");
    }

    public static void updateLabelAndShowPane(JFXButton button){
        String buttonId = button.getId();
        switch (buttonId){
            case "affaireNextBtn" :{
                mainAffaireFormController.executeUpdate(mainAffaireFormController.demandeurPane
                    , mainAffaireFormController.demandeurLabel,null);}break;
            case "dmdNextBtn" :{
                mainAffaireFormController.executeUpdate(mainAffaireFormController.terrainFormPane
                        , mainAffaireFormController.terrainLabel,mainAffaireFormController.demandeurLabel);}break;
            case "terrainNextBtn":{
                mainAffaireFormController.executeUpdate(mainAffaireFormController.pieceJointePane
                        , mainAffaireFormController.pieceJointeLabel, mainAffaireFormController.terrainLabel);}break;
            case "pjPrevBtn" :{
                mainAffaireFormController.executeUpdate(mainAffaireFormController.terrainFormPane
                        ,mainAffaireFormController.terrainLabel,mainAffaireFormController.pieceJointeLabel);}break;
            case "terrainPrevBtn" : {
                mainAffaireFormController.executeUpdate(mainAffaireFormController.demandeurPane
                        ,mainAffaireFormController.demandeurLabel,mainAffaireFormController.terrainLabel);}break;
            case "dmdPrevBtn":{
                mainAffaireFormController.executeUpdate(mainAffaireFormController.affairePane
                        ,mainAffaireFormController.affaireLabel,mainAffaireFormController.demandeurLabel);}break;
        }
    }

    private static MainAffaireFormController mainAffaireFormController;
    // Label
    @FXML private Label affaireLabel;
    @FXML private Label demandeurLabel;
    @FXML private Label terrainLabel;
    @FXML private Label pieceJointeLabel;
    @FXML private JFXButton closeBtn;
    // pane
    @FXML private AnchorPane terrainFormPane;
    @FXML private AnchorPane pieceJointePane;
    @FXML private AnchorPane demandeurPane;
    @FXML private AnchorPane affairePane;
}
