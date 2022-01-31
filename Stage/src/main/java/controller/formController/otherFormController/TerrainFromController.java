package controller.formController.otherFormController;

import model.pojo.business.other.Propriete;
import view.Cell.ListCell.TitleCell;
import view.Helper.Other.AutoCompleteCombobox;
import com.jfoenix.controls.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import view.Helper.Other.ControllerUtils;
import view.Helper.Other.FormNavigationButton;

import java.net.URL;
import java.util.*;
import java.util.function.Predicate;

import static controller.formController.otherFormController.MainAffaireFormController.getTerrainFromInstance;

public class TerrainFromController implements Initializable, ControllerUtils {

    @Override public void initialize(URL url, ResourceBundle resourceBundle){
       terrainFromController = this;
       initsuperficieUniteCombobox();
       initTitleCombobox();
       initButton();
       initBinding();
       initTypePropriete();
       initDistrictAndCommuneAndRegion();
    }

    public Propriete createTerrain(Boolean isForTextArea){
        TerrainFromController instance = getTerrainFromInstance();
        String superficie = instance.getSuperficieLabel().getText();

        String parcelle = instance.getParcelleTerrain().getText();
        String parcelle1 = instance.getParcelleTerrain1().getText();
        String parcelleTerrain = null;
        if (!parcelle1.isBlank() && !parcelle.isBlank()) {
            parcelleTerrain = parcelle
                    + "/" + parcelle1;
        }
        String quartierTerrain = instance.getSise().getText();
        String district = instance.getDistrict().getText();
        String commune = instance.getCommune().getText();
        String region = instance.getRegion().getText();

        Propriete propriete = new Propriete();
        propriete.setQuartier(quartierTerrain);
        propriete.setCommune(commune);
        propriete.setDistrict(district);
        propriete.setParcelle(parcelleTerrain);
        propriete.setRegion(region);
        propriete.setSuperficie(superficie);
        propriete.setTitreDependant(initPropriete(isForTextArea));

        return propriete;
    }

    private Propriete initPropriete(Boolean isForTexteArea ){
        if (getRadioDependant().isSelected()) {
            Propriete titre = getTitleCombobox().getValue();
            return titre;
        }
        return null;
    }

    public void resetTerrain(){
        getSuperficieLabel().setText("");
        getParcelleTerrain().clear();
        getParcelleTerrain1().clear();
        getSise().clear();
        if (radioDependant.isSelected())
            getTitleCombobox().setValue(null);
        getTerrainScrollPane().setVvalue(0.0);
    }


    @Override
    public boolean isOk(String result) {
        return false;
    }

    @Override
    public void initBinding(){
        titleBox.disableProperty().bind(Bindings.not(radioDependant.selectedProperty()));
        // terrain button
        BooleanBinding rechercherTitre = Bindings.and(radioDependant.selectedProperty(),titleCombobox.valueProperty().isNull());
        BooleanBinding terrainBinding = sise.textProperty().isEqualTo("")
                                        .or(commune.textProperty().isEqualTo(""))
                                        .or(district.textProperty().isEqualTo(""))
                                        .or(region.textProperty().isEqualTo(""))
                                        .and(rechercherTitre);
        terrainNextBtn.disableProperty().bind(terrainBinding);
    }

    @Override public void initButton() {
        initClearSupericieBtn();
        initAddSuperficieBtn();
        initNavigationButton();
    }

    private void initNavigationButton() {
        MainAffaireFormController instance = MainAffaireFormController.getInstance();
        // PREV
        AnchorPane demandeurFormPane = instance.getDemandeurFormPane();
        Label demandeurLabel = instance.getDemandeurLabel();
        // NEXT
        Label pieceJointeLabel = instance.getPieceJointeLabel();
        Label terrainLabel = instance.getTerrainLabel();
        AnchorPane piecejointeFormPane = instance.getPiecejointeFormPanel();
        new FormNavigationButton(terrainNextBtn, pieceJointeLabel, terrainLabel,piecejointeFormPane);
        new FormNavigationButton(terrainPrevBtn,demandeurLabel,terrainLabel,demandeurFormPane);
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
        clearSuperficie.setOnAction(event -> superficieLabel.setText(""));
    }


