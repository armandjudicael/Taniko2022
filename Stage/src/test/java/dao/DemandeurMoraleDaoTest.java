package dao;
import Model.Enum.TypeDemandeur;
import Model.Pojo.PersonnePhysique;
import org.junit.Test;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import static org.junit.Assert.*;

public class DemandeurMoraleDaoTest {

    @Test public void create() {
        int i = DaoFactory.getDemandeurMoraleDao().create(createDemandeurPhysique());
        assertEquals(" INSERTION DE DEMANDEUR PHYSIQUE DANS LA TABLE DEMANDEUR_MORALE ",1,i);
    }

    public static PersonnePhysique createDemandeurPhysique(){
        String name = "TOMBO";
        String firstName = "Augustin";
        LocalDate now = LocalDate.now();
        Timestamp dateNaissance =  Timestamp.valueOf(LocalDateTime.of(now.getYear(),now.getMonthValue(),now.getDayOfMonth(),0,0));
        String lieuDeNaissance = " Toamasina I";
        String sexe = "Masculin";
        String adresse = " Ambalamanasy Carreau I Lot 68 ";
        String parcelle = "";
        String lot = "";
        String profession = "Controleur";
        String email = "tomboaugustin@gmail.com ";
        String numTel = "0340588519";
        String situationMatrimoniale = "veuve";
        String nationalite =" Malagasy ";
        String nomDuPere = "inconnu";
        String nomDuMere = "THERESE";
        PersonnePhysique personnePhysique = new PersonnePhysique(name + firstName, adresse, numTel, email, TypeDemandeur.PERSONNE_PHYSIQUE, nationalite, parcelle, lot, dateNaissance, lieuDeNaissance, sexe, nomDuPere, nomDuMere, situationMatrimoniale, profession);
        return personnePhysique;
    }

    @Test public void delete(){
        PersonnePhysique physique = createDemandeurPhysique();
        physique.setId(4);
        int delete = DaoFactory.getDemandeurMoraleDao().delete(physique);
        assertEquals(" suppression du "+physique.getNom()+" est effectué avec succés ",1,delete);
    }

    @Test public void getDemandeurIdBy() {

    }
}