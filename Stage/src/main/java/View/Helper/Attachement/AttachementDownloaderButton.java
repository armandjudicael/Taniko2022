package View.Helper.Attachement;

import Controller.detailsController.PieceJointeViewController;
import Model.Other.MainService;
import Model.Pojo.PieceJointe;
import View.Model.PieceJointeForView;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.TilePane;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class AttachementDownloaderButton implements EventHandler<ActionEvent>{

    public AttachementDownloaderButton(JFXButton button, Boolean isOnDetails){
        button.setOnAction(this);
        this.isOnDetails = isOnDetails;
    }
    @Override public void handle(ActionEvent event){
        Task<Void> task = new Task<>() {
            @Override protected Void call() throws Exception {
                if (isOnDetails) downloadAllSelectedAttachement();
                else downloadSelectedAttachement();
                return null;
            }
            @Override protected void scheduled() {}
            @Override protected void succeeded(){
               openExplorer();
            }
        };
        MainService.getInstance().launch(task);
    }
    private void downloadAllSelectedAttachement(){
        PieceJointeViewController instance = PieceJointeViewController.getInstance();
        TilePane pjTilepane = instance.getPjTilepane();
        ObservableList<Node> children = pjTilepane.getChildren();
        List<Node> collect = children.stream().filter(node -> {
            PieceJointeForView pieceJointeForView = (PieceJointeForView) node;
            PieceJointe pieceJointe = pieceJointeForView.getPieceJointe();
            if (pieceJointeForView.getPieceCheckbox().isSelected()){
                pieceJointe.download();
                return true; }
            else return false;
        }).collect(Collectors.toList());

        if (collect.isEmpty()){
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(" Veuillez cochez le(s) piece(s) jointe(s) a télécharger !");
                alert.show();
            });
        }

    }
    private void downloadSelectedAttachement(){
        AttachementPopup.getInstance().hide();
        PieceJointe selectedAttachement = AttachementPopup.getSelectedAttachement();
        selectedAttachement.download();
    }

    private void openExplorer(){
       if (Desktop.isDesktopSupported()){
           try {
               if (desktop==null) desktop = Desktop.getDesktop();
               desktop.open(new File(PieceJointe.getDownloadDirectory()));
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }

    private static String path = "";
    private static Desktop desktop;
    private Boolean isOnDetails;
}
