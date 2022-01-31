package controller.formController.otherFormController;

import Main.InitializeApp;
import com.jfoenix.controls.JFXListView;
import dao.DbUtils;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.Enum.*;
import model.other.EmailFactory;
import model.other.SmsUtils;
import model.pojo.business.attachement.Attachement;
import model.pojo.business.other.*;
import model.pojo.utils.Mariage;
import model.pojo.utils.UtilsData;
import view.Helper.Other.ControllerUtils;
import view.Helper.Other.FormNavigationButton;
import view.Helper.Other.ProgressTaskBinder;
import view.Model.ViewObject.*;
import controller.viewController.FolderViewController;
import dao.DaoFactory;
import model.other.MainService;
import view.Dialog.Other.Notification;
import view.Helper.Attachement.AttachementCheckerButton;
import view.Helper.Attachement.AttachementCreatorButton;
import view.Helper.Attachement.AttachementRemoverButton;
import com.jfoenix.controls.JFXButton;
import controller.formController.demandeurController.MainDemandeurFormController;
import controller.formController.demandeurController.NouveauDemandeurController;
import controller.viewController.LoginController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.TilePane;

import java.net.URL;
import java.time.LocalDate;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static controller.formController.otherFormController.MainAffaireFormController.*;
import static dao.PersonneMoraleDao.*;

