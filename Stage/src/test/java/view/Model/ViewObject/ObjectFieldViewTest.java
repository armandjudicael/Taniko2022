package view.Model.ViewObject;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Enum.SituationMatrimoniale;
import model.pojo.business.other.PersonnePhysique;
import org.junit.Test;
import view.Helper.Other.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

import static org.junit.Assert.*;

public class ObjectFieldViewTest extends Application implements Initializable {
   private static ObjectFieldViewTest objectFieldViewTest;
   public static ObjectFieldViewTest getObjectFieldViewTest() {
        return objectFieldViewTest;
    }
    @Test public void create() throws IllegalAccessException {
//        ObservableList<ObjectFieldView> list = addItem();
//        assertTrue(!list.isEmpty());
    }

    private ObservableList<ObjectFieldView> addItem() throws IllegalAccessException {
        PersonnePhysique ph = new PersonnePhysique();
        ph.setNom("RATOMBOTANA Armand Judcael");
        ph.setAdresse("Ambalamanasy carreau 1");
        ph.setLot("60");
        ph.setMere("VOANGY");
        ph.setPere("TOMBO");
        ph.setLieuDelivranceCin("Tamatave");
        ph.setNumCin("3A3A36666663T63");
        ph.setSexe("Masculin");
        ph.setProfession(" Full stack java dellopper ");
        ObservableList<ObjectFieldView> list = ObjectFieldView.create(ph);
        return list;
    }

    public ObjectFieldViewTest() {
        super();
    }

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println(" Lancement de l'application ");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println(" Arret de l'application ");
    }

    private static AnchorPane acceuilPane;
    private static AnchorPane affairePane;

    @Override
    public void start(Stage primaryStage) throws Exception{
//        HBox buttonHbox = new HBox();
//        StackPane stackPane = new StackPane();
//        // ACCEUIL
//        JFXButton acceuilButton = new JFXButton("acceuil");
//        acceuilButton.setOnAction(event -> {
//           if (acceuilPane ==null){
//               acceuilPane = new AnchorPane(new Label(" acceuil button clicked "));
//               acceuilPane.setStyle("-fx-background-color: white");
//               stackPane.getChildren().add(acceuilPane);
//           }
//           acceuilPane.toFront();
//        });
//        // MAINFOLDERVIEW
//        JFXButton mainFolderBtn = new JFXButton("affaire");
//        mainFolderBtn.setOnAction(event -> {
//            if (affairePane ==null){
//                affairePane = new AnchorPane(new Label(" affaire button clicked "));
//                affairePane.setStyle("-fx-background-color: white");
//                stackPane.getChildren().add(affairePane);
//            }
//            affairePane.toFront();
//        });
//        buttonHbox.getChildren().addAll(acceuilButton,mainFolderBtn);
//        VBox box = new VBox();
//        ListView<ObjectFieldView> listView = new ListView(addItem());
//        listView.setPrefWidth(200);
//        box.setAlignment(Pos.TOP_CENTER);
//        box.getChildren().addAll(stackPane,listView,buttonHbox);
        Parent parent = (Parent)new ViewFactory<ObjectFieldViewTest>().create("/fxml/View/Other/demandeurDetailsView.fxml",this);
        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.setTitle(" Dynamic field view ");
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

    @Override public void initialize(URL location, ResourceBundle resources){
        objectFieldViewTest = this;
        PersonnePhysique ph = new PersonnePhysique();
        ph.setNom("RATOMBOTANA Armand Judcael");
        ph.setAdresse("Ambalamanasy carreau 1");
        ph.setLot("60");
        ph.setMere("VOANGY");
        ph.setPere("TOMBO");
        ph.setLieuDelivranceCin("Tamatave");
        ph.setNumCin("3A3A36666663T63");
        ph.setSexe("Masculin");
        ph.setProfession(" Full stack java dellopper ");
        ph.setNumTel("0340588519");
        ph.setEmail("armandjudicaelratombotiana@gmail.com");
        ph.setSituationMatrimoniale(SituationMatrimoniale.CELIBATAIRE);

        PersonnePhysique ph1 = new PersonnePhysique();
        ph1.setNom("RANDRIANARISON VOAHANGINIRINA NOELINE");
        ph1.setAdresse("Ambalamanasy carreau 1");
        ph1.setLot("60");
        ph1.setPere("RANDRIANARISON  Jean");
        ph1.setMere("KAMISY Juliette");
        ph1.setLieuDelivranceCin("Tamatave");
        ph1.setNumCin("3A3A36666663T63");
        ph1.setSexe("Femini");
        ph1.setProfession("Menagere");
        ph1.setNumTel("0348023827");
        ph1.setEmail("kamisyJulliette@gmail.com");
        ph1.setSituationMatrimoniale(SituationMatrimoniale.VEUVE);

        ApplicantForView applicantForView = new ApplicantForView(ph);
        ApplicantForView appV1 = new ApplicantForView(ph1);

        VBox.setVgrow(applicantForView, Priority.NEVER);
        VBox.setVgrow(appV1,Priority.NEVER);
        ApplicantForView.setViewBox(fieldViewBox);
        applicantForViewBox.getChildren().addAll(applicantForView,appV1);
    }
    @FXML private VBox applicantForViewBox;
    @FXML private VBox fieldViewBox;
}