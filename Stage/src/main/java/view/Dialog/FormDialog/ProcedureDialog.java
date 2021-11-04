package view.Dialog.FormDialog;

import controller.detailsController.ProcedureInfoController;
import controller.viewController.AffairViewController;
import controller.viewController.MainController;
import model.Enum.NotifType;
import model.other.MainService;
import view.Dialog.Other.Notification;
import view.Model.ViewObject.AffaireForView;
import view.Model.ViewObject.ProcedureForView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ProcedureDialog extends JFXDialog implements Initializable {

    private static ProcedureInfoController.StatusCheckBox checkBoxPrcd;
    private static Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
    private static ProcedureDialog procedureDialog;
    @FXML private JFXTextField numProcedure;
    @FXML private JFXDatePicker dateProcedure;
    @FXML private JFXButton closeBtn1;
    @FXML private JFXButton closeBtn;
    @FXML private JFXButton saveBtn;

    private ProcedureDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Form/Other/procedureNumForm.fxml"));
            loader.setController(this);
            this.setContent((AnchorPane) loader.load());
            this.setDialogContainer(MainController.getInstance().getMainStackPane());
            this.setTransitionType(DialogTransition.CENTER);
            this.setOverlayClose(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ProcedureDialog getInstance(ProcedureInfoController.StatusCheckBox checkBox) {
        if (procedureDialog == null)
            procedureDialog = new ProcedureDialog();
        setCheckBoxPrcd(checkBox);
        return procedureDialog;
    }

    public static void setCheckBoxPrcd(ProcedureInfoController.StatusCheckBox checkBoxPrcd) {
        ProcedureDialog.checkBoxPrcd = checkBoxPrcd;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closeBtn.setOnAction(event -> this.close());
        closeBtn1.setOnAction(event -> this.close());
        saveBtn.setOnAction(event -> {
            // CLOSE THE DIALOG
            this.close();
            checkBoxPrcd.selectedProperty().unbind();
            checkBoxPrcd.selectedProperty().set(true);
            MainService.getInstance().launch(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    saveProcedure();
                    return null;
                }
            });
        });
        dateProcedure.setValue(LocalDate.now());
        numProcedure.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.isEmpty()) {
                if (!pattern.matcher(t1).matches())
                    saveBtn.disableProperty().set(true);
                else saveBtn.disableProperty().set(false);
            } else saveBtn.disableProperty().set(false);
        });
    }

    public void saveProcedure() {
        TableCell cell = (TableCell) checkBoxPrcd.getParent();
        TableRow tableRow = (TableRow) cell.getParent();
        int index = tableRow.getIndex();
        ProcedureForView procedureForView = ProcedureInfoController.getInstance().getProcedureTableView().getItems().get(index);
        AffaireForView affair = AffairViewController.getInstance().getTableView().getSelectionModel().getSelectedItem();
        int idProcedure = procedureForView.getIdProcedure();

        LocalDate date = dateProcedure.getValue();

        LocalDateTime localDateTime = LocalDateTime.of(date, LocalTime.now());

        procedureForView.setDateArrive(date.toString() + " " + LocalTime.now().toString());

        int value = 0;

        if (numProcedure.getText().isEmpty()) {
            procedureForView.setNumDepart("");
            // insertion dans l'annuaire
//            value = DbOperation.insertOnAffAndPrcdTable(idProcedure,
//                    affair.getId(), 0,
//                    Timestamp.valueOf(localDateTime));
            // UPDATE THE PRINCIPALVIEW
            //    affair.setSituation(procedureForView.getProcedureName() + " du " + date.toString() + " " + LocalTime.now().toString());
        } else {
            int num = Integer.valueOf(numProcedure.getText());
            procedureForView.setNumDepart(String.valueOf(numProcedure.getText()));
//            value = DbOperation.insertOnAffAndPrcdTable(idProcedure,
//                    affair.getId(), num,
//                    Timestamp.valueOf(localDateTime));
//            // UPDATE THE PRINCIPALVIEW
            //        affair.setSituation(procedureForView.getProcedureName() + " N° " + numProcedure.getText() + " du " + date.toString() + " " + LocalTime.now().toString());
        }
        numProcedure.setText("");
        if (value == 1)
            Platform.runLater(() -> Notification.getInstance(" Procedure enrégistré avec succès ", NotifType.SUCCESS).
                    showNotif());

    }

    public JFXTextField getNumProcedure() {
        return numProcedure;
    }
    public JFXDatePicker getDateProcedure() {
        return dateProcedure;
    }
}
