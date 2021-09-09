package View.Cell.TableCell;

import Model.Enum.ColumType;
import View.Model.ProcedureForView;
import View.helper.ProcedureEditPopOver;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;

public class ProcedureNameCell extends TableCell<ProcedureForView, String> {

    public ProcedureNameCell() {

    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (getText() != null && !getText().isEmpty()) {
            ProcedureEditPopOver.getInstance(ColumType.NAME).showOnCell(this);
        }
    }

    @Override
    public void commitEdit(String s) {
        super.commitEdit(s);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    protected void updateItem(String s, boolean b) {
        super.updateItem(s, b);
        setText(s);
        setAlignment(Pos.CENTER);
        setContentDisplay(isEditing() ? ContentDisplay.GRAPHIC_ONLY : ContentDisplay.TEXT_ONLY);
    }
}
