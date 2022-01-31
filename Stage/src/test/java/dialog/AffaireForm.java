package dialog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.http.io.SessionOutputBuffer;

import java.util.Objects;

public class AffaireForm extends Application{

    public AffaireForm() {
        super();
    }

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println(" INITIALISATION OF THE DIALOG ");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println(" CLOSE THE DIALOG ");
    }

    @Override public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass()
                .getResource("/fxml/Form/AffaireForm/MainAffaireForm.fxml")));
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

}
