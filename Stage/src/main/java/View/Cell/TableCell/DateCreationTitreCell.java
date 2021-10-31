package View.Cell.TableCell;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;

public class DateCreationTitreCell extends TableCell {
    @Override protected void updateItem(Object o, boolean b) {
        super.updateItem(o, b);
        setGraphic(null);
        if ( o==null || b ){
            setText(null);
        }else {
            setText((String) o);
            setAlignment(Pos.CENTER);
        }
    }
}
