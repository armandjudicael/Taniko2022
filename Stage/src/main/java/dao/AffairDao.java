package dao;

import Model.Enum.AffaireStatus;
import Model.Enum.ProcedureStatus;
import Model.Pojo.*;
import View.Model.ViewObject.ProcedureForTableview;
import View.Model.ViewObject.AffaireForView;
import View.Model.ViewObject.ConnexAffairForView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static dao.UserDao.getImageFromBuffer;
import static Model.Pojo.Affaire.*;

public class AffairDao extends Dao implements DaoHelper<Affaire>{

    private final String APPLICANT_FULLNAME = "( SELECT DMD.nomDmd FROM demandeur_morale AS DMD WHERE DMD.idDmd = AFF.demandeurId ) AS APPLICANT_FULLNAME ";

    private final String LAST_REDACTOR = "(SELECT CONCAT(u.nom,'@',u.prenom,'@',u.idUtilisateur)"+
            " FROM utilisateur AS u INNER JOIN dispatch d on u.idUtilisateur = d.utilisateurId"+
            " WHERE d.affaireId = AFF.idAffaire " +
            " ORDER BY d.dateDispatch desc limit 1) AS LAST_REDACTOR ";

    private final String LAST_PROCEDURE = "(SELECT CONCAT(p.nomProcedure,"+
            "'@',pca.numDepart," +
            "'@',DATE_FORMAT(pca.dateDepart,'%d-%m-%Y %H:%i:%s'),"+
            "'@',pca.numArrive," +
            "'@',DATE_FORMAT(pca.dateArrive,'%d-%m-%Y %H:%i:%s'))"+
            "FROM _procedure as p INNER JOIN procedure_concerner_affaire AS pca ON p.idProcedure = pca.procedureId " +
            "WHERE pca.affaireId= AFF.idAffaire ORDER BY p.idProcedure desc LIMIT 1) AS LAST_PROCEDURE";

    private final String ALL_AFFAIRE_COLUMN =
                    "AFF.idAffaire," +
                    "AFF.numAffaire," +
                    "AFF.typeAffaire," +
                    "AFF.dateFormulation," +
                    "AFF.situation ";

    private final String TITRE_DEPENDANT = " (SELECT CONCAT(ti.idTitre,'@',ti.numTitre,'@',ti.nomPropriete)" +
                    " FROM  titre AS ti,terrain as te,terrain_depend_titre as tdt" +
                    " WHERE tdt.titreId = ti.idTitre " +
                    " AND tdt.terrainId = te.idTerrain " +
                    " AND AFF.terrainId = te.idTerrain) AS TITRE_DEPENDANT ";

    private final String SUPERFICIE = " ( SELECT CONCAT(te.idTerrain,'@',te.superficie)"+
                                      " FROM terrain AS te"+
                                      " WHERE  te.idTerrain = AFF.terrainId ) AS SUPERFICIE_TERRAIN ";

