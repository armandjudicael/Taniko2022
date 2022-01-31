package dao;
import model.Enum.FolderStatus;
import model.Enum.NotifType;
import model.Enum.TableName;
import model.other.MainService;
import model.pojo.business.attachement.Attachement;
import model.pojo.business.other.*;
import model.pojo.utils.Mariage;
import model.pojo.utils.UtilsData;
import view.Dialog.Other.Notification;
import view.Model.ViewObject.ConnexAffairForView;
import view.Model.ViewObject.ProcedureForView;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbUtils{

    public DbUtils() {}

    public static void executeQuery(String query) {
        MainService.getInstance().launch(new Task<Void>() {
            @Override protected Void call() throws Exception {
                try (Statement statement = connection.createStatement()){
                   int status = statement.executeUpdate(query);
                   connection.commit();
                   if (status!=0) Notification.getInstance(" Mis à jour efféctué avec succès ", NotifType.SUCCESS).showNotif();
                   else Notification.getInstance(" échec de la  mis à jour  ", NotifType.WARNING).showNotif();
                } catch (SQLException e) {
                    Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " There are a problem with the 'executeQuery' method ", e);
                }
                return null;
            }
        });
    }

    public static int updateAttachStatusTo(Attachement attachement, int status){
   //     String query = " UPDATE pieceJointe SET attachStatus = ? ;";
        try(PreparedStatement ps = connection.prepareStatement("")){
            ps.setBoolean(1,status == 1 ? true : false);
            int i = ps.executeUpdate();
            connection.commit();
            return i;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public static int launchQuery(String query){
       int status = 0;
        try (Statement statement = connection.createStatement()){
            status = statement.executeUpdate(query);
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " There are a problem with the 'launchQuery' method ", e);
        }
        return status;
    }

    public static int insertOnTerrainTitre(Propriete propriete, TableName name) {
        String query = "";
//        if (name.equals(TableName.ABOUTIR_TERRAIN_TITRE))
//            query = "INSERT  INTO " + TERRAIN_ABOUTIR_TITRE + "(titreId,terrainId) VALUES (?,?);";
//        else query = "INSERT  INTO " + TERRAIN_DEPEND_TITRE + "(titreId,terrainParentId) VALUES (?,?);";
//        int status = 0;
//        try (PreparedStatement ps = connection.prepareStatement(query)) {
//            ps.setInt(1, propriete.getTitreDependant().getId());
//            ps.setInt(2, propriete.getIdTerrain());
//            status = ps.executeUpdate();
//            connection.commit();
//        } catch (SQLException e) {
//            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " There are a problem with the 'insertOnTerrrainTitre' method ", e);
//        }
        return 0;
    }

    public static int[] executeBacthRequest(ObservableList<ProcedureForView> selectedItems){
        int[] nbRequest = {0};
        String query = " DELETE FROM `_procedure` where idProcedure = ?;";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (ProcedureForView item : selectedItems) {
                ps.setInt(1, item.getIdProcedure());
                ps.addBatch();
            }
            nbRequest = ps.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " There are a problem with the 'executeBacthRequest' method ", e);
        }
        return nbRequest;
    }

    public static int insertOnTableRepresentant(Personne representant , Personne personneMorale,Date dateFormulation , UtilsData procuration){
        int status = 0;
     //   String query = "INSERT  INTO representant (idPersonneMorale, idPersonnePhysique, dateRepresentantion,dateProcuration,numProcuration) VALUES (?,?,?,?,?);";
        try (PreparedStatement ps = connection.prepareStatement("")) {
            ps.setInt(1, personneMorale.getId());
            ps.setInt(2, representant.getId());
            ps.setDate(3,dateFormulation);
            ps.setDate(4,procuration.getDate());
            ps.setString(5,procuration.getNum());
            status = ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " There are a problem with the 'insertOnTheTableRepresentant' method ", e);
        }
        return status;
    }

    public static int insertOnTableTitulaire(Dossier dossier, Personne titulaire){
        int status = 0;
        String query = " INSERT INTO titulaire_dossier(dossierId,titulaireId) values (?,?);";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, dossier.getId());
            ps.setInt(1,titulaire.getId());
            status = ps.executeUpdate();
            connection.commit();
        }catch (SQLException e){
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " There are a problem with the 'insertOnTheTableTitulaire' method ", e);
        }
        return 0;
    }

    public static int[] insertOnTableDemandeur(List<Personne> personneList, Dossier dossier){
        int [] status = {0} ;
        String query = "INSERT INTO demandeurDossier(dossierId,demandeurId) values (?,?);";
        try (PreparedStatement ps = connection.prepareStatement(query)){
            for (Personne personne : personneList){
                ps.setInt(1, dossier.getId());
                ps.setInt(2,personne.getId());
                ps.addBatch();
            }
            status = ps.executeBatch();
        }catch (SQLException e){
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " There are a problem with the 'insertOnTableDemandeur' method ", e);
        }
        return status;
    }

    public static int insertOnTableOrdonnance(UtilsData ordonnance,int affaireId){
        int status = 0;
//        String query = "INSERT  INTO ordonnance (numOrdonnance, dateOdronnance, affaireId) VALUES (?,?,?);";
//        try (PreparedStatement ps = connection.prepareStatement(query)) {
//            ps.setString(1,ordonnance.getNum());
//            ps.setDate(2,ordonnance.getDate());
//            ps.setInt(3,affaireId);
//            status = ps.executeUpdate();
//            connection.commit();
//        } catch (SQLException e) {
//            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " There are a problem with the 'insertOnTheTableRepresentant' method ", e);
//        }
        return status;
    }
    public static int insertOnTableJtr(String numJtr,Date dateJtr,int affaireId){
        int status = 0;
        String query = "INSERT  INTO journal_de_tresorerie (numJtr, dateJtr, affaireId) VALUES (?,?,?);";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1,numJtr);
            ps.setDate(2,dateJtr);
            ps.setInt(3,affaireId);
            status = ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " There are a problem with the 'insertOnTheTableRepresentant' method ", e);
        }
        return status;
    }
    public static int insertOnTableReperage(UtilsData reperage ,int affaireId){
        int status = 0;
//        String query = "INSERT  INTO reperage (numReperage, dateReperage, affaireId) VALUES (?,?,?);";
//        try (PreparedStatement ps = connection.prepareStatement(query)) {
//            ps.setString(1,reperage.getNum());
//            ps.setDate(2,reperage.getDate());
//            ps.setInt(3,affaireId);
//            status = ps.executeUpdate();
//            connection.commit();
//        } catch (SQLException e) {
//            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " There are a problem with the 'insertOnTheTableReperage' method ", e);
//        }
        return status;
    }

    public static int insertOnTableMariage(Mariage mariage){
        int status = 0;
        String query = "INSERT  INTO mariage (idDemandeur, idConjoint, dateMariage, lieuMariage, regime) VALUES (?,?,?,?,?);";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, mariage.getDemandeur().getId());
            ps.setInt(2, mariage.getConjoint().getId());
            ps.setDate(3,mariage.getDateMariage());
            ps.setString(4,mariage.getLieuMariage());
            ps.setString(5,Mariage.regimeMatrimoniale2String(mariage.getRegimeMatrimoniale()));
            status = ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " Problem with the 'insertOnTableMariage' method ", e);
        }
        return status;
    }

    public static int insertOnTableMariage(Mariage mariage, PersonnePhysique demandeur, PersonnePhysique conjoint){
        int status = 0;
        String query = "INSERT  INTO mariage (idDemandeur, idConjoint, dateMariage, lieuMariage, regime) VALUES (?,?,?,?,?);";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1,demandeur.getId());
            ps.setInt(2,conjoint.getId());
            ps.setDate(3,mariage.getDateMariage());
            ps.setString(4,mariage.getLieuMariage());
            ps.setString(5,Mariage.regimeMatrimoniale2String(mariage.getRegimeMatrimoniale()));
            status = ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " Problem with the 'insertOnTableMariage' method ", e);
        }
        return status;
    }

    public static int[] executeBatchRequest(ObservableList<ConnexAffairForView> selectedItems){
        int[] nbRequest = {0};
        String query = "update dossier set dossier.situation = ? where idDossier = ?;";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (ConnexAffairForView item : selectedItems) {
                ps.setInt(2, item.getId());
                ps.setString(1, Dossier.affaireStatus2String(FolderStatus.REJECTED));
                ps.addBatch();
            }
            nbRequest = ps.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " Problem with the 'executeBatchRequest' method ", e);
        }
        return nbRequest;
    }
    public static int updateProcedureName(String name,int idProcedure) {
        int status = 0;
        String request = "UPDATE _procedure SET nomProcedure = ? WHERE _procedure.idProcedure = ?;";
        try (PreparedStatement ps = connection.prepareStatement(request)) {
            ps.setString(1, name);
            ps.setInt(2, idProcedure);
            status = ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " Problem with the ' updateProcedureName' method ", e);
        }
        return status;
    }

    public static int insertOnAffAndPrcdTable(int idProcedure, int idAffair, Timestamp date) {
        int status = 0;
        String query = "INSERT  INTO procedure_concerner_dossier (procedureId,dossierId,dateDepart) VALUES (?,?,?);";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idProcedure);
            ps.setInt(2, idAffair);
            ps.setTimestamp(3, date);
            status = ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " Problem with the 'insertOnAffAndPrcdTable' method ", e);
        }
        return status;
    }

    public static int updateUserProfil(File file, int userId) {
        int status = 0;
        String query = " UPDATE utilisateur SET photo = ? WHERE idUtilisateur = ?;";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            try {
                ps.setBlob(1, new FileInputStream(file));
                ps.setInt(2, userId);
                status = ps.executeUpdate();
                connection.commit();
            } catch (FileNotFoundException e) {
                Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " Problem with the 'updateUserProfil' method ", e);
            }
        } catch (SQLException e) {
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " Problem with the 'updateUserProfil' method ", e);
        }
        return status;
    }

    public static Map<String , String> getAllAdminEmailAndNumTel(){
        Map<String , String> emailAndNumMap = new HashMap<>();
        String query = " SELECT dm.numTelDmd,emailDmd FROM utilisateur u " +
                "inner join personne_physique dp on u.idUtilisateur = dp.idDmdPhysique " +
                " inner join personne_morale dm on dp.idDmdPhysique = dm.idDmd" +
                " WHERE type = 'administrateur';";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                emailAndNumMap.put(rs.getString("email"),rs.getString("numTel"));
        }catch (SQLException e){
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " Problem with the 'getAllAdminEmail' method ", e);
        }
        return emailAndNumMap;
    }

    public static int insertOnDispatchTable(User user, Dossier dossier){
        int status = 0;
        String query = " INSERT INTO dispatch (utilisateurId,dossierId,dateDispatch) VALUES ( ? , ? , ? ); ";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1,user.getId());
            ps.setInt(2, dossier.getId());
            ps.setTimestamp(3,Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.now())));
           status = ps.executeUpdate();
           connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " Problem with the 'insertOnDispatchTable' method ", e);
        }
        return  status;
    }

    public static int deleteOnAffaireUtilisateurTable(int id_user, int id_aff){
        int status = 0;
        String query = " delete from dispatch where (utilisateurId = ? and dossierId = ? ); ";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id_user);
            ps.setInt(2, id_aff);
            status = ps.executeUpdate();
            connection.commit();
        } catch (SQLException e){
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " Problem with the 'deleteOnAffaireUtilisateurTable' method ", e);
        }
        return status;
    }

    public static String checkAffaireInfoForm(String numAff,String numOrd,String numRep,String numJtr){
        try {
            CallableStatement cst = connection.prepareCall("{ ? = call AFFAIRE_INFO_FORM_CHECKER(?,?,?,?) }");
            cst.registerOutParameter(1,Types.VARCHAR);
            cst.setString(2,numAff.toLowerCase());
            cst.setString(3,numOrd);
            cst.setString(4,numRep);
            cst.setString(5,numJtr);
            cst.execute();
            return cst.getString(1);
        } catch (SQLException exception) {
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " Problem with the 'deleteOnAffaireUtilisateurTable' method ", exception);
        }
        return "";
    }

    public static String checkDemandeurInfoForm(String nom, String numTel, String email){
        try{
            CallableStatement cst = connection.prepareCall("{ ? = call DEMANDEUR_INFO_FORM_CHECKER(?,?,?) }");
            cst.registerOutParameter(1,Types.VARCHAR);
            cst.setString(2,nom);
            cst.setString(3,numTel);
            cst.setString(4,email);
            cst.execute();
            return cst.getString(1);
        }catch (SQLException e){
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, " Problem with the 'checkDemandeurInfoForm' method ", e);
        }
        return "";
    }

    public static Connection getConnection() {
        return connection;
    }
    private static final Connection connection = new ConnectionFactory().getConnection();
}
