package DAO;

import Model.Pojo.PersonnePhysique;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DemandeurPhysiqueDao extends Dao implements DaoHelper<PersonnePhysique>{

    private final String PERSONNE_PHYSIQUE = "  ";

    public DemandeurPhysiqueDao(Connection connection) {
        super(connection);
    }
    @Override public int create(PersonnePhysique demandeurPhysique) {
        if (demandeurPhysique.getConjoint()!=null)
            return createDemandeurPhysiqueMarie(demandeurPhysique);
        return createDemandeurPhysiqueCelibataire(demandeurPhysique);
    }
    @Override public int delete(PersonnePhysique personnePhysique) {
        return 0;
    }
    @Override public int update(PersonnePhysique personnePhysique) {
        return 0;
    }
    private int createDemandeurPhysiqueCelibataire(PersonnePhysique demandeurPhysique){
        String query = " INSERT INTO demandeur_physique ("+
                "idDmdPhysique," +
                "lotDmd," +
                "parcelleDmd," +
                "lieuDeNaissanceDmd," +
                "dateDeNaissanceDmd," +
                "situationMatrimoniale," +
                "sexe" +
                ") VALUES (?,?,?,?,?,?,?,?,?);";


        try (PreparedStatement ps = this.connection.prepareStatement(query)){
            ps.setInt(1, demandeurPhysique.getId());
            ps.setString(3, demandeurPhysique.getAdresse());
            ps.setString(4, demandeurPhysique.getLot());
            ps.setString(5, demandeurPhysique.getParcelle());
            ps.setString(6, demandeurPhysique.getLieuDeNaissance());
            ps.setTimestamp(7, demandeurPhysique.getDateDeNaissance());
            ps.setString(8, demandeurPhysique.getSituationMatrimoniale());
            ps.setString(9, demandeurPhysique.getSexe());
            int status = ps.executeUpdate();
            connection.commit();
            return status;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private int createDemandeurPhysiqueMarie(PersonnePhysique demandeurPhysique){
        String query = " INSERT INTO demandeur_physique ("+
                " idDmdPhysique," +
                " lotDmd," +
                " parcelleDmd," +
                " lieuDeNaissanceDmd," +
                " dateDeNaissanceDmd," +
                " situationMatrimoniale," +
                " profession," +
                " sexe" +
                ") VALUES (?,?,?,?,?,?,?,?);";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, demandeurPhysique.getId());
            ps.setString(2, demandeurPhysique.getLot());
            ps.setString(3, demandeurPhysique.getParcelle());
            ps.setString(4, demandeurPhysique.getLieuDeNaissance());
            ps.setTimestamp(5, demandeurPhysique.getDateDeNaissance());
            ps.setString(6, demandeurPhysique.getSituationMatrimoniale());
            ps.setString(7, demandeurPhysique.getProfession());
            ps.setString(8,demandeurPhysique.getSexe());
            int status = ps.executeUpdate();
            connection.commit();
            return status;
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            dp.setDateDeNaissance(rs.getTimestamp("dateDeNaissanceDmd"));
            dp.setSituationMatrimoniale(rs.getString("SituationMatrimoniale"));
            dp.setProfession(rs.getString("profession"));
            dp.setConjoint(new PersonnePhysique());
        } catch (SQLException exception){
            exception.printStackTrace();
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
                    "lotDmd," +
                    "parcelleDmd," +
                    "lieuDeNaissanceDmd," +
                    "dateDeNaissanceDmd," +
                    "situationMatrimoniale" +
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
