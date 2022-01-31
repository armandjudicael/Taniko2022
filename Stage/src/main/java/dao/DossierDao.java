package dao;

import model.Enum.FolderStatus;
import model.Enum.ProcedureStatus;
import model.Enum.TypeDemande;
import model.Enum.TypeDemandeur;
import model.pojo.business.other.*;
import view.Model.ViewObject.FolderForView;
import view.Model.ViewObject.ProcedureForTableview;
import view.Model.ViewObject.ConnexAffairForView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static dao.UserDao.getImageFromBuffer;
import static model.Enum.TypeDemande.string2TypeDemande;
import static model.Enum.TypeDemande.typeDemande2String;
import static model.pojo.business.other.Dossier.*;

public class DossierDao extends Dao implements DaoHelper<Dossier>{

    private final String APPLICANT_FULLNAME = "( SELECT CONCAT(pm.nomDmd,'@',pm.typeDmd)  FROM personne_morale pm WHERE pm.idDmd = AFF.demandeurId ) AS APPLICANT_FULLNAME ";

    private final String LAST_REDACTOR = "(SELECT CONCAT(pm.nomDmd,'@',u.idUtilisateur)"+
            " FROM utilisateur AS u " +
            " inner join  dispatch d on u.idUtilisateur = d.utilisateurId"+
            " inner join personne_morale pm on u.idUtilisateur = pm.idDmd WHERE d.dossierId = AFF.idDossier " +
            " ORDER BY d.dateDispatch desc limit 1) AS LAST_REDACTOR ";

    private final String LAST_PROCEDURE = "(SELECT CONCAT(p.nomProcedure,"+
            "'@',pca.numDepart," +
            "'@',DATE_FORMAT(pca.dateDepart,'%d-%m-%Y %H:%i:%s'),"+
            "'@',pca.numArrive," +
            "'@',DATE_FORMAT(pca.dateArrive,'%d-%m-%Y %H:%i:%s'))"+
            "FROM _procedure as p INNER JOIN procedure_concerner_dossier AS pca ON p.idProcedure = pca.procedureId " +
            "WHERE pca.dossierId= AFF.idDossier ORDER BY p.idProcedure desc LIMIT 1) AS LAST_PROCEDURE";

    private final String ALL_FOLDER_COLUMN =
                    "AFF.idAffaire," +
                    "AFF.numAffaire," +
                    "AFF.typeAffaire," +
                    "AFF.dateFormulation," +
                    "AFF.situation ";

    private final String PROPRIETE_MERE = " (SELECT CONCAT(po.idPropriete,'@',po.numTitre,'@',po.nomPropriete)" +
                    " FROM propriete  po inner join propriete_depend_propriete pdp on po.idPropriete = pdp.idProprieteMere " +
                    " WHERE po.idPropriete = AFF.proprieteId ) AS PROPRIETE_MERE ";

    private final String SUPERFICIE = " ( SELECT CONCAT(po.idPropriete,'@',po.superficie)"+
                                      " FROM propriete AS po"+
                                      " WHERE  po.idPropriete = AFF.proprieteId ) AS SUPERFICIE_TERRAIN ";

    private final String FULL_INFORMATION =
                            ALL_FOLDER_COLUMN +"," +
                            APPLICANT_FULLNAME+"," +
                            LAST_REDACTOR +"," +
                            LAST_PROCEDURE +","+
                                    PROPRIETE_MERE +","+
                            SUPERFICIE;

    private final String FULL_INFORMATION_WITHOUT_APPLICANT =
            ALL_FOLDER_COLUMN +","+
            LAST_REDACTOR +"," +
            LAST_PROCEDURE +","+
                    PROPRIETE_MERE +","+
            SUPERFICIE;

