package view.Helper.Other;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import view.Model.Dialog.AlertDialog;

public class IndisponibleFunctionality implements EventHandler<ActionEvent> {
    public IndisponibleFunctionality(JFXButton button){
        button.setOnAction(this);
    }
    @Override
    public void handle(ActionEvent event) {
        AlertDialog.getInstance(Alert.AlertType.INFORMATION,"Fonctionnalité non disponible").showAndWait();
    }
}
