package controller.formController.demandeurController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import controller.formController.otherFormController.MainAffaireFormController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Enum.SituationMatrimoniale;
import model.Enum.TypeDemandeur;
import model.pojo.business.other.PersonneMorale;
import model.pojo.business.other.PersonnePhysique;
import model.pojo.utils.Mariage;
import view.Helper.Other.ControllerUtils;
import view.Helper.Other.SnackBarNotification;
import view.Model.ViewObject.ApplicantForView;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class NouveauDemandeurController implements Initializable, ControllerUtils{

    public static boolean isWithConjoint() {
        return withConjoint;
    }

    @Override public void initialize(URL location, ResourceBundle resources) {
        nouveauDemandeurController = this;
        initRegimeMatrimoniale();
        initTextFieldAndErrorLabel();
        initBinding();
        initButton();
        physiquePane.toFront();
    }

    private void initTextFieldAndErrorLabel() {
        // PHYSIQUE
        initTextFieldFocusedProperty(nomDmd, physiqueNameError);
        initTextFieldFocusedProperty(telephone, telPhysiqueError);
        initTextFieldFocusedProperty(email, emailPhysiqueError);
        // MORALE
        initTextFieldFocusedProperty(raisonSocial, nomMoraleError);
        initTextFieldFocusedProperty(telPersonneMorale, telMoraleError);
        initTextFieldFocusedProperty(emailPersonneMorale, emailMoraleError);
    }

    private void initTextFieldFocusedProperty(TextInputControl textInputControl, Label errorLabel) {
        textInputControl.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) {
                textInputControl.getStyleClass().add("boxError");
                errorLabel.setVisible(false);
            }
        });
    }

    private void initRegimeMatrimoniale() {
        String[] regimeType = {"Droit commun", "Séparation des biens"};
        regimeMatrimoniale.getItems().addAll(regimeType);
        regimeMatrimoniale.setValue(regimeType[0]);
    }

    private String initSex(Boolean isApplicant) {
        Toggle selectedToggle = getSexe().getSelectedToggle();
        boolean equals = selectedToggle.equals(getMasculin());
        if (isApplicant) {
            if (equals) return "Masculin";
            else return "Féminin";
        } else {
            if (!equals) return "Masculin";
            else return "Féminin";
        }
    }

    private SituationMatrimoniale initSituationMatrimoniale() {
        return (getMarie().isSelected() ? SituationMatrimoniale.MARIE : (getCelibataire().isSelected() ? SituationMatrimoniale.CELIBATAIRE : SituationMatrimoniale.VEUVE));
    }

    public PersonnePhysique createDemandeurPhysique(boolean isConjoint) {
        PersonnePhysique ph = new PersonnePhysique();
        ph.setNom(getNomDmd().getText());
        ph.setAdresse(getAdresse().getText());
        String numero = getTelephone().getText();
        ph.setNumTel((numero != null && !numero.isEmpty() ? numero : null));
        String email = getEmail().getText();
        ph.setEmail((email != null && !email.isEmpty()) ? email : null);
        LocalDate value = getDateDeNaissanceDmd().getValue();
        ph.setDateDeNaissance(java.sql.Date.valueOf(value != null ? value : LocalDate.now()));
        ph.setLieuDeNaissance(getLieuDeNaissanceDmd().getText());
        ph.setSexe(initSex(isConjoint).trim());
        String parcelle = getDemandeurParcelle1().getText() + "/" + getDemandeurParcelle2().getText();
        ph.setParcelle((parcelle != null && !parcelle.isEmpty()) ? parcelle : "");
        ph.setLot(getLot().getText());
        ph.setProfession(getProffession().getText());
        ph.setSituationMatrimoniale(isConjoint ? SituationMatrimoniale.MARIE : initSituationMatrimoniale());
        ph.setNationalite(getNationalite().getText());
        ph.setType(TypeDemandeur.PERSONNE_PHYSIQUE);
        ph.setPere(getNomPere().getText());
        ph.setMere(getNomMere().getText());
        ph.setNumCin(getCinPersonnePhysique().getText());
        LocalDate value1 = getDateDelivranceCinPh().getValue();
        ph.setDateDelivranceCin(value1 == null ? null : Date.valueOf(value1));
        ph.setLieuDelivranceCin(getLieuDelivranceCinPh().getText());
        if (!isConjoint) {
            if (marie.isSelected()) ph.setMariage(createMariage());
        }
        resetDemandeurForm();
        return ph;
    }

    private Mariage createMariage() {
        // MARIAGE
        String lieuMariage = getLieuMariage().getText();
        LocalDate value1 = getDateMariage().getValue();
        Date dateMariage = value1 == null ? null : Date.valueOf(value1);
        String regimeMatrimoniale = getRegimeMatrimoniale().getValue();
        return new Mariage(dateMariage, lieuMariage, Mariage.String2RegimeMatrimoniale(regimeMatrimoniale));
    }

    public PersonneMorale createDemandeurMorale() {
        String raisonSocial = getRaisonSocial().getText();
        String siegeSocial = getSiegeSocial().getText();
        String telephone = getTelPersonneMorale().getText();
        String email = getEmailPersonneMorale().getText();
        String nationalite = getNationaliteMorale().getText();
        PersonneMorale pm = new PersonneMorale();
        TypeDemandeur typePersonneMorale = getTypePersonneMorale();
        if (typePersonneMorale == TypeDemandeur.PERSONNE_MORALE_PRIVE) {
            String nif = nifTextField.getText();
            String stat = statTextField.getText();
            String cap = capitalTextField.getText();
            String rcs = rcsTextField.getText();
            pm.setStatistique(stat);
            pm.setNumIdentificationFiscal(nif);
            pm.setCapital(cap);
            pm.setRcs(rcs);
        }
        pm.setNumTel(telephone);
        pm.setEmail(email);
        pm.setNationalite(nationalite);
        pm.setAdresse(siegeSocial);
        pm.setNom(raisonSocial);
        pm.setType(typePersonneMorale);
        pm.setRepresentant(null);
        return pm;
    }

    @Override public boolean isOk(String result) {
        return false;
    }

    @Override public void initBinding() {
  //      initMoralPersonneBinding();
        initMariageBinding();
    }

    private void initMariageBinding() {
        BooleanBinding notSelected = marie.selectedProperty().not();
        dateMariage.disableProperty().bind(notSelected);
        regimeMatrimoniale.disableProperty().bind(notSelected);
        lieuMariage.disableProperty().bind(notSelected);
        conjointBtn.disableProperty().bind(notSelected);
    }

    private void initMoralPersonneBinding() {
        BooleanBinding notSelected = Bindings.not(moralePriveImposableRadio.selectedProperty());
        nifTextField.disableProperty().bind(notSelected);
        capitalTextField.disableProperty().bind(notSelected);
        rcsTextField.disableProperty().bind(notSelected);
        statTextField.disableProperty().bind(notSelected);
    }

    @Override public void initButton(){
        representantBtn.setOnAction(this::goToRepresentant);
        conjointBtn.setOnAction(this::goToConjoint);
        saveRepBtn.setOnAction(event -> {
            addNewRepToListAndShowMoralInfo();
        });
    }

    private void addNewRepToListAndShowMoralInfo() {
        JFXListView<ApplicantForView> representantList = RechercherDemandeurController.getInstance().getRepresentantList();
        PersonnePhysique ph = createDemandeurPhysique(false);
        representantList.getItems().add(0,new ApplicantForView(ph));
        MainDemandeurFormController.getInstance().mainTabPaneSelect(false);
        RechercherDemandeurController.getInstance().getMoralInfo().toFront();
    }

    public ScrollPane getMoralPane() {
        return moralPane;
    }

    public TextArea getRaisonSocial() {
        return raisonSocial;
    }

    public void resetDemandeurForm(){
        resetDemandeurMorale();
        resetDemandeurPhysique(false);
    }

    private void resetMariage() {
        // MARIAGE
        getLieuMariage().clear();
        getDateMariage().setValue(null);
    }

    private void resetDemandeurMorale() {
        clearAndEnable(getRaisonSocial());
        clearAndEnable(getSiegeSocial());
        clearAndEnable(getTelPersonneMorale());
        clearAndEnable(getEmailPersonneMorale());
        clearAndEnable(getNationaliteMorale());
        getMoralPane().setVvalue(0.0);
    }

    private void clearAndEnable(TextInputControl inputControl) {
        inputControl.clear();
        inputControl.setDisable(inputControl.isDisable());
    }

    private void resetDemandeurPhysique(boolean isConjoint) {
        getNomDmd().clear();
        getDateDeNaissanceDmd().setValue(null);
        getLieuDeNaissanceDmd().clear();
        getAdresse().clear();
        getDemandeurParcelle1().clear();
        getDemandeurParcelle2().clear();
        getLot().clear();
        getProffession().clear();
        getEmail().clear();
        getTelephone().clear();
        getNationalite().clear();
        getNomPere().clear();
        getNomMere().clear();
        getCinPersonnePhysique().clear();
        getLieuDelivranceCinPh().clear();
        // scrollpane
        getPhysiquePane().setVvalue(0.0);
        if (!isConjoint) resetMariage();
    }

    public TextArea getSiegeSocial() {
        return siegeSocial;
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
    public TextField getLieuMariage() {
        return lieuMariage;
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

    public Label getPhysiqueNameError() {
        return physiqueNameError;
    }

    public Label getTelPhysiqueError() {
        return telPhysiqueError;
    }

    public Label getEmailPhysiqueError() {
        return emailPhysiqueError;
    }

    public Label getEmailMoraleError() {
        return emailMoraleError;
    }

    public Label getTelMoraleError() {
        return telMoraleError;
    }

    public Label getNomMoraleError() {
        return nomMoraleError;
    }

    public TextField getCinPersonnePhysique() {
        return cinPersonnePhysique;
    }

    public TextField getLieuDelivranceCinPh() {
        return lieuDelivranceCinPh;
    }

    public DatePicker getDateDelivranceCinPh() {
        return dateDelivranceCinPh;
    }

    public DatePicker getDateProcuration() {
        return dateProcuration;
    }

    public TextField getNumeroProcuration() {
        return numeroProcuration;
    }

    public static NouveauDemandeurController getInstance() {
        return nouveauDemandeurController;
    }

    public void goToRepresentant(ActionEvent event){
        if (!raisonSocial.getText().isBlank() && !siegeSocial.getText().isBlank()) {
            updateView();
            // enregister le personne morale
            personneMorale = createDemandeurMorale();
            withRepresentant = true;
            // On a pas besoin d'enregistrer les informations du conjoint du representant
            conjointBtn.setVisible(false);
            SnackBarNotification.getInstance().showOn(MainAffaireFormController.getInstance().getMainAffBorderPane(),
                    "Veuillez entrer les information concernant le representant ", Alert.AlertType.INFORMATION);
        } else SnackBarNotification.getInstance().showOn(MainAffaireFormController.getInstance().getMainAffBorderPane(),
                "Veuillez remplir les champs ", Alert.AlertType.ERROR);
    }

    public void updateView() {
        personneInfo.setText(" INFORMATION DU REPRESENTANT ");
        NouveauDemandeurController.getInstance().getPhysiquePane().toFront();
        RechercherDemandeurController.getInstance().getPhysiqueInfo().toFront();
    }

    private void goToConjoint(ActionEvent event) {
        if (dateMariage.getValue() == null || lieuMariage.getText().isBlank()) {
            SnackBarNotification.getInstance().showOn(MainAffaireFormController.getInstance().getMainAffBorderPane(), " Veuillez remplir les champs ");
        } else {
            withConjoint = true;
            initConjointForm();
        }
    }

    private void initConjointForm() {
        personneInfo.setText(" INFORMATION DU CONJOINT ");
        // enregistrer le demandeur
        personnePhysique = createDemandeurPhysique(false);
        // effacer le formulaire
        resetDemandeurPhysique(true);
        // Rendre le boutton invisible
        conjointBtn.setVisible(false);
        selectAndEnableMatrimonialsituation();
        // initialiser le sexe
        selectAndEnableSexeRadio();
        // notifier l'utilisateur
        SnackBarNotification.getInstance().showOn(" Veuillez saisir les informations du conjoint ", Alert.AlertType.INFORMATION);
    }

    private void selectAndEnableSexeRadio(){
        if (personnePhysique.getSexe().equals("Masculin")) {
            masculin.setSelected(true);
        } else feminin.setSelected(true);
        masculin.setDisable(true);
        feminin.setDisable(true);
    }

    private void selectAndEnableMatrimonialsituation() {
        marie.setSelected(true);
        marie.setDisable(true);
        celibataire.setDisable(true);
        veuve.setDisable(true);
    }

    public Label getPersonneInfo() {
        return personneInfo;
    }

    public static PersonnePhysique getPersonnePhysique() {
        return personnePhysique;
    }

    private TypeDemandeur getTypePersonneMorale() {
        Toggle selectedToggle = typePersonneMorale.getSelectedToggle();
        return selectedToggle.equals(moralePriveRadio) ? TypeDemandeur.PERSONNE_MORALE_PRIVE : TypeDemandeur.PERSONNE_MORALE_PUBLIQUE;
    }

    @FXML
    private TextField lieuMariage;
    @FXML
    private ComboBox<String> regimeMatrimoniale;
    @FXML
    private JFXDatePicker dateMariage;
    @FXML
    private TextField nationalite;
    @FXML
    private TextField lieuDeNaissanceDmd;
    @FXML
    private JFXRadioButton celibataire;
    @FXML
    private ToggleGroup situationMatrimoniale;
    @FXML
    private JFXRadioButton marie;
    @FXML
    private JFXRadioButton veuve;
    @FXML
    private JFXRadioButton masculin;
    @FXML
    private ToggleGroup sexe;
    @FXML
    private JFXRadioButton feminin;
    @FXML
    private JFXDatePicker dateDeNaissanceDmd;
    @FXML
    private ScrollPane moralPane;
    @FXML
    private TextArea raisonSocial;
    @FXML
    private TextArea siegeSocial;
    @FXML
    private TextField nationaliteMorale;
    @FXML
    private TextField emailPersonneMorale;
    @FXML
    private TextField telPersonneMorale;
    @FXML
    private ScrollPane physiquePane;
    @FXML
    private TextField adresse;
    @FXML
    private TextField nomDmd;
    @FXML
    private TextField demandeurParcelle2;
    @FXML
    private TextField demandeurParcelle1;
    @FXML
    private TextField lot;
    @FXML
    private TextField telephone;
    @FXML
    private TextField email;
    @FXML
    private TextField proffession;
    @FXML
    private TextField nomPere;
    @FXML
    private TextField nomMere;
    @FXML
    private Label personneInfo;
    // ERROR LABEL
    // PHYSIQUE
    @FXML
    private Label physiqueNameError;
    @FXML
    private Label telPhysiqueError;
    @FXML
    private Label emailPhysiqueError;
    // MORALE
    @FXML
    private Label emailMoraleError;
    @FXML
    private Label telMoraleError;
    @FXML
    private Label nomMoraleError;
    // CIN
    @FXML
    private TextField cinPersonnePhysique;
    @FXML
    private TextField lieuDelivranceCinPh;
    @FXML
    private DatePicker dateDelivranceCinPh;
    @FXML
    private DatePicker dateProcuration;
    @FXML
    private TextField numeroProcuration;
    @FXML
    private JFXRadioButton moralePubliqueRadio;
    @FXML
    private ToggleGroup typePersonneMorale;
    @FXML
    private JFXRadioButton moralePriveRadio;
    @FXML
    private JFXRadioButton moralePriveImposableRadio;
    @FXML
    private TextField rcsTextField;
    @FXML
    private TextField nifTextField;
    @FXML
    private TextField capitalTextField;
    @FXML
    private TextField statTextField;
    @FXML
    private JFXButton representantBtn;
    @FXML
    private JFXButton conjointBtn;
    @FXML
    private JFXButton saveRepBtn;
    private static boolean withConjoint = false;
    public static boolean isWithRepresentant() {
        return withRepresentant;
    }
    private static boolean withRepresentant = false;
    private static NouveauDemandeurController nouveauDemandeurController;

    public static void setPersonnePhysique(PersonnePhysique personnePhysique) {
        NouveauDemandeurController.personnePhysique = personnePhysique;
    }

    private static PersonnePhysique personnePhysique;

    public static PersonneMorale getPersonneMorale() {
        return personneMorale;
    }

    public static void setPersonneMorale(PersonneMorale personneMorale) {
        NouveauDemandeurController.personneMorale = personneMorale;
    }
    private static PersonneMorale personneMorale;
}
