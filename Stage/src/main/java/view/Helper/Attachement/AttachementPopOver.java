package view.Helper.Attachement;

import controller.detailsController.PieceJointeInfoController;
import model.Enum.NotifType;
import model.other.MainService;
import model.pojo.business.attachement.Attachement;
import view.Dialog.Other.Notification;
import view.Model.ViewObject.AttachementForView;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AttachementPopOver extends PopOver implements Initializable {

    public AttachementPopOver(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View/Other/popOver/AttachementPopOver.fxml"));
            loader.setController(this);
            VBox load = (VBox)loader.load();
            this.setContentNode(load);
            this.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void initialize(URL location, ResourceBundle resources){
        attachementPopOver = this;
        viewbtn.setOnAction(this::viewAttachement);
        delAttachement.setOnAction(this::deleteAttachement);
        new AttachementDownloaderButton(downBtn,false);
    }

    public void showPopup(JFXButton button){
        jfxButton = button;
        ImageView graphic = (ImageView)button.getGraphic();
        AttachementForView selectedAttachementForView = (AttachementForView)button.getParent();
        selectedAttachement = selectedAttachementForView.getAttachement();
        this.show(graphic);
    }

    public static AttachementPopOver getInstance(){
        if (attachementPopOver == null)
            attachementPopOver = new AttachementPopOver();
        return attachementPopOver;
    }

    private void deleteAttachement(ActionEvent event){
        this.hide();
        MainService.getInstance().launch(new Task<Void>() {
            @Override protected Void call() throws Exception {
                int deleteStatus = selectedAttachement.delete();
                if (deleteStatus==1){
                    Platform.runLater(() -> {
                        TilePane pjTilepane = PieceJointeInfoController.getInstance().getPjTilepane();
                        boolean remove = pjTilepane.getChildren().remove((AttachementForView)jfxButton.getParent());
                        if (remove){
                            String message = " Piece jointe supprimé avec suucès ";
                            Notification.getInstance(message, NotifType.SUCCESS).showNotif();
                        }
                    });
                }else {
                    String message = " Impossible de supprimer la piece jointe "+selectedAttachement.getDescription()+"."+selectedAttachement.getExtensionPiece();
                    Notification.getInstance(message, NotifType.WARNING).showNotif();
                }
                return null;
            }
        });
    }

    private void viewAttachement(ActionEvent event){
        this.hide();
        selectedAttachement.visualize();
    }

    public static Attachement getSelectedAttachement() {
        return selectedAttachement;
    }
    private static Attachement selectedAttachement;
    private static AttachementPopOver attachementPopOver;
    private static JFXButton jfxButton;
    @FXML private JFXButton viewbtn;
    @FXML private JFXButton downBtn;
    @FXML private JFXButton delAttachement;
}
