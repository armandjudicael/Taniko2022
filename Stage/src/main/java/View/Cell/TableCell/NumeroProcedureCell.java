package View.Cell.TableCell;

import Model.Enum.ColumType;
import View.helper.ProcedureEditPopOver;
import View.Model.ProcedureForView;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;

public class NumeroProcedureCell extends TableCell<ProcedureForView, String> {

    @Override public void commitEdit(String s){
        super.commitEdit(s);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override public void cancelEdit(){
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override public void startEdit(){
        super.startEdit();
        ProcedureForView item = this.getTableRow().getItem();
        if (item.isChecked()){
            if (getTableColumn().getId().equals("numArrive")){
                if (item.getDateArrive()!=null){
                    ProcedureEditPopOver.getInstance(ColumType.NUMERO).showOnCell(this);
                }
            }else {
                ProcedureEditPopOver.getInstance(ColumType.NUMERO).showOnCell(this);
            }
        }
    }

    @Override protected void updateItem(String s, boolean b){
        super.updateItem(s, b);
        setText(s);
        setAlignment(Pos.CENTER);
        setContentDisplay(isEditing() ? ContentDisplay.GRAPHIC_ONLY : ContentDisplay.TEXT_ONLY);
    }
}
