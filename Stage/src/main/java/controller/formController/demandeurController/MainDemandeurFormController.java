package controller.formController.demandeurController;
import com.jfoenix.controls.JFXButton;
import controller.formController.MainAffaireFormController;
import io.github.palexdev.materialfx.filter.IFilterable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import java.net.URL;
import java.util.ResourceBundle;

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
    public static MainDemandeurFormController getInstance() {
        return mainDemandeurFormController;
    }

    public static void setMainDemandeurFormController(MainDemandeurFormController mainDemandeurFormController) {
        MainDemandeurFormController.mainDemandeurFormController = mainDemandeurFormController;
    }
    private static MainDemandeurFormController mainDemandeurFormController;
    @FXML private JFXButton dmdPrevBtn;
    @FXML private JFXButton dmdNextBtn;
    @FXML private Tab searchTab;
    @FXML private Tab newTab;
    @FXML private ToggleButton personnePhysique;
    @FXML private ToggleGroup typeDemandeur;
    @FXML private ToggleButton personneMorale;

}
