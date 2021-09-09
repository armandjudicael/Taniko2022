package View.Dialog.FormDialog;

import Controller.FormController.LoginController;
import Controller.ViewController.AffairViewController;
import Controller.ViewController.MainController;
import DAO.DaoFactory;
import DAO.DbOperation;
import Model.Enum.*;
import Model.Pojo.*;
import Model.other.ProcedureForTableview;
import Model.serviceManager.MainService;
import View.Cell.ListCell.ConnexeListCell;
import View.Cell.ListCell.DispatchListcell;
import View.Cell.ListCell.TitleCell;
import View.Dialog.Other.Notification;
import View.Model.AffaireForView;
import View.Model.ConnexAffairForView;
import View.helper.AutoCompleteCombobox;
import animatefx.animation.FadeIn;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.skin.ComboBoxBaseSkin;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static Model.Pojo.Affaire.string2TypeDemande;

public class AffaireForm extends JFXDialog implements Initializable {

    private static AffaireForm affaireForm;
    private static String cigle = "TAM-I";
    private static String _region = "ATSINANANA";
    private static String _district = "TOAMASINA-I";
    private static String _commune = " Urbaine de Toamasina ";
    private static String cigleTitre = "-BA";
    private static Pattern pattern = Pattern.compile("^[0-9]{1,5}$");
    private static AffaireStatus status = AffaireStatus.RUNNING ;

    private static StringProperty typeAffair = new SimpleStringProperty(cigle);
    private static StringProperty yearAffairCigle = new SimpleStringProperty(String.valueOf(LocalDate.now().getYear()).substring(2));

    @FXML private DatePicker dateCreation;
    @FXML private JFXSpinner btnSpinner;
    @FXML private ImageView btnImageView;
    @FXML private JFXButton closeBtn1;
    // panneaux
    @FXML private VBox terrainPanel;
    @FXML private VBox affairePanel;
    @FXML private VBox observationPanel;
    @FXML private ComboBox<String> typeDemande;
    // superficie
    @FXML private TextField hectare;
    @FXML private TextField are;
    @FXML private TextField centiAre;
    // TEXTFIELD
    @FXML private TextField nom;
    @FXML private TextField prenom;
    @FXML private TextField adresse;
    @FXML private TextField demandeurParcelle1;
    @FXML private TextField demandeurParcelle2;
    @FXML private TextField lot;
    @FXML private TextField telephone;
    @FXML private TextField mail;
    @FXML private TextField numero;
    @FXML private TextField sise;
    @FXML private TextField parcelleTerrain;
    @FXML private TextField parcelleTerrain1;
    @FXML private TextField commune;
    @FXML private TextField district;
    @FXML private TextField region;
    // LABEL
    @FXML private Label affaireLabel;
    @FXML private Label TerrainLabel;
    @FXML private Label observationLabel;
    @FXML private Label numLabel;
    @FXML private ToggleGroup typeTerrain;
    @FXML private DatePicker dateFormulation;
    // BACK
    @FXML private JFXButton back2AffBtn;
    @FXML private JFXButton back2ObsBtn;
    // GO
    @FXML private JFXButton go2ObsBtn;
    @FXML private JFXButton go2FieldBtn;
    // CONNEXE_VIEW_BTN
    @FXML private JFXButton saveBtn;
    @FXML private JFXButton closeBtn;
    @FXML private JFXButton refreshBtn;

    @FXML private ComboBox<String> statusCombobox;
    @FXML private JFXSpinner propertySpinner;

    @FXML private ToggleGroup typePropriete;
    @FXML private JFXRadioButton rechercherPropriete;
    @FXML private TextField nomPropriete;
    @FXML private TextField numeroTitre;
    @FXML private AnchorPane proprietePane;
    @FXML private Label notifLabel;
    @FXML private JFXSpinner redactorSpinner;
    @FXML private TextField numeroMorcellement;

