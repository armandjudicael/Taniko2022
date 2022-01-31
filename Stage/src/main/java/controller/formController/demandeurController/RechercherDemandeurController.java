package controller.formController.demandeurController;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import model.Enum.TypeDemandeur;
import model.pojo.business.other.Personne;
import model.pojo.business.other.PersonneMorale;
import view.Helper.Other.AutoCompleteCombobox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import view.Helper.Other.ControllerUtils;
import view.Model.ViewObject.ApplicantForView;

import java.net.URL;
import java.util.ResourceBundle;

public class RechercherDemandeurController implements Initializable, ControllerUtils {
    @Override public void initialize(URL location, ResourceBundle resources) {
        rechercherDemandeurController = this;
        physiqueInfo.toFront();
        initSearchApplicantCombobox();
        initButton();
        stackpane.visibleProperty().bind(demandeurCombobox.valueProperty().isNotNull());
    }
    private void initSearchApplicantCombobox(){
        demandeurCombobox.setCellFactory(param -> new DemandeurComboboxCell());
        new AutoCompleteCombobox<Personne>(demandeurCombobox,personne -> {
            String text = demandeurCombobox.getEditor().getText().toLowerCase();
           if (personne.getNom().startsWith(text)) return true;
           return false;
        });
    }
    private void goToRepresentantForm(ActionEvent event) {
        NouveauDemandeurController.getInstance().updateView();
    }
    @Override
    public boolean isOk(String result) {
        return false;
    }

    @Override
    public void initBinding() {
    }
    @Override
    public void initButton() {
        newRepresentantBtn.setOnAction(this::goToRepresentantForm);
    }

    private class DemandeurComboboxCell extends ListCell<Personne>{
        @Override protected void updateItem(Personne item, boolean empty) {
            super.updateItem(item, empty);
            if (item==null && empty){
                setText(null);
                setGraphic(null);
            }else{
                setText(item.getNom());
                setGraphic(initImageView(item));
            }
        }
        private ImageView initImageView(Personne item){
            ImageView imageView = new ImageView();
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
            imageView.setImage(item.getType() == TypeDemandeur.PERSONNE_PHYSIQUE ?
                    new Image(getClass().getResourceAsStream("/img/people_50px.png")) :
                    new Image(getClass().getResourceAsStream("/img/male_user_40px.png") ));
            return imageView;
        }
    }

    public static RechercherDemandeurController getInstance() {
        return rechercherDemandeurController;
    }
    public Label getNationalite() {
        return nationalite;
    }
    public JFXComboBox<Personne> getDemandeurCombobox() {
        return demandeurCombobox;
    }
    public JFXButton getNewRepresentantBtn() {
        return newRepresentantBtn;
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
    public JFXProgressBar getRepresentantProgress() {
        return representantProgress;
    }
    public JFXListView<ApplicantForView> getRepresentantList() {
        return representantList;
    }
    @FXML private JFXProgressBar representantProgress;
    @FXML private JFXComboBox<Personne> demandeurCombobox;
    @FXML private StackPane stackpane;
    public static PersonneMorale getPersonneMorale() {
        return personneMorale;
    }
    // MORALE
    @FXML private JFXButton newRepresentantBtn;
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
    @FXML private JFXListView<ApplicantForView> representantList;
    @FXML private AnchorPane physiqueInfo;
    private static PersonneMorale personneMorale;
}
