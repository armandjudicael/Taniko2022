package View.Dialog.Other;

import Controller.ViewController.MainController;
import Model.Enum.NotifType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.Notifications;

public class Notification {

    private static Image successImg = new Image(Notification.class.getResourceAsStream("/img/ok_48px.png"));
    private static Image errorImg = new Image(Notification.class.getResourceAsStream("/img/error_50px.png"));
    private static Image notificationImg = new Image(Notification.class.getResourceAsStream("/img/info_50px.png"));

    private static Notifications notifications;
    public static Notifications getInstance(String message, NotifType type) {
        if (notifications == null) {
            notifications = Notifications.create().owner(MainController.getInstance().getMainStackPane().getScene().getWindow());
        }
        notifications.text(message);
        switch (type) {
            case SUCCESS: {
                notifications.graphic(new ImageView(successImg));
            }
            break;
            case WARNING: {
                notifications.graphic(new ImageView(errorImg));
            }
            break;
            case INFORMATION: {
                notifications.graphic(new ImageView(notificationImg));
            }
            break;
        }
        return notifications;
    }

    public static void show(int status) {
        if (status == 1)
            Notification.getInstance(" modification enregistré avec succès ", NotifType.SUCCESS).show();
        else Notification.getInstance(" enrégistrement echoué ", NotifType.WARNING).show();
    }
}
