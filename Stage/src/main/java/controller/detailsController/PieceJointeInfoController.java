package controller.detailsController;

import controller.viewController.FolderViewController;
import model.other.MainService;
import model.pojo.business.attachement.Attachement;
import view.Helper.Attachement.*;
import view.Model.ViewObject.AttachementForView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;

import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PieceJointeInfoController implements Initializable, Serializable{

    public PieceJointeInfoController(){ }
    @Override public void initialize(URL location, ResourceBundle resources){
      pieceJointeInfoController = this;
      initButtonAction();
      initPieceJointeSearchTexfield();
    }

    private void initButtonAction(){
        ObservableList<Node> children = pjTilepane.getChildren();
        new AttachementCreatorButton(addAttachOutBtn,children,true);
        new AttachementCreatorButton(addAttachInBtn,children,true);
        new AttachementRemoverButton(delPieceBtn,children,true);
        new AttachementDownloaderButton(downBtn,true);
        new AttachementCheckerButton(checkBtn,children);
        refreshBtn.setOnAction(this::refreshAttachementList);
        BooleanBinding emptyAttachement = Bindings.isEmpty(children);
        delPieceBtn.disableProperty().bind(emptyAttachement);
        downBtn.disableProperty().bind(emptyAttachement);
        checkBtn.disableProperty().bind(emptyAttachement);
        initDeleteSearchBtn();
    }

    private void initDeleteSearchBtn(){
        deleteSearch.visibleProperty().bind(pjSearchTextField.textProperty().isNotEmpty());
        deleteSearch.setOnAction(event ->pjSearchTextField.clear());
    }
    private void initPieceJointeSearchTexfield(){
        pjSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Node> children = pjTilepane.getChildren();
            if (newValue.isEmpty()){
                children.setAll(attachementList);
            }else {
                List<Node> collect = children.stream().filter(node -> {
                    AttachementForView attachementForView = (AttachementForView) node;
                    Attachement attachement = attachementForView.getAttachement();
                    String attachementName = attachement.getDescription().toLowerCase().trim();
                    String finalNewValue = newValue.toLowerCase().trim();
                    if (attachementName.startsWith(finalNewValue))
                        return true;
                    else return false;
                }).collect(Collectors.toList());
                if (!collect.isEmpty())
                    children.setAll(collect);
                else children.clear();
            }
        });
    }
    public static PieceJointeInfoController getInstance() {
        return pieceJointeInfoController;
    }
    private void refreshAttachementList(ActionEvent actionEvent){
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                FolderViewController.getInstance().initAttachementView(AffairDetailsController.getAffaire());
                return null;
            }
            @Override protected void scheduled() {
                attachementProgress.progressProperty().unbind();
                attachementProgress.visibleProperty().unbind();
                attachementProgress.visibleProperty().bind(this.runningProperty());
                attachementProgress.progressProperty().bind(this.progressProperty());
            }
        });
    }
    public Tab getPieceJointeTab() {
        return pieceJointeTab;
    }
    public JFXButton getCheckBtn() {
        return checkBtn;
    }
    private static PieceJointeInfoController pieceJointeInfoController;
    public TilePane getPjTilepane() { return pjTilepane; }
    public static  ObservableList<Node> getAttachementList() {
        return attachementList;
    }
    private static ObservableList<Node> attachementList = FXCollections.observableArrayList();
    public JFXProgressBar getAttachementProgress() { return attachementProgress; }

    @FXML private AnchorPane pieceJointePanel;
    @FXML private TilePane pjTilepane;
    @FXML private JFXButton delPieceBtn;
    @FXML private JFXButton searchbutton;
    @FXML private TextField pjSearchTextField;
    @FXML private JFXButton deleteSearch;
    @FXML private JFXButton downBtn;
    @FXML private JFXButton refreshBtn;
    @FXML private JFXButton checkBtn;
    @FXML private JFXButton addAttachInBtn;
    @FXML private JFXButton addAttachOutBtn;
    @FXML private JFXProgressBar attachementProgress;
    @FXML private Tab pieceJointeTab;
}
