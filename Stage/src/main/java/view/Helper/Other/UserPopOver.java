package view.Helper.Other;

import com.jfoenix.controls.JFXButton;
import controller.viewController.MainController;
import controller.viewController.RedacteurDashboardController;
import controller.viewController.UserViewController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.Enum.Origin;
import org.controlsfx.control.PopOver;
import view.Dialog.SecurityDialog.AdminSecurity;
import view.Model.ViewObject.UserForView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserPopOver extends PopOver implements Initializable{
    private static UserPopOver userPopOver;
    public UserPopOver(){
        userPopOver = this;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View/Other/popOver/UserPopOver.fxml"));
            loader.setController(this);
            VBox load = (VBox)loader.load();
            this.setContentNode(load);
            this.setArrowLocation(ArrowLocation.TOP_CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static UserPopOver getInstance() {
        if (userPopOver == null)
            userPopOver = new UserPopOver();
        return userPopOver;
    }

    public void showOn(ImageView node){
        if (this.isShowing())
            this.hide();
        else this.show(node);
    }

    @Override public void initialize(URL location, ResourceBundle resources) {
        listAffBtn.setOnAction(this::showAffList);
        delUserBtn.setOnAction(event -> {
            this.hide();
            AdminSecurity.show(Origin.DELETE_USER);
        });
    }

    public void showAffList(ActionEvent event){
        this.hide();
        UserForView instance = UserForView.getInstance();
        // Desactiver le boutton a propos
        JFXButton button = (JFXButton)this.getOwnerNode().getParent();
        button.setVisible(false);
        //AFFICHER LE PANNEAU
        MainController.getInstance().getUserAffaireView().toFront();
        // ENVOYER L'UTILISATEUR
        RedacteurDashboardController.getInstance().setSelectedUserForView(instance);
    }

    @FXML private JFXButton listAffBtn;
    @FXML private JFXButton delUserBtn;
}