    private final String FULL_INFORMATION =
                            ALL_AFFAIRE_COLUMN+"," +
                            APPLICANT_FULLNAME+"," +
                            LAST_REDACTOR +"," +
                            LAST_PROCEDURE +","+
                            TITRE_DEPENDANT+","+
                            SUPERFICIE;
    public ObservableList<AffaireForView> getAllAffair(){
        ObservableList<AffaireForView> list = FXCollections.observableArrayList();
        String query="SELECT " +
                FULL_INFORMATION +
                "FROM affaire AS AFF ";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(createAffaireView(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private AffaireForView createAffaireView(ResultSet rs) throws SQLException {
        String applicantName = rs.getString("APPLICANT_FULLNAME");
        String redactor = rs.getString("LAST_REDACTOR");
        String lastProcedure = rs.getString("LAST_PROCEDURE");
        String titreDependant = rs.getString("TITRE_DEPENDANT");
        String superficieTerrain = rs.getString("SUPERFICIE_TERRAIN");
        return new AffaireForView(
                rs.getInt("idAffaire"),
                rs.getString("numAffaire"),
                rs.getTimestamp("dateFormulation"),
                string2TypeDemande(rs.getString("typeAffaire")),
                createRedactor(redactor),
                string2AffaireStatus(rs.getString("situation")),
                createDemandeur(applicantName),
                initTerrain(titreDependant,superficieTerrain),
                initSituation(lastProcedure)
        );
    }

    private User createRedactor(String redactor) {
        if (redactor != null && !redactor.isEmpty()) {
            String[] tab = redactor.split("@",3);
            if (tab.length ==3){
               User user = new User();
                user.setNom(tab[0]);
                user.setPrenom(tab[1]);
                user.setId(Integer.valueOf(tab[2]));
                return user;
            }
        }
        return null;
    }

    private PersonneMorale createDemandeur(String applicantName) {
       PersonneMorale demandeurPhysique = new PersonneMorale();
        if (applicantName != null && !applicantName.isEmpty()){
            String[] tab = applicantName.split("@", 2);
            demandeurPhysique.setNom(tab[0]);
        }
        return demandeurPhysique;
    }

    private Titre initTitre(String nomTitreDependant){
        if (nomTitreDependant!=null && !nomTitreDependant.isBlank()){
            String nomPropriete = "";
            String numeroPropriete = "";
            int idTitre = 0;
            String[] split = nomTitreDependant.split("@",3);
            if (split.length==3){
                // TITRE DEPENDANT
                idTitre = Integer.valueOf(split[0]);
                numeroPropriete = split[1];
                nomPropriete = split[2];

                Titre titre = new Titre();
                titre.setNomPropriete(nomPropriete);
                titre.setNumero(numeroPropriete);
                titre.setId(idTitre);
                return titre;
            }

        }
        return null;
    }

    private Terrain initTerrain(String nomTitreDependant, String superficieTerrain){
        Terrain terrain = new Terrain();
        terrain.setTitreDependant(initTitre(nomTitreDependant));
        if (superficieTerrain!=null){
             String[] split = superficieTerrain.split("@", 2);
             int idTerrain = Integer.valueOf(split[0]);
             String superficie = split[1];
             terrain.setIdTerrain(idTerrain);
             terrain.setSuperficie(superficie);
        }
        return terrain;
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
                    if (!numeroArrive.isEmpty()) situation = nomProcedure + " N° " + numeroArrive + " du " + dateArrive;
                    else situation = nomProcedure + " du " + dateArrive;
                } else if (!dateDepart.equals("30-07-1999 00:00:00")) {
                    procedureForTableview.setStatus(ProcedureStatus.GO);
                    if (!numeroDepart.isEmpty()) situation = nomProcedure + " N° " + numeroDepart + " du " + dateDepart;
                    else situation = nomProcedure + " du " + dateDepart;
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
    public AffairDao(Connection connection) {
        super(connection);
    }
    @Override public int delete(Affaire affaire) {
        int status = 0;
        String query = " DELETE FROM affaire WHERE numAffaire = ? ;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, affaire.getNumero());
            status = ps.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(AffairDao.class.getName()).log(Level.SEVERE, " Problem with the 'delete' method ", e);
        }
        return status;
    }

    public int deleteById(Affaire affaire) {
        int status = 0;
        String query = "DELETE FROM affaire WHERE idAffaire = ? ;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, affaire.getId());
            status = ps.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(AffairDao.class.getName()).log(Level.SEVERE, " Problem with the 'delete' method ", e);
        }
        return status;
    }

    @Override public int update(Affaire affaire) {
        return 0;
    }
    @Override public Affaire findById(int id) {
        return null;
    }
    public int getIdByNum(String num) {
        int id = 0;
        String query = " SELECT aff.idAffaire FROM affaire as aff WHERE numAffaire = ? ; ";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, num);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next())
                id = resultSet.getInt("idAffaire");
        } catch (SQLException e) {
            Logger.getLogger(AffairDao.class.getName()).log(Level.SEVERE, " Problem with the 'delete' method ", e);
        }
        return id;
    }

    public User getActualEditor(Affaire affaire){
        String query = "SELECT utilisateur.prenom,utilisateur.mot_de_passe,utilisateur.photo" +
                "               FROM utilisateur inner join dispatch d on utilisateur.idUtilisateur = d.utilisateurId" +
                "               WHERE d.affaireId = ? " +
                "               ORDER BY dateDispatch desc limit 1;";
        User user = new User();
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, affaire.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user.setPrenom(rs.getString("prenom"));
                user.setPassword(rs.getString("mot_de_passe"));
                user.setPhoto(getImageFromBuffer(rs));
            }
        } catch (SQLException e) {
            Logger.getLogger(AffairDao.class.getName()).log(Level.SEVERE, " Problem with the 'getActualEditor' method ", e);
        }
        return user;
    }

    @Override public Affaire finByNum(int num) {
        return null;
    }
    public ObservableList<String> groupeAffaireByDate() {
        ObservableList<String> list = FXCollections.observableArrayList();
        String query = "SELECT distinct year(dateFormulation) as anne FROM affaire order by dateFormulation desc ;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(rs.getString("anne"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<String> groupeAffaireByDate(int min , int max) {
        ObservableList<String> list = FXCollections.observableArrayList();
        String query = "SELECT distinct year(dateFormulation) as anne FROM affaire where year(dateFormulation) not between  ? and ? group by dateFormulation order by dateFormulation desc ;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1,min);
            ps.setInt(2,max);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(rs.getString("anne"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override public int create(Affaire affaire){
        String query = "";
        int status = 0;
        query = "INSERT INTO affaire (" +
                "numAffaire,"+
                "typeAffaire,"+
                "dateFormulation ," +
                "situation,"+
                "demandeurId," +
                "terrainId)"+
                "VALUES (?,?,?,?,?,?);";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, affaire.getNumero());
            ps.setString(2, typeDemande2String(affaire.getTypeDemande()));
            ps.setTimestamp(3, affaire.getDateDeFormulation());
            ps.setString(4,affaireStatus2String(affaire.getStatus()));
            ps.setInt(5, affaire.getDemandeur().getId());
            ps.setInt(6,affaire.getTerrain().getIdTerrain());
            status = ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    @Deprecated public ObservableList<String> getAllNumAffaire() {
        ObservableList<String> list = FXCollections.observableArrayList();
        String query = " SELECT distinct affaire.numAffaire as num from affaire;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(rs.getString("num"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<AffaireForView> getAffaireBy(String dateCreation) {
        ObservableList<AffaireForView> list = FXCollections.observableArrayList();
        String query = "SELECT " +
                FULL_INFORMATION +
                " FROM affaire AS AFF" +
                " WHERE year(AFF.dateFormulation)= ?";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, dateCreation);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                list.add(createAffaireView(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Affaire finByNum(String num) {
        AffaireForView affairForView = null;
        try {
            String query = "SELECT " +
                    FULL_INFORMATION +
                    " FROM affaire AS AFF" +
                    " WHERE AFF.numAffaire = ?";
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setString(1, num);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                affairForView = createAffaireView(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affairForView;
    }

    public ObservableList<ConnexAffairForView> checkConnexAffaireBy(String num){
        num = num.toLowerCase();
        ObservableList<ConnexAffairForView> list = FXCollections.observableArrayList();
        /**
         *   SELECTIONNER LES AFFAIRES QUI NE SONT PAS  DE TYPE ' PRESCRIPTION OU  AFFECTATION ' ET QUI NE SONT PAS ENCORE TITRE
         */
        String query = " SELECT "+
                " a.terrainId"+
                " ,a.numAffaire"+
                " ,CONCAT(d.nomDmd,' ',d.prenomDmd) AS NOM_DEMANDEUR  "+
                " FROM affaire a " +
                " inner join demandeur d on a.demandeurId = d.idDmd " +
                " WHERE ( LOWER(a.numAffaire) = ? or LOWER(TRIM(d.nomDmd))LIKE CONCAT(?,'%') or LOWER(TRIM(d.prenomDmd)) LIKE CONCAT(?,'%') ) " +
                       "AND STRCMP(a.typeAffaire,'PRESCRIPTION_ACQUISITIVE')<> 0 " +
                " AND a.terrainId NOT IN (SELECT tat.terrainId FROM terrain_aboutir_titre tat);";
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setString(1, num);
            ps.setString(2,num);
            ps.setString(3,num);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nomDemandeur = rs.getString("NOM_DEMANDEUR");
                String numero = rs.getString("numAffaire");
                int idTerrain = rs.getInt("terrainId");
                list.add(new ConnexAffairForView(numero, nomDemandeur,idTerrain)); }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getEditorPassWordBy(int idAffair) {
        String query = "SELECT utilisateur.mot_de_passe AS passWord " +
                "FROM utilisateur inner JOIN dispatch ON utilisateur.idUtilisateur = dispatch.utilisateurId " +
                "WHERE dispatch.affaireId = ? ORDER BY dispatch.dateDispatch desc limit 1 ;";
        String passWord = "";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, idAffair);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                passWord = rs.getString("passWord");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passWord;
    }

    public int getNbAffaireWhereActualEditorIs(int userId) {
        int nb = 0;
        String query = "SELECT " +
                "COUNT(AFF.idAffaire) as nb " +
                " FROM affaire AS AFF " +
                " WHERE " +
                " (SELECT utilisateur.idUtilisateur " +
                " FROM utilisateur " +
                " LEFT JOIN dispatch d on utilisateur.idUtilisateur = d.utilisateurId" +
                " WHERE d.affaireId = AFF.idAffaire " +
                " ORDER BY d.dateDispatch desc limit 1 ) = ? ;";

        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                nb = rs.getInt("nb");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nb;
    }

    public Boolean checkNumAffair(String num) {
        String query = " SELECT COUNT(AFF.idAffaire) AS nb FROM affaire AS AFF where AFF.numAffaire = ?";
        int value = 0;
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, num);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                value = rs.getInt("nb");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return value == 0;
    }

    public Boolean checkNumOrdonance(String num) {
        String query = " SELECT COUNT(AFF.idAffaire) AS nb FROM affaire AS AFF where AFF.numAffaire = ?";
        int value = 0;
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, num);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                value = rs.getInt("nb");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return value == 0;
    }

    public AffaireForView getAffairByDemandeurNameOrNum(String type) {
        AffaireForView affairForView = null;
        type = type.toLowerCase();
        String query = " SELECT " +
                  FULL_INFORMATION +
                " FROM demandeur as d , affaire as AFF " +
                " WHERE AFF.demandeurId = d.idDmd " +
                " AND ( AFF.numAffaire = ? or lower(d.prenomDmd) LIKE CONCAT(?,'%') or lower(d.nomDmd) LIKE CONCAT(?,'%') )";
        try (PreparedStatement ps = this.connection.prepareStatement(query)){
            ps.setString(1,type);
            ps.setString(2,type);
            ps.setString(3,type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                affairForView = createAffaireView(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affairForView;
    }

    public ObservableList<AffaireForView> getAllAffairWhereStatusIs(AffaireStatus status) {
        ObservableList<AffaireForView> list = FXCollections.observableArrayList();
        String query = "SELECT " +
                FULL_INFORMATION +
                " FROM affaire AS AFF" +
                " WHERE AFF.situation = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1,Affaire.affaireStatus2String(status));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(createAffaireView(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<AffaireForView> getAffairWhereActualEditorIs(User editor) {
        ObservableList<AffaireForView> list = FXCollections.observableArrayList();
        // listes des affaires avec laquelle l'utilisateur connecté est l'actuelle redacteur !
        // c'est different de la listes des affaires que l'utilisateur a été redacteur
        String query = "SELECT " +
                FULL_INFORMATION +
                " FROM affaire AS AFF " +
                " WHERE " +
                "(SELECT us.idUtilisateur" +
                " FROM utilisateur as us " +
                " inner JOIN dispatch as d ON us.idUtilisateur = d.utilisateurId"+
                " WHERE d.affaireId = AFF.idAffaire " +
                " ORDER BY d.dateDispatch desc limit 1) = ? ORDER BY AFF.idAffaire ;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, editor.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(createAffaireView(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getAffaireIdByNum(String num) {
        int id = 0;
        String query = " SELECT affaire.idAffaire FROM affaire where numAffaire = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setString(1, num);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next())
                id = resultSet.getInt("idAffaire");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public ObservableList<ArrayList<String>> getAllProcedureAndDateConcernedByThis(int idAffaire) {
        ObservableList<ArrayList<String>> a = FXCollections.observableArrayList();
        String query = "SELECT a.procedureId,"+
                " a.dateDepart,"+
                " DATE_FORMAT(a.dateDepart,"+"'%d-%m-%Y %H:%i:%s') dateDepart," +
                " a.numArrive, +"+
                " DATE_FORMAT(a.dateArrive," + "'%d-%m-%Y %H:%i:%s') dateArrive," +
                 "a.numDepart"+
                " FROM _procedure inner"+
                " join procedure_concerner_affaire a on `_procedure`.idProcedure = a.procedureId" +
                " WHERE a.affaireId= ? ORDER BY _procedure.idProcedure  DESC ;";
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setInt(1, idAffaire);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ArrayList<String> tab = new ArrayList<>();
                String dateDepart = rs.getString("dateDepart");
                String dateArrive = rs.getString("dateArrive");
                tab.add(String.valueOf(rs.getInt("procedureId")));
                tab.add(rs.getString("numDepart"));
                tab.add(dateDepart.equals("30-07-1999 00:00:00") ? "" : dateDepart);
                tab.add(rs.getString("numArrive"));
                tab.add(dateArrive.equals("30-07-1999 00:00:00") ? "" : dateArrive);
                a.add(tab);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }

    public ObservableList<ConnexAffairForView> getAllAffaireConnexeWith(Affaire affaire) {
        ObservableList<ConnexAffairForView> a = FXCollections.observableArrayList();
        String query =" SELECT AFF.idAffaire AS id,AFF.numAffaire AS num , " +
                " CONCAT(d.nomDmd,'   ',d.prenomDmd) as demandeur,AFF.situation as situation ,"+LAST_PROCEDURE+
                " FROM affaire AFF inner join demandeur as d on AFF.demandeurId = d.idDmd " +
                "WHERE AFF.terrainId = ?  and AFF.idAffaire <> ? ; ";
        try (PreparedStatement ps = this.connection.prepareStatement(query)){
            ps.setInt(1,affaire.getTerrain().getIdTerrain());
            ps.setInt(2,affaire.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                a.add(new ConnexAffairForView(
                        rs.getInt("id"),
                        rs.getString("num"),
                        rs.getString("demandeur"),
                        initSituation(rs.getString("LAST_PROCEDURE")),
                        Affaire.string2AffaireStatus(rs.getString("situation"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }

    // CHART
    public ObservableList<XYChart.Series<String, Number>> getNbAffaireByDateForChart() {
        ObservableList<XYChart.Series<String, Number>> list = FXCollections.observableArrayList();
        String query ="SELECT distinct year(dateFormulation) AS date_creation," +
                " ( SELECT count(typeAffaire) from affaire where  typeAffaire = 'ACQUISITION' AND YEAR(dateFormulation) = date_creation ) AS acquisition ," +
                " ( SELECT COUNT(typeAffaire) from affaire where typeAffaire = 'PRESCRIPTION_ACQUISITIVE' AND YEAR(dateFormulation) = date_creation ) AS prescription FROM affaire GROUP BY date_creation ORDER BY date_creation desc ;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            XYChart.Series<String, Number> acquisition = new XYChart.Series<String, Number>();
            acquisition.setName(" Demande d'acquisition");
            XYChart.Series<String, Number> prescrition = new XYChart.Series<String, Number>();
            prescrition.setName("Prescription Acquisitive");
            while (rs.next()) {
                XYChart.Data<String, Number> acquisitionData = new XYChart.Data<String, Number>(rs.getString("date_creation"), rs.getInt("acquisition"));
                XYChart.Data<String, Number> prescriptionData = new XYChart.Data<>(rs.getString("date_creation"), rs.getInt("prescription"));
                acquisition.getData().add(acquisitionData);
                prescrition.getData().add(prescriptionData);
            }
            list.addAll(acquisition, prescrition);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public ArrayList<Integer> getDataForDashboard(String year) {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        String query = "SELECT (SELECT COUNT(*) FROM affaire as aff1 WHERE year(AFF.dateFormulation) = year(aff1.dateFormulation) ) as fullTotal , " +
                "(SELECT COUNT(aff2.idAffaire) FROM affaire as aff2 WHERE aff2.typeAffaire ='ACQUISITION' AND year(aff2.dateFormulation) = year(AFF.dateFormulation)) as aquisitionTotal ," +
                "(SELECT COUNT(idTitre) from titre  where year(titre.dateCreation) = year(AFF.dateFormulation)  AND " +
                "                          (SELECT a.numAffaire from terrain_aboutir_titre as tat " +
                "                           inner join terrain t on tat.terrainId = t.idTerrain " +
                "                           inner join affaire a on t.idTerrain = a.terrainId where (tat.titreId = titre.idTitre AND strcmp(a.situation,'REJETER') <> 0 )) is not null ) as totalTitle " +
                "FROM affaire as AFF where year(AFF.dateFormulation)= ? limit 1;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                integerArrayList.add(rs.getInt("fullTotal"));
                integerArrayList.add(rs.getInt("aquisitionTotal"));
                integerArrayList.add(rs.getInt("totalTitle"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return integerArrayList;
    }

    public int nombreDeTerrainNonImmatricule() {
        int nb = 0;
        String query = " SELECT COUNT(idAffaire)  AS NB from affaire inner join terrain t on affaire.terrainId = t.idTerrain" +
                " where idTerrain not in ( SELECT tdt.terrainId FROM terrain_depend_titre AS tdt );";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next())
                nb = resultSet.getInt("NB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nb;
    }
}
