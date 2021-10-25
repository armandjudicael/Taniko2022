package DAO;

import Model.Pojo.Terrain;
import Model.Pojo.Titre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TerrainDao extends Dao implements DaoHelper<Terrain> {
    public TerrainDao(Connection connection) {
        super(connection);
    }
    @Override public int create(Terrain terrain) {
        String query = " INSERT INTO terrain (region,district,commune,quartier,parcelle,superficie) VALUES (? , ? , ? , ? , ? , ?) ;";
        int status = 0;
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1,terrain.getRegion());
            ps.setString(2,terrain.getDistrict());
            ps.setString(3,terrain.getCommune());
            ps.setString(4,terrain.getQuartier());
            ps.setString(5,terrain.getParcelle());
            ps.setString(6,terrain.getSuperficie());
            status = ps.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    @Override public int delete(Terrain terrain) {
        int status = 0;
        String query = " DELETE FROM terrain WHERE idTerrain= ? ;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1,terrain.getIdTerrain());
            status = ps.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    @Override public int update(Terrain terrain) { return 0; }

    public int getIdTerrainBy(String superficie,String parcelle,String quartier){
        int idTerrain = 0;
          String query = "SELECT idTerrain FROM  terrain WHERE ( TRIM(LOWER(superficie)) =  ? AND TRIM(LOWER(parcelle)) = ? AND TRIM(LOWER(quartier)) = ? );";
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setString(1,superficie.toLowerCase().trim());
            ps.setString(2,parcelle.toLowerCase().trim());
            ps.setString(3,quartier.toLowerCase().trim());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
              idTerrain = rs.getInt("idTerrain");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idTerrain;
    }

    public boolean checkTerrainAndTitre(Terrain terrain){
        String query = " SELECT COUNT(tat.terrainId) AS nb FROM terrain_aboutir_titre AS tat WHERE tat.terrainId= ?";
        int value = 0;
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1,terrain.getIdTerrain());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                value = rs.getInt("nb");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value == 0 ? true : false;
    }

    public Terrain find(int idTerrain){

        String query = " SELECT "+
                "ti.numTitre," +
                "ti.nomPropriete,"+
                "te.superficie," +
                "te.idTerrain," +
                "te.quartier," +
                "te.parcelle," +
                "te.district," +
                "te.commune," +
                "te.region" +
                " FROM terrain AS te,terrain_depend_titre as tat,titre as ti"+
                " WHERE te.idTerrain = ? AND  te.idTerrain = tat.terrainId AND tat.titreId = ti.idTitre;";
        Terrain terrain = new Terrain();
        Titre titre = new Titre();

        try {

            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setInt(1,idTerrain);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){

                titre.setNomPropriete(rs.getString("nomPropriete"));
                titre.setNumero(rs.getString("numTitre"));

                terrain.setSuperficie(rs.getString("superficie"));
                terrain.setCommune(rs.getString("commune"));
                terrain.setDistrict(rs.getString("district"));
                terrain.setQuartier(rs.getString("quartier"));
                terrain.setParcelle(rs.getString("parcelle"));
                terrain.setRegion(rs.getString("region"));

                terrain.setTitreDependant(titre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return terrain;

    }

    @Override public Terrain findById(int id){

        String query = " SELECT "+
                       " ti.numTitre," +
                       " ti.nomPropriete,"+
                       " te.superficie," +
                       " te.idTerrain" +
                       " FROM terrain AS te,terrain_depend_titre as tat,titre as ti"+
                       " WHERE te.idTerrain = ? AND  te.idTerrain = tat.terrainId AND tat.titreId = ti.idTitre;";

        Terrain terrain = new Terrain();
        Titre titre = new Titre();
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                terrain.setSuperficie(rs.getString("superficie"));
                titre.setNomPropriete(rs.getString("nomPropriete"));
                titre.setNumero(rs.getString("numTitre"));
                terrain.setIdTerrain(rs.getInt("idTerrain"));
                terrain.setTitreDependant(titre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return terrain;
    }



    @Override public Terrain finByNum(int num) {
        return null;
    }
}
