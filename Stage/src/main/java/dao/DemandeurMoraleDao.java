package dao;
import Model.Enum.TypeDemandeur;
import Model.Pojo.business.PersonneMorale;
import Model.Pojo.business.PersonnePhysique;
import View.Model.ViewObject.AffaireForView;
import View.Model.ViewObject.RepresentantForView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DemandeurMoraleDao extends Dao implements DaoHelper<PersonneMorale>{

    private final String REPRESENTANT_QUERY = "SELECT dm.idDmd ,dm.nomDmd,rep.dateRepresentantion,aff.numAffaire" +
            " FROM demandeur_morale as dm , representant as rep , affaire as aff " +
            " WHERE rep.idPersonneMorale = dm.idDmd AND aff.dateFormulation = rep.dateRepresentantion AND dm.idDmd = ? ";

    private final String ACTUAL_REPRESENTANT_QUERY = REPRESENTANT_QUERY+" ORDER BY dateFormulation DESC ";

    private final String ALL_AFFAIRE_QUERY = " ";

    public DemandeurMoraleDao(Connection connection) {
        super(connection);
    }

    @Override public int create(PersonneMorale pm){
         if (pm.getEmail()!=null && pm.getNumTel()!=null) return storeWithEmailAndTel(pm);
         if (pm.getEmail()!=null && pm.getNumTel() == null) return storeWithEmailOnly(pm);
         if (pm.getEmail()==null && pm.getNumTel()!=null) return storeWithNumTelOnly(pm);
         else return storeWithoutEmailAndTel(pm);
    }

    private int storeWithEmailAndTel(PersonneMorale personneMorale){
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

    private int storeWithoutEmailAndTel(PersonneMorale personneMorale){
        String query = "INSERT INTO demandeur_morale (adresseDmd,nomDmd,nationalite,typeDmd) VALUES (?,?,?,?);";
        try (PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1, personneMorale.getAdresse());
            ps.setString(2,personneMorale.getNom());
            ps.setString(3, personneMorale.getNationalite());
            ps.setString(4, TypeDemandeur.typeDemandeur2String(personneMorale.getType()));
            int status = ps.executeUpdate();
            connection.commit();
            return status;
        }catch (SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return 0;
    }

    private int storeWithEmailOnly(PersonneMorale personneMorale){
        String query = "INSERT INTO demandeur_morale (adresseDmd,nomDmd,nationalite,typeDmd,emailDmd) VALUES (?,?,?,?,?);";
        try (PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1, personneMorale.getAdresse());
            ps.setString(2,personneMorale.getNom());
            ps.setString(3, personneMorale.getNationalite());
            ps.setString(4, TypeDemandeur.typeDemandeur2String(personneMorale.getType()));
            ps.setString(5,personneMorale.getEmail());
            int status = ps.executeUpdate();
            connection.commit();
            return status;
        }catch (SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return 0;
    }

    private int storeWithNumTelOnly(PersonneMorale personneMorale){
        String query = "INSERT INTO demandeur_morale (adresseDmd,nomDmd,nationalite,typeDmd,numTelDmd) VALUES (?,?,?,?,?);";
        try (PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1, personneMorale.getAdresse());
            ps.setString(2,personneMorale.getNom());
            ps.setString(3, personneMorale.getNationalite());
            ps.setString(4, TypeDemandeur.typeDemandeur2String(personneMorale.getType()));
            ps.setString(5,personneMorale.getNumTel());
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
        String query = " SELECT idDmd FROM demandeur_morale WHERE TRIM("+columnName+") = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1,parameter);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) return rs.getInt("idDmd");
        }catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, " Problem in the getDemandeurById method ", e);
        }
        return 0;
    }

    public ObservableList<AffaireForView> getAllAffaires(){
        return null;
    }

    public RepresentantForView getActualRepresentant(PersonneMorale pm){
        try(PreparedStatement ps = connection.prepareStatement(ACTUAL_REPRESENTANT_QUERY)){
            ps.setInt(1,pm.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) return createRepresentantForView(rs);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    private RepresentantForView createRepresentantForView(ResultSet rs){
        PersonneMorale pm = new PersonneMorale();
        try {
            pm.setId(rs.getInt("idDmd"));
            pm.setNom(rs.getString("nomDmd"));
            Date dateFormulation = rs.getDate("dateRepresentantion");
            String numAffaire = rs.getString("numAffaire");
            return new RepresentantForView(pm,dateFormulation,numAffaire);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public ObservableList<RepresentantForView> getAllRepresentant(PersonneMorale pm){
        ObservableList<RepresentantForView> observableList = FXCollections.observableArrayList();
        try(PreparedStatement ps = connection.prepareStatement(REPRESENTANT_QUERY)){
            ps.setInt(1,pm.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) observableList.add(createRepresentantForView(rs));
        }catch (SQLException e){
            e.printStackTrace();
        }
        return observableList;
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

    public ObservableList<PersonneMorale> findbyName(String name,TypeDemandeur typeDemandeur){
        ObservableList<PersonneMorale> observableList = FXCollections.observableArrayList();
        String query = " SELECT * FROM demandeur_morale  WHERE LOWER(TRIM(nomDmd)) LIKE CONCAT(?,'%') AND typeDmd = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1,name);
            ps.setString(2,TypeDemandeur.typeDemandeur2String(typeDemandeur));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) observableList.add(createPersonneMorale(rs));
            return observableList;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    private PersonneMorale createPersonneMorale(ResultSet rs){
        try {
            int idDmd = rs.getInt("idDmd");
            String nom = rs.getString("nomDmd");
            String addresse = rs.getString("adresseDmd");
            String email = rs.getString("emailDmd");
            String numTel = rs.getString("numTelDmd");
            String nationalite = rs.getString("nationalite");
            String typeDmd = rs.getString("typeDmd");
            PersonneMorale pm = new PersonneMorale();
            pm.setId(idDmd);
            pm.setNom(nom);
            pm.setAdresse(addresse);
            pm.setEmail(email);
            pm.setNumTel(numTel);
            pm.setType(TypeDemandeur.string2TypeDemandeur(typeDmd));
            pm.setNationalite(nationalite);
            return pm;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
