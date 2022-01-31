package controller.viewController;

import controller.detailsController.UserDetailsController;
import dao.DbUtils;
import Main.Main;
import model.pojo.business.other.User;
import model.other.MainService;

import org.controlsfx.control.PopOver;
import view.Helper.Other.DragableStage;
import view.Helper.Other.ExitButton;
import animatefx.animation.SlideInRight;
import com.jfoenix.controls.JFXButton;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;


public class MainController implements Initializable {


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
        initButton();
        new DragableStage(LoginController.getMainStage(), appStackPane);
    }

    private void initButton() {
        // CLOSE APP BUTTON
        new ExitButton(closeAppBtn);
        // MenuBtn
        menuBtn.setOnAction(event -> menuView.toFront());
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
                    DbUtils.launchQuery(" UPDATE utilisateur SET userStatus = 0 WHERE idUtilisateur=" + connectedUser.getId() + ";");
                    return null;
                }
            });
            Main.getLoginStage().show();
        });
        vBox.getChildren().addAll(userProfil, hyperlink, button);
        popOver.setAnimated(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
    }

    private void initializeUserDetails() {
        User connectedUser = LoginController.getConnectedUser();
        UserDetailsController.getInstance().getNameLabel().setText(connectedUser.getNom());
        UserDetailsController.getInstance().getFonctionLabel().setText(connectedUser.getProfession());
        UserDetailsController.getInstance().getUserNameLabel().setText(connectedUser.getUserName());
        UserDetailsController.getInstance().getPassWord().setText(connectedUser.getPassword());
        UserDetailsController.getInstance().getTypeLabel().setText(connectedUser.getUtilisateurType());
        UserDetailsController.getInstance().getPhoto().setFill(userProfil.getFill());
        UserDetailsController.setUserDetailsId(connectedUser.getId());
    }

    public void showAffaireDetailsView() {
        affaireDetailsView.toFront();
    }

    // GETTER
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

    public AnchorPane getMenuView() {
        return menuView;
    }

    public BorderPane getAffaireView() {
        return affaireView;
    }

    public VBox getTitleView() {
        return titleView;
    }

    public AnchorPane getUserview() {
        return userview;
    }

    public static MainController getInstance() {
        return mainController;
    }

    public StackPane getAppStackPane() {
        return appStackPane;
    }

    public AnchorPane getUserAffaireView() {
        return userAffaireView;
    }

    private static PopOver popOver = null;
    private final Image restoreImg = new Image(getClass().getResourceAsStream("/img/restore_down_30px.png"));
    private final Image expandImg = new Image(getClass().getResourceAsStream("/img/expand_100px.png"));
    private final Image deconnectImg = new Image(getClass().getResourceAsStream("/img/export_50px.png"));
    // ICON
    @FXML
    private ImageView expandIcon;
    // STACKPANE
    @FXML
    private StackPane appStackPane;
    @FXML
    private StackPane mainStackPane;
    // BORDERPANE
    @FXML
    private BorderPane mainPanel;
    @FXML private BorderPane affaireView;
    @FXML private AnchorPane menuView;
    @FXML private AnchorPane affaireDetailsView;
    @FXML private AnchorPane dashboardView;
    @FXML private VBox titleView;
    @FXML private AnchorPane userAffaireView;
    @FXML private AnchorPane userview;
    @FXML private AnchorPane userDetailsView;
    @FXML private Label userName;
    @FXML private JFXButton menuBtn;
    @FXML private JFXButton closeAppBtn;
    private Circle userProfil;
    private static MainController mainController = null;
}
