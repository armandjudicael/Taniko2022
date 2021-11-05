package controller.formController.demandeurController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import controller.formController.other.MainAffaireFormController;
import controller.formController.other.PieceJointeFormController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import java.net.URL;
import java.util.ResourceBundle;

import static controller.formController.other.MainAffaireFormController.getNewDemandeurFormInstance;

public class MainDemandeurFormController implements Initializable {
    @Override public void initialize(URL location, ResourceBundle resources) {
        mainDemandeurFormController = this;
        initButton();
        initToggleGroup();
    }
    private void initButton(){
        dmdNextBtn.setOnAction(event -> MainAffaireFormController.getInstance().updateLabelAndShowPane(dmdNextBtn));
        dmdPrevBtn.setOnAction(event -> MainAffaireFormController.getInstance().updateLabelAndShowPane(dmdPrevBtn));
    }
    private void initToggleGroup(){
        NouveauDemandeurController nouveauDemandeurcontrollerInstance = NouveauDemandeurController.getInstance();
        RechercherDemandeurController rechercherDemandeurControllerInstance = RechercherDemandeurController.getInstance();
        typeDemandeur.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                if (newValue.equals(personneMorale)){
                    nouveauDemandeurcontrollerInstance.getMoralPane().toFront();
                    rechercherDemandeurControllerInstance.getMoralInfo().toFront();
                }else {
                    nouveauDemandeurcontrollerInstance.getPhysiquePane().toFront();
                    rechercherDemandeurControllerInstance.getPhysiqueInfo().toFront();
                }
            }
        });
    }

    private boolean checkDemandeurForm(){
        if (personnePhysique.isSelected()) return checkDemandeurPhysique();
        else return checkDemandeurMorale();
    }

    private Boolean checkDemandeurPhysique(){
        NouveauDemandeurController newDemandeurFormInstance = getNewDemandeurFormInstance();
        String email = newDemandeurFormInstance.getEmailPersonneMorale().getText();
        String numTel = newDemandeurFormInstance.getTelephone().getText();
        String nomEtPrenom = newDemandeurFormInstance.getNomDmd().getText();
        if (    email!=null && email.isEmpty() &&
                numTel!=null && !numTel.isEmpty() &&
                !nomEtPrenom.isEmpty() && nomEtPrenom!=null){

        }
        return false;
    }

    private boolean checkDemandeurMorale(){
        NouveauDemandeurController instance = getNewDemandeurFormInstance();
        String raisonSocial = instance.getRaisonSocial().getText();
        String telephone = instance.getTelPersonneMorale().getText();
        String email = instance.getEmailPersonneMorale().getText();
        if ((raisonSocial!=null && !raisonSocial.isEmpty()) &&
                (telephone!=null && !telephone.isEmpty()) &&
                (email!=null && !email.isEmpty())){

        }
        return false;
    }

    public ToggleButton getPersonnePhysique() {
        return personnePhysique;
    }
    public ToggleButton getPersonneMorale() {
        return personneMorale;
    }
    public Tab getSearchTab() {
        return searchTab;
    }
    public Tab getNewTab() {
        return newTab;
    }
    public JFXTabPane getApplicantTabPane() {
        return applicantTabPane;
    }
    public static MainDemandeurFormController getInstance() {
        return mainDemandeurFormController;
    }
    public static void setMainDemandeurFormController(MainDemandeurFormController mainDemandeurFormController) {
        MainDemandeurFormController.mainDemandeurFormController = mainDemandeurFormController;
    }
    private static MainDemandeurFormController mainDemandeurFormController;
    @FXML private JFXTabPane applicantTabPane;
    @FXML private JFXButton dmdPrevBtn;
    @FXML private JFXButton dmdNextBtn;
    @FXML private Tab searchTab;
    @FXML private Tab newTab;
    @FXML private ToggleButton personnePhysique;
    @FXML private ToggleGroup typeDemandeur;
    @FXML private ToggleButton personneMorale;
}
