package controller.viewController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import model.Enum.FolderStatus;
import model.other.MainService;
import model.other.RedactorAffair;
import model.pojo.business.other.User;
import view.Cell.TableCell.IconCell;
import view.Helper.Other.ProgressTaskBinder;
import view.Helper.TableColumn.ProcedureColumnFactory;
import view.Model.ViewObject.UserForView;
import java.net.URL;
import java.util.ListIterator;
import java.util.ResourceBundle;

public class RedacteurDashboardController implements Initializable {

    @Override public void initialize(URL location, ResourceBundle resources) {
        redacteurDashboardController = this;
        initTableView();
        initSearchFilter();
        initViewFilter();
        initButtonAction();
    }

    public  Task<Void> initializeData(User user, Boolean actual){
        Task<Void> task = new Task<Void>() {
            @Override protected Void call() throws Exception{
                ObservableList<RedactorAffair> affairs = FXCollections.observableArrayList();
                if (actual)
                    affairs = user.getActualAffairTraited();
                else affairs = user.getAllAffairTraited();
                ObservableList<RedactorAffair> finalAffairs = affairs;
                Platform.runLater(() -> tableview.getItems().setAll(finalAffairs));
                return null;
            }
            @Override protected void scheduled() {
             new ProgressTaskBinder(progress,this);
            }
        };
        return task;
    }

    private void initButtonAction(){
        deleteButton.setOnAction(event -> searchTextField.clear());
        refreshBtn.setOnAction(event -> {
            MainService.getInstance().launch(initializeData(user, true));
        });
        nextBtn.setOnAction(event ->show(Status.NEXT));
        prevBtn.setOnAction(event ->show(Status.PREV));
    }

    private enum Status{
        NEXT,PREV
    }

    private void show(Status status){
        UserForView userForView = null;
        if (status.equals(Status.NEXT)) {
            if (listIterator.hasNext()) userForView = (UserForView)listIterator.next();
        } else {
            if (listIterator.hasPrevious()) userForView = (UserForView)listIterator.previous();
        }
        if (userForView !=null) showSelectedUser(userForView);
    }

    public void setSelectedUserForView(UserForView userForView){
        // ENREGISTER L'INDEX
        ObservableList<Node> children = UserViewController.getInstance().getUserTilePane().getChildren();
        int currentIndex = children.indexOf(userForView);
        listIterator = children.listIterator(currentIndex+1);
        showSelectedUser(userForView);
    }

    private void showSelectedUser(UserForView userForView){
        user = userForView.getEditor();
        // PROFIL
        profil.setFill(userForView.getProfil().getFill());
        // NAME
        nom.setText(userForView.getNameAndFirstName().getText());
        // FONCTION
        fonction.setText(userForView.getFonction().getText());
        // AFFAIRE
        nbAff.setText(userForView.getAffaireCount().getText());
        MainService.getInstance().launch(initializeData(user,true));
    }

    private void initSearchFilter(){
        searchTextField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.isEmpty()) {
                filteredList = items.filtered(redactorAffair -> redactorAffair.getNumero().toLowerCase().contains(t1.toLowerCase()) || redactorAffair.getFullName().toLowerCase().contains(t1.toLowerCase()));
                if (filteredList.size() > 0)
                    items.retainAll(filteredList);
                else items.setAll(temp);
            } else items.setAll(temp);
        });
    }
    private void initViewFilter(){
        // VIEW FILTER
        String[] type = {"actuelement", "Toutes"};
        viewFilter.getItems().addAll(type);
        viewFilter.setValue(type[0]);
        viewFilter.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if (t1.equals("Toutes"))
                MainService.getInstance().launch(initializeData(user, false));
            else MainService.getInstance().launch(initializeData(user, true));
        });
    }
    private void initTableView() {
        dateDispatch.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override public void addListener(ChangeListener<? super String> changeListener) {}
            @Override public void removeListener(ChangeListener<? super String> changeListener) {}
            @Override public String getValue() {
                return param.getValue().getDispatchDate();
            }
            @Override public void addListener(InvalidationListener invalidationListener) {}
            @Override public void removeListener(InvalidationListener invalidationListener) {}
        });
        applicantName.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override public void addListener(ChangeListener<? super String> changeListener) {}
            @Override public void removeListener(ChangeListener<? super String> changeListener) {}
            @Override public String getValue() {
                return param.getValue().getFullName();
            }
            @Override public void addListener(InvalidationListener invalidationListener) {}
            @Override public void removeListener(InvalidationListener invalidationListener) {}
        });
        numero.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override public void addListener(ChangeListener<? super String> changeListener) {}
            @Override public void removeListener(ChangeListener<? super String> changeListener) {}
            @Override public String getValue() {
                return param.getValue().getNumero();
            }
            @Override public void addListener(InvalidationListener invalidationListener) {}
            @Override public void removeListener(InvalidationListener invalidationListener) {}});
        statusColumn.setCellFactory(param -> new IconCell());
        statusColumn.setCellValueFactory(features -> new ObservableValue<ImageView>() {
            @Override public void addListener(ChangeListener<? super ImageView> changeListener) {}
            @Override public void removeListener(ChangeListener<? super ImageView> changeListener) {}
            @Override public ImageView getValue(){
                final FolderStatus status = features.getValue().getStatus();
                return FolderViewController.getInstance().getStatusIcon(status);
            }
            @Override public void addListener(InvalidationListener invalidationListener) {}
            @Override public void removeListener(InvalidationListener invalidationListener) {}
        });
        situation.setCellValueFactory(param -> ProcedureColumnFactory.createLabel(param.getValue().getSituationProcedure()));
    }
    public static RedacteurDashboardController getInstance() {
        return redacteurDashboardController;
    }
    public static ObservableList<UserForView> getUserListCopy() {
        return userListCopy;
    }
    public static void setUserListCopy(ObservableList<UserForView> userListCopy) {
        RedacteurDashboardController.userListCopy = userListCopy;
    }
    public JFXProgressBar getProgress() {
        return progress;
    }
    @FXML private TableView<RedactorAffair> tableview;
    @FXML private TableColumn<RedactorAffair, String> applicantName;
    @FXML private TableColumn<RedactorAffair, String> dateDispatch;
    @FXML private TableColumn<RedactorAffair, String> numero;
    @FXML private TableColumn<RedactorAffair, ImageView> statusColumn;
    @FXML private TableColumn<RedactorAffair, Label> situation;
    //  JFXBUTTON
    @FXML private JFXButton prevBtn;
    @FXML private JFXButton nextBtn;
    @FXML private JFXButton deleteButton;
    @FXML private JFXButton refreshBtn;
    @FXML private JFXButton prinBtn;
    // PROGRESSBAR
    @FXML private JFXProgressBar progress;
    // TEXTFIELD
    @FXML private TextField searchTextField;
    // COMBOBOX
    @FXML private ComboBox<String> viewFilter;
    @FXML private Label nom;
    @FXML private Label fonction;
    @FXML private Label nbAff;
    @FXML private Circle profil;
    private static User user;
    private static ListIterator<Node> listIterator;
    private static RedacteurDashboardController redacteurDashboardController;
    private static ObservableList<RedactorAffair> temp = FXCollections.observableArrayList();
    private static FilteredList<RedactorAffair> filteredList = null;
    private static ObservableList<RedactorAffair> items = FXCollections.observableArrayList();
    private static ObservableList<UserForView> userListCopy = FXCollections.observableArrayList();
}
