package Model.serviceManager;

import javafx.concurrent.Task;

public class TaskService extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        return null;
    }

    @Override
    protected void scheduled() {
        super.scheduled();
    }

    @Override
    protected void running() {
        super.running();
    }

    @Override
    protected void succeeded() {
        super.succeeded();
    }

    @Override
    protected void updateProgress(long l, long l1) {
        super.updateProgress(l, l1);
    }

    @Override
    protected void updateProgress(double v, double v1) {
        super.updateProgress(v, v1);
    }

    @Override
    protected void updateMessage(String s) {
        super.updateMessage(s);
    }

    @Override
    protected void updateTitle(String s) {
        super.updateTitle(s);
    }
}
