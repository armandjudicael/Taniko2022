package view.Helper.Other;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.TextField;

public class TextFieldReseterBtn {
    public TextFieldReseterBtn(JFXButton button, TextField textField){
        button.setOnAction(event -> textField.setText(""));
        button.visibleProperty().bind(textField.textProperty().isNotEmpty());
    }
}
