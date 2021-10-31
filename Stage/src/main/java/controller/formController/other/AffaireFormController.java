package controller.formController.other;
import Model.Enum.Type;
import Model.Pojo.business.User;
import View.Cell.ListCell.ConnexeListCell;
import View.Cell.ListCell.DispatchListcell;
import View.Cell.ListCell.StatusCell;
import View.Dialog.FormDialog.MainAffaireForm;
import View.Model.ViewObject.ConnexAffairForView;
import View.Helper.Other.AutoCompleteCombobox;
import View.Helper.Other.NumeroChecker;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class AffaireFormController  implements Initializable{

    @Override public void initialize(URL url, ResourceBundle resourceBundle){
        affaireFormPanel.toFront();
        affaireFormController = this;
        initRedacteurRadio();
        initTypeDemande();
        initDatePicker();
        initStatusCombobox();
        initRadioButton();
        initRedactorCombobox();
        initConnexeCombobox();
        initOtherBinding();
        initButtonActionAndBinding();
        initNumChecker();
        initDatePickerValue();
        // numLabel
        StringExpression stringExpression = new SimpleStringProperty(" - ").concat(typeAffair).concat(" / ").concat(yearAffairCigle);
        numAndDateAffLabel.textProperty().bind(stringExpression);
    }

    private void initOtherBinding(){
        ReadOnlyObjectProperty<String> typeDemandeSelectItemProperty = typeDemande.getSelectionModel().selectedItemProperty();
        BooleanBinding prescriptionOrAffectation = typeDemandeSelectItemProperty.isEqualTo("PRESCRIPTION_AQUISITIVE").or(typeDemandeSelectItemProperty.isEqualTo("AFFECTATION"));
        connexeBox.disableProperty().bind(sansEmpietementRadio.selectedProperty().or(prescriptionOrAffectation));
        connexeRadio.disableProperty().bind(prescriptionOrAffectation);
    }

    private void initButtonActionAndBinding(){
        BooleanBinding rechercherRedacteur = Bindings.and(Bindings.not(redacteurBox.disableProperty()),redactorCombobox.valueProperty().isNull());
        BooleanBinding rechercherConnexe = Bindings.and(Bindings.not(connexeBox.disableProperty()),connexeCombobox.valueProperty().isNull());
        BooleanBinding affaireBtnBinding = numeroAffaire.textProperty().isEqualTo("")
                .or(dateFormulation.valueProperty().isNull())
                .or(typeDemande.getSelectionModel().selectedItemProperty().isNull()
                        .or(errorNumAffaireLabel.visibleProperty())
                        .or(errorNumReperageLabel.visibleProperty())
                        .or(errorNumJtrLabel.visibleProperty())
                        .or(rechercherRedacteur)
                        .or(rechercherConnexe));
        // BIND THE BUTTON
        affaireNextBtn.disableProperty().bind(affaireBtnBinding);
        // BUTTON ACTION
        affaireNextBtn.setOnAction(event -> MainAffaireFormController.getInstance().updateLabelAndShowPane(affaireNextBtn));
        closeBtn.setOnAction(event -> MainAffaireForm.getInstance().close());
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
            if (t1) TerrainFromController.getInstance().getTerrainFormPane().setDisable(true);
        });
        sansEmpietementRadio.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) connexeCombobox.setValue(null);
        });
    }

    private void initRedacteurRadio(){
        redacteurBox.disableProperty().bind(Bindings.not(rechercherRedacteurRadio.selectedProperty()));
        moiMemeRadio.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) redactorCombobox.setValue(null);
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
            if (t1.equals("ACQUISITION")) typeAffair.set(cigle);
            if (t1.equals("PRESCRIPTION_AQUISITIVE")) typeAffair.set(cigle + "/G");
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
    public static String getCigle() {
        return cigle;
    }
    public static String getTypeAffair() {
        return typeAffair.get();
    }
    public static StringProperty typeAffairProperty() {
        return typeAffair;
    }
    public static String getYearAffairCigle() {
        return yearAffairCigle.get();
    }
    public static StringProperty yearAffairCigleProperty() {
        return yearAffairCigle;
    }
    public static AffaireFormController getInstance() {
        return affaireFormController;
    }
    public AnchorPane getAffaireFormPanel() {
        return affaireFormPanel;
    }
    public JFXComboBox<String> getTypeDemande() {
        return typeDemande;
    }
    public JFXComboBox<String> getStatusCombobox() {
        return statusCombobox;
    }
    public JFXComboBox<ConnexAffairForView> getConnexeCombobox() {
        return connexeCombobox;
    }
    public JFXComboBox<User> getRedactorCombobox() {
        return redactorCombobox;
    }
    public ToggleGroup getObservationGroup() {
        return observationGroup;
    }
    public ToggleGroup getRedactorGroup() {
        return redactorGroup;
    }
    public JFXRadioButton getSansEmpietementRadio() {
        return sansEmpietementRadio;
    }
    public JFXRadioButton getConnexeRadio() {
        return connexeRadio;
    }
    public JFXRadioButton getPasDeRedacteurRadio() {
        return pasDeRedacteurRadio;
    }
    public JFXRadioButton getRechercherRedacteurRadio() {
        return rechercherRedacteurRadio;
    }
    public JFXRadioButton getMoiMemeRadio() {
        return moiMemeRadio;
    }
    public TextField getNumeroReperage() {
        return numeroReperage;
    }
    public TextField getNumeroAffaire() {
        return numeroAffaire;
    }
    public TextField getNumeroJtr() {
        return numeroJtr;
    }
    public TextField getNumOrdonance() {
        return numOrdonance;
    }
    public JFXDatePicker getDateFormulation() {
        return dateFormulation;
    }
    public JFXDatePicker getDateReperage() {
        return dateReperage;
    }
    public JFXDatePicker getDateJtr() {
        return dateJtr;
    }
    public JFXDatePicker getDateOrdonance() {
        return dateOrdonance;
    }
    public HBox getRedacteurBox() {
        return redacteurBox;
    }
    public HBox getConnexeBox() {
        return connexeBox;
    }
    public HBox getUsernameBox() {
        return usernameBox;
    }
    public HBox getReperageBox() {
        return reperageBox;
    }
    public HBox getJtrBox() {
        return jtrBox;
    }
    public HBox getOrdonanceBox() {
        return ordonanceBox;
    }
    public Label getDateLabelJtr() {
        return dateLabelJtr;
    }
    public Label getDateLabelReperage() {
        return dateLabelReperage;
    }
    public Label getDateLabelOrdonance() {
        return dateLabelOrdonance;
    }
    public Label getNumAndDateAffLabel() {
        return numAndDateAffLabel;
    }
    public Label getErrorNumAffaireLabel() {
        return errorNumAffaireLabel;
    }
    public Label getErrorNumReperageLabel() {
        return errorNumReperageLabel;
    }
    public Label getErrorNumJtrLabel() {
        return errorNumJtrLabel;
    }
    public Label getErrorNumOrdonanceLabel() {
        return errorNumOrdonanceLabel;
    }
    public JFXButton getCloseBtn() {
        return closeBtn;
    }
    public JFXButton getRechercherRedacteurBtn() {
        return rechercherRedacteurBtn;
    }
    public JFXButton getAffaireNextBtn() {
        return affaireNextBtn;
    }
    private static StringProperty yearAffairCigle = new SimpleStringProperty(String.valueOf(LocalDate.now().getYear()));
    private static AffaireFormController affaireFormController;

    @FXML private AnchorPane affaireFormPanel;
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