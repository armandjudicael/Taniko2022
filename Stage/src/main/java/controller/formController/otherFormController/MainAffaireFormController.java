package controller.formController.otherFormController;
import com.jfoenix.controls.JFXProgressBar;
import controller.formController.demandeurController.MainDemandeurFormController;
import controller.formController.demandeurController.NouveauDemandeurController;
import controller.formController.demandeurController.RechercherDemandeurController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Enum.TypeDemande;
import model.Enum.TypeDemandeur;
import view.Dialog.FormDialog.MainAffaireForm;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import view.Helper.Other.ControllerUtils;
import view.Helper.Other.FormNavigationButton;
import view.Helper.Other.TextFieldReseterBtn;
import view.Model.Dialog.AlertDialog;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainAffaireFormController implements Initializable,ControllerUtils{

    private void closeDialogForm(ActionEvent event){
        MainAffaireForm instance = MainAffaireForm.getMainAffaireForm();
        if (instance != null) instance.close();
        else {
            Stage stage = (Stage) mainAffBorderPane.getScene().getWindow();
            stage.close();
        }
    }

    public void menuFormButtonAction(ActionEvent actionEvent){
        JFXButton clickedButton = (JFXButton)actionEvent.getSource();
       if (    !clickedButton.equals(acquisitionBtn) &&
               !clickedButton.equals(affectationBtn) &&
               !clickedButton.equals(prescriptionBtn)){
           AlertDialog.getInstance(Alert.AlertType.INFORMATION,"Fonctionnalité non disponible").showDialog();
       }else {
           FormNavigationButton.executeUpdate(folderFormPane, folderLabel,menuLabel);
           if (clickedButton.equals(acquisitionBtn)){
               // Une demande d'acquisition est faite par une personne physique
               typeDemande = TypeDemande.ACQUISITION;
               DossierFormController.setTypeAffair(cigle);
               disableJtr();
           }else{
               if (clickedButton.equals(affectationBtn)){
                   // Une demande d'affectation est faite par une personne morale
                   typeDemande = TypeDemande.AFFECTATION;
                   DossierFormController.setTypeAffair(cigle);
                   disableJtr();
               }else if (clickedButton.equals(prescriptionBtn)){
                   typeDemande = TypeDemande.PRESCRIPTION;
                   DossierFormController.setTypeAffair(cigle+"/G");
                   //  La prescription acquisitive est faite par une personne physique
                   //  et ne concerne que les terrains privé titré
               }
               // desactiver combobox pour les affaires connexe
               DossierFormController.getInstance().getConnexeBox().setDisable(true);
               DossierFormController.getInstance().getConnexeRadio().setDisable(true);
           }
           initDemandeurForm();
       }
    }

    public void typeDemandeurBtnAction(ActionEvent event){
        JFXButton source = (JFXButton)event.getSource();
        Boolean isPersonGroup = (source == groupeUndividuBtn);
        NouveauDemandeurController nouveauDemandeurcontrollerInstance = NouveauDemandeurController.getInstance();
        RechercherDemandeurController rechercherDemandeurControllerInstance = RechercherDemandeurController.getInstance();
        if(source == undividuBtn || isPersonGroup){
            // SHOW LISTVIEW
            if (isPersonGroup) updateSplitPaneDividerTo(INITIAL_POS);
            else updateSplitPaneDividerTo(1.0);
            // SHOW PHYSIQUE FORM
            nouveauDemandeurcontrollerInstance.getPersonneInfo().setText(" INFORMATION DU DEMANDEUR ");
            nouveauDemandeurcontrollerInstance.getPhysiquePane().toFront();
            rechercherDemandeurControllerInstance.getPhysiqueInfo().toFront();
            FormNavigationButton.show(MainAffaireFormController.getInstance().getDemandeurFormPane());
            return;
        }
        // SHOW MORAL FORM
        nouveauDemandeurcontrollerInstance.getMoralPane().toFront();
        rechercherDemandeurControllerInstance.getMoralInfo().toFront();
        // HIDE THE LISTE VIEW
        updateSplitPaneDividerTo(1.0);
        FormNavigationButton.show(MainAffaireFormController.getInstance().getDemandeurFormPane());
    }

    private void updateSplitPaneDividerTo(double value) {
        MainDemandeurFormController instance = MainDemandeurFormController.getInstance();
        instance.getDmdSplitPane().setDividerPosition(0,value);
        instance.getAddApplicantBtn().setDisable( value == INITIAL_POS ? false : true);
    }

    private void disableJtr() {
        DossierFormController.getInstance().getOrdonanceBox().setDisable(true);
        DossierFormController.getInstance().getDateOrdonance().setDisable(true);
    }

    private void initSearchTextField(){
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Node> tilePaneChildren = tilePane.getChildren();
            if (nodeObservableList == null){
                List<JFXButton> jfxButtonList = List.of(acquisitionBtn,
                affectationBtn,
                prescriptionBtn,
                desaffectationBtn,
                hypothequeBtn,
                declassementBtn,
                donnationBtn,
                echangeBtn,
                dotationBtn,
                deceBtn,
                jugementBtn,
                classementBtn,
                        bailBtn,
                dispositionGratuiteBtn,venteBtn);
                nodeObservableList = FXCollections.observableList(jfxButtonList);
            }
            if (!newValue.isBlank()){
                FilteredList<Node> filteredList = tilePaneChildren.filtered(node -> {
                    JFXButton button = (JFXButton) node;
                    if (button.getText().toLowerCase(Locale.ROOT).startsWith(newValue.toLowerCase(Locale.ROOT))) return true;
                    else return false;
                });
                if (!filteredList.isEmpty()) tilePaneChildren.retainAll(filteredList);
                else tilePaneChildren.clear();
            }else resetAndInitList(tilePaneChildren);
        });
        menuDmdSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Node> menuItems = dmdMenuTilePane.getChildren();
            if (typeDmdButtonList==null){
                List<JFXButton> jfxButtonList = List.of(
                        undividuBtn,
                        groupeUndividuBtn,
                        egliseBtn,
                        associationBtn,
                        ongBtn,
                        societeBtn,
                        autreBtn);
                typeDmdButtonList  = FXCollections.observableList(jfxButtonList);
            }
            if (!newValue.isBlank()){
                FilteredList<Node> filtered = menuItems.filtered(node -> {
                    JFXButton button = (JFXButton) node;
                    if (button.getText().toLowerCase().startsWith(newValue.toLowerCase())) return true;
                    else return false;
                });
                if (!filtered.isEmpty()) menuItems.retainAll(filtered);
                else menuItems.clear();
            }else {
                menuItems.clear();
                menuItems.setAll(typeDmdButtonList);
            }
        });
    }

    private void resetAndInitList(ObservableList<Node> tilePaneChildren) {
        tilePaneChildren.clear();
        tilePaneChildren.setAll(nodeObservableList);
    }

    private void initDemandeurForm() {
        switch (typeDemande){
            case ACQUISITION:
            case PRESCRIPTION:{
                typeDemandeur = TypeDemandeur.PERSONNE_PHYSIQUE;
                NouveauDemandeurController.getInstance().getPhysiquePane().toFront();
                RechercherDemandeurController.getInstance().getPhysiqueInfo().toFront();
            }break;
            case AFFECTATION:{
                NouveauDemandeurController.getInstance().getMoralPane().toFront();
                RechercherDemandeurController.getInstance().getMoralInfo().toFront();
            }break;
        }
    }
    public MainAffaireFormController() {
        mainAffaireFormController = this;
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle){
       mainAffaireFormController = this;
        closeBtn.setOnAction(this::closeDialogForm);
       folderFormPane.toFront();
       initButton();
       formMenuPane.toFront();
       initSearchTextField();
    }
    public JFXProgressBar getCheckProgressBar() {
        return checkProgressBar;
    }
    public BorderPane getMainAffBorderPane() {
        return mainAffBorderPane;
    }
    public JFXButton getSaveBtn() {
        return saveBtn;
    }
    @Override public boolean isOk(String result) {
        return false;
    }
    @Override public void initBinding(){
    }
    public static TerrainFromController getTerrainFromInstance() {
        if(terrainFromInstance == null) terrainFromInstance = TerrainFromController.getInstance();
        return terrainFromInstance;
    }

    public static NouveauDemandeurController getNewDemandeurFormInstance(){
        if (newDemandeurFormInstance == null) newDemandeurFormInstance = NouveauDemandeurController.getInstance();
        return newDemandeurFormInstance;
    }

    public static RechercherDemandeurController getSearchDmdFormInstance() {
        if (searchDmdFormInstance == null) searchDmdFormInstance = RechercherDemandeurController.getInstance();
        return searchDmdFormInstance;
    }

    public static DossierFormController getAffFormInstance() {
        if (affFormInstance ==null) affFormInstance = DossierFormController.getInstance();
        return affFormInstance;
    }

    public static MainDemandeurFormController getMainDmdFormInstance(){
        if (mainDmdFormInstance == null) mainDmdFormInstance = MainDemandeurFormController.getInstance();
        return mainDmdFormInstance;
    }

    public static MainAffaireFormController getInstance() {
        return mainAffaireFormController;
    }
    @Override public void initButton(){
        navBtn.setOnAction(this::updateNavigationForm);
        new  TextFieldReseterBtn(deleteButton,searchTextField);
        new TextFieldReseterBtn(typeDmdResetBtn,menuDmdSearchTextField);
        saveBtn.setOnAction(event -> {
            PieceJointeFormController.getInstance().enregistrerAffaire();
        });
        new FormNavigationButton(typeDmdMenuPrevBtn,folderLabel,demandeurLabel,folderFormPane);
        new FormNavigationButton(finalPrevBtn,pieceJointeLabel,validationLabel,piecejointeFormPanel);
    }

    private void updateNavigationForm(ActionEvent event) {
        double boxWidth = navBox.getWidth();
        double xNavPos = navBox.getTranslateX();
        ImageView imageView = (ImageView)navBtn.getGraphic();
        if (xNavPos >= 0){
            navBox.setTranslateX(-boxWidth);
            imageView.setImage(rightChevronImg);
            updatePositionTo(0.0);
        }else{
            imageView.setImage(leftChevronImg);
            navBox.setTranslateX(0);
            updatePositionTo(boxWidth);
        }
    }

    private void updatePositionTo(double boxWidth) {
        AnchorPane.setLeftAnchor(checkProgressBar,boxWidth);
        AnchorPane.setLeftAnchor(stackContainer,boxWidth);
    }

    public TextArea getTextArea() {
        return textArea;
    }
    public static TypeDemande getTypeDemande() {
        return typeDemande;
    }
    public static TypeDemandeur getTypeDemandeur() {
        return typeDemandeur;
    }
    private static TypeDemande typeDemande;
    public Label getFolderLabel() {
        return folderLabel;
    }
    public Label getDemandeurLabel() {
        return demandeurLabel;
    }
    public Label getTerrainLabel() {
        return terrainLabel;
    }
    public Label getPieceJointeLabel() {
        return pieceJointeLabel;
    }
    public Label getValidationLabel() {
        return validationLabel;
    }
    public Label getMenuLabel() {
        return menuLabel;
    }
    public AnchorPane getFolderFormPane() {
        return folderFormPane;
    }
    public AnchorPane getDemandeurFormPane() {
        return demandeurFormPanel;
    }
    public AnchorPane getTerrainFormPane() {
        return terrainFormPanel;
    }
    public AnchorPane getPiecejointeFormPanel() {
        return piecejointeFormPanel;
    }
    public AnchorPane getValidationPane() {
        return validationPanel;
    }
    public AnchorPane getFormMenuPane() {
        return formMenuPane;
    }

    // LABEL
    @FXML private Label folderLabel;
    @FXML private Label demandeurLabel;
    @FXML private Label terrainLabel;
    @FXML private Label pieceJointeLabel;
    @FXML private Label validationLabel;
    @FXML private Label menuLabel;
    @FXML private JFXButton closeBtn;
    // PANE
    @FXML private AnchorPane folderFormPane;
    @FXML private AnchorPane demandeurFormPanel;


    @FXML private AnchorPane terrainFormPanel;
    @FXML private AnchorPane piecejointeFormPanel;
    @FXML private AnchorPane validationPanel;
    // BORDERPANE
    @FXML private  BorderPane mainAffBorderPane;
    // PROGRESSBAR
    @FXML private JFXProgressBar checkProgressBar;
    // BUTTON
    @FXML private JFXButton finalPrevBtn;
    @FXML private JFXButton saveBtn;
    //TEXTAREA
    @FXML private TextArea textArea;
    @FXML private VBox navBox;
    @FXML private JFXButton navBtn;
    @FXML private AnchorPane formMenuPane;
    // TYPE DOSSIER BTN
    @FXML private JFXButton acquisitionBtn;
    @FXML private JFXButton affectationBtn;
    @FXML private JFXButton prescriptionBtn;
    @FXML private JFXButton desaffectationBtn;
    @FXML private JFXButton hypothequeBtn;
    @FXML private JFXButton declassementBtn;
    @FXML private JFXButton donnationBtn;
    @FXML private JFXButton echangeBtn;
    @FXML private JFXButton dotationBtn;
    @FXML private JFXButton deceBtn;
    @FXML private JFXButton jugementBtn;
    @FXML private JFXButton classementBtn;
    @FXML private JFXButton bailBtn;
    @FXML private JFXButton deleteButton;
    @FXML private JFXButton dispositionGratuiteBtn;
    @FXML private JFXButton venteBtn;

    // TYPE DEMANDEUR BTN
    @FXML private JFXButton undividuBtn;
    @FXML private JFXButton groupeUndividuBtn;
    @FXML private JFXButton societeBtn;
    @FXML private JFXButton associationBtn;
    @FXML private JFXButton ongBtn;
    @FXML private JFXButton egliseBtn;
    @FXML private JFXButton autreBtn;

    @FXML private JFXButton typeDmdResetBtn;
    @FXML private JFXButton typeDmdMenuPrevBtn;

    @FXML private StackPane stackContainer;
    @FXML private TextField searchTextField;
    @FXML private TilePane tilePane;
    @FXML private TilePane dmdMenuTilePane;
    @FXML private AnchorPane typeDemandeurPanel;
    @FXML private TextField menuDmdSearchTextField;
    public StackPane getStackContainer() {
        return stackContainer;
    }
    public AnchorPane getTypeDemandeurPanel() {
        return typeDemandeurPanel;
    }
    public JFXButton getAcquisitionBtn() {
        return acquisitionBtn;
    }
    public JFXButton getAffectationBtn() {
        return affectationBtn;
    }
    public JFXButton getPrescriptionBtn() {
        return prescriptionBtn;
    }
    public JFXButton getDesaffectationBtn() {
        return desaffectationBtn;
    }
    public JFXButton getHypothequeBtn() {
        return hypothequeBtn;
    }
    public JFXButton getDeclassementBtn() {
        return declassementBtn;
    }
    public JFXButton getDonnationBtn() {
        return donnationBtn;
    }
    public JFXButton getEchangeBtn() {
        return echangeBtn;
    }
    public JFXButton getDotationBtn() {
        return dotationBtn;
    }
    public JFXButton getDeceBtn() {
        return deceBtn;
    }
    public JFXButton getJugementBtn() {
        return jugementBtn;
    }
    public JFXButton getClassementBtn() {
        return classementBtn;
    }
    public JFXButton getBailBtn() {
        return bailBtn;
    }
    public JFXButton getDispositionGratuiteBtn() {
        return dispositionGratuiteBtn;
    }
    private static TerrainFromController terrainFromInstance;
    private static NouveauDemandeurController newDemandeurFormInstance;
    private static RechercherDemandeurController searchDmdFormInstance;
    private static DossierFormController affFormInstance;
    private static MainDemandeurFormController mainDmdFormInstance;
    private static MainAffaireFormController mainAffaireFormController;
    private static TypeDemandeur typeDemandeur = TypeDemandeur.PERSONNE_MORALE_PUBLIQUE;
    private static String cigle = "TAM-I";
    private static ObservableList<JFXButton> typeDmdButtonList;
    private static ObservableList<JFXButton> nodeObservableList;
    private static Image leftChevronImg = new Image(Objects.requireNonNull(MainAffaireFormController.class.getResourceAsStream("/img/chevron_left_50px.png")));
    private static Image rightChevronImg = new Image(Objects.requireNonNull(MainAffaireFormController.class.getResourceAsStream("/img/chevron_right_50px.png")));
    private final double INITIAL_POS = 0.65;
}
