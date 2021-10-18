package View.Model.Dialog;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;

public class AlertDialog extends Alert{
    private static AlertDialog dialog;
    public AlertDialog(AlertType alertType) {
        super(alertType);
        imageView = new ImageView();
        this.setGraphic(imageView);
        this.setHeaderText("");
        this.setTitle("");
        this.initModality(Modality.WINDOW_MODAL);
    }

    private void initDialog(Alert.AlertType type, String message){
        switch (type){
            case CONFIRMATION:{imageView.setImage(confImg);}break;
            case INFORMATION:{imageView.setImage(infoImg);}break;
            case WARNING:{imageView.setImage(warnImg); }break;
            case ERROR :{ imageView.setImage(errorImg); }break;
        }
        this.setContentText(message);
    }

    public static AlertDialog getInstance(Alert.AlertType type,String message){
        if (dialog==null){
            dialog = new AlertDialog(type);
        }
        dialog.initDialog(type,message);
        return dialog;
    }

    private ImageView imageView;
    private  Image confImg = new Image(getClass().getResourceAsStream("/img/help_50px.png"));
    private  Image errorImg = new Image(getClass().getResourceAsStream("/img/high_priority_48px.png"));
    private  Image infoImg = new Image(getClass().getResourceAsStream("/img/info_50px.png"));
    private  Image warnImg = new Image(getClass().getResourceAsStream("/img/box_important_50px.png"));
}
