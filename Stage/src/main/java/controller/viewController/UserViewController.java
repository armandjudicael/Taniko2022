package controller.viewController;

import dao.DaoFactory;
import Main.InitializeApp;
import javafx.scene.Node;
import model.other.MainService;
import view.Dialog.FormDialog.UserFrom;
import view.Helper.Other.ProgressTaskBinder;
import view.Model.ViewObject.UserForView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;

import java.net.URL;
import java.util.ResourceBundle;

public class UserViewController implements Initializable{

    public static UserViewController getInstance() {
        return userViewController;
    }

    @Override public void initialize(URL location, ResourceBundle resources) {
        userViewController = this;
        refreshBtn.setOnAction(event -> refresh());
        userTilePane.getChildren().addAll(InitializeApp.getUsers());
        deleteButton.visibleProperty().bind(Bindings.not(searchTextField.textProperty().isEqualTo("")));
        searchTextField.textProperty().addListener(this::changed);
    }

    @FXML void addUser(ActionEvent event) {
        if (userFrom == null)
            userFrom = new UserFrom();
        else UserFrom.getInstance().resetForm();
        userFrom.show();
    }

    @FXML
    void deleteSearch(ActionEvent event) {
        searchTextField.setText("");
    }

    public void refresh(){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<UserForView> allUsers = DaoFactory.getUserDao().getAllUsers();
                Platform.runLater(() -> userTilePane.getChildren().setAll(allUsers));
                return null;
            }
            @Override protected void scheduled() {
                new ProgressTaskBinder(userProgress,this);
                refreshBtn.setDisable(true);
            }
            @Override protected void succeeded() {
                refreshBtn.setDisable(false);
            }
        };
        MainService.getInstance().launch(task);
    }

    private void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
        if (!newValue.isEmpty()) {
            filtered = InitializeApp.getUsers().filtered(userForView -> {
                String lowerCaseFilter = newValue.toLowerCase();
                if (userForView.getEditor().getNom().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else return false;
            });
            userTilePane.getChildren().setAll(filtered);
        } else userTilePane.getChildren().setAll(InitializeApp.getUsers());
    }

    private UserForView getNode(int i){
        return (UserForView)userTilePane.getChildren().get(i);
    }

    public TilePane getUserTilePane() {
        return userTilePane;
    }
    private static UserViewController userViewController;
    private static UserFrom userFrom;
    private static FilteredList<UserForView> filtered;
    @FXML private TextField searchTextField;
    @FXML private JFXButton deleteButton;
    @FXML private TilePane userTilePane;
    @FXML private JFXButton refreshBtn;
    @FXML private JFXProgressBar userProgress;
}
