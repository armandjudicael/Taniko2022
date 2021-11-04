package view.Helper.Attachement;

import controller.detailsController.PieceJointeInfoController;
import model.Enum.NotifType;
import model.other.MainService;
import model.pojo.business.PieceJointe;
import view.Dialog.Other.Notification;
import view.Model.ViewObject.PieceJointeForView;
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
            @Override protected Void call() throws Exception {
                int deleteStatus = selectedAttachement.delete();
                if (deleteStatus==1){
                    Platform.runLater(() -> {
                        TilePane pjTilepane = PieceJointeInfoController.getInstance().getPjTilepane();
                        boolean remove = pjTilepane.getChildren().remove((PieceJointeForView)jfxButton.getParent());
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
