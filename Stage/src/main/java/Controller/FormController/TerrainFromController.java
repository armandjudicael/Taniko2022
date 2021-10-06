package Controller.FormController;

import DAO.DaoFactory;
import Model.Enum.Type;
import Model.Other.MainService;
import Model.Pojo.Titre;
import View.Cell.ListCell.TitleCell;
import View.helper.AutoCompleteCombobox;
import View.helper.NumeroChecker;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

public class TerrainFromController implements Initializable{

    @Override public void initialize(URL url, ResourceBundle resourceBundle){
       terrainFromController = this;
       initsuperficieUniteCombobox();
       initTitleCombobox();
       initAllButton();
       initSuperficieSpinner();
       initTypePropriete();
       initNumeroChecker();
       initDistrictAndCommuneAndRegion();
       dateCreation.setValue(LocalDate.now());
       notifLabel.visibleProperty().bind(insererPropriete.selectedProperty());
    }

    private void initAllButton(){
        initClearSupericieBtn();
        initAddSuperficieBtn();
        initBinding();
    }

    private void initNumeroChecker(){
        new NumeroChecker(numeroTitre,existNumTitreLabel,Type.TITRE);
        new NumeroChecker(numeroMorcellement,existNumMorcLabel,Type.MORCELEMENT);
    }

    private void initBinding(){
        // terrain button
        BooleanBinding rechercherTitre = Bindings.and(radioDependant.selectedProperty(),titleCombobox.valueProperty().isNull());
        BooleanBinding insererBooleanBinding = insererPropriete.selectedProperty().and(nomPropriete.textProperty().isNull()).and(numeroTitre.textProperty().isNull());
        BooleanBinding terrainBinding = sise.textProperty().isEqualTo("")
                                        .or(commune.textProperty().isEqualTo(""))
                                        .or(district.textProperty().isEqualTo(""))
                                        .or(region.textProperty().isEqualTo(""))
                                        .and((existNumMorcLabel.visibleProperty()
                                        .or(existNumTitreLabel.visibleProperty()))
                                        .or(rechercherTitre))
                                        .or(insererBooleanBinding);
        saveBtn.disableProperty().bind(terrainBinding);
    }

    private void initDistrictAndCommuneAndRegion() {
        //region
        region.setText(_region);
        //district
        district.setText(_district);
        //commune
        commune.setText(_commune);
    }

    private void initClearSupericieBtn(){
        clearSuperficie.disableProperty().bind(superficieLabel.textProperty().isEmpty());
        clearSuperficie.setOnAction(event -> {
            superficieLabel.setText("");
        });
    }

    private void initSuperficieSpinner(){
        SpinnerValueFactory<Float> spinnerValueFactory = new SpinnerValueFactory<Float>() {
            @Override public void decrement(int i) {
                i--;
            }
            @Override public void increment(int i) {
               i++;
            }
        };
        spinnerValueFactory.setConverter(new StringConverter<Float>() {
            @Override public String toString(Float aFloat) {
                return String.valueOf(aFloat);
            }
            @Override public Float fromString(String s) {
                return Float.valueOf(s);
            }
        });
        spinnerValueFactory.setWrapAround(true);
        spinnerValueFactory.setValue(0.0F);
        superficieSpinner.setValueFactory(spinnerValueFactory);
        superficieSpinner.setEditable(true);
    }