    public ObservableList<FolderForView> getAllFolder(){
        ObservableList<FolderForView> list = FXCollections.observableArrayList();
        String query="SELECT "+
                FULL_INFORMATION +
                "FROM dossier AS AFF ";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(createFolderForView(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private FolderForView createFolderForView(ResultSet rs) throws SQLException {

        String applicantName = rs.getString("APPLICANT_FULLNAME");
        String responsable = rs.getString("LAST_REDACTOR");
        String lastProcedure = rs.getString("LAST_PROCEDURE");
        String titreDependant = rs.getString("PROPRIETE_MERE");
        String superficieTerrain = rs.getString("SUPERFICIE_TERRAIN");
        FolderForView affaireForView = new FolderForView();
        int idDossier = rs.getInt("idDossier");
        String numAffaire = rs.getString("numAffaire");
        Date dateFormulation = rs.getDate("dateFormulation");
        TypeDemande typeAffaire = string2TypeDemande(rs.getString("typeAffaire"));
        User redacteur = createRedactor(responsable);
        FolderStatus situation = string2AffaireStatus(rs.getString("situation"));
        Personne demandeur = createDemandeur(applicantName);
        Propriete propriete = initTerrain(titreDependant, superficieTerrain);
        ProcedureForTableview procedureForTableview = initSituation(lastProcedure);

        affaireForView.setId(idDossier);
        affaireForView.setNumero(numAffaire);
        affaireForView.setDateDeFormulation(dateFormulation);
        affaireForView.setTypeDemande(typeAffaire);
        affaireForView.setRedacteur(redacteur);
        affaireForView.setStatus(situation);
        affaireForView.setDemandeur(demandeur);
        affaireForView.setTerrain(propriete);
        affaireForView.setProcedure(procedureForTableview);
        return affaireForView;
    }

    private User createRedactor(String redactor) {
        if (redactor != null && !redactor.isEmpty()) {
            String[] tab = redactor.split("@",2);
            if (tab.length ==2){
               User user = new User();
                user.setNom(tab[0]);
                user.setId(Integer.valueOf(tab[1]));
                return user;
            }
        }
        return null;
    }

    private Personne createDemandeur(String applicantName){
       Personne dm = new PersonneMorale();
        if (applicantName != null && !applicantName.isEmpty()){
            String[] tab = applicantName.split("@", 2);
            dm.setNom(tab[0]);
            dm.setType(TypeDemandeur.string2TypeDemandeur(tab[1]));
        }
        return dm;
    }

    private Propriete initTitre(String nomTitreDependant){
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
                return null;
            }

        }
        return null;
    }

    private Propriete initTerrain(String nomTitreDependant, String superficieTerrain){
        Propriete propriete = new Propriete();
        propriete.setTitreDependant(initTitre(nomTitreDependant));
        if (superficieTerrain!=null){
             String[] split = superficieTerrain.split("@", 2);
             int idTerrain = Integer.valueOf(split[0]);
             String superficie = split[1];
             propriete.setIdPropriete(idTerrain);
             propriete.setSuperficie(superficie);
        }
        return propriete;
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

    public DossierDao(Connection connection) {
        super(connection);
    }

    @Override public int delete(Dossier dossier) {
        int status = 0;
        String query = " DELETE FROM dossier WHERE numDossier = ? ;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, dossier.getNumero());
            status = ps.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DossierDao.class.getName()).log(Level.SEVERE, " Problem with the 'delete' method ", e);
        }
        return status;
    }

