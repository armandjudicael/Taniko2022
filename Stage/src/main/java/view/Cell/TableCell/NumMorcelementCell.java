package view.Cell.TableCell;

import model.pojo.business.other.Titre;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;

public class NumMorcelementCell extends TableCell<Titre, String> {

    @Override
    protected void updateItem(String s, boolean b) {
        super.updateItem(s, b);
        setText(s);
        setAlignment(Pos.CENTER);
    }

}
