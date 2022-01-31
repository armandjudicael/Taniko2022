package view.Helper.Other;

import com.jfoenix.controls.JFXButton;
import javafx.animation.RotateTransition;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class MenuButtonAnimation implements EventHandler<MouseEvent> {
    private RotateTransition rotate1;
    public MenuButtonAnimation (JFXButton button){
        button.setOnMouseEntered(this);
        rotate1 = new RotateTransition();
        Node node = button.getGraphic();
        rotate1.setAxis(Rotate.Z_AXIS);
        rotate1.setByAngle(30);
        rotate1.setCycleCount(2);
        rotate1.setDuration(Duration.millis(50));
        rotate1.setAutoReverse(true);
        rotate1.setNode(node);
    }
    @Override public void handle(MouseEvent event) {
        rotate1.play();
    }
}
