package View.Helper.Other;

import DAO.DaoFactory;
import Model.Enum.Type;
import Model.Other.MainService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import java.util.regex.Pattern;

public class NumeroChecker {
    private static Pattern pattern = Pattern.compile("^[0-9]{1,5}$");
    private static String cigleTitre = "-BA";

    public NumeroChecker(TextField textField, Label label, Type type){
        textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            boolean matches = pattern.matcher(textField.getText()).matches();
            if (newValue.isEmpty()&&!matches){
                showInputError((HBox) textField.getParent(),label,false);
                return;
            }
            MainService.getInstance().launch(new Task<Void>(){
                    @Override protected Void call() throws Exception {
                        if (!checkType(type,newValue))
                            Platform.runLater(() ->{ showInputError((HBox)textField.getParent(),label,true); });
                        return null;
                    }
            });
        });
    }

    private Boolean checkType(Type type,String newValue){
        Boolean existContent = true;
        switch (type){
            case MORCELEMENT:{ existContent = DaoFactory.getTitreDao().checkNumMorcelement(newValue+cigleTitre); }break;
            case TITRE:{ existContent = DaoFactory.getTitreDao().checkNumTitle(newValue+cigleTitre);}break;
            case AFFAIRE:{ existContent = DaoFactory.getAffaireDao().checkNumAffair(newValue); }break;
            case ORDONNANCE:{ existContent = DaoFactory.getAffaireDao().checkNumOrdonance(newValue); }break;
            default: existContent = true;
        }
        return existContent;
    }

    private void showInputError(HBox box,Label label,Boolean visible){
        if (visible){
            label.setVisible(visible);
            box.getStyleClass().add("boxError");
        }else {
            label.setVisible(visible);
            box.getStyleClass().removeAll("boxError");
        }
    }
}
