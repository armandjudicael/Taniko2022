package View.helper;

import Main.Main;
import Model.Pojo.PieceJointe;
import View.Model.PieceJointeForView;
import com.jfoenix.controls.JFXButton;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.apache.commons.io.FileUtils;
import org.controlsfx.control.PopOver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class AttachementPopup extends PopOver implements Initializable {

    public AttachementPopup(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View/Other/AttachementPopOver.fxml"));
            loader.setController(this);
            VBox load = (VBox)loader.load();
            this.setContentNode(load);
            this.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void initialize(URL location, ResourceBundle resources){
        attachementPopup = this;
        viewbtn.setOnAction(this::viewAttachement);
        downBtn.setOnAction(this::downloadAttachement);
    }

    public void showPopup(JFXButton button){
        ImageView graphic = (ImageView)button.getGraphic();
        PieceJointeForView selectedAttachementForView = (PieceJointeForView)button.getParent();
        selectedAttachement = selectedAttachementForView.getPieceJointe();
        this.show(graphic);
    }

    public static AttachementPopup getInstance() {
        if (attachementPopup == null)
            attachementPopup = new AttachementPopup();
        return attachementPopup;
    }

    private void downloadAttachement(ActionEvent event){
        this.hide();

    }

    private void viewAttachement(ActionEvent event){
        this.hide();
        File selectedFile = createFileFromInputstream();
        if (selectedFile!=null && selectedFile.isFile()){
            HostServices hostServices = Main.getMainApplication().getHostServices();
            hostServices.showDocument(selectedFile.toURI().toString());
        }else{

        }
    }

    private File createFileFromInputstream(){
        try {
            InputStream valeur = selectedAttachement.getInputStream();
            if (valeur!=null){
                String extension = selectedAttachement.getExtensionPiece();
                String fileName = selectedAttachement.getDescription();
                String path = fileName+"."+extension;
                File file = new File(path);
                FileUtils.copyInputStreamToFile(valeur,file);
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static PieceJointe selectedAttachement;
    private static AttachementPopup attachementPopup;
    private final String path = "";
    @FXML private JFXButton viewbtn;
    @FXML private JFXButton downBtn;
}
