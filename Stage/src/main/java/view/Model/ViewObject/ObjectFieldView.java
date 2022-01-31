package view.Model.ViewObject;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.Enum.TypeDemandeur;
import model.pojo.business.other.PersonnePhysique;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectFieldView extends AnchorPane{
    private static Object actualObject;
    private Label fieldNameLabel;
    private Label fieldValueLabel;
    private JFXButton fieldButtonModif;
    private SimpleStringProperty fieldName;
    @Override public String toString() {
        return "ObjectFieldView{" +
                "fieldName=" + fieldName.get() +
                ", fieldValue=" + fieldValue.get() +
                '}';
    }
    private SimpleStringProperty fieldValue;
    private static final double HEIGHT = 50.0;
    private static ArrayList<java.lang.Class<? extends Object>> classArrayList = new ArrayList<>(List.of(TypeDemandeur.class,FileInputStream.class,int.class));
    private void initObjectPosition(){
        this.setPrefHeight(HEIGHT);
    }
    private static HashMap<String,EventHandler<ActionEvent>> eventHandlerHashMap;
    public ObjectFieldView(String name , String value , EventHandler<ActionEvent> eventHandler) {
        initObjectPosition();
        this.fieldName = new SimpleStringProperty(resolveFieldName(name));
        this.fieldValue = new SimpleStringProperty(value);
        fieldNameLabel = createField(fieldName,true);
        fieldValueLabel = createField(fieldValue,false);
        JFXButton fieldButton = createFieldButton(eventHandler);
        Separator separator = createSeparator();
        this.getChildren().addAll(fieldNameLabel,fieldValueLabel,separator,fieldButton);
    }
    private Label createField(SimpleStringProperty stringProperty,Boolean isName){
        Label label = new Label();
        String value = stringProperty.getValue();
        label.setText(value == null || value.isEmpty() ?"inconnu": value);
        Color color = Color.valueOf("#544e4e");
        label.setTextFill(color);
        Font font = new Font("verdana", 14.0);
        label.setFont(font);
        if (isName) AnchorPane.setTopAnchor(label,0.0);
        else AnchorPane.setBottomAnchor(label,5.0);
        AnchorPane.setLeftAnchor(label,2.0);
        return label;
    }

    private Separator createSeparator(){
        Separator sp = new Separator(Orientation.HORIZONTAL);
        AnchorPane.setBottomAnchor(sp,0.0);
        AnchorPane.setRightAnchor(sp,0.0);
        AnchorPane.setLeftAnchor(sp,0.0);
        return sp;
    }

    private JFXButton createFieldButton(EventHandler<ActionEvent> eventHandler){
        JFXButton button = new JFXButton();
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.setPrefHeight(36.0);
        button.setPrefWidth(40);
        ImageView imageView = initImageView();
        button.setGraphic(imageView);

        button.setLayoutX(534);
        button.setLayoutY(7.0);
        button.setStyle("-fx-background-color: #389955");
        AnchorPane.setRightAnchor(button,5.0);
        AnchorPane.setBottomAnchor(button,6.0);
        AnchorPane.setTopAnchor(button,5.0);

        button.setOnAction(eventHandler== null ? null : eventHandler);
        return button;
    }

    private ImageView initImageView() {
        Image img = new Image(getClass().getResourceAsStream("/img/edit_510px.png"));
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(20.0);
        imageView.setFitWidth(20);
        imageView.setPreserveRatio(true);
        imageView.setPickOnBounds(true);
        return imageView;
    }

    public static ObservableList<ObjectFieldView> create(Object object) throws IllegalAccessException{
        ObservableList<ObjectFieldView> list  = FXCollections.observableArrayList();
        Map<String, String> map = getFieldNameAndFieldValue(object);
        map.forEach((name, value) -> {
            ObjectFieldView objectFieldView = new ObjectFieldView(name, value, null);
            VBox.setVgrow(objectFieldView,Priority.NEVER);
            list.add(objectFieldView);
        });
        return list;
    }

    private void initEventMap(){
        if (actualObject.getClass() == PersonnePhysique.class){
            PersonnePhysique ph = (PersonnePhysique)actualObject;
            if(eventHandlerHashMap == null){
                eventHandlerHashMap = new HashMap<>();
                 // nom
                eventHandlerHashMap.put("nom",event -> {

                });
                // addresse
                eventHandlerHashMap.put("adresse",event -> {

                });
            }
        }
    }

    private static Map<String,String> getFieldNameAndFieldValue(Object o) throws IllegalAccessException{
        HashMap<String,String> map = new HashMap<>();
        Class<?> childClass = o.getClass();
        Class<?> superclass = childClass.getSuperclass();
        createFieldBy(o, map,childClass);
        if (superclass!=null) createFieldBy(o,map,superclass);
        return map;
    }

    private static void createFieldBy(Object o, HashMap<String, String> map, Class<?> aClass) throws IllegalAccessException {
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields){
            Class<?> type = declaredField.getType();
            if (!classArrayList.contains(type)) {
                declaredField.setAccessible(true);
                String name = declaredField.getName();
                Object o1 = declaredField.get(o);
                if (o1 != null) {
                    map.put(name, o1.toString());
                } else {
                    map.put(name, "inconnu");
                }
            }
        }
    }

    private  String resolveFieldName(String name){
        char[] chars = name.toCharArray();
        for (char aChar : chars) {
            // SNAKE_CASE
            if (aChar == '_') name = name.replace("_"," ");
            // CAMEL_CASE
            if (Character.isUpperCase(aChar)){
                String value ="  "+Character.toLowerCase(aChar);
                name = name.replace(String.valueOf(aChar), value);
            }
        }
        return name;
    }

    public static void initObjectPosition(VBox viewBox, Object viewObject) throws IllegalAccessException{
        if (viewBox!=null){
            ObservableList<Node> children = viewBox.getChildren();
            ObservableList<ObjectFieldView> list = create(viewObject);
            if (children.isEmpty()){
                if (!list.isEmpty()) children.addAll(list);
                return;
            }
            Map<String, String> fieldMap = getFieldNameAndFieldValue(viewObject);
            fieldMap.forEach((mapName, mapValue) ->{
                children.forEach(node ->{
                    ObjectFieldView objectFieldView = (ObjectFieldView)node;
                    String fieldName = objectFieldView.getFieldName().replaceAll("\\ ","").toLowerCase();
                    String s = mapName.trim().toLowerCase();
                    if (fieldName.equals(s)) objectFieldView.getFieldValueLabel().setText(mapValue);
                } );
            });
        }
    }
    public String getFieldName() {
        return fieldName.get();
    }
    public SimpleStringProperty fieldNameProperty() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName.set(fieldName);
    }
    public String getFieldValue() {
        return fieldValue.get();
    }
    public SimpleStringProperty fieldValueProperty() {
        return fieldValue;
    }
    public void setFieldValue(String fieldValue) {
        fieldValueLabel.textProperty().unbind();
        this.fieldValue.set(fieldValue);
    }
    public Label getFieldNameLabel() {
        return fieldNameLabel;
    }
    public void setFieldNameLabel(Label fieldNameLabel) {
        this.fieldNameLabel = fieldNameLabel;
    }
    public Label getFieldValueLabel(){
        return fieldValueLabel;
    }
    public void setFieldValueLabel(Label fieldValueLabel){
        this.fieldValueLabel = fieldValueLabel;
    }
    public JFXButton getFieldButtonModif() {
        return fieldButtonModif;
    }
    public void setFieldButtonModif(JFXButton fieldButtonModif){
        this.fieldButtonModif = fieldButtonModif;
    }
}
