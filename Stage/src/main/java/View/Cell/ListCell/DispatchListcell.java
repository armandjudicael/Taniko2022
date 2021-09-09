package View.Cell.ListCell;

import Model.Pojo.User;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class DispatchListcell extends ListCell<User> {

    private final String style = DispatchListcell.class.getResource("/css/listCell.css").toExternalForm();

    @Override protected void updateItem(User user, boolean b){
        super.updateItem(user, b);
        if (b || user==null){
            setText(null);
            setGraphic(null);
        }else {
            setText(user.getFullName());
            // PROFIL
            Circle profil = new Circle();
            profil.setRadius(28);
            profil.setFill(new ImagePattern(user.getPhoto()));
            setGraphic(profil);
           this.getStylesheets().add(style);
        }
    }
}
