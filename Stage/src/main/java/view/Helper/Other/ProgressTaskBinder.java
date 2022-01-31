package view.Helper.Other;

import com.jfoenix.controls.JFXProgressBar;
import javafx.concurrent.Task;

public class ProgressTaskBinder {
    public ProgressTaskBinder(JFXProgressBar progressBar, Task<Void> task) {
        progressBar.progressProperty().unbind();
        progressBar.visibleProperty().unbind();
        progressBar.visibleProperty().bind(task.runningProperty());
        progressBar.progressProperty().bind(task.progressProperty());
    }
}
