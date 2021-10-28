package dao;

import Model.Pojo.Titre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TitleDao extends Dao implements DaoHelper<Titre> {

//    private static final String ALL_TITLE_COLUMN = "T.idTitre," +
//            "T.numTitre," +
//            "T.nomProperite," +
//            "DATE_FORMAT(T.dateCreation,'%d-%m-%Y %H:%i:%s') AS DATE_CREATION ," +
//            "T.numMorcelement";

private static final String ALL_TITLE_COLUMN = "T.idTitre AS IDENTIFIANT ," +
        "T.numTitre AS NUMERO_TITRE ," +
        "T.nomPropriete AS NOM_PROPERIETE," +
        "DATE_FORMAT(T.dateCreation,'%d-%m-%Y %H:%i:%s') AS DATE_CREATION ," +
        "T.numMorcelement AS MORCELEMENT ";


    private static final String EX_AFFAIRE = "(SELECT a.numAffaire from " +
            "terrain_aboutir_titre as tat " +
            "inner join terrain t on tat.terrainId = t.idTerrain " +
            "inner join affaire a on t.idTerrain = a.terrainId where (tat.titreId = T.idTitre AND strcmp(a.situation,'REJETER') <> 0 )) AS EX_AFFAIRE";

    private static final String APPLICANT_FULLNAME = "(SELECT "+
            "CONCAT(DM.nomDmd,' ',DM.prenomDmd) " +
            "FROM demandeur as DM inner join affaire a on DM.idDmd = a.demandeurId " +
            "inner join terrain t on a.terrainId = t.idTerrain " +
            "inner join terrain_aboutir_titre tat on t.idTerrain = tat.terrainId " +
            "WHERE tat.titreId =T.idTitre) AS APPLICANT_FULLNAME ";

    private static final String NUMERO_TITRE_MERE = "(SELECT t1.numTitre from titre as t1 "+
            "inner join terrain_aboutir_titre t2 on t1.idTitre = t2.titreId "+
            "inner join terrain t on t2.terrainId = t.idTerrain " +
            "inner join terrain_depend_titre tdt on t.idTerrain = tdt.terrainId" +
            " where t2.titreId = T.idTitre AND tdt.titreId = t1.idTitre ) AS NUM_TITRE_MERE";

    public ObservableList<Titre> getTitleBy(String dateCreation) {
        ObservableList<Titre> list = FXCollections.observableArrayList();

        /*
        *  ON PREND LES TITRES DONT LA DATE DE CREATION EST L'ANNE ACTUELLE ET L'EXAFFAIRE EST NON NULL
        *  CAR CERTAIN TITRE MERE N'ONT PAS D'EXAFFAIRE PAR EXEMPLE  " LE JARDIN BOTANNIQUE "
        * */

        String query = "SELECT "+
                FULL_TITLE_INFORMATION +
                " FROM titre AS T " +
                " WHERE  year(T.dateCreation)= ?" +
                " AND (SELECT a.numAffaire from "+
                "            terrain_aboutir_titre as tat "+
                "            inner join terrain t on tat.terrainId = t.idTerrain "+
                "            inner join affaire a on t.idTerrain = a.terrainId where (tat.titreId = T.idTitre AND strcmp(a.situation,'REJETER') <> 0 )) is not null ;";

        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, dateCreation);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next())
                list.add(createTitle(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    Titre createTitle(ResultSet resultSet) {
        try {
            String nomPropriete = resultSet.getString("NOM_PROPERIETE");
            int id = resultSet.getInt("IDENTIFIANT");
            String morcelement = resultSet.getString("MORCELEMENT");
            String numeroTitre = resultSet.getString("NUMERO_TITRE");
            String exAffaire = resultSet.getString("EX_AFFAIRE");
            String fullName = resultSet.getString("APPLICANT_FULLNAME");
            String dateCreation = resultSet.getString("DATE_CREATION");
            String numeroTitreMere = resultSet.getString("NUM_TITRE_MERE");

            return new Titre(numeroTitre,
                    nomPropriete,
                    fullName,
                    id,
                    numeroTitreMere,
                    morcelement,
                    dateCreation,
                    exAffaire);

        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

//    private static final String NUMERO_TITRE_MERE = "SELECT "+
//            ALL_TITLE_COLUMN + " , " +
//            " a.numAffaire as NUM_EXAFFAIRE ," +
//            "t1.numTitre as TITRE_DEPENDANT , " +
//            "CONCAT(d.nomDmd,' ',d.prenomDmd) AS DEMANDEUR , " +
//            "from titre as T , titre as t1 " +
//            "inner join terrain_aboutir_titre tat on t1.idTitre = tat.titreId " +
//            "inner join terrain t on tat.terrainId = t.idTerrain " +
//            "inner join terrain_depend_titre tdt on t.idTerrain = tdt.terrainId " +
//            "inner join affaire a on t.idTerrain = a.terrainId " +
//            "inner join demandeur d on a.demandeurId = d.idDmd " +
//            " WHERE tat.titreId = T.idTitre AND tdt.titreId = t1.idTitre ;";

    private static final String FULL_TITLE_INFORMATION =
            ALL_TITLE_COLUMN + "," +
                    EX_AFFAIRE + "," +
                    APPLICANT_FULLNAME +
                    "," + NUMERO_TITRE_MERE;

    public TitleDao(Connection connection) {
        super(connection);
    }

    public ObservableList<Titre> getAlltitle() {
        ObservableList<Titre> list = FXCollections.observableArrayList();
        String query = "SELECT " + FULL_TITLE_INFORMATION + " FROM titre AS T ORDER BY T.idTitre ;";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                list.add(createTitle(resultSet));
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }


    @Override
    public int create(Titre titre){
        String query = "";
        query = "INSERT INTO titre (numTitre,nomPropriete,dateCreation,numMorcelement) VALUES(?,?,?,?);";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, titre.getNumero());
            ps.setString(2, titre.getNomPropriete());
            ps.setTimestamp(3, titre.getDate());
            ps.setString(4, titre.getNumMorcelement());
            int status = ps.executeUpdate();
            this.connection.commit();
            return status;
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return 0;
    }

    @Override public int delete(Titre titre){
        String query = " DELETE FROM  titre  WHERE numTitre= ? ; ";
        int status = 0;
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setString(1, titre.getNumero());
            status = preparedStatement.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return status;
    }

    public int deleteById(Titre titre){
        String query = " DELETE FROM  titre  WHERE idTitre= ?; ";
        int status = 0;
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setInt(1, titre.getId());
            status = preparedStatement.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return status;
    }

    public ObservableList<String> groupeTitleByDate() {
        ObservableList<String> list = FXCollections.observableArrayList();
        String query = " SELECT distinct YEAR(dateCreation) AS DT FROM titre ORDER BY DT DESC ;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(rs.getString("DT"));
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    @Override public int update(Titre titre){
        String query = " UPDATE titre SET numTitre = ? , nomPropriete = ? , numMorcelement = ?,dateCreation = ? WHERE idTitre = ? ;";
        int status = 0;
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1,titre.getNumero());
            ps.setString(2,titre.getNomPropriete());
            ps.setString(3,titre.getNumMorcelement());
            ps.setTimestamp(4,titre.getDate());
            ps.setInt(5,titre.getId());
            status = ps.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return status;
    }

    @Override public Titre findById(int id) {
        return null;
    }
    @Override public Titre finByNum(int num) {
        return null;
    }
    public ObservableList<Titre> findByNumOrName(String numeroOrName) {
        numeroOrName = numeroOrName.toLowerCase().trim();
        ObservableList<Titre> observableList = FXCollections.observableArrayList();
        String query = " SELECT DISTINCT T.idTitre,T.numTitre,T.nomPropriete " +
                       " FROM titre as T " +
                       " WHERE ( strcmp(LOWER(TRIM(T.numTitre)),?)=0 or TRIM(LOWER(T.nomPropriete)) LIKE CONCAT(?,'%'));";
        try (PreparedStatement ps = this.connection.prepareStatement(query)){
            ps.setString(1, numeroOrName);
            ps.setString(2, numeroOrName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Titre titre = new Titre();
                int id = rs.getInt("T.idTitre");
                String numero = rs.getString("T.numTitre");
                String nomProperiete = rs.getString("T.nomPropriete");

                titre.setId(id);
                titre.setNumero(numero);
                titre.setNomPropriete(nomProperiete);
                observableList.add(titre);
            }
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }

        return observableList;
    }

    public ObservableList<Titre> find(String numeroOrName) {

        ObservableList<Titre> observableList = FXCollections.observableArrayList();

        String query = "SELECT "+FULL_TITLE_INFORMATION +" FROM titre as T WHERE ( strcmp(T.numTitre,?)=0 or TRIM(LOWER(T.nomPropriete)) LIKE CONCAT(?,'%'));";

        try (PreparedStatement ps = this.connection.prepareStatement(query)){

            ps.setString(1, numeroOrName.toLowerCase());
            ps.setString(2, numeroOrName.toLowerCase());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                observableList.add(createTitle(rs));
            }
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return observableList;
    }

    public int findByNum(String num) {
        int id = 0;
        String query = " SELECT idTitre from titre where numTitre = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, num);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                id = rs.getInt("idTitre");
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return id;
    }

    public Boolean checkNumTitle(String num) {
        String query = " SELECT COUNT(T.idTitre) AS nb FROM titre AS T where TRIM(T.numTitre) = ?";
        int value = 0;
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, num.trim());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                value = rs.getInt("nb");
            }
        } catch (SQLException e) {             Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e); }
        return value == 0;
    }

    public Boolean checkNumMorcelement(String num){
        String query = " SELECT COUNT(T.idTitre) AS nb FROM titre AS T where TRIM(T.numMorcelement) = ?";
        int value = 0;
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, num.trim());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                value = rs.getInt("nb");
            }
        } catch (SQLException e) { Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e); }
        return value == 0;
    }
}
