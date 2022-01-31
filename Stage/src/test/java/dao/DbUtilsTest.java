package dao;

import model.Enum.RegimeMatrimoniale;
import model.pojo.business.other.PersonneMorale;
import model.pojo.business.other.PersonnePhysique;
import model.pojo.utils.Mariage;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;

public class DbUtilsTest {

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
        PersonnePhysique demandeurPhysique = PersonneMoraleDaoTest.createDemandeurPhysique();
        demandeurPhysique.setId(10);
        PersonneMorale personneMorale = new PersonneMorale();
        personneMorale.setId(9);
//        int i = DbUtils.insertOnTableRepresentant(demandeurPhysique, personneMorale, Date.valueOf(LocalDate.now()));
//        Assert.assertEquals(1,i);
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
        int i = DbUtils.insertOnTableMariage(mariage);
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

    @Test public void getConnection(){

    }

    @Test public void checkAffaireInfoForm() {
        String s = DbUtils.checkAffaireInfoForm("5 - TAM-I / 2021", "46", "5", "46");
        Assertions.assertThat(s).isEqualTo("1;1;1;1");
    }

    @Test public void checkDemandeurInfoForm(){
        String s = DbUtils.checkDemandeurInfoForm("Armand Judicael","0340588519","armandjudicaelratombotiana@gmail.com");
        Assertions.assertThat(s).isEqualTo("1;1;1");
    }
}