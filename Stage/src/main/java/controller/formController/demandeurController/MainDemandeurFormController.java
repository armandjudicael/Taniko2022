package controller.formController.demandeurController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTabPane;
import controller.formController.otherFormController.MainAffaireFormController;
import dao.DbUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.Enum.TypeDemandeur;
import model.other.MainService;
import model.pojo.business.other.Personne;
import model.pojo.business.other.PersonneMorale;
import model.pojo.business.other.PersonnePhysique;
import view.Helper.Other.ControllerUtils;
import view.Helper.Other.FormNavigationButton;
import view.Helper.Other.SnackBarNotification;
import view.Model.ViewObject.ApplicantForView;
import java.net.URL;
import java.util.ResourceBundle;

public class MainDemandeurFormController implements Initializable,ControllerUtils{
    @Override public void initialize(URL location,ResourceBundle resources){
        mainDemandeurFormController = this;
        initButton();
        initApplicantList();
        initBinding();
    }
    @Override public void initButton(){
        initNavigationButton();
        addApplicantBtn.setOnAction(event -> {
          //  verifyNameAndNumtelAndEmail();
            addApplicantToJFXListView();
        });
        clearForm.setOnAction(event -> {
            NouveauDemandeurController.setPersonneMorale(null);
            NouveauDemandeurController.setPersonnePhysique(null);
            NouveauDemandeurController.getInstance().resetDemandeurForm();
        });
    }
    private void initNavigationButton() {
        MainAffaireFormController instance = MainAffaireFormController.getInstance();
        // dmdNext
        Label demandeurLabel = instance.getDemandeurLabel();
        AnchorPane terrainFormPane = instance.getTerrainFormPane();
        Label terrainLabel = instance.getTerrainLabel();
       // new FormNavigationButton(dmdNextBtn,terrainLabel,demandeurLabel,instance.getTerrainFormPane());
        dmdNextBtn.setOnAction(event -> FormNavigationButton.executeUpdate(instance.getTerrainFormPane(),terrainLabel,demandeurLabel));
        // dmdPrev
        AnchorPane folderFormPane = instance.getFolderFormPane();
        Label FolderLabel = instance.getFolderLabel();
        new FormNavigationButton(dmdPrevBtn,FolderLabel,demandeurLabel,folderFormPane);
    }

