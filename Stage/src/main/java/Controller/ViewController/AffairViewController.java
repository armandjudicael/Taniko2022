package Controller.ViewController;

import Controller.detailsController.*;
import DAO.DaoFactory;
import Main.InitializeApp;
import Model.Enum.AffaireStatus;
import Model.Enum.NotifType;
import Model.Enum.Origin;
import Model.Enum.TypeDemande;
import Model.Pojo.*;
import Model.Other.ProcedureForTableview;
import Model.Other.MainService;
import View.Cell.TableCell.IconCell;
import View.Dialog.FormDialog.MainAffaireForm;
import View.Dialog.Other.Notification;
import View.Dialog.SecurityDialog.AdminSecurity;
import View.Model.*;
import View.helper.CheckboxTooltip;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.*;

import static Model.Pojo.Affaire.typeDemande2String;

public class AffairViewController implements Initializable {

    private static ObservableList<ProcedureForView> procedureTab[];
    private static AffairViewController affairViewController = null;

    private static MenuItem detailsMenuItem;
    private static MenuItem deleteMenuItem;
    private static ContextMenu contextMenu;

    private static Image runningImg;
    private static Image suspendImg;
    private static Image finishedImg;
    private static Image rejectImg;

    private static Image goImg;
    private static Image backImg;
    private static Image noneImg;

    private final int ACQUISITION_AND_AFFECTATION_INDEX = 0;
    private final int PRESCRIPTION_INDEX = 1;

    @FXML private JFXButton launchSearchBtn;
    @FXML private JFXButton createAffBtn;
    @FXML private JFXButton delTextfield;
    @FXML private TextField searchInput;
    @FXML private JFXButton detailsBtn;
    @FXML private JFXButton deleteBtn;
    @FXML private JFXButton printBtn;
    @FXML private JFXButton refreshBtn;
    // FILTER
    @FXML private JFXButton finishBtn;
    @FXML private JFXButton suspendBtn;
    @FXML private JFXButton runningBtn;
    @FXML private JFXButton allBtn;
    @FXML private JFXButton rejectBtn;
    // TABLEVIEW ET TABLECOLUMN
    @FXML private TableView<AffaireForView> tableView;
    @FXML private TableColumn<AffaireForView, String> numero;
    @FXML private TableColumn<AffaireForView, String> demandeur;
    @FXML private TableColumn<AffaireForView, String> nomPropriete;
    @FXML private TableColumn<AffaireForView, String> numeroTitre;
    @FXML private TableColumn<AffaireForView, String> superficie;
    @FXML private TableColumn<AffaireForView, String> redactor;
    @FXML private TableColumn<AffaireForView, Label> situation;
    @FXML private TableColumn<AffaireForView, String> type;
    @FXML private TableColumn<AffaireForView, ImageView> status;
    @FXML private AnchorPane searchPanel;
    @FXML private HBox actionPanel;
    @FXML private TitledPane titledPane;
    @FXML private ComboBox<String> yearFilter;
    @FXML private ComboBox<String> viewType;
    @FXML private HBox buttonBox;
    @FXML private JFXButton searchPanelBtn;
    @FXML private JFXButton actionPanelBtn;
    @FXML private JFXCheckBox match;

    public void initializeDetailsData(Affaire affaire) {
        AffairDetailsController.setAffaire(affaire);
        // initialisation de la liste des redacteurs
        initializeEditorTableView(affaire);
        // initialisation de la liste des affaires connexe
        if (affaire.getTypeDemande().equals(TypeDemande.ACQUISITION))
            initializeConnexeTableView(affaire);
        // initialisation de la vue avec les données de l'affaire
        initializeDetailsView(affaire);
        // initialisation de la liste des procedures concerné par l'affaires
        initializeProcedureTableView(affaire);
        // INITIALIZATION DE LA LISTE DES PIECES JOINTE CONCERNANT L'AFFAIRE
        initAttachement(affaire);
    }

    private void initAttachement(Affaire affaire){
        ObservableList<PieceJointeForView> allPieceJointe = DaoFactory.getPieceJointeDao().getAllPieceJointe(affaire);
        PieceJointeViewController.getInstance().getPjTilepane().getChildren().setAll(allPieceJointe);
    }

