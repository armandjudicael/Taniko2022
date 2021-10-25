package DAO;
import Model.Pojo.PersonneMorale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DemandeurMoraleDao extends Dao implements DaoHelper<PersonneMorale>{

    public DemandeurMoraleDao(Connection connection) {
        super(connection);
    }

    @Override public int create(PersonneMorale personneMorale){
        String query = "INSERT INTO demandeur_morale (idDmd,numTelDmd,emailDmd,adresseDmd,nomDmd,nationalite) VALUES (?,?,?,?,?,?);";
        try (PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, personneMorale.getId());
            ps.setString(2, personneMorale.getNumTel());
            ps.setString(3, personneMorale.getEmail());
            ps.setString(4, personneMorale.getAdresse());
            ps.setString(5,personneMorale.getNom());
            ps.setString(6, personneMorale.getNationalite());
            int status = ps.executeUpdate();
            connection.commit();
            return status;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override public int delete(PersonneMorale demandeurMorale){
        String query = "DELETE FROM  demandeur WHERE idDmd = ? ";
        int status = 0;
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setInt(1,demandeurMorale.getId());
            status = preparedStatement.executeUpdate();
            connection.commit();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    @Override public int update(PersonneMorale personneMorale) {
        return 0;
    }

    @Override public PersonneMorale findById(int id) {
        return null;
    }

    @Override public PersonneMorale finByNum(int num) {
        return null;
    }

}
