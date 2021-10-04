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
        this.fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF","*.pdf"),
                new FileChooser.ExtensionFilter("docx","*.docx"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("ICO", "*.ico"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpg")
        );
    }
    public static List<File> getInstance(){
        if (fileChooserDialog==null)
            fileChooserDialog = new FileChooserDialog();
        return fileChooser.showOpenMultipleDialog(MainController.getInstance().getMainStackPane().getScene().getWindow());
    }

}
