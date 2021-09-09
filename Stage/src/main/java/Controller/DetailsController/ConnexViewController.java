package Controller.DetailsController;

import Controller.ViewController.AffairViewController;
import DAO.DbOperation;
import Model.Enum.AffaireStatus;
import Model.Enum.NotifType;
import Model.Enum.Origin;
import Model.Pojo.Affaire;
import Model.serviceManager.MainService;
import View.Cell.TableCell.IconCell;
import View.Dialog.Other.Notification;
import View.Dialog.SecurityDialog.AdminSecurity;
import View.Model.ConnexAffairForView;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnexViewController implements Initializable {

    private static ConnexViewController connexViewController;
    @FXML private JFXButton rejeterBtn;
    @FXML private JFXButton selectAllBtn;
    @FXML private JFXButton  refreshBtn;
    // AFFAIRE CONNEXE_VIEW_BTN
    @FXML private AnchorPane connexePanel;
    @FXML private TableView<ConnexAffairForView> connexeTableView;
    // CONNEXE_VIEW_BTN TABLEVIEW ET TABLECOLUMN
    @FXML private TableColumn<ConnexAffairForView, String> demandeurAffairConnex;
    @FXML private TableColumn<ConnexAffairForView, String> numAffairConnex;
    @FXML private TableColumn<ConnexAffairForView, ImageView> statusColumn;


    private void initializeConnexeTableview() {
        demandeurAffairConnex.setCellValueFactory(features -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> changeListener) {
            }

            @Override
            public void removeListener(ChangeListener<? super String> changeListener) {

            }

            @Override public String getValue() {
                return features.getValue().getNomDemandeur();
            }

            @Override public void addListener(InvalidationListener invalidationListener) {

            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {

            }
        });
        numAffairConnex.setCellValueFactory(features -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> changeListener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> changeListener) {

            }

            @Override
            public String getValue() {
                return features.getValue().getNumero();
            }

            @Override
            public void addListener(InvalidationListener invalidationListener) {

            }
            @Override public void removeListener(InvalidationListener invalidationListener) {

            }
        });
        statusColumn.setCellFactory(column -> new IconCell());
        statusColumn.setCellValueFactory(features -> new ObservableValue<ImageView>() {

            @Override public void addListener(ChangeListener<? super ImageView> changeListener) {

            }

            @Override
            public void removeListener(ChangeListener<? super ImageView> changeListener) {

            }

            @Override public ImageView getValue() {
                AffaireStatus status = features.getValue().getStatus();
                return AffairViewController.getInstance().getStatusIcon(status);
            }

            @Override public void addListener(InvalidationListener invalidationListener) {

            }

            @Override public void removeListener(InvalidationListener invalidationListener) {

            }
        });
    }

    @Override public void initialize(URL location, ResourceBundle resources) {
        connexViewController = this;
        initializeConnexeTableview();
        initButton();
    }

    private void initButton(){

        connexeTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        BooleanBinding aNull = connexeTableView.getSelectionModel().selectedItemProperty().isNull();
        rejeterBtn.disableProperty().bind(aNull);
        rejeterBtn.setOnAction(event -> AdminSecurity.show(Origin.REJETER_AFFAIRE));
        selectAllBtn.setOnAction(event -> connexeTableView.getSelectionModel().selectAll());
        selectAllBtn.disableProperty().bind(connexeTableView.itemsProperty().isNull());
        refreshBtn.setOnAction(event -> {
            Affaire affaire = AffairDetailsController.getAffaire();
            connexeTableView.getItems().setAll(affaire.getAllAffaireConnexe());
        });
    }

    public void rejeterAffaireConnexe(){

        ObservableList<ConnexAffairForView> items = connexeTableView.getSelectionModel().getSelectedItems();
        MainService.getInstance().launch(new Task<Void>() {
            @Override protected Void call() throws Exception {
                items.forEach(connexAffairForView -> connexAffairForView.setStatus(AffaireStatus.REJECTED));
                int[] ints = DbOperation.executeBatchRequest(items);
                if (ints.length == items.size()){
                    Platform.runLater(() -> {
                        Notification.getInstance(" Mis a jour effectuer avec succès ",NotifType.SUCCESS).show();
                    });
                }
                return null;
            }
        });
    }

    public AnchorPane getConnexePanel() {
        return connexePanel;
    }
    public static ConnexViewController getInstance() {
        return connexViewController;
    }
    public TableView<ConnexAffairForView> getConnexeTableView() {
        return connexeTableView;
    }
}