public class PieceJointeFormController implements Initializable, ControllerUtils {

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        pieceJointeFormController = this;
        initButton();
        initBinding();
    }

    @Override public void initBinding() {
        ObservableList<Node> children = pjTilepane.getChildren();
        delPieceBtn.disableProperty().bind(Bindings.isEmpty(children));
        checkBtn.disableProperty().bind(Bindings.isEmpty(children));
    }

    @Override public void initButton() {
        initNavigationButton();
        ObservableList<Node> children = pjTilepane.getChildren();
        new AttachementCreatorButton(addAttachInBtn, children, false);
        new AttachementCreatorButton(addAttachOutBtn, children, false);
        new AttachementRemoverButton(delPieceBtn, children, false);
        new AttachementCheckerButton(checkBtn, children);
    }

    private void initNavigationButton() {
        MainAffaireFormController instance = MainAffaireFormController.getInstance();
        // NEXT
        Label validationLabel = instance.getValidationLabel();
        Label pieceJointeLabel = instance.getPieceJointeLabel();
        pjNextBtn.setOnAction(event -> initTextArea());
        // PREV
        Label terrainLabel = instance.getTerrainLabel();
        AnchorPane terrainFormPane = instance.getTerrainFormPane();
       // new FormNavigationButton(pjPrevBtn,terrainLabel,pieceJointeLabel,terrainFormPane);
        pjPrevBtn.setOnAction(event -> FormNavigationButton.executeUpdate(instance.getTerrainFormPane(),terrainLabel,pieceJointeLabel));
    }
    // APPEND
    private void appendAffaire(StringBuffer buffer) {
        DossierFormController instance = getAffFormInstance();
        buffer.append(SPACE);
        buffer.append(TAB1 + " ############################# AFFAIRE ##############################" + SPACE);
        buffer.append(TAB + " Numero affaire : " + instance.getNumeroAffaire().getText() + instance.getNumAndDateAffLabel().getText() + SPACE);
        buffer.append(TAB + " Objet du demande : " + TypeDemande.typeDemande2String(MainAffaireFormController.getTypeDemande()) + SPACE);
        String numReperage = instance.getNumeroReperage().getText();
        if (numReperage != null && !numReperage.isBlank()) {
            String numAndDateRep = numReperage + instance.getDateLabelReperage().getText();
            buffer.append(TAB + " Numero répérage : " + numAndDateRep + SPACE);
        }
        String numJtr = instance.getNumeroJtr().getText();
        if (numJtr != null && !numJtr.isBlank()) {
            String numAndDateJtr = numJtr + instance.getDateLabelJtr().getText();
            buffer.append(TAB + " Numero JTR : " + numAndDateJtr + SPACE);
        }
        String numOrd = instance.getNumOrdonance().getText();
        if (numOrd != null && !numOrd.isBlank()) {
            String numAndDateOrd = numOrd + instance.getDateLabelOrdonance().getText();
            buffer.append(TAB + " Numero Ordonnance : " + numAndDateOrd + SPACE);
        }
        if (instance.getSansEmpietementRadio().isSelected()) {
            buffer.append(TAB + " Observation :  SANS EMPIETEMENT " + SPACE);
        } else buffer.append(TAB + " Obseration : CONNEXE" + SPACE);
    }
    private void appendDemandeur(StringBuffer buffer){
        JFXListView<ApplicantForView> listView = MainDemandeurFormController.getInstance().getApplicantListView();
        ObservableList<ApplicantForView> listViewItems = listView.getItems();
        if (!listViewItems.isEmpty()) {
            appendAllDemandeurOn(buffer,listViewItems);
        }
    }
    private void appendAllDemandeurOn(StringBuffer buffer, ObservableList<ApplicantForView> listViewItems){
        buffer.append(TAB1 + "############################# DEMANDEUR ##############################" + SPACE);
        AtomicInteger i = new AtomicInteger(1);
        listViewItems.forEach(applicantForView -> {
            Personne personne = applicantForView.getPersonne();
            if (personne.getType() == TypeDemandeur.PERSONNE_PHYSIQUE) {
                PersonnePhysique ph = (PersonnePhysique) personne;
                if (listViewItems.size() > 1)
                    buffer.append(TAB1 + TAB1 + " >>> DEMANDEUR  " + i.getAndIncrement() + SPACE);
                appendPersonnePhysique(ph, buffer);
            } else {
                PersonneMorale pm = (PersonneMorale) personne;
                appendPersonneMorale(pm, buffer);
            }
        });
    }
    private void appendPersonneMorale(PersonneMorale dm, StringBuffer buffer){
        if (!dm.getNom().isBlank()) buffer.append(TAB + " Nom : " + dm.getNom() + SPACE);
        if (!dm.getAdresse().isBlank()) buffer.append(TAB + " Siege social : " + dm.getAdresse() + SPACE);
        if (!dm.getNationalite().isBlank()) buffer.append(TAB + " Nationnalité : " + dm.getNationalite() + SPACE);
        if (!dm.getNumTel().isBlank()) buffer.append(TAB + " Telephone : " + dm.getNumTel() + SPACE);
        if (!dm.getEmail().isBlank()) buffer.append(TAB + " Email : " + dm.getEmail() + SPACE);
        PersonnePhysique representant = dm.getRepresentant();
        if (representant != null) {
            buffer.append(TAB1 + TAB1 + "### REPRESENTANT ###" + SPACE);
            appendPersonnePhysique(representant, buffer);
            appendProcuration(buffer);
        }
    }
    private void appendPersonnePhysique(PersonnePhysique ph,StringBuffer buffer){
        buffer.append(TAB + " Nom complet :" + ph.getNom() + SPACE);
        if (ph.getLieuDeNaissance() != null)
            buffer.append(TAB + " Date et lieu de naissance  :" + ph.getDateDeNaissance() + " à " + ph.getLieuDeNaissance() + SPACE);
        buffer.append(TAB + " Sexe :" + ph.getSexe() + SPACE);
        // FILIATION
        appendFiliation(buffer, ph);
        // ADRESSE
        appendAdresse(buffer, ph);
        // PROFESSION
        if (!ph.getProfession().isBlank()) buffer.append(TAB + " Profession : " + ph.getProfession() + SPACE);
        // CIN
        appendCin(ph, buffer);
        // NATIONALITE
        if (!ph.getNationalite().isBlank()) buffer.append(TAB + " Nationalité : " + ph.getNationalite() + SPACE);
        // EMAIL
        if (!ph.getEmail().isBlank()) buffer.append(TAB + " Email : " + ph.getEmail() + SPACE);
        // NUMERO
        if (ph.getNumTel() != null && !ph.getNumTel().isBlank())
            buffer.append(TAB + " Telephone : " + ph.getNumTel() + SPACE);
        // CONJOINT
        appendConjointEtMariage(ph, buffer);
    }
    private void appendCin(PersonnePhysique ph, StringBuffer buffer) {
        if (!ph.getNumCin().isBlank()) {
            String cinInfo = TAB + " Cin :  " + ph.getNumCin()
                    + " délivré le  " + ph.getDateDelivranceCin()
                    + " à  " + ph.getLieuDeNaissance() + SPACE;
            buffer.append(cinInfo);
        }
    }
    private void appendConjointEtMariage(PersonnePhysique ph, StringBuffer buffer){
        if (ph.getSituationMatrimoniale() == SituationMatrimoniale.MARIE) {
            PersonnePhysique conjoint = ph.getConjoint();
            Mariage mariage = ph.getMariage();
            if (conjoint != null && mariage != null) {
                buffer.append(TAB1 + TAB1 + "#### CONJOINT ####" + SPACE);
                appendPersonnePhysique(conjoint, buffer);
                appendMariage(mariage, buffer);
            }
        }
    }
    private void appendMariage(Mariage mariage, StringBuffer buffer) {
        if (mariage.getDateMariage() != null && !mariage.getLieuMariage().isBlank()) {
            buffer.append(TAB + " Date et lieu du mariage : " + mariage.getDateMariage() + " à  " + mariage.getLieuMariage() + SPACE);
            buffer.append(TAB + " Regime matrimoniale : " + mariage.getRegimeMatrimoniale() + SPACE);
        }
    }
    private void appendFiliation(StringBuffer buffer, PersonnePhysique dp){
        if (!dp.getPere().isBlank()) {
            String filsOuFille = dp.getSexe().toLowerCase().equals("masculin") ? " Fils " : " Fille ";
            String filiation = TAB + filsOuFille + " de : " + dp.getPere();
            if (!dp.getMere().isBlank()) filiation += " et de " + dp.getMere() + SPACE;
            buffer.append(filiation);
        }
    }
    private void appendAdresse(StringBuffer buffer, PersonnePhysique dp) {
        if (!dp.getAdresse().isBlank()) {
            String parcelle = !dp.getParcelle().isBlank() ? dp.getParcelle() : "";
            String lot = !dp.getLot().isBlank() ? dp.getLot() : "";
            String fullAdresse = TAB + " Adresse : " + dp.getAdresse() + " "
                    + (!parcelle.isBlank() ? parcelle : "")
                    + " " + lot + SPACE;
            buffer.append(fullAdresse);
        }
    }
    private void appendProcuration(StringBuffer buffer) {
        UtilsData procuration = createProcuration();
        if (!procuration.getNum().isBlank() && procuration.getDate() != null) {
            buffer.append(TAB1 + TAB1 + "### PROCURATION ###" + SPACE);
            buffer.append(TAB + " Numero procuration  : " + procuration.getNum() + SPACE);
            buffer.append(TAB + " Date procuration  : " + procuration.getDate() + SPACE);
        }
    }
    private void appendTerrain(StringBuffer buffer) {
        Propriete propriete = TerrainFromController.getInstance().createTerrain(true);
        buffer.append(TAB1 + " ########################### TERRAIN ########################### " + SPACE);
        if (!propriete.getQuartier().isBlank()) {
            String terrainFullAdresse = propriete.getQuartier();
            if (!propriete.getParcelle().isBlank())
                terrainFullAdresse = terrainFullAdresse + " , Parcelle  " + propriete.getParcelle();
            buffer.append(TAB + " Sise à : " + terrainFullAdresse + SPACE);
        }
        buffer.append(TAB + " Commune  : " + propriete.getCommune() + SPACE);
        buffer.append(TAB + " District  : " + propriete.getDistrict() + SPACE);
        buffer.append(TAB + " Region  : " + propriete.getRegion() + SPACE);
        buffer.append(TAB + " Superficie (Approximative)  : " + propriete.getSuperficie() + SPACE);
        if (propriete.getTitreDependant() != null) {
            buffer.append(TAB + " Dependant de la propriete dite  : " + propriete.getTitreDependant().getNomPropriete() + SPACE);
        }
    }
    // END APPEND
    public void enregistrerAffaire(){
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected void succeeded() {
                resetForm();
            }

            @Override
            protected Void call() throws Exception {
                saveFolder();
                return null;
            }

            @Override
            protected void failed() {
                Throwable exception = this.getException();
                exception.printStackTrace();
            }

            @Override
            protected void scheduled() {
                new ProgressTaskBinder(MainAffaireFormController
                        .getInstance()
                        .getCheckProgressBar(),this);
            }
        });
    }
    private void saveAllAttachement(Dossier dossier){
        ObservableList<Attachement> attachementList = AttachementCreatorButton.getAttachementList();
        if (attachementList != null && !attachementList.isEmpty()) {
            int[] all = DaoFactory.getPieceJointeDao().createAll(attachementList, dossier);
            if (attachementList.size() == all.length) {
                String message = " Piece jointe enregistré avdec succès ";
                Notification.getInstance(message, NotifType.SUCCESS).showNotif();
            } else {
                String message = " Echec de l'enregistrement des pieces jointes ";
                Notification.getInstance(message, NotifType.WARNING).showNotif();
            }
        }
    }
    private FolderForView createAffaireView(){

        DossierFormController instance = getAffFormInstance();
        TypeDemande typeDemande = MainAffaireFormController.getTypeDemande();
        LocalDate localDate = instance.getDateFormulation().getValue();
        dateFormulation = createJavaSqlDateBy(localDate!=null ? localDate : LocalDate.now());
        Personne demandeur = initDemandeur();
        String numeroAffaire = instance.getNumeroAffaire().getText() + instance.getNumAndDateAffLabel().getText();
        User redacteur = initRedateur();
        Propriete propriete = initTerrain();
        return initAffaireForView(typeDemande,demandeur,numeroAffaire, redacteur, propriete);
    }
    private FolderForView initAffaireForView(TypeDemande typeDemande, Personne demandeur, String numeroAffaire, User redacteur, Propriete propriete){
        FolderForView affaireForView = new FolderForView();
        affaireForView.setNumero(numeroAffaire);
        affaireForView.setDateDeFormulation(dateFormulation);
        affaireForView.setTypeDemande(typeDemande);
        affaireForView.setRedacteur(redacteur);
        affaireForView.setStatus(FolderStatus.RUNNING);
        affaireForView.setDemandeur(demandeur);
        affaireForView.setTerrain(propriete);
        affaireForView.setProcedure(new ProcedureForTableview(ProcedureStatus.NONE, new SimpleStringProperty("Auccune procedure")));
        return affaireForView;
    }
    private void saveFolder(){
        FolderForView affairForView = createAffaireView();
        // ENREGISTRER AFFAIRE
        if (DaoFactory.getDossierDao().create(affairForView) != 0){
            affairForView.setId(DaoFactory.getDossierDao().getAffaireIdByNum(affairForView.getNumero()));
            JFXListView<ApplicantForView> applicantListView;
            applicantListView = MainDemandeurFormController.getInstance().getApplicantListView();
            ObservableList<ApplicantForView> items = applicantListView.getItems();
            if (!items.isEmpty()){
                List<Personne> personList = items.stream().map(ApplicantForView::getPersonne).collect(Collectors.toList());
                // INSERTION DANS LA TABLE DES DEMANDEURS
                int[] ints = DbUtils.insertOnTableDemandeur(personList,affairForView);
                if (ints.length == items.size()){
                    // INSERTION DANS LA TABLE TITULAIRE
                    if (DbUtils.insertOnTableTitulaire(affairForView, affairForView.getDemandeur()) == 1){
                        updateViewAndNotifyUser(affairForView.getNumero(),affairForView);
                        saveReperageAndOrdonanceAndDispatchAndJtrAndAttachements(affairForView.getRedacteur(),affairForView);
                    }
                }
            }
        }
    }
    private void updateViewAndNotifyUser(String numeroAffaire, FolderForView affairForView){
        Notification.getInstance(" L'affaire N° " + numeroAffaire + " est enregistrer avec succès ", NotifType.SUCCESS).showNotif();
        // MIS A JOUR DE L'AFFICHAGE
        Platform.runLater(() -> FolderViewController.getInstance().getTableView().getItems().add(affairForView));
        // ENVOYER SMS ET EMAIL AU CHEF ET ADJOINTE
        sendSmsAndEmailToAllAdmin(affairForView);
    }
    private void sendSmsAndEmailToAllAdmin(FolderForView affaireForView){
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<UserForView> users = InitializeApp.getUsers();
                String messageBody = " L'affaire N° " + affaireForView.getNumero() + " est enregistrer avec succès !";
                String emailSubject = "Nouvelle demande";
                if (users != null && !users.isEmpty())
                    sendWithLocalEmailAndNumtel(users, messageBody, emailSubject);
                else sendToWithRemoteEmailAndNumTel(messageBody, emailSubject);
                return null;
            }
        });
    }
    private void sendToWithRemoteEmailAndNumTel(String messageBody, String emailSubject){
        Map<String, String> allAdminEmailAndNumTel = DbUtils.getAllAdminEmailAndNumTel();
        if (!allAdminEmailAndNumTel.isEmpty()) {
            List<String> numTelList = new ArrayList<>();
            List<String> emailList = new ArrayList<>();
            allAdminEmailAndNumTel.forEach(new BiConsumer<String, String>() {
                @Override
                public void accept(String email, String numTel) {
                    numTelList.add(numTel);
                    emailList.add(email);
                }
            });
            if (!numTelList.isEmpty()) SmsUtils.send((String[]) numTelList.toArray(), messageBody);
            if (!emailList.isEmpty()) EmailFactory.sendTo((String[]) emailList.toArray(), emailSubject, messageBody);
        }
    }
    private void sendWithLocalEmailAndNumtel(ObservableList<UserForView> users, String messageBody, String emailSubject){
        Stream<User> userStream = users.stream().map(UserForView::getEditor);
        // LISTE DES EMAILS ADMINISTRATEURS
        List<String> emailList = userStream.map(User::getEmail).collect(Collectors.toList());
        // LISTE DES NUMERO DES ADMINISTRATEUR
        List<String> numTelList = userStream.map(User::getNumTel).collect(Collectors.toList());
        // ENVOYER SMS
        if (!numTelList.isEmpty()) SmsUtils.send((String[]) numTelList.toArray(), messageBody);
        // ENVOYER EMAIL
        if (!emailList.isEmpty()) EmailFactory.sendTo((String[]) emailList.toArray(), emailSubject, "");
    }
    private void saveReperageAndOrdonanceAndDispatchAndJtrAndAttachements(User redacteur, FolderForView affairForView) {
        // INSERTION DANS LA TABLE JOURNAL DE TRESORERIE
        saveJournalTresorerieRecette(affairForView.getId());
        // INSERTION DANS LA TABLE REPERAGE
        saveReperage(affairForView.getId());
        // INSERTION DANS LA TABLE ORDONANCE
        saveOrdonnance(affairForView.getId());
        // INSERTION DANS LA TABLE DE DISPATCH
        if (redacteur != null) DbUtils.insertOnDispatchTable(redacteur, affairForView);
        // ENREGISTREMENT DES PIECES JOINTES
        if (pjTilepane.getChildren().size() > 1) saveAllAttachement(affairForView);
    }
    private int saveOrdonnance(int idAffaire) {
        DossierFormController instance = getAffFormInstance();
        String numOrdonnance = instance.getNumOrdonance().getText();
        LocalDate value = instance.getDateOrdonance().getValue();
        UtilsData ordonnance = new UtilsData(createJavaSqlDateBy(value), numOrdonnance);
        if (numOrdonnance != null && !numOrdonnance.isEmpty() && value != null)
            return DbUtils.insertOnTableOrdonnance(ordonnance, idAffaire);
        return 0;
    }
    private int saveReperage(int idAffaire) {
        DossierFormController instance = getAffFormInstance();
        String rep = instance.getNumeroReperage().getText();
        LocalDate value = instance.getDateReperage().getValue();
        UtilsData reperage = new UtilsData(createJavaSqlDateBy(value), rep);
        if (rep != null && !rep.isEmpty() && value != null)
            return DbUtils.insertOnTableReperage(reperage, idAffaire);
        return 0;
    }
    private int saveJournalTresorerieRecette(int idAffaire){
        DossierFormController instance = getAffFormInstance();
        String numJtr = instance.getNumeroJtr().getText();
        LocalDate value = instance.getDateJtr().getValue();
        if (numJtr != null && !numJtr.isEmpty() && value != null)
            return DbUtils.insertOnTableJtr(numJtr, createJavaSqlDateBy(value), idAffaire);
        return 0;
    }
    private int getLasInsertId(Personne dp){
        if (dp.getType() == TypeDemandeur.PERSONNE_PHYSIQUE){
              PersonnePhysique ph = (PersonnePhysique) dp;
              if (ph.getNumCin()!=null && !ph.getNumCin().isBlank())
                  return DaoFactory.getDemandeurMoraleDao().getDemandeurIdBy(ph.getNumCin(),ParamerterType.CIN);
        }
        if (dp.getNumTel() != null && !dp.getNumTel().isEmpty())
            return DaoFactory.getDemandeurMoraleDao().getDemandeurIdBy(dp.getNumTel(), ParamerterType.NUMTEL);
        else if (dp.getEmail() != null && !dp.getEmail().isEmpty())
            return DaoFactory.getDemandeurMoraleDao().getDemandeurIdBy(dp.getEmail(), ParamerterType.EMAIL);

        return DaoFactory.getDemandeurMoraleDao().getDemandeurIdBy(dp.getNom(), ParamerterType.NAME);
    }
    // CREATE
    private UtilsData createProcuration() {
        NouveauDemandeurController newDemandeurFormInstance = getNewDemandeurFormInstance();
        Date dateProcuration = Date.valueOf(newDemandeurFormInstance.getDateProcuration().getValue());
        String numeroProcuration = newDemandeurFormInstance.getNumeroProcuration().getText();
        return new UtilsData(dateProcuration, numeroProcuration);
    }
    private Date createJavaSqlDateBy(LocalDate date) {
        return java.sql.Date.valueOf(date);
    }
    // END CREATE
    private User initRedateur() {
        DossierFormController instance = getAffFormInstance();
        if (instance.getMoiMemeRadio().isSelected()) {
            return LoginController.getConnectedUser();
        } else if (instance.getRechercherRedacteurRadio().isSelected()) {
            User value = instance.getRedactorCombobox().getValue();
            if (value != null)
                return value;
        }
        return null;
    }
    // BEGIN RESET
    private void resetForm(){
        NouveauDemandeurController.getInstance().resetDemandeurForm();
        DossierFormController.getInstance().resetAffaireForm();
        TerrainFromController.getInstance().resetTerrain();
        resetAttachement();
    }
    private void resetAttachement(){
        AttachementCreatorButton.getAttachementList().clear();
        pjTilepane.getChildren().retainAll(addAttachInBtn);
    }
    // END RESET
    private Propriete initTerrain(){
        DossierFormController instance = getAffFormInstance();
        if (instance.getConnexeRadio().isSelected()) {
            ConnexAffairForView value = instance.getConnexeCombobox().getValue();
            Propriete propriete1 = DaoFactory.getTerrainDao().findById(value.getIdTerrain());
            if (propriete1 != null)
                return propriete1;
        } else {
            Propriete propriete = TerrainFromController.getInstance().createTerrain(false);
            if (DaoFactory.getTerrainDao().create(propriete) == 1) {
                int idTerrain = DaoFactory.getTerrainDao().getIdTerrainBy(propriete.getSuperficie(),propriete.getParcelle(),propriete.getQuartier());
                propriete.setIdPropriete(idTerrain);
                if (propriete.getTitreDependant() != null)
                    DbUtils.insertOnTerrainTitre(propriete, TableName.DEPEND_TERRAIN_TITRE);
                return propriete;
            }
        }
        return null;
    }

    public void initTextArea() {
        MainService.getInstance().launch(new Task<Void>() {
            @Override protected Void call() throws Exception {
                StringBuffer stringBuffer = new StringBuffer();
                appendAffaire(stringBuffer);
                appendDemandeur(stringBuffer);
                if (getAffFormInstance().getSansEmpietementRadio().isSelected())
                    appendTerrain(stringBuffer);
                Platform.runLater(() -> MainAffaireFormController.getInstance().getTextArea().setText(stringBuffer.toString()));
                return null;
            }

            @Override protected void scheduled() {
                // DESACTIVER LE BOUTTON ENREGISTRER
                MainAffaireFormController.getInstance().getSaveBtn().setDisable(true);
                new ProgressTaskBinder(MainAffaireFormController.getInstance().getCheckProgressBar(), this);
            }

            @Override protected void succeeded() {

                MainAffaireFormController instance = MainAffaireFormController.getInstance();
                Label validationLabel = instance.getValidationLabel();
                AnchorPane validationPane = instance.getValidationPane();
                Label pieceJointeLabel = instance.getPieceJointeLabel();

                FormNavigationButton.executeUpdate(validationPane,validationLabel,pieceJointeLabel);
                MainAffaireFormController.getInstance().getSaveBtn().setDisable(false);
            }
            @Override protected void failed() {
                this.getException().printStackTrace();
            }
        });
    }
    private Personne initDemandeur(){
        saveAllDemandeur();
        JFXListView<ApplicantForView> applicantListView;
        applicantListView = MainDemandeurFormController.getInstance().getApplicantListView();
        ObservableList<ApplicantForView> items = applicantListView.getItems();
        TypeDemande typeDemande = getTypeDemande();
        switch (typeDemande){
            case AFFECTATION:{
                ApplicantForView applicantForView = items.get(0);
                return  (PersonneMorale)applicantForView.getPersonne();
            }
            case ACQUISITION:
            case PRESCRIPTION:{
                if (items.size()>1){
                    for (ApplicantForView item : items) {
                        boolean isRepresentant = item.getRadioButton().isSelected();
                        return isRepresentant ? item.getPersonne() : null;
                    }
                }else return items.get(0).getPersonne();
            }break;
        }
        return null;
    }
    private void saveAllDemandeur(){
        JFXListView<ApplicantForView> applicantListView = MainDemandeurFormController.getInstance().getApplicantListView();
        ObservableList<ApplicantForView> items = applicantListView.getItems();
        if (!items.isEmpty()) {
            items.forEach(applicantForView ->{
                Personne personne = applicantForView.getPersonne();
                savePersonne(personne);
            });
        }
    }
    private void savePersonne(Personne personne){
        if (personne.getType() == TypeDemandeur.PERSONNE_PHYSIQUE) savePersonnePhysique((PersonnePhysique) personne);
        else savePersonneMorale((PersonneMorale) personne);
    }
    private void savePersonneMorale(PersonneMorale dm){
        // verifier si elle existe déja dans la base
        if (dm.getId()!=0){
            if (DaoFactory.getDemandeurMoraleDao().create(dm) == 1){
                PersonnePhysique rep = dm.getRepresentant();
                // RECUPERATION DE L'IDENTIFIANT DU PERSONNE MORALE
                dm.setId(getLasInsertId(dm));
                if (rep!=null && rep.getId()!=0){
                      savePersonnePhysique(rep);
                      rep.setId(getLasInsertId(rep));
                    // INSERTION DANS LA TABLE REPRESENTANT
                    if (DbUtils.insertOnTableRepresentant(rep,dm,dateFormulation,createProcuration()) == 1){

                    }
                }
            }
        }
    }
    private void savePersonnePhysique(PersonnePhysique ph){
        /*
         * ON FAIT DEUX INSERTION POUR LES PRESONNES PHYSIQUE CAR ON A UNE RELATION D'HERITAGE
         * - UNE DANS LA TABLE " personnne morale " pour enregister le nom , adresse
         * - UNE DANS LA TABLE " personne physique " pour enregister le situation matrimoniale ,n ect
         * */
        // verifier si la personne existe déja dans la base
        if (ph.getId() != 0) {
            // INSERTION DEs DONNES DANS LA TABLE PERSONNE MORALE
            if (DaoFactory.getDemandeurMoraleDao().create(ph) == 1){
                // RECUPERATION DE L'IDENTIFIANT DU DEMANDEUR INSERER
                ph.setId(getLasInsertId(ph));
                // INSERTION DANS LA TABLE PERSONNE PHYSIQUE
                DaoFactory.getDemandeurPhysiqueDao().create(ph);
            }
            if (ph.getSituationMatrimoniale() == SituationMatrimoniale.MARIE) {
                PersonnePhysique conjoint = ph.getConjoint();
                Mariage mariage = ph.getMariage();
                if (conjoint != null) {
                    savePersonnePhysique(conjoint);
                    if (mariage != null) DbUtils.insertOnTableMariage(mariage, ph, conjoint);
                }
            }
        }
    }
    // END INIT
    public static PieceJointeFormController getInstance() {
        return pieceJointeFormController;
    }
    private final String SPACE = "\n\n\n";
    private final String TAB = "\t\t\t\t\t\t\t";
    private final String TAB1 = "\t\t\t\t";
    @FXML private JFXButton checkBtn;
    @FXML private TilePane pjTilepane;
    @FXML private JFXButton addAttachInBtn;
    @FXML private JFXButton addAttachOutBtn;
    @FXML private JFXButton delPieceBtn;
    @FXML private JFXButton pjNextBtn;
    @FXML private JFXButton pjPrevBtn;
    private static Date dateFormulation;
    private static PieceJointeFormController pieceJointeFormController;
    @Override public boolean isOk(String result) {

        return true;
    }
}
