package View.Dialog.Other;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class DialogCloserButton{
    public DialogCloserButton(JFXButton button,JFXDialog dialog){
        button.setOnAction(event -> dialog.close());
    }
}
