package view.Dialog.Other;

import controller.detailsController.AffairDetailsController;
import controller.detailsController.RedacteurInfoController;
import controller.viewController.MainController;
import dao.DbOperation;
import model.Enum.NotifType;
import model.pojo.business.Affaire;
import model.pojo.business.User;
import model.other.MainService;
import view.Cell.ListCell.DispatchListcell;
import view.Model.ViewObject.EditorForView;
import view.Helper.Other.AutoCompleteCombobox;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class DispatchDialog extends JFXDialog implements Initializable {

    private DispatchDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Form/Other/newDispatchDialog.fxml"));
            loader.setController(this);
            this.setContent((AnchorPane) loader.load());
            this.setDialogContainer(MainController.getInstance().getMainStackPane());
            this.setTransitionType(DialogTransition.CENTER);
            this.setOverlayClose(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DispatchDialog getInstance() {
        if (dispatchDialog == null) {
            dispatchDialog = new DispatchDialog();
        }
        return dispatchDialog;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        closeBtn.setOnAction(event -> this.close());
        closeBtn1.setOnAction(event -> this.close());
        addBtn.disableProperty().bind(redactorCombobox.valueProperty().isNull());
        addBtn.setOnAction(event -> {
            this.close();
            MainService.getInstance().launch(insertTask());
        });
        redactorCombobox.setCellFactory(userListView -> new DispatchListcell());
        new AutoCompleteCombobox<User>(redactorCombobox, new Predicate<User>(){
            @Override public boolean test(User user){
                String text = redactorCombobox.getEditor().getText();
                if ( user.getNom().toLowerCase().startsWith(text) || user.getPrenom().toLowerCase().startsWith(text))
                    return true;
                else return false;
            }
        });
    }

    private Task insertTask() {

        return new Task() {
            @Override protected void succeeded() {
                redactorCombobox.setValue(null);
                checkBoxNouvDispatch.setSelected(false);
            }
            @Override protected Void call() throws Exception {
                User user = redactorCombobox.getValue();
                // ajout nouveau redacteur dans la liste
                RedacteurInfoController.getInstance().getEditorTableView().getItems().add(new EditorForView(Timestamp.valueOf(LocalDateTime.now()),
                        user.getFullName(),
                        user.getPhoto(),
                        user.getId()));
                Affaire affaire = AffairDetailsController.getAffaire();
                // insertion dans la table des dispatch
                if (DbOperation.insertOnDispatchTable(user,affaire) == 1) {
                    if (checkBoxNouvDispatch.isSelected()){
                        String nomR = user.getNom();
                        String prenomR = user.getPrenom();
                 //       DemandeurPhysique demandeurPhysique = affaire.getDemandeur();
                        String emailBody = "Salama tompoko ,Mampahafantarana anao izahay ato amin'ny sampan-draharahan'ny fananan-tany fa i "+nomR+" "+prenomR+" no"+
                                " mpiasa tompon-andraikitra amin'ny atontan-taratasinao manomboka izao . misaotra tompoko !";
                    //    Mail.send(emailBody, demandeurPhysique);
                    }
                    String message = user.getPrenom() + " est le nouveau r??dacteur de cette affaire ";
                    Notification.getInstance(message, NotifType.SUCCESS).showNotif();
                }
                return null;
            }
        };
    }

    private static DispatchDialog dispatchDialog;
    @FXML private JFXComboBox<User> redactorCombobox;
    @FXML private JFXButton closeBtn;
    @FXML private JFXButton closeBtn1;
    @FXML private JFXButton addBtn;
    @FXML private JFXCheckBox checkBoxNouvDispatch;
}
