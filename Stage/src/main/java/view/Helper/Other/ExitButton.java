package view.Helper.Other;

import com.jfoenix.controls.JFXButton;
import controller.viewController.LoginController;
import dao.DbUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import view.Model.Dialog.AlertDialog;

public class ExitButton implements EventHandler<ActionEvent> {
    public ExitButton(JFXButton exitBtn) {
        exitBtn.setOnAction(this);
    }
    @Override public void handle(ActionEvent event) {
        AlertDialog.getInstance(Alert.AlertType.CONFIRMATION," Etes-vous sure de vouloir quitter l'application ? ").showAndWait()
                .filter(response -> response == ButtonType.OK).ifPresent(buttonType1 -> {
            DbUtils.launchQuery(" UPDATE utilisateur SET userStatus = 0 WHERE utilisateur.idUtilisateur =" + LoginController.getConnectedUser().getId() + ";");
            Platform.exit();
        });
    }
}
