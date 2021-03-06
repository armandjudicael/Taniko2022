package view.Dialog.FormDialog;

import controller.detailsController.ProcedureInfoController;
import controller.viewController.AffairViewController;
import controller.viewController.MainController;
import dao.DaoFactory;
import model.Enum.NotifType;
import model.Enum.TypeDemande;
import model.other.MainService;
import view.Dialog.Other.Notification;
import view.Model.ViewObject.ProcedureForView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class NewProcedureDialog extends JFXDialog implements Initializable {
    private NewProcedureDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Form/Other/newProcedureForm.fxml"));
            loader.setController(this);
            this.setContent((AnchorPane) loader.load());
            this.setDialogContainer(MainController.getInstance().getMainStackPane());
            this.setTransitionType(DialogTransition.CENTER);
            this.setOverlayClose(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NewProcedureDialog getInstance() {
        if (newProcedureDialog == null)
            newProcedureDialog = new NewProcedureDialog();
        return newProcedureDialog;
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        addBtn.disableProperty().bind(procedureName.textProperty().isEmpty());
        closeBtn.setOnAction(event -> this.close());
        closeBtn1.setOnAction(event -> this.close());
        addBtn.setOnAction(event -> {
            this.close();
            MainService.getInstance().launch(saveProcedure());
        });
    }

    private Task<Void> saveProcedure(){
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                TypeDemande typeDemande = AffairViewController
                        .getInstance()
                        .getTableView()
                        .getSelectionModel()
                        .getSelectedItem()
                        .getTypeDemande();
                String name = procedureName.getText();
                String type = typeDemande.equals(TypeDemande.ACQUISITION) ? "A" : "P";
                // SAVE THE PROCEDURE ON THE DATABASE
                int status = DaoFactory.getProcedureDao().create(name,type);
                if (status!=0){
                    // INSERT ON THE FILE SYSTEM
                    int idprcd = DaoFactory.getProcedureDao().getLastInsertId();
                    ProcedureForView view = new ProcedureForView(
                            idprcd,
                            new SimpleStringProperty(name),
                            new SimpleStringProperty(""),
                            new SimpleBooleanProperty(false),
                            new SimpleStringProperty(""),
                            new SimpleStringProperty(""),
                            new SimpleStringProperty(""));
                    ProcedureInfoController.getInstance().getProcedureTableView().getItems().add(view);
                    Platform.runLater(() -> {
                        procedureName.setText("");
                        Notification.getInstance("Nouveau procedure enr??gistr?? avec succ??s", NotifType.SUCCESS).
                                showNotif();
                    });
                }
                return null;
            }
        };
    }
    private static NewProcedureDialog newProcedureDialog;
    @FXML private JFXButton closeBtn;
    @FXML private JFXButton closeBtn1;
    @FXML private JFXButton addBtn;
    @FXML private TextArea procedureName;
}
