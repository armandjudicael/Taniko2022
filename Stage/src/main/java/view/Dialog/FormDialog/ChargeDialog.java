package view.Dialog.FormDialog;

import com.jfoenix.controls.JFXDialog;
import controller.viewController.MainController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChargeDialog extends JFXDialog implements Initializable {
    private static ChargeDialog chargeDialog;

    private ChargeDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Form/Other/newChargeDialog.fxml"));
            loader.setController(this);
            this.setContent((AnchorPane) loader.load());
            this.setDialogContainer(MainController.getInstance().getMainStackPane());
            this.setTransitionType(DialogTransition.CENTER);
            this.setOverlayClose(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ChargeDialog getInstance(){
        if (chargeDialog == null)
            chargeDialog = new ChargeDialog();
        return chargeDialog;
    }

    @Override public void initialize(URL location, ResourceBundle resources) {

    }

}
