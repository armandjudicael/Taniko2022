package controller.formController.other;
import javafx.scene.control.TextInputControl;
import model.Enum.*;
import model.pojo.business.*;
import model.pojo.utils.Mariage;
import view.Model.ViewObject.AffaireForView;
import view.Model.ViewObject.ProcedureForTableview;
import com.jfoenix.controls.JFXComboBox;
import controller.viewController.AffairViewController;
import dao.DaoFactory;
import dao.DbOperation;
import model.other.MainService;
import view.Dialog.Other.Notification;
import view.Helper.Attachement.AttachementCheckerButton;
import view.Helper.Attachement.AttachementCreatorButton;
import view.Helper.Attachement.AttachementRemoverButton;
import view.Model.ViewObject.ConnexAffairForView;
import com.jfoenix.controls.JFXButton;
import controller.formController.demandeurController.MainDemandeurFormController;
import controller.formController.demandeurController.NouveauDemandeurController;
import controller.formController.demandeurController.RechercherDemandeurController;
import controller.viewController.LoginController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.TilePane;
import java.net.URL;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ResourceBundle;
import static model.pojo.business.Affaire.string2TypeDemande;
import static dao.DemandeurMoraleDao.*;

public class PieceJointeFormController implements Initializable{

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        initBtnAction();
    }
    private void initBtnAction(){
        ObservableList<Node> children = pjTilepane.getChildren();
        new AttachementCreatorButton(addAttachInBtn,children,false);
        new AttachementCreatorButton(addAttachOutBtn,children,false);
        new AttachementRemoverButton(delPieceBtn,children,false);
        new AttachementCheckerButton(checkBtn,children);
        delPieceBtn.disableProperty().bind(Bindings.isEmpty(children));
        checkBtn.disableProperty().bind(Bindings.isEmpty(children));
        pjPrevBtn.setOnAction(event -> MainAffaireFormController.updateLabelAndShowPane(pjPrevBtn));
        saveBtn.setOnAction(event -> enregistrerAffaire());
    }
    private void enregistrerAffaire(){
        MainService.getInstance().launch(new Task<Void>() {
            @Override protected void succeeded() {
                resetForm();
            }
            @Override protected Void call() throws Exception {
                saveAffaire();
                return null;
            }
            @Override protected void failed() {
                super.failed();
                Throwable exception = this.getException();
                exception.printStackTrace();
            }
        });
    }
    private void saveAllAttachement(Affaire affaire){
        ObservableList<PieceJointe> attachementList = AttachementCreatorButton.getAttachementList();
        if (attachementList!=null && !attachementList.isEmpty()){
           int[] all = DaoFactory.getPieceJointeDao().createAll(attachementList,affaire);
           if (attachementList.size() == all.length){
               String message = " Piece jointe enregistré avdec succès ";
               //     Notification.getInstance(message, NotifType.SUCCESS).showNotif();
           }else {
               String message = " Echec de l'enregistrement des pieces jointes ";
               //  Notification.getInstance(message,NotifType.WARNING).showNotif();
           }
        }
    }
    private void saveAffaire(){
        AffaireFormController instance = getAffFormInstance();
        LocalDate localDate = instance.getDateFormulation().getValue();
            dateFormulation = createJavaSqlDateBy(localDate);
            PersonneMorale personneMorale = initDemandeur();
            String numeroAffaire = instance.getNumeroAffaire().getText()+ instance.getNumAndDateAffLabel().getText();
            User redacteur = initRedateur();
            Terrain terrain = initTerrain();
            AffaireForView affairForView = new AffaireForView(numeroAffaire,dateFormulation,string2TypeDemande(instance.getTypeDemande().getValue()), redacteur, AffaireStatus.RUNNING, personneMorale, terrain, new ProcedureForTableview(ProcedureStatus.NONE,new SimpleStringProperty("Auccune procedure")));
            // ENREGISTRER AFFAIRE
            if (DaoFactory.getAffaireDao().create(affairForView) != 0){
                affairForView.setId(DaoFactory.getAffaireDao().getAffaireIdByNum(affairForView.getNumero()));
               // updateViewAndNotifyUser(numeroAffaire,affairForView);
                saveReperageAndOrdonanceAndDispatchAndJtrAndAttachements(redacteur,affairForView);
            }
    }
    private void updateViewAndNotifyUser(String numeroAffaire, AffaireForView affairForView) {
        Notification.getInstance(" L'affaire N° "+ numeroAffaire +" est enregistrer avec succès ", NotifType.SUCCESS).showNotif();
        // MIS A JOUR DE L'AFFICHAGE

        // Eto mandefa email am chef jiaby !
        Platform.runLater(() -> AffairViewController.getInstance().getTableView().getItems().add(affairForView));
    }
    private void saveReperageAndOrdonanceAndDispatchAndJtrAndAttachements(User redacteur, AffaireForView affairForView) {
        // INSERTION DANS LA TABLE JOURNAL DE TRESORERIE
        saveJournalTresorerieRecette(affairForView.getId());
        // INSERTION DANS LA TABLE REPERAGE
        saveReperage(affairForView.getId());
        // INSERTION DANS LA TABLE ORDONANCE
        saveOrdonnance(affairForView.getId());
        // INSERTION DANS LA TABLE DE DISPATCH
        if (redacteur !=null) DbOperation.insertOnDispatchTable(redacteur, affairForView);
        // ENREGISTREMENT DES PIECES JOINTES
        if (pjTilepane.getChildren().size() > 1) saveAllAttachement(affairForView);
    }
    private int saveOrdonnance(int idAffaire){
        AffaireFormController instance = getAffFormInstance();
        String ord = instance.getNumOrdonance().getText();
        LocalDate value = instance.getDateOrdonance().getValue();
        if (ord!=null && !ord.isEmpty() && value!=null)
            return DbOperation.insertOnTableOrdonnance(ord,createJavaSqlDateBy(value),idAffaire);
        return 0;
    }
    private int saveReperage(int idAffaire){
        AffaireFormController instance = getAffFormInstance();
        String rep = instance.getNumeroReperage().getText();
        LocalDate value = instance.getDateReperage().getValue();
        if (rep!=null && !rep.isEmpty() && value!=null)
            return DbOperation.insertOnTableReperage(rep,createJavaSqlDateBy(value),idAffaire);
        return 0;
    }
    private int saveJournalTresorerieRecette(int idAffaire){
        AffaireFormController instance = getAffFormInstance();
        String numJtr = instance.getNumeroJtr().getText();
        LocalDate value = instance.getDateJtr().getValue();
        if (numJtr!=null && !numJtr.isEmpty() && value!=null)
           return DbOperation.insertOnTableJtr(numJtr,createJavaSqlDateBy(value),idAffaire);
        return 0;
    }
    private PersonneMorale initDemandeur(){
        MainDemandeurFormController instance = getMainDmdFormInstance();
        Tab searchTab = instance.getSearchTab();
        Tab newTab = instance.getNewTab();
        ToggleButton moraleToogle = instance.getPersonneMorale();
        if (newTab.isSelected()){
            if (moraleToogle.isSelected()) return initDemandeurMorale();
            else return initDemandeurPhysique();
        }else if (searchTab.isSelected()){
            return  getSearchDmdFormInstance().getDemandeurCombobox().getValue();
        }
        return null;
    }
    private PersonneMorale initDemandeurMorale(){
        JFXComboBox<PersonneMorale> demandeurCombobox = getSearchDmdFormInstance().getDemandeurCombobox();
        PersonnePhysique rep = createRepresentant();
        if (nouveauRepresentant()) return enregisterRepresentant(demandeurCombobox.getValue(),rep,true);
        else return enregistrerDemandeurEtRepresenant(rep) ;
    }
    private PersonneMorale enregistrerDemandeurEtRepresenant(PersonnePhysique rep) {
        PersonneMorale dm = createDemandeurMorale();
        // INSERTION DU DEMANDEUR MORALE
        if (DaoFactory.getDemandeurMoraleDao().create(dm) == 1){
            // RECUPERATION DE L'IDENTIFIANT DU PERSONNE MORALE
            dm.setId(getLasInsertId(dm));
            PersonneMorale dm1 = enregisterRepresentant(dm, rep,false);
            if (dm1 != null) return dm1;
        }
        return dm;
    }
    private PersonneMorale enregisterRepresentant(PersonneMorale dm, PersonnePhysique rep,Boolean isNewRepresentant) {
        // INSERTION DU REPRESENTANT
        if (DaoFactory.getDemandeurMoraleDao().create(rep) == 1){
            // RECUPERATION DE L'IDENTIFIANT DU REPRESENTANT
            rep.setId(getLasInsertId(rep));
            // INSERTION DANS LA PERSONNE PHYSIQUE
            if (DaoFactory.getDemandeurPhysiqueDao().create(rep)==1){
                // INSERTION DANS LA TABLE REPRESENTANT
                if (DbOperation.insertOnTableRepresentant(rep, dm,isNewRepresentant ? Date.valueOf(LocalDate.now()) : dateFormulation )==1) return dm;
            }
        }
        return null;
    }
    private PersonnePhysique initDemandeurPhysique(){
        PersonnePhysique ph = createDemandeurPhysique();
        /*
        * ON FAIT DEUX INSERTION POUR LES PRESONNES PHYSIQUE CAR ON A UNE RELATION D'HERITAGE
        * - UNE DANS LA TABLE " personnne morale " pour enregister le nom , adresse
        * - UNE DANS LA TABLE " personne physique " pour enregister le situation matrimoniale ,n ect
        * */
        // INSERTION DEs DONNES DANS LA TABLE PERSONNE MORALE
        if (DaoFactory.getDemandeurMoraleDao().create(ph) == 1){
            // RECUPERATION DE L'IDENTIFIANT DU DEMANDEUR INSERER
            ph.setId(getLasInsertId(ph));
            // INSERTION DANS LA TABLE PERSONNE PHYSIQUE
            DaoFactory.getDemandeurPhysiqueDao().create(ph);
        }
        if(NouveauDemandeurController.getInstance().getMarie().isSelected()){
            PersonnePhysique conjoint = createConjoint();
            // INSERTION DU CONJOINT DONNES DANS LA TABLE PERSONNE MORALE
            if (DaoFactory.getDemandeurMoraleDao().create(conjoint) == 1){
                // RECUPERATION DE LA L'IDENTIFIANT DU CONJOINT INSERER
                conjoint.setId(getLasInsertId(conjoint));
                // INSERTION DANS LA TABLE PERSONNE PHYSIQUE
                if (DaoFactory.getDemandeurPhysiqueDao().create(conjoint) == 1){
                    // MARIAGE
                    Mariage mariage = createMariage();
                    mariage.setDemandeur(ph);
                    mariage.setConjoint(conjoint);
                    // INSERTION DANS LA TABLE MARIAGE
                     if (DbOperation.insertOnTableMariage(mariage) == 1) return ph;
                }
            }
        }
        return ph;
    }
    private int getLasInsertId(PersonneMorale dp){
        if ( dp.getNumTel()!=null && !dp.getNumTel().isEmpty()) return DaoFactory.getDemandeurMoraleDao().getDemandeurIdBy(dp.getNumTel(),ParamerterType.NUMTEL);
        else if ( dp.getEmail()!=null && !dp.getEmail().isEmpty()) return DaoFactory.getDemandeurMoraleDao().getDemandeurIdBy(dp.getEmail(),ParamerterType.EMAIL);;
        return DaoFactory.getDemandeurMoraleDao().getDemandeurIdBy(dp.getNom(),ParamerterType.NAME);
    }
    private PersonnePhysique createDemandeurPhysique(){
        NouveauDemandeurController instance = getNewDemandeurFormInstance();
        PersonnePhysique ph = new PersonnePhysique();
        ph.setNom(instance.getNomDmd().getText() +" "+(instance.getPrenomDmd().getText()!=null ? instance.getPrenomDmd().getText() : ""));
        ph.setAdresse(instance.getAdresse().getText());
        String numero = instance.getTelephone().getText();
        ph.setNumTel((numero!=null && !numero.isEmpty() ? numero : null));
        String email = instance.getEmail().getText();
        ph.setEmail((email!=null && !email.isEmpty()) ? email : null);
        ph.setDateDeNaissance(createJavaSqlDateBy(instance.getDateDeNaissanceDmd().getValue()));
        ph.setLieuDeNaissance(instance.getLieuDeNaissanceDmd().getText());
        ph.setSexe(initSex(true).trim());
        String parcelle = instance.getDemandeurParcelle1().getText()+"/"+instance.getDemandeurParcelle2().getText();
        ph.setParcelle((parcelle!=null && !parcelle.isEmpty()) ? parcelle : "");
        ph.setLot(instance.getLot().getText());
        ph.setProfession(instance.getProffession().getText());
        ph.setSituationMatrimoniale(initSituationMatrimoniale());
        ph.setNationalite(instance.getNationalite().getText());
        ph.setType(TypeDemandeur.PERSONNE_PHYSIQUE);
        ph.setPere(instance.getNomPere().getText());
        ph.setMere(instance.getNomMere().getText());
        return ph;
    }
    private Date createJavaSqlDateBy(LocalDate date){
       return java.sql.Date.valueOf(date);
    }
    private String initSex(Boolean isApplicant){
        NouveauDemandeurController instance = getNewDemandeurFormInstance();
        Toggle selectedToggle = instance.getSexe().getSelectedToggle();
        boolean equals = selectedToggle.equals(instance.getMasculin());
        if (isApplicant){
            if (equals) return  "Masculin";
            else return  "Féminin";
        }else {
            if (!equals) return  "Masculin";
            else return  "Féminin";
        }
    }
    private String initSituationMatrimoniale(){
        NouveauDemandeurController instance = getNewDemandeurFormInstance();
        return (instance.getMarie().isSelected() ? "Marié" :
               (instance.getCelibataire().isSelected() ? "célibataire":"veuve" ));
    }
    private PersonnePhysique createConjoint(){
        NouveauDemandeurController instance = getNewDemandeurFormInstance();
        // CONJOINT
        String nomConjoint = instance.getNomConjoint().getText();
        String prenomConjoint = instance.getPrenomConjoint().getText();
        String lieuDeNaissanceConjoint = instance.getLieuDeNaissanceConjoint().getText();
        LocalDate value = instance.getDateNaissanceConjoint().getValue();
        PersonnePhysique conjoint = new PersonnePhysique();
        conjoint.setNom(nomConjoint+" "+prenomConjoint);
        conjoint.setLieuDeNaissance(lieuDeNaissanceConjoint);
        conjoint.setDateDeNaissance(createJavaSqlDateBy(value));
        conjoint.setSexe(initSex(false));
        conjoint.setType(TypeDemandeur.PERSONNE_PHYSIQUE);
        conjoint.setSituationMatrimoniale("Marié");
        return conjoint;
    }
    private Mariage createMariage(){
        NouveauDemandeurController instance = getNewDemandeurFormInstance();
        // MARIAGE
        String lieuMariage = instance.getLieuMariage().getText();
        LocalDate value1 = instance.getDateMariage().getValue();
        Date dateMariage = createJavaSqlDateBy(value1);
        String regimeMatrimoniale = instance.getRegimeMatrimoniale().getValue();
        return new Mariage(dateMariage,lieuMariage,Mariage.String2RegimeMatrimoniale(regimeMatrimoniale));
    }
    private PersonneMorale createDemandeurMorale(){
        NouveauDemandeurController instance = getNewDemandeurFormInstance();
        String raisonSocial = instance.getRaisonSocial().getText();
        String siegeSocial = instance.getSiegeSocial().getText();
        String telephone = instance.getTelPersonneMorale().getText();
        String email = instance.getEmailPersonneMorale().getText();
        String nationalite = instance.getNationaliteMorale().getText();
        PersonneMorale pm = new PersonneMorale();
        pm.setNumTel(telephone);
        pm.setEmail(email);
        pm.setNationalite(nationalite);
        pm.setAdresse(siegeSocial);
        pm.setNom(raisonSocial);
        pm.setType(TypeDemandeur.PERSONNE_MORALE);
        return pm;
    }
    private PersonnePhysique createRepresentant(){
        NouveauDemandeurController instance = getNewDemandeurFormInstance();
        String nomRepresentant = instance.getNomRepresentant().getText();
        String prenomRepresentant = instance.getPrenomRepresentant().getText();
        String professionRepresentant = instance.getFonctionRepresentant().getText();
        String adresseRepresentant = instance.getAdresseRepresentant().getText();
        PersonnePhysique ps = new PersonnePhysique();
        ps.setType(TypeDemandeur.PERSONNE_PHYSIQUE);
        ps.setNom(nomRepresentant+" "+prenomRepresentant);
        ps.setProfession(professionRepresentant);
        ps.setAdresse(adresseRepresentant);
        return ps;
    }
    private User initRedateur(){
        AffaireFormController instance = getAffFormInstance();
        if (instance.getMoiMemeRadio().isSelected()){
            return LoginController.getConnectedUser();
        }else if (instance.getRechercherRedacteurRadio().isSelected()){
            User value = instance.getRedactorCombobox().getValue();
            if (value!=null)
                return value;
        }
        return null;
    }
    private void resetForm(){
        resetDemandeurForm();
        resetAffaireForm();
        resetTerrain();
        resetAttachement();
        resetScrollpane();
    }
    private void resetAttachement(){
        AttachementCreatorButton.getAttachementList().clear();
        pjTilepane.getChildren().retainAll(addAttachInBtn);
    }
    private void resetTerrain(){
        TerrainFromController instance = getTerrainFromInstance();
        instance.getSuperficieLabel().setText("");
        instance.getParcelleTerrain().clear();
        instance.getParcelleTerrain1().clear();
        instance.getSise().clear();
//        instance.getDistrict().clear();
//        instance.getCommune().clear();
//        instance.getRegion().clear();
        if (instance.getInsererPropriete().isSelected()){
            instance.getNomPropriete().clear();
            instance.getNumeroTitre().clear();
            instance.getNumeroMorcellement().clear();
            instance.getDateCreation().setValue(null);
        }else if (instance.getRechercherPropriete().isSelected()){
           instance.getTitleCombobox().setValue(null);
        }
    }
    // AFFAIRE
    private void resetAffaireForm(){
        resetReperageAndAffaire();
        resetJtr();
        resetOrdonnance();
        // RESET NAVIGATION
        MainAffaireFormController.updateLabelAndShowPane(null);
        // RESET COMBOBOX
        RechercherDemandeurController.getInstance().getDemandeurCombobox().setValue(null);
        // RESET TREETABLEVIEW ITEM
        RechercherDemandeurController.getInstance().getRepresentantForViews().clear();
    }
    private void resetReperageAndAffaire(){
        AffaireFormController instance = getAffFormInstance();
        instance.getNumeroReperage().clear();
        instance.getDateReperage().setValue(LocalDate.now());
        instance.getTypeDemande().setValue("ACQUISITION");
        instance.getNumeroAffaire().clear();
        instance.getDateFormulation().setValue(LocalDate.now());
    }
    private void resetJtr(){
        AffaireFormController instance = getAffFormInstance();
        instance.getNumeroJtr().clear();
        instance.getDateJtr().setValue(LocalDate.now());
    }
    private void resetOrdonnance(){
        AffaireFormController instance = getAffFormInstance();
        instance.getNumOrdonance().clear();
        instance.getDateOrdonance().setValue(LocalDate.now());
    }
    // DEMANDEUR
    private void resetDemandeurForm(){
        resetDemandeurMorale();
        resetDemandeurPhysique();
        resetConjoint();
        resetMariage();
        resetRepresentant();
    }
    private void resetScrollpane(){
        // DEMANDEUR
        NouveauDemandeurController instance = getNewDemandeurFormInstance();
        instance.getMoralPane().setVvalue(0.0);
        instance.getPhysiquePane().setVvalue(0.0);
        // TERRAIN
        TerrainFromController instance1 = getTerrainFromInstance();
        instance1.getTerrainScrollPane().setVvalue(0.0);
        // AFFAIREFORM
        AffaireFormController instance2 = getAffFormInstance();
        instance2.getAffaireScrollpane().setVvalue(0.0);
    }
    private void resetMariage(){
        NouveauDemandeurController instance = getNewDemandeurFormInstance();
        // MARIAGE
        instance.getLieuMariage().clear();
        instance.getDateMariage().setValue(LocalDate.now());
    }
    private void resetConjoint(){
        NouveauDemandeurController instance = getNewDemandeurFormInstance();
        // CONJOINT
        instance.getNomConjoint().clear();
        instance.getPrenomConjoint().clear();
        instance.getLieuDeNaissanceConjoint().clear();
        instance.getDateNaissanceConjoint().setValue(null);
    }
    private void resetRepresentant(){
        NouveauDemandeurController instance = getNewDemandeurFormInstance();
        instance.getNomRepresentant().clear();
        instance.getPrenomRepresentant().clear();
        instance.getFonctionRepresentant().clear();
        instance.getAdresseRepresentant().clear();
    }
    private void resetDemandeurMorale(){
        NouveauDemandeurController instance = getNewDemandeurFormInstance();
        clearAndEnable(instance.getRaisonSocial());
        clearAndEnable(instance.getSiegeSocial());
        clearAndEnable(instance.getTelPersonneMorale());
        clearAndEnable(instance.getEmailPersonneMorale());
        clearAndEnable(instance.getNationaliteMorale());
    }
    private void clearAndEnable(TextInputControl inputControl){
        inputControl.clear();
        inputControl.setDisable(inputControl.isDisable());
    }
    private boolean nouveauRepresentant(){
        NouveauDemandeurController instance = getNewDemandeurFormInstance();
        JFXComboBox<PersonneMorale> demandeurCombobox = getSearchDmdFormInstance().getDemandeurCombobox();
        return instance.getRaisonSocial().isDisable() &&
                instance.getSiegeSocial().isDisable() &&
                instance.getTelPersonneMorale().isDisable() &&
                instance.getEmailPersonneMorale().isDisable() &&
                instance.getNationalite().isDisable() &&
                demandeurCombobox.getValue()!=null;
    }
    private void resetDemandeurPhysique(){
        NouveauDemandeurController instance = getNewDemandeurFormInstance();
        instance.getNomDmd().clear();
        instance.getPrenomDmd().clear();
        instance.getDateDeNaissanceDmd().setValue(null);
        instance.getLieuDeNaissanceDmd().clear();
        instance.getAdresse().clear();
        instance.getDemandeurParcelle1().clear();
        instance.getDemandeurParcelle2().clear();
        instance.getLot().clear();
        instance.getProffession().clear();
        instance.getEmail().clear();
        instance.getTelephone().clear();
        instance.getNationalite().clear();
        instance.getNomPere().clear();
        instance.getNomMere().clear();
    }
    private Titre initTitre(){
        TerrainFromController instance = getTerrainFromInstance();
        if (instance.getRadioDependant().isSelected()){
            if (instance.getRechercherPropriete().isSelected()) {
                Titre titre = instance.getTitleCombobox().getValue();
                return titre;
            }else{
                Titre titre = new Titre();
                titre.setNomPropriete(instance.getNomPropriete().getText());
                titre.setNumero(instance.getNumeroTitre()+"-BA");
                titre.setNumMorcelement(instance.getNumeroMorcellement().getText());
                titre.setDate(createJavaSqlDateBy(instance.getDateCreation().getValue()));
                if (DaoFactory.getTitreDao().create(titre) != 0) {
                    int id = DaoFactory.getTitreDao().findByNum(titre.getNumero());
                    titre.setId(id);
                    Notification.getInstance("Titre enregistré ave succés", NotifType.SUCCESS).showNotif();
                }
                return titre;
            }
        }
        return null;
    }
    private Terrain initTerrain(){
        AffaireFormController instance = getAffFormInstance();
        if (instance.getConnexeRadio().isSelected()){
            ConnexAffairForView value = instance.getConnexeCombobox().getValue();
            Terrain terrain1 = DaoFactory.getTerrainDao().findById(value.getIdTerrain());
            if (terrain1!=null)
                return terrain1;
        }else{
            Terrain terrain = createTerrain();
            if (DaoFactory.getTerrainDao().create(terrain) == 1 ){
                int idTerrain = DaoFactory.getTerrainDao().getIdTerrainBy(terrain.getSuperficie(),terrain.getParcelle(),terrain.getQuartier());
                terrain.setIdTerrain(idTerrain);
                if (terrain.getTitreDependant()!=null)
                    DbOperation.insertOnTerrainTitre(terrain,TableName.DEPEND_TERRRAIN_TITRE);
                return terrain;
            }
        }
        return null;
    }
    private Terrain createTerrain(){
       TerrainFromController instance = getTerrainFromInstance();
        String superficie = instance.getSuperficieLabel().getText();
        String parcelleTerrain = instance.getParcelleTerrain().getText()+ "/" + instance.getParcelleTerrain1().getText();
        String quartierTerrain = instance.getSise().getText();
        String district = instance.getDistrict().getText();
        String commune = instance.getCommune().getText();
        String region = instance.getRegion().getText();
        return new Terrain(superficie,parcelleTerrain,district,commune,quartierTerrain,region,initTitre());
    }
    @FXML private JFXButton checkBtn;
    @FXML private TilePane pjTilepane;
    @FXML private JFXButton addAttachInBtn;
    @FXML private JFXButton addAttachOutBtn;
    @FXML private JFXButton delPieceBtn;
    @FXML private JFXButton saveBtn;
    @FXML private JFXButton pjPrevBtn;
    public static TerrainFromController getTerrainFromInstance() {
        if(terrainFromInstance == null) terrainFromInstance = TerrainFromController.getInstance();
        return terrainFromInstance;
    }
    public static NouveauDemandeurController getNewDemandeurFormInstance() {
        if (newDemandeurFormInstance == null) newDemandeurFormInstance = NouveauDemandeurController.getInstance();
        return newDemandeurFormInstance;
    }
    public static RechercherDemandeurController getSearchDmdFormInstance() {
        if (searchDmdFormInstance == null) searchDmdFormInstance = RechercherDemandeurController.getInstance();
        return searchDmdFormInstance;
    }
    public static AffaireFormController getAffFormInstance() {
        if (affFormInstance ==null) affFormInstance = AffaireFormController.getInstance();
        return affFormInstance;
    }
    public static MainDemandeurFormController getMainDmdFormInstance() {
        if (mainDmdFormInstance == null) mainDmdFormInstance = MainDemandeurFormController.getInstance();
        return mainDmdFormInstance;
    }
    private static TerrainFromController terrainFromInstance;
    private static NouveauDemandeurController newDemandeurFormInstance;
    private static RechercherDemandeurController searchDmdFormInstance;
    private static AffaireFormController affFormInstance;
    private static MainDemandeurFormController mainDmdFormInstance;
    private static Date dateFormulation;
}
