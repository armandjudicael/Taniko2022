package view.Dialog.FormDialog;
import controller.viewController.MainController;
import controller.viewController.UserViewController;
import dao.DaoFactory;
import Main.InitializeApp;
import model.Enum.FileChooserType;
import model.Enum.NotifType;
import model.pojo.business.other.User;
import model.other.MainService;
import view.Dialog.Other.FileChooserDialog;
import view.Dialog.Other.Notification;
import view.Model.ViewObject.PasswordView;
import view.Model.ViewObject.UserForView;
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
                    user.setNom(name.getText());
                    user.setUserName(userName.getText());
                    user.setPassword(passworld.getText());
                    user.setTypeUtilisateur(type.getValue());
                    user.setProfession(fonction.getText());
                    user.setInsertPhoto(file!=null ? new FileInputStream(file) : (FileInputStream) null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                createOperation(user);
                return null;
            }
        });
    }

    private void createOperation(User user){
        if (DaoFactory.getUserDao().create(user) == 1) {
            // Recuperation de la tilePane contenant la liste des utilisateurs
            TilePane tilePane = UserViewController.getInstance().getUserTilePane();
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
                                name.textProperty().isEqualTo(""),
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
            updateProgressAndPassWordStrenghTo("#3772a2",1.0," Mot de passe très fort ",Color.GREEN);
        } else if (6 <= lng && up && lw && nb && spChar) {
            updateProgressAndPassWordStrenghTo("#3772a2",0.75," Mot de passe fort ",Color.valueOf("#1135FF"));
        } else if (5 <= lng && (lw || up) && nb) {
            updateProgressAndPassWordStrenghTo("#FF7F00",0.50,"Mot de passe moyen",Color.ORANGE);
        } else if (lng <= 4 && (lw || up)) {
            updateProgressAndPassWordStrenghTo("#f1bc4f",0.25," Mot de passe faible",Color.valueOf("#f1bc4f"));
        } else if (!newValue.isEmpty()) {
            if (lng <= 4) updateProgressAndPassWordStrenghTo("#f1bc4f",0.25," Mot de passe faible",Color.valueOf("#f1bc4f"));
            else updateProgressAndPassWordStrenghTo("#FF7F00",0.50,"Mot de passe moyen",Color.ORANGE);
        }
    }

    private void updateProgressAndPassWordStrenghTo(String progressColor, Double progressValue, String message, Color messageColor) {
        progressBar.lookup(".bar").setStyle("-fx-background-color:"+progressColor+";");
        passwordStrengh.setTextFill(messageColor);
        passwordStrengh.setText(message);
        progressBar.setProgress(progressValue);
    }

    public void resetForm() {
        name.setText("");
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
    @FXML private JFXProgressBar progressBar;
    @FXML private Label passwordStrengh;
    @FXML private TextField userName;
    @FXML private JFXButton saveBtn;
    @FXML private ComboBox<String> type;
    @FXML private JFXButton closeBtn;
    @FXML private JFXButton closeStage;
    @FXML private JFXButton addPhoto;
}
