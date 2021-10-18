package Controller.detailsController;

import Controller.ViewController.AffairViewController;
import DAO.DbOperation;
import Model.Enum.NotifType;
import Model.Enum.Origin;
import Model.Enum.TypeDemande;
import Model.Pojo.Affaire;
import Model.Other.MainService;
import View.Cell.TableCell.DateProcedureCell;
import View.Cell.TableCell.NumeroProcedureCell;
import View.Cell.TableCell.ProcedureNameCell;
import View.Dialog.FormDialog.NewProcedureDialog;
import View.Dialog.Other.Notification;
import View.Dialog.SecurityDialog.EditorSecurity;
import View.Model.Dialog.ProcedureConfirmationDialog;
import View.Model.ViewObject.AffaireForView;
import View.Model.ViewObject.ProcedureForView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProcedureViewController implements Initializable{

    @Override public void initialize(URL location, ResourceBundle resources) {
        procedureViewController = this;
        initTableview();
        initializeSearchTextField();
        deleteSearch.visibleProperty().bind(Bindings.not(procedureSearchTextField.textProperty().isEqualTo("")));
        deleteSearch.setOnAction(event -> procedureSearchTextField.setText(""));
        delPrcdBtn.disableProperty().bind(procedureTableView.getSelectionModel().selectedItemProperty().isNull());
        refreshBtn.setOnAction(this::refreshProcedureTableview);
    }

    private void refreshProcedureTableview(ActionEvent event){
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                AffairViewController instance = AffairViewController.getInstance();
                TableView<AffaireForView> tableView = instance.getTableView();
                AffaireForView affair = tableView.getSelectionModel().getSelectedItem();
                instance.initAffaireProcedure(affair,procedureTableView.getItems());
                return null;
            }
        });
    }

    @Deprecated public void actionOnAffaire(ActionEvent actionEvent) {
        if (!EditorSecurity.getRemember()) {
            EditorSecurity.show(Origin.PROCEDURE_VIEW_BTN, actionEvent);
        } else launchAction(actionEvent);
    }

    public void launchAction(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source == newPrcdBtn) {
            NewProcedureDialog.getInstance().show();
        } else if (source == delPrcdBtn) {
            deleteProcedure();
        }
    }

    private void deleteProcedure(){
        ObservableList<ProcedureForView> selectedItems = procedureTableView.getSelectionModel().getSelectedItems();
        long count = selectedItems.stream().filter(procedureForView -> procedureForView.isChecked()).count();
        if (count != 0)
            ProcedureConfirmationDialog.getInstance(null,event -> runDeleteTask(selectedItems) );
        else runDeleteTask(selectedItems);
    }

    private void runDeleteTask(ObservableList<ProcedureForView> selectedItems) {
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                runDelete(selectedItems,procedureTableView.getItems());
                return null;
            }
        });
    }
    private void runDelete(ObservableList<ProcedureForView> selectedItems, ObservableList<ProcedureForView> items) {
        int[] procedureDeleted = DbOperation.executeBacthRequest(selectedItems);
        Platform.runLater(() -> {
            if (selectedItems.size() == procedureDeleted.length ){
                items.removeAll(selectedItems);
                Notification.getInstance(procedureDeleted.length + " procedure(s) supprimé(s) avec succès ", NotifType.SUCCESS).showNotif();
            }
        });
    }
    private void initializeSearchTextField(){
        procedureSearchTextField.textProperty().addListener((observableValue, s, value) -> {
            final ObservableList<ProcedureForView> items = procedureTableView.getItems();
            if (!value.isEmpty()){
                List<ProcedureForView> collect = items.stream().filter(procedureForView -> {
                    if (!value.isBlank()) {
                        if (procedureForView.getProcedureName().toLowerCase().startsWith(value.toLowerCase()))
                            return true;
                        else return false;
                    } else return false;
                }).collect(Collectors.toList());
                if (!collect.isEmpty())
                 items.retainAll(collect);
                else {
                    ObservableList<ProcedureForView>[] procedureTab = AffairViewController.getProcedureTab();
                    Affaire affaire = AffairDetailsController.getAffaire();
                    TypeDemande typeDemande = affaire.getTypeDemande();
                    items.setAll(typeDemande.equals(TypeDemande.ACQUISITION) ? procedureTab[0] : procedureTab[1] );
                }
            }else {
                ObservableList<ProcedureForView>[] procedureTab = AffairViewController.getProcedureTab();
                Affaire affaire = AffairDetailsController.getAffaire();
                TypeDemande typeDemande = affaire.getTypeDemande();
                items.setAll(typeDemande.equals(TypeDemande.ACQUISITION) ? procedureTab[0] : procedureTab[1] );
            }
        });
    }
    private void initTableview() {
        procedureTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        initProcedureNameColumn();
        initStatusColumn();
        initNumArriveColumn();
        initNumDepartColumn();
        initDateDepartColumn();
        initDateArriveColumn();
    }
    private void initStatusColumn() {
        statusColumn.setCellFactory(param -> new StatusTableCell());
        statusColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProcedureForView, StatusCheckBox>, ObservableValue<StatusCheckBox>>() {
            @Override
            public ObservableValue<StatusCheckBox> call(TableColumn.CellDataFeatures<ProcedureForView, StatusCheckBox> param) {
                return new ObservableValue<StatusCheckBox>() {
                    @Override
                    public void addListener(ChangeListener<? super StatusCheckBox> listener) {
                    }

                    @Override
                    public void removeListener(ChangeListener<? super StatusCheckBox> listener) {
                    }

                    @Override public StatusCheckBox getValue() {
                        StatusCheckBox checkBox = new StatusCheckBox();
                        checkBox.selectedProperty().bindBidirectional(param.getValue().checkedProperty());
                        return checkBox;
                    }

                    @Override public void addListener(InvalidationListener listener) {

                    }
                    @Override public void removeListener(InvalidationListener listener) {

                    }
                };
            }
        });
    }
    private void initProcedureNameColumn() {
        nomProcedure.setCellValueFactory(new PropertyValueFactory<ProcedureForView, String>("procedureName"));
        nomProcedure.setCellFactory(procedureForViewStringTableColumn -> new ProcedureNameCell());
        nomProcedure.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProcedureForView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ProcedureForView, String> event) {
                String newProcedureName = event.getNewValue();
                ProcedureForView procedureForView = event.getRowValue();
                MainService.getInstance().launch(new Task<Void>() {
                    @Override protected Void call() throws Exception {
                       int query = DbOperation.updateProcedureName(newProcedureName, procedureForView.getIdProcedure());
                       if (query!=0) Notification.getInstance(" Mis à jour effectué avec succès ",NotifType.SUCCESS).showNotif();
                       else  Notification.getInstance(" Echec de la mis à jour ",NotifType.WARNING).showNotif();
                       return null;
                    }
                });
            }
        });
    }
    private void initNumArriveColumn() {
        numArrive.setCellValueFactory(new PropertyValueFactory<>("numArrive"));
        numArrive.setCellFactory(procedureForViewStringTableColumn -> new NumeroProcedureCell());
        numArrive.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProcedureForView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ProcedureForView, String> event) {
                MainService.getInstance().launch(new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        String newNumArrive = event.getNewValue();
                        ProcedureForView procedureForView = event.getRowValue();
                        procedureForView.setNumArrive(newNumArrive);
                        Affaire affair = AffairViewController.getInstance().getTableView().getSelectionModel().getSelectedItem();
                        int query = DbOperation.launchQuery("UPDATE procedure_concerner_affaire SET numArrive ='" + newNumArrive + "' WHERE  procedureId =" + procedureForView.getIdProcedure() + " AND affaireId = " + affair.getId() + ";");
                        if (query!=0) Notification.getInstance(" numero enregister avec succès ",NotifType.SUCCESS).showNotif();
                        else  Notification.getInstance(" Echec de la mis à jour ",NotifType.WARNING).showNotif();
                        return null;
                    }
                });
            }
        });
    }
    private void initNumDepartColumn() {
        numDepart.setCellValueFactory(new PropertyValueFactory<>("numDepart"));
        numDepart.setCellFactory(procedureForViewStringTableColumn -> new NumeroProcedureCell());
        numDepart.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProcedureForView, String>>() {
            @Override public void handle(TableColumn.CellEditEvent<ProcedureForView, String> event) {
                MainService.getInstance().launch(new Task<Void>() {
                    @Override protected Void call() throws Exception {
                        String newNumDepart = event.getNewValue();
                        ProcedureForView procedureForView = event.getRowValue();
                        procedureForView.setNumDepart(newNumDepart);
                        Affaire affaire = AffairViewController.getInstance().getTableView().getSelectionModel().getSelectedItem();
                        int query = DbOperation.launchQuery("UPDATE procedure_concerner_affaire SET numDepart ='" + newNumDepart + "' WHERE  procedureId =" + procedureForView.getIdProcedure() + " AND affaireId = " + affaire.getId() + ";");
                        if (query!=0) Notification.getInstance(" Mis à jour effectué avec succès ",NotifType.SUCCESS).showNotif();
                        else  Notification.getInstance(" Echec de la mis à jour ",NotifType.WARNING).showNotif();
                        return null;
                    }
                });
            }

        });

    }
    private void initDateDepartColumn() {
        dateDepart.setCellValueFactory(new PropertyValueFactory<ProcedureForView, String>("dateDepart"));
        dateDepart.setCellFactory(procedureForViewStringTableColumn -> new DateProcedureCell());
        dateDepart.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProcedureForView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ProcedureForView, String> event) {
                MainService.getInstance().launch(new Task<Void>() {

                    @Override protected Void call() throws Exception {
                        String newDateDepart = event.getNewValue();
                        ProcedureForView procedureForView = event.getRowValue();
                        procedureForView.setDateDepart(newDateDepart);
                        Affaire affair = AffairViewController.getInstance().getTableView().getSelectionModel().getSelectedItem();
                        int query = DbOperation.launchQuery("UPDATE procedure_concerner_affaire SET dateDepart ='" + newDateDepart + "' WHERE  procedureId =" + procedureForView.getIdProcedure() + " AND affaireId = " + affair.getId() + ";");
                        if (query!=0) Notification.getInstance(" Mis à jour effectué avec succès ",NotifType.SUCCESS).showNotif();
                        else  Notification.getInstance(" Echec de la mis à jour ",NotifType.WARNING).showNotif();
                        return null;

                    }
                });

            }
        });
    }
    private void initDateArriveColumn() {
        dateArrive.setCellValueFactory(new PropertyValueFactory<ProcedureForView, String>("dateArrive"));
        dateArrive.setCellFactory(procedureForViewStringTableColumn -> new DateProcedureCell());
        dateArrive.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProcedureForView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ProcedureForView, String> event) {
                MainService.getInstance().launch(new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        String newDateArrive = event.getNewValue();
                        ProcedureForView procedureForView = event.getRowValue();
                        procedureForView.setDateArrive(newDateArrive);
                        Affaire affair = AffairViewController.getInstance().getTableView().getSelectionModel().getSelectedItem();
                        int query = DbOperation.launchQuery("UPDATE procedure_concerner_affaire SET dateArrive ='" + newDateArrive + "' WHERE  procedureId =" + procedureForView.getIdProcedure() + " AND affaireId = " + affair.getId() + ";");
                        if (query!=0) Notification.getInstance(" Mis à jour effectué avec succès ",NotifType.SUCCESS).showNotif();
                        else Notification.getInstance(" Echec de la mis à jour ",NotifType.WARNING).showNotif();
                       return null;
                    }
                });

            }
        });
    }
    public static class StatusCheckBox extends JFXCheckBox{
        public StatusCheckBox() {
            statusCheckBox = this;
        }
        private static StatusCheckBox statusCheckBox;
        private void resetProcedure(ProcedureForView procedureForView) {
            setSelected(false);
            procedureForView.setNumDepart(null);
            procedureForView.setDateArrive(null);
            procedureForView.setNumArrive(null);
            procedureForView.setDateArrive(null);
            statusCheckBox = this;
        }
        @Override public void fire(){
            ProcedureConfirmationDialog.getInstance(this, new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent event){

                    Affaire affair = AffairDetailsController.getAffaire();
                    TableCell cell = (TableCell) statusCheckBox.getParent();
                    TableRow tableRow = (TableRow) cell.getParent();
                    int index = tableRow.getIndex();
                    
                    ProcedureForView procedureForView = ProcedureViewController.getInstance().getProcedureTableView().getItems().get(index);
                    if (!isSelected()){
                        statusCheckBox.setSelected(true);
                        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
                        procedureForView.setDateDepart(localDateTime.toString());
                        MainService.getInstance().launch(new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                int status = DbOperation.insertOnAffAndPrcdTable(procedureForView.getIdProcedure(),
                                        affair.getId(),
                                        Timestamp.valueOf(localDateTime));
                                if (status != 0) Notification.getInstance(" Procedure enregistrer avec succès ", NotifType.SUCCESS).showNotif();
                                else Notification.getInstance(" Echec de la mis à jour ",NotifType.WARNING).showNotif();
                                return null;
                            }
                        });
                    }else{
                        resetProcedure(procedureForView);
                        MainService.getInstance().launch(new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                int status = DbOperation.launchQuery(" DELETE FROM  procedure_concerner_affaire WHERE (procedureId = " + procedureForView.getIdProcedure() + " AND affaireId = " + affair.getId() + ");");
                                if (status !=0) Notification.getInstance(" Mis à jour enregistrer avec succès ",NotifType.SUCCESS).showNotif();
                                else Notification.getInstance(" Echec de la mis à jour ",NotifType.WARNING).showNotif();
                                return null;
                            }
                        });
                    }
                }
            }).show(); }
    }
    public static ProcedureViewController getInstance() {
        return procedureViewController;
    }
    public class StatusTableCell extends TableCell {
        @Override protected void updateItem(Object item, boolean empty) {
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(null);
                setGraphic((StatusCheckBox) item);
                setAlignment(Pos.CENTER);
                setCursor(Cursor.HAND);
            }
        }
    }
    public TableView<ProcedureForView> getProcedureTableView() {
        return procedureTableView;
    }
    private static ProcedureViewController procedureViewController;
    // PROCEDURE_VIEW_BTN
    @FXML private JFXButton newPrcdBtn;
    @FXML private JFXButton deleteSearch;
    @FXML private JFXButton delPrcdBtn;
    @FXML private JFXButton refreshBtn;
    // ProcedureForView TableView et TableColumn
    @FXML private TableView<ProcedureForView> procedureTableView;
    @FXML private TableColumn<ProcedureForView, StatusCheckBox> statusColumn;
    @FXML private TableColumn<ProcedureForView, String> nomProcedure;
    @FXML private TableColumn<ProcedureForView, String> numDepart;
    @FXML private TableColumn<ProcedureForView, String> dateDepart;
    @FXML private TableColumn<ProcedureForView, String> numArrive;
    @FXML private TableColumn<ProcedureForView, String> dateArrive;
    @FXML private TextField procedureSearchTextField;
}
