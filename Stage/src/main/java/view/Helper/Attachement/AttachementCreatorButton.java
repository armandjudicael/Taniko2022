package view.Helper.Attachement;
import controller.detailsController.AffairDetailsController;
import dao.DaoFactory;
import model.Enum.FileChooserType;
import model.Enum.NotifType;
import model.other.MainService;
import model.pojo.business.PieceJointe;
import view.Dialog.Other.FileChooserDialog;
import view.Dialog.Other.Notification;
import view.Helper.Other.TanikoProgress;
import view.Model.Dialog.AlertDialog;
import view.Model.ViewObject.PieceJointeForView;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import java.io.File;
import java.util.List;

public class AttachementCreatorButton implements EventHandler<ActionEvent>{

    public AttachementCreatorButton(JFXButton button, ObservableList<Node> list, Boolean isOnDetailsView){
        button.setOnAction(this);
        this.attachementList = list;
        this.isOnDetailsView = isOnDetailsView;
    }

    @Override public void handle(ActionEvent event){
        List<File> instance = FileChooserDialog.getInstance(FileChooserType.ATTACHEMENT);
        if (instance!=null && !instance.isEmpty()){
            pieceJointeFiles.setAll(instance);
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    savefile();return null; }
                @Override protected void succeeded() {
                    pieceJointeFiles.clear(); }
            };
            if(isOnDetailsView)
                TanikoProgress.getInstance(task,"Enregistrement des fichiers ........");
            MainService.getInstance().launch(task);
        }
    }
    private void savefile(){
        if (pieceJointeFiles!= null && !pieceJointeFiles.isEmpty()){
            if (pieceJointeFiles.size()>10){
                String message ="Le nombre de limite de fichier selectionn?? ?? importer simultanement est de 10";
                AlertDialog.getInstance(Alert.AlertType.ERROR,message).showAndWait();
                return;
            }
            pieceJointeFiles.forEach(file ->{
                PieceJointe attachement = new PieceJointe(file);
                pieceJointeObservableList.add(attachement);
                Platform.runLater(() -> attachementList.add(0,new PieceJointeForView(attachement, isOnDetailsView)) );
            });
            if (isOnDetailsView) saveFileOnDb();
        }
    }
    private void saveFileOnDb(){
        int[] all = DaoFactory.getPieceJointeDao().createAll(pieceJointeObservableList, AffairDetailsController.getAffaire());
        if (all.length == pieceJointeObservableList.size()){
            String message = all.length+" piece(s) jointe(s) enregistrer avec succ??s !";
            Notification.getInstance(message, NotifType.SUCCESS).showNotif();
        }else Notification.getInstance(
                " Echec de l'enregistrement de(s) piece(s) jointe(s) ",
                NotifType.WARNING).showNotif();
    }
    public static ObservableList<PieceJointe> getAttachementList() {
        return pieceJointeObservableList;
    }
    private static ObservableList<PieceJointe> pieceJointeObservableList = FXCollections.observableArrayList();
    private static ObservableList<File> pieceJointeFiles = FXCollections.observableArrayList();
    private  ObservableList<Node> attachementList;
    private Boolean isOnDetailsView;
}
