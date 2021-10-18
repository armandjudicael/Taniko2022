package View.Dialog.FormDialog;

import Controller.detailsController.UserDetailsController;
import Controller.ViewController.LoginController;
import Controller.ViewController.MainController;
import Model.Pojo.User;
import View.Model.ViewObject.PasswordView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXProgressBar;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static DAO.DbOperation.executeQuery;

public class PasswordFrom extends JFXDialog implements Initializable {

    private static User user;
    private static Pattern lowercase = Pattern.compile("[a-z]"); // Au moins 1 caractere minuscule
    private static Pattern uppercase = Pattern.compile("[A-Z]"); // Au moins 1 Caractere majuscule
    private static Pattern number = Pattern.compile("[0-9]"); // Au moins 1 chiffre
    private static Pattern specialCharacter = Pattern.compile("[!@#%^&*()\\-,;ù^$:_+.]"); // Au moins caractere special
    private static PasswordFrom passwordFrom;
    @FXML
    private JFXProgressBar progressBar;
    // BUTTON
    @FXML
    private JFXButton exitBtn;
    @FXML
    private JFXButton saveBtn;
    @FXML
    private JFXButton resetBtn;
    // IMAGEVIEW
    @FXML
    private ImageView eye1;
    @FXML
    private ImageView eye2;
    @FXML
    private ImageView eye3;
    // PASSWORD
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField verifPassword;
    @FXML
    private PasswordField oldPassword;
    // BOX
    @FXML
    private HBox verifPassBox;
    @FXML
    private HBox oldPassBox;
    // LABEL
    @FXML
    private Label passwordStrengh;
    @FXML
    private Label oldPassLabel;
    @FXML
    private Label newPassLabel;

    private PasswordFrom() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Form/Other/PasswordForm.fxml"));
            loader.setController(this);
            this.setContent((AnchorPane) loader.load());
            this.setDialogContainer(MainController.getInstance().getMainStackPane());
            this.setTransitionType(DialogTransition.CENTER);
            this.setOverlayClose(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PasswordFrom getInstance() {
        if (passwordFrom == null) {
            passwordFrom = new PasswordFrom();
        } else {
            passwordFrom.getNewPassword().setText("");
            passwordFrom.getOldPassword().setText("");
            passwordFrom.getVerifPassword().setText("");
        }
        user = LoginController.getConnectedUser();
        return passwordFrom;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        PasswordFrom.user = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeBox();
        new PasswordView(eye1, oldPassword);
        new PasswordView(eye2, newPassword);
        new PasswordView(eye3, verifPassword);
        newPassword.textProperty().addListener(this::changed);
        BooleanBinding booleanBinding = oldPassword.textProperty().isEqualTo("")
                .or(newPassword.textProperty().isEqualTo(""))
                .or(verifPassword.textProperty().isEqualTo(""));
        saveBtn.disableProperty().bind(booleanBinding);
        newPassword.disableProperty().bind(oldPassword.textProperty().isEqualTo(""));
        verifPassword.disableProperty().bind(newPassword.textProperty().isEqualTo(""));
        saveBtn.setOnAction(this::saveNewPassword);
        progressBar.visibleProperty().bind(Bindings.not(newPassword.textProperty().isEqualTo("")));
        resetBtn.setOnAction(this::exit);
        exitBtn.setOnAction(this::exit);
    }

    private void saveNewPassword(ActionEvent event) {
        this.close();
        if (!verifPassword.getText().isEmpty()) {
            String query = " UPDATE utilisateur SET mot_de_passe = '" + verifPassword.getText() + "' WHERE idUtilisateur = " + user.getId() + ";";
            LoginController.getConnectedUser().setPassword(verifPassword.getText());
            UserDetailsController.getInstance().getPassWord().setText(verifPassword.getText());
            executeQuery(query);
        }
    }

    private void initializeBox() {
        // old password
        oldPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            oldPassLabel.setText("");
            oldPassBox.getStyleClass().removeAll("boxError", "ok");
            if (!newValue.isEmpty() && !newValue.equals("")) {

                if (!user.getPassword().contains(newValue)) {
                    oldPassBox.getStyleClass().add("boxError");
                    oldPassLabel.setText("mot de passe incorect!");
                } else {
                    oldPassBox.getStyleClass().add("ok");
                    oldPassLabel.setText("");
                }
            }
        });

        // verify the password
        verifPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            newPassLabel.setText("");
            verifPassBox.getStyleClass().removeAll("boxError", "ok");
            if (!newValue.isEmpty()) {
                if (!newPassword.getText().contains(newValue)) {
                    verifPassBox.getStyleClass().add("boxError");
                    newPassLabel.setText("mot de passe incorect!");
                } else {
                    verifPassBox.getStyleClass().add("ok");
                    newPassLabel.setText("");
                }
            }
        });
    }

    private void exit(ActionEvent event) {
        this.close();
    }

    private void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

        passwordStrengh.setText("");
        boolean spChar = specialCharacter.matcher(newValue).find();
        boolean nb = number.matcher(newValue).find();
        boolean lw = lowercase.matcher(newValue).find();
        boolean up = uppercase.matcher(newValue).find();
        int lng = newValue.length();
        if (8 <= lng && up && lw && nb && spChar) {
            progressBar.lookup(".bar").setStyle("-fx-background-color: #0ed145;");
            passwordStrengh.setTextFill(Color.GREEN);
            passwordStrengh.setText(" Mot de passe très fort ");
            progressBar.setProgress(1);
        } else if (6 <= lng && up && lw && nb && spChar) {
            progressBar.lookup(".bar").setStyle("-fx-background-color: #1135ff;");
            passwordStrengh.setTextFill(Color.valueOf("#1135FF"));
            passwordStrengh.setText(" Mot de passe fort ");
            progressBar.setProgress(0.75);
        } else if (5 <= lng && (lw || up) && nb) {
            progressBar.lookup(".bar").setStyle("-fx-background-color:#FF7F00;");
            passwordStrengh.setTextFill(Color.ORANGE);
            passwordStrengh.setText(" Mot de passe Moyen");
            progressBar.setProgress(0.50);
        } else if (lng <= 4 && (lw || up)) {
            progressBar.lookup(".bar").setStyle("-fx-background-color: #f1cc19;");
            passwordStrengh.setTextFill(Color.YELLOW);
            passwordStrengh.setText(" Mot de passe faible ");
            progressBar.setProgress(0.25);
        } else if (!newValue.isEmpty()) {
            if (lng <= 4) {
                progressBar.lookup(".bar").setStyle("-fx-background-color: #f1cc19;");
                passwordStrengh.setTextFill(Color.YELLOW);
                passwordStrengh.setText(" Mot de passe faible ");
                progressBar.setProgress(0.25);
            } else {
                progressBar.lookup(".bar").setStyle("-fx-background-color:#FF7F00;");
                passwordStrengh.setTextFill(Color.ORANGE);
                passwordStrengh.setText(" Mot de passe Moyen");
                progressBar.setProgress(0.50);
            }
        }
    }

    public PasswordField getNewPassword() {
        return newPassword;
    }

    public PasswordField getVerifPassword() {
        return verifPassword;
    }

    public PasswordField getOldPassword() {
        return oldPassword;
    }
}
