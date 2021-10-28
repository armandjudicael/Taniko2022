package View.Dialog.FormDialog;

import controller.viewController.MainController;
import controller.viewController.TitleViewController;
import dao.DaoFactory;
import dao.DbOperation;
import Model.Enum.NotifType;
import Model.Enum.TableName;
import Model.Enum.TitleOperation;
import Model.Pojo.Affaire;
import Model.Pojo.Titre;
import Model.Other.MainService;
import View.Dialog.Other.Notification;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class TitleFrom extends JFXDialog implements Initializable {

    private final String TITRE_CIGLE = "-BA";
    private static TitleFrom titleFrom;
    private static Affaire exAffaire;
    @FXML private TextField nomTitulaire;
    @FXML private TextField prenomTitulaire;
    @FXML private TextField nomPropriete;
    @FXML private TextField numeroPropriete;
    @FXML private DatePicker dateAttribution;
    @FXML private TextField morcelement;
    @FXML private TextField numImmat;
    @FXML private JFXButton closeBtn;
    @FXML private JFXButton saveBtn;
    @FXML private JFXButton closeStage;
    @FXML private Label titleLabel;
    private static TitleOperation operation;
    @FXML private Label errorNumLabel;
    @FXML private Label errorMorcLabel;

    private TitleFrom() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Form/Other/TitleForm.fxml"));
            loader.setController(this);
            this.setContent((AnchorPane) loader.load());
            this.setDialogContainer(MainController.getInstance().getMainStackPane());
            this.setTransitionType(DialogTransition.CENTER);
            this.setOverlayClose(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void show(TitleOperation titleOperation){
        operation = titleOperation;
        if (titleOperation.equals(TitleOperation.CREATE_OPERATION)){
            titleLabel.setText(" Nouveau titre ");
        }else {
            titleLabel.setText(" Editer titre ");
        }
        super.show();
    }

    public static TitleFrom getInstance(){
        if (titleFrom == null) {
            titleFrom = new TitleFrom();
        }
        return titleFrom;
    }

    private void bindSaveBtn(){

        BooleanBinding booleanBinding = nomPropriete.textProperty().isEmpty().
                or(numeroPropriete.textProperty().isEmpty())
                .or(errorNumLabel.visibleProperty())
                .or(errorMorcLabel.visibleProperty())
                .or(Bindings.and(numImmat.textProperty().isNotEmpty(),morcelement.textProperty().isEmpty()));

        saveBtn.disableProperty().bind(booleanBinding);
    }

    public static void setExAffaire(Affaire exAffaire) {
        TitleFrom.exAffaire = exAffaire;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleFrom = this;
        dateAttribution.setValue(LocalDate.now());
        saveBtn.setOnAction(event -> {
            //  fermer le dialog
            this.close();
            MainService.getInstance().launch(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    saveTitleOnDB();
                    return null;
                }
            });
        });
        bindSaveBtn();
        closeBtn.setOnAction(event -> this.close());
        closeStage.setOnAction(event -> this.close());
    }

    private void resetFrom() {
        nomTitulaire.clear();
        prenomTitulaire.clear();
        nomPropriete.clear();
        numeroPropriete.clear();
        morcelement.clear();
        numImmat.clear();
    }
    public void saveTitleOnDB(){

        if (!numeroPropriete.getText().isEmpty() && !nomPropriete.getText().isEmpty()) {

            Titre titre = new Titre();

            String numeroTitre = numeroPropriete.getText()+TITRE_CIGLE;
            String fullName = nomTitulaire.getText() + " " + prenomTitulaire.getText();
            String dateCreation = dateAttribution.getValue().toString() + " " + LocalTime.now().toString();
            String nomPPT = nomPropriete.getText();
            String numMorcelemment = morcelement.getText().isEmpty() ? "" : morcelement.getText()+TITRE_CIGLE;
            String numeroTitreMere = numImmat.getText();

            if (numImmat.disableProperty().get())
                titre = new Titre(
                        numeroTitre,
                        nomPPT,
                        fullName,
                        dateCreation,
                        numMorcelemment
                        ,numeroTitreMere);
            else titre = new Titre(numeroTitre, nomPPT, fullName, dateCreation, "", "");
            titre.setDate(Timestamp.valueOf(LocalDateTime.of(dateAttribution.getValue(), LocalTime.now())));
            switch (operation){
                case CREATE_OPERATION:{
                    titre.setNumExAffaire(exAffaire.getNumero());
                    createOperation(titre);
                }break;
                case UPDATE_OPERATION:{
                    updateOperation(titre);
                }break;
                default:{
                    createOperation(titre);
                }
            }
            resetFrom();
        }
    }

    private void updateOperation(Titre titre) {
        Titre selectedItem = TitleViewController.getInstance().getTableView().getSelectionModel().getSelectedItem();
        int id = selectedItem.getId();
        System.out.println(id);
        titre.setId(id);

        if (DaoFactory.getTitreDao().update(titre) != 0) {
            Platform.runLater(() -> Notification.getInstance(" Mis à jour titre avec succès ", NotifType.SUCCESS).showNotif());
        }

    }

    private void createOperation(Titre titre) {

        if (DaoFactory.getTitreDao().create(titre) == 1) {
            int idTitre = DaoFactory.getTitreDao().findByNum(titre.getNumero());
            titre.setId(idTitre);
            exAffaire.getTerrain().setTitreDependant(titre);
            int status = DbOperation.insertOnTerrrainTitre(exAffaire.getTerrain(),TableName.ABOUTIR_TERRAIN_TITRE);
            if (status == 1){
                Platform.runLater(() -> {
                    // mis a jour de l'affichage
                    TableView<Titre> titreTableView = TitleViewController.getInstance().getTableView();
                    titreTableView.getItems().add(titre);
                    Notification.getInstance("Le titre N° " + numeroPropriete.getText() + " est enrégistrer avec succès", NotifType.SUCCESS).showNotif();
                });
            }
        }
    }

    public TextField getNomTitulaire() {
        return nomTitulaire;
    }
    public TextField getPrenomTitulaire() {
        return prenomTitulaire;
    }
    public TextField getNomPropriete() {
        return nomPropriete;
    }
    public TextField getNumeroPropriete() {
        return numeroPropriete;
    }

    public DatePicker getDateAttribution() {
        return dateAttribution;
    }

    public TextField getMorcelement() {
        return morcelement;
    }

    public TextField getNumImmat() {
        return numImmat;
    }

    public JFXButton getCloseBtn() {
        return closeBtn;
    }

    public JFXButton getSaveBtn() {
        return saveBtn;
    }
}
