package controller.viewController;

import controller.detailsController.UserDetailsController;
import DAO.DaoFactory;
import DAO.DbOperation;
import Main.InitializeApp;
import Main.Main;
import Model.Enum.Origin;
import Model.Pojo.Titre;
import Model.Pojo.User;
import Model.Other.MainService;
import View.Dialog.Other.About;
import View.Dialog.SecurityDialog.AdminSecurity;
import View.Model.Dialog.AlertDialog;
import View.Model.ViewObject.AffaireForView;
import View.Helper.Other.DragableStage;
import View.Model.ViewObject.UserForView;
import animatefx.animation.SlideInRight;
import com.jfoenix.controls.JFXButton;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;


public class MainController implements Initializable{

    private static void showUserForm() {
        AnchorPane userForm = MainController.getInstance().getUserDetailsView();
        popOver.hide();
        new SlideInRight(userForm).play();
        userForm.toFront();
    }

    public void minimizeApp(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setIconified(true);
    }

    private void initializeNavigation() {
        initButtonAction();
        initNavigationCircle();
    }

    private void initNavigationCircle(){
        navCircle.setSmooth(true);
        navCircle.setFocusTraversable(true);
        navCircle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/img/tuscany(3).png"))));

    }

    private void initButtonAction(){
        hideBtn.setOnAction(event -> {
            navigationView.toBack();
        });
        menuBtn.setOnAction(event -> {
            navigationView.toFront();
        });
        exitBtn.setOnAction(event -> {
            quitTheProgram(event);
            navigationView.toBack();
        });
        acceuilBtn.setOnAction(event -> showDashboardView(event));
        affairBtn.setOnAction(event -> showAffaireView(event));
        titleBtn.setOnAction(event -> showTitleView(event));
        aboutBtn.setOnAction(event -> {
            navigationView.toBack();
            About.getInstance().show();
        });
        agentBtn.setOnAction(event -> {
            navigationView.toBack();
            if (!isToFront("userview"))
                AdminSecurity.show(Origin.SHOW_USER_PANEL);
        });
        delTextField.setOnAction(event -> searchTextField.setText(""));
    }

    public void animateMenuNavigation(double duration, Node node, int valueX) {
        TranslateTransition translateTransition = new TranslateTransition(new Duration( duration ),node);
        translateTransition.setByX(valueX);
        translateTransition.play();
        translateTransition.setOnFinished(actionEvent -> {
            if (valueX == NAV_POSITION) {
                menuNav.setTranslateX(0);
                navigationView.toBack();
            }
        });
    }

    public void expandApp(ActionEvent actionEvent) {
        final Node source = (Node) actionEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        if (!stage.isMaximized()) {
            stage.setMaximized(true);
            expandIcon.setImage(restoreImg);
        } else {
            stage.setMaximized(false);
            expandIcon.setImage(expandImg);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainController = this;
        mainPanel.toFront();
        dashboardView.toFront();
        menuNav.setOnMouseExited(this::hideNavivation);
        initializeNavigation();
        new DragableStage(LoginController.getMainStage(),appStackPane);
    }

    public void backtoAffaireView() {
        affaireView.toFront();
    }

    private void hyperlinkHandle(ActionEvent event) {
        int userDetailsId = UserDetailsController.getUserDetailsId();
        if (userDetailsId == 0 || userDetailsId != LoginController.getConnectedUser().getId()) // 0 if panel haven' t info
            initializeUserDetails();
        // disable the details panel
        AnchorPane userFormPane = UserDetailsController.getInstance().getUserFormPane();
        if (!userFormPane.isDisabled()) {
            userFormPane.setDisable(true);
            JFXButton lockBtn = UserDetailsController.getInstance().getLockBtn();
            lockBtn.setText("Déverrouiller");
            lockBtn.setGraphic(UserDetailsController.getInstance().getUnlockImg());
        }
        showUserForm();
    }

    @Deprecated
    public void showCurrentUser(ActionEvent actionEvent) {
        JFXButton button = (JFXButton) actionEvent.getSource();
        Node source = button.getGraphic();
        if (popOver == null) {
            initializePopOver(actionEvent);
            popOver.show(source);
        } else {
            if (popOver.isShowing())
                popOver.hide(new Duration(500));
            else popOver.show(source);
        }
    }

    public void showUserView(){
        userview.toFront();
        MainService.getInstance().launch(new Task() {
            @Override
            protected Object call() throws Exception {
                if (InitializeApp.getUsers().isEmpty()) {
                    ObservableList<UserForView> allUsers = DaoFactory.getUserDao().getAllUsers();
                    Platform.runLater(() -> {
                        UserViewController.getInstance().getUserTilePane().getChildren().setAll(allUsers);
                    });
                    InitializeApp.setUsers(allUsers);
                }
                return null;
            }
        });
    }

    @Deprecated
    private void initializePopOver(ActionEvent actionEvent) {

        VBox vBox = new VBox(15);
        vBox.setPrefWidth(266);
        vBox.setPrefHeight(278);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10));
        popOver = new PopOver(vBox);
        userProfil = new Circle();
        userProfil.setStrokeType(StrokeType.CENTERED);
        userProfil.setRadius(70);
        userProfil.setStroke(Color.WHITE);
        User connectedUser = LoginController.getConnectedUser();
        userProfil.setFill(new ImagePattern(connectedUser.getPhoto()));