    @FXML private Label centiAreLabel;
    @FXML private Label areLabel;
    @FXML private Label hectareLabel;

    @FXML private HBox usernameBox;
    @FXML private HBox connexeBox;
    @FXML private HBox titleBox;
    @FXML private HBox redacteurBox;
    @FXML private HBox usernameBox2;
    @FXML private HBox usernameBox1;

    @FXML private Label existNumLabel;
    @FXML private Label existNumTitreLabel;
    @FXML private Label existNumMorcLabel;
    @FXML private Label invalidSuperficie;



    @FXML private ToggleGroup observationGroup;
    @FXML private JFXSpinner connexeSpinner;

    @FXML private ToggleGroup redactorGroup;
    @FXML private JFXButton rechercherRedacteurBtn;
    @FXML private AnchorPane terrainFormPanel;

    // RADIO BUTTON
    @FXML private JFXRadioButton sansEmpietementRadio;
    @FXML private JFXRadioButton connexeRadio;
    @FXML private JFXRadioButton rechercherRedacteurRadio;
    @FXML private JFXRadioButton moiMemeRadio;
    @FXML private JFXRadioButton insererPropriete;
    @FXML private JFXRadioButton radioDependant;
    @FXML private JFXRadioButton tnicRadio;
    @FXML private JFXRadioButton pasDeRedacteurRadio;

    // COMBOBOX
    @FXML private JFXComboBox<User> redactorCombobox;
    @FXML private JFXComboBox<ConnexAffairForView> connexeCombobox;
    @FXML private JFXComboBox<Titre> titleCombobox;


