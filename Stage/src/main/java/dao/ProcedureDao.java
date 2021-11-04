package dao;

import model.pojo.business.Procedure;
import view.Model.ViewObject.ProcedureForView;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcedureDao extends Dao implements DaoHelper<Procedure> {
    public ProcedureDao(Connection connection) {
        super(connection);
    }
    @Override public int create(Procedure procedure) {
        String query = " INSERT INTO _procedure ( nomProcedure , typeProcedure ) VALUES (? , ? ) ;";
        int status = 0;
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
                preparedStatement.setString(1, procedure.getNom());
                status = preparedStatement.executeUpdate();
                this.connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
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
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
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
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
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
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
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
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
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
               Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return procedure;
    }

    @Override
    public Procedure finByNum(int num) {
        return null;
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
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return procedureForViews;
    }
}