        Hyperlink hyperlink = new Hyperlink("Mon compte");
        hyperlink.setTextFill(Color.MEDIUMSEAGREEN);
        hyperlink.setCursor(Cursor.HAND);
        hyperlink.setAlignment(Pos.CENTER);
        hyperlink.setVisited(true);
        hyperlink.setFont(new Font("System", 14));
        hyperlink.setOnAction(this::hyperlinkHandle);

        JFXButton button = new JFXButton(" Se déconnecter ");
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.setPrefHeight(35);
        button.setPrefWidth(200);
        button.setStyle("-fx-background-color:  #3CB371;\n" + "-fx-border-radius: 8px;");
        button.setTextFill(Color.WHITE);

        ImageView view = new ImageView(deconnectImg);
        view.setPreserveRatio(true);
        view.setFitHeight(31.0);
        view.setFitWidth(33.0);
        button.setGraphic(view);
        button.setFont(new Font("Microsoft JhengHei", 14));

        button.setOnAction(event -> {
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.hide();
            MainService.getInstance().launch(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    DbOperation.launchQuery(" UPDATE utilisateur SET userStatus = 0 WHERE idUtilisateur=" + connectedUser.getId() + ";");
                    return null;
                }
            });
            Main.getLoginStage().show();
        });

        vBox.getChildren().addAll(userProfil,hyperlink,button);
        popOver.setAnimated(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
    }

    private void initializeUserDetails() {
        User connectedUser = LoginController.getConnectedUser();
        UserDetailsController.getInstance().getNameLabel().setText(connectedUser.getNom());
        UserDetailsController.getInstance().getFirstNameLabel().setText(connectedUser.getPrenom());
        UserDetailsController.getInstance().getFonctionLabel().setText(connectedUser.getFonction());
        UserDetailsController.getInstance().getUserNameLabel().setText(connectedUser.getUserName());
        UserDetailsController.getInstance().getPassWord().setText(connectedUser.getPassword());
        UserDetailsController.getInstance().getTypeLabel().setText(connectedUser.getType());
        UserDetailsController.getInstance().getPhoto().setFill(userProfil.getFill());
        UserDetailsController.setUserDetailsId(connectedUser.getId());
    }

    // SHOW PANEL
    public void showAffaireDetailsView() {
        affaireDetailsView.toFront();
    }

    public void showDashboardView(ActionEvent actionEvent) {
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                DashboardController.getInstance().initDashBoard();
                return null;
            }
        });
        navigationView.toBack();
        dashboardView.toFront();
    }

    // afficher la liste des affaire
    public void showAffaireView(ActionEvent actionEvent) {
        navigationView.toBack();
        if(!isToFront("affaireView")){
            affaireView.toFront();
            MainService.getInstance().launch(new Task() {
                @Override
                protected Object call() throws Exception {
                    if (InitializeApp.getAffaires().isEmpty()) {
                        ObservableList<AffaireForView> observableList = DaoFactory.getAffaireDao().getAffaireBy(String.valueOf(LocalDate.now().getYear()));
                        InitializeApp.setAffaires(observableList);
                        Platform.runLater(() -> {
                            AffairViewController.getInstance().getTableView().getItems().addAll(observableList);
                        });
                    }
                    return null;
                }
            });
        }
    }

    private Boolean isToFront(String paneName){
        ObservableList<Node> children = mainStackPane.getChildren();
        int size = children.size();
        Node node = children.get(size-1);
        return node.getId().equals(paneName);
    }

    public void showTitleView(ActionEvent actionEvent){
        navigationView.toBack();
        if (!isToFront("titleView")){
            titleView.toFront();
            MainService.getInstance().launch(new Task() {
                @Override
                protected Object call() throws Exception {
                    if (InitializeApp.getTitres().isEmpty()) {
                        int year = LocalDate.now().getYear();
                        ObservableList<Titre> alltitle = DaoFactory.getTitreDao().getTitleBy(String.valueOf(year));
                        InitializeApp.setTitres(alltitle);
                        Platform.runLater(() -> {
                            TitleViewController.getInstance().getTableView().getItems().setAll(alltitle);
                        });
                    }
                    return null;
                }
            });
        }
    }

    public void quitTheProgram(ActionEvent actionEvent){
        AlertDialog.getInstance(Alert.AlertType.CONFIRMATION," Etes-vous sure de vouloir quitter l'application ? ").showAndWait()
                .filter(response -> response == ButtonType.OK).ifPresent(buttonType1 -> {
            DbOperation.launchQuery(" UPDATE utilisateur SET userStatus = 0 WHERE utilisateur.idUtilisateur =" + LoginController.getConnectedUser().getId() + ";");
            Platform.exit();
        });
    }

    public AnchorPane getAffaireDetailsView() {
        return affaireDetailsView;
    }
    public AnchorPane getUserDetailsView() {
        return userDetailsView;
    }
    public StackPane getMainStackPane() {
        return mainStackPane;
    }
    public Label getUserName() {
        return userName;
    }
    public Circle getUserProfil() {
        return userProfil;
    }
    public AnchorPane getDashboardView() {
        return dashboardView;
    }
    private void hideNavivation(MouseEvent event) {
        animateMenuNavigation(duration, menuNav, NAV_POSITION);
    }
    public void indisponibleAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, " fonctionnalité indisponible ");
        alert.showAndWait();
    }

    public static MainController getInstance() {
        return mainController;
    }
    public StackPane getAppStackPane() {
        return appStackPane;
    }
    private final int NAV_POSITION = -500;
    private static PopOver popOver = null;
    private final double duration = 200;

    private final Image restoreImg = new Image(getClass().getResourceAsStream("/img/restore_down_30px.png"));
    private final Image expandImg = new Image(getClass().getResourceAsStream("/img/expand_100px.png"));
    private final Image deconnectImg = new Image(getClass().getResourceAsStream("/img/export_50px.png"));

    @FXML private Circle navCircle;
    @FXML private JFXButton hideBtn;
    @FXML private TilePane navTitlePane;
    // ICON
    @FXML private ImageView expandIcon;
    // STACKPANE
    @FXML private StackPane appStackPane;
    @FXML private StackPane mainStackPane;
    // BORDERPANE
    @FXML private BorderPane mainPanel;
    @FXML private BorderPane affaireView;

    @FXML private AnchorPane affaireDetailsView;
    @FXML private AnchorPane dashboardView;
    @FXML private VBox titleView;
    @FXML private AnchorPane navigationView;
    @FXML private AnchorPane userview;
    @FXML private AnchorPane userDetailsView;
    @FXML private AnchorPane menuNav;
    @FXML private Label userName;

    @FXML private JFXButton acceuilBtn;
    @FXML private JFXButton affairBtn;
    @FXML private JFXButton titleBtn;
    @FXML private JFXButton themeBtn;
    @FXML private JFXButton agentBtn;
    @FXML private JFXButton aboutBtn;
    @FXML private JFXButton settingBtn;
    @FXML private JFXButton notificationBtn;
    @FXML private JFXButton exitBtn;
    @FXML private JFXButton menuBtn;
    @FXML private JFXButton delTextField;
    @FXML private TextField searchTextField;
    private Circle userProfil;
    private static MainController mainController = null;
}
