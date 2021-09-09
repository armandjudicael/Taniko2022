package DAO;

import Model.Pojo.Demandeur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DemandeurDao extends DAO<Demandeur> {

    public DemandeurDao(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Demandeur demandeur) {

        String query = " INSERT INTO demandeur (nomDmd,prenomDmd,adresseDmd,lotDmd,parcelleDmd) VALUES (?,?,?,?,?);";
        int status = 0;
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setString(1, demandeur.getNom());
            preparedStatement.setString(2, demandeur.getPrenom());
            preparedStatement.setString(3, demandeur.getAdresse());
            preparedStatement.setString(4, demandeur.getLot());
            preparedStatement.setString(5, demandeur.getParcelle());
            status = preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    @Override
    public int delete(Demandeur demandeur) {
        String query = "DELETE FROM  demandeur WHERE id = ? ";
         int status = 0;
            try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
                preparedStatement.setInt(1, demandeur.getId());
                status = preparedStatement.executeUpdate();
                connection.commit();
            }
            catch (SQLException e) {
               e.printStackTrace();
            }
        return status;
    }

    @Override
    public int update(Demandeur demandeur) {

        String query = " UPDATE demandeur SET nom = ? , prenom = ?, adresse = ? , lot = ? , parcelle = ?  WHERE id = ? ; ";
        int status = 0 ;
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)){
            preparedStatement.setString(1, demandeur.getNom());
            preparedStatement.setString(2, demandeur.getPrenom());
            preparedStatement.setString(3, demandeur.getAdresse());
            preparedStatement.setString(4, demandeur.getLot());
            preparedStatement.setString(5, demandeur.getParcelle());
            preparedStatement.setInt(6, demandeur.getId());
            status = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    public Demandeur findById(int id) {
        Demandeur demandeur = new Demandeur();
        try {
            String query = "SELECT * FROM demandeur WHERE id = ?;";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){

                demandeur = new Demandeur(
                        id,
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("adresse"),
                        resultSet.getString("parcelle"),
                        resultSet.getString("lot")
                );

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return demandeur;
    }

    @Override
    public Demandeur finByNum(int num) {
        return null;
    }

    public Demandeur findDemandeurBy(int idAffaire) {
        Demandeur demandeur = new Demandeur();
        try {
            String query = "SELECT * FROM demandeur as dmd left join affaire on affaire.demandeurId = dmd.idDmd WHERE  affaire.idAffaire = ? ;";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1, idAffaire);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                demandeur = new Demandeur(
                        resultSet.getInt("idDmd"),
                        resultSet.getString("nomDmd"),
                        resultSet.getString("prenomDmd"),
                        resultSet.getString("adresseDmd"),
                        resultSet.getString("parcelleDmd"),
                        resultSet.getString("lotDmd")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return demandeur;
    }

    public  int getIdDemandeurByFullName(String name ,String firstName){
       int id = 0;
       String query = "SELECT  demandeur.idDmd  FROM demandeur where nomDmd = ? and  prenomDmd = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1,name);
            ps.setString(2,firstName);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next())
                id = resultSet.getInt("idDmd");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return id;
    }

    public String[] getNameAndFirstNameById(int id){

        String tmp [] = new String[2];
        String query = " SELECT nom,prenom FROM demandeur WHERE id = ? ;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
                         ps.setInt(1,id);
                         ResultSet rs = ps.executeQuery();
                         while (rs.next()){
                          tmp[0]=rs.getString("nom");
                          tmp[1]= rs.getString("prenom");
                         }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tmp;
    }

//    public ArrayList<Affaire> getAllAffaireCreatedByThis(Demandeur demandeur){
//        ArrayList<Affaire>  affaireArrayList = new ArrayList<>();
//       String query = " SELECT * FROM affaire where affaire.id_demandeur = ? ; ";
//       try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
//                preparedStatement.setInt(1,demandeur.getId());
//                ResultSet rs = preparedStatement.executeQuery();
//                while (rs.next()){
//                    affaireArrayList.add(
//                            new Affaire(
//                                    rs.getString("numero"),
//                                    rs.getTimestamp("date_formulation"),
//                                    DaoFactory.getDemandeurDao().findById(rs.getInt("id_demandeur")),
//                                    rs.getString("superficie"),
//                                    rs.getString("region"),
//                                    rs.getString("district"),
//                                    rs.getString("commune"),
//                                    rs.getString("parcelle"),
//                                    rs.getString("quartier"),
//                                    string2TypeDemande(rs.getString("type_demande")),
//                                    rs.getInt("id"),
//                                    string2UniteDeMesure(rs.getString("unite_de_mesure")),
//                                    string2Observation(rs.getString("observation")),
//                                    rs.getString("propriete_dependant"),
//                                    rs.getString("num_titre_propriete"),null,null
//                            )
//                    );
//                }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return affaireArrayList;
//    }
}
