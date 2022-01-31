package dao;

import model.pojo.business.other.Dossier;
import model.pojo.business.attachement.Attachement;
import view.Model.ViewObject.AttachementForView;
import javafx.collections.ObservableList;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttachementDaoTest {

    @Test
    public void removeAll() {
    }

    @Test
    public void createAll() {
    }

    @Test
    public void getAllPieceJointe() {
        Dossier dossier = new Dossier();
        dossier.setId(16);
        ObservableList<AttachementForView> allPieceJointe = DaoFactory.getPieceJointeDao().getAllPieceJointe(dossier);
        assertTrue(" Is not empty ",!allPieceJointe.isEmpty());
    }

    @Test
    public void getAllAttachement() {
        Dossier dossier = new Dossier();
        dossier.setId(16);
        ObservableList<Attachement> allAttachement = DaoFactory.getPieceJointeDao().getAllAttachement(dossier);
        assertTrue(" Is not empty ",!allAttachement.isEmpty());
        allAttachement.forEach(System.out::println);
    }
}