package controller.viewController;

import dao.DossierDao;
import javafx.beans.binding.BooleanBinding;
import controller.detailsController.*;
import dao.DaoFactory;
import Main.InitializeApp;
import model.Enum.FolderStatus;
import model.Enum.NotifType;
import model.Enum.Origin;
import model.Enum.TypeDemande;
import model.pojo.business.attachement.Attachement;
import model.pojo.business.other.*;
import view.Helper.Other.IndisponibleFunctionality;
import view.Helper.Other.ProgressTaskBinder;
import view.Helper.TableColumn.ProcedureColumnFactory;
import view.Model.Dialog.AlertDialog;
import view.Model.ViewObject.*;
import model.other.MainService;
import view.Cell.TableCell.IconCell;
import view.Dialog.FormDialog.MainAffaireForm;
import view.Dialog.Other.Notification;
import view.Dialog.SecurityDialog.AdminSecurity;
import view.Helper.Other.CheckboxTooltip;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXProgressBar;
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
import javafx.scene.Node;
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

public class FolderViewController implements Initializable {

    public void initializeDetailsData(Dossier dossier) {
        AffairDetailsController.setAffaire(dossier);
        // initialisation de la vue avec les données de l'affaire
            initializeDetailsView(dossier);
        // initialisation de la liste des redacteurs
        initializeEditorTableView(dossier);
        // initialisation de la liste des affaires connexe
        if (dossier.getTypeDemande().equals(TypeDemande.ACQUISITION))
            initializeConnexeTableView(dossier);
        // initialisation de la liste des procedures concerné par l'affaires
        initializeProcedureTableView(dossier);
        // INITIALIZATION DE LA LISTE DES PIECES JOINTE CONCERNANT L'AFFAIRE
        initAttachementView(dossier);
    }

