package view.Model.ViewObject;

import dao.AttachementDao;
import dao.DbUtils;
import javafx.concurrent.Task;
import model.Enum.NotifType;
import model.Enum.Origin;
import model.other.MainService;
import model.pojo.business.attachement.Attachement;
import view.Dialog.Other.Notification;
import view.Dialog.SecurityDialog.AdminSecurity;
import view.Helper.Attachement.AttachementPopOver;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.Objects;

public class AttachementForView extends AnchorPane{

    public AttachementForView(Attachement attachement, Boolean enableBtn){
        initPieceJointe();
        this.attachement = attachement;
        ImageView icon = initImgIcon(attachement.getExtensionPiece());
        attachCheckbox = initCheckBox();
        JFXButton optionBtn = initOptionButton(enableBtn);
        Label descriptionName = initAttachementDescription(attachement.getDescription());
        Label sizeLabel = initSizeLabel(attachement.getSize());
        this.getChildren().addAll(sizeLabel,descriptionName,optionBtn, attachCheckbox,icon);
    }

    private  void initPieceJointe(){
        int dim = 150;
        this.setPrefHeight(dim);
        this.setPrefHeight(dim);
        this.setId("pieceJointe");
        this.getStylesheets().add(style);
        this.setEffect(initEffect());
        this.setCursor(Cursor.HAND);
    }

    private Effect initEffect(){
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.DARKGRAY);
        dropShadow.setRadius(4);
        dropShadow.setWidth(10);
        dropShadow.setHeight(10);
        return dropShadow;
    }

    private ImageView initImgIcon(String extension){
        ImageView icon = new ImageView();
        icon.setFitHeight(63);
        icon.setFitWidth(72);
        icon.setPreserveRatio(true);
        icon.setPickOnBounds(true);
        icon.setImage(initImage(extension));
        AnchorPane.setLeftAnchor(icon,44.0);
        AnchorPane.setRightAnchor(icon,43.0);
        return icon;
    }

    private JFXCheckBox initCheckBox(){
        JFXCheckBox checkBox = new JFXCheckBox();
        checkBox.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        checkBox.setCursor(Cursor.HAND);
        AnchorPane.setLeftAnchor(checkBox,6.0);
        AnchorPane.setTopAnchor(checkBox,4.0);
        return checkBox;
    }

    private void onAttachCheckboxChecked(){
        attachementForView = this;
        AdminSecurity.show(Origin.VALIDATE_ATTACHEMENT);
    }

    public static void updateCheckBox(){
        JFXCheckBox attachCheckbox = attachementForView.getAttachCheckbox();
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Attachement attachement = attachementForView.getAttachement();
                boolean isSelected = attachCheckbox.isSelected();
                int queryStatus = DbUtils.updateAttachStatusTo(attachement, isSelected ? 0 : 1);
                if (queryStatus!=0){
                    attachCheckbox.setSelected(isSelected);
                    String message =  "";
                    if (isSelected) message = "Modification enregistrer avec succès";
                    else  message = "Validation enregistré avec succès";
                    Notification.getInstance(message, NotifType.SUCCESS).showNotif();
                }
                return null;
            }
        });
    }


    private class AttachCheckbox extends JFXCheckBox{
        @Override public void fire(){
           onAttachCheckboxChecked();
        }
    }

    private JFXButton initOptionButton(Boolean enableButton ){
        JFXButton button = new JFXButton();
        button.setOnAction(event -> AttachementPopOver.getInstance().showPopup(button));
        button.setVisible(enableButton);
        ImageView buttonImgView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu_vertical_50px.png"))));
        buttonImgView.setPreserveRatio(true);
        buttonImgView.setPickOnBounds(true);
        buttonImgView.setFitWidth(35.0);
        buttonImgView.setFitHeight(38.0);
        button.setGraphic(buttonImgView);
        button.setPrefHeight(43.0);
        button.setPrefWidth(49.0);
        AnchorPane.setRightAnchor(button,-8.0);
        AnchorPane.setTopAnchor(button,0.0);
        return button;
    }
    private Label initSizeLabel(String size){
        Label sizeLabel = new Label(size);
        sizeLabel.setAlignment(Pos.CENTER);
        sizeLabel.setContentDisplay(ContentDisplay.CENTER);
        sizeLabel.setPrefWidth(90.0);
        sizeLabel.setPrefHeight(21.0);
        sizeLabel.setTextFill(Color.valueOf("#3c3939"));
        sizeLabel.setFont(new Font("Arial Unicode MS",15.0));
        AnchorPane.setLeftAnchor(sizeLabel,30.0);
        AnchorPane.setRightAnchor(sizeLabel,30.0);
        AnchorPane.setBottomAnchor(sizeLabel,64.0);
        AnchorPane.setTopAnchor(sizeLabel,65.0);
        return sizeLabel;
    }

    private Label initAttachementDescription(String description){
        Label pieceNom = new Label(description);
        pieceNom.setAlignment(Pos.CENTER);
        pieceNom.setContentDisplay(ContentDisplay.CENTER);
        pieceNom.setLayoutX(8.0);
        pieceNom.setLayoutY(129.0);
        pieceNom.setPrefHeight(54.0);
        pieceNom.setPrefWidth(140.0);
        pieceNom.setWrapText(true);
        pieceNom.setTextFill(Color.valueOf("#434141"));
        pieceNom.setFont(new Font("Arial Unicode MS",15.0));
        pieceNom.setCursor(Cursor.HAND);
        AnchorPane.setBottomAnchor(pieceNom,5.0);
        AnchorPane.setLeftAnchor(pieceNom,5.0);
        AnchorPane.setRightAnchor(pieceNom,5.0);
        return pieceNom;
    }

    private Image initImage(String extension){
        switch (extension){
            case "pdf" :{
                return pdfImg;
            }
            case "docx":
            case "doc":{
                return docxImg;
            }
            case "jpg":
            case "png":
            case "jpeg":
            case "gif":{
                return iconImg;
            }
            default:{
                return unknownImg;
            }
        }
    }

    public Attachement getAttachement() { return attachement; }
    public JFXCheckBox getAttachCheckbox() {
        return attachCheckbox;
    }
    private JFXCheckBox attachCheckbox;
    private Attachement attachement;
    private static AttachementForView attachementForView;
    private final String style = this.getClass().getResource("/css/Card.css").toExternalForm();
    private static final Image unknownImg = new Image(Objects.requireNonNull(Attachement.class.getResourceAsStream("/img/question_mark_40px.png")));
    private static final Image docxImg =  new Image(Objects.requireNonNull(Attachement.class.getResourceAsStream("/img/microsoft_word_2019_50px.png"))) ;
    private static final Image pdfImg = new Image(Objects.requireNonNull(Attachement.class.getResourceAsStream("/img/adobe_acrobat_reader_40px.png")));
    private static final Image iconImg = new Image(Objects.requireNonNull(Attachement.class.getResourceAsStream("/img/image_80px.png"))) ;
}
