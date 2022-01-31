package view.Cell.ListCell;

import model.pojo.business.other.Propriete;
import model.pojo.business.other.Titre;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TitleCell extends ListCell<Propriete> {
    private final static   String style = DispatchListcell.class.getResource("/css/listCell.css").toExternalForm();
    @Override protected void updateItem(Propriete propriete, boolean b){
        if (propriete == null || b) {
            setText(null);
            setGraphic(null);
        } else {
            setText(propriete.toString());
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/img/title(2).png")));
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setFitWidth(25);
            imageView.setFitWidth(25);
            this.setGraphicTextGap(20);
            setGraphic(imageView);
            this.getStylesheets().add(style);

        }

    }

}
