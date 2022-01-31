package FormTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Objects;

public class AffaireFormTest extends Application{

    public static void main(String []args) throws IOException, InterruptedException{
      launch(args);
    }
    @Override public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects
                .requireNonNull(getClass().
                        getResource("/fxml/Form/AffaireForm/newDesign.fxml")));
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
}
