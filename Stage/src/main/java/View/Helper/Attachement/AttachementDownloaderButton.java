package View.Helper.Attachement;

import Controller.detailsController.PieceJointeViewController;
import Model.Enum.NotifType;
import Model.Other.MainService;
import Model.Pojo.PieceJointe;
import View.Dialog.Other.Notification;
import View.Helper.Other.TanikoProgress;
import View.Model.Dialog.AlertDialog;
import View.Model.ViewObject.PieceJointeForView;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AttachementDownloaderButton implements EventHandler<ActionEvent>{

    public AttachementDownloaderButton(JFXButton button, Boolean isOnDetails){
        button.setOnAction(this);
        this.isOnDetails = isOnDetails;
    }

    @Override public void handle(ActionEvent event){
        Task<Void> task = new Task<Void>() {
            @Override protected Void call() throws Exception {
                if (isOnDetails){
                    downloadAllSelectedAttachement();
                }else downloadSelectedAttachement();
                return null;
            }
            @Override protected void succeeded() {
                resetStatus();
            }
        };
        TanikoProgress.getInstance(task,"Chargement des fichiers .........");
        MainService.getInstance().launch(task);
    }

    private void resetStatus(){
        PieceJointeViewController instance = PieceJointeViewController.getInstance();
        TilePane pjTilepane = instance.getPjTilepane();
        ObservableList<Node> children = pjTilepane.getChildren();
        children.forEach(node -> {
            PieceJointeForView pieceJointeForView = (PieceJointeForView) node;
            pieceJointeForView.getPieceCheckbox().setSelected(false);
        });
    }

    private void downloadAllSelectedAttachement(){
        PieceJointeViewController instance = PieceJointeViewController.getInstance();
        TilePane pjTilepane = instance.getPjTilepane();
        ObservableList<Node> children = pjTilepane.getChildren();
        List<Node> collect = children.stream().filter(node -> {
            PieceJointeForView pieceJointeForView = (PieceJointeForView) node;
            if (pieceJointeForView.getPieceCheckbox().isSelected()){
                pieceJointeForView.getPieceJointe().download();
                return true; }
            else return false;
        }).collect(Collectors.toList());
        if (collect.isEmpty()){
            Platform.runLater(() -> {
                String message = "Veuillez cochez le(s) piece(s) jointe(s) a télécharger !";
                AlertDialog.getInstance(Alert.AlertType.ERROR,message).showAndWait();
            });
        }else openExplorer();
    }

    private void downloadSelectedAttachement(){
        AttachementPopup.getInstance().hide();
        PieceJointe selectedAttachement = AttachementPopup.getSelectedAttachement();
        selectedAttachement.download();
        openExplorer();
    }

    private void openExplorer(){
       if (Desktop.isDesktopSupported()){
           try {
               if (desktop==null) desktop = Desktop.getDesktop();
               desktop.open(PieceJointe.getDownloadPathDirectory());
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }

    private static Desktop desktop;
    private Boolean isOnDetails;
}
