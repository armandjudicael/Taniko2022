package controller.detailsController;

import controller.viewController.AffairViewController;
import dao.DbOperation;
import Model.Enum.NotifType;
import Model.Enum.Origin;
import Model.Pojo.business.Affaire;
import Model.Other.MainService;
import View.Cell.TableCell.DateCreationTitreCell;
import View.Cell.TableCell.PhotoColumnCell;
import View.Dialog.Other.Notification;
import View.Dialog.SecurityDialog.AdminSecurity;
import View.Dialog.SecurityDialog.EditorSecurity;
import View.Model.ViewObject.EditorForView;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class RedacteurInfoController implements Initializable {

    @Override public void initialize(URL location, ResourceBundle resources) {
        dispatchBtn.setOnAction(event -> AdminSecurity.show(Origin.DISPACTH_BTN));
        redacteurInfoController = this;
        delEditorBtn.setOnAction(event -> AdminSecurity.show(Origin.REMOVE_EDITOR_FROM_LIST_BTN));
        initializeRedactorTableView();
        delEditorBtn.disableProperty().bind(editorTableView.getSelectionModel().selectedItemProperty().isNull());
        refreshBtn.setOnAction(event -> {
            MainService service = MainService.getInstance();
            service.launch(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    Affaire affair = AffairDetailsController.getAffaire();
                    editorTableView.getItems().setAll(affair.getAllEditor());
                    return null;
                }
                @Override
                protected void running() {
                    super.running();
                    refreshBtn.setDisable(true);
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    refreshBtn.setDisable(false);
                }
            });
        });
       // new TabBinding(editorTableView.getItems(),redacteurTab);
    }

    private void initializeRedactorTableView() {
        photo.setCellFactory(param -> new PhotoColumnCell());
        photo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EditorForView, Circle>, ObservableValue<Circle>>() {
            @Override
            public ObservableValue<Circle> call(TableColumn.CellDataFeatures<EditorForView, Circle> param) {
                return new ObservableValue<Circle>() {
                    @Override
                    public void addListener(ChangeListener<? super Circle> listener) {
                    }

                    @Override
                    public void removeListener(ChangeListener<? super Circle> listener) {
                    }

                    @Override
                    public Circle getValue() {
                        Circle tmp = new Circle(40, new ImagePattern(param.getValue().getPhoto()));
                        return tmp;
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
        nomEtPrenom.setCellFactory(editorForViewStringTableColumn -> new DateCreationTitreCell());
        nomEtPrenom.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EditorForView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EditorForView, String> p) {
                return new ObservableValue<String>() {
                    @Override
                    public void addListener(ChangeListener<? super String> changeListener) {

                    }

                    @Override
                    public void removeListener(ChangeListener<? super String> changeListener) {

                    }

                    @Override
                    public String getValue() {
                        return p.getValue().getFullName();
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
        dateEtHeure.setCellFactory(editorForViewStringTableColumn -> new DateCreationTitreCell());
        dateEtHeure.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EditorForView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EditorForView, String> p) {
                return new ObservableValue<String>() {
                    @Override
                    public void addListener(ChangeListener<? super String> changeListener) {

                    }

                    @Override
                    public void removeListener(ChangeListener<? super String> changeListener) {

                    }

                    @Override
                    public String getValue() {
                        return p.getValue().getDateEtHeure().toString();
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
    }

    public void actionOnAffaire(ActionEvent actionEvent) {
        if (!EditorSecurity.getRemember()) {
            EditorSecurity.show(Origin.EDITOR_VIEW_BTN, actionEvent);
        } else launchAction(actionEvent);
    }

    public void launchAction(ActionEvent actionEvent) {
        if (actionEvent.getSource() == delEditorBtn) {
            deleteUserFromList();
        }
    }

    public void deleteUserFromList(){
        MainService service = MainService.getInstance();
        service.launch(deleteTask());
    }

    private Task deleteTask() {

        return new Task() {

            @Override protected Void call() throws Exception {
                int idUser = editorTableView
                        .getSelectionModel()
                        .getSelectedItem().getId();
                Affaire affaire = AffairViewController.getInstance().getTableView().getSelectionModel().getSelectedItem();
                if (DbOperation.deleteOnAffaireUtilisateurTable(idUser,affaire.getId()) == 1) {
                    Platform.runLater(() -> {
                        editorTableView.getItems().remove(
                                editorTableView.
                                        getSelectionModel().getSelectedItem());
                    });
                    String message = "Suppression effectué avec succes";
                    Notification.getInstance(message,NotifType.SUCCESS).showNotif();
                }
                return null;
            }

        };
    }

    public static RedacteurInfoController getInstance() {
        return redacteurInfoController;
    }
    public TableView<EditorForView> getEditorTableView() {
        return editorTableView;
    }
    private static RedacteurInfoController redacteurInfoController;

    @FXML private Tab redacteurTab;
    @FXML private JFXButton dispatchBtn;
    // EDITOR_VIEW_BTN TABLEVIEW ET TABLECOLUMN
    @FXML private TableView<EditorForView> editorTableView;
    @FXML private TableColumn<EditorForView, Circle> photo;
    @FXML private TableColumn<EditorForView, String> nomEtPrenom;
    @FXML private TableColumn<EditorForView, String> dateEtHeure;
    @FXML private JFXButton delEditorBtn;
    @FXML private JFXButton refreshBtn;
}
