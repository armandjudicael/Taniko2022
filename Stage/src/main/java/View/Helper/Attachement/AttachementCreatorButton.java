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

public class AttachementCreatorButton implements EventHandler<ActionEvent> {

    public AttachementCreatorButton(JFXButton button, ObservableList<Node> list, Boolean isOnDetailsView){
        button.setOnAction(this);
        this.attachementList = list;
        this.isOnDetailsView = isOnDetailsView;
    }

    private PieceJointe createAttachementBy(File file) throws FileNotFoundException {
        String name = file.getName();
        String[] split = name.split("\\.");
        String description = split[0];
        String extension = split[1];
        String size = calculateFileSize(file);
        return new PieceJointe(description,extension,size,new FileInputStream(file));
    }

    public String calculateFileSize(File file){
        long length = FileUtils.sizeOf(file);
        long kilo = length / FileUtils.ONE_KB;
        if (kilo<FileUtils.ONE_KB) return kilo+" Kb";
        return  kilo/FileUtils.ONE_MB+" Mb";
    }

    public static ObservableList<PieceJointe> getAttachementList() {
        return pieceJointeObservableList;
    }
    private Task<Void> saveAttachementTask(){
        return new Task<Void>() {
            @Override protected Void call() throws Exception{
                pieceJointeObservableList = FXCollections.observableArrayList();
                pieceJointeFiles.forEach(file -> {
                    try {
                        pieceJointeObservableList.add(createAttachementBy(file));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
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
        pieceJointeFiles.setAll(FileChooserDialog.getInstance());
        if (pieceJointeFiles != null && !pieceJointeFiles.isEmpty()){
            if (pieceJointeFiles.size()>10){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(" Le nombre de limite de fichier selectionné à importer simultanement est de 10");
                alert.showAndWait();
                return;
            }
            pieceJointeFiles.forEach(file -> {
                try {
                    PieceJointe attachement = createAttachementBy(file);
                    pieceJointeObservableList.add(attachement);
                    attachementList.add(new PieceJointeForView(attachement, isOnDetailsView));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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
