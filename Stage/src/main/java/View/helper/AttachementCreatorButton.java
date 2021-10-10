package View.helper;

import Controller.detailsController.AffairDetailsController;
import DAO.DaoFactory;
import Model.Other.MainService;
import Model.Pojo.PieceJointe;
import View.Dialog.Other.FileChooserDialog;
import View.Model.PieceJointeForView;
import com.jfoenix.controls.JFXButton;
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
        //        Dialog dialog = new Dialog();
//        DialogPane dialogPane = new DialogPane();
//        dialogPane.setContent(new Node() {});
//        dialog.setDialogPane(dialogPane);
//        dialog.initStyle(StageStyle.UNDECORATED);
    }
    public static ObservableList<PieceJointe> getAttachementList() {
        return pieceJointeObservableList;
    }

    private Task<Void> saveAttachementTask(){
        return new Task<Void>() {
            @Override protected Void call() throws Exception {
                pieceJointeObservableList = FXCollections.observableArrayList();
                pieceJointeFiles.forEach(file -> {
                    try {
                        pieceJointeObservableList.add(createAttachementBy(file));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                });
                DaoFactory.getPieceJointeDao().createAll(pieceJointeObservableList,AffairDetailsController.getAffaire());
                return null;
            }
            @Override protected void scheduled() {}
            @Override
            protected void succeeded() {
                pieceJointeObservableList.clear();
            }
        };
    }

    @Override
    public void handle(ActionEvent event) {
        pieceJointeFiles.setAll(FileChooserDialog.getInstance());
        if (pieceJointeFiles != null && !pieceJointeFiles.isEmpty()) {
            if (pieceJointeFiles.size()>10){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(" le nombre de limite de fichier selectionné a importer simultanement est de 10");
                alert.showAndWait();
                return;
            }
            pieceJointeFiles.forEach(file -> {
                try {
                    PieceJointe attachement = createAttachementBy(file);
                    pieceJointeObservableList.add(attachement);
                    attachementList.add(new PieceJointeForView(attachement, false));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
            if (isOnDetailsView)
                MainService.getInstance().launch(saveAttachementTask());
        }
    }

    private static   ObservableList<PieceJointe> pieceJointeObservableList;
    private static ObservableList<File> pieceJointeFiles;
    private  ObservableList<Node> attachementList;
    private Boolean isOnDetailsView;
}
