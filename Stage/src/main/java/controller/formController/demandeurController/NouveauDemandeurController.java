package controller.formController.demandeurController;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;

public class NouveauDemandeurController implements Initializable {

    @Override public void initialize(URL location, ResourceBundle resources) {
        nouveauDemandeurController = this;
        initRegimeMatrimoniale();
        physiquePane.toFront();
        mariagePane.disableProperty().bind(Bindings.not(marie.selectedProperty()));
    }

    private void initRegimeMatrimoniale(){
       String[] regimeType = {"Droit commun","Séparation des biens"};
       regimeMatrimoniale.getItems().addAll(regimeType);
       regimeMatrimoniale.setValue(regimeType[0]);
    }

    public ScrollPane getMoralPane() {
        return moralPane;
    }
    public TextArea getRaisonSocial() {
        return raisonSocial;
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
    public TextArea getSiegeSocial() {
        return siegeSocial;
    }
    public TextField getAdresseRepresentant() {
        return adresseRepresentant;
    }
    public TextField getNationaliteMorale() {
        return nationaliteMorale;
    }
    public TextField getEmailPersonneMorale() {
        return emailPersonneMorale;
    }
    public TextField getTelPersonneMorale() {
        return telPersonneMorale;
    }
    public ScrollPane getPhysiquePane() {
        return physiquePane;
    }
    public TextField getAdresse() {
        return adresse;
    }
    public TextField getPrenomDmd() {
        return prenomDmd;
    }
    public TextField getNomDmd() {
        return nomDmd;
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
    public TextField getEmail() {
        return email;
    }
    public TextField getProffession() {
        return proffession;
    }
    public TextField getPrenomConjoint() {
        return prenomConjoint;
    }
    public TextField getLieuDeNaissanceConjoint() {
        return lieuDeNaissanceConjoint;
    }
    public TextField getLieuMariage() {
        return lieuMariage;
    }
    public TextField getNomConjoint() {
        return nomConjoint;
    }
    public JFXDatePicker getDateNaissanceConjoint() {
        return dateNaissanceConjoint;
    }
    public ComboBox<String> getRegimeMatrimoniale() {
        return regimeMatrimoniale;
    }
    public JFXDatePicker getDateMariage() {
        return dateMariage;
    }
    public TextField getNationalite() {
        return nationalite;
    }
    public TextField getLieuDeNaissanceDmd() {
        return lieuDeNaissanceDmd;
    }
    public JFXRadioButton getCelibataire() {
        return celibataire;
    }
    public ToggleGroup getSituationMatrimoniale() {
        return situationMatrimoniale;
    }
    public JFXRadioButton getMarie() {
        return marie;
    }
    public JFXRadioButton getVeuve() {
        return veuve;
    }
    public JFXRadioButton getMasculin() {
        return masculin;
    }
    public ToggleGroup getSexe() {
        return sexe;
    }
    public JFXRadioButton getFeminin() {
        return feminin;
    }
    public JFXDatePicker getDateDeNaissanceDmd() {
        return dateDeNaissanceDmd;
    }
    public TextField getNomPere() {
        return nomPere;
    }
    public TextField getNomMere() {
        return nomMere;
    }
    public static NouveauDemandeurController getInstance() {
        return nouveauDemandeurController;
    }

    @FXML private TextField lieuDeNaissanceConjoint;
    @FXML private TextField lieuMariage;
    @FXML private TextField nomConjoint;
    @FXML private JFXDatePicker dateNaissanceConjoint;
    @FXML private ComboBox<String> regimeMatrimoniale;
    @FXML private JFXDatePicker dateMariage;
    @FXML private TextField nationalite;
    @FXML private TextField lieuDeNaissanceDmd;
    @FXML private JFXRadioButton celibataire;
    @FXML private ToggleGroup situationMatrimoniale;
    @FXML private JFXRadioButton marie;
    @FXML private JFXRadioButton veuve;
    @FXML private JFXRadioButton masculin;
    @FXML private ToggleGroup sexe;
    @FXML private JFXRadioButton feminin;
    @FXML private JFXDatePicker dateDeNaissanceDmd;
    @FXML private ScrollPane moralPane;
    @FXML private TextArea raisonSocial;
    @FXML private TextField nomRepresentant;
    @FXML private TextField prenomRepresentant;
    @FXML private TextField fonctionRepresentant;
    @FXML private TextArea siegeSocial;
    @FXML private TextField adresseRepresentant;
    @FXML private TextField nationaliteMorale;
    @FXML private TextField emailPersonneMorale;
    @FXML private TextField telPersonneMorale;
    @FXML private ScrollPane physiquePane;
    @FXML private TextField adresse;
    @FXML private TextField prenomDmd;
    @FXML private TextField nomDmd;
    @FXML private TextField demandeurParcelle2;
    @FXML private TextField demandeurParcelle1;
    @FXML private TextField lot;
    @FXML private TextField telephone;
    @FXML private TextField email;
    @FXML private TextField proffession;
    @FXML private TextField prenomConjoint;
    @FXML private TextField nomPere;
    @FXML private TextField nomMere;
    @FXML private AnchorPane mariagePane;
    private static NouveauDemandeurController nouveauDemandeurController;
}
