package controller.viewController;

import dao.DaoFactory;
import Main.InitializeApp;
import Model.Enum.NotifType;
import Model.Enum.Origin;
import Model.Enum.TitleOperation;
import Model.Pojo.Affaire;
import Model.Pojo.Titre;
import Model.Other.MainService;
import View.Cell.TableCell.DateCreationTitreCell;
import View.Cell.TableCell.NomProprieteTitreCell;
import View.Cell.TableCell.NumMorcelementCell;
import View.Cell.TableCell.NumeroTitreCell;
import View.Dialog.FormDialog.TitleFrom;
import View.Dialog.Other.Notification;
import View.Dialog.SecurityDialog.AdminSecurity;
import View.Model.ViewObject.AffaireForView;
import View.Helper.Other.CheckboxTooltip;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TitleViewController implements Initializable {

    @Override public void initialize(URL location, ResourceBundle resources) {
        titleViewController = this;
        new CheckboxTooltip("rechercher dans la liste courante ou global",matchSearch);
        initializeAnneTitre();
        initButton();
        initTableview();
        initTitleSearch();
        initializeContextMenu();
    }

    private void initTitleSearch(){

        titleSearch.textProperty().addListener((observableValue, s, value) -> {
            if (matchSearch.isSelected()){
                ObservableList<Titre> items = tableView.getItems();
                if (!value.isEmpty()){
                    Stream<Titre> titreStream = items.stream().filter(titre -> titre.getNomPropriete().toLowerCase().startsWith(value.toLowerCase()) || titre.getNumero().toLowerCase().equals(value.toLowerCase()));
                    final List<Titre> filteredTitle = titreStream.collect(Collectors.toList());
                    if (! filteredTitle.isEmpty()){
                        items.setAll( filteredTitle);
                    }
                }else {
                    items.setAll(InitializeApp.getTitres());
                }
            }
        });

    }

    private void initDateCreation(){
        // date de creation
        dateDeCreation.setCellValueFactory(p -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> changeListener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> changeListener) {

            }

            @Override
            public String getValue() {
                return p.getValue().getDateCreation();
            }

            @Override
            public void addListener(InvalidationListener invalidationListener) {

            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {

            }
        });
        dateDeCreation.setCellFactory(titreStringTableColumn -> new DateCreationTitreCell());
    }

    private void initNomPropriete(){
        // num du propriete
        nomPropriete.setCellValueFactory(new PropertyValueFactory<>("nomPropriete"));
        nomPropriete.setCellFactory(titreStringTableColumn -> new NomProprieteTitreCell());
    }
    private void initNumero() {
        // numero
        numero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        numero.setCellFactory(titreStringTableColumn -> new NumeroTitreCell());
    }

    private void initNumeroMorcelement(){
        // numero du morcelement
        morcelement.setCellValueFactory(p -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> changeListener) {

            }

            @Override public void removeListener(ChangeListener<? super String> changeListener) {

            }

            @Override public String getValue() {
                return p.getValue().getNumMorcelement() == null || p.getValue().getNumMorcelement().isEmpty() ? "" : p.getValue().getNumMorcelement();
            }

            @Override public void addListener(InvalidationListener invalidationListener) {

            }

            @Override public void removeListener(InvalidationListener invalidationListener) {

            }
        });
        morcelement.setCellFactory(titreStringTableColumn -> new NumMorcelementCell());
    }

    private void initNumeroImmatriculation() {
        // numero immatriculation
        numImmat.setCellValueFactory(p -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> changeListener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> changeListener) {

            }

            @Override
            public String getValue() {
                return p.getValue().getNumTitreMere() == null || p.getValue().getNumTitreMere().isEmpty() ? "" : p.getValue().getNumTitreMere();
            }

            @Override
            public void addListener(InvalidationListener invalidationListener) {

            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {

            }
        });
    }

    // CONTEXTUAL MENU
    private void initializeContextMenu(){
        contextMenu = new ContextMenu();
        // details
        editerMenuItem = new MenuItem("Editer");
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/img/edit_20px.png")));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        editerMenuItem.setGraphic(imageView);
        editerMenuItem.setOnAction(this::getAllAction);
        //supprimer
        deleteMenuItem = new MenuItem("Supprimer");
        ImageView imageView1 = new ImageView(new Image(getClass().getResourceAsStream("/img/cancel_20px.png")));
        imageView1.setPreserveRatio(true);
        imageView1.setFitWidth(20);
        imageView1.setFitHeight(20);
        deleteMenuItem.setGraphic(imageView1);
        deleteMenuItem.setOnAction(this::getAllAction);
        BooleanBinding aNull = tableView.getSelectionModel().selectedItemProperty().isNull();
        editerMenuItem.disableProperty().bind(aNull);
        deleteMenuItem.disableProperty().bind(aNull);
        contextMenu.getItems().addAll(editerMenuItem, deleteMenuItem);
    }

    @FXML
    public void getAllAction(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if ( source == editerMenuItem || source == editBtn ) {
            AdminSecurity.show(Origin.EDIT_TITLE);
        } else if ( source == deleteMenuItem  || source == delTitleBtn )
            AdminSecurity.show(Origin.DELETE_TITLE);
    }

    public void initTitleEdit(){
        Titre selectedItem = tableView.getSelectionModel().getSelectedItem();
        TitleFrom instance = TitleFrom.getInstance();
        instance.getNomTitulaire().setText(selectedItem.getTitulaire());
        instance.getNomPropriete().setText(selectedItem.getNomPropriete());
        String numero = selectedItem.getNumero();
        String[] split = numero.split("-");
        instance.getNumeroPropriete().setText(split[0]);
        instance.getNumImmat().setText(selectedItem.getNumTitreMere());
        TextField morcelement = instance.getMorcelement();

        if (selectedItem.getNumTitreMere()!=null && !selectedItem.getNumTitreMere().isEmpty() ){
            morcelement.setText(selectedItem.getNumMorcelement());
        }else {
            morcelement.setDisable(true);
        }
        instance.getDateAttribution().setValue(LocalDate.now());
        instance.show(TitleOperation.UPDATE_OPERATION);
    }

    private void initTableview() {
        initDateCreation();
        initNumeroImmatriculation();
        initNumeroMorcelement();
        initNumero();
        initNomPropriete();
        tableView.setRowFactory(new Callback<TableView<Titre>, TableRow<Titre>>() {
            @Override
            public TableRow<Titre> call(TableView<Titre> param) {
                final TableRow<Titre> tableRow = new TableRow<>();
                tableRow.contextMenuProperty().bind(
                        Bindings.when(
                                Bindings.isNotNull(param.itemsProperty())).
                                then(contextMenu).
                                otherwise((ContextMenu) null));
                return tableRow;
            }
        });
        // titulaire
        titulaire.setCellValueFactory(new PropertyValueFactory<>("titulaire"));
        // ex-affaire
        numExAffaire.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Titre, Hyperlink>, ObservableValue<Hyperlink>>() {
            @Override
            public ObservableValue<Hyperlink> call(TableColumn.CellDataFeatures<Titre, Hyperlink> param) {

                return new ObservableValue<Hyperlink>() {

                    @Override public void addListener(ChangeListener<? super Hyperlink> listener) {

                    }

                    @Override public void removeListener(ChangeListener<? super Hyperlink> listener) {
                    }

                    @Override public Hyperlink getValue() {
                        String num = param.getValue().getNumExAffaire();
                        Hyperlink hyperlink = new Hyperlink(num);
                        hyperlink.setTextFill(Color.BLACK);
                        hyperlink.setOnMouseClicked(event -> AdminSecurity.show(Origin.SHOW_AFFAIRE_DETAILS_FROM_EX_AFFAIRE,event));
                        return hyperlink;
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

    public void showExAffaire(MouseEvent event){
        Hyperlink hyperlink = (Hyperlink)event.getSource();
        String num = hyperlink.getText();
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Affaire affaire = null;
                if (!InitializeApp.getAffaires().isEmpty()) {
                    Optional<AffaireForView> first = InitializeApp.getAffaires().stream().filter(affairForView -> affairForView.getNumero().equals(num)).findFirst();
                    if (first.isPresent())
                        affaire = first.get();
                } else {
                    affaire = DaoFactory.getAffaireDao().finByNum(num);
                }
                AffairViewController.getInstance().show(affaire);
                Platform.runLater(() -> MainController.getInstance().showAffaireDetailsView());
                return null;

            }
        };
        MainService.getInstance().launch(task);

    }

    private void initButton() {

        refreshBtn.setOnAction(event -> {
            Task<Void> task1 = new Task<Void>() {
                @Override protected Void call() throws Exception {
                       String newValue = dateTitre.getValue();
                        ObservableList<Titre> finalTitreObservableList = DaoFactory.getTitreDao().getTitleBy(newValue);
                        if (!finalTitreObservableList.isEmpty()){
                            Platform.runLater(() -> {
                                tableView.getItems().setAll(finalTitreObservableList);
                                InitializeApp.getTitres().setAll(finalTitreObservableList);
                            });
                        }
                    return null;
                }
                @Override protected void scheduled() {
                    titleProgress.progressProperty().unbind();
                    titleProgress.visibleProperty().unbind();
                    titleProgress.visibleProperty().bind(this.runningProperty());
                    titleProgress.progressProperty().bind(this.progressProperty());
                }
            };

            MainService.getInstance().launch(task1);

        });

        BooleanBinding booleanBinding = tableView.getSelectionModel().selectedItemProperty().isNull();
        delTitleBtn.disableProperty().bind(booleanBinding);
        editBtn.disableProperty().bind(booleanBinding);
        deleteButton.visibleProperty().bind(titleSearch.textProperty().isNotEqualTo(""));
        deleteButton.setOnAction(event -> titleSearch.setText(""));
        searchbutton.setOnAction(event -> {
            String titleNameOrNumOrFullName = titleSearch.getText().toLowerCase();
            MainService.getInstance().launch(new Task  <Void>() {
                @Override
                protected void scheduled() {
                    titleProgress.progressProperty().unbind();
                    titleProgress.visibleProperty().unbind();
                    titleProgress.visibleProperty().bind(this.runningProperty());
                    titleProgress.progressProperty().bind(this.progressProperty());
                }

                @Override
                protected Void call() throws Exception {
                    if (!titleNameOrNumOrFullName.isEmpty()){
                        ObservableList<Titre> items = tableView.getItems();
                        ObservableList<Titre> dbItems = DaoFactory.getTitreDao().find(titleNameOrNumOrFullName);
                        Platform.runLater(() -> items.setAll(dbItems) );
                    }
                    return null;
                }
            });
        });
    }

    public void deleteTitle(){
        Titre selectedItem = tableView.getSelectionModel().getSelectedItem();
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int deleteStatus = DaoFactory.getTitreDao().delete(selectedItem);
                if (deleteStatus!=0){
                    Platform.runLater(() -> {
                        tableView.getItems().remove(selectedItem);
                        InitializeApp.getTitres().remove(selectedItem);
                        String message = "Le titre foncier  N° "+selectedItem.getNumero()+" a été supprimé avec succès";
                        Notification.getInstance(message, NotifType.SUCCESS).showNotif();
                    });
                }
                return null;
            }
        });
    }

    private void initializeAnneTitre(){
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<String> observableList = DaoFactory.getTitreDao().groupeTitleByDate();
                observableList.add("Toutes");
                Platform.runLater(() -> {
                    dateTitre.getItems().setAll(observableList);
                    dateTitre.setValue(observableList.get(0));
                });
                return null;
            }
        });
        dateTitre.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            MainService.getInstance().launch(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    if (!newValue.isEmpty()) {
                        if (newValue.equals("Toutes")) finalTitreObservableList.setAll(DaoFactory.getTitreDao().getAlltitle());
                        else finalTitreObservableList.setAll(DaoFactory.getTitreDao().getTitleBy(newValue));
                        Platform.runLater(() -> tableView.getItems().setAll(finalTitreObservableList));
                        InitializeApp.getTitres().setAll(finalTitreObservableList);
                    }
                    return null;
                }

                @Override
                protected void scheduled() {
                    titleProgress.progressProperty().unbind();
                    titleProgress.visibleProperty().unbind();
                    titleProgress.visibleProperty().bind(this.runningProperty());
                    titleProgress.progressProperty().bind(this.progressProperty());
                }
            });
        });
    }
    public static TitleViewController getInstance() {
        return titleViewController;
    }
    public TableView<Titre> getTableView() {
        return tableView;
    }

    private static TitleViewController titleViewController;
    @FXML private JFXButton searchbutton;
    @FXML private TextField titleSearch;
    @FXML private ComboBox<String> dateTitre;
    @FXML private JFXButton deleteButton;
    @FXML private JFXButton refreshBtn;
    @FXML private TableView<Titre> tableView;
    // TABLECOLUMN
    @FXML private TableColumn<Titre, String> titulaire;
    @FXML private TableColumn<Titre, Hyperlink> numExAffaire;

    @FXML private TableColumn<Titre, String> numero;
    @FXML private TableColumn<Titre, String> morcelement;
    @FXML private TableColumn<Titre, String> dateDeCreation;
    @FXML private TableColumn<Titre, String> numImmat;
    @FXML private TableColumn<Titre, String> nomPropriete;
    @FXML private JFXCheckBox matchSearch;

    @FXML private JFXButton delTitleBtn;
    @FXML private JFXButton editBtn;
    @FXML private JFXProgressBar titleProgress;
    private static ContextMenu contextMenu;
    private MenuItem editerMenuItem;
    private MenuItem deleteMenuItem;
    private static ObservableList<Titre> finalTitreObservableList = FXCollections.observableArrayList();
}
