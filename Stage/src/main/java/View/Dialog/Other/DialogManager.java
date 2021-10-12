package View.Dialog.Other;

import javafx.event.EventDispatchChain;
import javafx.scene.Node;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.StageStyle;

public class DialogManager extends Dialog<String>{
    public DialogManager() {
        DialogPane dialogPane = new DialogPane();
        dialogPane.setContent(new Node() {});
        this.setDialogPane(dialogPane);
        this.initStyle(StageStyle.UNDECORATED);
    }
}