    private void initImage() {
        runningImg = new Image(Objects.requireNonNull(AffairViewController.class.getResourceAsStream("/img/play1_20px.png")));
        suspendImg = new Image(Objects.requireNonNull(AffairViewController.class.getResourceAsStream("/img/pause_20px.png")));
        finishedImg = new Image(Objects.requireNonNull(AffairViewController.class.getResourceAsStream("/img/ok_20px.png")));
        rejectImg = new Image(Objects.requireNonNull(AffairViewController.class.getResourceAsStream("/img/cancel_20px.png")));

        goImg = new Image(Objects.requireNonNull(AffairViewController.class.getResourceAsStream("/img/go1.png")));
        backImg = new Image(Objects.requireNonNull(AffairViewController.class.getResourceAsStream("/img/back.png")));
        noneImg = new Image(Objects.requireNonNull(AffairViewController.class.getResourceAsStream("/img/none.png")));
    }

    @Override public void initialize(URL location, ResourceBundle resources) {
        new CheckboxTooltip("rechercher dans la liste courante ou global",match);
        affairViewController = this;
        initButton();
        initImage();
        initSearch();
        initializeTableView();
        initializeNavigationBar();
        initializeFilter();
        initializeContextMenu();
        titledPane.setExpanded(true);
    }

    private void initSearch() {
        delTextfield.visibleProperty().bind(searchInput.textProperty().isNotEqualTo("").or(searchInput.textProperty().isNotEmpty()));
        delTextfield.setOnAction(event -> searchInput.setText(""));
    }

