package View.Model;

import Controller.ViewController.UserViewController;
import DAO.DaoFactory;
import Main.InitializeApp;
import Model.Enum.NotifType;
import Model.Enum.Origin;
import Model.Pojo.User;
import Model.serviceManager.MainService;
import View.Dialog.Other.EditorTrack;
import View.Dialog.Other.Notification;
import View.Dialog.SecurityDialog.AdminSecurity;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.controlsfx.control.PopOver;

public class UserForView extends AnchorPane {

    private static PopOver popOver;
    private static UserForView userForView;
    private User editor;
    private Label nomEtPrenom;
    private Label fonction;
    private Label nbAffaire;

    public UserForView(User user) {

        userForView = this;
        this.editor = user;
        this.setPrefWidth(230.0);
        this.setPrefHeight(280.0);
        // PROFIL
        Circle profil = new Circle();
        profil.setRadius(60);
        if (user.getPhoto() != null)
            profil.setFill(new ImagePattern(user.getPhoto()));
        else profil.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/img/male_user_100px.png"))));
        AnchorPane.setTopAnchor(profil, 14.0);
        AnchorPane.setBottomAnchor(profil, 146.0);
        AnchorPane.setLeftAnchor(profil, 55.0);
        AnchorPane.setRightAnchor(profil, 55.0);
        // STATUS
        Circle status = new Circle();
        status.setStrokeWidth(1);
        status.setStroke(Color.WHITE);
        status.setRadius(7);
        if (user.getStatus() == 1)
            status.setFill(Color.valueOf("#1c7f22"));
        else status.setFill(Color.DARKGRAY);
        AnchorPane.setTopAnchor(status, 7.0);
        AnchorPane.setLeftAnchor(status, 7.0);

        // BUTTON
        JFXButton button = new JFXButton();
        button.setButtonType(JFXButton.ButtonType.RAISED);
        Image verticalMenu = new Image(getClass().getResourceAsStream("/img/menu_vertical_50px.png"));
        ImageView imgBtn = new ImageView(verticalMenu);

        imgBtn.setPreserveRatio(true);
        imgBtn.setFitWidth(32);
        imgBtn.setFitHeight(36);

        imgBtn.setSmooth(true);
        button.setGraphic(imgBtn);
        AnchorPane.setTopAnchor(button, 0.0);
        AnchorPane.setRightAnchor(button, -8.0);
        button.setOnAction(this::optionCliqued);
        Font font = new Font("Arial Unicode MS", 13);

        // NOM ET PRENOM
        nomEtPrenom = new Label();
        nomEtPrenom.setText(user.getFullName());
        nomEtPrenom.setWrapText(true);
        nomEtPrenom.setAlignment(Pos.CENTER);
        nomEtPrenom.setFont(font);
        nomEtPrenom.setTextAlignment(TextAlignment.CENTER);
        AnchorPane.setRightAnchor(nomEtPrenom, 0.0);
        AnchorPane.setLeftAnchor(nomEtPrenom, 0.0);
        AnchorPane.setTopAnchor(nomEtPrenom, 141.0);

        // FONCTION
        fonction = new Label();
        fonction.setText(user.getFonction());
        fonction.setWrapText(true);
        fonction.setAlignment(Pos.CENTER);
        fonction.setFont(font);
        fonction.setTextAlignment(TextAlignment.CENTER);
        AnchorPane.setRightAnchor(fonction, 5.0);
        AnchorPane.setLeftAnchor(fonction, 5.0);
        AnchorPane.setBottomAnchor(fonction, 80.0);

        // NOMBRE D'AFFAIRE
        nbAffaire = new Label();
        nbAffaire.setText("Nombre d'affaire traité(s): " + String.valueOf(DaoFactory.getAffaireDao().getNbAffaireWhereActualEditorIs(user.getId())));
        nbAffaire.setWrapText(true);
        nbAffaire.setAlignment(Pos.CENTER);
        nbAffaire.setFont(font);
        nbAffaire.setTextAlignment(TextAlignment.CENTER);

        AnchorPane.setRightAnchor(nbAffaire, 5.0);
        AnchorPane.setLeftAnchor(nbAffaire, 5.0);
        AnchorPane.setBottomAnchor(nbAffaire, 20.0);

        this.setStyle("-fx-background-color: white;-fx-background-radius: 6px;");
        this.getChildren().addAll(profil, button, status, nbAffaire, nomEtPrenom, fonction);
    }

    public static UserForView getInstance() {
        return userForView;
    }

    private void optionCliqued(ActionEvent actionEvent) {
        // affectation de l'userview selectionné
        userForView = this;

        if (popOver == null){

            popOver = new PopOver();
            JFXButton listAffBtn = new JFXButton("affaires traité(s)");
            JFXButton supprimer = new JFXButton("supprimer");

            listAffBtn.setOnAction(event -> {
                popOver.hide();
                EditorTrack.getInstance(getInstance().getEditor()).show();
            });

            supprimer.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent event) {
                    popOver.hide();
                    AdminSecurity.show(Origin.DELETE_USER);
                }
            });

            VBox box = new VBox();

            box.setSpacing(10);

            box.getChildren().addAll(listAffBtn, supprimer);

            box.setVgrow(listAffBtn, Priority.ALWAYS);

            box.setVgrow(supprimer, Priority.ALWAYS);

            popOver.setContentNode(box);

            popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);

        }

        if (popOver.isShowing())
            popOver.hide();
        else {
            JFXButton btn = (JFXButton) actionEvent.getSource();
            popOver.show(btn.getGraphic());
        }
    }

    private void deleteFromTilepane(){
        TilePane userTilePane = UserViewController.getInstance().getUserTilePane();
        userTilePane.getChildren().remove(getInstance());
        InitializeApp.getUsers().remove(getInstance());
    }

    public void deleteUser() {

        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                final int status = DaoFactory.getUserDao().deleteUser(getInstance().getEditor());
                if (status!=0) {
                    Platform.runLater(() ->{
                        String message = "Utilisateur supprimer avec succès !";
                        Notification.getInstance(message, NotifType.SUCCESS).show();
                        deleteFromTilepane();
                    });
                }
                return null;
            }
        });

    }

    public User getEditor() {
        return editor;
    }

    public Label getNomEtPrenom() {
        return nomEtPrenom;
    }

    public Label getFonction() {
        return fonction;
    }
}
