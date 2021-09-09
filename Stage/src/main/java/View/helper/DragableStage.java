package View.helper;

import Main.Main;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DragableStage {

    private double xoffset = 0;

    private double yoffset = 0;

    public DragableStage(Stage newStage, Pane pane) {

            pane.setOnMousePressed((e) -> {
                if (!newStage.isMaximized()){
                    xoffset = e.getSceneX();
                    yoffset = e.getSceneY();
                }
            });

            pane.setOnMouseDragged((e) -> {
                if (!newStage.isMaximized()){
                    newStage.setX(e.getScreenX() - xoffset);
                    newStage.setY(e.getScreenY() - yoffset);
                    newStage.setOpacity(0.6f);
                }
            });

            pane.setOnDragDone((e) -> {
                if (!newStage.isMaximized())
                   newStage.setOpacity(1.0f);
            });

            pane.setOnMouseReleased((e) -> {
                if (!newStage.isMaximized())
                   newStage.setOpacity(1.0f);
            });
    }
}
