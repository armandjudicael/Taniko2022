package view.Cell.TableCell;

import view.Model.ViewObject.EditorForView;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.shape.Circle;

public class PhotoColumnCell extends TableCell<EditorForView, Circle> {
    @Override protected void updateItem(Circle circle, boolean empty) {
        super.updateItem(circle,empty);
        if (empty || circle==null){
            setGraphic(null);
        }else {
            this.setGraphic(circle);
            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            this.setAlignment(Pos.CENTER);
        }
        this.setText(null);
    }
}