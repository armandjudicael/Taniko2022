package view.Dialog.Other;
import controller.detailsController.AffairDetailsController;
import controller.detailsController.RedacteurInfoController;
import controller.viewController.MainController;
import dao.DbUtils;
import model.Enum.NotifType;
import model.other.EmailFactory;
import model.other.SmsUtils;
import model.pojo.business.other.Dossier;
import model.pojo.business.other.User;
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
    @Override public void initialize(URL url, ResourceBundle resourceBundle){
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
                if ( user.getNom().toLowerCase().startsWith(text))
                    return true;
                else return false;
            }
        });
    }
    private Task insertTask(){
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
                Dossier dossier = AffairDetailsController.getAffaire();
                // insertion dans la table des dispatch
                if (DbUtils.insertOnDispatchTable(user, dossier) == 1) {
                    String nomR = user.getNom();
                    if (checkBoxNouvDispatch.isSelected()){
                        String email = dossier.getDemandeur().getEmail();
                        String numTel = dossier.getDemandeur().getNumTel();
                        String message = "Salama tompoko ,Mampahafantarana anao izahay ato amin'ny sampan-draharahan'ny fananan-tany fa i "
                                +nomR+"no mpiasa tompon-andraikitra amin'ny atontan-taratasinao manomboka izao . misaotra tompoko !";
                        if (!email.isBlank() && !email.isEmpty()) EmailFactory.sendTo(email,"Dispatch affaire",message);
                        if (!numTel.isBlank() && !numTel.isEmpty() ) SmsUtils.send(numTel,message);
                    }
                    String message = nomR + " est le nouveau rédacteur de cette affaire ";
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
