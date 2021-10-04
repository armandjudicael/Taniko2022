package DAO;

import Model.Pojo.Affaire;
import Model.Pojo.User;
import Model.Other.RedactorAffair;
import Model.Other.UserForLogin;
import View.Model.EditorForView;
import View.Model.UserForView;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.BufferedInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class UserDao extends  DAO<User> {

    public UserDao(Connection connection) {
        super(connection);
    }
    public static Image getImageFromBuffer(ResultSet rs) {
        Image image = null;
        try {
            if (rs.getBinaryStream("photo") != null) {
                image = new Image(rs.getBinaryStream("photo"));
            } else {
                image = new Image(UserDao.class.getResourceAsStream("/img/male_user_100px.png"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override public int create(User user){
        String query = "";
        if (user.getInsertPhoto() != null)
            query = "INSERT INTO utilisateur (nom_utilisateur,mot_de_passe,nom,prenom,type,fonction,photo) VALUES(?,?,?,?,?,?,?);";
        else
            query = "INSERT INTO utilisateur (nom_utilisateur,mot_de_passe,nom,prenom,type,fonction) VALUES(?,?,?,?,?,?);";
        int status = 0;
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
                preparedStatement.setString(1, user.getUserName());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getNom());
                preparedStatement.setString(4, user.getPrenom());
            preparedStatement.setString(5, user.getType());
            preparedStatement.setString(6, user.getFonction());
            if (user.getInsertPhoto() != null)
                preparedStatement.setBlob(7, user.getInsertPhoto());
                status = preparedStatement.executeUpdate();
                this.connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    @Override public int update(User user) {
      return 0;
    }

    @Override
    public int delete(User user) {
        return 0;
    }

    public int deleteUser(User user) {
        String query = " DELETE FROM  utilisateur WHERE idUtilisateur = ? ; ";
        int status = 1;
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setInt(1, user.getId());
            status = preparedStatement.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status ;
    }

    @Override
    public User findById(int id) {

        BufferedInputStream buffer = null;
        User user = new User();
        String query = "SELECT * FROM utilisateur WHERE idUtilisateur= ?;";
        ResultSet rs;
            try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
                preparedStatement.setInt(1,id);
                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    user = new User(id,
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            getImageFromBuffer(rs),
                            rs.getString("nom_utilisateur"),
                            rs.getString("mot_de_passe"),
                            rs.getString("type"),
                            rs.getString("fonction"),
                            rs.getInt("userStatus"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return user;
    }

    public ArrayList<BufferedInputStream> getPhoto() {
        String query = "SELECT utilisateur.photo FROM utilisateur ;";
        ResultSet rs;
        ArrayList<BufferedInputStream> buf = new ArrayList<>();
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                buf.add(new BufferedInputStream(rs.getBinaryStream("photo")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buf;
    }

    @Override
    public User finByNum(int num) {
        return null;
    }

    public User findByUserNameAndPassword(String userName, String password) {

        User user = null;
        BufferedInputStream buffer = null;

        String query = "SELECT * from utilisateur  WHERE ( nom_utilisateur = ? and mot_de_passe = ? );";
            try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
                preparedStatement.setString(1,userName);
                preparedStatement.setString(2,password);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    user = new User(rs.getInt("idUtilisateur"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            getImageFromBuffer(rs),
                            rs.getString("nom_utilisateur"),
                            rs.getString("mot_de_passe"),
                            rs.getString("type"),
                            rs.getString("fonction"),
                            rs.getInt("userStatus"));
                }
                return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public int checkAdmin(String adminPassword) {
        String query = "SELECT count(user.idUtilisateur) as value FROM utilisateur as user WHERE user.type='administrateur' and user.mot_de_passe  = ?; ";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, adminPassword);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("value");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public ArrayList<User> checkUser(String name) {
        String query = "SELECT distinct user.idUtilisateur as id ,user.nom,user.prenom,user.photo FROM utilisateur as user WHERE (user.nom like CONCAT(?,'%') or user.prenom like CONCAT(?,'%'));";
        ArrayList<User> observableList = new ArrayList<User>();
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {

            ps.setString(1, name);
            ps.setString(2, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                User user = new User();
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int id = rs.getInt("id");

                user.setId(id);
                user.setNom(nom);
                user.setPrenom(prenom);
                user.setPhoto(getImageFromBuffer(rs));

                observableList.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return observableList;
    }

    public ObservableList<User> filterUser(String name,ObservableList<User> list ){
        String query = "";
        String[] text = {""};
        Boolean notNullAndNotEmpty = !list.isEmpty();
        if (notNullAndNotEmpty){
            query = " SELECT DISTINCT user.idUtilisateur as id ,user.nom,user.prenom,user.photo " +
                    " FROM utilisateur as user" +
                    " WHERE  " +
                    " (TRIM(LOWER(user.nom)) like CONCAT(?,'%') " +
                    " or TRIM(LOWER(user.prenom)) like CONCAT(?,'%')) " +
                    " AND TRIM(LOWER(user.nom)) NOT LIKE CONCAT('%',?,'%');";
            text[0] = list.stream().map(User::getNom).collect(Collectors.joining());
        }else{
            query = "SELECT DISTINCT user.idUtilisateur as id ,user.nom,user.prenom,user.photo " +
                    " FROM utilisateur as user" +
                    " WHERE  ( TRIM(LOWER(user.nom)) like CONCAT(?,'%') or LOWER(TRIM(user.prenom)) like CONCAT(?,'%'))";
        }
        ObservableList<User> userObservableList = FXCollections.observableArrayList();

        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, name);
            if (notNullAndNotEmpty){
                ps.setString(3, text[0]);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                User user = new User();
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int id = rs.getInt("id");

                user.setId(id);
                user.setNom(nom);
                user.setPrenom(prenom);
                user.setPhoto(getImageFromBuffer(rs));

                userObservableList.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userObservableList;
    }

    public ObservableList<UserForView> getAllUsers(){
        ObservableList<UserForView> users = FXCollections.observableArrayList();
        String query = " SELECT * FROM utilisateur where strcmp(utilisateur.nom,'kael')<>0;";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("idUtilisateur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        getImageFromBuffer(rs),
                        rs.getString("nom_utilisateur"),
                        rs.getString("mot_de_passe"),
                        rs.getString("type"),
                        rs.getString("fonction"),
                        rs.getInt("userStatus"));
                users.add(new UserForView(user));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public ObservableList<UserForLogin> getAllUserNameAndPasswordForLogin() {
        ObservableList<UserForLogin> users = FXCollections.observableArrayList();
        String query = " SELECT utilisateur.type,utilisateur.idUtilisateur as id, utilisateur.nom_utilisateur , utilisateur.mot_de_passe FROM utilisateur";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                users.add(new UserForLogin(
                        new SimpleStringProperty(rs.getString("nom_utilisateur")),
                        new SimpleStringProperty(rs.getString("mot_de_passe")),
                        new SimpleIntegerProperty(rs.getInt("id")),
                        new SimpleStringProperty(rs.getString("type"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public ObservableList<RedactorAffair> getAllActualAffairTraitedBy(User user, Boolean actual) {
        ObservableList<RedactorAffair> list = FXCollections.observableArrayList();
        String query = "";

        if (actual) {
            query = "SELECT " +
                    "aff.numAffaire," +
                    "(select concat(dmd.nomDmd,' ',dmd.prenomDmd) from demandeur AS dmd where AFF.demandeurId = dmd.idDmd) as nom" +
                    ",AFF.situation" +
                    ",DATE_FORMAT(d.dateDispatch,'%d-%m-%Y %H:%i:%s') as DATE_DISPATCH " +
                    " FROM affaire as aff inner join dispatch d on aff.idAffaire = d.affaireId WHERE " +
                    "(SELECT u.idUtilisateur " +
                    " FROM utilisateur as u " +
                    " INNER JOIN dispatch d2 on u.idUtilisateur = d2.utilisateurId " +
                    " WHERE d2.affaireId = AFF.idAffaire " +
                    " ORDER BY d2.dateDispatch desc limit 1 ) = ? " +
                    "" +
                    "order by AFF.idAffaire ;";
        } else {
            query = "SELECT " +
                    "AFF.numAffaire," +
                    "(select concat(dmd.nomDmd,' ',dmd.prenomDmd) from demandeur AS dmd where AFF.demandeurId = dmd.idDmd) as nom" +
                    ",AFF.situation" +
                    ",DATE_FORMAT(d.dateDispatch,'%d-%m-%Y %H:%i:%s') as DATE_DISPATCH " +
                    " FROM affaire AS AFF inner join" +
                    " dispatch d on AFF.idAffaire = d.affaireId WHERE "+
                    " d.utilisateurId  = ? "+
                    " order by AFF.idAffaire ;";
        }

        try {

            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new RedactorAffair(
                        rs.getString("AFF.numAffaire"),
                        rs.getString("nom"),
                        rs.getString("DATE_DISPATCH")
                        ,Affaire.string2AffaireStatus(rs.getString("situation"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // liste de redacteur de cette affaire ainsi que la date
    public ArrayList<EditorForView> getAllEditorAndDateByThis(Affaire affaire){
        ArrayList<EditorForView> listViews = new ArrayList<>();
        String query = "SELECT DISTINCT " +
                "u.idUtilisateur as id," +
                "CONCAT(u.nom,' ',u.prenom) AS fullName," +
                "u.photo," +
                "d.dateDispatch" +
                " FROM utilisateur as u INNER JOIN" +
                " dispatch d on u.idUtilisateur = d.utilisateurId" +
                " INNER JOIN affaire a on d.affaireId = a.idAffaire " +
                " WHERE  a.idAffaire = ?  " +
                " ORDER BY d.dateDispatch desc";

        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, affaire.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                listViews.add(new EditorForView(
                        rs.getTimestamp("dateDispatch")
                        , rs.getString("fullName"),
                        getImageFromBuffer(rs),
                        rs.getInt("id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listViews;
    }
}
