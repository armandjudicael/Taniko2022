package controller.formController.demandeurController;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class RechercherDemandeurController implements Initializable {

    @Override public void initialize(URL location, ResourceBundle resources) {
        rechercherDemandeurController = this;
        physiqueInfo.toFront();
       // stackpane.visibleProperty().bind(redactorCombobox.valueProperty().isNotNull());
    }

    public static RechercherDemandeurController getInstance() {
        return rechercherDemandeurController;
    }
    public Label getNationalite() {
        return nationalite;
    }
    public JFXComboBox getRedactorCombobox() {
        return redactorCombobox;
    }
    public JFXTreeTableView<?> getRepresentantTreeTableView() {
        return representantTreeTableView;
    }
    public TreeTableColumn<?, ?> getNomRepresentantTreeTbColum() {
        return nomRepresentantTreeTbColum;
    }
    public TreeTableColumn<?, ?> getNumAffTreeTbColumn() {
        return numAffTreeTbColumn;
    }
    public TreeTableColumn<?, ?> getDateTreeTbColumn() {
        return dateTreeTbColumn;
    }
    public JFXButton getNewPieceBtn() {
        return newPieceBtn;
    }
    public JFXButton getDelPieceBtn() {
        return delPieceBtn;
    }

    public Label getNomMorale() {
        return nomMorale;
    }
    public Label getSiegeMorale() {
        return siegeMorale;
    }
    public Label getEmailMorale() {
        return emailMorale;
    }
    public Label getTelMorale() {
        return telMorale;
    }
    public Label getNomDemandeurPhysique() {
        return nomDemandeurPhysique;
    }
    public Label getAdressePhysique() {
        return adressePhysique;
    }
    public Label getEmailPhysique() {
        return emailPhysique;
    }
    public Label getTelPhysique() {
        return telPhysique;
    }
    public ScrollPane getMoralInfo() {
        return moralInfo;
    }
    public AnchorPane getPhysiqueInfo() {
        return physiqueInfo;
    }
    private static RechercherDemandeurController rechercherDemandeurController;

    @FXML private JFXComboBox<String> redactorCombobox;
    @FXML private StackPane stackpane;
    @FXML private JFXTreeTableView<?> representantTreeTableView;
    @FXML private TreeTableColumn<?, ?> nomRepresentantTreeTbColum;
    @FXML private TreeTableColumn<?, ?> numAffTreeTbColumn;
    @FXML private TreeTableColumn<?, ?> dateTreeTbColumn;
    // MORALE
    @FXML private JFXButton newPieceBtn;
    @FXML private JFXButton delPieceBtn;
    @FXML private Label nomMorale;
    @FXML private Label siegeMorale;
    @FXML private Label emailMorale;
    @FXML private Label telMorale;
    @FXML private ScrollPane moralInfo;
    // PHYSIQUE
    @FXML private Label nomDemandeurPhysique;
    @FXML private Label adressePhysique;
    @FXML private Label emailPhysique;
    @FXML private Label telPhysique;
    @FXML private Label nationalite;
    @FXML private AnchorPane physiqueInfo;
}
