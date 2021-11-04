package view.Cell.ListCell;
import controller.viewController.AffairViewController;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class StatusCell extends ListCell<String>{

    private final static   String style = StatusCell.class.getResource("/css/listCell.css").toExternalForm();

    private static    Image runningImg = new Image(Objects.requireNonNull(AffairViewController.class.getResourceAsStream("/img/play1_20px.png")));
    private static   Image   suspendImg = new Image(Objects.requireNonNull(AffairViewController.class.getResourceAsStream("/img/pause_20px.png")));
    private static    Image   finishedImg = new Image(Objects.requireNonNull(AffairViewController.class.getResourceAsStream("/img/ok_20px.png")));
    private static    Image   rejectImg = new Image(Objects.requireNonNull(AffairViewController.class.getResourceAsStream("/img/cancel_20px.png")));

    @Override protected void updateItem(String s, boolean b){
        super.updateItem(s, b);
        if (s!=null && !b){
            this.setText(s);
            switch (s){
                case "En cours": { setGraphic(new ImageView(runningImg)); }break;
                case "Suspendu": { setGraphic(new ImageView(suspendImg)); }break;
                case "Terminer": { setGraphic(new ImageView(finishedImg)); }break;
                case "réjété": { setGraphic(new ImageView(rejectImg)); }break;
            }
            this.getStylesheets().add(style);
        }else {
            this.setText(null);
            this.setGraphic(null);
        }
    }

}
