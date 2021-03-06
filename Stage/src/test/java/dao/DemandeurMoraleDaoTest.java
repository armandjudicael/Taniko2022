package dao;
import model.Enum.TypeDemandeur;
import model.pojo.business.PersonneMorale;
import model.pojo.business.PersonnePhysique;
import javafx.collections.ObservableList;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class DemandeurMoraleDaoTest {

    @Test public void create() {
        int i = DaoFactory.getDemandeurMoraleDao().create(createDemandeurPhysique());
        assertEquals(" INSERTION DE DEMANDEUR PHYSIQUE DANS LA TABLE DEMANDEUR_MORALE ",1,i);
    }

    public static PersonnePhysique createDemandeurPhysique(){
        String name = "Dias";
        String firstName = "kaya";
        LocalDate now = LocalDate.now();
        Date dateNaissance = Date.valueOf(now);
                String lieuDeNaissance = "Toamasina I";
        String sexe = "féminin";
        String adresse = "Ambalamanasy Carreau I Lot 68";
        String parcelle = "";
        String lot = "";
        String profession = "Guide touristique";
        String email = "DiasKaia@gmail.com ";
        String numTel = "03214755";
        String situationMatrimoniale = "Marié";
        String nationalite ="Malagasy";
        String nomDuPere = "inconnu";
        String nomDuMere = "inconnu";
        PersonnePhysique personnePhysique = new PersonnePhysique(name + firstName, adresse, numTel, email, TypeDemandeur.PERSONNE_PHYSIQUE, nationalite, parcelle, lot, dateNaissance, lieuDeNaissance, sexe, nomDuPere, nomDuMere, situationMatrimoniale, profession);
        return personnePhysique;
    }

    @Test public void delete(){
        PersonnePhysique physique = createDemandeurPhysique();
        physique.setId(4);
        int delete = DaoFactory.getDemandeurMoraleDao().delete(physique);
        assertEquals(" suppression du "+physique.getNom()+" est effectué avec succés ",1,delete);
    }

    @Test public void getDemandeurIdBy(){
        PersonnePhysique demandeurPhysique = createDemandeurPhysique();
        int demandeurIdBy = DaoFactory.getDemandeurMoraleDao().getDemandeurIdBy(demandeurPhysique.getNom(), DemandeurMoraleDao.ParamerterType.NAME);
        assertEquals("identifiant azo tsara",10,demandeurIdBy);
    }

    @Test public void update() {

    }

    @Test
    public void findById() {

    }

    @Test
    public void finByNum() {

    }

    @Test
    public void findbyName() {
        ObservableList<PersonneMorale> observableList = DaoFactory.getDemandeurMoraleDao().findbyName("min", TypeDemandeur.PERSONNE_MORALE);
        assertTrue(!observableList.isEmpty());
        observableList.forEach(System.out::println);
    }
}