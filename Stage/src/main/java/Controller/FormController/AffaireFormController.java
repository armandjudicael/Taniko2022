package Controller.FormController;

import Model.Enum.Type;
import Model.Pojo.User;
import View.Cell.ListCell.ConnexeListCell;
import View.Cell.ListCell.DispatchListcell;
import View.Cell.ListCell.StatusCell;
import View.Model.ConnexAffairForView;
import View.helper.AutoCompleteCombobox;
import View.helper.DialogCloserButton;
import View.helper.NumeroChecker;
import com.jfoenix.controls.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class AffaireFormController  implements Initializable {

    @Override public void initialize(URL url, ResourceBundle resourceBundle){
        affaireFormController = this;
        initRedacteurRadio();
        initTypeDemande();
        initDatePicker();
        initStatusCombobox();
        initRadioButton();
        initRedactorCombobox();
        initConnexeCombobox();
        initBinding();
        initNumChecker();
        initDatePickerValue();
        // numLabel
        StringExpression stringExpression = new SimpleStringProperty(" - ").concat(typeAffair).concat(" / ").concat(yearAffairCigle);
        numAndDateAffLabel.textProperty().bind(stringExpression);
    }

    private void initBinding(){

        BooleanBinding rechercherRedacteur = Bindings.and(Bindings.not(redacteurBox.disableProperty()), redactorCombobox.valueProperty().isNull());
        BooleanBinding rechercherConnexe = Bindings.and(Bindings.not(connexeBox.disableProperty()), connexeCombobox.valueProperty().isNull());

        BooleanBinding affaireBtnBinding = numeroAffaire.textProperty().isEqualTo("")
                .or(dateFormulation.valueProperty().isNull())
                .or(typeDemande.getSelectionModel().selectedItemProperty().isNull()
                        .or(errorNumAffaireLabel.visibleProperty())
                        .or(errorNumReperageLabel.visibleProperty())
                        .or(errorNumJtrLabel.visibleProperty())
                        .or(rechercherRedacteur)
                        .or(rechercherConnexe));

        affaireNextBtn.disableProperty().bind(affaireBtnBinding);

        ReadOnlyObjectProperty<String> typeDemandeSelectItemProperty = typeDemande.getSelectionModel().selectedItemProperty();
        BooleanBinding prescriptionOrAffectation = typeDemandeSelectItemProperty.isEqualTo("PRESCRIPTION_AQUISITIVE").or(typeDemandeSelectItemProperty.isEqualTo("AFFECTATION"));

        connexeBox.disableProperty().bind(sansEmpietementRadio.selectedProperty().or(prescriptionOrAffectation));
        connexeRadio.disableProperty().bind(prescriptionOrAffectation);
    }

    private void initDatePicker() {
        dateFormulation.valueProperty().addListener((observableValue, localDate, t1) -> {
            yearAffairCigle.set(String.valueOf(t1.getYear()));
        });
        initDateAndYearLabelBinding(dateJtr, dateLabelJtr);
        initDateAndYearLabelBinding(dateReperage,dateLabelReperage);
        initDateAndYearLabelBinding(dateOrdonance,dateLabelOrdonance);
        initDatePickerValue();
    }

    private void initDateAndYearLabelBinding(JFXDatePicker datePicker,Label label){
        datePicker.valueProperty().addListener((observableValue, localDate, newLocalDate) ->{
          label.setText("/ "+String.valueOf(newLocalDate.getYear()));
        });
    }

    private void initDatePickerValue(){
        LocalDate now = LocalDate.now();
        dateFormulation.setValue(now);
        dateJtr.setValue(now);
        dateOrdonance.setValue(now);
        dateReperage.setValue(now);
    }

    private void initRadioButton(){
        // Si l'affaire est connexe alors on desactive le formulaire de terrain
        connexeRadio.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1)
               TerrainFromController.getInstance().getTerrainFormPane().setDisable(true);
        });
        sansEmpietementRadio.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1)
                connexeCombobox.setValue(null);
        });
    }

    private void initRedacteurRadio(){
        redacteurBox.disableProperty().bind(Bindings.not(rechercherRedacteurRadio.selectedProperty()));
        moiMemeRadio.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1)
                redactorCombobox.setValue(null);
        });
    }

    private void initRedactorCombobox(){
        redactorCombobox.setCellFactory(userListView -> new DispatchListcell());
        new AutoCompleteCombobox<User>(redactorCombobox, new Predicate<User>(){
            @Override public boolean test(User user){
                String text = redactorCombobox.getEditor().getText();
                if ( user.getNom().toLowerCase().startsWith(text) || user.getPrenom().toLowerCase().startsWith(text))
                    return true;
                else return false;
            }
        },Type.USER);
    }

    private void initNumChecker(){
        new NumeroChecker(numeroAffaire,errorNumAffaireLabel,Type.AFFAIRE);
        new NumeroChecker(numeroReperage,errorNumReperageLabel,Type.REPERAGE);
        new NumeroChecker(numeroJtr,errorNumJtrLabel,Type.JTR);
        new NumeroChecker(numOrdonance,errorNumOrdonanceLabel,Type.ORDONNANCE);
    }

    private void initTypeDemande(){
        // le type du demande
        typeDemande.getItems().addAll("ACQUISITION", "PRESCRIPTION_AQUISITIVE","AFFECTATION");
        typeDemande.setValue("ACQUISITION");
        typeDemande.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if (t1.equals("ACQUISITION")){
                typeAffair.set(cigle);
                DemandeurFormController.getInstance().getTypeDemandeur().setDisable(false);
            }else {
                if (t1.equals("PRESCRIPTION_AQUISITIVE")){
                    typeAffair.set(cigle + " / G");
                    DemandeurFormController.getInstance().getPhysiquePane().toFront();
                    DemandeurFormController.getInstance().getTypeDemandeur().setValue("Personne Physique");
                }else {
                    DemandeurFormController.getInstance().getMoralPane().toFront();
                    DemandeurFormController.getInstance().getTypeDemandeur().setValue("Personne Morale");
                }
                DemandeurFormController.getInstance().getTypeDemandeur().setDisable(true);
            }
        });
    }

    private void initConnexeCombobox(){
        connexeCombobox.setCellFactory(connexAffairForViewListView -> new ConnexeListCell());
        new AutoCompleteCombobox<ConnexAffairForView>(connexeCombobox, new Predicate<ConnexAffairForView>(){
            @Override
            public boolean test(ConnexAffairForView connexAffairForView) {
                String text = connexeCombobox.getEditor().getText();
                if (connexAffairForView.getNomDemandeur().startsWith(text)  ||
                        connexAffairForView.getNumero().startsWith(text) )
                    return true;
                else return false;
            }
        },Type.CONNEXE);
    }

    public void initStatusCombobox(){
        ObservableList<String> strings = FXCollections.observableArrayList("En cours","Suspendu","Terminer","réjété");
        statusCombobox.setCellFactory(stringListView -> new StatusCell());
        statusCombobox.getItems().addAll(strings);
        statusCombobox.setValue(strings.get(0));
    }

    private static String cigle = "TAM-I";
    private static StringProperty typeAffair = new SimpleStringProperty(cigle);
    private static StringProperty yearAffairCigle = new SimpleStringProperty(String.valueOf(LocalDate.now().getYear()));
    private static AffaireFormController affaireFormController;

    // JFXCOMBOBOX
    @FXML private JFXComboBox<String> typeDemande;
    @FXML private JFXComboBox<String> statusCombobox;
    @FXML private JFXComboBox<ConnexAffairForView> connexeCombobox;
    @FXML private JFXComboBox<User> redactorCombobox;
    // TOOGLEGROUP
    @FXML private ToggleGroup observationGroup;
    @FXML private ToggleGroup redactorGroup;
    // JFXRADIONBUTTON
    @FXML private JFXRadioButton sansEmpietementRadio;
    @FXML private JFXRadioButton connexeRadio;
    @FXML private JFXRadioButton pasDeRedacteurRadio;
    @FXML private JFXRadioButton rechercherRedacteurRadio;
    @FXML private JFXRadioButton moiMemeRadio;
    // TEXTFIELD
    @FXML private TextField numeroReperage;
    @FXML private TextField numeroAffaire;
    @FXML private TextField numeroJtr;
    @FXML private TextField numOrdonance;
    // JFXDATEPICKER
    @FXML private JFXDatePicker dateFormulation;
    @FXML private JFXDatePicker dateReperage;
    @FXML private JFXDatePicker dateJtr;
    @FXML private JFXDatePicker dateOrdonance;
    // HBOX
    @FXML private HBox redacteurBox;
    @FXML private HBox connexeBox;
    @FXML private HBox usernameBox;
    @FXML private HBox reperageBox;
    @FXML private HBox jtrBox;
    @FXML private HBox ordonanceBox;
    // LABEL
            // date Label
            @FXML private Label dateLabelJtr;
            @FXML private Label dateLabelReperage;
            @FXML private Label dateLabelOrdonance;
            @FXML private Label numAndDateAffLabel;
            // error
            @FXML private Label errorNumAffaireLabel;
            @FXML private Label errorNumReperageLabel;
            @FXML private Label errorNumJtrLabel;
            @FXML private Label errorNumOrdonanceLabel;
    // JFXBUTTON
    @FXML private JFXButton closeBtn;
    @FXML private JFXButton rechercherRedacteurBtn;
    @FXML private JFXButton affaireNextBtn;
}