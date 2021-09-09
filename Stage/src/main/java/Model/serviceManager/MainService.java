package Model.serviceManager;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class MainService<Void> extends Service<Void> {
    private static MainService mainService;

    private MainService() {

    }

    public static MainService getInstance() {
        if (mainService == null) {
            mainService = new MainService();
        }
        return mainService;
    }

    @Override
    protected Task<Void> createTask() {
        return null;
    }

    public void launch(Task<Void> task) {
        if (!this.isFxApplicationThread()){
            Platform.runLater(() -> {
                this.executeTask(task);
            });
        } else {
            this.executeTask(task);
        }
    }

    boolean isFxApplicationThread() {
        return Platform.isFxApplicationThread();
    }
}
