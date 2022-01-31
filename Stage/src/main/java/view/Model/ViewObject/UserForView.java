package view.Model.ViewObject;
import Main.InitializeApp;
import com.jfoenix.controls.JFXButton;
import controller.viewController.UserViewController;
import dao.DaoFactory;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import model.Enum.NotifType;
import model.other.MainService;
import model.pojo.business.other.User;
import view.Dialog.Other.Notification;
import view.Helper.Other.UserPopOver;

public class UserForView extends AnchorPane {
    private static UserForView userForView;
    private static final Font FONT = new Font("Arial Unicode MS", 14);
    private User editor;
    private Circle status;
    private Label nameAndFirstName;
    private  Label fonction;
    private Label affaireCount;
    private  Circle profil;

    public UserForView(User user){
        this.editor = user;
        initUserForView();
        profil = initProfil(user);
        status = initStatus(user);
        JFXButton  button = initOptionButton();
        nameAndFirstName = initNameAndFirstName(user);
        fonction = initUserFonction(user);
        affaireCount = initAffaireCount(user);
        this.getChildren().addAll(profil,button,status,affaireCount,nameAndFirstName,fonction);
    }

    private void initUserForView(){
        userForView = this;
        this.setPrefWidth(300);
        this.setPrefHeight(150.0);
        this.setStyle("-fx-background-color: white;-fx-background-radius: 6px;");
    }

    private Circle initStatus(User user){
        Circle status = new Circle();
        status.setStrokeWidth(1);
        status.setStroke(Color.WHITE);
        status.setRadius(7);
        if (user.getStatus() == 1)
            status.setFill(Color.valueOf("#1c7f22"));
        else status.setFill(Color.DARKGRAY);
        AnchorPane.setTopAnchor(status, 7.0);
        AnchorPane.setLeftAnchor(status, 7.0);
        return status;
    }

    private Label initAffaireCount(User user){
        Label nbAffaire = new Label();
        nbAffaire.setText("affaire traité(s): " + String.valueOf(user.getNbAffaire()));
        nbAffaire.setWrapText(true);
        nbAffaire.setAlignment(Pos.CENTER);
        nbAffaire.setFont(FONT);
        nbAffaire.setTextAlignment(TextAlignment.CENTER);
        AnchorPane.setRightAnchor(nbAffaire, 15.0);
        AnchorPane.setLeftAnchor(nbAffaire, 160.0);
        AnchorPane.setTopAnchor(nbAffaire, 100.0);
        return nbAffaire;
    }

    private Circle initProfil(User user){
        Circle profil = new Circle();
        profil.setRadius(60);
        if (user.getPhoto() != null)
            profil.setFill(new ImagePattern(user.getPhoto()));
        else profil.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/img/male_user_100px.png"))));
        AnchorPane.setTopAnchor(profil, 20.0);
        AnchorPane.setBottomAnchor(profil, 20.0);
        AnchorPane.setLeftAnchor(profil, 14.0);
        AnchorPane.setRightAnchor(profil, 180.0);
        profil.setStrokeWidth(0.0);
        return profil;
    }

    private JFXButton initOptionButton(){
        JFXButton button = new JFXButton();
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.setCursor(Cursor.HAND);
        RotateTransition rotation = new RotateTransition();
        rotation.setAxis(Rotate.Z_AXIS);
        rotation.setByAngle(90);
        Image verticalMenu = new Image(getClass().getResourceAsStream("/img/menu_vertical_50px.png"));
        ImageView imgBtn = new ImageView(verticalMenu);

        imgBtn.setPreserveRatio(true);
        imgBtn.setFitWidth(32);
        imgBtn.setFitHeight(32);

        rotation.setNode(imgBtn);
        rotation.play();
        imgBtn.setSmooth(true);
        button.setGraphic(imgBtn);

        AnchorPane.setTopAnchor(button, -8.0);
        AnchorPane.setRightAnchor(button, 0.0);
        // button action
        button.setOnAction(this::showUserPopup);
        return button;
    }

    private void showUserPopup(ActionEvent event){
        userForView = this;
        JFXButton button = (JFXButton) event.getSource();
        UserPopOver.getInstance().showOn((ImageView)button.getGraphic());
    }

    private Label initNameAndFirstName(User user){
        Label nameAndFirstName = new Label(user.getFullName());
        nameAndFirstName.setWrapText(true);
        nameAndFirstName.setAlignment(Pos.CENTER);
        nameAndFirstName.setFont(FONT);
        nameAndFirstName.setTextAlignment(TextAlignment.CENTER);
        AnchorPane.setRightAnchor(nameAndFirstName, 15.0);
        AnchorPane.setLeftAnchor(nameAndFirstName, 160.0);
        AnchorPane.setTopAnchor(nameAndFirstName, 30.0);
        return nameAndFirstName;
    }

    private Label initUserFonction(User user){
        Label fonction = new Label();
        fonction.setText(user.getProfession());
        fonction.setWrapText(true);
        fonction.setAlignment(Pos.CENTER);
        fonction.setFont(FONT);
        fonction.setTextAlignment(TextAlignment.CENTER);
        AnchorPane.setRightAnchor(fonction, 15.0);
        AnchorPane.setLeftAnchor(fonction, 160.0);
        AnchorPane.setTopAnchor(fonction, 65.0);
        return fonction;
    }

    public static UserForView getInstance() {
        return userForView;
    }

    private void deleteFromTilepane(){
        TilePane userTilePane = UserViewController.getInstance().getUserTilePane();
        userTilePane.getChildren().remove(getInstance());
        InitializeApp.getUsers().remove(getInstance());
    }

    public void deleteUser(){
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int status = DaoFactory.getUserDao().deleteUser(getInstance().getEditor());
                if (status!=0) {
                    Platform.runLater(() ->{
                        Notification.getInstance("Utilisateur supprimer avec succès !", NotifType.SUCCESS).showNotif();
                        deleteFromTilepane();
                    });
                }
                return null;
            }
        });
    }
    public Circle getStatus() {
        return status;
    }
    public Label getNameAndFirstName() {
        return nameAndFirstName;
    }
    public Label getFonction() {
        return fonction;
    }
    public Label getAffaireCount() {
        return affaireCount;
    }
    public Circle getProfil() {
        return profil;
    }
    public User getEditor() {
        return editor;
    }
}
