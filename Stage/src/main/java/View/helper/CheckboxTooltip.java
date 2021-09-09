package View.helper;

import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class CheckboxTooltip extends Tooltip {
    private String message;
    private JFXCheckBox checkBox;
    public CheckboxTooltip(String message, JFXCheckBox checkBox) {
        this.message = message;
        this.checkBox = checkBox;
        this.setText(message);
        checkBox.setTooltip(this);
        this.setShowDelay(new Duration(500));
    }
}
