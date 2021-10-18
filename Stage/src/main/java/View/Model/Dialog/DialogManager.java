package View.Model.Dialog;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class DialogManager extends Dialog<JFXButton> implements Initializable {
    private static DialogManager dialogManager;
    private static DialogPane dialogPane;

    public DialogManager() {
        if (dialogPane==null)
            dialogPane = new DialogPane();
        this.setDialogPane(dialogPane);
        this.initStyle(StageStyle.UNDECORATED);
        this.initModality(Modality.APPLICATION_MODAL);
    }

    public static DialogManager getInstance(DialogType type,String message){
        if (dialogManager == null)
            dialogManager = new DialogManager();
        dialogPane.setContent(dialogManager.initDialogPane(type));
        return dialogManager;
    }

    @Override public void initialize(URL location, ResourceBundle resources){

    }

    private enum DialogType{
        ERROR,WARNING,CONFIRMATION,SIMPLE_DIALOG
    }

    private Node initDialogPane(DialogType type){
        switch (type){
                case ERROR:{

                }break;
                case WARNING:{

                }break;
                case CONFIRMATION:{

                }break;
                case SIMPLE_DIALOG:{

                }break;
        }
        return null;
    }
}
