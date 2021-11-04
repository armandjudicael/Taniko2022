package view.Cell.TableCell;

import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;

public class IconCell extends TableCell {
    @Override protected void updateItem(Object item, boolean empty){
        if (item != null && !empty) {
            this.setAlignment(Pos.CENTER);
            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            this.setGraphic((ImageView)item);
        } else {
            this.setText(null);
            this.setGraphic(null);
        }
    }

}