package View.helper;

import Controller.detailsController.PieceJointeViewController;
import Model.Other.MainService;
import Model.Pojo.PieceJointe;
import View.Dialog.Other.Notification;
import View.Model.PieceJointeForView;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.TilePane;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DownloaderButton implements EventHandler<ActionEvent> {

    public DownloaderButton(JFXButton button,Boolean isOnDetails){
        button.setOnAction(this);
        this.isOnDetails = isOnDetails;
    }

    @Override public void handle(ActionEvent event){
        MainService.getInstance().launch(new Task<Void>(){
            @Override protected Void call() throws Exception {
                if (isOnDetails) downloadAllSelectedAttachement();
                else downloadSelectedAttachement();
                return null;
            }
            @Override protected void scheduled() {

            }
            @Override protected void succeeded() {

            }
        });
    }
    private void downloadAllSelectedAttachement(){
        PieceJointeViewController instance = PieceJointeViewController.getInstance();
        TilePane pjTilepane = instance.getPjTilepane();
        ObservableList<Node> children = pjTilepane.getChildren();
        children.forEach(node -> {
            PieceJointeForView pieceJointeForView = (PieceJointeForView) node;
            PieceJointe pieceJointe = pieceJointeForView.getPieceJointe();
            if (pieceJointeForView.getPieceCheckbox().isSelected()){
                createFileFromInputstream(pieceJointe);
            }
        });
    }

    private void downloadSelectedAttachement(){
        AttachementPopup.getInstance().hide();
        PieceJointe selectedAttachement = AttachementPopup.getSelectedAttachement();
        createFileFromInputstream(selectedAttachement);
    }

    private void createFileFromInputstream(PieceJointe attachement){
        try {
            InputStream valeur = attachement.getInputStream();
            if (valeur!=null){
                String extension = attachement.getExtensionPiece();
                String fileName = attachement.getDescription();
                String path = fileName+"."+extension;
                File file = new File(path);
                FileUtils.copyInputStreamToFile(valeur,file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Boolean isOnDetails;
}
