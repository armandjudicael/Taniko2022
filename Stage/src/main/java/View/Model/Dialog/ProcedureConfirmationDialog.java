package View.Model.Dialog;
import Controller.ViewController.MainController;
import Controller.detailsController.ProcedureViewController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProcedureConfirmationDialog extends JFXDialog implements Initializable {

    public ProcedureConfirmationDialog(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View/Dialog/alert.fxml"));
            loader.setController(this);
            this.setContent((AnchorPane) loader.load());
            this.setDialogContainer(MainController.getInstance().getMainStackPane());
            this.setTransitionType(DialogTransition.CENTER);
            this.setOverlayClose(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ProcedureConfirmationDialog getInstance(ProcedureViewController.StatusCheckBox checkBox, EventHandler<ActionEvent> handler){
        if (procedureConfirmationDialog == null) procedureConfirmationDialog = new ProcedureConfirmationDialog();
        if (handler!=null) procedureConfirmationDialog.getOkBtn().setOnAction(handler);
        procedureConfirmationDialog.initTextAndSmsCheckbox(checkBox);
        return procedureConfirmationDialog;
    }

    private void initTextAndSmsCheckbox(ProcedureViewController.StatusCheckBox checkBox){
        if (checkBox!=null){
            if (checkBox.isSelected()){ notifMessage.setText("Voulez-vous decoché cette procedure de cette affaire ?"); }
            else notifMessage.setText(" Voulez-vous coché cette procedure ? ");
            smsAndEmailCheckBox.setVisible(!checkBox.isSelected());
        }else{
            String message = " Etes - vous sure de vouloir supprimé ce(s) procédure(s)";
            notifMessage.setText(message);
        }
    }

    @Override public void initialize(URL location, ResourceBundle resources) {
       procedureConfirmationDialog = this;
       closeBtn.setOnAction(event -> this.close());
       notOkBtn.setOnAction(event -> this.close());
    }

    public static ProcedureConfirmationDialog getProcedureConfirmationDialog() {
        return procedureConfirmationDialog;
    }
    public JFXCheckBox getSmsAndEmailCheckBox() {
        return smsAndEmailCheckBox;
    }
    public JFXButton getOkBtn() {
        return okBtn;
    }
    private static ProcedureConfirmationDialog procedureConfirmationDialog;
    @FXML private JFXButton closeBtn;
    @FXML private ImageView imageView;
    @FXML private Label notifMessage;
    @FXML private JFXButton notOkBtn;
    @FXML private JFXButton okBtn;
    @FXML private JFXCheckBox smsAndEmailCheckBox;
}