    private void initTypePropriete(){
        titleCombobox.disableProperty().bind(radioDependant.selectedProperty());
    }

   private void initsuperficieUniteCombobox(){
           // les unité de la superficie doit etre inclus dans le parametre d'administration
           String[] superficieTab = {"Hectare (Ha)", "Are (A)", "CentiAre (Ca)", "mètre carré (m²)", "décimètre carré (dm²)"};
           uniteSuperficieCombobox.getItems().addAll(Arrays.asList(superficieTab));
   }



   private String convert(String value){
       return  (value == "Hectare (Ha)" ? "Ha":
                     (value == "Are (A)" ? "A" :
                          (value == "CentiAre (Ca)" ? "Ca":
                                ( value == "mètre carré (m²)" ? "m²":
                                        (value == "décimètre carré (dm²)" ? "dm²" :"Ca")))));
   }

   private void initAddSuperficieBtn(){
       BooleanBinding superficieIsEmptyOrUnitIsEmpty = superficieTextfield.textProperty().isEmpty().or(uniteSuperficieCombobox.valueProperty().isNull());
       addSuperficieBtn.disableProperty().bind(superficieIsEmptyOrUnitIsEmpty);
       addSuperficieBtn.setOnAction(this::updateSuperficieLabel);
   }

   public void updateSuperficieLabel(ActionEvent event){
        String superficieUnit = convert(uniteSuperficieCombobox.getValue());
        if(!superficieLabel.getText().contains(superficieUnit)){
            String superficieValue = superficieTextfield.getText();
            superficieTextfield.clear();
            String  text = "";
            if (superficieLabel.getText().isEmpty() || superficieLabel.getText().isBlank())
                text = superficieValue+" "+superficieUnit;
            else{ text = superficieLabel.getText()+" - "+superficieValue+" "+superficieUnit; }
            superficieLabel.setText(text);
        }
   }

   private void initTitleCombobox(){
        titleCombobox.setCellFactory(titreListView -> new TitleCell());
        new AutoCompleteCombobox<Propriete>(titleCombobox, new Predicate<Propriete>() {
            @Override public boolean test(Propriete titre) {
                String text = titleCombobox.getEditor().getText();
                if (titre.toString().startsWith(text))
                    return true;
                else return false;
            }
        });
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
    public HBox getTitleBox() {
        return titleBox;
    }
    public JFXComboBox<Propriete> getTitleCombobox() {
        return titleCombobox;
    }
    public ComboBox<String> getUniteSuperficieCombobox() {
        return uniteSuperficieCombobox;
    }
    public Label getSuperficieLabel() {
        return superficieLabel;
    }
    public AnchorPane getTerrainFormPane() {
        return terrainFormPanel;
    }
    public ScrollPane getTerrainScrollPane() { return terrainScrollPane; }
    private static TerrainFromController terrainFromController;

    private static String _region = "ATSINANANA";
    private static String _district = "TOAMASINA-I";
    private static String _commune = " Urbaine de Toamasina ";

    // SCROLLPANE
    @FXML private  ScrollPane terrainScrollPane;
    // ANCHORPANE
    @FXML private AnchorPane terrainFormPanel;
    // RADIOBUTTON
    @FXML private JFXRadioButton radioDependant;
    @FXML private JFXRadioButton tnicRadio;
    // TOGGLEGROUP
    @FXML private ToggleGroup typeTerrain;
    // HBOX
    @FXML private HBox titleBox;
    // COMBOBOX
    @FXML private ComboBox<String> uniteSuperficieCombobox;
    @FXML private JFXComboBox<Propriete> titleCombobox;
    @FXML private Label superficieErrorLabel;
    @FXML  private Label superficieLabel;
    // TEXTFIELD
    @FXML private TextField sise;
    @FXML private TextField parcelleTerrain;
    @FXML private TextField parcelleTerrain1;
    @FXML private TextField commune;
    @FXML private TextField district;
    @FXML private TextField region;
    @FXML private TextField superficieTextfield;
    // BUTTON
    @FXML private JFXButton addSuperficieBtn;
    @FXML private JFXButton clearSuperficie;
    @FXML private  JFXButton terrainPrevBtn;
    @FXML private  JFXButton terrainNextBtn;
}
