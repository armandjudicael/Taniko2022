package view.Dialog.SecurityDialog;

import controller.detailsController.AffairDetailsController;
import controller.detailsController.ConnexeInfoController;
import controller.detailsController.RedacteurInfoController;
import controller.detailsController.ProcedureInfoController;
import controller.viewController.AffairViewController;
import controller.viewController.MainController;
import controller.viewController.TitleViewController;
import dao.DaoFactory;
import model.Enum.Origin;
import model.other.MainService;
import view.Dialog.Other.DispatchDialog;
import view.Model.ViewObject.AffaireForView;
import view.Model.ViewObject.PasswordView;
import view.Model.ViewObject.UserForView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminSecurity extends JFXDialog implements Initializable {

    private static AdminSecurity adminSecurity;
    private static String errorText;
    private static String editorPassword = "";
    private static Origin securityOrigin;
    private static Boolean isValid = false;

    @FXML private Label label;
    @FXML private JFXButton submitBtn;
    @FXML private JFXButton closeBtn;
    @FXML private PasswordField password;
    @FXML private ImageView eye;
    @FXML private HBox wrapper;
    @FXML private Label passwordLabel;

    private static  ActionEvent actionEvent;
    private static MouseEvent mouseEvent;

    public AdminSecurity() {
        try {
            this.setDialogContainer(MainController.getInstance().getMainStackPane());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login/AdminLogin.fxml"));
            loader.setController(this);
            this.setContent((AnchorPane) loader.load());
            this.setTransitionType(DialogTransition.CENTER);
            this.setOverlayClose(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void show(Origin origin) {
        if (adminSecurity == null) {
            adminSecurity = new AdminSecurity();
        }
        securityOrigin = origin;
        launchInitTask(origin);
        adminSecurity.show();
    }

    public static void show(Origin origin,ActionEvent event) {
        if (adminSecurity == null) {
            adminSecurity = new AdminSecurity();
        }
        securityOrigin = origin;
        actionEvent = event;
        launchInitTask(origin);
        adminSecurity.show();
    }


    public static void show(Origin origin,MouseEvent event) {
        if (adminSecurity == null) {
            adminSecurity = new AdminSecurity();
        }
        securityOrigin = origin;
        mouseEvent = event;
        launchInitTask(origin);
        adminSecurity.show();
    }


    public static void launchInitTask(Origin origin) {

        MainService.getInstance().launch(new Task<Void>(){
            @Override protected Void call() throws Exception {
                switch (origin) {
                    case SHOW_AFFAIR_DETAILS:{
                        AffaireForView selectedItem = AffairViewController.getInstance().getTableView().getSelectionModel().getSelectedItem();
                        // we find the editor password in the database
                        editorPassword = DaoFactory.getAffaireDao().getEditorPassWordBy(selectedItem.getId());
                        errorText = "Veuillez vous authentifier en tant qu'administrateur ou rédacteur";
                    }break;
                    default: {
                        errorText = "Veuillez vous authentifier en tant qu'administrateur";
                    }
                }
                return null;
            }
        });
    }

    public static AdminSecurity getInstance() {
        return adminSecurity;
    }

    @Override public void initialize(URL location, ResourceBundle resources) {
        bindButton();
        passwordLabel.visibleProperty().bind(password.focusedProperty());
        new PasswordView(eye, password);
        submitBtn.setOnAction(event -> MainService.getInstance().launch(ckeckPasswordTask()));
        closeBtn.setOnAction(event -> {
            password.setText("");
            this.close();
        });
    }

    public void fermerApp(ActionEvent actionEvent) {
        password.setText("");
        this.close();
    }

    public Label getLabel() {
        return label;
    }

    private void bindButton() {

        submitBtn.disableProperty().bind(password.textProperty().isEmpty().or(password.textProperty().isNull()));

        password.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            wrapper.getStyleClass().removeAll("boxError");
            label.setText("");
        });

        password.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.isEmpty()){
                wrapper.getStyleClass().removeAll("boxError");
                label.setText("");
            }
        });

    }

    private Task<Void> ckeckPasswordTask() {

        return new Task<Void>(){

            @Override protected Void call() throws Exception{
                String passwordEntered = password.getText();
                if (!passwordEntered.isEmpty()) {
                    if (editorPassword != null)
                        isValid = DaoFactory.getUserDao().checkAdmin(passwordEntered) == 1 || editorPassword.equals(passwordEntered);
                    else isValid = DaoFactory.getUserDao().checkAdmin(passwordEntered) == 1;
                }else isValid = false;
                return null;
            }

            @Override protected void succeeded(){
                if (isValid){
                    password.clear();
                    AdminSecurity.getInstance().close();
                    switch (securityOrigin) {
                        case REMOVE_EDITOR_FROM_LIST_BTN: {
                            RedacteurInfoController.getInstance().deleteUserFromList();
                        }break;
                        case DELETE_USER: {
                            UserForView.getInstance().deleteUser();
                        }break;
                        case EDIT_TITLE:{
                            TitleViewController.getInstance().initTitleEdit();
                        }break;
                        case SHOW_ALL_AFFAIR_TRAITED_BY_EDITOR:{

                        }break;
                        case DISPACTH_BTN: {
                            DispatchDialog.getInstance().show();
                        }break;
                        case SHOW_USER_PANEL: {
                            MainController.getInstance().showUserView();
                        }break;
                        case SHOW_AFFAIR_DETAILS: {
                            // afficher les details de l'affaire
                            AffairViewController.getInstance().showDetails();
                        }break;
                        case DELETE_AFFAIR: {
                            AffairViewController.getInstance().deleteAffaire();
                        }break;
                        case CREATE_TITLE: {
                            AffairDetailsController.getInstance().creerTitre();
                        }break;
                        case ALL_AFF_DETAILS_VIEW_BTN:{
                            AffairDetailsController.getInstance().launchAction(actionEvent);
                        }break;

                        case REJETER_AFFAIRE:{
                            ConnexeInfoController.getInstance().rejeterAffaireConnexe();
                        }break;

                        case DELETE_TITLE:{
                            TitleViewController.getInstance().deleteTitle();
                        }break;

                        case PROCEDURE_VIEW_BTN:{
                            ProcedureInfoController.getInstance().launchAction(actionEvent);
                        }break;

                        case SHOW_AFFAIRE_DETAILS_FROM_EX_AFFAIRE:{
                            TitleViewController.getInstance().showExAffaire(mouseEvent);
                        }break;

                        default: {
                            // afficher les details de l'affaire
                            AffairViewController.getInstance().showDetails();
                        }
                    }
                } else {
                    Platform.runLater(() -> {
                        wrapper.getStyleClass().add("boxError");
                        label.setText(errorText);
                    });
                }
            }
        };
    }
}
