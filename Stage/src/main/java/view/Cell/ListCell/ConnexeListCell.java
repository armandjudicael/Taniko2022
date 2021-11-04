package view.Cell.ListCell;

import view.Model.ViewObject.ConnexAffairForView;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ConnexeListCell extends ListCell<ConnexAffairForView> {

    private final Image image = new Image(this.getClass().getResourceAsStream("/img/folder_50px.png"));
    private final static   String style = DispatchListcell.class.getResource("/css/listCell.css").toExternalForm();
    @Override
    protected void updateItem(ConnexAffairForView connexAffairForView, boolean b) {

        super.updateItem(connexAffairForView, b);
        if (b || connexAffairForView == null) {
            setText(null);
            setGraphic(null);
        } else {
            this.setText(connexAffairForView.toString());
            this.setAlignment(Pos.CENTER_LEFT);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            this.setGraphicTextGap(20);
            imageView.setPreserveRatio(true);
            this.setGraphic(imageView);
            this.getStylesheets().add(style);
        }

    }

}
