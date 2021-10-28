package View.Dialog.Other;

import controller.detailsController.AffairDetailsController;
import controller.viewController.MainController;
import dao.DbOperation;
import Model.Enum.Type;
import Model.Enum.NotifType;
import Model.Pojo.Affaire;
import Model.Pojo.Terrain;
import Model.Pojo.Titre;
import Model.Other.MainService;
import View.Cell.ListCell.TitleCell;
import View.Helper.Other.AutoCompleteCombobox;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class TitreMereEditDialog extends JFXDialog  implements Initializable {

    private static TitreMereEditDialog titreMereEditDialog;
    @FXML private JFXButton closeBtn;
    @FXML private JFXButton closeBtn1;
    @FXML private JFXButton addBtn;
    @FXML private JFXComboBox<Titre> titleCombobox;

    public TitreMereEditDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Form/Other/titreMereEdit.fxml"));
            loader.setController(this);
            this.setContent((AnchorPane) loader.load());
            this.setDialogContainer(MainController.getInstance().getMainStackPane());
            this.setTransitionType(DialogTransition.CENTER);
            this.setOverlayClose(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TitreMereEditDialog getInstance(){
        if (titreMereEditDialog==null)
            titreMereEditDialog = new TitreMereEditDialog();

        return titreMereEditDialog;
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle){

        titreMereEditDialog = this;

        closeBtn.setOnAction(event -> this.close());

        closeBtn1.setOnAction(event -> this.close());

        addBtn.disableProperty().bind(titleCombobox.valueProperty().isNull());

        titleCombobox.setCellFactory(titreListView -> new TitleCell());

        new AutoCompleteCombobox<Titre>(titleCombobox, new Predicate<Titre>() {
            @Override
            public boolean test(Titre titre) {
                String text = titleCombobox.getEditor().getText();
                if (titre.toString().startsWith(text))
                    return true;
                else return false;
            }
        }, Type.TITRE);

        addBtn.setOnAction(event -> {
            this.close();
            MainService.getInstance().launch(new Task<Void>() {
                @Override
                protected void succeeded() {
                    titleCombobox.setValue(null);
                }

                @Override
                protected Void call() throws Exception {
                    upadteTitreMere();
                    return null;
                }
            });
        });
    }

    private void upadteTitreMere(){

            Titre newValue = titleCombobox.getValue();
            Affaire affaire = AffairDetailsController.getAffaire();
            Terrain terrain = affaire.getTerrain();
            Label nomPropriete = AffairDetailsController.getInstance().getNomPropriete();
            int queryStatus = DbOperation.launchQuery("UPDATE terrain_depend_titre SET titreId = " + newValue.getId() + " WHERE terrainId =" + terrain.getIdTerrain() + ";");
            if (queryStatus!=0){
                Platform.runLater(() -> {
                    nomPropriete.setText(newValue.toString());
                    String message = " Mis à jour enregistré avec succès ";
                    Notification.getInstance(message, NotifType.SUCCESS).showNotif();
                });
            }else {
                Platform.runLater(() -> {
                    String message = " Echec de la mise a jour de la titre mère ";
                    Notification.getInstance(message, NotifType.WARNING).showNotif();
                });
            }
    }

}
