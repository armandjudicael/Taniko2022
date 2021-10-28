package View.Helper.Other;

import dao.DaoFactory;
import Model.Enum.Type;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.util.StringConverter;
import java.util.Optional;
import java.util.function.Predicate;

public class AutoCompleteCombobox<T> implements EventHandler<KeyEvent>{
    private JFXComboBox<T> comboBox;
    private ObservableList<T> comboboxItemsCopy = FXCollections.observableArrayList();
    private Predicate<T> comboboxPredicate;
    private Type TYPE;

    public AutoCompleteCombobox(JFXComboBox<T> tComboBox, Predicate<T> predicate, Type type){
        this.TYPE = type;
        this.comboBox = tComboBox;
        this.comboboxPredicate = predicate;
        this.comboBox.visibleRowCountProperty().bind(Bindings.size(this.comboBox.getItems()).multiply(20));
        this.doAutoCompleteBox();
    }

    private void doAutoCompleteBox(){
        this.comboBox.setEditable(true);
        this.comboBox.setConverter(new StringConverter<T>(){
            @Override
            public String toString(T object) {
                return object!=null ? object.toString() : "";
            }
            @Override
            public T fromString(String stringValue) {
                Optional<T> first = comboBox.getItems().stream().filter(t -> t.toString().equals(stringValue)).findFirst();
                return first.orElse(null);
            }
        });

        this.comboBox.getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                 //mean on focus
                 this.comboBox.show();
            }
        });

        this.comboBox.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                String text = comboBox.getEditor().getText();
                if (comboboxItemsCopy.size()>0 && text.isEmpty()){
                    ObservableList<T> items = comboBox.getItems();
                    items.setAll(comboboxItemsCopy);
                }
            }
        });

        this.comboBox.getEditor().setOnMouseClicked(event ->{
            if(event.getButton().equals(MouseButton.PRIMARY)){
                if(event.getClickCount() == 2){
                    return;
                }
            }
            this.comboBox.show();
        });
        this.comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            moveCaret(this.comboBox.getEditor().getText().length());
        });
        this.comboBox.setOnKeyPressed(t -> comboBox.hide());
        this.comboBox.setOnKeyReleased(AutoCompleteCombobox.this);
    }

    @Override public void handle(KeyEvent event) {

        if ( event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN
                || event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
                || event.getCode() == KeyCode.HOME
                || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB){
            return;
        }
        if(event.getCode() == KeyCode.BACK_SPACE){
            String str = this.comboBox.getEditor().getText();
            if (str != null && str.length() > 0) {
                str = str.substring(0, str.length() - 1);
            }
            if(str != null){
                this.comboBox.getEditor().setText(str);
                moveCaret(str.length());
            }
            this.comboBox.getSelectionModel().clearSelection();
        }
        if(event.getCode() == KeyCode.ENTER ){
            this.comboBox.hide();
            return;
        }else {
            String text = this.comboBox.getEditor().getText().toLowerCase().trim();
            if (!text.isEmpty()){
                ObservableList<T> items = this.comboBox.getItems();
                launchDataSearch(items,comboboxItemsCopy,text);
            }
        }
    }

     void launchDataSearch(ObservableList<T> items, ObservableList<T> copyItems, String numOrName){
        if (items.isEmpty()){
            getDataFromDB(items,copyItems,numOrName);
        }else{
            FilteredList<T> filteredComboboxItem = copyItems.filtered(comboboxPredicate);
            if( filteredComboboxItem.size()>0){
                items.setAll(filteredComboboxItem);
                comboBox.show();
            }else{
                getDataFromDB(items,copyItems,numOrName);
            }
        }
    }

    private void getDataFromDB(ObservableList items, ObservableList copyItems, String numOrName){
        ObservableList observableList = FXCollections.observableArrayList();
        switch (TYPE){
            case USER:{
                observableList.setAll(DaoFactory.getUserDao().filterUser(numOrName,items));
            }break;
            case TITRE:{
                observableList.setAll(DaoFactory.getTitreDao().findByNumOrName(numOrName));
            }break;
            case CONNEXE:{
                observableList.setAll(DaoFactory.getAffaireDao().checkConnexAffaireBy(numOrName));
            }break;
        }
        if(observableList.size()>0){
            items.setAll(observableList);
            this.comboBox.show();
            copyItems.addAll(observableList);
        }else {
            this.comboBox.hide();
        }
    }

    private void moveCaret(int textLength) {
        this.comboBox.getEditor().positionCaret(textLength);
    }
    /**
     * Invoked when a specific event of the type for which this handler is
     * registered happens.
     *
     * @param event the event which occurred
     */
}
