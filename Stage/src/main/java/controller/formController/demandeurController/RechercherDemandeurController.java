package controller.formController.demandeurController;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Enum.TypeDemandeur;
import model.pojo.business.PersonneMorale;
import view.Helper.Other.AutoCompleteCombobox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import view.Model.ViewObject.RepresentantForView;

import java.net.URL;
import java.util.ResourceBundle;

public class RechercherDemandeurController implements Initializable{

    @Override public void initialize(URL location, ResourceBundle resources) {
        rechercherDemandeurController = this;
        physiqueInfo.toFront();
        initSearchApplicantCombobox();
        initNewReprensenantBtn();
        initJfxTreeTableView();
        stackpane.visibleProperty().bind(demandeurCombobox.valueProperty().isNotNull());
    }

    private void initNewReprensenantBtn(){
        newRepresentantBtn.setOnAction(event ->{
            selectNewTabAndShowMoralForm();
            initSelectedApplicant();
            setVerticalScrollbarToRepresentantForm();
        });
        newRepresentantBtn.disableProperty().bind(representantTreeTableView.currentItemsCountProperty().isNotEqualTo(0));
    }

    private void initSelectedApplicant(){
            PersonneMorale selectedPersonneMorale = demandeurCombobox.getValue();
            NouveauDemandeurController instance = NouveauDemandeurController.getInstance();
            initAndDisableTextField(instance.getRaisonSocial(),selectedPersonneMorale.getNom());
            initAndDisableTextField(instance.getSiegeSocial(),selectedPersonneMorale.getAdresse());
            initAndDisableTextField(instance.getTelPersonneMorale(),selectedPersonneMorale.getNumTel());
            initAndDisableTextField(instance.getEmailPersonneMorale(),selectedPersonneMorale.getEmail());
            initAndDisableTextField(instance.getNationaliteMorale(),selectedPersonneMorale.getNationalite());
    }

    private void selectNewTabAndShowMoralForm(){
        NouveauDemandeurController.getInstance().getMoralPane().toFront();
        MainDemandeurFormController instance = MainDemandeurFormController.getInstance();
        JFXTabPane applicantTabPane = instance.getApplicantTabPane();
        Tab newTab = instance.getNewTab();
        applicantTabPane.getSelectionModel().select(newTab);
    }

    private void setVerticalScrollbarToRepresentantForm(){
        NouveauDemandeurController instance = NouveauDemandeurController.getInstance();
        instance.getMoralPane().setVvalue(1);
    }

    private void initAndDisableTextField(TextInputControl textInputControl,String value){
        textInputControl.setDisable(true);
        textInputControl.setText(value);
    }


    private void initSearchApplicantCombobox(){
        demandeurCombobox.setCellFactory(param -> new DemandeurComboboxCell());
        new AutoCompleteCombobox<PersonneMorale>(demandeurCombobox,personneMorale -> {
            String text = demandeurCombobox.getEditor().getText().toLowerCase();
           if (personneMorale.getNom().startsWith(text)) return true;
           return false;
        });
    }

    private class DemandeurComboboxCell extends ListCell<PersonneMorale>{
        private  final Image moralImg = new Image(getClass().getResourceAsStream("/img/user_group.png"));
        private final Image physiqueImg = new Image(getClass().getResourceAsStream("/img/account_40px.png"));
        @Override protected void updateItem(PersonneMorale item, boolean empty) {
            super.updateItem(item, empty);
            if (item==null && empty){
                setText(null);
                setGraphic(null);
            }else{
                setText(item.getNom());
                setGraphic(initImageView(item));
            }
        }
        private ImageView initImageView(PersonneMorale item){
            ImageView imageView = new ImageView();
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            if (item.getType().equals(TypeDemandeur.PERSONNE_PHYSIQUE)) imageView.setImage(physiqueImg);
            else imageView.setImage(moralImg);
            return imageView;
        }
    }

    public static RechercherDemandeurController getInstance() {
        return rechercherDemandeurController;
    }
    public Label getNationalite() {
        return nationalite;
    }
    public JFXComboBox<PersonneMorale> getDemandeurCombobox() {
        return demandeurCombobox;
    }
    public JFXButton getNewRepresentantBtn() {
        return newRepresentantBtn;
    }

