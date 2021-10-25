package View.Dialog.Other;

import controller.viewController.AffairViewController;
import controller.viewController.MainController;
import Model.Enum.AffaireStatus;
import Model.Pojo.User;
import Model.Other.RedactorAffair;
import Model.Other.MainService;
import View.Cell.TableCell.IconCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditorTrack extends JFXDialog implements Initializable {

    private static EditorTrack editorTrack;
    private static User editor;
    private static ObservableList<RedactorAffair> temp = FXCollections.observableArrayList();
    private static FilteredList<RedactorAffair> filteredList = null;
    private static ObservableList<RedactorAffair> items = FXCollections.observableArrayList();
    @FXML
    private TableView<RedactorAffair> tableview;
    @FXML
    private TableColumn<RedactorAffair, String> applicantName;
    @FXML
    private TableColumn<RedactorAffair, String> dateDispatch;
    @FXML
    private TableColumn<RedactorAffair, String> numero;
    @FXML
    private TableColumn<RedactorAffair, String> affairStatus;
    @FXML
    private TableColumn<RedactorAffair, ImageView> statusColumn;
    @FXML
    private JFXButton closeStage;
    @FXML
    private JFXButton refreshBtn;
    @FXML
    private Label redactorName;
    @FXML
    private ComboBox<String> viewFilter;
    @FXML
    private TextField searchTextField;
    @FXML
    private JFXButton deleteButton;

    private EditorTrack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View/Other/AffairEditor.fxml"));
            loader.setController(this);
            this.setContent((AnchorPane) loader.load());
            this.setDialogContainer(MainController.getInstance().getMainStackPane());
            this.setTransitionType(DialogTransition.CENTER);
            this.setOverlayClose(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static EditorTrack getInstance(User user) {
        editor = user;
        if (editorTrack == null)
            editorTrack = new EditorTrack();
        editorTrack.redactorName.setText(user.getFullName());
        MainService.getInstance().launch(initializeData(editor, true));
        editorTrack.getSearchTextField().setText("");

        return editorTrack;
    }

    public static Task<Void> initializeData(User user, Boolean actual) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<RedactorAffair> affairs = FXCollections.observableArrayList();
                if (actual)
                    affairs = user.getActualAffairTraited();
                else affairs = user.getAllAffairTraited();
                ObservableList<RedactorAffair> finalAffairs = affairs;
                Platform.runLater(() -> items.setAll(finalAffairs));
                temp = finalAffairs;
                return null;
            }

            @Override
            protected void scheduled() {

            }

            @Override
            protected void succeeded() {

            }

            @Override
            protected void updateProgress(long l, long l1) {
                super.updateProgress(l, l1);
            }

            @Override
            protected void updateProgress(double v, double v1) {
                super.updateProgress(v, v1);
            }
        };
        return task;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deleteButton.visibleProperty().bind(Bindings.not(searchTextField.textProperty().isEqualTo("")));
        searchTextField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.isEmpty()) {
                filteredList = items.filtered(redactorAffair -> redactorAffair.getNumero().toLowerCase().contains(t1.toLowerCase()) || redactorAffair.getFullName().toLowerCase().contains(t1.toLowerCase()));
                if (filteredList.size() > 0)
                    items.retainAll(filteredList);
                else items.setAll(temp);
            } else items.setAll(temp);
        });
        editorTrack = this;
        initializeTableView();
        closeStage.setOnAction(event -> this.close());
        tableview.itemsProperty().bind(new SimpleObjectProperty<>(items));
        // VIEW FILTER
        String[] type = {"actuelement", "Toutes"};
        viewFilter.getItems().addAll(type);
        viewFilter.setValue(type[0]);
        viewFilter.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if (t1.equals("Toutes"))
                MainService.getInstance().launch(initializeData(editor, false));
            else MainService.getInstance().launch(initializeData(editor, true));
        });
        // YEAR FILTER
        refreshBtn.setOnAction(event -> MainService.getInstance().launch(initializeData(editor, true)));
    }

    private void initializeTableView() {
        dateDispatch.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> changeListener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> changeListener) {
            }

            @Override
            public String getValue() {
                return param.getValue().getDispatchDate();
            }

            @Override
            public void addListener(InvalidationListener invalidationListener) {

            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {

            }
        });
        applicantName.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> changeListener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> changeListener) {

            }

            @Override
            public String getValue() {
                return param.getValue().getFullName();
            }

            @Override
            public void addListener(InvalidationListener invalidationListener) {

            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {

            }
        });
        numero.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> changeListener) {
            }

            @Override
            public void removeListener(ChangeListener<? super String> changeListener) {

            }

            @Override
            public String getValue() {
                return param.getValue().getNumero();
            }

            @Override
            public void addListener(InvalidationListener invalidationListener) {

            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {

            }
        });
        statusColumn.setCellFactory(param -> new IconCell());
        statusColumn.setCellValueFactory(features -> new ObservableValue<ImageView>() {
            @Override
            public void addListener(ChangeListener<? super ImageView> changeListener) {

            }

            @Override
            public void removeListener(ChangeListener<? super ImageView> changeListener) {

            }

            @Override
            public ImageView getValue() {
                final AffaireStatus status = features.getValue().getStatus();
                return AffairViewController.getInstance().getStatusIcon(status);
            }

            @Override
            public void addListener(InvalidationListener invalidationListener) {

            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {

            }
        });
    }

    public TableView<RedactorAffair> getTableview() {
        return tableview;
    }

    public Label getRedactorName() {
        return redactorName;
    }

    public JFXButton getRefreshBtn() {
        return refreshBtn;
    }

    public TextField getSearchTextField() {
        return searchTextField;
    }
}