    public int deleteById(Dossier dossier) {
        int status = 0;
        String query = "DELETE FROM dossier WHERE idDossier = ? ;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, dossier.getId());
            status = ps.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(DossierDao.class.getName()).log(Level.SEVERE, " Problem with the 'delete' method ", e);
        }
        return status;
    }

    @Override public int update(Dossier dossier) {
        return 0;
    }
    @Override public Dossier findById(int id) {
        return null;
    }

    public int getIdByNum(String num) {
        int id = 0;
        String query = " SELECT d.idDossier FROM dossier as d WHERE numDossier = ? ; ";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, num);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next())
                id = resultSet.getInt("idDossier");
        } catch (SQLException e) {
            Logger.getLogger(DossierDao.class.getName()).log(Level.SEVERE, " Problem with the 'delete' method ", e);
        }
        return id;
    }

    public User getActualEditor(Dossier dossier){
        String query = "SELECT pm.nomDmd,u.mot_de_passe,u.photo"+
                "       FROM utilisateur as u " +
                "           inner join dispatch d on u.idUtilisateur = d.utilisateurId" +
                "           inner join personne_morale pm on u.idUtilisateur = pm.idDmd " +
                "       WHERE d.dossierId = ? ORDER BY dateDispatch desc limit 1;";
        User user = new User();
        try (PreparedStatement ps = this.connection.prepareStatement(query)){
            ps.setInt(1, dossier.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user.setNom(rs.getString("nomDmd"));
                user.setPassword(rs.getString("mot_de_passe"));
                user.setPhoto(getImageFromBuffer(rs));
            }
        } catch (SQLException e) {
            Logger.getLogger(DossierDao.class.getName()).log(Level.SEVERE, " Problem with the 'getActualEditor' method ", e);
        }
        return user;
    }

    @Override public Dossier finByNum(int num) {
        return null;
    }

    public ObservableList<String> groupFolderByDate() {
        ObservableList<String> list = FXCollections.observableArrayList();
        String query = "SELECT distinct year(dateFormulation) as anne FROM dossier order by dateFormulation desc ;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(rs.getString("anne"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<String> groupFolderByDate(int min , int max) {
        ObservableList<String> list = FXCollections.observableArrayList();
        String query = "SELECT distinct year(dateFormulation) as anne FROM dossier where year(dateFormulation) not between  ? and ? group by dateFormulation order by dateFormulation desc ;";
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

    @Override public int create(Dossier dossier){
        String query = "";
        int status = 0;
        query = "INSERT INTO dossier ("+
                "numDossier,"+
                "nature,"+
                "dateFormulation ," +
                "situation,"+
                "titulaireId,"+
                "proprieteId)"+
                "VALUES (?,?,?,?,?,?);";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, dossier.getNumero());
            ps.setString(2, typeDemande2String(dossier.getTypeDemande()));
            ps.setDate(3, dossier.getDateDeFormulation());
            ps.setString(4,affaireStatus2String(dossier.getStatus()));
            ps.setInt(5, dossier.getDemandeur().getId());
            ps.setInt(6, dossier.getTerrain().getIdPropriete());
            status = ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    public ObservableList<FolderForView> getAffaireBy(String dateCreation) {
        ObservableList<FolderForView> list = FXCollections.observableArrayList();
        String query = "SELECT " +
                FULL_INFORMATION +
                " FROM dossier " +
                " WHERE year(dossier.dateFormulation)= ?";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, dateCreation);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                list.add(createFolderForView(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Dossier finByNum(String num) {
        FolderForView affairForView = null;
        try {
            String query = "SELECT " +
                    FULL_INFORMATION +
                    " FROM dossier " +
                    " WHERE numDossier = ?";
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setString(1, num);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                affairForView = createFolderForView(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affairForView;
    }

    public ObservableList<ConnexAffairForView> checkConnexAffaireBy(String num){
        num = num.toLowerCase();
        ObservableList<ConnexAffairForView> list = FXCollections.observableArrayList();
        /**
         *   SELECTIONNER LES AFFAIRES QUI NE SONT PAS  DE TYPE ' PRESCRIPTION OU  AFFECTATION ' ET QUI NE SONT PAS ENCORE TITRER
         */
        String query = " SELECT "+
                " a.proprieteId"+
                " ,a.numDossier"+
                " ,d.nomDmd  "+
                " FROM dossier a " +
                " inner join personne_morale d on a.titulaireId = d.idDmd " +
                " WHERE ( LOWER(a.numDossier) = ? or LOWER(TRIM(d.nomDmd))LIKE CONCAT(?,'%')) " +
                       "AND STRCMP(a.nature,'PRESCRIPTION_ACQUISITIVE')<> 0 " +
                " );";
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setString(1, num);
            ps.setString(2,num);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nomDemandeur = rs.getString("nomDmd");
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
                "WHERE dispatch.dossierId = ? ORDER BY dispatch.dateDispatch desc limit 1 ;";
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

    public int getNbAffaireWhereActualEditorIs(User user) {
        int nb = 0;
        String query = "SELECT " +
                "COUNT(AFF.idDossier) as nb " +
                " FROM dossier AS AFF " +
                " WHERE " +
                " (SELECT utilisateur.idUtilisateur " +
                " FROM utilisateur " +
                " LEFT JOIN dispatch d on utilisateur.idUtilisateur = d.utilisateurId" +
                " WHERE d.dossierId = AFF.idDossier " +
                " ORDER BY d.dateDispatch desc limit 1 ) = ? ;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                nb = rs.getInt("nb");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nb;
    }


    public FolderForView getFolderByApplicantNameOrNum(String type){
        FolderForView affairForView = null;
        type = type.toLowerCase();
        String query = " SELECT " +
                  FULL_INFORMATION +
                " FROM personne_morale pm , dossier d " +
                " WHERE d.titulaireId = pm.idDmd" +
                " AND ( d.numDossier = ?or lower(pm.nomDmd) LIKE CONCAT(?,'%') )";
        try (PreparedStatement ps = this.connection.prepareStatement(query)){
            ps.setString(1,type);
            ps.setString(2,type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                affairForView = createFolderForView(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affairForView;
    }

    public ObservableList<FolderForView> getAllAffairWhereStatusIs(FolderStatus status){
        ObservableList<FolderForView> list = FXCollections.observableArrayList();
        String query = "SELECT " +
                FULL_INFORMATION +
                " FROM dossier AS AFF" +
                " WHERE AFF.situation = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, Dossier.affaireStatus2String(status));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(createFolderForView(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<FolderForView> getAffairWhereActualEditorIs(User editor) {
        ObservableList<FolderForView> list = FXCollections.observableArrayList();
        // listes des affaires avec laquelle l'utilisateur connecté est l'actuelle redacteur !
        // c'est different de la listes des affaires que l'utilisateur a été redacteur
        String query = "SELECT " +
                FULL_INFORMATION +
                " FROM dossier AS AFF " +
                " WHERE " +
                "(SELECT us.idUtilisateur" +
                " FROM utilisateur as us " +
                " inner JOIN dispatch as d ON us.idUtilisateur = d.utilisateurId"+
                " WHERE d.dossierId = AFF.idDossier " +
                " ORDER BY d.dateDispatch desc limit 1) = ? ORDER BY AFF.idDossier ;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, editor.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(createFolderForView(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getAffaireIdByNum(String num) {
        int id = 0;
        String query = " SELECT dossier.idDossier FROM dossier where numDossier = ?";
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
        String query = "SELECT pcd.procedureId,"+
                " pcd.dateDepart,"+
                " DATE_FORMAT(pcd.dateDepart,"+"'%d-%m-%Y %H:%i:%s') dateDepart," +
                " pcd.numArrive, +"+
                " DATE_FORMAT(pcd.dateArrive," + "'%d-%m-%Y %H:%i:%s') dateArrive," +
                 "pcd.numDepart"+
                " FROM _procedure inner"+
                " join procedure_concerner_dossier pcd on `_procedure`.idProcedure = pcd.procedureId" +
                " WHERE pcd.dossierId= ? ORDER BY _procedure.idProcedure  DESC ;";
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

    public ObservableList<ConnexAffairForView> getAllAffaireConnexeWith(Dossier dossier){
        ObservableList<ConnexAffairForView> a = FXCollections.observableArrayList();
        String query =" SELECT AFF.idAffaire AS id,AFF.numAffaire AS num ,"+
                " d.nomDmd as demandeur,AFF.situation as situation ,"+LAST_PROCEDURE+
                " FROM affaire AFF inner join demandeur_morale as d on AFF.demandeurId = d.idDmd " +
                " WHERE AFF.terrainId = ?  and AFF.idAffaire <> ? ; ";

        String connexQuery = "SELECT d.idDossier , d.numDossier,d.situation ,"+LAST_PROCEDURE+"FROM dossier d"+
                " inner join dossierconnexe dc on d.idDossier = dc.idDossierEnfant "+
                " inner join personne_morale pm on d.titulaireId = pm.idDmd "+
                " WHERE d.idDossier <> ?;";

        try (PreparedStatement ps = this.connection.prepareStatement(connexQuery)){
            ps.setInt(1, dossier.getTerrain().getIdPropriete());
            ps.setInt(2, dossier.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                a.add(new ConnexAffairForView(
                        rs.getInt("d.idDossier"),
                        rs.getString("d.numDossier"),
                        rs.getString("demandeur"),
                        initSituation(rs.getString("LAST_PROCEDURE")),
                        Dossier.string2AffaireStatus(rs.getString("d.situation"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }

    // CHART
    public ObservableList<XYChart.Series<String, Number>> getNbAffaireByDateForChart(){
        ObservableList<XYChart.Series<String, Number>> list = FXCollections.observableArrayList();
        String query ="SELECT distinct year(dateFormulation) AS date_creation," +
                " ( SELECT count(nature) from dossier where  nature = 'ACQUISITION' AND YEAR(dateFormulation) = date_creation ) AS acquisition ," +
                " ( SELECT COUNT(nature) from dossier where nature = 'PRESCRIPTION_ACQUISITIVE' AND YEAR(dateFormulation) = date_creation ) AS prescription FROM affaire GROUP BY date_creation ORDER BY date_creation desc ;";
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

    public ArrayList<Integer> getDataForDashboard(String year){
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        String query = "SELECT (SELECT COUNT(*) FROM dossier as aff1 WHERE year(AFF.dateFormulation) = year(aff1.dateFormulation) ) as fullTotal , " +
                "(SELECT COUNT(aff2.idDossier) FROM dossier as aff2 WHERE aff2.typeAffaire ='ACQUISITION' AND year(aff2.dateFormulation) = year(AFF.dateFormulation)) as aquisitionTotal," +
                "(SELECT COUNT(aff3.idDossier) FROM dossier as aff3 WHERE aff3.typeAffaire ='AFFECTATION' AND year(aff3.dateFormulation) = year(AFF.dateFormulation)) as affectationTotal," +
                "(SELECT COUNT(idPropriete) from propriete  where year(titre.dateCreation) = year(AFF.dateFormulation)  AND " +
                "                          (SELECT a.numAffaire from terrain_aboutir_titre as tat " +
                "                           inner join propriete t on tat.terrainId = t.idPropriete " +
                "                           inner join affaire a on t.idPropriete = a.terrainId where (tat.titreId = titre.idTitre AND strcmp(a.situation,'REJETER') <> 0 )) is not null ) as totalTitle " +
                "FROM dossier as AFF where year(AFF.dateFormulation)= ? limit 1;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                integerArrayList.add(rs.getInt("fullTotal"));
                integerArrayList.add(rs.getInt("aquisitionTotal"));
                integerArrayList.add(rs.getInt("totalTitle"));
                integerArrayList.add(rs.getInt("affectationTotal"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return integerArrayList;
    }
    public int testProcedure(){
        try {
            CallableStatement cst = connection.prepareCall("{ ? = call new_function()}");
            cst.registerOutParameter(1,Types.INTEGER);
            // execute the statement
            if (cst.execute()) System.out.println(" Lancement de la requete de fonction stocké ....... ");
            return cst.getInt(1);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return 0;
    }
}
