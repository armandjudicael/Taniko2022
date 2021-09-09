package Controller.ViewController;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class OfflineModeController {

    public void fermerApp(ActionEvent actionEvent) {
        final Node source = (Node) actionEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
