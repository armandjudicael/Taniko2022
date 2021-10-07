package View.Model;

import Model.Pojo.PieceJointe;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PieceJointeForView implements Initializable{

    public PieceJointeForView(PieceJointe pieceJointe, Boolean enableBtn) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View/Other/PieceJointe.fxml"));
        loader.setController(this);
        pane = (AnchorPane)loader.load();
        this.pieceJointe = pieceJointe;
        pieceOptionBtn.setVisible(enableBtn);
        size.setText(pieceJointe.getSize());
        initPieceIcon(pieceJointe.getExtensionPiece());
        pieceNom.setText(pieceJointe.getDescription());
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void initPieceIcon(String extension){
        switch (extension){
            case "pdf" :{
                pieceIcon.setImage(pdfImg);
            }break;
            case "docx":
            case "doc":{
                pieceIcon.setImage(docxImg);
            }break;
            default:{
                pieceIcon.setImage(iconImg);
            }
        }
    }

    public PieceJointe getPieceJointe() { return pieceJointe; }
    public JFXCheckBox getPieceCheckbox() {
        return pieceCheckbox;
    }
    public AnchorPane getPane() {
        return pane;
    }

    @FXML private ImageView pieceIcon;
    @FXML private JFXCheckBox pieceCheckbox;
    @FXML private JFXButton pieceOptionBtn;
    @FXML private Label pieceNom;
    @FXML private Label size;

    private PieceJointe pieceJointe;
    private AnchorPane pane;

    private static final Image docxImg =  new Image(Objects.requireNonNull(PieceJointe.class.getResourceAsStream("/img/microsoft_word_2019_50px.png"))) ;
    private static final Image pdfImg = new Image(Objects.requireNonNull(PieceJointe.class.getResourceAsStream("/img/adobe_acrobat_reader_40px.png")));
    private static final Image iconImg = new Image(Objects.requireNonNull(PieceJointe.class.getResourceAsStream("/img/image_80px.png"))) ;
}
