package Main;

import DAO.ConnectionFactory;
import Model.serviceManager.MainService;
import com.textmagic.sdk.RestClient;
import com.textmagic.sdk.RestException;
import com.textmagic.sdk.resource.instance.TMNewMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Optional;

public class Main extends Application {

    public static Boolean isReacheable = false;
    private static Stage loginStage;
    final static String finalHost = "192.168.1.50";
    final static String localHost = "127.0.0.1";

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getLoginStage() {
        return loginStage;
    }

    public static Boolean serverIsReacheable(String ip) {
        try {
             InetAddress byName = InetAddress.getByName(ip);
             if (byName.isReachable(2000))
                 return true;
             else return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showLogin() {
        try {
            loginStage = new Stage();
            Parent root = FXMLLoader.load(this.getClass().getResource("/fxml/Login/MainLogin.fxml"));
            loginStage.initStyle(StageStyle.UNDECORATED);
            Scene loginScene = new Scene(root);
            loginScene.setFill(Color.TRANSPARENT);
            loginStage.setScene(loginScene);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void start(Stage primaryStage) throws Exception{
        MainService.getInstance().launch(new Task<Void>() {
            @Override protected void succeeded() {
                showLogin();
            }
            @Override protected Void call() throws Exception {
                checkReachability(localHost);
                return null;
            }
        });

    }

    private void checkReachability(String host){

        isReacheable = serverIsReacheable(host);

        if (isReacheable) {
            Connection connection = new ConnectionFactory().getConnection();
            runServerPing(host);
        }

    }

    public static void runServerPing( String host ) {

        // RUN A DEAMON THREAD WHO PING THE LOCAL SERVER
        ScheduledService<Boolean> scheduledService = new ScheduledService<Boolean>() {
            @Override
            protected Task<Boolean> createTask() {
                return new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        isReacheable = serverIsReacheable(host);
                        return isReacheable;
                    }
                };
            }
        };

        scheduledService.stateProperty().addListener((observableValue, state, t1) -> {
            switch (t1) {

                case SCHEDULED:{

                    if (!isReacheable){

                        if (scheduledService.cancel()){
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle(" Problème de connexion ");
                                alert.initModality(Modality.WINDOW_MODAL);
                                alert.setHeaderText(" Vous etes déconnecté ");
                                alert.setContentText(" 1 - veuillez verifier votre cable reseau ou wifi !\n" +
                                        " 2 - Si le probleme persiste , veuillez contacter le responsable informatique !");
                                Optional<ButtonType> buttonType = alert.showAndWait();
                                if (buttonType.get().equals(ButtonType.OK)) {
                                    Platform.exit();
                                } else Platform.exit();
                            });
                        }

                    }

                }break;
            }
        });

        scheduledService.setPeriod(new Duration(500));
        scheduledService.setRestartOnFailure(true);
        scheduledService.start();
    }
}
