package View.Helper.Other;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;

public class TabBinding{
    public TabBinding(ObservableList items, Tab tab){
        IntegerBinding itemSize = Bindings.size(items);
        StringBinding stringBinding = new StringBinding() {
            @Override
            protected String computeValue() {
                return "("+itemSize.asString()+")" ;
            }
        };
        tab.textProperty().bind(stringBinding);
    }
}
