package View.Helper.Attachement;

import Controller.detailsController.PieceJointeViewController;
import DAO.DaoFactory;
import Main.Main;
import Model.Enum.NotifType;
import Model.Other.MainService;
import Model.Pojo.PieceJointe;
import View.Dialog.Other.Notification;
import View.Model.PieceJointeForView;
import com.jfoenix.controls.JFXButton;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.apache.commons.io.FileUtils;
import org.controlsfx.control.PopOver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class AttachementPopup extends PopOver implements Initializable {

    public AttachementPopup(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View/Other/AttachementPopOver.fxml"));
            loader.setController(this);
            VBox load = (VBox)loader.load();
            this.setContentNode(load);
            this.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void initialize(URL location, ResourceBundle resources){
        attachementPopup = this;
        viewbtn.setOnAction(this::viewAttachement);
        delAttachement.setOnAction(this::deleteAttachement);
        new AttachementDownloaderButton(downBtn,false);
    }

    public void showPopup(JFXButton button){
        jfxButton = button;
        ImageView graphic = (ImageView)button.getGraphic();
        PieceJointeForView selectedAttachementForView = (PieceJointeForView)button.getParent();
        selectedAttachement = selectedAttachementForView.getPieceJointe();
        this.show(graphic);
    }

    public static AttachementPopup getInstance(){
        if (attachementPopup == null)
            attachementPopup = new AttachementPopup();
        return attachementPopup;
    }

    private void deleteAttachement(ActionEvent event){
        this.hide();
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int deleteStatus = DaoFactory.getPieceJointeDao().delete(selectedAttachement);
                if (deleteStatus==1){
                    Platform.runLater(() -> {
                        TilePane pjTilepane = PieceJointeViewController.getInstance().getPjTilepane();
                        boolean remove = pjTilepane.getChildren().remove((PieceJointeForView)jfxButton.getParent());
                        if (remove){
                            String message = " Piece jointe supprimé avec suucès ";
                            Notification.getInstance(message, NotifType.SUCCESS).show();
                        }
                    });
                }else {
                    Platform.runLater(() -> {
                        String message = " Impossible de supprimer la piece jointe "+selectedAttachement.getDescription()+"."+selectedAttachement.getExtensionPiece();
                        Notification.getInstance(message, NotifType.WARNING).show();
                    });
                }
                return null;
            }
        });
    }
    private void viewAttachement(ActionEvent event){
        this.hide();
        File selectedFile = createFileFromInputstream();
        if (selectedFile!=null && selectedFile.isFile()){
            HostServices hostServices = Main.getMainApplication().getHostServices();
            hostServices.showDocument(selectedFile.toURI().toString());
        }
    }
    private File createFileFromInputstream(){
        try {
            InputStream valeur = selectedAttachement.getInputStream();
            if (valeur!=null){
                String extension = selectedAttachement.getExtensionPiece();
                String fileName = selectedAttachement.getDescription();
                String path = fileName+"."+extension;
                File file = new File(path);
                file.setReadOnly();
                if (!file.exists())
                   FileUtils.copyInputStreamToFile(valeur,file);
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static PieceJointe getSelectedAttachement() {
        return selectedAttachement;
    }
    private static PieceJointe selectedAttachement;
    private static AttachementPopup attachementPopup;
    private static JFXButton jfxButton;
    @FXML private JFXButton viewbtn;
    @FXML private JFXButton downBtn;
    @FXML private JFXButton delAttachement;
}
