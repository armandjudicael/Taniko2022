package dao;
import Model.Enum.TypeDemandeur;
import Model.Pojo.PersonneMorale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DemandeurMoraleDao extends Dao implements DaoHelper<PersonneMorale>{
    public DemandeurMoraleDao(Connection connection) {
        super(connection);
    }
    @Override public int create(PersonneMorale personneMorale){
        String query = "INSERT INTO demandeur_morale (numTelDmd,emailDmd,adresseDmd,nomDmd,nationalite,typeDmd) VALUES (?,?,?,?,?,?);";
        try (PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1, personneMorale.getNumTel());
            ps.setString(2, personneMorale.getEmail());
            ps.setString(3, personneMorale.getAdresse());
            ps.setString(4,personneMorale.getNom());
            ps.setString(5, personneMorale.getNationalite());
            ps.setString(6, TypeDemandeur.typeDemandeur2String(personneMorale.getType()));
            int status = ps.executeUpdate();
            connection.commit();
            return status;
        }catch (SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return 0;
    }
    @Override public int delete(PersonneMorale demandeurMorale){
        String query = "DELETE FROM  demandeur_morale WHERE idDmd = ? ";
        int status = 0;
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setInt(1,demandeurMorale.getId());
            status = preparedStatement.executeUpdate();
            connection.commit();
        }catch (SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return status;
    }
    public enum ParamerterType {EMAIL,NUMTEL,NAME}
    public int getDemandeurIdBy(String parameter, ParamerterType type){
        String columnName = type.equals(ParamerterType.NAME) ? " nomDmd " :
                ((type.equals(ParamerterType.EMAIL) ? " emailDmd " : " numTelDmd "));
        String query = " SELECT idDmd FROM demandeur_morale where "+columnName+" = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1,parameter);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                return rs.getInt("idDmd");
        }catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return 0;
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
