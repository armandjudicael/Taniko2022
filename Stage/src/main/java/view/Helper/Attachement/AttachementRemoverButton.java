package view.Helper.Attachement;

import dao.DaoFactory;
import model.Enum.NotifType;
import model.other.MainService;
import model.pojo.business.attachement.Attachement;
import view.Dialog.Other.Notification;
import view.Model.Dialog.AlertDialog;
import view.Model.ViewObject.AttachementForView;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
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

    public AttachementRemoverButton(JFXButton button,ObservableList<Node> attachementList,Boolean isOnDetailsView) {
        this.isOnDetailsView = isOnDetailsView;
        this.attachementList = attachementList;
        button.setOnAction(this);
    }

    @Override public void handle(ActionEvent event){
        List<Node> collect = attachementList.stream().filter(node -> {
            if ( !(node instanceof JFXButton )){
                AttachementForView attachementForView = (AttachementForView) node;
                boolean selected = attachementForView.getAttachCheckbox().isSelected();
                if (selected){
                    if (isOnDetailsView)
                        removalAttachements.add(attachementForView.getAttachement());
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
        if (!collect.isEmpty()){
          removeAttachement(collect);
        }else AlertDialog.getInstance(Alert.AlertType.ERROR, "Veuillez cochez le(s) pièce(s) jointe à supprimer ").show();
    }

    private void removeAttachement(List<Node> collect){
        if (!isOnDetailsView)
            attachementList.removeAll(collect);
        else{
            String message =" Etes-vous sur de vouloir supprimer ce(s) "+collect.size()+" piece jointe(s) ?";
            AlertDialog.getInstance(Alert.AlertType.CONFIRMATION,message).showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                        attachementList.removeAll(collect);
                        MainService.getInstance().launch(new Task<Void>() {
                            @Override protected Void call() throws Exception {
                                int[] ints = DaoFactory.getPieceJointeDao().removeAll(removalAttachements);
                                if (collect.size() == ints.length)
                                    Notification.getInstance(collect.size()+" Pièce(s) jointe(s) supprimé(s) avec succés",NotifType.SUCCESS).showNotif();
                                return null;
                            }
                            @Override protected void scheduled() {
                                // C'EST ICI QU'ON BIND AFFICHE LA PROGRESSBAR DE SUPPRESSION
                            }
                            @Override protected void succeeded() {
                                removalAttachements.clear();
                            }
                        });
                    });
        }
    }
    private Boolean isOnDetailsView;
    private ObservableList<Node> attachementList;
    private static ObservableList<Attachement> removalAttachements = FXCollections.observableArrayList();
}