    private void initTypePropriete(){
        nomPropriete.disableProperty().bind(rechercherPropriete.selectedProperty());
        usernameBox1.disableProperty().bind(rechercherPropriete.selectedProperty());
        usernameBox2.disableProperty().bind(rechercherPropriete.selectedProperty());
        numeroTitre.disableProperty().bind(rechercherPropriete.selectedProperty());
        titleCombobox.disableProperty().bind(insererPropriete.selectedProperty());
        proprietePane.disableProperty().bind(Bindings.not(radioDependant.selectedProperty()));
        dateCreation.disableProperty().bind(rechercherPropriete.selectedProperty());
        insererPropriete.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1)
                titleCombobox.setValue(null);
        });
    }

   private void initsuperficieUniteCombobox(){
           // les unité de la superficie doit etre inclus dans le parametre d'administration
           String[] superficieTab = {"Hectare (Ha)", "Are (A)", "CentiAre (Ca)", "mètre carré (m²)", "décimètre carré (dm²)"};
           uniteSuperficieCombobox.getItems().addAll(Arrays.asList(superficieTab));
           // initialize the listener
           uniteSuperficieCombobox.getSelectionModel().selectedItemProperty().addListener((observableValue, s, newValue) -> {
           boolean containsValue = superficieLabel.getText().contains(newValue);
           uniteSuperficieCombobox.getStylesheets().remove("BoxError");
           if (!containsValue)
             superficieUnit = testSelectedvalue(newValue);
           else{
               // disable the textfield when the map contains the selectedItem
               superficieSpinner.setDisable(true);
               uniteSuperficieCombobox.getStylesheets().addAll("BoxError");
           }
       });
   }

   private String testSelectedvalue(String value){
       return  (value == "Hectare (Ha)" ? "Ha":
                     (value == "Are (A)" ? "A" :
                          (value == "CentiAre (Ca)" ? "Ca":
                                ( value == "mètre carré (m²)" ? "m²":
                                        (value == "décimètre carré (dm²)" ? "dm²" :"Ca")))));
   }

   private void initAddSuperficieBtn(){
       BooleanBinding superficieIsNull = superficieSpinner.valueProperty().isNull();
       addSuperficieBtn.disableProperty().bind(superficieIsNull);
       addSuperficieBtn.setOnAction(this::updateSuperficieLabel);
   }

   public void updateSuperficieLabel(ActionEvent event){
       String superficieValue = String.valueOf(superficieSpinner.getValue());;
       superficieSpinner.getEditor().clear();
       String  text = "";
       if (!superficieLabel.getText().isEmpty() && !superficieLabel.getText().isBlank())
           text = superficieValue+" "+superficieUnit;
       else{ text = superficieLabel.getText()+" - "+superficieValue+" "+superficieUnit; }
       superficieLabel.setText(text);
   }

   private void initTitleCombobox(){

        titleBox.disableProperty().bind(insererPropriete.selectedProperty());
        titleCombobox.setCellFactory(titreListView -> new TitleCell());
        new AutoCompleteCombobox<Titre>(titleCombobox, new Predicate<Titre>() {
            @Override public boolean test(Titre titre) {
                String text = titleCombobox.getEditor().getText();
                if (titre.toString().startsWith(text))
                    return true;
                else return false;
            }
        },Type.TITRE);
    }

    public static TerrainFromController getInstance(){
        return terrainFromController;
    }
    public TextField getSise() {
        return sise;
    }
    public TextField getParcelleTerrain() {
        return parcelleTerrain;
    }
    public TextField getParcelleTerrain1() {
        return parcelleTerrain1;
    }
    public TextField getCommune() {
        return commune;
    }
    public TextField getDistrict() {
        return district;
    }
    public TextField getRegion() {
        return region;
    }
    public JFXRadioButton getRadioDependant() {
        return radioDependant;
    }
    public ToggleGroup getTypeTerrain() {
        return typeTerrain;
    }
    public JFXRadioButton getTnicRadio() {
        return tnicRadio;
    }
    public AnchorPane getProprietePane() {
        return proprietePane;
    }
    public JFXRadioButton getInsererPropriete() {
        return insererPropriete;
    }
    public ToggleGroup getTypePropriete() {
        return typePropriete;
    }
    public JFXRadioButton getRechercherPropriete() {
        return rechercherPropriete;
    }
    public TextField getNomPropriete() {
        return nomPropriete;
    }
    public Label getNotifLabel() {
        return notifLabel;
    }
    public HBox getUsernameBox2() {
        return usernameBox2;
    }
    public TextField getNumeroTitre() {
        return numeroTitre;
    }
    public Label getNumLabel2() {
        return numLabel2;
    }
    public HBox getUsernameBox1() {
        return usernameBox1;
    }
    public TextField getNumeroMorcellement() {
        return numeroMorcellement;
    }
    public Label getNumLabel1() {
        return numLabel1;
    }
    public HBox getTitleBox() {
        return titleBox;
    }
    public JFXComboBox<Titre> getTitleCombobox() {
        return titleCombobox;
    }
    public Label getExistNumTitreLabel() {
        return existNumTitreLabel;
    }
    public Label getExistNumMorcLabel() {
        return existNumMorcLabel;
    }
    public ComboBox<String> getUniteSuperficieCombobox() {
        return uniteSuperficieCombobox;
    }
    public Label getSuperficieLabel() {
        return superficieLabel;
    }
    public AnchorPane getTerrainFormPane() {
        return terrainFormPane;
    }

    private static TerrainFromController terrainFromController;
    private static String superficieUnit;
    private static String _region = "ATSINANANA";
    private static String _district = "TOAMASINA-I";
    private static String _commune = " Urbaine de Toamasina ";
    // DATEPICKER
    @FXML private JFXDatePicker dateCreation;
    // ANCHORPANE
    @FXML private AnchorPane proprietePane;
    @FXML private AnchorPane terrainFormPane;
    // RADIOBUTTON
    @FXML private JFXRadioButton radioDependant;
    @FXML private JFXRadioButton tnicRadio;
    @FXML private JFXRadioButton insererPropriete;
    @FXML private JFXRadioButton rechercherPropriete;
    // TOGGLEGROUP
    @FXML private ToggleGroup typeTerrain;
    @FXML private ToggleGroup typePropriete;
    // HBOX
    @FXML private HBox usernameBox2;
    @FXML private HBox usernameBox1;
    @FXML private HBox titleBox;
    // SPINNER
    @FXML private Spinner<Float> superficieSpinner;
    // COMBOBOX
    @FXML private ComboBox<String> uniteSuperficieCombobox;
    @FXML private JFXComboBox<Titre> titleCombobox;
    // LABEL
    @FXML private Label existNumTitreLabel;
    @FXML private Label existNumMorcLabel;
    @FXML private Label numLabel1;
    @FXML private Label numLabel2;
    @FXML private Label notifLabel;
    @FXML private Label superficieErrorLabel;
    @FXML  private Label superficieLabel;
    // TEXTFIELD
    @FXML private TextField numeroMorcellement;
    @FXML private TextField numeroTitre;
    @FXML private TextField nomPropriete;
    @FXML private TextField sise;
    @FXML private TextField parcelleTerrain;
    @FXML private TextField parcelleTerrain1;
    @FXML private TextField commune;
    @FXML private TextField district;
    @FXML private TextField region;
    // BUTTON
    @FXML private JFXButton addSuperficieBtn;
    @FXML private JFXButton clearSuperficie;
    @FXML  private JFXButton saveBtn;
}
