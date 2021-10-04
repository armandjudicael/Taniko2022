package View.Cell.ListCell;
import Controller.ViewController.AffairViewController;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;

public class StatusCell extends ListCell<String>{

    @Override protected void updateItem(String s, boolean b){
        super.updateItem(s, b);
        if (s!=null && !b){
            this.setText(s);
            switch (s){
                case "En cours": { setGraphic(new ImageView(AffairViewController.getRunningImg())); }break;
                case "Suspendu": { setGraphic(new ImageView(AffairViewController.getSuspendImg())); }break;
                case "Terminer": { setGraphic(new ImageView(AffairViewController.getFinishedImg())); }break;
                case "réjété": { setGraphic(new ImageView(AffairViewController.getRejectImg())); }break;
            }
        }else {
            this.setText(null);
            this.setGraphic(null);
        }
    }
}
