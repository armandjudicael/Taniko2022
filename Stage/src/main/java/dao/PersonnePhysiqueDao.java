package dao;

import model.pojo.business.other.PersonnePhysique;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonnePhysiqueDao extends Dao implements DaoHelper<PersonnePhysique>{
    private final String PERSONNE_PHYSIQUE = "  ";
    public PersonnePhysiqueDao(Connection connection) { super(connection); }
    @Override public int create(PersonnePhysique dp){
        String query = " INSERT INTO demandeur_physique(" +
                "idDmdPhysique," +
                "lotDmd," +
                "parcelleDmd," +
                "lieuDeNaissanceDmd," +
                "dateDeNaissanceDmd," +
                "pere," +
                "mere," +
                "profession," +
                "sexe," +
                "situationMatrimoniale," +
                "cin," +
                "dateDelivrance," +
                "lieuDelivrance) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1,dp.getId());
            ps.setString(2,dp.getLot());
            ps.setString(3,dp.getParcelle());
            ps.setString(4,dp.getLieuDeNaissance());
            ps.setDate(5,dp.getDateDeNaissance());
            ps.setString(6,dp.getPere());
            ps.setString(7,dp.getMere());
            ps.setString(8,dp.getProfession());
            ps.setString(9,dp.getSexe());
     //       ps.setString(10,dp.getSituationMatrimoniale());
            ps.setString(11,dp.getNumCin());
            ps.setDate(12,dp.getDateDelivranceCin());
            ps.setString(13,dp.getLieuDelivranceCin());
            int i = ps.executeUpdate();
            this.connection.commit();
            return i;
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return 0;
    }

    @Override public int delete(PersonnePhysique personnePhysique) {
        return 0;
    }
    @Override public int update(PersonnePhysique personnePhysique) {
        return 0;
    }
    @Override public PersonnePhysique findById(int id) {
        return null;
    }
    @Override public PersonnePhysique finByNum(int num) {
        return null;
    }

    private PersonnePhysique createDemandeurPhysique(ResultSet rs){
        PersonnePhysique dp = new PersonnePhysique();
        try {
            dp.setId(rs.getInt("idDmd"));
            dp.setNumTel(rs.getString("numTelDmd"));
            dp.setEmail(rs.getString("emailDmd"));
            dp.setNom(rs.getString("nomDmd"));
            dp.setAdresse(rs.getString("adresseDmd"));
            dp.setLot(rs.getString("lotDmd"));
            dp.setParcelle(rs.getString("parcelleDmd"));
            dp.setLieuDeNaissance(rs.getString("lieuDeNaissanceDmd"));
            dp.setDateDeNaissance(rs.getDate("dateDeNaissanceDmd"));
       //     dp.setSituationMatrimoniale(rs.getString("SituationMatrimoniale"));
            dp.setProfession(rs.getString("profession"));
            dp.setSexe(rs.getString("sexe"));
        } catch (SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public PersonnePhysique findDemandeurBy(int idAffaire){
        PersonnePhysique demandeurPhysique = new PersonnePhysique();
        try {
            String query = "SELECT "+
                    "idDmd," +
                    "numTelDmd," +
                    "emailDmd," +
                    "nomDmd," +
                    "adresseDmd," +
                    "nationalite," +
                    "lotDmd," +
                    "parcelleDmd," +
                    "lieuDeNaissanceDmd," +
                    "dateDeNaissanceDmd," +
                    "situationMatrimoniale," +
                    "pere," +
                    "mere," +
                    "profession," +
                    "sexe" +
                    " FROM affaire as AFF , demandeur_physique as DF ,demandeur_morale as DM "+
                    " WHERE  AFF.idAffaire = ? AND AFF.demandeurId = DM.idDmd AND DM.idDmd = DF.idDmdPhysique;";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1, idAffaire);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) demandeurPhysique = createDemandeurPhysique(rs);
            return demandeurPhysique;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return demandeurPhysique;
    }


}
