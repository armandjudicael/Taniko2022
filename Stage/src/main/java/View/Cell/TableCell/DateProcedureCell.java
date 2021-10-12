package View.Cell.TableCell;

import Model.Enum.ColumType;
import View.Helper.Other.ProcedureEditPopOver;
import View.Model.ProcedureForView;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;


public class DateProcedureCell extends TableCell<ProcedureForView, String> {

    @Override public void commitEdit(String s) {
        super.commitEdit(s);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override public void startEdit() {
        super.startEdit();
        ProcedureForView item = this.getTableRow().getItem();
        if (item.isChecked())
            ProcedureEditPopOver.getInstance(ColumType.DATE).showOnCell(this);
    }

    @Override public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override protected void updateItem(String s, boolean b) {
        super.updateItem(s, b);
        setText(s);
        setAlignment(Pos.CENTER);
        setContentDisplay(isEditing() ? ContentDisplay.GRAPHIC_ONLY : ContentDisplay.TEXT_ONLY);
    }
}
