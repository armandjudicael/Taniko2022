package view.Cell.TableCell;

import model.pojo.business.Titre;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;

public class NumeroTitreCell extends TableCell<Titre, String> {

    @Override protected void updateItem(String s, boolean b) {
        super.updateItem(s, b);
        setText(s);
        setAlignment(Pos.CENTER);
    }

}
