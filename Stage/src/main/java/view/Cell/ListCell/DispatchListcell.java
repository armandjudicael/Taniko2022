package view.Cell.ListCell;

import model.pojo.business.other.User;
import javafx.scene.control.ListCell;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

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