    public void initAttachementView(Dossier dossier){
        JFXProgressBar attachementProgress = PieceJointeInfoController.getInstance().getAttachementProgress();
        Task<Void> attachementTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<AttachementForView> allPieceJointe = Attachement.getAllAttachementOf(dossier);
                ObservableList<Node> children = PieceJointeInfoController.getInstance().getPjTilepane().getChildren();
                Platform.runLater(() -> {
                    children.clear();
                    if (!allPieceJointe.isEmpty()) {
                        children.setAll(allPieceJointe);
                        PieceJointeInfoController.getAttachementList().setAll(allPieceJointe);
                    }
                });
                return null;
            }
            @Override protected void scheduled() {
                new ProgressTaskBinder(attachementProgress, this);
            }
        };
        MainService.getInstance().launch(attachementTask);
    }

    private void initImage() {
        runningImg = new Image(Objects.requireNonNull(FolderViewController.class.getResourceAsStream("/img/play1_20px.png")));
        suspendImg = new Image(Objects.requireNonNull(FolderViewController.class.getResourceAsStream("/img/pause_20px.png")));
        finishedImg = new Image(Objects.requireNonNull(FolderViewController.class.getResourceAsStream("/img/ok_20px.png")));
        rejectImg = new Image(Objects.requireNonNull(FolderViewController.class.getResourceAsStream("/img/cancel_20px.png")));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new CheckboxTooltip("rechercher dans la liste courante ou global", match);
        folderViewController = this;
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

    private void initButton() {
        BooleanBinding selectedItemIsNull = tableView.getSelectionModel().selectedItemProperty().isNull();
        launchSearchBtn.setOnAction(this::launchSearch);
        launchSearchBtn.disableProperty().bind(searchInput.textProperty().isEmpty());
        refreshBtn.setOnAction(event -> refreshFolderView());
        // CREATE NEW AFFAIR
        createAffBtn.setOnAction(event -> MainAffaireForm.getInstance().show());
        // BOUTTON ALL_AFF_DETAILS_VIEW_BTN
        detailsBtn.disableProperty().bind(selectedItemIsNull);
        detailsBtn.setOnAction(this::getAllAction);
        // BOUTTON IMPRIMER
        printBtn.disableProperty().bind(selectedItemIsNull);
        new IndisponibleFunctionality(printBtn);
        // DELETE BUTTON
        deleteBtn.disableProperty().bind(selectedItemIsNull);
        deleteBtn.setOnAction(this::getAllAction);
    }

    private void launchSearch(ActionEvent event) {
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                FolderForView affairBy = DaoFactory.getDossierDao().getFolderByApplicantNameOrNum(searchInput.getText());
                if (affairBy != null)
                    Platform.runLater(() -> tableView.getItems().setAll(affairBy));
                else
                    Platform.runLater(() -> AlertDialog.getInstance(Alert.AlertType.INFORMATION, " Auccun affaire ne correspond a " + searchInput.getText()).showAndWait());
                return null;
            }

            @Override
            protected void scheduled() {
                new ProgressTaskBinder(mainProgress, this);
                launchSearchBtn.disableProperty().unbind();
                launchSearchBtn.setDisable(true);
            }

            @Override
            protected void succeeded() {
                launchSearchBtn.disableProperty().unbind();
                launchSearchBtn.setDisable(false);
                launchSearchBtn.disableProperty().bind(searchInput.textProperty().isEmpty());
            }
        });
    }

    private void refreshFolderView() {
        ObservableList<String> items = yearFilter.getItems();
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<FolderForView> observableList = DaoFactory.getDossierDao().getAffaireBy(yearFilter.getValue());
                if (!items.isEmpty()) {
                    int size = items.size();
                    int max = Integer.valueOf(items.get(0));
                    int min = Integer.valueOf(items.get(size - 1));
                    ObservableList<String> dateList = DaoFactory.getDossierDao().groupFolderByDate(min, max);
                    if (!dateList.isEmpty())
                        Platform.runLater(() -> items.addAll(dateList));
                }
                Platform.runLater(() -> tableView.getItems().setAll(observableList));
                InitializeApp.getAffaires().setAll(observableList);
                return null;
            }

            @Override protected void scheduled() {
                new ProgressTaskBinder(mainProgress, this);
                refreshBtn.setDisable(true);
            }

            @Override
            protected void succeeded() {
                refreshBtn.setDisable(false);
            }
        });
    }

    @FXML
    void statusFilterAction(ActionEvent event) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected void scheduled() {
                new ProgressTaskBinder(mainProgress, this);
            }

            @Override
            protected Void call() throws Exception {
                ObservableList<FolderForView> affairForViews = FXCollections.observableArrayList();
                DossierDao dossierDao = DaoFactory.getDossierDao();
                if (event.getSource() == finishBtn) {
                    affairForViews = dossierDao.getAllAffairWhereStatusIs(FolderStatus.SUCCEED);
                } else if (event.getSource() == runningBtn) {
                    affairForViews = dossierDao.getAllAffairWhereStatusIs(FolderStatus.RUNNING);
                } else if (event.getSource() == suspendBtn) {
                    affairForViews = dossierDao.getAllAffairWhereStatusIs(FolderStatus.SUSPEND);
                } else if (event.getSource() == rejectBtn) {
                    affairForViews = dossierDao.getAllAffairWhereStatusIs(FolderStatus.REJECTED);
                } else if (event.getSource() == allBtn) {
                    affairForViews = InitializeApp.getAffaires();
                }
                ObservableList<FolderForView> finalAffairForViews = affairForViews;
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

    @Deprecated
    public void getAllAction(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if ((source == detailsBtn) || (source == detailsMenuItem)) {
            AdminSecurity.show(Origin.SHOW_AFFAIR_DETAILS);
        } else if ((source == deleteBtn) || (source == deleteMenuItem))
            AdminSecurity.show(Origin.DELETE_AFFAIR);
    }

    public void deleteAffaire() {
        deleteSelectedAffaire();
    }

    private void deleteSelectedAffaire() {
        FolderForView selectedItem = tableView.getSelectionModel().getSelectedItem();
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (selectedItem != null) {
                    if (DaoFactory.getDossierDao().deleteById(selectedItem) == 1) {
                        Platform.runLater(() -> tableView.getItems().remove(selectedItem));
                        Notification.getInstance(" L'affaire N° " + selectedItem.getNumero() + " est supprimé avec succès ", NotifType.SUCCESS).showNotif();
                    }
                }
                return null;
            }
        };
        MainService.getInstance().launch(task);
    }

    private void initializeViewType() {
        viewType.setItems(FXCollections.observableArrayList("Toutes", "Mes affaires uniquement"));
        viewType.setValue("Toutes");
        viewType.getSelectionModel().selectedItemProperty().addListener((observableValue, toggle, t1) -> {
            MainService.getInstance().launch(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    ObservableList<FolderForView> views = FXCollections.observableArrayList();
                    if (t1.equals("Toutes")) {
                        views = DaoFactory.getDossierDao().getAffaireBy(yearFilter.getValue());
                    } else {
                        User connectedUser = LoginController.getConnectedUser();
                        views = DaoFactory.getDossierDao().getAffairWhereActualEditorIs(connectedUser);
                    }
                    ObservableList<FolderForView> finalViews = views;
                    Platform.runLater(() -> tableView.getItems().setAll(finalViews));
                    return null;
                }

                @Override
                protected void scheduled() {
                    new ProgressTaskBinder(mainProgress, this);
                    viewType.setDisable(true);
                }

                @Override
                protected void succeeded() {
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

    private void initializeSearchTextField() {
        searchInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (match.isSelected()) {
                ObservableList<FolderForView> items = tableView.getItems();
                if (!newValue.isBlank()) {
                    List<FolderForView> views = items.filtered(folderForView -> {
                        String lowerCaseFilter = newValue.toLowerCase();
                        if (folderForView.getNumero().toLowerCase().contains(lowerCaseFilter))
                            return true;
                        else if (folderForView.getDemandeur().getNom().toLowerCase().contains(lowerCaseFilter))
                            return true;
                        else return false;
                    });
                    if (!views.isEmpty()) items.retainAll(views);
                    else items.setAll(InitializeApp.getAffaires());
                } else items.setAll(InitializeApp.getAffaires());
            }
        });
    }

    private void initializeYearFilter() {
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<String> observableList = DaoFactory.getDossierDao().groupFolderByDate();
                yearFilter.getItems().setAll(observableList);
                if (!observableList.isEmpty())
                    yearFilter.setValue(observableList.get(0));
                return null;
            }

            @Override
            protected void scheduled() {
                new ProgressTaskBinder(mainProgress, this);
            }
        });
        yearFilter.getSelectionModel().selectedItemProperty().addListener(this::yearFilterChanged);
    }

    private void initializeTableView() {
        tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, affairForView, t1) -> {
            if (t1 != null) {
                if (t1.getTypeDemande().equals(TypeDemande.PRESCRIPTION))
                    ConnexeInfoController.getInstance().getConnexePanel().setDisable(true);
                else ConnexeInfoController.getInstance().getConnexePanel().setDisable(false);
            }
        });
        tableView.setRowFactory(new Callback<TableView<FolderForView>, TableRow<FolderForView>>() {
            @Override
            public TableRow<FolderForView> call(TableView<FolderForView> param) {
                final TableRow<FolderForView> tableRow = new TableRow<>();
                tableRow.contextMenuProperty().bind(
                        Bindings.when(
                                Bindings.isNotNull(param.itemsProperty())).
                                then(contextMenu).
                                otherwise((ContextMenu) null));
                return tableRow;
            }
        });
        // numero
        numero.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FolderForView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FolderForView, String> param) {
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
        demandeur.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FolderForView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FolderForView, String> param) {
                return new ObservableValue<String>() {
                    @Override
                    public void addListener(ChangeListener<? super String> listener) {
                    }

                    @Override
                    public void removeListener(ChangeListener<? super String> listener) {
                    }

                    @Override
                    public String getValue() {
                        Personne demandeur = param.getValue().getDemandeur();
                        return demandeur.getNom();
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
        nomPropriete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FolderForView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FolderForView, String> param) {
                return new ObservableValue<String>() {
                    @Override
                    public void addListener(ChangeListener<? super String> listener) {
                    }

                    @Override
                    public void removeListener(ChangeListener<? super String> listener) {
                    }

                    @Override
                    public String getValue() {
                        Propriete titreDependant = param.getValue().getTerrain().getTitreDependant();
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
        numeroTitre.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FolderForView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FolderForView, String> param) {
                return new ObservableValue<String>() {

                    @Override
                    public void addListener(ChangeListener<? super String> listener) {
                    }

                    @Override
                    public void removeListener(ChangeListener<? super String> listener) {
                    }

                    @Override
                    public String getValue() {
                        Propriete titre = param.getValue().getTerrain().getTitreDependant();
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
        superficie.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FolderForView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FolderForView, String> param) {
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
        redactor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FolderForView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FolderForView, String> param) {
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
        situation.setCellValueFactory(param -> ProcedureColumnFactory.createLabel(param.getValue().getProcedureForTableView()));
        // type
        type.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FolderForView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FolderForView, String> param) {
                return new ObservableValue<String>() {
                    @Override
                    public void addListener(ChangeListener<? super String> changeListener) {

                    }

                    @Override
                    public void removeListener(ChangeListener<? super String> changeListener) {

                    }

                    @Override
                    public String getValue() {
                        TypeDemande typeDemande = param.getValue().getTypeDemande();
                        return TypeDemande.typeDemande2String(typeDemande);
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
        status.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FolderForView, ImageView>, ObservableValue<ImageView>>() {
            @Override
            public ObservableValue<ImageView> call(TableColumn.CellDataFeatures<FolderForView, ImageView> param) {
                return new ObservableValue<ImageView>() {
                    @Override
                    public void addListener(ChangeListener<? super ImageView> listener) {

                    }

                    @Override
                    public void removeListener(ChangeListener<? super ImageView> listener) {

                    }

                    @Override
                    public ImageView getValue() {
                        FolderForView value = param.getValue();
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

    public ImageView getStatusIcon(FolderStatus status) {
        switch (status) {
            case SUCCEED:
                return new ImageView(FolderViewController.getFinishedImg());
            case RUNNING:
                return new ImageView(FolderViewController.getRunningImg());
            case SUSPEND:
                return new ImageView(FolderViewController.getSuspendImg());
            case REJECTED:
                return new ImageView(FolderViewController.getRejectImg());
            default:
                return new ImageView(FolderViewController.getRunningImg());
        }
    }

    private void yearFilterChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue != null) {
            MainService.getInstance().launch(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    ObservableList<FolderForView> affairForViews = DaoFactory.getDossierDao().getAffaireBy(newValue);
                    Platform.runLater(() -> tableView.getItems().setAll(affairForViews));
                    InitializeApp.getAffaires().setAll(affairForViews);
                    return null;
                }

                @Override protected void scheduled() {
                    new ProgressTaskBinder(mainProgress, this);
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
    private void initializeEditorTableView(Dossier dossier) {
        Task<Void> editorTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ArrayList<EditorForView> arrayList = dossier.getAllEditor();
                SimpleStringProperty tab = new SimpleStringProperty("redacteur (" + String.valueOf(arrayList.toArray().length) + ")");
                RedacteurInfoController.getInstance().getEditorTableView().getItems().setAll(arrayList);
                Platform.runLater(() -> {
                    AffairDetailsController.getInstance().getRedacteurTab().textProperty().bind(tab);
                });
                return null;
            }
        };
        MainService.getInstance().launch(editorTask);
    }

    public void showDetails() {
        // afficher le panneau
        Dossier dossier = FolderViewController.getInstance().getTableView().getSelectionModel().getSelectedItem();
        MainController.getInstance().showAffaireDetailsView();
        show(dossier);
    }

    public void show(Dossier dossier) {
        // VERIFIER SI L'AFFAIRE EST DEJA OUVERT
        Dossier detailsDossier = AffairDetailsController.getAffaire();
        if (detailsDossier == null) {
            initializeDetailsData(dossier);
        } else if (detailsDossier.getId() != dossier.getId()) {
            initializeDetailsData(detailsDossier);
        }
    }

    private void initializeProcedureTableView(Dossier dossier) {
        Task<Void> procedureTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                TableView<ProcedureForView> procedureTableView = ProcedureInfoController.getInstance().getProcedureTableView();
                ObservableList<ProcedureForView> items = procedureTableView.getItems();
                TypeDemande typeDemande = dossier.getTypeDemande();
                if (procedureTab == null) procedureTab = new ObservableList[2];
                if (typeDemande.equals(TypeDemande.ACQUISITION)) {
                    if (procedureTab[ACQUISITION_AND_AFFECTATION_INDEX] == null)
                        procedureTab[ACQUISITION_AND_AFFECTATION_INDEX] = DaoFactory.getProcedureDao().getAllProcedureFromBD("A");
                    items.setAll(procedureTab[ACQUISITION_AND_AFFECTATION_INDEX]);
                } else {
                    if (procedureTab[PRESCRIPTION_INDEX] == null)
                        procedureTab[PRESCRIPTION_INDEX] = DaoFactory.getProcedureDao().getAllProcedureFromBD("P");
                    items.setAll(procedureTab[PRESCRIPTION_INDEX]);
                }
                initAffaireProcedure(dossier, items);
                return null;
            }

            @Override
            protected void scheduled() {
            }
        };
        MainService.getInstance().launch(procedureTask);
    }

    public void initAffaireProcedure(Dossier dossier, ObservableList<ProcedureForView> itemList) {
        ObservableList<ArrayList<String>> viewArrayList = dossier.getAllProcedureChecked();
        bindTabToItemSize(viewArrayList, AffairDetailsController.getInstance().getProcedureTab(), "Procedure");
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

    // LE DETAILS DE L'AFFAIRE EST EN FONCTION DU TYPE DU DEMANDEUR
    private void initializeDetailsView(Dossier dossier) {
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
//                DemandeurPhysique demandeurPhysique = DaoFactory.getDemandeurPhysiqueDao().findDemandeurBy(affaire.getId());
//                affaire.setDemandeur(demandeurPhysique);
                Propriete propriete = DaoFactory.getTerrainDao().find(dossier.getTerrain().getIdPropriete());
                Platform.runLater(() -> {
                    // afficher le numero de l'affaire selectionner
                    AffairDetailsController.getInstance().getSelectedAffaire().setText(dossier.getNumero());
                    AffairDetailsController controller = AffairDetailsController.getInstance();
//    //                controller.getNumeroAffaire().setText(affaire.getNumero());
//                    controller.getDateFromulation().setText(affaire.getDateDeFormulation().toString());
//                    controller.getTypeDemande().setText(typeDemande2String(affaire.getTypeDemande()));
//                    controller.getNom().setText(demandeur.getNom());
//                    controller.getPrenom().setText(demandeur.getPrenom());
//                    controller.getAdresse().setText(demandeur.getAdresse());
//                    controller.getParcelleDemandeur().setText(demandeur.getParcelle());
//                    controller.getLot().setText(demandeur.getLot());
//                    controller.getSise().setText(propriete.getQuartier());
//                    controller.getParcelleTerrain().setText(propriete.getParcelle());
//                    controller.getCommune().setText(propriete.getCommune());
//                    controller.getDistrict().setText(propriete.getDistrict());
//                    controller.getRegion().setText(propriete.getRegion());
//                    controller.getSuperficie().setText(propriete.getSuperficie());
                    Propriete titreDependant = propriete.getTitreDependant();
                    if (titreDependant != null) {
//                        controller.getNomPropriete().setText(titreDependant.toString());
                    }
                    Label statusLabel = controller.getStatusLabel();
                    System.out.println(dossier.getStatus());
                    switch (dossier.getStatus()) {
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
                return null;
            }
        });
    }

    private void initializeConnexeTableView(Dossier dossier) {
        Task<Void> connexTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<ConnexAffairForView> connexAffairForViews = dossier.getAllAffaireConnexe();
                ConnexeInfoController.getInstance().getConnexeTableView().getItems().clear();
                if (!connexAffairForViews.isEmpty())
                    Platform.runLater(() -> ConnexeInfoController.getInstance().getConnexeTableView().getItems().setAll(connexAffairForViews));
                Tab connexeTab = AffairDetailsController.getInstance().getConnexeTab();
                bindTabToItemSize(connexAffairForViews, connexeTab, "connexe");
                return null;
            }

            @Override
            protected void scheduled() {
            }
        };
        MainService.getInstance().launch(connexTask);
    }

    private void bindTabToItemSize(ObservableList observableList, Tab tab, String tabText) {
        Platform.runLater(() -> tab.setText(tabText + "(" + String.valueOf(observableList.size()) + ")"));
    }

    public TableView<FolderForView> getTableView() {
        return tableView;
    }

    public TitledPane getTitledPane() {
        return titledPane;
    }

    public static ObservableList<ProcedureForView>[] getProcedureTab() {
        return procedureTab;
    }

    public static FolderViewController getInstance() {
        return folderViewController;
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

    private static ObservableList<ProcedureForView> procedureTab[];
    private static FolderViewController folderViewController = null;

    private static MenuItem detailsMenuItem;
    private static MenuItem deleteMenuItem;
    private static ContextMenu contextMenu;
    private static Image runningImg;
    private static Image suspendImg;
    private static Image finishedImg;
    private static Image rejectImg;
    private final int ACQUISITION_AND_AFFECTATION_INDEX = 0;
    private final int PRESCRIPTION_INDEX = 1;
    @FXML
    private JFXButton launchSearchBtn;
    @FXML
    private JFXButton createAffBtn;
    @FXML
    private JFXButton delTextfield;
    @FXML
    private TextField searchInput;
    @FXML
    private JFXButton detailsBtn;
    @FXML
    private JFXButton deleteBtn;
    @FXML
    private JFXButton printBtn;
    @FXML
    private JFXButton refreshBtn;
    // FILTER
    @FXML
    private JFXButton finishBtn;
    @FXML
    private JFXButton suspendBtn;
    @FXML
    private JFXButton runningBtn;
    @FXML
    private JFXButton allBtn;
    @FXML
    private JFXButton rejectBtn;
    // TABLEVIEW ET TABLECOLUMN
    @FXML
    private TableView<FolderForView> tableView;
    @FXML
    private TableColumn<FolderForView, String> numero;
    @FXML
    private TableColumn<FolderForView, String> demandeur;
    @FXML
    private TableColumn<FolderForView, String> nomPropriete;
    @FXML
    private TableColumn<FolderForView, String> numeroTitre;
    @FXML
    private TableColumn<FolderForView, String> superficie;
    @FXML
    private TableColumn<FolderForView, String> redactor;
    @FXML
    private TableColumn<FolderForView, Label> situation;
    @FXML
    private TableColumn<FolderForView, String> type;
    @FXML
    private TableColumn<FolderForView, ImageView> status;
    @FXML
    private AnchorPane searchPanel;
    @FXML
    private HBox actionPanel;
    @FXML
    private TitledPane titledPane;
    @FXML
    private ComboBox<String> yearFilter;
    @FXML
    private ComboBox<String> viewType;
    @FXML
    private HBox buttonBox;
    @FXML
    private JFXButton searchPanelBtn;
    @FXML
    private JFXButton actionPanelBtn;
    @FXML
    private JFXCheckBox match;
    @FXML
    private JFXProgressBar mainProgress;
}