    private void initRedacteurRadio(){
        redacteurBox.disableProperty().bind(Bindings.not(rechercherRedacteurRadio.selectedProperty()));
        moiMemeRadio.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1)
                redactorCombobox.setValue(null);
        });
    }

    private void initSuperficieLabel() {
        centiAreLabel.visibleProperty().bind(centiAre.focusedProperty().or(centiAre.textProperty().isNotNull()));
        areLabel.visibleProperty().bind(are.focusedProperty().or(are.textProperty().isNotNull()));
        hectareLabel.visibleProperty().bind(hectare.focusedProperty().or(hectare.textProperty().isNotNull()));
    }

    private void initBox(){
        BooleanBinding prescription_aquisitive = typeDemande.getSelectionModel().selectedItemProperty().isEqualTo("PRESCRIPTION_AQUISITIVE");
        connexeBox.disableProperty().bind(sansEmpietementRadio.selectedProperty().
                or(prescription_aquisitive));
        titleBox.disableProperty().bind(insererPropriete.selectedProperty());
        connexeRadio.disableProperty().bind(prescription_aquisitive);
        sansEmpietementRadio.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1)
                connexeCombobox.setValue(null);
        });
    }

    private void initTitleCombobox(){
        titleCombobox.setCellFactory(titreListView -> new TitleCell());
        new AutoCompleteCombobox<Titre>(titleCombobox, new Predicate<Titre>() {
            @Override
            public boolean test(Titre titre) {
                String text = titleCombobox.getEditor().getText();
                if (titre.toString().startsWith(text))
                    return true;
                else return false;
            }
        },AutoCompleteComboboxType.TITRE);
    }

    private void initRedactorCombobox() {

        redactorCombobox.setCellFactory(userListView -> new DispatchListcell());
        new AutoCompleteCombobox<User>(redactorCombobox, new Predicate<User>(){
            @Override public boolean test(User user){
                String text = redactorCombobox.getEditor().getText();
                if ( user.getNom().toLowerCase().startsWith(text) || user.getPrenom().toLowerCase().startsWith(text))
                    return true;
                else return false;
            }
        },AutoCompleteComboboxType.USER);

    }

    private void initConnexeCombobox(){
        connexeCombobox.setCellFactory(connexAffairForViewListView -> new ConnexeListCell());
        new AutoCompleteCombobox<ConnexAffairForView>(connexeCombobox, new Predicate<ConnexAffairForView>() {
            @Override
            public boolean test(ConnexAffairForView connexAffairForView) {
                String text = connexeCombobox.getEditor().getText();
                if (connexAffairForView.getNomDemandeur().startsWith(text)  ||
                        connexAffairForView.getNumero().startsWith(text) )
                    return true;
                else return false;
            }
        },AutoCompleteComboboxType.CONNEXE);
    }

    public static AffaireForm getInstance() {
        if (affaireForm == null)
            affaireForm = new AffaireForm();
        return affaireForm;
    }

    private AffaireForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Form/AffairForm.fxml"));
            loader.setController(this);
            this.setContent((BorderPane) loader.load());
            this.setDialogContainer(MainController.getInstance().getMainStackPane());
            this.setTransitionType(DialogTransition.CENTER);
            this.setOverlayClose(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSuperficie(TextField hectare, TextField are, TextField centiAre) {
        String superficie = "";
        if (centiAre.getText().isEmpty()){
            if (are.getText().isEmpty()) {
                if (!hectare.getText().isEmpty())
                    superficie = hectare.getText() + " Ha";
            } else if (!are.getText().isEmpty()) {
                if (!hectare.getText().isEmpty())
                    superficie = hectare.getText() + " Ha-" + are.getText() + " A";
                else superficie = are.getText() + " A";
            }
        } else if (are.getText().isEmpty()) {
            if (centiAre.getText().isEmpty()) {
                if (!hectare.getText().isEmpty())
                    superficie = hectare.getText() + " Ha";
            } else if (!centiAre.getText().isEmpty()){
                if (!hectare.getText().isEmpty())
                    superficie = hectare.getText() + " Ha-" + centiAre.getText() + " Ca";
                else superficie = centiAre.getText() + " Ca";
            }
        } else if (hectare.getText().isEmpty()){
            if (centiAre.getText().isEmpty()){
                if (!are.getText().isEmpty())
                    superficie = are.getText() + " A";
            } else if (!centiAre.getText().isEmpty()) {
                if (!are.getText().isEmpty())
                    superficie = are.getText() + " A-" + centiAre.getText() + " Ca";
                else superficie = centiAre.getText() + " Ca";
            }
        } else if (!hectare.getText().isEmpty() && !are.getText().isEmpty() && !centiAre.getText().isEmpty())
            superficie = hectare.getText() + " Ha-" + are.getText() + " A-" + centiAre.getText() + " Ca";
        return superficie;

    }

    @Override public void initialize(URL location, ResourceBundle resources){
        initSuperficie();
        affairePanel.toFront();
        initRedacteurRadio();
        initTypePropriete();
        initAffaireForm();
        initBox();
        initCombobox();
        initTypeDemande();
        initDateFormulation();
        initStatus(statusCombobox);
        initBtnAction();
        initButtonBinding();
        checkAllNumero();
        terrainFormPanel.disableProperty().bind(connexeRadio.selectedProperty());
        dateCreation.setValue(LocalDate.now());
        dateFormulation.setEditable(false);
        dateCreation.setEditable(false);
        // numLabel
        StringExpression stringExpression = new SimpleStringProperty(" - ").concat(typeAffair).concat(" / ").concat(yearAffairCigle);
        numLabel.textProperty().bind(stringExpression);
        notifLabel.visibleProperty().bind(insererPropriete.selectedProperty());
    }


    private void initCombobox(){
        initTitleCombobox();
        initConnexeCombobox();
        initRedactorCombobox();
    }

    private void initBtnAction(){
        // enregistrer affaire
        saveBtn.setOnAction(event -> {
            this.close();
            MainService.getInstance().launch(new Task<Void>() {
                @Override protected Void call() throws Exception {
                    sauvegarderAffaire();
                    return null;
                }

                @Override protected void succeeded() {
                    TerrainLabel.getStyleClass().removeAll("navLabel");
                    affaireLabel.getStyleClass().add("navLabel");
                    resetAffairForm();
                }
            });
        });
        //annuler
        closeBtn.setOnAction(event -> this.close());
        closeBtn1.setOnAction(event -> this.close());
        // go2ObsView
        go2ObsBtn.setOnAction(this::go2ObservationView);
        // go2FieldView
        go2FieldBtn.setOnAction(this::go2FieldView);
        //back2Obsview
        back2ObsBtn.setOnAction(this::back2ObservationView);
        // back2affview
        back2AffBtn.setOnAction(this::back2Affairview);
    }

    private void sauvegarderAffaire(){

        Demandeur demandeur = new Demandeur(nom.getText(), prenom.getText(),adresse.getText(), demandeurParcelle1.getText() + "/" + demandeurParcelle1.getText(), lot.getText());
        // enregistrement du demandeur
        if (DaoFactory.getDemandeurDao().create(demandeur) != 0) {

            int idDemandeur = DaoFactory.getDemandeurDao().getIdDemandeurByFullName(nom.getText(),prenom.getText());
            demandeur.setId(idDemandeur);

            Timestamp dateFormulation = Timestamp.valueOf(LocalDateTime.of(AffaireForm.this.dateFormulation.getValue(), LocalTime.now()));

            String numeroAffaire = numero.getText()+numLabel.getText();

            User redacteur= initRedateur();

            Terrain terrain = initTerrain();

            AffaireForView affairForView = new AffaireForView(numeroAffaire,
                    dateFormulation,
                    string2TypeDemande(typeDemande.getValue()),
                    redacteur ,
                    status,
                    demandeur,
                    terrain,
                    new ProcedureForTableview(ProcedureStatus.NONE, new SimpleStringProperty("Auccune procedure")));

                 // ENREGISTRER AFFAIRE
                 if (DaoFactory.getAffaireDao().create(affairForView) != 0){

                 int idAffaire = DaoFactory.getAffaireDao().getAffaireIdByNum(affairForView.getNumero());
                 affairForView.setId(idAffaire);

                 // INSERTION DANS LA TABLE DE DISPATCH
                if (redacteur!=null){
                    DbOperation.insertOnDispatchTable(redacteur,affairForView);
                }

                Platform.runLater(() -> {
                    // mis a jour de l'affichage
                    AffairViewController.getInstance().getTableView().getItems().add(affairForView);
                    Notification.getInstance(" L'affaire N° "+ numeroAffaire +" est enregistrer avec succès ", NotifType.SUCCESS).show();
                });

            }
        }
    }

    private User initRedateur(){

        if (moiMemeRadio.isSelected()){
            return LoginController.getConnectedUser();
        }else if (rechercherRedacteurRadio.isSelected()){
            User value = redactorCombobox.getValue();
            if (value!=null)
               return value;
        }

        return null;
    }

    private Terrain initTerrain(){

        if (connexeRadio.isSelected()){
            ConnexAffairForView value = connexeCombobox.getValue();
            Terrain terrain1 = DaoFactory.getTerrainDao().findById(value.getIdTerrain());
             if (terrain1!=null)
                 return terrain1;
        }else {

            Terrain terrain = new Terrain();
            String superficie = getSuperficie(hectare, are, centiAre);
            String parcelle = parcelleTerrain.getText() + "/" + parcelleTerrain1.getText();
            String quertier = sise.getText();
            String districtTerrain = district.getText();
            String communeTerrrain = commune.getText();

            Titre titre = initTitre();

            terrain.setSuperficie(superficie);
            terrain.setTitreDependant(titre);
            terrain.setParcelle(parcelle);
            terrain.setQuartier(quertier);
            terrain.setDistrict(districtTerrain);
            terrain.setRegion(region.getText());
            terrain.setCommune(communeTerrrain);

            if ( DaoFactory.getTerrainDao().create(terrain) == 1 ){

                 int idTerrain = DaoFactory.getTerrainDao().getIdTerrainBy(superficie,parcelle,quertier);
                  terrain.setIdTerrain(idTerrain);
                 if (titre!=null)
                      DbOperation.insertOnTerrrainTitre(terrain, TableName.DEPEND_TERRRAIN_TITRE);
                 return terrain;
            }
        }

        return null;
    }

    private Titre initTitre() {

        if (radioDependant.isSelected()){
            if (rechercherPropriete.isSelected()) {
               Titre titre = titleCombobox.getValue();
               return titre;
            }else{
                Titre titre = new Titre();
                titre.setNomPropriete(nomPropriete.getText());
                titre.setNumero(numeroTitre.getText()+cigleTitre);
                titre.setNumMorcelement(numeroMorcellement.getText());
                Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(dateCreation.getValue(), LocalTime.now()));
                titre.setDate(timestamp);

                if (DaoFactory.getTitreDao().create(titre) != 0) {

                    int id = DaoFactory.getTitreDao().findByNum(titre.getNumero());
                    titre.setId(id);
                    Platform.runLater(() -> Notification.getInstance("Titre enregistré ave succés", NotifType.SUCCESS).show());
                }
                return titre;
            }
        }

        return null;
    }

    private void initNumTitre(){

        numeroTitre.textProperty().addListener((observableValue, s, num) -> {
            if (num.isEmpty()){
                showInputError((HBox) numeroTitre.getParent(),existNumTitreLabel,false);
                return;
            }
            if (pattern.matcher(num).matches()){
                MainService.getInstance().launch(new Task<Void>() {
                    @Override protected void scheduled() {
                        super.scheduled();
                    }
                    @Override protected Void call() throws Exception {
                        if (!DaoFactory.getTitreDao().checkNumTitle(num+cigleTitre))
                            Platform.runLater(() ->{
                                showInputError((HBox) numeroTitre.getParent(),existNumTitreLabel,true);
                            });
                        else Platform.runLater(() ->{
                            showInputError((HBox) numeroTitre.getParent(),existNumTitreLabel,false);
                        } );
                        return null;
                    }
                });
            }else {
                showInputError((HBox) numeroTitre.getParent(),existNumTitreLabel,true);
            }
        });

    }

    private void initNumeroAffair(){
        // verifier si le numero correspond deja
        numero.textProperty().addListener((observableValue, s, num) -> {

            if (num.isEmpty()){
                showInputError((HBox) numero.getParent(),existNumLabel,false);
                return;
            }

            if (pattern.matcher(num).matches()){
                MainService.getInstance().launch(new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        if (!num.isEmpty()) {
                            String numAffair = num + numLabel.getText();
                            Boolean aBoolean = DaoFactory.getAffaireDao().checkNumAffair(numAffair);
                            if (!aBoolean){
                                Platform.runLater(() ->{
                                    showInputError((HBox) numero.getParent(),existNumLabel,true);
                                });
                            }else {
                                Platform.runLater(() ->{
                                    showInputError((HBox) numero.getParent(),existNumLabel,false);
                                });
                            }
                        }
                        return null;
                    }
                });

            }else {

                showInputError(usernameBox,existNumLabel,true);

            }
        });
    }

    private void showInputError(HBox box,Label label,Boolean visible){

        if (visible){
            label.setVisible(visible);
            box.getStyleClass().add("boxError");
        }else {
            label.setVisible(visible);
            box.getStyleClass().removeAll("boxError");
        }

    }

    private void initSuperficie(){
        initSuperficieLabel();
        bindSuperficie(hectare);
        bindSuperficie(are);
        bindSuperficie(centiAre);
    }

    private void bindSuperficie( TextField textField ){

        textField.textProperty().addListener((observableValue, s, value) -> {
            if (value.isEmpty()) {
                showInputError((HBox)textField.getParent(),invalidSuperficie,false);
                return;
            }
            showInputError((HBox)textField.getParent(),invalidSuperficie,false);
            if (!pattern.matcher(value).matches()){
                showInputError((HBox)textField.getParent(),invalidSuperficie,true);
            }
        });
    }

    private void initNumMorcelement(){

        numeroMorcellement.textProperty().addListener((observableValue, s, num) -> {
            if (num.isEmpty()){
                showInputError((HBox) numeroMorcellement.getParent(),existNumMorcLabel,false);
                return;
            }
            if (pattern.matcher(num).matches()){
                MainService.getInstance().launch(new Task<Void>(){
                    @Override protected Void call() throws Exception {
                        if (!DaoFactory.getTitreDao().checkNumMorcelement(num+cigleTitre))
                            Platform.runLater(() ->{
                                showInputError((HBox) numeroMorcellement.getParent(),existNumMorcLabel,true);
                            });
                        else Platform.runLater(() -> {
                            showInputError((HBox) numeroMorcellement.getParent(),existNumMorcLabel,false);
                        });
                        return null;
                    }
                });
            }else {
                showInputError((HBox) numeroMorcellement.getParent(),existNumMorcLabel,true);
            }
        });
    }

    private void checkAllNumero(){
        initNumMorcelement();
        initNumeroAffair();
        initNumTitre();
    }

    private void initAffaireForm() {
        affaireForm = this;
        //region
        region.setText(_region);
        //district
        district.setText(_district);
        //commune
        commune.setText(_commune);
    }

    private void initDateFormulation() {
        dateFormulation.setValue(LocalDate.now());
        dateFormulation.valueProperty().addListener((observableValue, localDate, t1) -> {
            yearAffairCigle.set(String.valueOf(t1.getYear()).substring(2));
        });
    }

    private void initTypePropriete() {

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

    private void initTypeDemande() {
        // le type du demande
        typeDemande.getItems().addAll("ACQUISITION", "PRESCRIPTION_AQUISITIVE");
        typeDemande.setValue("ACQUISITION");
        typeDemande.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if (t1.equals("ACQUISITION")) {
                typeAffair.set(cigle);
            } else typeAffair.set(cigle + " / G");
        });
    }

    private class SituationCell extends ListCell<Label>{
        @Override protected void updateItem(Label label, boolean b) {
            if (b && label!=null){
                setGraphic(label);
                setText(null);
                this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }else {
                setText(null);
                setGraphic(null);
            }
        }
    }

    public void initStatus(ComboBox<String> statusCombobox){
        statusCombobox.getSelectionModel().selectedItemProperty().addListener((observableValue, label, text) -> {
            if (text.equals("En cours")) status = AffaireStatus.RUNNING;
            else if (text.equals("Suspendu")) status = AffaireStatus.SUSPEND;
            else if (text.equals("Terminer")) status = AffaireStatus.SUCCEED;
            else if (text.equals("réjété")) status = AffaireStatus.REJECTED;
        });
        ObservableList<String> strings = FXCollections.observableArrayList("En cours","Suspendu","Terminer","réjété");
        statusCombobox.getItems().addAll(strings);
        statusCombobox.setValue(strings.get(0));
    }

    void go2ObservationView(ActionEvent event){
        new FadeIn(observationPanel).play();
        observationPanel.toFront();
        affaireLabel.getStyleClass().removeAll("navLabel");
        observationLabel.getStyleClass().add("navLabel");
    }

    void go2FieldView(ActionEvent event) {
        new FadeIn(terrainPanel).play();
        terrainPanel.toFront();
        observationLabel.getStyleClass().removeAll("navLabel");
        TerrainLabel.getStyleClass().add("navLabel");
    }

    void back2Affairview(ActionEvent event) {
        new FadeIn(affairePanel).play();
        affairePanel.toFront();
        observationLabel.getStyleClass().removeAll("navLabel");
        affaireLabel.getStyleClass().add("navLabel");
    }

    void back2ObservationView(ActionEvent event) {
        new FadeIn(observationPanel).play();
        observationPanel.toFront();
        TerrainLabel.getStyleClass().removeAll("navLabel");
        observationLabel.getStyleClass().add("navLabel");
    }

    private void initButtonBinding(){

        // affaire button
        BooleanBinding rechercherRedacteur = Bindings.and(Bindings.not(redacteurBox.disableProperty()), redactorCombobox.valueProperty().isNull());

        BooleanBinding rechercherConnexe = Bindings.and(Bindings.not(connexeBox.disableProperty()), connexeCombobox.valueProperty().isNull());

        BooleanBinding affaireBtnBinding = numero.textProperty().isEqualTo("")
                        .or(dateFormulation.valueProperty().isNull())
                        .or(typeDemande.getSelectionModel().selectedItemProperty().isNull()
                        .or(existNumLabel.visibleProperty())
                        .or(rechercherRedacteur)
                        .or(rechercherConnexe));

        go2ObsBtn.disableProperty().bind(affaireBtnBinding);

        // terrain button
        BooleanBinding rechercherTitre = Bindings.and(radioDependant.selectedProperty(),titleCombobox.valueProperty().isNull());

        BooleanBinding insererBooleanBinding = insererPropriete.selectedProperty().and(nomPropriete.textProperty().isNull()).and(numeroTitre.textProperty().isNull());

        BooleanBinding terrainBinding = sise.textProperty().isEqualTo("")
                        .or(commune.textProperty().isEqualTo(""))
                        .or(district.textProperty().isEqualTo(""))
                        .or(region.textProperty().isEqualTo(""))
                        .or(hectare.textProperty().isEmpty().and(are.textProperty().isEmpty()).and(centiAre.textProperty().isEmpty())).and(Bindings.not(connexeRadio.selectedProperty())
                        .or(existNumMorcLabel.visibleProperty()
                                .or(existNumTitreLabel.visibleProperty()))
                        .or(rechercherTitre)
                        .or(invalidSuperficie.visibleProperty()))
                .or(insererBooleanBinding);

        BooleanBinding demandeurBinding = nom.textProperty().isNull().or(nom.textProperty().isEqualTo(""));
        go2FieldBtn.disableProperty().bind(demandeurBinding);
        saveBtn.disableProperty().bind(terrainBinding);
    }

    public void resetAffairForm(){

        this.typeDemande.setValue("ACQUISITION");
        this.affairePanel.toFront();
        this.numero.clear();
        this.dateFormulation.setValue(LocalDate.now());
        this.sise.clear();
        this.parcelleTerrain.clear();
        this.parcelleTerrain1.clear();
        this.hectare.clear();
        this.are.clear();
        this.centiAre.clear();
        this.demandeurParcelle2.clear();
        this.demandeurParcelle1.clear();
        this.nom.clear();
        this.prenom.clear();
        this.adresse.clear();
        this.lot.clear();

        if (insererPropriete.isSelected()){
            nomPropriete.clear();
            numeroTitre.clear();
            numeroMorcellement.clear();
            dateCreation.setValue(LocalDate.now());
        }else if (rechercherPropriete.isSelected()){
            titleCombobox.setValue(null);
        }

    }

    public VBox getAffairePanel() {
        return affairePanel;
    }
    public TextField getNom() {
        return nom;
    }
    public TextField getPrenom() {
        return prenom;
    }
    public TextField getAdresse() {
        return adresse;
    }
    public TextField getDemandeurParcelle1() {
        return demandeurParcelle1;
    }
    public TextField getLot() {
        return lot;
    }
    public TextField getNumero() {
        return numero;
    }
    public DatePicker getDateFormulation() {
        return dateFormulation;
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

}
