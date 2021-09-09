package DAO;


import Model.Enum.AffaireStatus;
import Model.Enum.NotifType;
import Model.Enum.TableName;
import Model.Pojo.Affaire;
import Model.Pojo.Procedure;
import Model.Pojo.Terrain;
import Model.Pojo.User;
import Model.serviceManager.MainService;
import View.Dialog.Other.Notification;
import View.Model.ConnexAffairForView;
import View.Model.ProcedureForView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DbOperation {
    private static Connection connection = new ConnectionFactory().getConnection();
    public static Connection getConnection() {
        return connection;
    }

    private static final String TERRAIN_DEPEND_TITRE = "terrain_depend_titre";
    private static final String TERRAIN_ABOUTIR_TITRE = "terrain_aboutir_titre";

    public DbOperation() {

    }

    public static void executeQuery(String query) {

        MainService.getInstance().launch(new Task<Void>() {
            @Override protected Void call() throws Exception {
                try (Statement statement = connection.createStatement()){

                   int status = statement.executeUpdate(query);
                   connection.commit();

                   if (status!=0){

                       Platform.runLater(() -> {
                           String message = " Mis à jour efféctué avec succès ";
                           Notification.getInstance(message, NotifType.SUCCESS).show();
                       });

                   }else {

                       Platform.runLater(() -> {
                           String message = " échec de la  mis à jour  ";
                           Notification.getInstance(message, NotifType.WARNING).show();
                       });

                   }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
            e.printStackTrace();
        }
        return status;
    }

    public static int[] executeBacthRequest(ObservableList<ProcedureForView> selectedItems) {
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
            e.printStackTrace();
        }
        return nbRequest;
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static int UpdateProcedureAnnuaire(Procedure procedure, Affaire affaire, Timestamp newTime) {

        int status = 0;
        String query = " UPDATE affaire_procedure SET date_prcd = ? WHERE id_prcd = ? and id_aff = ? ";
        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setTimestamp(1,newTime);
            ps.setInt(2,procedure.getId());
            ps.setInt(3, affaire.getId());
            status = ps.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  status;
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
            e.printStackTrace();
        }
        return  status;
    }

    public static int deleteOnAffaireUtilisateurTable(int id_user, int id_aff) {
        int status = 0;
        String query = " delete from dispatch where (utilisateurId = ? and affaireId = ? ); ";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id_user);
            ps.setInt(2, id_aff);
            status = ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static Timestamp getProcedureDate(Affaire affaire, Procedure procedure) {
        Timestamp timestamp = null;
        String query = " SELECT date_prcd FROM affaire_procedure WHERE id_prcd = ? AND id_aff = ? ; ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, affaire.getId());
            preparedStatement.setInt(2,procedure.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                timestamp = resultSet.getTimestamp("date_prcd");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return timestamp;
    }
}
