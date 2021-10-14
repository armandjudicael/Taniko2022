package View.Helper.Attachement;

import Controller.detailsController.AffairDetailsController;
import DAO.DaoFactory;
import Model.Enum.NotifType;
import Model.Other.MainService;
import Model.Pojo.PieceJointe;
import View.Dialog.Other.FileChooserDialog;
import View.Dialog.Other.Notification;
import View.Model.PieceJointeForView;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class AttachementCreatorButton implements EventHandler<ActionEvent> {

    public AttachementCreatorButton(JFXButton button, ObservableList<Node> list, Boolean isOnDetailsView){
        button.setOnAction(this);
        this.attachementList = list;
        this.isOnDetailsView = isOnDetailsView;
    }
    public static ObservableList<PieceJointe> getAttachementList() {
        return pieceJointeObservableList;
    }
    private Task<Void> saveAttachementTask(){

        return new Task<Void>() {
            @Override protected Void call() throws Exception{
                pieceJointeObservableList = FXCollections.observableArrayList();
                pieceJointeFiles.forEach(file -> {
                    pieceJointeObservableList.add(new PieceJointe(file));
                });
                int[] all = DaoFactory.getPieceJointeDao().createAll(pieceJointeObservableList, AffairDetailsController.getAffaire());
                if (all.length == pieceJointeObservableList.size()){
                    Platform.runLater(() -> {
                        String message = all.length+"piece(s) jointe(s) enregistrer avec succès !";
                        Notification.getInstance(message, NotifType.SUCCESS).show();
                    });
                }else {
                    Platform.runLater(() -> {
                        String message ="Echec de l'enregistrement de(s) piece(s) jointe(s) ";
                        Notification.getInstance(message, NotifType.WARNING).show();
                    });
                }
                return null;
            }
            @Override protected void succeeded() {
                pieceJointeObservableList.clear();
            }
        };

    }
    @Override public void handle(ActionEvent event){
        List<File> instance = FileChooserDialog.getInstance();
        if(!instance.isEmpty())
            pieceJointeFiles.setAll(instance);
        if (pieceJointeFiles != null && !pieceJointeFiles.isEmpty()){
            if (pieceJointeFiles.size()>10){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(" Le nombre de limite de fichier selectionné à importer simultanement est de 10");
                alert.showAndWait();
                return;
            }
            pieceJointeFiles.forEach(file ->{
                PieceJointe attachement = new PieceJointe(file);
                pieceJointeObservableList.add(attachement);
                attachementList.add(new PieceJointeForView(attachement, isOnDetailsView));
            });
            if (isOnDetailsView)
                MainService.getInstance().launch(saveAttachementTask());
        }
    }
    private static   ObservableList<PieceJointe> pieceJointeObservableList = FXCollections.observableArrayList();
    private static ObservableList<File> pieceJointeFiles = FXCollections.observableArrayList();
    private  ObservableList<Node> attachementList;
    private Boolean isOnDetailsView;
}