    public Label getNomMorale() {
        return nomMorale;
    }
    public Label getSiegeMorale() {
        return siegeMorale;
    }
    public Label getEmailMorale() {
        return emailMorale;
    }
    public Label getTelMorale() {
        return telMorale;
    }
    public Label getNomDemandeurPhysique() {
        return nomDemandeurPhysique;
    }
    public Label getAdressePhysique() {
        return adressePhysique;
    }
    public Label getEmailPhysique() {
        return emailPhysique;
    }
    public Label getTelPhysique() {
        return telPhysique;
    }
    public ScrollPane getMoralInfo() {
        return moralInfo;
    }
    public AnchorPane getPhysiqueInfo() {
        return physiqueInfo;
    }
    private static RechercherDemandeurController rechercherDemandeurController;
    public JFXProgressBar getRepresentantProgress() {
        return representantProgress;
    }
    public ObservableList<RepresentantForView> getRepresentantForViews() {
        return representantForViews;
    }
    public JFXTreeTableView<RepresentantForView> getRepresentantTreeTableView() {
        return representantTreeTableView;
    }

    @FXML private JFXProgressBar representantProgress;
    @FXML private JFXComboBox<PersonneMorale> demandeurCombobox;
    @FXML private StackPane stackpane;
    @FXML private JFXTreeTableView<RepresentantForView> representantTreeTableView;
    @FXML private TreeTableColumn<RepresentantForView,String> nomRepresentantTreeTbColum;
    @FXML private TreeTableColumn<RepresentantForView,String> numAffTreeTbColumn;
    @FXML private TreeTableColumn<RepresentantForView,String> dateTreeTbColumn;
    private void initJfxTreeTableView(){
     numAffTreeTbColumn.setCellValueFactory(param -> new ObservableValue<String>() {
         @Override
         public void addListener(ChangeListener<? super String> listener) {

         }

         @Override
         public void removeListener(ChangeListener<? super String> listener) {

         }

         @Override
         public String getValue() {
             return param.getValue().getValue().getNumeroAffaire();
         }

         @Override
         public void addListener(InvalidationListener listener) {

         }

         @Override
         public void removeListener(InvalidationListener listener) {

         }
     });
     dateTreeTbColumn.setCellValueFactory(param -> new ObservableValue<String>() {
         @Override
         public void addListener(ChangeListener<? super String> listener) {

         }

         @Override
         public void removeListener(ChangeListener<? super String> listener) {

         }

         @Override
         public String getValue() {
            return param.getValue().getValue().getDateFormulation().toString();
         }

         @Override
         public void addListener(InvalidationListener listener) {

         }

         @Override
         public void removeListener(InvalidationListener listener) {

         }
     });
     nomRepresentantTreeTbColum.setCellValueFactory(param -> new ObservableValue<String>() {
         @Override
         public void addListener(ChangeListener<? super String> listener) {

         }

         @Override
         public void removeListener(ChangeListener<? super String> listener) {

         }

         @Override
         public String getValue() {
            return param.getValue().getValue().getPersonneMorale().getNom();
         }

         @Override
         public void addListener(InvalidationListener listener) {

         }

         @Override
         public void removeListener(InvalidationListener listener) {

         }
     });
     TreeItem<RepresentantForView> representantTreeRoot = new RecursiveTreeItem<RepresentantForView>(representantForViews,RecursiveTreeObject::getChildren);
     representantTreeTableView.setRoot(representantTreeRoot);
     representantTreeTableView.setShowRoot(false);
    }
    private static ObservableList<RepresentantForView> representantForViews = FXCollections.observableArrayList();
    // MORALE
    @FXML private JFXButton newRepresentantBtn;
    @FXML private Label nomMorale;
    @FXML private Label siegeMorale;
    @FXML private Label emailMorale;
    @FXML private Label telMorale;
    @FXML private ScrollPane moralInfo;
    JFXSnackbar
    // PHYSIQUE
    @FXML private Label nomDemandeurPhysique;
    @FXML private Label adressePhysique;
    @FXML private Label emailPhysique;
    @FXML private Label telPhysique;
    @FXML private Label nationalite;
    @FXML private AnchorPane physiqueInfo;
}
