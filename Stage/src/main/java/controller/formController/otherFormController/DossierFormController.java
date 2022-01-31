package controller.formController.otherFormController;
import controller.formController.demandeurController.RechercherDemandeurController;
import model.pojo.business.other.User;
import view.Cell.ListCell.ConnexeListCell;
import view.Cell.ListCell.DispatchListcell;
import view.Cell.ListCell.StatusCell;
import view.Helper.Other.*;
import view.Model.ViewObject.ConnexAffairForView;
import com.jfoenix.controls.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import static controller.formController.otherFormController.MainAffaireFormController.*;

public class DossierFormController implements Initializable, ControllerUtils{

    @Override public void initialize(URL url, ResourceBundle resourceBundle){
        affaireFormPanel.toFront();
        dossierFormController = this;
        initRedacteurRadio();
        initDatePicker();
        initStatusCombobox();
        initRadioButton();
        initRedactorCombobox();
        initConnexeCombobox();
        initBinding();
        initButton();
        initDatePickerValue();
        initBox();
    }

    private void initOtherBinding(){
        // numLabel
        StringExpression stringExpression = new SimpleStringProperty(" - ").concat(typeAffair).concat(" / ").concat(yearAffairCigle);
        numAndDateAffLabel.textProperty().bind(stringExpression);
    }

    @Override
    public void initButton(){
        // BUTTON ACTION
         initNavigationButton();
    }

    private void initNavigationButton(){
        MainAffaireFormController instance = MainAffaireFormController.getInstance();
        // PREV
        Label menuLabel = instance.getMenuLabel();
        Label folderLabel = instance.getFolderLabel();
        folderPrevBtn.setOnAction(event -> FormNavigationButton.executeUpdate(instance.getFormMenuPane(),menuLabel,folderLabel));
        // NEXT
        AnchorPane demandeurFormPanel = instance.getDemandeurFormPane();
        Label demandeurLabel = instance.getDemandeurLabel();
        new FormNavigationButton(folderNextBtn,demandeurLabel,folderLabel,demandeurFormPanel);
    }

    private void initDatePicker() {
        dateFormulation.valueProperty().addListener((observableValue, localDate, t1) -> {
            yearAffairCigle.set(String.valueOf(t1.getYear()));
        });
        initDateAndYearLabelBinding(dateJtr,dateLabelJtr);
        initDateAndYearLabelBinding(dateReperage,dateLabelReperage);
        initDateAndYearLabelBinding(dateOrdonance,dateLabelOrdonance);
        initDatePickerValue();
    }

    @Override public boolean isOk(String result){
        return true;
    }

    @Override public void initBinding() {
      //initButtonBinding();
      initOtherBinding();
    }

    private void initBox(){
        removeErr(numeroAffaire,errorNumAffaireLabel);
        removeErr(numOrdonance,errorNumOrdonanceLabel);
        removeErr(numeroReperage,errorNumReperageLabel);
        removeErr(numeroJtr,errorNumJtrLabel);
    }

    private void removeErr(TextField textField,Label errLabel){
       HBox box = (HBox)textField.getParent();
       textField.setOnMouseEntered(event -> {
            if (errLabel.isVisible()){
                box.getStyleClass().removeAll("boxError");
                errLabel.setVisible(false);
            }
       });
    }

    private void showErrorOn(HBox box,Label errLabel){
        box.getStyleClass().add("boxError");
        errLabel.setVisible(true);
    }

    private void initDateAndYearLabelBinding(JFXDatePicker datePicker,Label label){
        datePicker.valueProperty().addListener((observableValue, localDate, newLocalDate) ->{
          label.setText("/ "+String.valueOf(newLocalDate.getYear()));
        });
    }

    private void initDatePickerValue(){
        dateFormulation.setValue(null);
        dateJtr.setValue(null);
        dateOrdonance.setValue(null);
        dateReperage.setValue(null);
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
                if ( user.getNom().toLowerCase().startsWith(text))
                    return true;
                else return false;
            }
        });
    }

    public void resetAffaireForm(){
        resetReperageAndAffaire();
        resetJtr();
        resetOrdonnance();
        // RESET NAVIGATION
        resetNavigation();
        // RESET COMBOBOX
        RechercherDemandeurController.getInstance().getDemandeurCombobox().setValue(null);
        // RESET REPRESENTANT LISTVIEW ITEM
        RechercherDemandeurController.getInstance().getRepresentantList().getItems().clear();
    }

    private void resetNavigation(){
        MainAffaireFormController instance = MainAffaireFormController.getInstance();
        AnchorPane formMenuPane = instance.getFormMenuPane();
        Label menuLabel = instance.getMenuLabel();
        Label pieceJointeLabel = instance.getPieceJointeLabel();
        FormNavigationButton.executeUpdate(formMenuPane,menuLabel,pieceJointeLabel);
    }

    private void resetReperageAndAffaire(){
        DossierFormController instance = getAffFormInstance();
        instance.getNumeroReperage().clear();
        instance.getDateReperage().setValue(LocalDate.now());
        instance.getNumeroAffaire().clear();
        instance.getDateFormulation().setValue(LocalDate.now());
    }

    private void resetJtr(){
        DossierFormController instance = getAffFormInstance();
        instance.getNumeroJtr().clear();
        instance.getDateJtr().setValue(LocalDate.now());
    }

    private void resetOrdonnance(){
        DossierFormController instance = getAffFormInstance();
        instance.getNumOrdonance().clear();
        instance.getDateOrdonance().setValue(LocalDate.now());
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
        });
    }

    public void initStatusCombobox(){
        ObservableList<String> strings = FXCollections.observableArrayList("En cours","Suspendu","Terminer","réjété");
        statusCombobox.setCellFactory(stringListView -> new StatusCell());
        statusCombobox.getItems().addAll(strings);
        statusCombobox.setValue(strings.get(0));
    }

    public static void setTypeAffair(String typeAffair) {
        DossierFormController.typeAffair.set(typeAffair);
    }

    private static StringProperty typeAffair = new SimpleStringProperty("TAM-I");
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
    public static DossierFormController getInstance() {
        return dossierFormController;
    }
    public AnchorPane getAffaireFormPanel() {
        return affaireFormPanel;
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
    public JFXButton getRechercherRedacteurBtn() {
        return rechercherRedacteurBtn;
    }
    public JFXButton getFolderNextBtn() {
        return folderNextBtn;
    }
    public ScrollPane getAffaireScrollpane() {
        return affaireScrollpane;
    }
    @FXML private ScrollPane affaireScrollpane;
    @FXML private AnchorPane affaireFormPanel;
    // JFXCOMBOBOX
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

    public HBox getNumAffBox() {
        return numAffBox;
    }

    @FXML private HBox numAffBox;
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
    @FXML private JFXButton rechercherRedacteurBtn;
    @FXML private JFXButton folderNextBtn;
    @FXML private JFXButton folderPrevBtn;
    private static StringProperty yearAffairCigle = new SimpleStringProperty(String.valueOf(LocalDate.now().getYear()));
    private static DossierFormController dossierFormController;
}