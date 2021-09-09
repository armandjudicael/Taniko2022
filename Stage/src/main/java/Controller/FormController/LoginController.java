package Controller.FormController;

import Controller.ViewController.AffairViewController;
import Controller.ViewController.MainController;
import DAO.ConnectionFactory;
import DAO.DaoFactory;
import DAO.DbOperation;
import Main.Main;
import Model.Pojo.User;
import Model.serviceManager.MainService;
import View.helper.DragableStage;
import View.Model.PasswordView;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    static LoginController loginController;
    private static Stage mainStage = null;
    @FXML private AnchorPane parent;
    @FXML private AnchorPane adminPane;
    @FXML private AnchorPane settingPane;
    @FXML private AnchorPane deconnectedPane;
    @FXML private AnchorPane loginPane;
    // HBOX
    @FXML private HBox passwordBox;
    @FXML private HBox usernameBox;
    @FXML private HBox adminPasswordBox;
    // JFXBUTTON
    @FXML private JFXButton connectButton;
    @FXML private JFXButton go2LoginBtn;
    @FXML private JFXButton configBtn;
    @FXML private JFXButton exitBtn;
    @FXML private JFXButton validateAdminBtn;
    @FXML private JFXButton testConnectionBtn;
    // TEXTFIELD
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private TextField ip;
    @FXML private TextField port;
    @FXML private TextField databaseName;
    @FXML private TextField dbUser;
    @FXML private PasswordField adminPassword;
    @FXML private PasswordField dbPassword;
    // LABEL
    @FXML
    private Label notificationLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label adminPasswordLabel;
    @FXML
    private Label connexionNotifLabel;
    // CHECKBOX
    @FXML
    private CheckBox remember;
    // IMAGEVIEW
    @FXML
    private ImageView adminEye;
    @FXML
    private ImageView eye;
    // pane
    @FXML private ProgressBar progressBar;
    @FXML private JFXButton sttBtn;
    @FXML private JFXButton backToLoginBtn;

    private double xoffset = 0;
    private double yoffset = 0;
    private static User connectedUser = null;
    private static Boolean isReacheable = false;
    private static Connection connection = null;

    final Image finishedImg = new Image(AffairViewController.class.getResourceAsStream("/img/ok_20px.png"));
    final Image rejectImg = new Image(AffairViewController.class.getResourceAsStream("/img/cancel_20px.png"));

    private void checkUser() {
        User userChecked = DaoFactory.getUserDao().findByUserNameAndPassword(username.getText(), password.getText());
        if (userChecked != null) {
            this.setConnectedUser(userChecked);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    openApp();
                    Main.getLoginStage().hide();
                    initConnectedUser(userChecked);
                    deleteForm();
                }
            });
            DbOperation.launchQuery(" UPDATE utilisateur SET userStatus = 1 WHERE utilisateur.idUtilisateur =" + userChecked.getId() + ";");
        } else {
            Platform.runLater(() -> {
                usernameBox.getStyleClass().add("boxError");
                passwordBox.getStyleClass().add("boxError");
                notificationLabel.setText(" nom d'utilisateur ou mot de passe incorrect ");
            });
        }
    }

    @FXML
    private void fermerApp(ActionEvent actionEvent) {
        final Node source = (Node) actionEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void initConnectedUser(User connectedUser ) {
        MainController.getInstance().getUserName().setText(connectedUser.getPrenom());
        Circle userProfil = MainController.getInstance().getUserProfil();
        if (userProfil != null) {
            userProfil.fillProperty().unbind();
            userProfil.setFill(new ImagePattern(connectedUser.getPhoto()));
        }

    }

    private void openApp() {

        if (mainStage == null) {
            mainStage = new Stage(StageStyle.UNDECORATED);
            mainStage.setMaximized(true);
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/View/PrincipalView/MainApp.fxml"));
                mainStage.setScene(new Scene(root));
                mainStage.show();
                // on initialize l'entete après l'affichage de la fenetre
                initializeTitledPane();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else {
            MainController.getInstance().getDashboardView().toFront();
            mainStage.show();
        }

    }

    private void initializeTitledPane() {
        TitledPane titledPane = AffairViewController.getInstance().getTitledPane();
        if (titledPane != null) {
            Pane arrow = (Pane) titledPane.lookup(".titled-pane > .title > .arrow-button");
            arrow.translateXProperty().bind(titledPane.widthProperty().subtract(20));
        }
    }

    private void deleteForm() {

        if (remember.isSelected()) {
            password.setText("");
        } else {
            password.setText("");
            username.setText("");
        }

    }

    private void initializeUserNameAndPassword() {

        username.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                notificationLabel.setText("");
                usernameBox.getStyleClass().add("ok");
            } else usernameBox.getStyleClass().removeAll("ok", "boxError");
        });

        password.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                passwordBox.getStyleClass().add("ok");
                notificationLabel.setText("");
            } else passwordBox.getStyleClass().removeAll("ok", "boxError");
        });
    }

    private void makeStageDragable(){

        parent.setOnMousePressed((e) -> {
            xoffset = e.getSceneX();
            yoffset = e.getSceneY();
        });

        parent.setOnMouseDragged((e) -> {
            Main.getLoginStage().setX(e.getScreenX() - xoffset);
            Main.getLoginStage().setY(e.getScreenY() - yoffset);
            Main.getLoginStage().setOpacity(0.6f);
        });

        parent.setOnDragDone((e) -> {
            Main.getLoginStage().setOpacity(1.0f);
        });

        parent.setOnMouseReleased((e) -> {
            Main.getLoginStage().setOpacity(1.0f);
        });
    }

    public void initialize(URL location, ResourceBundle resources) {

        if (Main.isReacheable){
            loginPane.toFront();
        }else deconnectedPane.toFront();

        loginController = this;


        adminPassword.textProperty().addListener((observableValue, s, t1) -> {
            adminPasswordBox.getStyleClass().removeAll("boxError");
            if (!"Aj30071999".equals(t1)) {
                adminPasswordBox.getStyleClass().add("boxError");
            } else adminPasswordBox.getStyleClass().removeAll("boxError");
        });

        new DragableStage(Main.getLoginStage(),parent);
        initializeUserNameAndPassword();
        // initialize eye and password
        new PasswordView(eye, password);
        new PasswordView(adminEye, adminPassword);
        initValidateAdminBtn();
        initTestConnectionBtn();
        initButton();
        initProgressBar();
        initLabel();
    }

    private void initLabel() {
        userNameLabel.visibleProperty().bind(username.focusedProperty());
        passwordLabel.visibleProperty().bind(password.focusedProperty());
        adminPasswordLabel.visibleProperty().bind(adminPassword.focusedProperty());
    }

    private void initValidateAdminBtn() {
        validateAdminBtn.disableProperty().bind(adminPassword.textProperty().isNotEqualTo("Aj30071999"));
        validateAdminBtn.setOnAction(event -> settingPane.toFront());
    }

    private void initTestConnectionBtn() {
        testConnectionBtn.disableProperty().bind(dbPassword.textProperty().isEmpty());
        testConnectionBtn.setOnAction(event -> {
            MainService.getInstance().launch(runConnectionTask());
        });
    }

    private Task<Void> runConnectionTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String ipAddress = ip.getText();
                String serverPort = port.getText();
                String database = databaseName.getText();
                String user = dbUser.getText();
                String password = dbPassword.getText();
                if (Main.serverIsReacheable( ipAddress)){
                    connection = ConnectionFactory.getConnection(user, password, ipAddress, serverPort, database);
                    Main.runServerPing(ipAddress);
                }
                return null;
            }

            @Override
            protected void running() {
                progressBar.progressProperty().unbind();
                progressBar.progressProperty().bind(this.progressProperty());
            }

            @Override
            protected void succeeded() {
                progressBar.progressProperty().unbind();
                progressBar.setProgress(0);
                updateAdminNotificationLabel();
            }
        };
    }

    private void initProgressBar() {
        progressBar.setStyle("-fx-accent:#1aa848;");
    }

    private void updateAdminNotificationLabel(){
        ImageView graphic = (ImageView) connexionNotifLabel.getGraphic();
        if (connection != null) {
            go2LoginBtn.setDisable(false);
            graphic.setImage(finishedImg);
            connexionNotifLabel.setText("connexion avec la base de donné avec succès ");
            connexionNotifLabel.setTextFill(Color.GREEN);
        } else {
            go2LoginBtn.setDisable(true);
            graphic.setImage(rejectImg);
            connexionNotifLabel.setText("La connexion avec la base de donné à échoué ");
            connexionNotifLabel.setTextFill(Color.RED);
        }
        connexionNotifLabel.setVisible(true);
    }

    private void initButton() {

        sttBtn.setOnAction(event -> adminPane.toFront());
        backToLoginBtn.setOnAction(event -> loginPane.toFront());
        configBtn.setOnAction(event -> adminPane.toFront());
        exitBtn.setOnAction(event -> fermerApp(event));
        go2LoginBtn.setOnAction(event -> loginPane.toFront());

        connectButton.setOnAction(event -> MainService.getInstance().launch(runLoginTask()));
        connectButton.requestFocus();
        connectButton.disableProperty().bind(Bindings.or(username.textProperty().isEmpty(), password.textProperty().isEmpty()));
        connectButton.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER && !connectButton.disableProperty().get()) {
                connectButton.arm();
                MainService.getInstance().launch(runLoginTask());
            }
        });
    }

    private Task<Void> runLoginTask() {

        return new Task<Void>() {

            @Override protected void succeeded() {
                progressBar.progressProperty().unbind();
                progressBar.setProgress(0);
                connectButton.disableProperty().bind(Bindings.or(username.textProperty().isEmpty(), password.textProperty().isEmpty()));
            }
            @Override protected Void call() throws Exception {
                if (connectedUser == null) {
                    checkUser();
                } else {
                    if (connectedUser.getUserName().equals(username.getText()) && connectedUser.getPassword().equals(password.getText())) {
                        Platform.runLater(() -> {
                            openApp();
                            deleteForm();
                        });
                        DbOperation.launchQuery(" UPDATE utilisateur SET userStatus = 1 WHERE utilisateur.idUtilisateur =" + connectedUser.getId() + ";");
                    } else {
                        checkUser();
                    }
                }
                return null;
            }
            @Override protected void running() {
                progressBar.progressProperty().unbind();
                progressBar.progressProperty().bind(this.progressProperty());
            }
            @Override protected void scheduled() {
                connectButton.disableProperty().unbind();
                connectButton.setDisable(true);
            }
        };
    }

    @FXML
    void minimizeLogin(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setIconified(true);
    }
    public static User getConnectedUser() {
        return connectedUser;
    }
    public static void setConnectedUser(User connectedUser) {
        LoginController.connectedUser = connectedUser;
    }
    public static LoginController getInstance() {
        return loginController;
    }
    public static Stage getMainStage() {
        return mainStage;
    }
}