    private void verifyNameAndNumtelAndEmail(){
        MainService.getInstance().launch(new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                String nom = "";
                String numTel = "";
                String email = "";
                NouveauDemandeurController newDemandeurFormInstance = getNewDemandeurFormInstance();
                if (MainAffaireFormController.getTypeDemandeur().equals(TypeDemandeur.PERSONNE_PHYSIQUE)) {
                    email = newDemandeurFormInstance.getEmail().getText();
                    numTel = newDemandeurFormInstance.getTelephone().getText();
                    nom = newDemandeurFormInstance.getNomDmd().getText();
                } else {
                    nom = newDemandeurFormInstance.getRaisonSocial().getText();
                    numTel = newDemandeurFormInstance.getTelPersonneMorale().getText();
                    email = newDemandeurFormInstance.getEmailPersonneMorale().getText();
                }
                if (!nom.isEmpty() && !numTel.isEmpty() && !email.isEmpty()) {
                    String s = DbUtils.checkDemandeurInfoForm(nom, numTel, email);
                    if (isOk(s)) Platform.runLater(() -> addApplicantToJFXListView());
                    else Platform.runLater(() -> SnackBarNotification.getInstance().showOn(MainAffaireFormController.getInstance().getMainAffBorderPane(),
                                " Un element que vous avez saisie existe déja ", Alert.AlertType.ERROR));
                } else Platform.runLater(() -> SnackBarNotification.getInstance().showOn(MainAffaireFormController.getInstance().getMainAffBorderPane(), " Veuillez remplir les champs ", Alert.AlertType.ERROR));
                return null;
            }
        });
    }

    @Override public boolean isOk(String result) {
        String[] split = result.split(";");
        if (split.length == 3) {
            NouveauDemandeurController instance = NouveauDemandeurController.getInstance();
            int nomStatus = Integer.valueOf(split[0]);
            int numTelStatus = Integer.valueOf(split[1]);
            int emailStatus = Integer.valueOf(split[2]);
            if (MainAffaireFormController.getTypeDemandeur().equals(TypeDemandeur.PERSONNE_PHYSIQUE))
                showPhysiqueError(instance, nomStatus, numTelStatus, emailStatus);
            else showMoralError(instance, nomStatus, numTelStatus, emailStatus);
            Boolean value = (emailStatus == 0 && numTelStatus == 0 && emailStatus == 0);
            return value;
        }
        return false;
    }

    @Override public void initBinding() {

    }

    private void showPhysiqueError(NouveauDemandeurController instance, int nomStatus, int numTelStatus, int emailStatus) {
        if (nomStatus != 0) showError(instance.getNomDmd(), instance.getPhysiqueNameError());
        if (numTelStatus != 0) showError(instance.getTelephone(), instance.getTelMoraleError());
        if (emailStatus != 0) showError(instance.getEmail(), instance.getEmailPhysiqueError());
    }

    private void showMoralError(NouveauDemandeurController instance, int nomStatus, int numTelStatus, int emailStatus) {
        if (nomStatus != 0) showError(instance.getRaisonSocial(), instance.getNomMoraleError());
        if (numTelStatus != 0) showError(instance.getTelephone(), instance.getTelMoraleError());
        if (emailStatus != 0) showError(instance.getEmail(), instance.getEmailMoraleError());
    }

    private void showError(TextInputControl inputControl, Label label) {
        label.setVisible(true);
        inputControl.getStyleClass().add("boxError");
    }
    public Tab getSearchTab() {
        return searchTab;
    }
    public Tab getNewTab() {
        return newTab;
    }
    public static MainDemandeurFormController getInstance() {
        return mainDemandeurFormController;
    }

    public static NouveauDemandeurController getNewDemandeurFormInstance() {
        if (newDemandeurFormInstance == null) newDemandeurFormInstance = NouveauDemandeurController.getInstance();
        return newDemandeurFormInstance;
    }

    private void initApplicantList() {
        applicantListView.setExpanded(true);
        applicantListView.setVerticalGap(5.0);
    }

    private void addApplicantToJFXListView(){
        if (searchTab.isSelected()) {
            Personne selectedValue = RechercherDemandeurController.getInstance().getDemandeurCombobox().getValue();
            applicantListView.getItems().add(new ApplicantForView(selectedValue));
        } else {
            NouveauDemandeurController instance = NouveauDemandeurController.getInstance();
            if (NouveauDemandeurController.isWithRepresentant()){
                setRepresentantAndAddItem(instance);
            } else {
                if (MainAffaireFormController.getTypeDemandeur().equals(TypeDemandeur.PERSONNE_PHYSIQUE))
                    addPersonnePhysique(instance);
                else addPersonneMorale(instance);
            }
        }
    }

    private void setRepresentantAndAddItem(NouveauDemandeurController instance) {
        PersonneMorale pm = NouveauDemandeurController.getPersonneMorale();
        if (pm != null) {
            PersonnePhysique representant = instance.createDemandeurPhysique(false);
            pm.setRepresentant(representant);
            // Make morale pane to front
            NouveauDemandeurController.getInstance().getMoralPane().toFront();
            // add the item to listview
            applicantListView.getItems().add(new ApplicantForView(pm));
            String message = representant.getNom() + " est le representant de " + pm.getNom();
            SnackBarNotification.getInstance().showOn(message, Alert.AlertType.CONFIRMATION);
        } else SnackBarNotification.getInstance().showOn("Veuillez remplir le formulaire  ", Alert.AlertType.ERROR);
    }

    private void addPersonneMorale(NouveauDemandeurController instance) {
        ObservableList<ApplicantForView> items = MainDemandeurFormController.getInstance().applicantListView.getItems();
        if (items.isEmpty()) {
            if (!instance.getRaisonSocial().getText().isBlank()){
                PersonneMorale demandeurMorale = instance.createDemandeurMorale();
                applicantListView.getItems().add(new ApplicantForView(demandeurMorale));
            } else SnackBarNotification.getInstance().showOn(MainAffaireFormController.getInstance().getMainAffBorderPane(), " Veuillez remplir les champs ");
        } else SnackBarNotification.getInstance().showOn("Le nombre de demandeur morale est limité à 1", Alert.AlertType.ERROR);
    }

    private void addPersonnePhysique(NouveauDemandeurController instance){
        if (!instance.getNomDmd().getText().isBlank()) {
            PersonnePhysique dph = null;
            if (NouveauDemandeurController.isWithConjoint()) {
                dph = NouveauDemandeurController.getPersonnePhysique();
                // recuperer le conjoint
                PersonnePhysique conjoint = instance.createDemandeurPhysique(true);
                if (dph != null) {
                    dph.setConjoint(conjoint);
                    SnackBarNotification.getInstance().showOn(" Conjoint enregistrer ", Alert.AlertType.CONFIRMATION);
                }
            } else dph = instance.createDemandeurPhysique(false);
            // add to listview
            if (dph != null) applicantListView.getItems().add(new ApplicantForView(dph));
            else SnackBarNotification.getInstance().showOn(" Veuillez ajouter un demandeur s'il vous plait", Alert.AlertType.ERROR);
        } else SnackBarNotification.getInstance().showOn(" Veuillez remplir les champs ", Alert.AlertType.ERROR);
    }

    public void mainTabPaneSelect(boolean New){
        SingleSelectionModel<Tab> selectionModel = applicantTabPane.getSelectionModel();
        if (New) selectionModel.select(newTab);
        else selectionModel.select(searchTab);
    }

    @FXML private JFXButton dmdPrevBtn;
    @FXML private JFXButton dmdNextBtn;
    @FXML private Tab searchTab;
    @FXML private Tab newTab;
    public JFXButton getAddApplicantBtn() {
        return addApplicantBtn;
    }
    @FXML private JFXButton addApplicantBtn;
    @FXML private JFXButton clearForm;
    public SplitPane getDmdSplitPane() {
        return dmdSplitPane;
    }
    @FXML private SplitPane dmdSplitPane;
    @FXML private JFXTabPane applicantTabPane;
    private static NouveauDemandeurController newDemandeurFormInstance;
    public JFXListView<ApplicantForView> getApplicantListView() {
        return applicantListView;
    }
    @FXML private JFXListView<ApplicantForView> applicantListView;
    private static MainDemandeurFormController mainDemandeurFormController;
}
