package Controller.detailsController;

import Model.Pojo.PieceJointe;
import View.Model.PieceJointeForView;
import View.helper.AttachementCreatorButton;
import View.helper.AttachementRemoverButton;
import View.helper.DownloaderButton;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PieceJointeViewController implements Initializable {


    @Override public void initialize(URL location,ResourceBundle resources){
      pieceJointeViewController = this;
      initButtonAction();
      initPieceJointeSearchTexfield();
    }

    private void initButtonAction(){
        ObservableList<Node> children = pjTilepane.getChildren();
        new AttachementCreatorButton(newPieceBtn,children,true);
        new AttachementRemoverButton(delPieceBtn,children,true);
        new DownloaderButton(downBtn,true);
    }

    private void initPieceJointeSearchTexfield(){
        pjSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Node> children = pjTilepane.getChildren();
            if (!newValue.isEmpty()){
                List<Node> collect = children.stream().filter(node -> {
                    PieceJointeForView pieceJointeForView = (PieceJointeForView) node;
                    PieceJointe pieceJointe = pieceJointeForView.getPieceJointe();
                    String attachementName = pieceJointe.getDescription().toLowerCase().trim();
                    String finalNewValue = newValue.toLowerCase().trim();
                    if (attachementName.startsWith(finalNewValue))
                        return true;
                    else return false;
                }).collect(Collectors.toList());
                if (!collect.isEmpty())
                    children.setAll(collect);
                else children.setAll(attachementList);
            }else children.setAll(attachementList);
        });
    }

    private static PieceJointeViewController pieceJointeViewController;
    public TilePane getPjTilepane() { return pjTilepane; }
    public static PieceJointeViewController getInstance() {
        return pieceJointeViewController;
    }

    @FXML private AnchorPane pieceJointePanel;
    @FXML private TilePane pjTilepane;
    @FXML private JFXButton newPieceBtn;
    @FXML private JFXButton delPieceBtn;
    @FXML private JFXButton searchbutton;
    @FXML private TextField pjSearchTextField;
    @FXML private JFXButton deleteSearch;
    @FXML private JFXButton downBtn;
    private static ObservableList<Node> attachementList;
}
