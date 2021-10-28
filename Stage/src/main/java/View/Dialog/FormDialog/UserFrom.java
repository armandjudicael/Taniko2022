package View.Dialog.FormDialog;

import controller.viewController.MainController;
import controller.viewController.UserViewController;
import dao.DaoFactory;
import Main.InitializeApp;
import Model.Enum.FileChooserType;
import Model.Enum.NotifType;
import Model.Pojo.User;
import Model.Other.MainService;
import View.Dialog.Other.FileChooserDialog;
import View.Dialog.Other.Notification;
import View.Model.ViewObject.PasswordView;
import View.Model.ViewObject.UserForView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class UserFrom extends JFXDialog implements Initializable{
    public UserFrom() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Form/Other/UserForm.fxml"));
            loader.setController(this);
            this.setContent((AnchorPane) loader.load());
            this.setDialogContainer(MainController.getInstance().getMainStackPane());
            this.setTransitionType(DialogTransition.CENTER);
            this.setOverlayClose(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static UserFrom getInstance() {
        return userFrom;
    }

    private void initializePassworldField() {
        passworld1.textProperty().addListener((observable, oldValue, newValue) -> {
            passworldLabel.setText("");
            passworld1.getParent().getStyleClass().remove("boxError");
            if (!newValue.isEmpty() && !passworld.getText().isEmpty()) {
                if (!newValue.contains(passworld.getText())) {
                    passworld1.getParent().getStyleClass().add("boxError");
                    passworldLabel.setText("mot de passe incorect!");
                }
            }
        });
        passworld.textProperty().addListener(this::changed);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userFrom = this;
        initializeType();
        initializePassworldField();
        photo.setOnMouseClicked(this::photoButtonClicked);
        saveBtn.setOnAction(event -> {
            this.close();
            MainService.getInstance().launch(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    enregistrer();
                    return null;
                }
            });
        });
        closeStage.setOnAction(event -> this.close());
        closeBtn.setOnAction(event -> this.close());
        addPhoto.setOnAction(event -> photoButtonClicked(null));
        initializeButtonBinding();
        // initialiser l'image
        photo.setFill(new ImagePattern(profilImg));
        eye.visibleProperty().bind(passworld.textProperty().isNotEmpty());
        new PasswordView(eye, passworld);
        eye1.visibleProperty().bind(passworld1.textProperty().isNotEmpty());
        new PasswordView(eye1, passworld1);
        progressBar.visibleProperty().bind(Bindings.not(passworld.textProperty().isEmpty().or(passworld.textProperty().isEqualTo(""))));
    }

    void photoButtonClicked(MouseEvent event) {
        List<File> fileList = FileChooserDialog.getInstance(FileChooserType.SINGLE);
        file = fileList.get(0);
        if (file != null) {
            Platform.runLater(
                    () -> {
                        try {
                            photo.setFill(new ImagePattern(new Image(UserFrom.file.toURI().toURL().toString())));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
    public void enregistrer(){
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                User user = new User();
                try {
                    if (file != null) {
                        user = new User(name.getText()
                                , firstName.getText(), new FileInputStream(file),
                                userName.getText(),
                                passworld.getText(), type.getValue(), fonction.getText());
                    } else {
                        user = new User(name.getText()
                                , firstName.getText(), (FileInputStream) null,
                                userName.getText(),
                                passworld.getText(), type.getValue(), fonction.getText());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                createOperation(user);
                return null;
            }
        });
    }

    private void createOperation(User user) {
        // Recuperation de la tilePane contenant la liste des utilisateurs
        TilePane tilePane = UserViewController.getInstance().getUserTilePane();
        if (DaoFactory.getUserDao().create(user) == 1) {
            Platform.runLater(() -> {
                Notification.getInstance("Agent enregistré avec succès ! ", NotifType.SUCCESS).showNotif();
                try {
                    if (file != null)
                        user.setPhoto(new Image(file.toURI().toURL().toString()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                UserForView user1 = new UserForView(user);
                tilePane.getChildren().add(user1);
                InitializeApp.getUsers().add(user1);
            });
        }
    }

    private void initializeType() {
        String types[] = {"standard", "administrateur"};
        type.getItems().addAll(types);
    }

    private void initializeButtonBinding() {
        saveBtn.disableProperty().bind(
                Bindings.or(
                        Bindings.or(
                                Bindings.or(
                                        name.textProperty().isEqualTo(""), firstName.textProperty().isEqualTo("")),
                                Bindings.or(fonction.textProperty().isEqualTo(""), userName.textProperty().isEqualTo(""))
                        ), Bindings.or(type.getSelectionModel().selectedItemProperty().isNull(), passworld1.textProperty().isEqualTo(""))));

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
            progressBar.lookup(".bar").setStyle("-fx-background-color: #3772a2;");
            passwordStrengh.setTextFill(Color.valueOf("#1135FF"));
            passwordStrengh.setText(" Mot de passe fort ");
            progressBar.setProgress(0.75);
        } else if (5 <= lng && (lw || up) && nb) {
            progressBar.lookup(".bar").setStyle("-fx-background-color:#FF7F00;");
            passwordStrengh.setTextFill(Color.ORANGE);
            passwordStrengh.setText(" Mot de passe Moyen");
            progressBar.setProgress(0.50);
        } else if (lng <= 4 && (lw || up)) {
            progressBar.lookup(".bar").setStyle("-fx-background-color: #f1bc4f;");
            passwordStrengh.setTextFill(Color.valueOf("#f1bc4f"));
            passwordStrengh.setText(" Mot de passe faible ");
            progressBar.setProgress(0.25);
        } else if (!newValue.isEmpty()) {
            if (lng <= 4) {
                progressBar.lookup(".bar").setStyle("-fx-background-color: #f1bc4f;");
                passwordStrengh.setTextFill(Color.valueOf("#f1bc4f"));
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

    public void resetForm() {
        name.setText("");
        firstName.setText("");
        userName.setText("");
        passworld.setText("");
        passworld1.setText("");
        fonction.setText("");
        photo.setFill(new ImagePattern(profilImg));
    }
    private static UserFrom userFrom;
    private static File file = null;
    private static Pattern lowercase = Pattern.compile("[a-z]"); // Au moins 1 caractere minuscule
    private static Pattern uppercase = Pattern.compile("[A-Z]"); // Au moins 1 Caractere majuscule
    private static Pattern number = Pattern.compile("[0-9]"); // Au moins 1 chiffre
    private static Pattern specialCharacter = Pattern.compile("[!@#%^&*()\\-,;ù^$:_+.]"); // Au moins caractere special
    private final Image profilImg = new Image(getClass().getResourceAsStream("/img/camera_10px.png"));
    @FXML private Circle photo;
    @FXML private TextField name;
    @FXML private TextArea fonction;
    @FXML private HBox passIcon;
    @FXML private PasswordField passworld;
    @FXML private ImageView eye;
    @FXML private PasswordField passworld1;
    @FXML private ImageView eye1;
    @FXML private Label passworldLabel;
    @FXML private TextField firstName;
    @FXML private JFXProgressBar progressBar;
    @FXML private Label passwordStrengh;
    @FXML private TextField userName;
    @FXML private JFXButton saveBtn;
    @FXML private ComboBox<String> type;
    @FXML private JFXButton closeBtn;
    @FXML private JFXButton closeStage;
    @FXML private JFXButton addPhoto;
}
