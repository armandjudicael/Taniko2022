package Controller.FormController;

import animatefx.animation.SlideInRight;
import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DemandeurFormController implements Initializable {



    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        demandeurFormController = this;
        initTypeDemandeur();
        initButton();
    }

    private void initTypeDemandeur(){
        String[]type = {"Personne Morale","Personne Physique"};
        typeDemandeur.getItems().addAll(type);
        typeDemandeur.setValue(type[1]);
        typeDemandeur.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if (t1.equals(type[1])) {
                new SlideInRight(moralPane).play();
                moralPane.toFront();
            } else{
                new SlideInRight(physiquePane).play();
                physiquePane.toFront();
            }
        });
    }

    private void initButton(){
        BooleanBinding demandeurBinding = nom.textProperty().isNull().or(nom.textProperty().isEqualTo(""));
        dmdNextBtn.disableProperty().bind(demandeurBinding);
        dmdNextBtn.setOnAction(event -> MainAffaireFormController.getInstance().updateLabelAndShowPane(dmdNextBtn));
        dmdPrevBtn.setOnAction(event -> MainAffaireFormController.getInstance().updateLabelAndShowPane(dmdPrevBtn));
    }

    public static DemandeurFormController getInstance() {
        return demandeurFormController;
    }
    public ComboBox<String> getTypeDemandeur() {
        return typeDemandeur;
    }
    public TextArea getRaisonSocial() {
        return raisonSocial;
    }
    public TextField getSiegeSocial() {
        return siegeSocial;
    }
    public TextField getNomRepresentant() {
        return nomRepresentant;
    }
    public TextField getPrenomRepresentant() {
        return prenomRepresentant;
    }
    public TextField getFonctionRepresentant() {
        return fonctionRepresentant;
    }
    public TextField getAdresse() {
        return adresse;
    }
    public TextField getPrenom() {
        return prenom;
    }
    public TextField getNom() {
        return nom;
    }
    public TextField getDemandeurParcelle2() {
        return demandeurParcelle2;
    }
    public TextField getDemandeurParcelle1() {
        return demandeurParcelle1;
    }
    public TextField getLot() {
        return lot;
    }
    public TextField getTelephone() {
        return telephone;
    }
    public TextField getMail() {
        return mail;
    }
    public TextField getProffession() {
        return proffession;
    }
    public AnchorPane getPhysiquePane() {
        return physiquePane;
    }
    public AnchorPane getMoralPane() {
        return moralPane;
    }

    private static DemandeurFormController demandeurFormController;
    @FXML private ComboBox<String> typeDemandeur;
    // ANCHORPANE
    @FXML private AnchorPane physiquePane;
    @FXML private AnchorPane moralPane;
    // JFXBUTTON
    @FXML private JFXButton dmdNextBtn;
    @FXML private JFXButton dmdPrevBtn;
    // TEXTAREA
    @FXML private TextArea raisonSocial;
    // TEXTFIELD
    @FXML private TextField siegeSocial;
    @FXML private TextField nomRepresentant;
    @FXML private TextField prenomRepresentant;
    @FXML private TextField fonctionRepresentant;
    @FXML private TextField adresse;
    @FXML private TextField prenom;
    @FXML private TextField nom;
    @FXML private TextField demandeurParcelle2;
    @FXML private TextField demandeurParcelle1;
    @FXML private TextField lot;
    @FXML private TextField telephone;
    @FXML private TextField mail;
    @FXML private TextField proffession;
}
