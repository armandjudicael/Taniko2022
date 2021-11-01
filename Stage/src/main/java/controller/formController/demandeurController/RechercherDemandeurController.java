package controller.formController.demandeurController;

import Model.Enum.TypeDemandeur;
import Model.Pojo.business.PersonneMorale;
import View.Helper.Other.AutoCompleteCombobox;
import View.Model.ViewObject.RepresentantForView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import java.net.URL;
import java.util.ResourceBundle;

public class RechercherDemandeurController implements Initializable{

    @Override public void initialize(URL location, ResourceBundle resources) {
        rechercherDemandeurController = this;
        physiqueInfo.toFront();
        initSearchApplicantCombobox();
        stackpane.visibleProperty().bind(demandeurCombobox.valueProperty().isNotNull());
    }

    private void initSearchApplicantCombobox(){
        demandeurCombobox.setCellFactory(param -> new DemandeurComboboxCell());
        new AutoCompleteCombobox<PersonneMorale>(demandeurCombobox,personneMorale -> {
            String text = demandeurCombobox.getEditor().getText().toLowerCase();
           if (personneMorale.getNom().equals(text)) return true;
           return false;
        });
    }

    private class DemandeurComboboxCell extends ListCell<PersonneMorale>{
        private  final Image moralImg = new Image(getClass().getResourceAsStream("/img/user_group.png"));
        private final Image physiqueImg = new Image(getClass().getResourceAsStream("/img/account_40px.png"));
        @Override protected void updateItem(PersonneMorale item, boolean empty) {
            super.updateItem(item, empty);
            if (item==null && empty){
                setText(null);
                setGraphic(null);
            }else{
                setText(item.getNom());
                setGraphic(initImageView(item));
            }
        }
        private ImageView initImageView(PersonneMorale item){
            ImageView imageView = new ImageView();
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            if (item.getType().equals(TypeDemandeur.PERSONNE_PHYSIQUE)) imageView.setImage(physiqueImg);
            else imageView.setImage(moralImg);
            return imageView;
        }
    }
    public static RechercherDemandeurController getInstance() {
        return rechercherDemandeurController;
    }
    public Label getNationalite() {
        return nationalite;
    }
    public JFXComboBox<PersonneMorale> getDemandeurCombobox() {
        return demandeurCombobox;
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

    @FXML private JFXComboBox<PersonneMorale> demandeurCombobox;
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
