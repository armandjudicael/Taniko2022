package view.Helper.Other;
import com.jfoenix.controls.JFXSnackbar;
import controller.formController.otherFormController.MainAffaireFormController;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class SnackBarNotification extends JFXSnackbar{
    private static SnackBarNotification snackBarNotification;
    private static Label label;
    private static SnackbarEvent snackbarEvent;
    public SnackBarNotification() {
        snackBarNotification = this;
    }
    public static SnackBarNotification getInstance() {
        if (snackBarNotification == null) snackBarNotification = new SnackBarNotification();
        return snackBarNotification;
    }
    public void showOn(Pane snackbarContainer,String message){
        initSnackbarContainer(snackbarContainer);
        initMessage(message);
        if (snackbarEvent == null) snackbarEvent = new SnackbarEvent(label,new Duration(2500));
        snackBarNotification.enqueue(snackbarEvent);
    }

    public void showOn(Pane snackbarContainer,String message,AlertType type){
        initSnackbarContainer(snackbarContainer);
        initMessage(message,type);
        if (snackbarEvent == null) snackbarEvent = new SnackbarEvent(label,new Duration(2500));
        snackBarNotification.enqueue(snackbarEvent);
    }

    public void showOn(String message,AlertType type){
        // ADD THE NEW CONTAINER
        if (snackBarNotification.getPopupContainer()==null)
             snackBarNotification.registerSnackbarContainer(MainAffaireFormController.getInstance().getMainAffBorderPane());
        initMessage(message,type);
        if (snackbarEvent == null) snackbarEvent = new SnackbarEvent(label,new Duration(2500));
        snackBarNotification.enqueue(snackbarEvent);
    }

    private void initSnackbarContainer(Pane snackbarContainer) {
        Pane popupContainer = snackBarNotification.getPopupContainer();
        if (popupContainer ==null)
            snackBarNotification.registerSnackbarContainer(snackbarContainer);
        else {
            if (!popupContainer.equals(snackbarContainer)){
                // REMOVE THE OLD CONTAINER
                snackBarNotification.unregisterSnackbarContainer(popupContainer);
                // ADD THE NEW CONTAINER
                snackBarNotification.registerSnackbarContainer(snackbarContainer);
            }
        }
    }

    private void initMessage(String message){
        if (label == null){
            label = new Label(message);
            label.setPrefHeight(50);
            label.setPrefWidth(400);
            label.setGraphicTextGap(20);
            label.setStyle("-fx-background-color: #e5a0a0; -fx-background-radius: 5px;");
            label.setTextFill(Color.WHITE);
            label.setFont(new Font("system",14));
        }
        label.setText(message);
    }

    private void initMessage(String message, AlertType type){
        if (label == null){
            initLabel(message,type);
            return;
        }
        initSnackBartype(type);
        label.setText(message);
    }

    private void initLabel(String message, AlertType type){
        label = new Label(message);
        label.setPrefHeight(50);
        label.setPrefWidth(400);
        label.setGraphicTextGap(20);
        initSnackBartype(type);
        label.setTextFill(Color.WHITE);
        label.setFont(new Font("system",14));
    }

    private void initSnackBartype(AlertType type) {
        if (type == AlertType.ERROR) {
            label.setStyle("-fx-background-color: #e5a0a0; -fx-background-radius: 5px;");
            label.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/img/cancel_40px.png"))));
        }else{
            if (type == AlertType.CONFIRMATION) label.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/img/ok_48px.png"))));
            else label.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/img/info_50px.png"))));
            label.setStyle("-fx-background-color: #79ea9e; -fx-background-radius: 5px;");
        }
    }
}
