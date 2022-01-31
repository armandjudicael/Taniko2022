package view.Helper.Other;
import model.Enum.ColumType;
import view.Model.ViewObject.ProcedureForView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import org.controlsfx.control.PopOver;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ProcedureEditPopOver extends PopOver {

    private final EventHandler<KeyEvent> eventHandler = new EventHandler<>(){
        @Override public void handle(KeyEvent keyEvent){
            if (keyEvent.getCode() == KeyCode.ENTER) {
                switch (type){
                    case NUMERO:{
                        commitNumero();
                    }break;
                    case DATE:{
                        commitDate();
                    }break;
                    case NAME:{
                        commitName();
                    }break;
                }
            } else if (keyEvent.getCode() == KeyCode.ESCAPE){
                if (!type.equals(ColumType.NAME)){
                    hide();
                }
                cell.cancelEdit();
            }
        }
    };

    public ProcedureEditPopOver(ColumType type) {
        try {
            this.setContentNode(initLoader(type));
            this.setArrowLocation(ArrowLocation.BOTTOM_CENTER);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private HBox initLoader(ColumType type) throws IOException {
        FXMLLoader loader = null;
        switch (type){
            case DATE:{
                loader = new FXMLLoader(getClass().getResource("/fxml/View/Other/Cell/editDateProcedure.fxml"));
            }break;
            case NAME:{
                loader = new FXMLLoader(getClass().getResource("/fxml/View/Other/Cell/editProcedureName.fxml"));
            }break;
            case NUMERO:{
                loader = new FXMLLoader(getClass().getResource("/fxml/View/Other/Cell/editNumero.fxml"));
            }break;
        }
        loader.setController(this);
        return (HBox)loader.load();
    }

    public static ProcedureEditPopOver getInstance(ColumType newType){
        if(procedureEditPopOver ==null)
            procedureEditPopOver = new ProcedureEditPopOver(newType);
        else{
            if (!newType.equals(type)){
                try {
                    procedureEditPopOver.setContentNode(procedureEditPopOver.initLoader(newType));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        type = newType;
        return procedureEditPopOver;
    }

    public void showOnCell(TableCell<ProcedureForView, String> newCell){
        setCell(newCell);
        if (!this.isShowing()){
            switch (type){
                case NAME:{
                    initTextArea(newCell);
                }break;
                case DATE:{
                    initDatePicker(newCell);
                }break;
                case NUMERO:{
                    initNumero(newCell);
                }break;
            }
            cancelBtn.setOnAction(event ->{
                this.hide();
                newCell.cancelEdit();
            });
            this.show(newCell);
        }else this.hide();

    }

    private void initTextArea(TableCell<ProcedureForView, String> cell){
        textArea.setText(cell.getText());
        textArea.requestFocus();
        textArea.addEventHandler(KeyEvent.KEY_PRESSED,eventHandler);
        okBtn.setOnAction(event -> commitName());
    }

    private void initDatePicker(TableCell<ProcedureForView, String> cell){
        datePicker.setEditable(false);
        datePicker.getEditor().setText(cell.getText());
        datePicker.addEventHandler(KeyEvent.KEY_PRESSED,eventHandler);
        okBtn.setOnAction(event -> commitDate());
    }

    private void initNumero(TableCell<ProcedureForView, String> cell){
        numPrcd.setText(cell.getText());
        numPrcd.requestFocus();
        numPrcd.addEventHandler(KeyEvent.KEY_PRESSED,eventHandler);
        okBtn.setOnAction(event -> commitNumero());
    }
    private void commitNumero(  ){
        String text = numPrcd.getText();
        if (text.matches("^[0-9]{1,5}$")){
            this.hide();
            getCell().commitEdit(text);
        }
    }
    private void commitDate(){
        LocalDate value = datePicker.getValue();
        if (value!=null){
            this.hide();
            LocalDateTime localDateTime = LocalDateTime.of(value, LocalTime.now());
            getCell().commitEdit(localDateTime.toString());
        }
    }
    private void commitName(){
        this.hide();
        cell.commitEdit(textArea.getText());
    }
    public TableCell<ProcedureForView, String> getCell() {
        return cell;
    }
    public void setCell(TableCell<ProcedureForView, String> cell) {
        this.cell = cell;
    }

    private static ProcedureEditPopOver procedureEditPopOver;
    private TableCell<ProcedureForView, String> cell;
    private static ColumType type;
    @FXML private JFXButton okBtn;
    @FXML private JFXButton cancelBtn;
    @FXML private TextField numPrcd;
    @FXML private JFXDatePicker datePicker;
    @FXML private TextArea textArea;
}
