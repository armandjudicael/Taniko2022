package View.Dialog.Other;

import Controller.DetailsController.AffairDetailsController;
import Controller.DetailsController.EditorViewController;
import Controller.ViewController.AffairViewController;
import Controller.ViewController.MainController;
import DAO.DaoFactory;
import DAO.DbOperation;
import Main.InitializeApp;
import Model.Enum.AutoCompleteComboboxType;
import Model.Enum.NotifType;
import Model.Pojo.Affaire;
import Model.Pojo.User;
import Model.serviceManager.MainService;
import View.Cell.ListCell.DispatchListcell;
import View.Model.AffaireForView;
import View.Model.EditorForView;
import View.Model.UserForView;
import View.helper.AutoCompleteCombobox;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DispatchDialog extends JFXDialog implements Initializable {

    private static DispatchDialog dispatchDialog;
    @FXML private JFXComboBox<User> combobox;
    @FXML private JFXButton closeBtn;
    @FXML private JFXButton closeBtn1;
    @FXML private JFXButton addBtn;

    private DispatchDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Form/newDispatchDialog.fxml"));
            loader.setController(this);
            this.setContent((AnchorPane) loader.load());
            this.setDialogContainer(MainController.getInstance().getMainStackPane());
            this.setTransitionType(DialogTransition.CENTER);
            this.setOverlayClose(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DispatchDialog getInstance() {
        if (dispatchDialog == null) {
            dispatchDialog = new DispatchDialog();
        }
        return dispatchDialog;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        closeBtn.setOnAction(event -> this.close());

        closeBtn1.setOnAction(event -> this.close());

        addBtn.disableProperty().bind(combobox.valueProperty().isNull());

        addBtn.setOnAction(event -> {
            this.close();
            MainService.getInstance().launch(insertTask());
        });

        combobox.setCellFactory(userListView -> new DispatchListcell());
        new AutoCompleteCombobox<User>(combobox, new Predicate<User>(){
            @Override public boolean test(User user){
                String text = combobox.getEditor().getText();
                if ( user.getNom().toLowerCase().startsWith(text) || user.getPrenom().toLowerCase().startsWith(text))
                    return true;
                else return false;
            }
        }, AutoCompleteComboboxType.USER);

    }

    private Task insertTask() {
        return new Task() {
            @Override
            protected Void call() throws Exception {
                User user = combobox.getValue();
                // ajout nouveau redacteur dans la liste
                EditorViewController.getInstance().getEditorTableView().getItems().add(new EditorForView(Timestamp.valueOf(LocalDateTime.now()), user.getFullName(), user.getPhoto(), user.getId()));
                Affaire affaire = AffairDetailsController.getAffaire();
                // recuperation de l'identifiant de l'utilisateur
                int userId = user.getId();
                // insertion dans la table des dispatch
                if (DbOperation.insertOnDispatchTable(user,affaire) == 1) {
                    String message = user.getPrenom() + " est le nouveau rédacteur de cette affaire ";
                    Platform.runLater(() -> {
                        Notification.getInstance(message, NotifType.SUCCESS).show();
                    });
                }
                combobox.setPromptText("");
                return null;
            }
        };
    }
}
