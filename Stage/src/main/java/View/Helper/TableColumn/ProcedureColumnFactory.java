package View.Helper.TableColumn;

import controller.viewController.AffairViewController;
import View.Model.ViewObject.ProcedureForTableview;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class ProcedureColumnFactory{
    private final static Image goImg = new Image(Objects.requireNonNull(AffairViewController.class.getResourceAsStream("/img/go1.png")));
    private final static Image backImg = new Image(Objects.requireNonNull(AffairViewController.class.getResourceAsStream("/img/back.png")));
    private final static Image noneImg = new Image(Objects.requireNonNull(AffairViewController.class.getResourceAsStream("/img/none.png")));
    public ProcedureColumnFactory() {
    }
    public static ObservableValue<Label> createLabel(ProcedureForTableview procedureForTableview){
        return new ObservableValue<Label>() {
            @Override public void addListener(ChangeListener<? super Label> listener) { }
            @Override public void removeListener(ChangeListener<? super Label> listener) { }
            @Override public Label getValue() {
                Label label = new Label(procedureForTableview.getDescription());
                label.setWrapText(true);
                label.setGraphicTextGap(10);
                switch (procedureForTableview.getStatus()){
                    case GO: {
                        label.setGraphic(new ImageView(goImg));
                    }break;
                    case BACK: {
                        label.setGraphic(new ImageView(backImg));
                    }break;
                    case NONE: {
                        label.setGraphic(new ImageView(noneImg));
                    }break;
                }
                return label;
            }
            @Override public void addListener(InvalidationListener listener) {
            }
            @Override public void removeListener(InvalidationListener listener) {

            }
        };
    }
}
