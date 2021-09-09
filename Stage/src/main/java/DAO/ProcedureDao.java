package DAO;

import Model.Pojo.Affaire;
import Model.Pojo.Procedure;
import View.Model.ProcedureForView;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProcedureDao extends DAO<Procedure> {

    public ProcedureDao(Connection connection) {
        super(connection);
    }
    @Override
    public int create(Procedure procedure) {
        String query = " INSERT INTO _procedure ( nomProcedure , typeProcedur ) VALUES (? , ? ) ;";
        int status = 0;
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
                preparedStatement.setString(1, procedure.getNom());
                status = preparedStatement.executeUpdate();
                this.connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    public int create(String name, String type) {
        String query = " INSERT INTO _procedure ( `_procedure`.nomProcedure , `_procedure`.typeProcedure ) VALUES (? , ? ) ;";
        int status = 0;
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, type);
            status = preparedStatement.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    public int getLastInsertId(){
        int id = 1;
        String query = " SELECT idProcedure  from `_procedure` order by idProcedure desc limit 1 ;";
        ResultSet resultSet;
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("idProcedure");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public int delete(Procedure procedure) {

        String query = " DELETE FROM _procedure WHERE idProcedure = ? ;";
        int status = 0;
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
                preparedStatement.setInt(1, procedure.getId());
                status = preparedStatement.executeUpdate();
                this.connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  status;

    }


    @Override
    public int update(Procedure procedure) {
        String query = " UPDATE _procedure SET nomProcedure = ? WHERE idProcedure = ? ; ";
        int status = 0 ;
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
              preparedStatement.setString(1,procedure.getNom());
              preparedStatement.setInt(2,procedure.getId());
              status = preparedStatement.executeUpdate();
              this.connection.commit();
        }catch (SQLException e ){
            e.printStackTrace();
        }
        return  status;
    }

    @Override
    public Procedure findById(int id) {
           Procedure procedure = new Procedure();
           String query = "SELECT * FROM _procedure WHERE idProcedure= ?;";
           ResultSet resultSet;
           try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)){
                preparedStatement.setInt(1, id);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                procedure = new Procedure(
                        id,
                        resultSet.getInt("numero"),
                        resultSet.getString("nom")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return procedure;
    }

//    public Procedure findByIdWithAffaires(int id) {
//        Procedure procedure = new Procedure();
//        try {
//            // recuperation de notre  procedure
//            String query = "SELECT * FROM _procedure WHERE id = ?;";
//            PreparedStatement   preparedStatement = this.connection.prepareStatement(query);
//            preparedStatement.setInt(1,id);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()){
//                procedure = new Procedure(
//                        id,
//                        resultSet.getInt("numero"),
//                        resultSet.getString("nom")
//                );
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return procedure;
//    }

//    public ObservableList<String> getAllProcedureName() {
//        ObservableList<String> list = FXCollections.observableArrayList();
//        try {
//            // recuperation de notre  procedure
//            String query = "SELECT distinct `_procedure`.nom FROM _procedure ";
//            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
//            ResultSet rs = preparedStatement.executeQuery();
//            while (rs.next()) {
//                list.add(rs.getString("nom"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }

    // recuperation des affaires concernés par le numero de la procedure procedure

//    public ArrayList<Affaire> getAffairesConcernedByProcedureNum(Procedure procedure) {
//        ArrayList<Affaire> affairArrayList = new ArrayList<>();
//        String query = "SELECT DISTINCT " +
//                "affaire.id," +
//                "affaire.numero," +
//                "affaire.type_demande," +
//                "affaire.date_formulation," +
//                "affaire.commune," +
//                "affaire.district," +
//                "affaire.region," +
//                "affaire.quartier," +
//                "affaire.parcelle," +
//                "affaire.superficie," +
//                "affaire.id_demandeur," +
//                "affaire.unite_de_mesure," +
//                "affaire.propriete_dependant," +
//                "affaire.num_titre_propriete" +
//                " FROM affaire inner join affaire_procedure on affaire_procedure.id_aff=affaire.id where(( select id from _procedure where numero = ?)=affaire_procedure.num_prcd );";
//        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
//            preparedStatement.setInt(1, procedure.getNumero());
//            ResultSet rs = preparedStatement.executeQuery();
//            while (rs.next()) {
//                affairArrayList.add(new Affaire(
//                        rs.getString("numero"),
//                        rs.getTimestamp("date_formulation"),
//                        DaoFactory.getDemandeurDao().findById(rs.getInt("id_demandeur")),
//                        rs.getString("superficie"),
//                        rs.getString("region"),
//                        rs.getString("district"),
//                        rs.getString("commune"),
//                        rs.getString("parcelle"),
//                        rs.getString("quartier"),
//                        string2TypeDemande(rs.getString("type_demande")),
//                        rs.getInt("id"),
//                        string2Observation(rs.getString("observation")),
//                        rs.getString("propriete_dependant"),
//                        rs.getString("num_titre_propriete")
//                ));
//
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return affairArrayList;
//
//    }
    // recuperation des affaires concernés par l'identifiant de la procedure

//    public ArrayList<Affaire> getAffairesConcernedByProcedureId(Procedure procedure) {
//
//        ArrayList<Affaire> affairArrayList = new ArrayList<>();
//
//        String  query = " SELECT DISTINCT  " +
//                "affaire.id," +
//                "affaire.numero," +
//                "affaire.type_demande," +
//                "affaire.date_formulation," +
//                "affaire.commune," +
//                "affaire.district," +
//                "affaire.region," +
//                "affaire.quartier," +
//                "affaire.parcelle," +
//                "affaire.superficie," +
//                "affaire.id_demandeur," +
//                "affaire.unite_de_mesure" +
//                "FROM affaire,affaire_procedure where " +
//                "(affaire.id = affaire_procedure.id_aff AND affaire_procedure.id_prcd = ?);";
//
//        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
//
//            ps.setInt(1, procedure.getId());
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//
//                affairArrayList.add(new Affaire(
//                        rs.getString("numero"),
//                        rs.getTimestamp("date_formulation"),
//                        DaoFactory.getDemandeurDao().findById(rs.getInt("id_demandeur")),
//                        rs.getString("superficie"),
//                        rs.getString("region"),
//                        rs.getString("district"),
//                        rs.getString("commune"),
//                        rs.getString("parcelle"),
//                        rs.getString("quartier"),
//                        string2TypeDemande(rs.getString("type_demande")),
//                        rs.getInt("id"),
//                        string2Observation(rs.getString("observation")),
//                        rs.getString("propriete_dependant"),
//                        rs.getString("num_titre_propriete")
//                ));
//
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return affairArrayList;
//    }

    @Override
    public Procedure finByNum(int num) {
        return null;
    }

    // liste de tous les procedures
    public ArrayList<Procedure> getAllProcedures(){

        ArrayList<Procedure> procedures = new ArrayList<>();
        String query = " SELECT DISTINCT * FROM _procedure ";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                procedures.add(new Procedure(
                   resultSet.getInt("id"),
                   resultSet.getInt("numero"),
                   resultSet.getString("nom")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  procedures;
    }

    public ObservableList<ProcedureForView> getAllProcedureFromBD(String type) {
        ObservableList<ProcedureForView> procedureForViews = FXCollections.observableArrayList();
        String query = " SELECT * FROM _procedure where typeProcedure = ? ";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setString(1, type);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                procedureForViews.add(new ProcedureForView(
                        resultSet.getInt("idProcedure"),
                        new SimpleStringProperty(resultSet.getString("nomProcedure")),
                        new SimpleStringProperty(""),
                        new SimpleBooleanProperty(false),
                        new SimpleStringProperty(""),
                        new SimpleStringProperty(""),
                        new SimpleStringProperty("")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return procedureForViews;
    }

    public int getProcedureBy(String numero, String nom){
        int count= 0;
        String query = "SELECT _procedure.idProcedure FROM `_procedure` WHERE numero = ? AND nom = ? ORDER BY _procedure.id DESC limit 1;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1,numero);
            ps.setString(2,nom);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                count = rs.getInt("id");
        }catch (SQLException e){
            e.printStackTrace();
        }

        return count;
    }


    public ArrayList getActualProcedure(Affaire affaire) {
        ArrayList a = new ArrayList();
        String query = "SELECT DISTINCT utilisateur.prenom,affaire_utilisateur.date_parrainage"+
                " FROM utilisateur INNER JOIN affaire_utilisateur" +
                " ON utilisateur.id = affaire_utilisateur.id_user" +
                " WHERE affaire_utilisateur.id_aff = ?  " +
                " ORDER BY affaire_utilisateur.date_parrainage desc limit 1";

        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, affaire.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                a.add(rs.getString("prenom"));
                a.add(rs.getTimestamp("date_parrainage"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }

    public int getProcedureId(int num){
        int count= 0;
        String query = "SELECT _procedure.id FROM `_procedure` where numero = ?;";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            preparedStatement.setInt(1,num);
            while (resultSet.next())
                count = resultSet.getInt("id");
        }catch (SQLException e){
            e.printStackTrace();
        }

        return count;
    }

}
