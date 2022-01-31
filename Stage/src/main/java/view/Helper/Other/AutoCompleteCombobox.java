package view.Helper.Other;
import com.jfoenix.controls.JFXProgressBar;
import controller.formController.otherFormController.MainAffaireFormController;
import model.Enum.TypeDemandeur;
import model.other.MainService;
import model.pojo.business.other.PersonneMorale;
import view.Model.ViewObject.ApplicantForView;
import view.Model.ViewObject.RepresentantForView;
import controller.formController.demandeurController.MainDemandeurFormController;
import controller.formController.demandeurController.RechercherDemandeurController;
import dao.DaoFactory;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.util.StringConverter;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AutoCompleteCombobox<T> implements EventHandler<KeyEvent>{

    private JFXComboBox<T> comboBox;
    private ObservableList<T> comboboxItemsCopy = FXCollections.observableArrayList();
    private Predicate<T> comboboxPredicate;
    private static JFXProgressBar representantProgress;

    public AutoCompleteCombobox(JFXComboBox<T> tComboBox, Predicate<T> predicate){
        this.comboBox = tComboBox;
        this.comboboxPredicate = predicate;
        this.comboBox.visibleRowCountProperty().bind(Bindings.size(this.comboBox.getItems()).multiply(20));
        this.doAutoCompleteBox();
    }

    private void initComboboxConverter(){
        this.comboBox.setConverter(new StringConverter<T>(){
            @Override
            public String toString(T object) {
                return object!=null ? object.toString() : "";
            }
            @Override
            public T fromString(String stringValue) {
                Optional<T> first = comboBox.getItems().stream().filter(t -> t.toString().equals(stringValue)).findFirst();
                return first.orElse(null);
            }
        });
    }

    private void initComboboxEditorFocusedProperty(){
        this.comboBox.getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                //mean on focus
                this.comboBox.show();
            }
        });
    }

    private void doAutoCompleteBox(){
        this.comboBox.setEditable(true);
        initComboboxConverter();
        initComboboxEditorFocusedProperty();
        initComboboxValueProperty();
        initComboboxShowingProperty();
        initComboboxMouseClicked();
        this.comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            moveCaret(this.comboBox.getEditor().getText().length());
        });
        initComboboxKeyEvent();
    }

    private void initComboboxKeyEvent() {
        this.comboBox.setOnKeyPressed(t -> comboBox.hide());
        this.comboBox.setOnKeyReleased(AutoCompleteCombobox.this);
    }

    private void initComboboxMouseClicked() {
        this.comboBox.getEditor().setOnMouseClicked(event ->{
            if(event.getButton().equals(MouseButton.PRIMARY)){
                if(event.getClickCount() == 2) return;
            }
            this.comboBox.show();
        });
    }

    private void initComboboxShowingProperty() {
        this.comboBox.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                String text = comboBox.getEditor().getText();
                if (comboboxItemsCopy.size()>0 && text.isEmpty()){
                    ObservableList<T> items = comboBox.getItems();
                    items.setAll(comboboxItemsCopy);
                }
            }
        });
    }

    private void initComboboxValueProperty() {
        this.comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (comboBox.getId().equals("demandeurCombobox") && newValue!=null ){
                PersonneMorale newValue1 = (PersonneMorale) newValue;
                if(newValue1.getType().equals(TypeDemandeur.PERSONNE_MORALE_PRIVE)) showMoraleValueDetails(newValue1)  ;
                else showPhysiqueValueDetails(newValue1);
            }
        });
    }

    private void showPhysiqueValueDetails(PersonneMorale pm){
        RechercherDemandeurController instance = RechercherDemandeurController.getInstance();
        instance.getNationalite().setText(pm.getNationalite());
        instance.getNomDemandeurPhysique().setText(pm.getNom());
        instance.getTelPhysique().setText(pm.getNumTel());
        instance.getEmailPhysique().setText(pm.getEmail());
        instance.getAdressePhysique().setText(pm.getAdresse());
    }

    private void showMoraleValueDetails(PersonneMorale pm){
        RechercherDemandeurController instance = RechercherDemandeurController.getInstance();
        instance.getEmailMorale().setText(pm.getEmail());
        instance.getNomMorale().setText(pm.getNom());
        instance.getTelMorale().setText(pm.getNumTel());
        instance.getSiegeMorale().setText(pm.getAdresse());
        getAndShowAllRepresentantOf(pm);
    }

    private void getAndShowAllRepresentantOf(PersonneMorale pm){
        // LANCEMENT DE LA RECHERCHE DES REPRESENTANTS
        MainService.getInstance().launch(new Task<Void>() {
            @Override protected Void call() throws Exception {
                initAllRepresentant(pm);
                return null;
            }
            @Override protected void scheduled(){
                if (representantProgress ==null) representantProgress = RechercherDemandeurController.getInstance().getRepresentantProgress();
                representantProgress.visibleProperty().bind(this.runningProperty());
                representantProgress.progressProperty().unbind();
                representantProgress.progressProperty().bind(this.progressProperty());
            }
            @Override protected void failed() {
                this.getException().printStackTrace();
            }
        });
    }

    private void initAllRepresentant(PersonneMorale personneMorale){
            if (personneMorale.getType().equals(TypeDemandeur.PERSONNE_MORALE_PRIVE)){
                ObservableList<RepresentantForView> allRepresentant = personneMorale.getAllRepresentant();
                if (!allRepresentant.isEmpty()){
                   // UPDATE THE LISTVIEW
                    ObservableList<ApplicantForView> items = RechercherDemandeurController.getInstance().getRepresentantList().getItems();
                  //  Platform.runLater(() -> representantForViews.setAll(allRepresentant));
                }
            }
    }

    private ObservableList<RepresentantForView> filterTreeItem(ObservableList<RepresentantForView> observableList){
        ObservableList<RepresentantForView> filteredTreeItemList = FXCollections.observableArrayList();
        if (!observableList.isEmpty()){
            if (observableList.size() > 1){
                observableList.forEach(representantForView -> {
                    List<RepresentantForView> collect = observableList.stream()
                            .filter(itemFilter -> itemFilter.getPersonneMorale().getId() == representantForView.getPersonneMorale().getId() && !representantForView.equals(itemFilter))
                            .collect(Collectors.toList());
                    if (!collect.isEmpty()){
                        // AJOUTER LES ELEMENTS ENFANTS
                        representantForView.setChildren(FXCollections.observableList(collect));
                        filteredTreeItemList.add(representantForView);
                    }
                });
            }
        }
        if (filteredTreeItemList!=null && !filteredTreeItemList.isEmpty()) return filteredTreeItemList;
        return observableList;
     }

    @Override public void handle(KeyEvent event) {
        if ( event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN
                || event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
                || event.getCode() == KeyCode.HOME
                || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB){
            return;
        }
        if(event.getCode() == KeyCode.BACK_SPACE){
            String str = this.comboBox.getEditor().getText();
            if (str != null && str.length() > 0) str = str.substring(0, str.length() - 1);
            if(str != null){
                this.comboBox.getEditor().setText(str);
                moveCaret(str.length());
            }
            this.comboBox.getSelectionModel().clearSelection();
        }
        if(event.getCode() == KeyCode.ENTER ){
            this.comboBox.hide();
            return;
        }else {
            String text = this.comboBox.getEditor().getText().toLowerCase().trim();
            if (!text.isEmpty()){
                ObservableList<T> items = this.comboBox.getItems();
                launchDataSearch(items,comboboxItemsCopy,text);
            }
        }
    }

     void launchDataSearch(ObservableList<T> items, ObservableList<T> copyItems, String numOrName){
        if(items.isEmpty()){
            getDataFromDB(items,copyItems,numOrName);
        }else{
            FilteredList<T> filteredComboboxItem = copyItems.filtered(comboboxPredicate);
            if( filteredComboboxItem.size()>0){
                items.setAll(filteredComboboxItem);
                comboBox.show();
            }else getDataFromDB(items,copyItems,numOrName);
        }
    }

    private void getDataFromDB(ObservableList items, ObservableList copyItems, String numOrName){
        ObservableList observableList = fetchData(items,numOrName);
        if(observableList.size()>0){
            items.setAll(observableList);
            this.comboBox.show();
            copyItems.addAll(observableList);
        }else this.comboBox.hide();
    }

     private ObservableList fetchData(ObservableList items, String numOrName) {
        switch (comboBox.getId()){
            case "redactorCombobox": return DaoFactory.getUserDao().filterUser(numOrName, items);
            case "titleCombobox": return DaoFactory.getTitreDao().findByNumOrName(numOrName);
            case "connexeCombobox": return DaoFactory.getDossierDao().checkConnexAffaireBy(numOrName);
            case "demandeurCombobox":{
                MainDemandeurFormController instance = MainDemandeurFormController.getInstance();
                if (MainAffaireFormController.getTypeDemandeur().equals(TypeDemandeur.PERSONNE_PHYSIQUE)) return DaoFactory.getDemandeurMoraleDao().findbyName(numOrName, TypeDemandeur.PERSONNE_PHYSIQUE);
                else return DaoFactory.getDemandeurMoraleDao().findbyName(numOrName, TypeDemandeur.PERSONNE_MORALE_PRIVE);
            }
        }
        return null;
    }

    private void moveCaret(int textLength) {
        this.comboBox.getEditor().positionCaret(textLength);
    }
    /**
     * Invoked when a specific event of the type for which this handler is
     * registered happens.
     *
     * @param event the event which occurred
     */
}
