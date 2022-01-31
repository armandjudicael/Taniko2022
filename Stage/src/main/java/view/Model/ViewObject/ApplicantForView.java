package view.Model.ViewObject;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import controller.formController.demandeurController.MainDemandeurFormController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.skin.ListViewSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.Enum.TypeDemandeur;
import model.pojo.business.other.Personne;
import model.pojo.business.other.PersonnePhysique;

import java.util.Objects;

public class ApplicantForView extends AnchorPane implements EventHandler<ActionEvent>{
    private static VBox viewBox;
    private Personne personne;
    private JFXRadioButton radioButton;
    private static final ToggleGroup TOOGLE_GROUP = new ToggleGroup();
    public ApplicantForView(Personne personne){
        this.personne = personne;
        initAnchorpane();
    }
    public ApplicantForView(Personne personne,VBox vbox){
        this.personne = personne;
        initAnchorpane();
    }
    private ImageView initImageView(Personne personne) {
        ImageView imageView = createImageView(personne);
        AnchorPane.setLeftAnchor(imageView,7.0);
        AnchorPane.setTopAnchor(imageView,11.0);
        AnchorPane.setBottomAnchor(imageView,11.0);
        return imageView;
    }

    private Label initLabel(Personne personne) {
        Label label = createLabel(personne);
        AnchorPane.setLeftAnchor(label,50.0);
        AnchorPane.setTopAnchor(label,0.0);
        AnchorPane.setBottomAnchor(label,0.0);
        AnchorPane.setRightAnchor(label,95.0);
        return label;
    }

    private JFXRadioButton initJfxRadioButton() {
        // radio
        JFXRadioButton radioButton = createJfxRadioButton();
        AnchorPane.setTopAnchor(radioButton,21.0);
        AnchorPane.setBottomAnchor(radioButton,22.0);
        AnchorPane.setRightAnchor(radioButton,-1.0);
        return  radioButton;
    }

    private JFXButton initJFXButton() {
        // Button
        JFXButton button = createJFXButton();
        AnchorPane.setRightAnchor(button,45.0);
        AnchorPane.setTopAnchor(button,14.0);
        AnchorPane.setBottomAnchor(button,14.0);
    return button;
    }

    private static String style = ApplicantForView.class.getResource("/css/applicantCss.css").toExternalForm();

    private void initAnchorpane(){
        this.setPrefWidth(Region.USE_COMPUTED_SIZE);
        this.setCursor(Cursor.HAND);
        this.setId("applicantForView");
        this.setPrefHeight(60);
        this.getStylesheets().add(style);
        this.setOnMouseClicked(event -> {
            try {
                ObjectFieldView.initObjectPosition(viewBox,personne);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        ImageView imageView = initImageView(personne);
        Label label = initLabel(personne);
        radioButton = initJfxRadioButton();
        JFXButton button = initJFXButton();
        this.getChildren().addAll(imageView,label,radioButton,button);
    }

    private ImageView createImageView(Personne personne){
        TypeDemandeur type = personne.getType();
        boolean isPersonMorale = type.equals(TypeDemandeur.PERSONNE_MORALE_PRIVE) || type.equals(TypeDemandeur.PERSONNE_MORALE_PUBLIQUE);
        ImageView imageView = new ImageView(isPersonMorale ? new Image(Objects.requireNonNull(ApplicantForView.class.getResourceAsStream("/img/people_50px.png"))) :
                new Image(ApplicantForView.class.getResourceAsStream("/img/male_user_40px.png")));
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setFitHeight(38);
        imageView.setFitWidth(44);
        return imageView;
    }

    private Label createLabel(Personne personne){
        Label label = new Label(personne.getNom());
        label.setAlignment(Pos.CENTER);
        label.setFont(new Font(14));
        return label;
    }

    private JFXRadioButton createJfxRadioButton(){
        JFXRadioButton radioButton = new JFXRadioButton();
        radioButton.setToggleGroup(TOOGLE_GROUP);
        radioButton.setSelected(true);
        radioButton.setCursor(Cursor.HAND);
        radioButton.setPrefWidth(15);
        radioButton.setPrefHeight(15);
        radioButton.setLayoutX(420);
        radioButton.setLayoutY(22);
        return radioButton;
    }

    private JFXButton createJFXButton(){
        JFXButton button = new JFXButton();
        ImageView imageView = initImageView();
        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: #f14858;");
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.setLayoutX(299);
        button.setLayoutY(16);
        button.setOnAction(this);
        button.setPrefWidth(30);
        button.setPrefHeight(30);
        return button;
    }

    private ImageView initImageView() {
        Image image = new Image(this.getClass().getResourceAsStream("/img/remove_50px.png"));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(21.0);
        imageView.setFitHeight(23.0);
        imageView.setPickOnBounds(true);
        return imageView;
    }

    public Personne getPersonne() {
        return personne;
    }
    public void setPersonne(Personne personne) {
        this.personne = personne;
    }
    @Override public void handle(ActionEvent event){
        VBox parent = (VBox)this.getParent();
        parent.getChildren().remove(this);
        viewBox.getChildren().clear();
    }
    public JFXRadioButton getRadioButton() {
        return radioButton;
    }
    public static VBox getViewBox() {
        return viewBox;
    }
    public static void setViewBox(VBox viewBox) {
        ApplicantForView.viewBox = viewBox;
    }
}
