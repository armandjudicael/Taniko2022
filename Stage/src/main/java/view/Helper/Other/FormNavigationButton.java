package view.Helper.Other;
import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import controller.formController.otherFormController.DossierFormController;
import controller.formController.otherFormController.MainAffaireFormController;
import controller.formController.otherFormController.PieceJointeFormController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.Enum.TypeDemande;
import model.other.MainService;

public class FormNavigationButton implements EventHandler<ActionEvent> {
    private  Label newLabel;
    private  Label oldLabel;
    private  Pane pane;
    private JFXButton button;

    public FormNavigationButton(JFXButton jfxButton,Label newLabel, Label oldLabel, Pane pane) {
        this.newLabel = newLabel;
        this.oldLabel = oldLabel;
        this.pane = pane;
        button = jfxButton;
        if(button!=null) button.setOnAction(this);
        else executeUpdate(pane, newLabel, oldLabel);
    }

    private void checkFolderPropretiesForm(){
        MainService.getInstance().launch(new Task<Void>() {
            @Override protected Void call() throws Exception {
                DossierFormController instance = DossierFormController.getInstance();
                String numAff = instance.getNumeroAffaire().getText()+ instance.getNumAndDateAffLabel().getText();
                String numOrd = instance.getNumOrdonance().getText();
                String numRep = instance.getNumeroReperage().getText();
                String numJtr = instance.getNumeroJtr().getText();
                //String value = DbUtils.checkAffaireInfoForm(numAff, numOrd,numRep,numJtr);

                if (isOk("")) Platform.runLater(() -> {
                    if (MainAffaireFormController.getTypeDemande()== TypeDemande.ACQUISITION){
                        AnchorPane typeDemandeurPanel = MainAffaireFormController.getInstance().getTypeDemandeurPanel();
                        executeUpdate(typeDemandeurPanel,newLabel,oldLabel);
                        return;
                    }
                    executeUpdate(pane,newLabel,oldLabel);
                });
                else Platform.runLater(() -> SnackBarNotification.getInstance().showOn(MainAffaireFormController.getInstance().getMainAffBorderPane(),"Veuillez Verifier les champs"));
                return null;
            }
            @Override protected void scheduled() {
                new ProgressTaskBinder(MainAffaireFormController.getInstance().getCheckProgressBar(),this);
                // desactiver le boutton
            }
            @Override protected void succeeded() {
                // re-activer le boutton
            }
        });
    }

    private void showErrorOn(HBox box, Label errLabel){
        box.getStyleClass().add("boxError");
        errLabel.setVisible(true);
    }

    private boolean isOk(String result){
        return true;
//        String[] split = result.split(";");
//        if (split.length==4){
//            int affStatus = Integer.valueOf(split[0]);
//            int ordStatus = Integer.valueOf(split[1]);
//            int repStatus = Integer.valueOf(split[2]);
//            int jtrStatus = Integer.valueOf(split[3]);
//            DossierFormController instance = DossierFormController.getInstance();
//            if (affStatus != 0) showErrorOn(instance.getNumAffBox(),instance.getErrorNumAffaireLabel());
//            if(ordStatus != 0) showErrorOn(instance.getOrdonanceBox(), instance.getErrorNumOrdonanceLabel());
//            if (repStatus != 0) showErrorOn(instance.getReperageBox(), instance.getErrorNumReperageLabel());
//            if (jtrStatus != 0) showErrorOn(instance.getJtrBox(), instance.getErrorNumJtrLabel());
//            Boolean value = (jtrStatus == 0 && repStatus == 0 && ordStatus == 0 && affStatus == 0);
//            return value;
//        }
//        return false;
    }

    public static void executeUpdate(Pane pane,Label newLabel,Label oldLabel){
            new FadeIn(pane).play();
            pane.toFront();
            if (oldLabel!=null) oldLabel.getStyleClass().removeAll("navLabel");
            if (newLabel!=null) newLabel.getStyleClass().add("navLabel");
    }

    public static void show(Pane pane){
        new FadeIn(pane).play();
        pane.toFront();
    }

    @Override
    public void handle(ActionEvent event) {
        MainAffaireFormController instance = MainAffaireFormController.getInstance();
        String id = button.getId();
        System.out.println(id);
        boolean isFolderNextBtn = id.equals("folderNextBtn");
        boolean isDmdPrevBtn = id.equals("dmdPrevBtn");
        if (isDmdPrevBtn || isFolderNextBtn ){
            // DOSSIER NEXT
            if (isFolderNextBtn){
                checkFolderPropretiesForm();
                return;
            }
            // PIECE JOINTE NEXT
          //  if (isAttachNextBtn) PieceJointeFormController.getInstance().initTextArea();
            // DEMANDEUR PREV
            if (MainAffaireFormController.getTypeDemande() == TypeDemande.ACQUISITION){
                    AnchorPane typeDemandeurPanel = instance.getTypeDemandeurPanel();
                    executeUpdate(typeDemandeurPanel,newLabel,oldLabel);
                    return;
            }
            executeUpdate(pane,newLabel,oldLabel);
        }
        executeUpdate(pane,newLabel,oldLabel);
    }
}
