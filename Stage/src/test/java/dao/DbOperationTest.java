package dao;

import Model.Enum.RegimeMatrimoniale;
import Model.Pojo.business.PersonneMorale;
import Model.Pojo.business.PersonnePhysique;
import Model.Pojo.utils.Mariage;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

public class DbOperationTest {

    @Test public void executeQuery() {

    }

    @Test public void launchQuery(){

    }

    @Test public void insertOnTerrrainTitre(){

    }

    @Test public void executeBacthRequest(){

    }

    @Test public void executeBatchRequest(){

    }

    @Test public void updateProcedureName(){

    }

    @Test public void insertOnAffAndPrcdTable() {

    }

    @Test public void updateUserProfil() {

    }

    @Test public void updateProcedureAnnuaire() {

    }

    @Test public void insertOnDispatchTable() {

    }

    @Test public void deleteOnAffaireUtilisateurTable() {

    }

    @Test public void getProcedureDate() {

    }

    @Test public void testExecuteQuery() {

    }

    @Test public void testLaunchQuery() {

    }

    @Test public void testInsertOnTerrrainTitre() {

    }

    @Test public void testExecuteBacthRequest() {

    }

    @Test public void insertOnTableRepresentant(){
        PersonnePhysique demandeurPhysique = DemandeurMoraleDaoTest.createDemandeurPhysique();
        demandeurPhysique.setId(10);
        PersonneMorale personneMorale = new PersonneMorale();
        personneMorale.setId(9);
        int i = DbOperation.insertOnTableRepresentant(demandeurPhysique, personneMorale, Date.valueOf(LocalDate.now()));
        Assert.assertEquals(1,i);
    }

    @Test public void insertOnTableOrdonnance(){

    }

    @Test public void insertOnTableJtr() {
    }

    @Test public void insertOnTableReperage() {
    }

    @Test public void insertOnTableMariage(){
        PersonnePhysique demandeur = new PersonnePhysique();
        demandeur.setId(10);
        PersonnePhysique conjoint = new PersonnePhysique();
        conjoint.setId(12);
        Mariage mariage = new Mariage();
        mariage.setConjoint(conjoint);
        mariage.setDemandeur(demandeur);
        mariage.setLieuMariage("TOAMASINA I");
        mariage.setDateMariage(Date.valueOf(LocalDate.now()));
        mariage.setRegimeMatrimoniale(RegimeMatrimoniale.SEPARATION_DES_BIENS);
        int i = DbOperation.insertOnTableMariage(mariage);
        Assert.assertEquals(1,i);
    }

    @Test public void testExecuteBatchRequest() {
    }

    @Test public void testUpdateProcedureName() {
    }

    @Test public void testInsertOnAffAndPrcdTable() {
    }

    @Test public void testUpdateUserProfil() {
    }

    @Test public void testInsertOnDispatchTable() {
    }

    @Test public void testDeleteOnAffaireUtilisateurTable() {
    }

    @Test
    public void getConnection() {
    }
}