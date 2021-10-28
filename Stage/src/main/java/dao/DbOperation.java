package dao;


import Model.Enum.AffaireStatus;
import Model.Enum.NotifType;
import Model.Enum.RegimeMatrimoniale;
import Model.Enum.TableName;
import Model.Pojo.*;
import Model.Other.MainService;
import View.Dialog.Other.Notification;
import View.Model.ViewObject.ConnexAffairForView;
import View.Model.ViewObject.ProcedureForView;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbOperation {
    public DbOperation() { }
    public static void executeQuery(String query) {
        MainService.getInstance().launch(new Task<Void>() {
            @Override protected Void call() throws Exception {
                try (Statement statement = connection.createStatement()){
                   int status = statement.executeUpdate(query);
                   connection.commit();
                   if (status!=0) Notification.getInstance(" Mis à jour efféctué avec succès ", NotifType.SUCCESS).showNotif();
                   else Notification.getInstance(" échec de la  mis à jour  ", NotifType.WARNING).showNotif();
                } catch (SQLException e) {
                    Logger.getLogger(DbOperation.class.getName()).log(Level.SEVERE, " There are a problem with the 'executeQuery' method ", e);
                }
                return null;
            }
        });
    }

    public static int launchQuery(String query){
       int status = 0;
        try (Statement statement = connection.createStatement()) {
            status = statement.executeUpdate(query);
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DbOperation.class.getName()).log(Level.SEVERE, " There are a problem with the 'launchQuery' method ", e);
        }
        return status;
    }

    public static int insertOnTerrrainTitre(Terrain terrain, TableName name) {
        String query = "";
        if (name.equals(TableName.ABOUTIR_TERRAIN_TITRE))
            query = "INSERT  INTO " + TERRAIN_ABOUTIR_TITRE + "(titreId,terrainId) VALUES (?,?);";
        else query = "INSERT  INTO " + TERRAIN_DEPEND_TITRE + "(titreId,terrainId) VALUES (?,?);";
        int status = 0;
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, terrain.getTitreDependant().getId());
            ps.setInt(2, terrain.getIdTerrain());
            status = ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DbOperation.class.getName()).log(Level.SEVERE, " There are a problem with the 'insertOnTerrrainTitre' method ", e);
        }
        return status;
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
            Logger.getLogger(DbOperation.class.getName()).log(Level.SEVERE, " There are a problem with the 'executeBacthRequest' method ", e);
        }
        return nbRequest;
    }

    public static int insertOnTableRepresentant(PersonnePhysique representant ,PersonneMorale personneMorale){
        int status = 0;
        String query = "INSERT  INTO representant (idPersonneMorale, idPersonnePhysique, dateRepresentantion) VALUES (?,?,?);";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, personneMorale.getId());
            ps.setInt(2, representant.getId());
            ps.setTimestamp(3,Timestamp.valueOf(LocalDateTime.now()));
            status = ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DbOperation.class.getName()).log(Level.SEVERE, " There are a problem with the 'insertOnTheTableRepresentant' method ", e);
        }
        return status;
    }

    public static int insertOnTableMariage(Mariage mariage){
        int status = 0;
        String query = "INSERT  INTO mariage (idDemandeur, idConjoint, dateMariage, lieuMariage, regime) VALUES (?,?,?);";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, mariage.getDemandeur().getId());
            ps.setInt(2, mariage.getConjoint().getId());
            ps.setTimestamp(3,mariage.getDateMariage());
            ps.setString(4,mariage.getLieuMariage());
            ps.setString(5,Mariage.regimeMatrimoniale2String(mariage.getRegimeMatrimoniale()));
            status = ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DbOperation.class.getName()).log(Level.SEVERE, " Problem with the 'insertOnTableMariage' method ", e);
        }
        return status;
    }

    public static int[] executeBatchRequest(ObservableList<ConnexAffairForView> selectedItems){
        int[] nbRequest = {0};
        String query = "update affaire set affaire.situation = ? where idAffaire = ?;";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (ConnexAffairForView item : selectedItems) {
                ps.setInt(2, item.getId());
                ps.setString(1,Affaire.affaireStatus2String(AffaireStatus.REJECTED));
                ps.addBatch();
            }
            nbRequest = ps.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DbOperation.class.getName()).log(Level.SEVERE, " Problem with the 'executeBatchRequest' method ", e);
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
            Logger.getLogger(DbOperation.class.getName()).log(Level.SEVERE, " Problem with the ' updateProcedureName' method ", e);
        }
        return status;
    }

    public static int insertOnAffAndPrcdTable(int idProcedure, int idAffair, Timestamp date) {
        int status = 0;
        String query = "INSERT  INTO procedure_concerner_affaire (procedureId,affaireId,dateDepart) VALUES (?,?,?);";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idProcedure);
            ps.setInt(2, idAffair);
            ps.setTimestamp(3, date);
            status = ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DbOperation.class.getName()).log(Level.SEVERE, " Problem with the 'insertOnAffAndPrcdTable' method ", e);
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
                Logger.getLogger(DbOperation.class.getName()).log(Level.SEVERE, " Problem with the 'updateUserProfil' method ", e);
            }
        } catch (SQLException e) {
            Logger.getLogger(DbOperation.class.getName()).log(Level.SEVERE, " Problem with the 'updateUserProfil' method ", e);
        }
        return status;
    }

    public static int insertOnDispatchTable(User user,Affaire affaire){
        int status = 0;
        String query = " INSERT INTO dispatch (utilisateurId,affaireId,dateDispatch) VALUES ( ? , ? , ? ); ";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1,user.getId());
            ps.setInt(2,affaire.getId());
            ps.setTimestamp(3,Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.now())));
           status = ps.executeUpdate();
           connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DbOperation.class.getName()).log(Level.SEVERE, " Problem with the 'insertOnDispatchTable' method ", e);
        }
        return  status;
    }

    public static int deleteOnAffaireUtilisateurTable(int id_user, int id_aff){
        int status = 0;
        String query = " delete from dispatch where (utilisateurId = ? and affaireId = ? ); ";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id_user);
            ps.setInt(2, id_aff);
            status = ps.executeUpdate();
            connection.commit();
        } catch (SQLException e){
            Logger.getLogger(DbOperation.class.getName()).log(Level.SEVERE, " Problem with the 'deleteOnAffaireUtilisateurTable' method ", e);
        }
        return status;
    }

    public static Connection getConnection() {
        return connection;
    }
    private static Connection connection = new ConnectionFactory().getConnection();
    private static final String TERRAIN_DEPEND_TITRE = "terrain_depend_titre";
    private static final String TERRAIN_ABOUTIR_TITRE = "terrain_aboutir_titre";
}
