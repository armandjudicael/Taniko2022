package controller.formController;

import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.PopupWindow;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import static com.jfoenix.controls.JFXButton.ButtonType.*;

public class AttachementFormControllerTest {

    public void calculateFileSize() {

    }

    private class TestRefection <T>{
        private T object;
        private Object value;
        private ObservableList<AnchorPane> fieldViewList;
        public TestRefection(T object){
            this.object = object;
            Class<?> aClass = object.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            if(declaredFields.length!=0){
                for (Field temp: declaredFields) {
                    String fieldName = temp.getName();
                    try {
                        Object o = temp.get(value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private AnchorPane createFieldViewObject(String name,String value,EventHandler<ActionEvent> eventHandler){
           AnchorPane pane = new AnchorPane();
           pane.setPrefHeight(50.0);

           return pane;
        }

        private JFXButton createButton(EventHandler<ActionEvent> eventHandler){

            JFXButton button = new JFXButton();
            button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            button.setOnAction(eventHandler);
            button.setPrefWidth(35);
            button.setPrefHeight(35);
            button.setButtonType(RAISED);
            button.setLayoutY(7.0);
            button.setLayoutX(534.0);
            ImageView imageView = createImageView();
            button.setGraphic(imageView);

            AnchorPane.setLeftAnchor(button,5.0);
            AnchorPane.setRightAnchor(button,5.0);
            AnchorPane.setTopAnchor(button,5.0);
            AnchorPane.setBottomAnchor(button,5.0);

            return button;
        }

        private ImageView createImageView() {
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/img/edit_510px.png")));
            imageView.setFitWidth(23);
            imageView.setFitHeight(21);
            imageView.setPickOnBounds(true);
            imageView.setPreserveRatio(true);
            return imageView;
        }

    }
}