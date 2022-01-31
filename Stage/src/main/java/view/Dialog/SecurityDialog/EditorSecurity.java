package view.Dialog.SecurityDialog;

import controller.detailsController.*;
import controller.viewController.LoginController;
import controller.viewController.FolderViewController;
import controller.viewController.MainController;
import Main.InitializeApp;
import javafx.event.EventHandler;
import model.Enum.Origin;
import model.pojo.business.other.Dossier;
import model.pojo.business.other.User;
import model.other.MainService;
import view.Model.ViewObject.FolderForView;
import view.Model.ViewObject.PasswordView;
import view.Model.ViewObject.UserForView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class EditorSecurity extends JFXDialog implements Initializable,EventHandler<ActionEvent> {

    private EditorSecurity() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login/EditorLogin.fxml"));
            loader.setController(this);
            this.setDialogContainer(MainController.getInstance().getMainStackPane());
            this.setContent((AnchorPane) loader.load());
            this.setTransitionType(DialogTransition.CENTER);
            this.setOverlayClose(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initPasswordAndProfil(){
        password.setDisable(false);
        nomRedacteur.setText(user.getUserName());
        profilRedacteur.setFill(new ImagePattern(user.getPhoto()));
    }

    public static void show(Origin origin, ActionEvent event) {
        if (editorSecurity == null)
            editorSecurity = new EditorSecurity();
        actionEvent = event;
        EditorSecurity.setOrigin(origin);
        if (!origin.equals(Origin.USER_DETAILS_LOCK_BTN)) {
            EditorSecurity.setActionEvent(event);
            Dossier dossier = FolderViewController.getInstance().getTableView().getSelectionModel().getSelectedItem();
            User redacteur = dossier.getRedacteur();
            if (redacteur !=null){
                MainService.getInstance().launch(new Task<Void>() {
                    @Override protected void scheduled() {
                       getInstance().password.setDisable(true);
                    }
                    @Override protected void succeeded() {
                     getInstance().initPasswordAndProfil();
                    }
                    @Override protected Void call() throws Exception{
                        ObservableList<UserForView> users = InitializeApp.getUsers();
                        if (!users.isEmpty()){
                            Stream<User> userStream = users.stream().map(UserForView::getEditor);
                            Optional<User> optionalUser = userStream.filter(user1 -> user1.getId() == redacteur.getId()).findFirst();
                            if (optionalUser.isPresent())
                                user = optionalUser.get();
                            else user = dossier.getActualEditor();
                        } else user = dossier.getActualEditor();
                        return null;
                    }
                });
                editorSecurity.show();
            }else {
                AdminSecurity.show(Origin.PROCEDURE_VIEW_BTN,actionEvent);
            }
        }else{
            user = LoginController.getConnectedUser();
            getInstance().initPasswordAndProfil();
            editorSecurity.show();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new PasswordView(eye, password);
        initBinding();
        checkBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            setRemember(t1);
        });
        checkBox.disableProperty().bind(submitBtn.disableProperty());
        submitBtn.setOnAction(this);
        passwordLabel.visibleProperty().bind(password.focusedProperty());
        closebtn.setOnAction(event -> this.close());
    }

    private void initBinding() {
        submitBtn.disableProperty().bind(password.textProperty().isEmpty());
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            submitBtn.disableProperty().bind(password.textProperty().isEmpty());
            wrapper.getStyleClass().removeAll("boxError", "ok");
            incorect.setText("");
            if (!newValue.isEmpty()) {
                if (user.getPassword().contains(newValue)) {
                    wrapper.getStyleClass().add("ok");
                    submitBtn.disableProperty().unbind();
                    submitBtn.setDisable(false);
                } else {
                    submitBtn.disableProperty().unbind();
                    submitBtn.setDisable(true);
                    wrapper.getStyleClass().add("boxError");
                    incorect.setText("Mot de passe Incorrect");
                }
            }
        });
    }

    public void handle(ActionEvent event) {
        this.close();
        password.setText("");
        switch (origin) {
            case EDITOR_VIEW_BTN: {
                RedacteurInfoController.getInstance().launchAction(actionEvent);
            }break;
            case PROCEDURE_VIEW_BTN: {
                ProcedureInfoController.getInstance().launchAction(actionEvent);
            }break;
            case USER_DETAILS_LOCK_BTN: {
                UserDetailsController.getInstance().unlock();
            }break;
            case ALL_AFF_DETAILS_VIEW_BTN: {
                // lancement de la modification
                AffairDetailsController.getInstance().launchAction(actionEvent);
            }break;
            default: {
                // lancement de la modification
                AffairDetailsController.getInstance().launchAction(actionEvent);
            }
        }
    }

    public static void setOrigin(Origin origin) { EditorSecurity.origin = origin; }
    public static void setUser(User user) { EditorSecurity.user = user; }
    public static void setActionEvent(ActionEvent actionEvent) { EditorSecurity.actionEvent = actionEvent; }
    public static Boolean getRemember() { return remember; }
    public static void setRemember(Boolean remember) { EditorSecurity.remember = remember; }
    public static EditorSecurity getInstance() { return editorSecurity; }
    private static ActionEvent actionEvent;
    private static EditorSecurity editorSecurity;
    private static Origin origin;
    private static User user;
    private static FolderForView folderForView;
    private static Boolean remember = false;
    @FXML private Circle profilRedacteur;
    @FXML private Label nomRedacteur;
    @FXML private JFXButton submitBtn;
    @FXML private HBox wrapper;
    @FXML private PasswordField password;
    @FXML private ImageView eye;
    @FXML private JFXButton closebtn;
    @FXML private CheckBox checkBox;
    @FXML private Label incorect;
    @FXML private Label passwordLabel;
}
