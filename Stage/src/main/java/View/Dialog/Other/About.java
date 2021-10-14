package View.Dialog.Other;

import Controller.ViewController.MainController;
import Main.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class About extends JFXDialog implements Initializable{

    private About() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View/Other/About.fxml"));
            loader.setController(this);
            this.setContent((AnchorPane) loader.load());
            this.setDialogContainer(MainController.getInstance().getMainStackPane());
            this.setTransitionType(DialogTransition.CENTER);
            this.setOverlayClose(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static About getInstance() {
        if (about == null)
            about = new About();
        return about;
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        closeBtn.setOnAction(event -> this.close());
        initHyperlinkAction();
        initProfil();
    }

    private void initHyperlinkAction(){
        github.setOnAction(event -> Main.getMainApplication().getHostServices().showDocument("http://www.github.com"));
        mail.setOnAction(event -> Main.getMainApplication().getHostServices().showDocument("https://mail.google.com"));
        linkedin.setOnAction(event -> Main.getMainApplication().getHostServices().showDocument("http://www.linkedin.com/login"));
        facebook.setOnAction(event -> Main.getMainApplication().getHostServices().showDocument("https://www.facebook.com/profile.php?id=100073077854036"));
    }

    private void initProfil(){
        profil.setSmooth(true);
        profil.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/img/photo.jpg"))));
    }

    private static About about;
    @FXML private JFXButton closeBtn;
    @FXML private Hyperlink facebook;
    @FXML private Hyperlink linkedin;
    @FXML private Hyperlink mail;
    @FXML private Hyperlink github;
    @FXML private Circle profil;
}
