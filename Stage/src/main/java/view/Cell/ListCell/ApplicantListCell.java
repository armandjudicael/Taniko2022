package view.Cell.ListCell;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.Enum.TypeDemandeur;
import model.pojo.business.other.Personne;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ApplicantListCell extends ListCell<Personne> implements Initializable{

    private final Image PHYSIQUE_IMG = new Image(getClass().getResourceAsStream("/img/male_user_50px.png"));
    private final Image MORALE_IMG = new Image(getClass().getResourceAsStream("/img/people_50px.png"));
    private static final String STYLE = DispatchListcell.class.getResource("/css/applicantListCell.css").toExternalForm();

    @Override protected void updateItem(Personne personne, boolean empty){
        super.updateItem(personne,empty);
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        if (personne==null && empty) setText(null);
        else{
            AnchorPane pane = initLoader(personne);
            initData(personne);
            this.setGraphic(pane);
        }
    }

    private AnchorPane initLoader(Personne personne){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View/Other/demandeurListViewItem.fxml"));
        loader.setController(this);
        try {
            return (AnchorPane)loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initData(Personne personne){
        TypeDemandeur type = personne.getType();
        boolean isPersonMorale = type.equals(TypeDemandeur.PERSONNE_MORALE_PRIVE) || type.equals(TypeDemandeur.PERSONNE_MORALE_PUBLIQUE);
        personneImageView.setImage( isPersonMorale ? MORALE_IMG : PHYSIQUE_IMG);
        // Desactiver radio si personne morale
        titulaireRadio.setVisible(!isPersonMorale);
        nomPersonne.setText(personne.getNom());
    }

    @Override public void initialize(URL location, ResourceBundle resources) {
       delDemandeurBtn.setOnAction(event -> {

       });
    }

    @FXML private JFXButton delDemandeurBtn;
    @FXML private ImageView personneImageView;
    @FXML private JFXRadioButton titulaireRadio;
    @FXML private Label nomPersonne;
}
