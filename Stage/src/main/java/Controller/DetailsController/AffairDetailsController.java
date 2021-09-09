package Controller.DetailsController;

import Controller.ViewController.AffairViewController;
import Controller.ViewController.MainController;
import DAO.DaoFactory;
import Model.Enum.AffaireStatus;
import Model.Enum.Origin;
import Model.Enum.TitleOperation;
import Model.Pojo.Affaire;
import Model.Pojo.Terrain;
import Model.Pojo.Titre;
import Model.Pojo.User;
import Model.serviceManager.MainService;
import View.Dialog.FormDialog.AffaireForm;
import View.Dialog.FormDialog.TitleFrom;
import View.Dialog.Other.DispatchDialog;
import View.Dialog.Other.TitreMereEditDialog;
import View.Dialog.SecurityDialog.AdminSecurity;
import View.Dialog.SecurityDialog.EditorSecurity;
import View.Model.AffaireForView;
import View.Model.ConnexAffairForView;
import animatefx.animation.SlideInLeft;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static DAO.DbOperation.executeQuery;

public class AffairDetailsController implements Initializable {
    private static Affaire affaire = null;
    private static AffairDetailsController detailsController;
    // DIALOG
    private static Dialog<String> superficieDialog;
    // Tab
    @FXML private Tab connexeTab;
    @FXML private Tab redacteurTab;
    @FXML private Tab procedureTab;
    // EDITER
    @FXML private JFXButton edtSiseBtn;
    @FXML private JFXButton edtParcelleTerrainBtn;
    @FXML private JFXButton edtCommuneBtn;
    @FXML private JFXButton edtDistrictBtn;
    @FXML private JFXButton editStatusBtn;
    @FXML private JFXButton edtRegionBtn;
    @FXML private JFXButton edtSuperficieBtn;
    @FXML private JFXButton edtTitreMereBtn;
    @FXML private JFXButton edtNameDmdBtn;
    @FXML private JFXButton edtFirstNameBtn;
    @FXML private JFXButton edtAdressBtn;
    @FXML private JFXButton edtParcelleBtn;
    @FXML private JFXButton edtLotBtn;
    @FXML private JFXButton edtNumBtn;
    @FXML private JFXButton edtDateFormBtn;
    @FXML private JFXButton edtTypeDmdBtn;
    @FXML private JFXButton createTitleBtn;
    // LABEL
    @FXML private Label selectedAffaire;
    @FXML private Label numeroAffaire;
    @FXML private Label dateFromulation;
    @FXML private Label typeDemande;
    @FXML private Label statusLabel;
    @FXML private Label nom;
    @FXML private Label prenom;
    @FXML private Label adresse;
    @FXML private Label parcelleDemandeur;
    @FXML private Label lot;
    @FXML private Label sise;
    @FXML private Label parcelleTerrain;
    @FXML private Label commune;
    @FXML private Label district;
    @FXML private Label region;
    @FXML private Label superficie;
    @FXML private Label nomPropriete;

    @Override public void initialize(URL location, ResourceBundle resources){
        detailsController = this;
        createTitleBtn.setOnAction(event ->{
            TableView<ConnexAffairForView> connexeTableView = ConnexViewController.getInstance().getConnexeTableView();
            ObservableList<ConnexAffairForView> items = connexeTableView.getItems();
            Terrain terrain = affaire.getTerrain();
            MainService.getInstance().launch(new Task<Void>() {
                @Override
                protected void scheduled() {
                    createTitleBtn.disableProperty().bind(this.runningProperty());
                }
                @Override protected void succeeded() {

                }

                @Override protected Void call() throws Exception {

                    boolean terrainNonTitre = DaoFactory.getTerrainDao().checkTerrainAndTitre(terrain);
                      if (terrainNonTitre){
                          if (items!=null){
                              if (!items.isEmpty()){

                                  long count = items.stream().filter(connexAffairForView -> connexAffairForView.getStatus().equals(AffaireStatus.REJECTED)).count();
                                  if (count==items.size()){
                                      Platform.runLater(() -> {
                                          AdminSecurity.show(Origin.CREATE_TITLE);
                                      });
                                  }else {
                                      Platform.runLater(() -> {
                                          Alert alert = new Alert(Alert.AlertType.INFORMATION,"Impossible de créer un titre foncier sur cette affaire ! Veuillez réjeter les affaires connexes avec ce dernier !");
                                          alert.show();
                                      });
                                  }
                              }else {
                                  Platform.runLater(() -> {
                                      AdminSecurity.show(Origin.CREATE_TITLE);
                                  });

                              }
                          }
                      }else {
                          Platform.runLater(() -> {
                              Alert alert = new Alert(Alert.AlertType.ERROR," Le terrain est déja titré ");
                              alert.show();
                          });
                      }
                    return null;
                }
            });
        });
    }

