package View.Dialog.FormDialog;

import controller.viewController.MainController;
import Model.Enum.*;
import com.jfoenix.controls.*;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainAffaireForm extends JFXDialog implements Initializable {
    private static MainAffaireForm mainAffaireForm;
    private static AffaireStatus status = AffaireStatus.RUNNING ;
    public static MainAffaireForm getInstance(){
        if (mainAffaireForm == null){
            mainAffaireForm = new MainAffaireForm();
        }
        return mainAffaireForm;
    }
    private MainAffaireForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Form/AffaireForm/MainAffaireForm.fxml"));
            this.setContent((BorderPane) loader.load());
            this.setDialogContainer(MainController.getInstance().getMainStackPane());
            this.setTransitionType(DialogTransition.CENTER);
            this.setOverlayClose(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override public void initialize(URL location, ResourceBundle resources){
        mainAffaireForm = this;
    }
}
