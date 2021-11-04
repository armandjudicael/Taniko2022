package Main;

import model.pojo.business.Titre;
import view.Model.ViewObject.AffaireForView;
import view.Model.ViewObject.UserForView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InitializeApp {

    private static ObservableList<AffaireForView> affaires = FXCollections.observableArrayList();
    private static ObservableList<UserForView> users = FXCollections.observableArrayList();
    private static ObservableList<Titre> titres = FXCollections.observableArrayList();

    public InitializeApp(){
    }

    public static ObservableList<UserForView> getUsers() {
        return users;
    }

    public static void setUsers(ObservableList<UserForView> users) {
        InitializeApp.users = users;
    }

    public static ObservableList<AffaireForView> getAffaires() {
        return affaires;
    }

    public static void setAffaires(ObservableList<AffaireForView> affaires) {
        InitializeApp.affaires = affaires;
    }

    public static ObservableList<Titre> getTitres() {
        return titres;
    }

    public static void setTitres(ObservableList<Titre> titres) {
        InitializeApp.titres = titres;
    }
}
