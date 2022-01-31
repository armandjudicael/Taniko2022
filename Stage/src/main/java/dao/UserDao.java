package dao;
import model.Enum.ProcedureStatus;
import model.pojo.business.other.Dossier;
import model.pojo.business.other.User;
import model.other.RedactorAffair;
import model.other.UserForLogin;
import view.Model.ViewObject.EditorForView;
import view.Model.ViewObject.ProcedureForTableview;
import view.Model.ViewObject.UserForView;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UserDao extends Dao implements DaoHelper<User>{

    private final String LAST_PROCEDURE = "(SELECT CONCAT(p.nomProcedure,"+
            "'@',pca.numDepart," +
            "'@',DATE_FORMAT(pca.dateDepart,'%d-%m-%Y %H:%i:%s'),"+
            "'@',pca.numArrive,"+
            "'@',DATE_FORMAT(pca.dateArrive,'%d-%m-%Y %H:%i:%s'))"+
            "FROM _procedure as p INNER JOIN procedure_concerner_affaire AS pca ON p.idProcedure = pca.procedureId " +
            "WHERE pca.affaireId= AFF.idAffaire ORDER BY p.idProcedure desc LIMIT 1) AS LAST_PROCEDURE";

    private final String LAST_PROCEDURE1 = "(SELECT CONCAT(p.nomProcedure,"+
            "'@',pca.numDepart," +
            "'@',DATE_FORMAT(pca.dateDepart,'%d-%m-%Y %H:%i:%s'),"+
            "'@',pca.numArrive," +
            "'@',DATE_FORMAT(pca.dateArrive,'%d-%m-%Y %H:%i:%s'))"+
            "FROM _procedure as p INNER JOIN procedure_concerner_affaire AS pca ON p.idProcedure = pca.procedureId " +
            "WHERE pca.affaireId= aff.idAffaire ORDER BY p.idProcedure desc LIMIT 1) AS LAST_PROCEDURE";
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
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, e);
        }
        return image;
    }

    @Override public int create(User user){
        return 0;
    }
    @Override public int update(User user) {
      return 0;
    }
    @Override public int delete(User user) {
        return 0;
    }
    public int deleteUser(User user) {
        String query = " DELETE FROM  utilisateur WHERE idUtilisateur = ? ; ";
        int status = 1;
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setInt(1, user.getId());
            status = preparedStatement.executeUpdate();
            this.connection.commit();
        } catch (SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return status ;
    }

    @Override public User findById(int id) {
        User user = null;
        String query = "SELECT user.nom_utilisateur,user.mot_de_passe,dm.nomDmd,user.photo,user.type,user.userStatus,dp.profession " +
                USER_SUBQUERY+
                "WHERE idUtilisateur = ?;";
        ResultSet rs;
            try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
                preparedStatement.setInt(1,id);
                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    user = new User();
                    user.setUserName(rs.getString("nom_utilisateur"));
                    user.setPassword(rs.getString("mot_de_passe"));
                    user.setId(rs.getInt("idUtilisateur"));
                    user.setNom(rs.getString("nomDmd"));
                    user.setTypeUtilisateur(rs.getString("type"));
                    user.setPhoto(getImageFromBuffer(rs));
                    user.setProfession(rs.getString("profession"));
                    user.setStatus(rs.getInt("userStatus"));
                }
            } catch (SQLException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, " THERE ARE A ERRORS ON ' findById ' method ", e);
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
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return buf;
    }

    @Override public User finByNum(int num) {
        return null;
    }

    private final String USER_SUBQUERY  = " from utilisateur as user " +
            " inner join demandeur_physique dp on user.idUtilisateur = dp.idDmdPhysique " +
            " inner join demandeur_morale dm on dp.idDmdPhysique = dm.idDmd ";

    public User findByUserNameAndPassword(String userName, String password) {
        User user = null;
        String query = "SELECT user.idUtilisateur,dm.nomDmd,user.photo,user.type,user.userStatus,dp.profession " +
                USER_SUBQUERY+
                "WHERE ( BINARY nom_utilisateur = ? and BINARY mot_de_passe = ?  );";
            try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
                preparedStatement.setString(1,userName);
                preparedStatement.setString(2,password);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                   user = new User();
                   user.setUserName(userName);
                   user.setPassword(password);
                   user.setId(rs.getInt("idUtilisateur"));
                   user.setNom(rs.getString("nomDmd"));
                   user.setTypeUtilisateur(rs.getString("type"));
                   user.setPhoto(getImageFromBuffer(rs));
                   user.setProfession(rs.getString("profession"));
                   user.setStatus(rs.getInt("userStatus"));
                }
                return user;
        } catch (SQLException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return user;
    }

    private void checkUser(String userName,String password){
        String query = "  ";
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
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return -1;
    }

    public ObservableList<User> filterUser(String name,ObservableList<User> list ){
        String query = "";
        String[] text = {""};
        Boolean notNullAndNotEmpty = !list.isEmpty();
        if (notNullAndNotEmpty){
            query = " SELECT DISTINCT user.idUtilisateur as id ,dm.nomDmd,user.photo " +
                    USER_SUBQUERY+
                    "WHERE  " +
                    " (TRIM(LOWER(dm.nomDmd)) like CONCAT(?,'%') " +
                    " AND TRIM(LOWER(dm.nomDmd)) NOT LIKE CONCAT('%',?,'%'));";
            text[0] = list.stream().map(User::getNom).collect(Collectors.joining());
        }else{
            query = "SELECT DISTINCT user.idUtilisateur as id ,dm.nomDmd,user.photo " +
                    USER_SUBQUERY+
                    " WHERE  TRIM(LOWER(dm.nomDmd)) like CONCAT(?,'%')";
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
                int id = rs.getInt("idUtilisateur");
                user.setId(id);
                user.setNom(nom);
                user.setPhoto(getImageFromBuffer(rs));
                userObservableList.add(user);
            }
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return userObservableList;
    }

    public ObservableList<UserForView> getAllUsers(){
        ObservableList<UserForView> users = FXCollections.observableArrayList();
        String query = " SELECT " +
                "user.idUtilisateur," +
                "user.nom_utilisateur," +
                "user.mot_de_passe," +
                "dm.nomDmd," +
                "user.photo," +
                "user.type," +
                "user.userStatus," +
                "dp.profession " +
                USER_SUBQUERY +
                "where strcmp(dm.nomDmd,'kael')<>0;";

        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("idUtilisateur"));
                user.setNom(rs.getString("nom"));
                user.setUserName(rs.getString("nom_utilisateur"));
                user.setPassword(rs.getString("mot_de_passe"));
                user.setPhoto(getImageFromBuffer(rs));
                user.setStatus(rs.getInt("userStatus"));
                user.setProfession(rs.getString("fonction"));
                user.setTypeUtilisateur(rs.getString("type"));
                users.add(new UserForView(user));
            }
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
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
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return users;
    }

    public ObservableList<RedactorAffair> getAllActualAffairTraitedBy(User user,Boolean actual) {
        ObservableList<RedactorAffair> list = FXCollections.observableArrayList();
        String query = "";
        if (actual) {
            query = "SELECT " +
                    "aff.numAffaire," +
                    "(select dmd.nomDmd from demandeur_morale AS dmd where AFF.demandeurId = dmd.idDmd) as nom" +
                    ",AFF.situation" +
                    ",DATE_FORMAT(d.dateDispatch,'%d-%m-%Y %H:%i:%s') as DATE_DISPATCH ,"+LAST_PROCEDURE1+
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
                    "(select dmd.nomDmd from demandeur_morale AS dmd where AFF.demandeurId = dmd.idDmd) as nom" +
                    ",AFF.situation" +
                    ",DATE_FORMAT(d.dateDispatch,'%d-%m-%Y %H:%i:%s') as DATE_DISPATCH ,"+LAST_PROCEDURE+
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
                        , Dossier.string2AffaireStatus(rs.getString("situation")),
                        initSituation(rs.getString("situation"))
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Deprecated private ProcedureForTableview initSituation(String lastProcedure){
        String situation = "Auccune procedure";
        ProcedureForTableview procedureForTableview = new ProcedureForTableview(ProcedureStatus.NONE, new SimpleStringProperty());
        if (lastProcedure != null){
            String situationTab[] = lastProcedure.split("@", 5);
            if (situationTab.length == 5) {
                String nomProcedure = situationTab[0];
                String numeroDepart = situationTab[1];
                String dateDepart = situationTab[2];
                String numeroArrive = situationTab[3];
                String dateArrive = situationTab[4];
                if (!dateArrive.equals("30-07-1999 00:00:00")){
                    procedureForTableview.setStatus(ProcedureStatus.BACK);
                    if (!numeroArrive.isEmpty())
                        situation = nomProcedure + " N° " + numeroArrive + " du " + dateArrive;
                    else situation = nomProcedure + " du " + dateArrive;
                } else if (!dateDepart.equals("30-07-1999 00:00:00")) {
                    procedureForTableview.setStatus(ProcedureStatus.GO);
                    if (!numeroDepart.isEmpty()) {
                        situation = nomProcedure + " N° " + numeroDepart + " du " + dateDepart;
                    } else {
                        situation = nomProcedure + " du " + dateDepart;
                    }
                } else {
                    procedureForTableview.setStatus(ProcedureStatus.NONE);
                    situation = "Auccune procedure";
                }
                procedureForTableview.setDescription(situation);
            }
        } else {
            procedureForTableview.setDescription(situation);
            procedureForTableview.setStatus(ProcedureStatus.NONE);
        }
        return procedureForTableview;
    }

    // liste de redacteur de cette affaire ainsi que la date
    public ArrayList<EditorForView> getAllEditorAndDateByThis(Dossier dossier){
        ArrayList<EditorForView> listViews = new ArrayList<>();
        String query = "SELECT DISTINCT " +
                "u.idUtilisateur as id," +
                "dm.nomDmd," +
                "u.photo," +
                "d.dateDispatch" +
                " FROM utilisateur as u " +
                " INNER JOIN dispatch d on u.idUtilisateur = d.utilisateurId" +
                " INNER JOIN affaire a on d.affaireId = a.idAffaire " +
                " inner join demandeur_physique dp on u.idUtilisateur = dp.idDmdPhysique" +
                " inner join demandeur_morale dm on dp.idDmdPhysique = dm.idDmd" +
                " WHERE  a.idAffaire = ?  " +
                " ORDER BY d.dateDispatch desc";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, dossier.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                listViews.add(new EditorForView(
                        rs.getTimestamp("dateDispatch")
                        , rs.getString("nomDmd"),
                        getImageFromBuffer(rs),
                        rs.getInt("id")
                ));
            }
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return listViews;
    }

}
