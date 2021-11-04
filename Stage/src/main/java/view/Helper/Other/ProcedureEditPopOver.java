////package View.Model;
////
////import Model.Enum.ColumType;
////import com.jfoenix.controls.JFXButton;
////import com.jfoenix.controls.JFXDatePicker;
////import javafx.event.EventHandler;
////import javafx.fxml.FXML;
////import javafx.fxml.FXMLLoader;
////import javafx.scene.control.*;
////import javafx.scene.input.KeyCode;
////import javafx.scene.input.KeyEvent;
////import javafx.scene.layout.HBox;
////import org.controlsfx.control.PopOver;
////
////import java.io.IOException;
////import java.time.LocalDateTime;
////import java.time.LocalTime;
////
////public class ProcedureEditPopOver extends PopOver {
////
////    private static ProcedureEditPopOver procedureEditPopOver;
////    private static TableCell<ProcedureForView, String> cell;
////    @FXML private JFXButton okBtn;
////    @FXML private JFXButton cancelBtn;
////    @FXML private Spinner<Integer> numPrcd;
////    @FXML private JFXDatePicker datePicker;
////    @FXML private TextArea textArea;
////    static ColumType type;
////
////    final EventHandler<KeyEvent> eventHandler = new EventHandler<>() {
////
////        @Override
////        public void handle(KeyEvent keyEvent) {
////
////            if (keyEvent.getCode() == KeyCode.ENTER) {
////
////                switch (type) {
////                    case NUMERO: {
////                        commitNumero();
////                    }
////                    break;
////                    case DATE: {
////                        commitDate();
////                    }
////                    break;
////                    case NAME: {
////                        commitName();
////                    }
////                    break;
////                }
////
////            }else if (keyEvent.getCode() == KeyCode.ESCAPE) {
////                if (!type.equals(ColumType.NAME)) {
////                    hide();
////                }
////                cell.cancelEdit();
////            }
////        }
////    };
////
////
////    public ProcedureEditPopOver(ColumType type) {
////        try {
////            this.setContentNode(initLoader(type));
////            this.setArrowLocation(ArrowLocation.BOTTOM_CENTER);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
////
////    private HBox initLoader(ColumType type) throws IOException{
////        FXMLLoader loader = null;
////        switch (type) {
////            case DATE: {
////                loader = new FXMLLoader(getClass().getResource("/fxml/View/Cell/editDateProcedure.fxml"));
////            }
////            break;
////            case NAME: {
////                loader = new FXMLLoader(getClass().getResource("/fxml/View/Cell/editProcedureName.fxml"));
////            }
////            break;
////            case NUMERO: {
////                loader = new FXMLLoader(getClass().getResource("/fxml/View/Cell/editNumero.fxml"));
////            }
////            break;
////        }
////
////        loader.setController(this);
////        return (HBox) loader.load();
////
////    }
////
////    private static void initPopOverContent(ColumType newType){
////        try {
////            procedureEditPopOver.setContentNode(procedureEditPopOver.initLoader(newType));
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
////
////    public static ProcedureEditPopOver getInstance(TableCell<ProcedureForView, String> newCell, ColumType newType) {
////        cell = newCell;
////        if (procedureEditPopOver == null)
////            procedureEditPopOver = new ProcedureEditPopOver(newType);
////
////        if (type!=null){
////            if (!newType.equals(type)) {
////                initPopOverContent(newType);
////            }
////        }else {
////            initPopOverContent(newType);
////        }
////
////        type = newType;
////
////        switch (newType) {
////            case NAME: {
////                procedureEditPopOver.initTextArea(newCell);
////            }break;
////            case DATE: {
////                procedureEditPopOver.initDatePicker(newCell);
////            }break;
////            case NUMERO: {
////                procedureEditPopOver.initNumero(newCell);
////            }break;
////        }
////
////        procedureEditPopOver.cancelBtn.setOnAction(event -> {
////            procedureEditPopOver.hide();
////            newCell.cancelEdit();
////        });
////
////        return procedureEditPopOver;
////    }
////
////    public void showOnCell(TableCell<ProcedureForView, String> cell) {
////        if (!this.isShowing()) {
////            this.show(cell);
////        } else this.hide();
////    }
////
////    private void initTextArea(TableCell<ProcedureForView, String> cell) {
////        cell.textProperty().unbind();
////        textArea.textProperty().unbind();
////        textArea.textProperty().bindBidirectional(cell.textProperty());
////        textArea.requestFocus();
////        textArea.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
////        okBtn.setOnAction(event -> commitName());
////    }
////
////    private void initDatePicker(TableCell<ProcedureForView, String> cell) {
////        datePicker.setEditable(false);
////        datePicker.getEditor().setText(cell.getText());
////        datePicker.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
////        okBtn.setOnAction(event -> commitDate());
////    }
////
////    private void initNumero(TableCell<ProcedureForView, String> cell) {
////        numPrcd.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000));
////        numPrcd.setEditable(true);
////        numPrcd.getEditor().textProperty().unbind();
////        numPrcd.getEditor().textProperty().bindBidirectional(cell.textProperty());
////        numPrcd.getEditor().requestFocus();
////        numPrcd.getEditor().addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
////        okBtn.setOnAction(event -> commitNumero());
////    }
////
////    private void commitNumero() {
////
////        String text = numPrcd.getEditor().getText();
////        if (text.matches("^[1-9]{1,5}$")) {
////            this.hide();
////            cell.commitEdit(text);
////        }
////
////    }
////
////    private void commitDate() {
////        this.hide();
////        LocalDateTime localDateTime = LocalDateTime.of(datePicker.getValue(), LocalTime.now());
////        cell.commitEdit(localDateTime.toString());
////    }
////
////    private void commitName() {
////        this.hide();
////        cell.commitEdit(textArea.getText());
////    }
////
////    public TableCell<ProcedureForView, String> getCell() {
////        return cell;
////    }
////    public void setCell(TableCell<ProcedureForView, String> cell) {
////        this.cell = cell;
////    }
////}
//package View.Model;
//
//import Model.Enum.ColumType;
//import com.jfoenix.controls.JFXButton;
//import com.jfoenix.controls.JFXDatePicker;
//import javafx.event.EventHandler;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.control.*;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.layout.HBox;
//import org.controlsfx.control.PopOver;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//
//public class ProcedureEditPopOver extends PopOver {
//
//    private static ProcedureEditPopOver procedureEditPopOver;
//    private static TableCell<ProcedureForView, String> cell;
//    @FXML private JFXButton okBtn;
//    @FXML private JFXButton cancelBtn;
//    @FXML private TextField numPrcd;
//    @FXML private JFXDatePicker datePicker;
//    @FXML private TextArea textArea;
//    private static ColumType type;
//
//    final EventHandler<KeyEvent> eventHandler = new EventHandler<>() {
//        @Override public void handle(KeyEvent keyEvent) {
//            if (keyEvent.getCode() == KeyCode.ENTER) {
//                switch (type){
//                    case NUMERO:{
//                        commitNumero(cell);
//                    }break;
//                    case DATE:{
//                        commitDate(cell);
//                    }break;
//                    case NAME:{
//                        commitName(cell);
//                    }break;
//                }
//            } else if (keyEvent.getCode() == KeyCode.ESCAPE){
//                if (!type.equals(ColumType.NAME)){
//                    hide();
//                }
//                cell.cancelEdit();
//            }
//        }
//    };
//
//    public ProcedureEditPopOver(ColumType type) {
//        try {
//            this.setContentNode(initLoader(type));
//            this.setArrowLocation(ArrowLocation.BOTTOM_CENTER);
//        } catch (IOException e) { e.printStackTrace(); }
//    }
//
//    private HBox initLoader(ColumType type) throws IOException {
//        FXMLLoader loader = null;
//        switch (type){
//            case DATE:{
//                loader = new FXMLLoader(getClass().getResource("/fxml/View/Cell/editDateProcedure.fxml"));
//            }break;
//            case NAME:{
//                loader = new FXMLLoader(getClass().getResource("/fxml/View/Cell/editProcedureName.fxml"));
//            }break;
//            case NUMERO:{
//                loader = new FXMLLoader(getClass().getResource("/fxml/View/Cell/editNumero.fxml"));
//            }break;
//        }
//        loader.setController(this);
//        return (HBox)loader.load();
//    }
//
//    private static void initPopOverContent(ColumType newType){
//        try{
//            procedureEditPopOver.setContentNode(procedureEditPopOver.initLoader(newType));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static ProcedureEditPopOver getInstance(ColumType newType){
//        type = newType;
//        if(procedureEditPopOver ==null)
//            procedureEditPopOver = new ProcedureEditPopOver(newType);
//        else {
//            if (type!=null){
//                if (!newType.equals(type)){
//                    initPopOverContent(newType);
//                }
//            }
//        }
//        switch (type){
//            case NAME:{
//                procedureEditPopOver.initTextArea(cell);
//            }break;
//            case DATE:{
//                procedureEditPopOver.initDatePicker(cell);
//            }break;
//            case NUMERO:{
//                procedureEditPopOver.initNumero(cell);
//            }break;
//
//        }
//        return procedureEditPopOver;
//    }
//
//    public void showOnCell(TableCell<ProcedureForView, String> cell){
//        if (!this.isShowing()){
//            setCell(cell);
//            cancelBtn.setOnAction(event ->{
//                this.hide();
//                cell.cancelEdit();
//            });
//            this.show(cell);
//        }else this.hide();
//
//    }
//
//    private void initTextArea(TableCell<ProcedureForView, String> cell){
//
//        textArea.textProperty().unbind();
//        textArea.textProperty().bindBidirectional(cell.textProperty());
//        textArea.requestFocus();
//        textArea.addEventHandler(KeyEvent.KEY_PRESSED,eventHandler);
//        okBtn.setOnAction(event -> commitName(cell));
//
//    }
//
//    private void initDatePicker(TableCell<ProcedureForView, String> cell){
//        datePicker.setEditable(false);
//        datePicker.getEditor().setText(cell.getText());
//        datePicker.addEventHandler(KeyEvent.KEY_PRESSED,eventHandler);
//        okBtn.setOnAction(event -> commitDate(cell));
//    }
//
//    private void initNumero(TableCell<ProcedureForView, String> cell){
//        numPrcd.textProperty().unbind();
//        numPrcd.textProperty().bindBidirectional(cell.textProperty());
//        numPrcd.requestFocus();
//        numPrcd.addEventHandler(KeyEvent.KEY_PRESSED,eventHandler);
//        okBtn.setOnAction(event -> commitNumero(cell));
//    }
//
//    private void commitNumero(TableCell<ProcedureForView, String> cell){
//        String text = numPrcd.getText();
//        if (text.matches("^[0-9]{1,5}$")){
//            this.hide();
//            cell.commitEdit(text);
//            cell.textProperty().unbind();
//        }
//
//    }
//
//    private void commitDate(TableCell<ProcedureForView, String> cell){
//        this.hide();
//        LocalDateTime localDateTime = LocalDateTime.of(datePicker.getValue(), LocalTime.now());
//        cell.commitEdit(localDateTime.toString());
//    }
//
//    private void commitName(TableCell<ProcedureForView, String> cell){
//        this.hide();
//        cell.commitEdit(textArea.getText());
//    }
//    public TableCell<ProcedureForView, String> getCell() {
//        return cell;
//    }
//    public void setCell(TableCell<ProcedureForView, String> cell) {
//        ProcedureEditPopOver.cell = cell;
//    }
//}
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
                loader = new FXMLLoader(getClass().getResource("/fxml/View/Cell/editDateProcedure.fxml"));
            }break;
            case NAME:{
                loader = new FXMLLoader(getClass().getResource("/fxml/View/Cell/editProcedureName.fxml"));
            }break;
            case NUMERO:{
                loader = new FXMLLoader(getClass().getResource("/fxml/View/Cell/editNumero.fxml"));
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
