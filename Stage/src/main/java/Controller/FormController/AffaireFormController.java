package Controller.FormController;

import DAO.DaoFactory;
import Model.Enum.Type;
import Model.Other.MainService;
import Model.Pojo.User;
import View.Cell.ListCell.ConnexeListCell;
import View.Cell.ListCell.DispatchListcell;
import View.Cell.ListCell.StatusCell;
import View.Model.ConnexAffairForView;
import View.helper.AutoCompleteCombobox;
import View.helper.NumeroChecker;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Struct;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class AffaireFormController  implements Initializable {

    @Override public void initialize(URL url, ResourceBundle resourceBundle){
        affaireFormController = this;
        initRedacteurRadio();
        initTypeDemande();
        initDateFormulation();
        initStatusCombobox();
        initRadioButton();
        initRedactorCombobox();
        initConnexeCombobox();
        initBinding();
        initNumChecker();
        // numLabel
        StringExpression stringExpression = new SimpleStringProperty(" - ").concat(typeAffair).concat(" / ").concat(yearAffairCigle);
        numLabel.textProperty().bind(stringExpression);
    }

    private void initBinding(){
        BooleanBinding rechercherRedacteur = Bindings.and(Bindings.not(redacteurBox.disableProperty()), redactorCombobox.valueProperty().isNull());
        BooleanBinding rechercherConnexe = Bindings.and(Bindings.not(connexeBox.disableProperty()), connexeCombobox.valueProperty().isNull());
        BooleanBinding affaireBtnBinding = numero.textProperty().isEqualTo("")
                .or(dateFormulation.valueProperty().isNull())
                .or(typeDemande.getSelectionModel().selectedItemProperty().isNull()
                        .or(existNumLabel.visibleProperty())
                        .or(rechercherRedacteur)
                        .or(rechercherConnexe));
        affaireNextBtn.disableProperty().bind(affaireBtnBinding);
        ReadOnlyObjectProperty<String> typeDemandeSelectItemProperty = typeDemande.getSelectionModel().selectedItemProperty();
        BooleanBinding prescriptionOrAffectation = typeDemandeSelectItemProperty.isEqualTo("PRESCRIPTION_AQUISITIVE").or(typeDemandeSelectItemProperty.isEqualTo("AFFECTATION"));
        connexeBox.disableProperty().bind(sansEmpietementRadio.selectedProperty().or(prescriptionOrAffectation));
        connexeRadio.disableProperty().bind(prescriptionOrAffectation);
    }


    private void initDateFormulation() {
        dateFormulation.setValue(LocalDate.now());
        dateFormulation.valueProperty().addListener((observableValue, localDate, t1) -> {
            yearAffairCigle.set(String.valueOf(t1.getYear()).substring(2));
        });
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
        new NumeroChecker(numero,existNumAffaireLabel,Type.AFFAIRE);
        new NumeroChecker(numeroReperage,existNumReperageLabel,Type.REPERAGE);
        new NumeroChecker(numeroJtr,existNumJtrLabel,Type.JTR);
    }

    private void initTypeDemande(){
        // le type du demande
        typeDemande.getItems().addAll("ACQUISITION", "PRESCRIPTION_AQUISITIVE","AFFECTATION");
        typeDemande.setValue("ACQUISITION");
        typeDemande.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if (t1.equals("ACQUISITION")){
                typeAffair.set(cigle);
            } else typeAffair.set(cigle + " / G");
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
    private static StringProperty yearAffairCigle = new SimpleStringProperty(String.valueOf(LocalDate.now().getYear()).substring(2));
    private static AffaireFormController affaireFormController;

    @FXML private VBox affairePanel;
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
    @FXML private TextField numero;
    @FXML private TextField numeroJtr;
    // JFXDATEPICKER
    @FXML private JFXDatePicker dateFormulation;
    @FXML private JFXDatePicker dateReperage;
    // HBOX
    @FXML private HBox usernameBox11;
    @FXML private HBox redacteurBox;
    @FXML private HBox connexeBox;
    @FXML private HBox usernameBox;
    // LABEL
    @FXML private Label existNumLabel;
    @FXML private Label reperageLabel;
    @FXML private Label numReperageLabel;
    @FXML private Label numLabel;
    @FXML private Label existNumAffaireLabel;
    @FXML private Label existNumReperageLabel;
    @FXML private Label existNumJtrLabel;
    // JFXBUTTON
    @FXML private JFXButton closeBtn;
    @FXML private JFXButton rechercherRedacteurBtn;
    @FXML private JFXButton affaireNextBtn;
}