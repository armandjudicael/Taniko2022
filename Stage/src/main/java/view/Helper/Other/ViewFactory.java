package view.Helper.Other;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class ViewFactory<T> {
    public Node create(String viewUrl,T viewController) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(viewUrl));
        if (viewController!=null) fxmlLoader.setController(viewController);
        return fxmlLoader.load();
    }
}
