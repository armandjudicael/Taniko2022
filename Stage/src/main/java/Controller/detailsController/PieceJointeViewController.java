package Controller.detailsController;

import View.helper.AttachementCreatorButton;
import View.helper.AttachementRemoverButton;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import java.net.URL;
import java.util.ResourceBundle;

public class PieceJointeViewController implements Initializable {


    @Override public void initialize(URL location,ResourceBundle resources){
      pieceJointeViewController = this;
      initButtonAction();
    }

    private void initButtonAction(){
        ObservableList<Node> children = pjTilepane.getChildren();
        new AttachementCreatorButton(newPieceBtn,children,true);
        new AttachementRemoverButton(delPieceBtn,children,true);
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
}
