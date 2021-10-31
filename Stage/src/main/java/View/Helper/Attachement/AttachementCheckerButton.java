package View.Helper.Attachement;

import View.Model.ViewObject.PieceJointeForView;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class AttachementCheckerButton implements EventHandler<ActionEvent> {

    public AttachementCheckerButton(JFXButton button,ObservableList<Node> list){
         this.attachementList = list;
         this.jfxButton = button;
         button.setOnAction(this);
         this.isClickedProperty = new SimpleBooleanProperty(this,null,false);
         this.isClickedProperty.addListener((observable, oldValue, newValue) -> {
            changeAttachementViewStatusTo(newValue);
            updateButtonIconTo(newValue);
         });
    }

    @Override public void handle(ActionEvent event) {
        isClickedProperty.set(!isClickedProperty.get());
    }

    private void updateButtonIconTo(Boolean status){
        ImageView imageView = (ImageView)jfxButton.getGraphic();
        if (status) imageView.setImage(unChekImg);
        else imageView.setImage(checkImg);
    }

    private void changeAttachementViewStatusTo(Boolean isChecked){
        attachementList.forEach(node -> {
            if (!(node instanceof JFXButton)){
                PieceJointeForView pieceJointeForView = (PieceJointeForView) node;
                pieceJointeForView.getPieceCheckbox().setSelected(isChecked);
            }
        });
    }

    private  ObservableList<Node> attachementList;
    private JFXButton jfxButton;
    private SimpleBooleanProperty isClickedProperty;
    private final Image checkImg = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/img/checked_checkbox_40px.png")));
    private final Image unChekImg = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/img/unchecked_checkbox_40px.png")));
}