    private void initButton(){

        launchSearchBtn.setOnAction(event -> {
            MainService.getInstance().launch(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    AffaireForView affairBy = DaoFactory.getAffaireDao().getAffairByDemandeurNameOrNum(searchInput.getText());
                    if (affairBy != null)
                        Platform.runLater(() -> tableView.getItems().setAll(affairBy));
                    else {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText(" Auccun affaire ne correspond a " + searchInput.getText());
                            alert.show();
                        });
                    }
                    return null;
                }

                @Override protected void scheduled() {
                    launchSearchBtn.disableProperty().unbind();
                    launchSearchBtn.setDisable(true);
                }
                @Override protected void succeeded() {
                    launchSearchBtn.disableProperty().unbind();
                    launchSearchBtn.setDisable(false);
                    launchSearchBtn.disableProperty().bind(searchInput.textProperty().isEmpty());
                }
            });
        });
        printBtn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, " fonctionnalité non disponible ");
            alert.showAndWait();
        });

        launchSearchBtn.disableProperty().bind(searchInput.textProperty().isEmpty());

        refreshBtn.setOnAction(event -> {

            ObservableList<String> items = yearFilter.getItems();

            MainService.getInstance().launch(new Task<Void>() {
                @Override protected Void call() throws Exception{
                    ObservableList<AffaireForView> observableList = DaoFactory.getAffaireDao().getAffaireBy(yearFilter.getValue());
                    if (!items.isEmpty()){
                        int size = items.size();
                        int max = Integer.valueOf(items.get(0));
                        int min = Integer.valueOf(items.get(size-1));
                        ObservableList<String> dateList = DaoFactory.getAffaireDao().groupeAffaireByDate(min,max);
                        if (!dateList.isEmpty())
                            Platform.runLater(() ->{
                                items.addAll(dateList);
                            });
                    }

                    Platform.runLater(() -> {
                        tableView.getItems().setAll(observableList);
                    });

                    InitializeApp.getAffaires().setAll(observableList);
                    return null;
                }
                @Override protected void scheduled() {
                    refreshBtn.setDisable(true);
                }
                @Override protected void succeeded() {
                    refreshBtn.setDisable(false);
                }
            });
        });
        // CREATE NEW AFFAIR
        createAffBtn.setOnAction(event -> MainAffaireForm.getInstance().show());
        deleteBtn.setOnAction(this::getAllAction);
        // BOUTTON ALL_AFF_DETAILS_VIEW_BTN
        detailsBtn.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
        detailsBtn.setOnAction(this::getAllAction);
    }

    @FXML void statusFilterAction(ActionEvent event) {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<AffaireForView> affairForViews = FXCollections.observableArrayList();
                if (event.getSource() == finishBtn) {
                    affairForViews = DaoFactory.getAffaireDao().getAllAffairWhereStatusIs(AffaireStatus.SUCCEED);
                } else if (event.getSource() == runningBtn) {
                    affairForViews = DaoFactory.getAffaireDao().getAllAffairWhereStatusIs(AffaireStatus.RUNNING);
                } else if (event.getSource() == suspendBtn) {
                    affairForViews = DaoFactory.getAffaireDao().getAllAffairWhereStatusIs(AffaireStatus.SUSPEND);
                } else if (event.getSource() == rejectBtn) {
                    affairForViews = DaoFactory.getAffaireDao().getAllAffairWhereStatusIs(AffaireStatus.REJECTED);
                } else if (event.getSource() == allBtn) {
                    affairForViews = InitializeApp.getAffaires();
                }
                ObservableList<AffaireForView> finalAffairForViews = affairForViews;
                Platform.runLater(() -> {
                    tableView.getItems().setAll(finalAffairForViews);
                });
                return null;
            }
        };
        MainService.getInstance().launch(task);
    }

    // CONTEXTUAL MENU
    private void initializeContextMenu() {
        contextMenu = new ContextMenu();
        // details
        detailsMenuItem = new MenuItem("Details");
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/img/eye_20px.png")));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        detailsMenuItem.setGraphic(imageView);
        detailsMenuItem.setOnAction(this::getAllAction);
        //supprimer
        deleteMenuItem = new MenuItem("Supprimer");
        ImageView imageView1 = new ImageView(new Image(getClass().getResourceAsStream("/img/delete_folder_20px.png")));
        imageView1.setPreserveRatio(true);
        imageView1.setFitWidth(20);
        imageView1.setFitHeight(20);

        deleteMenuItem.setGraphic(imageView1);
        deleteMenuItem.setOnAction(this::getAllAction);
        contextMenu.getItems().addAll(detailsMenuItem, deleteMenuItem);
    }

    @Deprecated public void getAllAction(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if ((source == detailsBtn) || (source == detailsMenuItem)) {
            AdminSecurity.show(Origin.SHOW_AFFAIR_DETAILS);
        } else if ((source == deleteBtn) || (source == deleteMenuItem))
            AdminSecurity.show(Origin.DELETE_AFFAIR);
    }

    public void deleteAffair(){

        AffaireForView selectedItem = tableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Etes-vous sur de vouloir supprimer l'affaire " + selectedItem.getNumero(), ButtonType.OK, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.isPresent()) {
            if (buttonType.get().equals(ButtonType.OK)) {
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        if (selectedItem != null) {
                            if (DaoFactory.getAffaireDao().deleteById(selectedItem) == 1) {
                                Platform.runLater(() -> {
                                    Notification.getInstance(" L'affaire N° " + selectedItem.getNumero() + " est supprimé avec succès ", NotifType.SUCCESS).show();
                                    tableView.getItems().remove(selectedItem);
                                });
                            }
                        }
                        return null;
                    }
                };
                MainService.getInstance().launch(task);
            }
        }
    }

    private void initializeViewType(){
        viewType.setItems(FXCollections.observableArrayList("Toutes", "Mes affaires uniquement"));
        viewType.setValue("Toutes");
        viewType.getSelectionModel().selectedItemProperty().addListener((observableValue, toggle, t1) -> {
            MainService.getInstance().launch(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    ObservableList<AffaireForView> views = FXCollections.observableArrayList();
                    if (t1.equals("Toutes")) {
                        views = DaoFactory.getAffaireDao().getAffaireBy(yearFilter.getValue());
                    } else {
                        User connectedUser = LoginController.getConnectedUser();
                        views = DaoFactory.getAffaireDao().getAffairWhereActualEditorIs(connectedUser);
                    }
                    ObservableList<AffaireForView> finalViews = views;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            tableView.getItems().setAll(finalViews);
                        }
                    });
                    return null;
                }
                @Override protected void scheduled() {
                    viewType.setDisable(true);
                }
                @Override protected void succeeded() {
                    viewType.setDisable(false);
                }
            });
        });

    }

    private void initializeNavigationBar() {

        actionPanelBtn.setOnAction(event -> {
            if (!titledPane.isExpanded())
                titledPane.setExpanded(true);
            actionPanel.toFront();
        });

        searchPanelBtn.setOnAction(event -> {
            if (!titledPane.isExpanded())
                titledPane.setExpanded(true);
            searchPanel.toFront();
        });

        titledPane.setOnMouseEntered(event -> {
            searchPanelBtn.arm();
            actionPanelBtn.arm();
        });

        buttonBox.setTranslateX(-25);
    }

    private void initializeSearchTextField(){

        searchInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if(match.isSelected()){
                ObservableList<AffaireForView> items = tableView.getItems();
                if (!newValue.isBlank() && newValue != null){
                    List<AffaireForView> views = items.filtered(affairForView -> {
                        String lowerCaseFilter = newValue.toLowerCase();
                        if (affairForView.getNumero().toLowerCase().contains(lowerCaseFilter))
                            return true;
                        else if (affairForView.getDemandeur().getNom().toLowerCase().contains(lowerCaseFilter))
                            return true;
                        else if (affairForView.getDemandeur().getPrenom().toLowerCase().contains(lowerCaseFilter))
                            return true;
                        else return false;
                    });
                    if (!views.isEmpty())
                        items.retainAll(views);
                    else {
                        items.setAll(InitializeApp.getAffaires());
                    }
                } else {
                    items.setAll(InitializeApp.getAffaires());
                }
            }
        });
    }

    private void initializeYearFilter(){
        MainService.getInstance().launch(new Task<Void> () {

            @Override protected Void call() throws Exception {
                ObservableList<String> observableList = DaoFactory.getAffaireDao().groupeAffaireByDate();
                yearFilter.getItems().setAll(observableList);
                if (!observableList.isEmpty())
                    yearFilter.setValue(observableList.get(0));
                return null;
            }

        });
        yearFilter.getSelectionModel().selectedItemProperty().addListener(this::yearFilterChanged);
    }
    private void initializeTableView() {
        tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, affairForView, t1) -> {
            if (t1 != null) {
                if (t1.getTypeDemande().equals(TypeDemande.PRESCRIPTION))
                    ConnexViewController.getInstance().getConnexePanel().setDisable(true);
                else ConnexViewController.getInstance().getConnexePanel().setDisable(false);
            }
        });
        tableView.setRowFactory(new Callback<TableView<AffaireForView>, TableRow<AffaireForView>>() {
            @Override
            public TableRow<AffaireForView> call(TableView<AffaireForView> param) {
                final TableRow<AffaireForView> tableRow = new TableRow<>();
                tableRow.contextMenuProperty().bind(
                        Bindings.when(
                                Bindings.isNotNull(param.itemsProperty())).
                                then(contextMenu).
                                otherwise((ContextMenu) null));
                return tableRow;
            }
        });
        // numero
        numero.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AffaireForView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<AffaireForView, String> param) {
                return new ObservableValue<String>() {
                    @Override
                    public void addListener(ChangeListener<? super String> listener) {
                    }

                    @Override
                    public void removeListener(ChangeListener<? super String> listener) {
                    }

                    @Override
                    public String getValue() {
                        return param.getValue().getNumero();
                    }

                    @Override
                    public void addListener(InvalidationListener listener) {

                    }

                    @Override
                    public void removeListener(InvalidationListener listener) {

                    }
                };
            }
        });
        // demandeur
        demandeur.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AffaireForView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<AffaireForView, String> param) {
                return new ObservableValue<String>() {
                    @Override
                    public void addListener(ChangeListener<? super String> listener) {
                    }

                    @Override
                    public void removeListener(ChangeListener<? super String> listener) {
                    }

                    @Override
                    public String getValue() {
                        Demandeur demandeur = param.getValue().getDemandeur();
                        String fullName = demandeur.getNom() + " " + demandeur.getPrenom();
                        return fullName;
                    }

                    @Override
                    public void addListener(InvalidationListener listener) {

                    }

                    @Override
                    public void removeListener(InvalidationListener listener) {

                    }
                };
            }
        });
        //nom du propriete dependant
        nomPropriete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AffaireForView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<AffaireForView, String> param) {
                return new ObservableValue<String>() {
                    @Override
                    public void addListener(ChangeListener<? super String> listener) {
                    }

                    @Override
                    public void removeListener(ChangeListener<? super String> listener) {
                    }

                    @Override
                    public String getValue() {
                        final Titre titreDependant = param.getValue().getTerrain().getTitreDependant();
                        if (titreDependant != null) {
                            return titreDependant.getNomPropriete();
                        } else return "";
                    }

                    @Override
                    public void addListener(InvalidationListener listener) {

                    }

                    @Override
                    public void removeListener(InvalidationListener listener) {

                    }
                };
            }
        });
        // numero titre
        numeroTitre.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AffaireForView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<AffaireForView, String> param) {
                return new ObservableValue<String>() {

                    @Override public void addListener(ChangeListener<? super String> listener) { }
                    @Override public void removeListener(ChangeListener<? super String> listener) { }
                    @Override public String getValue() {
                        Titre titre = param.getValue().getTerrain().getTitreDependant();
                        if (titre != null)
                            return titre.getNumero();
                        else return "";
                    }

                    @Override
                    public void addListener(InvalidationListener listener) {

                    }

                    @Override
                    public void removeListener(InvalidationListener listener) {

                    }
                };
            }
        });
        // superficie
        superficie.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AffaireForView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<AffaireForView, String> param) {
                return new ObservableValue<String>() {
                    @Override
                    public void addListener(ChangeListener<? super String> listener) {

                    }

                    @Override
                    public void removeListener(ChangeListener<? super String> listener) {

                    }

                    @Override
                    public String getValue() {
                        final String superficie = param.getValue().getTerrain().getSuperficie();
                        return superficie;
                    }

                    @Override
                    public void addListener(InvalidationListener listener) {

                    }

                    @Override
                    public void removeListener(InvalidationListener listener) {

                    }
                };
            }
        });
        // redacteur
        redactor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AffaireForView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<AffaireForView, String> param) {
                return new ObservableValue<String>() {
                    @Override
                    public void addListener(ChangeListener<? super String> listener) {

                    }

                    @Override
                    public void removeListener(ChangeListener<? super String> listener) {

                    }

                    @Override
                    public String getValue() {
                        User redacteur = param.getValue().getRedacteur();
                        if (redacteur == null)
                            return "Pas de redacteur";
                        else return redacteur.getFullName();
                    }

                    @Override
                    public void addListener(InvalidationListener listener) {

                    }

                    @Override
                    public void removeListener(InvalidationListener listener) {

                    }
                };
            }
        });
        // situation
        situation.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AffaireForView, Label>, ObservableValue<Label>>() {
            @Override
            public ObservableValue<Label> call(TableColumn.CellDataFeatures<AffaireForView, Label> param) {
                return new ObservableValue<Label>() {
                    @Override
                    public void addListener(ChangeListener<? super Label> listener) {

                    }

                    @Override
                    public void removeListener(ChangeListener<? super Label> listener) {

                    }

                    @Override
                    public Label getValue() {

                        ProcedureForTableview procedure = param.getValue().getProcedure();
                        Label label = new Label(procedure.getDescription());
                        label.setWrapText(true);
                        label.setGraphicTextGap(10);
                        switch (procedure.getStatus()) {
                            case GO: {
                                label.setGraphic(new ImageView(goImg));
                            }
                            break;
                            case BACK: {
                                label.setGraphic(new ImageView(backImg));
                            }
                            break;
                            case NONE: {
                                label.setGraphic(new ImageView(noneImg));
                            }
                            break;
                        }
                        return label;
                    }

                    @Override
                    public void addListener(InvalidationListener listener) {
                    }

                    @Override
                    public void removeListener(InvalidationListener listener) {
                    }
                };
            }
        });
        // type
        type.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AffaireForView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<AffaireForView, String> param) {
                return new ObservableValue<String>() {
                    @Override
                    public void addListener(ChangeListener<? super String> changeListener) {

                    }

                    @Override
                    public void removeListener(ChangeListener<? super String> changeListener) {

                    }

                    @Override
                    public String getValue() {
                        return param.getValue().getTypeDemande().getLablel();
                    }

                    @Override
                    public void addListener(InvalidationListener invalidationListener) {

                    }

                    @Override
                    public void removeListener(InvalidationListener invalidationListener) {

                    }
                };
            }
        });
        // status
        status.setCellFactory(param -> new IconCell());
        status.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AffaireForView, ImageView>, ObservableValue<ImageView>>() {
            @Override
            public ObservableValue<ImageView> call(TableColumn.CellDataFeatures<AffaireForView, ImageView> param) {
                return new ObservableValue<ImageView>() {
                    @Override
                    public void addListener(ChangeListener<? super ImageView> listener) {

                    }

                    @Override
                    public void removeListener(ChangeListener<? super ImageView> listener) {

                    }

                    @Override
                    public ImageView getValue() {
                        AffaireForView value = param.getValue();
                        return getStatusIcon(value.getStatus());
                    }

                    @Override
                    public void addListener(InvalidationListener listener) {

                    }

                    @Override
                    public void removeListener(InvalidationListener listener) {

                    }
                };
            }
        });
    }
    public ImageView getStatusIcon(AffaireStatus status) {
        switch (status) {
            case SUCCEED: {
                return new ImageView(AffairViewController.getFinishedImg());
            }
            case RUNNING: {
                return new ImageView(AffairViewController.getRunningImg());
            }
            case SUSPEND: {
                return new ImageView(AffairViewController.getSuspendImg());
            }
            case REJECTED: {
                return new ImageView(AffairViewController.getRejectImg());
            }
            default:
                return new ImageView(AffairViewController.getRunningImg());
        }
    }
    private void yearFilterChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue != null) {
            MainService.getInstance().launch(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    ObservableList<AffaireForView> affairForViews = DaoFactory.getAffaireDao().getAffaireBy(newValue);
                    Platform.runLater(() -> tableView.getItems().setAll(affairForViews));
                    InitializeApp.getAffaires().setAll(affairForViews);
                    return null;
                }
                @Override protected void scheduled() {
                    yearFilter.setDisable(true);
                }
                @Override protected void succeeded() {
                    yearFilter.setDisable(false);
                }
            });
        }
    }
    private void initializeFilter() {
        initializeViewType();
        initializeYearFilter();
        initializeSearchTextField();
    }
    // ALL_AFF_DETAILS_VIEW_BTN INITIALISATION
    private void initializeEditorTableView(Affaire affaire) {
        ArrayList<EditorForView> arrayList = affaire.getAllEditor();
        SimpleStringProperty tab = new SimpleStringProperty("redacteur (" + String.valueOf(arrayList.toArray().length) + ")");
        EditorViewController.getInstance().getEditorTableView().getItems().setAll(arrayList);
        Platform.runLater(() -> {
            AffairDetailsController.getInstance().getRedacteurTab().textProperty().bind(tab);
        });
    }

    public void showDetails() {
        // afficher le panneau
        Affaire affaire = AffairViewController.getInstance().getTableView().getSelectionModel().getSelectedItem();
        MainController.getInstance().showAffaireDetailsView();
        show(affaire);
    }

    public void show(Affaire affaire) {
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // VERIFIER SI L'AFFAIRE EST DEJA OUVERT
                Affaire detailsAffaire = AffairDetailsController.getAffaire();
                if (detailsAffaire == null){
                    initializeDetailsData(affaire);
                }else if(detailsAffaire.getId()!=affaire.getId()){
                    initializeDetailsData(affaire);
                }
                return null;
            }
        });
    }

    private void initializeProcedureTableView(Affaire affaire) {

        TableView<ProcedureForView> procedureTableView = ProcedureViewController.getInstance().getProcedureTableView();
        ObservableList<ProcedureForView> items = procedureTableView.getItems();
        TypeDemande typeDemande = affaire.getTypeDemande();
        if (procedureTab == null)
            procedureTab = new ObservableList[2];
        if (typeDemande.equals(TypeDemande.ACQUISITION)) {
            if (procedureTab[ACQUISITION_AND_AFFECTATION_INDEX] == null)
                procedureTab[ACQUISITION_AND_AFFECTATION_INDEX] = DaoFactory.getProcedureDao().getAllProcedureFromBD("A");
            items.setAll(procedureTab[ACQUISITION_AND_AFFECTATION_INDEX]);
        } else {
            if (procedureTab[PRESCRIPTION_INDEX] == null)
                procedureTab[PRESCRIPTION_INDEX] = DaoFactory.getProcedureDao().getAllProcedureFromBD("P");
            items.setAll(procedureTab[PRESCRIPTION_INDEX]);
        }
        initAffaireProcedure(affaire, items);
    }

    public void initAffaireProcedure(Affaire affaire, ObservableList<ProcedureForView> itemList) {

        ObservableList<ArrayList<String>> viewArrayList = affaire.getAllProcedureChecked();

        itemList.forEach(procedureForView -> {
            Platform.runLater(() -> procedureForView.setChecked(false));
            procedureForView.setNumDepart(null);
            procedureForView.setDateArrive(null);
            procedureForView.setNumArrive(null);
            procedureForView.setDateDepart(null);
        });

        if (viewArrayList.size() > 0) {
            itemList.forEach(procedureForView -> {
                viewArrayList.forEach(item -> {
                    String idProcedure = item.get(0);
                    String numDepart = item.get(1);
                    String dateDepart = item.get(2);
                    String numArrive = item.get(3);
                    String dateArrive = item.get(4);
                    if (procedureForView.getIdProcedure() == Integer.parseInt(idProcedure)) {
                        Platform.runLater(() -> procedureForView.setChecked(true));
                        procedureForView.setNumDepart(numDepart);
                        procedureForView.setDateDepart(dateDepart);
                        procedureForView.setNumArrive(numArrive);
                        procedureForView.setDateArrive(dateArrive);
                    }
                });
            });
        }
    }

    private void initializeDetailsView(Affaire affaire) {

        Demandeur demandeur = DaoFactory.getDemandeurDao().findDemandeurBy(affaire.getId());
        affaire.setDemandeur(demandeur);
        Terrain terrain = DaoFactory.getTerrainDao().find(affaire.getTerrain().getIdTerrain());

        Platform.runLater(() -> {
            // afficher le numero de l'affaire selectionner
            AffairDetailsController.getInstance().getSelectedAffaire().setText(affaire.getNumero());
            AffairDetailsController controller = AffairDetailsController.getInstance();
            controller.getNumeroAffaire().setText(affaire.getNumero());
            controller.getDateFromulation().setText(affaire.getDateDeFormulation().toString());
            controller.getTypeDemande().setText(typeDemande2String(affaire.getTypeDemande()));
            controller.getNom().setText(demandeur.getNom());
            controller.getPrenom().setText(demandeur.getPrenom());
            controller.getAdresse().setText(demandeur.getAdresse());
            controller.getParcelleDemandeur().setText(demandeur.getParcelle());
            controller.getLot().setText(demandeur.getLot());
            controller.getSise().setText(terrain.getQuartier());
            controller.getParcelleTerrain().setText(terrain.getParcelle());
            controller.getCommune().setText(terrain.getCommune());
            controller.getDistrict().setText(terrain.getDistrict());
            controller.getRegion().setText(terrain.getRegion());
            controller.getSuperficie().setText(terrain.getSuperficie());
            Titre titreDependant = terrain.getTitreDependant();

            if (titreDependant!=null){
                controller.getNomPropriete().setText(titreDependant.toString());
            }
            Label statusLabel = controller.getStatusLabel();
            System.out.println(affaire.getStatus());
            switch (affaire.getStatus()) {
                case SUCCEED: {
                    statusLabel.setGraphic(new ImageView(finishedImg));
                    statusLabel.setText(" Terminé ");
                }
                break;
                case RUNNING: {
                    statusLabel.setGraphic(new ImageView(runningImg));
                    statusLabel.setText(" En cours");
                }
                break;
                case SUSPEND: {
                    statusLabel.setGraphic(new ImageView(suspendImg));
                    statusLabel.setText(" Suspendu ");
                }
                break;
                case REJECTED: {
                    statusLabel.setGraphic(new ImageView(rejectImg));
                    statusLabel.setText(" Réjété ");
                }
                break;
            }
        });
    }

    private void initializeConnexeTableView(Affaire affaire){
        ObservableList<ConnexAffairForView> connexAffairForViews = affaire.getAllAffaireConnexe();
        // Binder le nombre d'affaire connexe sur l'entete avec le nombre de ligne de la listView
        SimpleStringProperty tab = new SimpleStringProperty("connexe (" + String.valueOf(connexAffairForViews.size()) + ")");
        ConnexViewController.getInstance().getConnexeTableView().getItems().removeAll();
        ConnexViewController.getInstance().getConnexeTableView().getItems().addAll(connexAffairForViews);
        Platform.runLater(() -> AffairDetailsController.getInstance().getConnexeTab().textProperty().bind(tab));

    }
    public TableView<AffaireForView> getTableView() {
        return tableView;
    }
    public TitledPane getTitledPane() {
        return titledPane;
    }


    public static ObservableList<ProcedureForView>[] getProcedureTab() { return procedureTab; }
    public static AffairViewController getInstance() {
        return affairViewController;
    }

    public static Image getRunningImg() {
        return runningImg;
    }
    public static Image getSuspendImg() {
        return suspendImg;
    }
    public static Image getFinishedImg() {
        return finishedImg;
    }
    public static Image getRejectImg() {
        return rejectImg;
    }
}
