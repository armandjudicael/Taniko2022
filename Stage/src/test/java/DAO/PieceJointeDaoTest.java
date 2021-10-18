package DAO;

import Model.Pojo.Affaire;
import Model.Pojo.PieceJointe;
import View.Model.ViewObject.PieceJointeForView;
import javafx.collections.ObservableList;
import org.junit.Test;

import static org.junit.Assert.*;

public class PieceJointeDaoTest {

    @Test
    public void removeAll() {
    }

    @Test
    public void createAll() {
    }

    @Test
    public void getAllPieceJointe() {
        Affaire affaire = new Affaire();
        affaire.setId(16);
        ObservableList<PieceJointeForView> allPieceJointe = DaoFactory.getPieceJointeDao().getAllPieceJointe(affaire);
        assertTrue(" Is not empty ",!allPieceJointe.isEmpty());
    }

    @Test
    public void getAllAttachement() {
        Affaire affaire = new Affaire();
        affaire.setId(16);
        ObservableList<PieceJointe> allPieceJointe = DaoFactory.getPieceJointeDao().getAllAttachement(affaire);
        assertTrue(" Is not empty ",!allPieceJointe.isEmpty());
        allPieceJointe.forEach(System.out::println);
    }
}