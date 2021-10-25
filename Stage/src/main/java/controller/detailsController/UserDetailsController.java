package controller.detailsController;

import controller.viewController.LoginController;
import controller.viewController.MainController;
import DAO.DbOperation;
import Model.Enum.FileChooserType;
import Model.Enum.NotifType;
import Model.Enum.Origin;
import View.Dialog.FormDialog.PasswordFrom;
import View.Dialog.Other.FileChooserDialog;
import View.Dialog.Other.Notification;
import View.Dialog.SecurityDialog.AdminSecurity;
import View.Dialog.SecurityDialog.EditorSecurity;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static DAO.DbOperation.executeQuery;

public class UserDetailsController implements Initializable {

    @Override public void initialize(URL location, ResourceBundle resources) {
        userDetailsController = this;
        unlockImg.setFitWidth(24);
        unlockImg.setFitHeight(20);

        lockImg.setFitWidth(24);
        lockImg.setFitHeight(20);

        backBtn.setOnAction(event -> {
            AnchorPane userForm = MainController.getInstance().getUserDetailsView();
            userForm.toBack();
        });
        // Editer compte
        lockBtn.setOnAction(this::lockBtnAction);
        photo.fillProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null)
                MainController.getInstance().getUserProfil().fillProperty().bind(photo.fillProperty());
        });
        photo.setOnMouseClicked(this::editPhoto);
        savePhotoBtn.setOnAction(this::saveProfil);
    }
    public void unlock(){
        userFormPane.setDisable(false); // enable the pannel details
        lockBtn.setText("verrouiller"); // change the text
        lockBtn.setGraphic(lockImg); // change the image
    }
    private void saveProfil(ActionEvent event){

        if (file!=null){
            int status = DbOperation.updateUserProfil(file,LoginController.getConnectedUser().getId());// update photo
            if (status!=0){
                String message  =" Profil enregistré avec succès !";
                Notification.getInstance(message,NotifType.SUCCESS);
            }
        }
        savePhotoBtn.setDisable(true);

    }
    void editPhoto(MouseEvent event) {
        List<File> fileList = FileChooserDialog.getInstance(FileChooserType.SINGLE);
        file = fileList.get(0);
        if (file != null) {
            savePhotoBtn.setDisable(false);
            Platform.runLater(
                    () -> {
                        try {
                            photo.setFill(new ImagePattern(new Image(file.toURI().toURL().toString())));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    });
        }

    }
    @FXML void editAction(ActionEvent event) {

        Object source = event.getSource();
        int status = 0;
        int id = LoginController.getConnectedUser().getId();
        TextInputDialog dialog = new TextInputDialog();
        dialog.getEditor().setPrefWidth(400);
        if (source == edtNameBtn){
              editName(dialog,id); // edit the user full name
        }else if (source == edtFirstName){
              editFirstName(dialog,id); // edit the user firts name
        }else if (source == edtTypeBtn){

        }else if (source == edtFonctionBtn){
              editFunction(dialog,id); // edit the function
        }else if (source == edtUserNameBtn){
              editUserName(dialog,id); // edit the username
        }else if (source == edtPasswordBtn){
              PasswordFrom.getInstance().show();
        }else if (source == editPhotoBtn){
              editPhoto(null); // change the photo
        }
        if (status == 1) {
         String message = " Mis a jour effectuer avec succès";
         Notification.getInstance(message, NotifType.SUCCESS).showNotif();
        }
    }

    private void editName(TextInputDialog dialog, int userId ) {
        String name = this.getNameLabel().getText();
        int status = 0;
        dialog.setHeaderText("Editer nom de l'utilisateur ");
        dialog.getEditor().setText(name);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.equals(name) ){
            // mettre a jour le nom du demandeur dans détails
            this.getNameLabel().setText(result.get());
            String query = " UPDATE utilisateur SET nom = '" + result.get() + "' WHERE idUtilisateur = " +userId+ ";";
            executeQuery(query);
        }
    }

    private void editFirstName(TextInputDialog dialog, int userId ) {
        String name = this.getFirstNameLabel().getText();
        int status = 0;
        dialog.setHeaderText("Editer prenom ");
        dialog.getEditor().setText(name);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.equals(name) ){
            // mettre a jour le nom du demandeur dans détails
            this.getFirstNameLabel().setText(result.get());
            String query = " UPDATE utilisateur SET prenom = '" + result.get() + "' WHERE idUtilisateur = " +userId+ ";";
            executeQuery(query);
        }
    }

    private void editFunction(TextInputDialog dialog, int userId ) {
        String name = this.getFonctionLabel().getText();
        dialog.setHeaderText("Editer fonction");
        dialog.getEditor().setText(name);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.equals(name) ){
            // mettre a jour le nom du demandeur dans détails
            this.getFonctionLabel().setText(result.get());
            String query = " UPDATE utilisateur SET fonction = '" + result.get() + "' WHERE idUtilisateur = " +userId+ ";";
            executeQuery(query);
        }
    }
    private void editUserName(TextInputDialog dialog, int userId ) {
        String name = this.getUserNameLabel().getText();
        int status = 0;
        dialog.setHeaderText("Editer nom d'utilisateur ");
        dialog.getEditor().setText(name);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.equals(name) ){
            // mettre a jour le nom du demandeur dans détails
            this.getUserNameLabel().setText(result.get());
            LoginController.getConnectedUser().setUserName(result.get());
            MainController.getInstance().getUserName().setText(result.get());
            String query = " UPDATE utilisateur SET nom_utilisateur = '" + result.get() + "' WHERE idUtilisateur = " +userId+ ";";
            executeQuery(query);
        }
    }
    private void editUserAccount() {
        EditorSecurity.show(Origin.USER_DETAILS_LOCK_BTN,null);
    }

    private void lockBtnAction(ActionEvent event){
        if (userFormPane.isDisabled()){ // si le panneau est desactivé alors on demande le mot de passe
            editUserAccount();
        }else {
            userFormPane.setDisable(true); // disable the panel
            lockBtn.setText("déverouiller");
            lockBtn.setGraphic(unlockImg);
        }
    }

    public JFXButton getBackBtn() {
        return backBtn;
    }
    public AnchorPane getUserFormPane() {
        return userFormPane;
    }
    public static UserDetailsController getInstance() {
        return userDetailsController;
    }
    public JFXButton getEdtPasswordBtn() {
        return edtPasswordBtn;
    }
    public JFXButton getEdtUserNameBtn() {
        return edtUserNameBtn;
    }
    public JFXButton getEdtTypeBtn() {
        return edtTypeBtn;
    }
    public JFXButton getEdtFonctionBtn() {
        return edtFonctionBtn;
    }
    public JFXButton getEdtFirstName() {
        return edtFirstName;
    }
    public JFXButton getEdtNameBtn() {
        return edtNameBtn;
    }
    public Circle getPhoto() {
        return photo;
    }
    public JFXButton getSavePhotoBtn() {
        return savePhotoBtn;
    }
    public JFXButton getEditPhotoBtn() {
        return editPhotoBtn;
    }
    public JFXButton getLockBtn() {
        return lockBtn;
    }
    public Label getNameLabel() {
        return nameLabel;
    }
    public Label getFirstNameLabel() {
        return firstNameLabel;
    }
    public Label getTypeLabel() {
        return typeLabel;
    }
    public Label getUserNameLabel() {
        return userNameLabel;
    }
    public Label getFonctionLabel() {
        return fonctionLabel;
    }
    public TextField getPassWord() {
        return passWord;
    }
    public static int getUserDetailsId() {
        return userDetailsId;
    }
    public static void setUserDetailsId(int userDetailsId) {
        UserDetailsController.userDetailsId = userDetailsId;
    }
    public ImageView getUnlockImg() {
        return unlockImg;
    }
    private static File file;
    private static int userDetailsId = 0;
    private static UserDetailsController userDetailsController;

    @FXML private JFXButton backBtn;
    @FXML private AnchorPane userFormPane;
    // button
    @FXML private JFXButton edtPasswordBtn;
    @FXML private JFXButton edtUserNameBtn;
    @FXML private JFXButton edtTypeBtn;
    @FXML private JFXButton edtFonctionBtn;
    @FXML private JFXButton edtFirstName;
    @FXML private JFXButton edtNameBtn;
    @FXML private Circle photo;
    @FXML private JFXButton savePhotoBtn;
    @FXML private JFXButton editPhotoBtn;
    @FXML private JFXButton lockBtn;
    // label
    @FXML private Label nameLabel;
    @FXML private Label firstNameLabel;
    @FXML private Label typeLabel;
    @FXML private Label userNameLabel;
    @FXML private Label fonctionLabel;
    @FXML private PasswordField passWord;
    private ImageView unlockImg = new ImageView(new Image(this.getClass().getResourceAsStream("/img/padlock_26px.png")));
    private ImageView lockImg = new ImageView(new Image(AdminSecurity.class.getResourceAsStream("/img/lock_26px.png")));
}
