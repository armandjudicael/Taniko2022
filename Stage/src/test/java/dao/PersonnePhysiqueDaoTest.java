package dao;

import model.pojo.business.other.PersonnePhysique;
import org.junit.Test;

import static org.junit.Assert.*;

public class PersonnePhysiqueDaoTest {

    @Test public void create(){
        PersonnePhysique demandeurPhysique = PersonneMoraleDaoTest.createDemandeurPhysique();
        demandeurPhysique.setId(12);
        int i = DaoFactory.getDemandeurPhysiqueDao().create(demandeurPhysique);
        assertEquals(" insertion ok ",1,i);
    }

    @Test public void delete() {
    }

    @Test
    public void findById() {
    }

    @Test
    public void finByNum() {
    }

    @Test
    public void findDemandeurBy() {
    }
}