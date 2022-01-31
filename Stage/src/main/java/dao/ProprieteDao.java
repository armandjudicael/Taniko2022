package dao;

import model.pojo.business.other.Propriete;
import model.pojo.business.other.Titre;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProprieteDao extends Dao implements DaoHelper<Propriete>{
    public ProprieteDao(Connection connection) {
        super(connection);
    }
    @Override public int create(Propriete propriete) {
        String query = " INSERT INTO propriete (region,district,commune,quartier,parcelle,superficie) VALUES (? , ? , ? , ? , ? , ?) ;";
        int status = 0;
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, propriete.getRegion());
            ps.setString(2, propriete.getDistrict());
            ps.setString(3, propriete.getCommune());
            ps.setString(4, propriete.getQuartier());
            ps.setString(5, propriete.getParcelle());
            ps.setString(6, propriete.getSuperficie());
            status = ps.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return status;
    }

    @Override public int delete(Propriete propriete) {
        int status = 0;
        String query = " DELETE FROM propriete WHERE idPropriete= ? ;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, propriete.getIdPropriete());
            status = ps.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return status;
    }

    @Override public int update(Propriete propriete) { return 0; }
    public int getIdTerrainBy(String superficie,String parcelle,String quartier){
        int idTerrain = 0;
          String query = "SELECT idPropriete FROM  propriete WHERE ( TRIM(LOWER(superficie)) =  ? AND TRIM(LOWER(parcelle)) = ? AND TRIM(LOWER(quartier)) = ? );";
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setString(1,superficie.toLowerCase().trim());
            ps.setString(2,parcelle.toLowerCase().trim());
            ps.setString(3,quartier.toLowerCase().trim());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
              idTerrain = rs.getInt("idPropriete");
            }
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return idTerrain;
    }

    public boolean checkTerrainAndTitre(Propriete propriete){
        String query = " SELECT COUNT(tat.terrainId) AS nb FROM terrain_aboutir_titre AS tat WHERE tat.terrainId= ?";
        int value = 0;
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, propriete.getIdPropriete());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                value = rs.getInt("nb");
            }
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return value == 0 ? true : false;
    }

    public Propriete find(int idTerrain){
        String query = " SELECT "+
                "ti.numTitre," +
                "ti.nomPropriete,"+
                "te.superficie," +
                "te.idPropriete," +
                "te.quartier," +
                "te.parcelle," +
                "te.district," +
                "te.commune," +
                "te.region" +
                " FROM propriete AS te,terrain_depend_propriete as tat,titre as ti"+
                " WHERE te.idPropriete = ? AND  te.idPropriete = tat.terrainParentId AND tat.titreId = ti.idTitre;";

        Propriete propriete = new Propriete();
        Titre titre = new Titre();

        try {

            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setInt(1,idTerrain);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){

                titre.setNomPropriete(rs.getString("nomPropriete"));
                titre.setNumero(rs.getString("numTitre"));

                propriete.setSuperficie(rs.getString("superficie"));
                propriete.setCommune(rs.getString("commune"));
                propriete.setDistrict(rs.getString("district"));
                propriete.setQuartier(rs.getString("quartier"));
                propriete.setParcelle(rs.getString("parcelle"));
                propriete.setRegion(rs.getString("region"));

                propriete.setTitreDependant(null);
            }
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return propriete;

    }

    @Override public Propriete findById(int id){
        String query = " SELECT "+
                       " te.numTitre," +
                       " te.nomPropriete,"+
                       " te.superficie," +
                       " te.idPropriete" +
                       " FROM propriete AS te,terrain_depend_propriete as tat"+
                       " WHERE te.idPropriete = ? AND  te.idPropriete = tat.terrainParentId AND tat.titreId = ti.idTitre;";
        Propriete propriete = new Propriete();
        Titre titre = new Titre();
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                propriete.setSuperficie(rs.getString("superficie"));
                titre.setNomPropriete(rs.getString("nomPropriete"));
                titre.setNumero(rs.getString("numTitre"));
                propriete.setIdPropriete(rs.getInt("idTerrain"));
                propriete.setTitreDependant(null);
            }
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return propriete;
    }
    @Override public Propriete finByNum(int num) {
        return null;
    }
}
