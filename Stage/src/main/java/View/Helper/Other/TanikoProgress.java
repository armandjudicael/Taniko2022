package View.Helper.Other;

import Controller.ViewController.MainController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXProgressBar;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TanikoProgress extends JFXDialog implements Initializable {
    private static Task<Void> runningTask;

    public static TanikoProgress getInstance(Task<Void> task,String text){
        if (tanikoProgress == null) tanikoProgress = new TanikoProgress();
        runningTask = task;
        tanikoProgress.initProgress(task,text);
        return tanikoProgress;
    }

    private TanikoProgress() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View/Other/progress.fxml"));
            loader.setController(this);
            this.setContent((AnchorPane) loader.load());
            this.setDialogContainer(MainController.getInstance().getMainStackPane());
            this.setTransitionType(DialogTransition.CENTER);
            this.setOverlayClose(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void initialize(URL location, ResourceBundle resources) {
       closeBtn.setOnAction(event -> {
           this.close();
           runningTask.cancel();
       });
       hideBtn.setOnAction(event -> this.close());
    }

    private void initProgress(Task<Void> task,String text){
        context.setText(text);
        task.setOnRunning(event -> this.show());
        task.setOnSucceeded(event -> this.close());
        progress.progressProperty().unbind();
        progress.progressProperty().bind(task.progressProperty());
    }

    private static TanikoProgress tanikoProgress;
    @FXML private JFXProgressBar progress;
    @FXML private JFXButton closeBtn;
    @FXML private JFXButton hideBtn;
    @FXML private Text context;
}
