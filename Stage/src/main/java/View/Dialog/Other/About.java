package View.Dialog.Other;

import Controller.ViewController.MainController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class About extends JFXDialog implements Initializable{

    private static About about;
    @FXML private JFXButton closeBtn;
    @FXML private Hyperlink facebook;
    @FXML private Hyperlink linkedin;
    @FXML private Hyperlink mail;
    @FXML private Hyperlink github;
    @FXML private Circle profil;


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
        if (about == null) {
            about = new About();
        }
        return about;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closeBtn.setOnAction(event -> this.close());
        profil.setSmooth(true);
        profil.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/img/photo.jpg"))));
    }
}
