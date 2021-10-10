package View.helper;

import DAO.DaoFactory;
import Model.Other.MainService;
import Model.Pojo.PieceJointe;
import View.Model.PieceJointeForView;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.List;
import java.util.stream.Collectors;

public class AttachementRemoverButton implements EventHandler<ActionEvent>{

    private Boolean isOnDetailsView;
    private ObservableList<Node> attachementList;
    private static ObservableList<PieceJointe> removalAttachements;

    public AttachementRemoverButton(JFXButton button,ObservableList<Node> attachementList,Boolean isOnDetailsView) {
        this.isOnDetailsView = isOnDetailsView;
        this.attachementList = attachementList;
        button.setOnAction(this);
    }

    @Override
    public void handle(ActionEvent event) {
        List<Node> collect = attachementList.stream().filter(node -> {
            PieceJointeForView pieceJointeForView = (PieceJointeForView) node;
            boolean selected = pieceJointeForView.getPieceCheckbox().isSelected();
            if (selected) {
                removalAttachements.add(pieceJointeForView.getPieceJointe());
                return true;
            }
            else return false;
        }).collect(Collectors.toList());
        if (!collect.isEmpty()){
          removeAttachement(collect);
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(" Veuillez cochez le(s) pièce(s) jointe à supprimer ");
            alert.showAndWait();
        }
    }

    private void removeAttachement(List<Node> collect){
        if (!isOnDetailsView)
            attachementList.removeAll(collect);
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText(" Etes-vous sur de vouloir supprimer ces "+collect.size()+" piece jointe(s) ?");
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType.equals(ButtonType.OK)){
                    attachementList.removeAll(collect);
                    MainService.getInstance().launch(new Task<Void>() {
                        @Override protected Void call() throws Exception {
                            DaoFactory.getPieceJointeDao().removeAll(removalAttachements);
                            return null;
                        }
                        @Override protected void scheduled() {
                         // C'EST ICI QU'ON BIND AFFICHE LA PROGRESSBAR DE SUPPRESSION
                        }
                        @Override protected void succeeded() {
                           removalAttachements.clear();
                        }
                    });
                }
            });
        }
    }

}
