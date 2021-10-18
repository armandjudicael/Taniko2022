package View.Dialog.Other;

import Controller.ViewController.MainController;
import Model.Enum.NotifType;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.Notifications;

public class Notification{

    public Notification() {

    }

    public static Notification getInstance(String message, NotifType type) {
        if (notification == null)
            notification = new Notification();
        if (notifications == null) {
            notifications = Notifications.create().owner(MainController.getInstance().getMainStackPane().getScene().getWindow());
            imageView = new ImageView();
            notifications.graphic(imageView);
        }
        notifications.text(message);
        switch (type) {
            case SUCCESS: { imageView.setImage(successImg); }break;
            case WARNING: { imageView.setImage(errorImg); } break;
            case INFORMATION: { imageView.setImage(notificationImg); }break;
        }
        return notification;
    }

    public void showNotif() {
       if (Platform.isFxApplicationThread())  notifications.show();
       else Platform.runLater(() -> notifications.show());
    }

    private static Image successImg = new Image(Notification.class.getResourceAsStream("/img/ok_48px.png"));
    private static Image errorImg = new Image(Notification.class.getResourceAsStream("/img/error_50px.png"));
    private static Image notificationImg = new Image(Notification.class.getResourceAsStream("/img/info_50px.png"));
    private static Notifications notifications;
    private static Notification notification;
    private static ImageView imageView;
}
