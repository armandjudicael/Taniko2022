package view.Dialog.Other;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;

public class DialogCloserButton{
    public DialogCloserButton(JFXButton button,JFXDialog dialog){
        button.setOnAction(event -> dialog.close());
    }
}
