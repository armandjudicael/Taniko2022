package controller.detailsController;

import controller.viewController.FolderViewController;
import dao.DbUtils;
import model.Enum.NotifType;
import model.Enum.Origin;
import model.Enum.TypeDemande;
import model.pojo.business.other.Dossier;
import model.other.MainService;
import view.Cell.TableCell.DateProcedureCell;
import view.Cell.TableCell.NumeroProcedureCell;
import view.Cell.TableCell.ProcedureNameCell;
import view.Dialog.FormDialog.NewProcedureDialog;
import view.Dialog.Other.Notification;
import view.Dialog.SecurityDialog.EditorSecurity;
import view.Model.Dialog.ProcedureConfirmationDialog;
import view.Model.ViewObject.FolderForView;
import view.Model.ViewObject.ProcedureForView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXProgressBar;
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
import java.time.Instant;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProcedureInfoController implements Initializable{

    @Override public void initialize(URL location, ResourceBundle resources){
        procedureInfoController = this;
        initTableview();
        initializeSearchTextField();
        deleteSearch.visibleProperty().bind(Bindings.not(procedureSearchTextField.textProperty().isEqualTo("")));
        deleteSearch.setOnAction(event -> procedureSearchTextField.setText(""));
        delPrcdBtn.disableProperty().bind(procedureTableView.getSelectionModel().selectedItemProperty().isNull());
        refreshBtn.setOnAction(this::refreshProcedureTableview);
      //  new TabBinding(procedureTableView.getItems(),procedureTab);
    }
    private void refreshProcedureTableview(ActionEvent event){
        MainService.getInstance().launch(new Task<Void>() {
            @Override protected Void call() throws Exception {
                FolderViewController instance = FolderViewController.getInstance();
                TableView<FolderForView> tableView = instance.getTableView();
                FolderForView affair = tableView.getSelectionModel().getSelectedItem();
                instance.initAffaireProcedure(affair,procedureTableView.getItems());
                return null;
            }
            @Override protected void scheduled() {
                procedureProgress.progressProperty().unbind();
                procedureProgress.visibleProperty().unbind();
                procedureProgress.visibleProperty().bind(this.runningProperty());
                procedureProgress.progressProperty().bind(this.progressProperty());
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
        if (source == newPrcdBtn) NewProcedureDialog.getInstance().show();
        else if (source == delPrcdBtn) deleteProcedure();
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
        int[] procedureDeleted = DbUtils.executeBacthRequest(selectedItems);
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
                    ObservableList<ProcedureForView>[] procedureTab = FolderViewController.getProcedureTab();
                    Dossier dossier = AffairDetailsController.getAffaire();
                    TypeDemande typeDemande = dossier.getTypeDemande();
                    items.setAll(typeDemande.equals(TypeDemande.ACQUISITION) ? procedureTab[0] : procedureTab[1] );
                }
            }else {
                ObservableList<ProcedureForView>[] procedureTab = FolderViewController.getProcedureTab();
                Dossier dossier = AffairDetailsController.getAffaire();
                TypeDemande typeDemande = dossier.getTypeDemande();
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
                    @Override public void addListener(ChangeListener<? super StatusCheckBox> listener) {
                    }
                    @Override public void removeListener(ChangeListener<? super StatusCheckBox> listener) {
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
                       int query = DbUtils.updateProcedureName(newProcedureName, procedureForView.getIdProcedure());
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
                        Dossier affair = FolderViewController.getInstance().getTableView().getSelectionModel().getSelectedItem();
                        int query = DbUtils.launchQuery("UPDATE procedure_concerner_affaire SET numArrive ='" + newNumArrive + "' WHERE  procedureId =" + procedureForView.getIdProcedure() + " AND affaireId = " + affair.getId() + ";");
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
                        Dossier dossier = FolderViewController.getInstance().getTableView().getSelectionModel().getSelectedItem();
                        int query = DbUtils.launchQuery("UPDATE procedure_concerner_affaire SET numDepart ='" + newNumDepart + "' WHERE  procedureId =" + procedureForView.getIdProcedure() + " AND affaireId = " + dossier.getId() + ";");
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
                        Dossier affair = FolderViewController.getInstance().getTableView().getSelectionModel().getSelectedItem();
                        int query = DbUtils.launchQuery("UPDATE procedure_concerner_affaire SET dateDepart ='" + newDateDepart + "' WHERE  procedureId =" + procedureForView.getIdProcedure() + " AND affaireId = " + affair.getId() + ";");
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
                        Dossier affair = FolderViewController.getInstance().getTableView().getSelectionModel().getSelectedItem();
                        int query = DbUtils.launchQuery("UPDATE procedure_concerner_affaire SET dateArrive ='" + newDateArrive + "' WHERE  procedureId =" + procedureForView.getIdProcedure() + " AND affaireId = " + affair.getId() + ";");
                        if (query!=0) Notification.getInstance(" Mis à jour effectué avec succès ",NotifType.SUCCESS).showNotif();
                        else Notification.getInstance(" Echec de la mis à jour ",NotifType.WARNING).showNotif();
                       return null;
                    }
                });
            }
        });
    }

   public class StatusCheckBox extends JFXCheckBox{
        public StatusCheckBox() {}
        private void resetProcedure(ProcedureForView procedureForView){
            setSelected(false);
            procedureForView.setNumDepart(null);
            procedureForView.setDateArrive(null);
            procedureForView.setNumArrive(null);
            procedureForView.setDateArrive(null);
        }

        private void onProcedureChecked(){
            // FERMER LE DIALOG
            ProcedureConfirmationDialog.getProcedureConfirmationDialog().close();
            // RECUPERER L'AFFAIRE SELECTIONNER
            Dossier affair = AffairDetailsController.getAffaire();
            TableCell cell = (TableCell)this.getParent();
            TableRow tableRow = (TableRow) cell.getParent();
            int index = tableRow.getIndex();
            ProcedureForView procedureForView = ProcedureInfoController.getInstance().getProcedureTableView().getItems().get(index);
            if (!isSelected()){
                // update the checkbox
                setSelected(true);
                Timestamp from = Timestamp.from(Instant.now());
                procedureForView.setDateDepart(from.toString());
                MainService.getInstance().launch(new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        int status = DbUtils.insertOnAffAndPrcdTable(procedureForView.getIdProcedure(),affair.getId(), from);
                        if (status != 0) Notification.getInstance(" Procedure enregistrer avec succès ", NotifType.SUCCESS).showNotif();
                        else Notification.getInstance(" Echec de la mis à jour ",NotifType.WARNING).showNotif();
                        return null;
                    }
                });
                JFXCheckBox smsAndEmailCheckBox = ProcedureConfirmationDialog.getProcedureConfirmationDialog().getSmsAndEmailCheckBox();
                if (smsAndEmailCheckBox.isSelected()){
                    // SEND E-MAIL AND SMS TO THE APPLICANT
                    smsAndEmailCheckBox.setSelected(false);
                    String message = " La situation actuelle de votre affair est "+procedureForView.getProcedureName();
                 //   EmailFactory.send(message,affair.getDemandeur());
                    // SEND SMS TO THE APPLICANT
                }
            }else{
                resetProcedure(procedureForView);
                MainService.getInstance().launch(new Task<Void>() {
                    @Override protected Void call() throws Exception {
                        int status = DbUtils.launchQuery(" DELETE FROM  procedure_concerner_affaire WHERE (procedureId = " + procedureForView.getIdProcedure() + " AND affaireId = " + affair.getId() + ");");
                        if (status !=0) Notification.getInstance(" Mis à jour enregistrer avec succès ",NotifType.SUCCESS).showNotif();
                        else Notification.getInstance(" Echec de la mis à jour ",NotifType.WARNING).showNotif();
                        return null;
                    }
                });
            }
        }

        @Override public void fire(){ ProcedureConfirmationDialog.getInstance(this,event -> onProcedureChecked()).show(); }
    }

    public static ProcedureInfoController getInstance() {
        return procedureInfoController;
    }

    public class StatusTableCell extends TableCell{
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
    private static ProcedureInfoController procedureInfoController;
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
    @FXML private JFXProgressBar procedureProgress;
}
