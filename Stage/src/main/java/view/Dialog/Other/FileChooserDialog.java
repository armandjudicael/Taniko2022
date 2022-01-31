package view.Dialog.Other;
import model.Enum.FileChooserType;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.List;

public class FileChooserDialog {

    private static FileChooserDialog fileChooserDialog;
    private static FileChooser fileChooser;
    private final static FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg","*.png","*.jpeg");
    private final static FileChooser.ExtensionFilter documentFilter = new FileChooser.ExtensionFilter("Documents", "Document","*.docx","*.pdf");

    private FileChooserDialog(){
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
    }
    public static List<File> getInstance(FileChooserType type){
        if (fileChooserDialog==null)
            fileChooserDialog = new FileChooserDialog();
        switch (type){
            case ATTACHEMENT: {
                fileChooser.getExtensionFilters().setAll(imageFilter,documentFilter);
                return fileChooser.showOpenMultipleDialog(null); }
            case SINGLE:{
                fileChooser.getExtensionFilters().setAll(imageFilter);
                File file = fileChooser.showOpenDialog(null);
                return List.of(file);
            }
        }
        return null;
    }
}
