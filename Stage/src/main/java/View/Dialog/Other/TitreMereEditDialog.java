package View.Dialog.Other;

import Controller.DetailsController.AffairDetailsController;
import Controller.ViewController.MainController;
import DAO.DaoFactory;
import DAO.DbOperation;
import Model.Enum.AutoCompleteComboboxType;
import Model.Enum.NotifType;
import Model.Pojo.Affaire;
import Model.Pojo.Terrain;
import Model.Pojo.Titre;
import Model.serviceManager.MainService;
import View.Cell.ListCell.TitleCell;
import View.Dialog.FormDialog.AffaireForm;
import View.helper.AutoCompleteCombobox;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Form/titreMereEdit.fxml"));
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
        },AutoCompleteComboboxType.TITRE);

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
                    Notification.getInstance(message, NotifType.SUCCESS).show();
                });
            }else {
                Platform.runLater(() -> {
                    String message = " Echec de la mise a jour de la titre mère ";
                    Notification.getInstance(message, NotifType.WARNING).show();
                });
            }
    }

}
