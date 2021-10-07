package View.Dialog.Other;

import Controller.ViewController.MainController;
import Model.Enum.ExtensionType;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class FileChooserDialog {

    private static FileChooserDialog fileChooserDialog;
    private static FileChooser fileChooser;

    private FileChooserDialog(){
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Document","*.docx","*.pdf"),
                new FileChooser.ExtensionFilter("Image Files", "*.jpg","*.ico","*.png")
        );
    }

    public static List<File> getInstance(){
        if (fileChooserDialog==null)
            fileChooserDialog = new FileChooserDialog();

        return fileChooser.showOpenMultipleDialog(null);
    }

}
