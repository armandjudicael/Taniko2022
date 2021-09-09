package View.Model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import org.controlsfx.control.PopOver;

public class PasswordView extends PopOver {
    private final Image visibleImage = new Image(this.getClass().getResourceAsStream("/img/eye_30px.png"));
    private final Image invisibleImage = new Image(this.getClass().getResourceAsStream("/img/invisible_50px.png"));
    private Label passwordLabel;
    private PasswordField passwordField;
    private ImageView imageView;

    public PasswordView(ImageView view, PasswordField field) {
        passwordLabel = new Label();
        passwordLabel.setAlignment(Pos.CENTER);
        passwordField = field;
        imageView = view;
        imageView.visibleProperty().bind(passwordField.textProperty().isNotEmpty());
        imageView.setOnMouseClicked(this::mouseClicked);
        passwordLabel.textProperty().bind(passwordField.textProperty());
        passwordLabel.setFont(new Font("Arial Unicode Ms", 13));
        passwordLabel.setPadding(new Insets(5));
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (this.isShowing() && newValue.isEmpty()) {
                this.hide();
            }
        });
        this.setArrowLocation(ArrowLocation.LEFT_CENTER);
        this.setContentNode(passwordLabel);
    }

    private void mouseClicked(MouseEvent mouseEvent) {
        if (!this.isShowing()) {
            imageView.setImage(visibleImage);
            this.show(passwordField.getParent());
        } else {
            imageView.setImage(invisibleImage);
            this.hide();
        }
    }
}