    public void creerTitre(){

        TitleFrom.setExAffaire(affaire);
        Terrain terrain = affaire.getTerrain();
        if (terrain !=null){
            Titre titre = terrain.getTitreDependant();
            if (titre != null) {
                TitleFrom.getInstance().getNumImmat().setText(titre.getNumero());
                TitleFrom.getInstance().getMorcelement().setDisable(false);
            } else {
                TitleFrom.getInstance().getNumImmat().setDisable(true);
                TitleFrom.getInstance().getMorcelement().setDisable(true);
            }
        }
        TitleFrom.getInstance().getNomTitulaire().setText(affaire.getDemandeur().getNom());
        TitleFrom.getInstance().getPrenomTitulaire().setText(affaire.getDemandeur().getPrenom());
        TitleFrom.getInstance().show(TitleOperation.CREATE_OPERATION);
    }

    private void editNomDemandeur(TextInputDialog dialog, Affaire affaire){
        String name = this.getNom().getText();
        ImageView imageView = new ImageView(new Image("/img/male_user_80px.png"));
        dialog.setHeaderText("Entrer le nom du demandeur");
        dialog.getEditor().setText(name);
        dialog.setGraphic(imageView);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().equals(name)) {
            // mettre a jour le nom du demandeur dans détails
            this.nom.setText(result.get());
            String query = "UPDATE demandeur SET nomDmd = '" + result.get() + "' WHERE idDmd = " + affaire.getDemandeur().getId() + ";";
            executeQuery(query);
        }
    }

    private void editPrenomDemandeur(TextInputDialog dialog, Affaire affaire){
        String firstName = this.getPrenom().getText();
        ImageView imageView = new ImageView(new Image("/img/male_user_80px.png"));
        dialog.setHeaderText("Entrer le prenom du demandeur");
        dialog.getEditor().setText(firstName);
        dialog.setGraphic(imageView);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.equals(firstName)) {
            this.getPrenom().setText(result.get());
            String query = " UPDATE demandeur SET prenomDmd = '" + result.get() + "' WHERE idDmd = " + affaire.getDemandeur().getId() + " ; ";
            executeQuery(query);
        }
    }

    private void editAdresseDemandeur(TextInputDialog dialog, Affaire affaire) {
        String adress = this.getAdresse().getText();
        ImageView imageView = new ImageView(new Image("/img/male_user_80px.png"));
        dialog.setHeaderText("Entrer l'adresse du demandeur");
        dialog.getEditor().setText(adress);
        dialog.setGraphic(imageView);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.equals(adress)) {
            this.getAdresse().setText(result.get());
            String query = " UPDATE demandeur SET adresseDmd = '" + result.get() + "' WHERE idDmd = " + affaire.getDemandeur().getId() + " ; ";
            executeQuery(query);
        }
    }

    private void editParcelleDemandeur(TextInputDialog dialog, Affaire affaire) {
        String parcelle = this.getParcelleDemandeur().getText();
        ImageView imageView = new ImageView(new Image("/img/male_user_80px.png"));
        dialog.setHeaderText("Entrer le  parcelle du demandeur");
        dialog.getEditor().setText(parcelle);
        dialog.setGraphic(imageView);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.equals(parcelle)) {
            this.getParcelleDemandeur().setText(result.get());
            String query = " UPDATE demandeur SET parcelleDmd = '" + result.get() + "' WHERE idDmd = " + affaire.getDemandeur().getId() + " ; ";
            executeQuery(query);
        }
    }

    private void editLotDemandeur(TextInputDialog dialog, Affaire affaire){

        String lot = this.getLot().getText();
        ImageView imageView = new ImageView(new Image("/img/male_user_80px.png"));
        dialog.setHeaderText("Entrer le lot du demandeur");
        dialog.getEditor().setText(lot);
        dialog.setGraphic(imageView);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.equals(lot)) {
            this.getLot().setText(result.get());
            String query = " UPDATE demandeur SET lotDmd = '" + result.get() + "' WHERE idDmd = " + affaire.getDemandeur().getId() + " ; ";
            executeQuery(query);
        }
    }

    @FXML public void actionOnAffaire(ActionEvent actionEvent) {
        if (!EditorSecurity.getRemember()){
            AffaireForView selectedItem = AffairViewController.getInstance().getTableView().getSelectionModel().getSelectedItem();
            User redacteur = selectedItem.getRedacteur();
            if (redacteur!=null){
                EditorSecurity.show(Origin.ALL_AFF_DETAILS_VIEW_BTN,actionEvent);
            }else {
                AdminSecurity.show(Origin.ALL_AFF_DETAILS_VIEW_BTN,actionEvent);
            }
        }else launchAction(actionEvent);
    }

    public void launchAction(ActionEvent actionEvent) {
        // Si l'operation de modification vient du liste des affaires
        //    alors on prend l'affaire selectionné
        // Mais si elle vient de la liste des titres alors ont on recupere l'affaire
        Affaire affaire = AffairViewController.getInstance().getTableView().getSelectionModel().getSelectedItem();
        if (affaire != null) {
            editOperation(actionEvent, affaire);
        } else {
            if (AffairDetailsController.affaire != null)
                editOperation(actionEvent, AffairDetailsController.affaire);
        }
    }

    private void editNumeroAffaire(TextInputDialog dialog, Affaire affaire) {
        String numAffaire = this.getNumeroAffaire().getText();
        ImageView imageView = new ImageView(new Image("/img/folder_50px.png"));
        dialog.setHeaderText("Entrer le numero d'affaire");
        dialog.getEditor().setText(numAffaire);
        dialog.setGraphic(imageView);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.equals(numAffaire)) {
            this.getNumeroAffaire().setText(result.get());
            selectedAffaire.setText(result.get());
            String query = " UPDATE affaire SET numAffaire = '" + result.get() + "' WHERE idAffaire = " + affaire.getId() + " ; ";
            executeQuery(query);
        }

    }

    private void editDateFormulation(Affaire affaire) {

        Dialog<LocalDate> dialog = new Dialog();
        dialog.setTitle("Editer date de formulation");
        dialog.setResizable(false);
        JFXDatePicker datePicker = new JFXDatePicker();
        datePicker.setPrefHeight(100);
        datePicker.setPrefWidth(200);
        datePicker.setValue(affaire.getDateDeFormulation().toLocalDateTime().toLocalDate());
        dialog.getDialogPane().setContent(datePicker);
        dialog.getDialogPane().getButtonTypes().add(new ButtonType("valider", ButtonBar.ButtonData.OK_DONE));
        dialog.getDialogPane().getButtonTypes().add(new ButtonType("annuler", ButtonBar.ButtonData.CANCEL_CLOSE));
        dialog.setResultConverter(new Callback<ButtonType, LocalDate>() {
            @Override
            public LocalDate call(ButtonType param) {
                return (param.getButtonData() == ButtonBar.ButtonData.OK_DONE) ? datePicker.getValue() : null;
            }
        });
        Optional<LocalDate> result = dialog.showAndWait();
        if (result.isPresent()) {
            this.getDateFromulation().setText(result.get().toString() + " " + LocalTime.now().toString());
            String query = " UPDATE affaire SET dateFormulation = '" + result.get() + "' WHERE idAffaire = " + affaire.getId() + " ; ";
            executeQuery(query);
        }
    }

    private void editTypeDemande(Affaire affaire) {

        String type[] = {"ACQUISITION", "PRESCRIPTION_ACQUISITIVE"};
        ChoiceDialog<String> dialog = new ChoiceDialog<>(type[0], type);
        Optional<String> choice = dialog.showAndWait();
        if (choice.isPresent()) {
            this.getTypeDemande().setText(choice.get());
            String query = " UPDATE affaire SET typeAffaire = '" + choice.get() + "' WHERE idAffaire = " + affaire.getId() + ";";
            executeQuery(query);
        }
    }

    private void editStatus(Affaire affaire) {
        String type[] = {"En cours", "Suspendu", "Terminé", "Réjété"};
        ChoiceDialog<String> dialog = new ChoiceDialog<>(type[0], type);
        Optional<String> choice = dialog.showAndWait();
        AffaireStatus value = AffaireStatus.RUNNING;
        if (choice.isPresent()) {
            String choix = choice.get();
            if (choix.equals("En cours")) {
                value = AffaireStatus.RUNNING;
                statusLabel.setGraphic(new ImageView(AffairViewController.getRunningImg()));
                statusLabel.setText(" En cours ");
            } else if (choix.equals("Suspendu")) {
                statusLabel.setGraphic(new ImageView(AffairViewController.getSuspendImg()));
                statusLabel.setText(" Suspendu ");
                value = AffaireStatus.SUSPEND;
            } else if (choix.equals("Terminé")) {
                statusLabel.setGraphic(new ImageView(AffairViewController.getFinishedImg()));
                statusLabel.setText("Terminé");
                value = AffaireStatus.SUCCEED;
            } else if (choix.equals("Réjété")) {
                statusLabel.setGraphic(new ImageView(AffairViewController.getRejectImg()));
                statusLabel.setText("Réjété");
                value = AffaireStatus.REJECTED;
            }
            String query = " UPDATE affaire SET situation = '" + Affaire.affaireStatus2String(value) + "' WHERE idAffaire = " + affaire.getId() + " ; ";
            executeQuery(query);
        }
    }

    private void editSuperficie(Affaire affaire){

        if (superficieDialog == null){
            superficieDialog = new Dialog();
            superficieDialog.setTitle("Editer superficie du terrain");
            HBox box = new HBox(10);
            // hectare
            JFXTextField hectareInput = new JFXTextField();
            hectareInput.setPromptText("Hectare");
            hectareInput.setLabelFloat(true);
            // Are
            JFXTextField areInput = new JFXTextField();
            areInput.setPromptText("Are");
            areInput.setLabelFloat(true);
            // Centiare
            JFXTextField centiAreInput = new JFXTextField();
            centiAreInput.setPromptText("Centiare");
            centiAreInput.setLabelFloat(true);
            box.getChildren().addAll(hectareInput, areInput, centiAreInput);
            // extraction de la superfice
            String value[] = affaire.getTerrain().getSuperficie().toString().split("-");
            if (value.length == 3) {
                String sup1[] = value[0].split(" ");
                String sup2[] = value[1].split(" ");
                String sup3[] = value[2].split(" ");
                if (sup1.length == 2 && sup2.length == 2 && sup3.length == 2) {
                    hectareInput.setText(sup1[0]);
                    areInput.setText(sup2[0]);
                    centiAreInput.setText(sup3[0]);
                }
            } else if (value.length == 2) {
                String sup1[] = value[0].split(" ");
                String sup2[] = value[1].split(" ");
                if (sup1[1].equals("Ha")) {
                    hectareInput.setText(sup1[0]);
                    if (sup2[1].equals("A"))
                        areInput.setText(sup2[0]);
                    else centiAreInput.setText(sup2[0]);
                } else if (sup1[1].equals("A")) {
                    areInput.setText(sup1[0]);
                    centiAreInput.setText(sup2[0]);
                }
            } else {
                String[] sup = affaire.getTerrain().getSuperficie().toString().split(" ");
                if (sup[1].equals("Ha"))
                    hectareInput.setText(sup[0]);
                else if (sup[1].equals("A"))
                    areInput.setText(sup[0]);
                else centiAreInput.setText(sup[0]);
            }

            superficieDialog.getDialogPane().setContent(box);
            superficieDialog.getDialogPane().getButtonTypes().add(new ButtonType("valider", ButtonBar.ButtonData.OK_DONE));
            superficieDialog.getDialogPane().getButtonTypes().add(new ButtonType("annuler", ButtonBar.ButtonData.CANCEL_CLOSE));
            superficieDialog.setResultConverter(new Callback<ButtonType, String>() {
                @Override
                public String call(ButtonType param){
                    return AffaireForm.getSuperficie(hectareInput, areInput, centiAreInput);
                }
            });
        }
        Optional<String> result = superficieDialog.showAndWait();
        if (result.isPresent() && !result.get().isEmpty()) {
            String newSuperficie = result.get();
            superficie.setText(newSuperficie);
            String query = " UPDATE terrain SET  superficie ='"+newSuperficie+"' WHERE idTerrain =" + affaire.getTerrain().getIdTerrain()+ ";";
            executeQuery(query);
        }

    }

    private void editSise(TextInputDialog dialog, Affaire affaire) {
        String numAffaire = this.getSise().getText();
        dialog.setHeaderText("Entrer l'adresse du terrain ");
        dialog.getEditor().setText(numAffaire);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.equals(numAffaire) && !result.get().isEmpty()) {
            this.getSise().setText(result.get());
            String query = " UPDATE terrain SET quartier = '" + result.get() + "' WHERE idTerrain = " + affaire.getTerrain().getIdTerrain() + " ; ";
            executeQuery(query);
        }
    }

    private void editParcelleTerrain(TextInputDialog dialog, Affaire affaire){
        String numAffaire = this.getParcelleTerrain().getText();
        dialog.setHeaderText("Entrer le parcelle du terrain");
        dialog.getEditor().setText(numAffaire);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.equals(numAffaire)) {
            this.getParcelleTerrain().setText(result.get());
            String query = " UPDATE terrain SET parcelle = '" + result.get() + "' WHERE idTerrain = " + affaire.getTerrain().getIdTerrain() + " ; ";
            executeQuery(query);
        }

    }

    private void editDistrict(TextInputDialog dialog, Affaire affaire) {
        String district = this.getDistrict().getText();
        dialog.setHeaderText("Entrer le district du terrain");
        dialog.getEditor().setText(district);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.equals(district)) {
            this.getDistrict().setText(result.get());
            String query = " UPDATE terrain SET district = '" + result.get() + "' WHERE idTerrain = " + affaire.getTerrain().getIdTerrain() + " ; ";
            executeQuery(query);
        }
    }

    private void editOperation(ActionEvent actionEvent, Affaire affaire) {
        TextInputDialog dialog = new TextInputDialog();
        if (actionEvent.getSource() == edtNameDmdBtn) { editNomDemandeur(dialog, affaire); }
        else if (actionEvent.getSource() == edtFirstNameBtn) editPrenomDemandeur(dialog, affaire);
        else if (actionEvent.getSource() == edtAdressBtn) editAdresseDemandeur(dialog, affaire);
        else if (actionEvent.getSource() == edtParcelleBtn) editParcelleDemandeur(dialog, affaire);
        else if (actionEvent.getSource() == edtLotBtn) editLotDemandeur(dialog, affaire);
        else if (actionEvent.getSource() == edtNumBtn) editNumeroAffaire(dialog, affaire);
        else if (actionEvent.getSource() == edtDateFormBtn) editDateFormulation(affaire);
        else if (actionEvent.getSource() == edtTypeDmdBtn) editTypeDemande(affaire);
        else if (actionEvent.getSource() == editStatusBtn) editStatus(affaire);
        else if (actionEvent.getSource() == edtSiseBtn) editSise(dialog, affaire);
        else if (actionEvent.getSource() == edtParcelleTerrainBtn) editParcelleTerrain(dialog, affaire);
        else if (actionEvent.getSource() == edtCommuneBtn) editCommune(dialog, affaire);
        else if (actionEvent.getSource() == edtDistrictBtn) editDistrict(dialog, affaire);
        else if (actionEvent.getSource() == edtRegionBtn) editRegion(dialog, affaire);
        else if (actionEvent.getSource() == edtSuperficieBtn) editSuperficie(affaire);
        else if (actionEvent.getSource() == edtTitreMereBtn) {
            TitreMereEditDialog.getInstance().show();
        }
    }

    private void editCommune(TextInputDialog dialog, Affaire affaire) {
        String commune = this.getCommune().getText();
        dialog.setHeaderText("Entrer le commune ");
        dialog.getEditor().setText(commune);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.equals(commune)) {
            this.getCommune().setText(result.get());
            String query = " UPDATE terrain SET commune = '" + result.get() + "' WHERE idTerrain = " + affaire.getId() + " ; ";
            executeQuery(query);
        }
    }

    private void editRegion(TextInputDialog dialog, Affaire affaire) {
        String region = this.getRegion().getText();
        dialog.setHeaderText("Entrer la region ");
        dialog.getEditor().setText(region);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.equals(region)) {
            this.getRegion().setText(result.get());
            String query = " UPDATE terrain SET region = '" + result.get() + "' WHERE idTerrain = " + affaire.getId() + " ; ";
            executeQuery(query);
        }
    }
    @FXML void backToAffaireView(ActionEvent event) {
        EditorSecurity.setRemember(false);
        AnchorPane affaireDetailsView = MainController.getInstance().getAffaireDetailsView();
        new SlideInLeft(affaireDetailsView).play();
        affaireDetailsView.toBack();
    }

    public Label getNumeroAffaire() {
        return numeroAffaire;
    }
    public Label getDateFromulation() {
        return dateFromulation;
    }
    public Label getTypeDemande() {
        return typeDemande;
    }
    public Label getNom() {
        return nom;
    }
    public Label getPrenom() {
        return prenom;
    }
    public Label getAdresse() {
        return adresse;
    }
    public Label getParcelleDemandeur() {
        return parcelleDemandeur;
    }
    public Label getLot() {
        return lot;
    }
    public Label getSise() {
        return sise;
    }
    public Label getParcelleTerrain() {
        return parcelleTerrain;
    }
    public Label getCommune() {
        return commune;
    }
    public Label getDistrict() {
        return district;
    }
    public Label getRegion() {
        return region;
    }
    public Label getSuperficie() {
        return superficie;
    }
    public Label getNomPropriete() {
        return nomPropriete;
    }
    public Label getSelectedAffaire() {
        return selectedAffaire;
    }
    public Tab getConnexeTab() {
        return connexeTab;
    }
    public Tab getRedacteurTab() {
        return redacteurTab;
    }
    public Tab getProcedureTab() {
        return procedureTab;
    }
    public Label getStatusLabel() {
        return statusLabel;
    }
    public static Affaire getAffaire() {
        return affaire;
    }
    public static void setAffaire(Affaire affaire) {
        AffairDetailsController.affaire = affaire;
    }
    public static AffairDetailsController getInstance() {
        return detailsController;
    }
}
