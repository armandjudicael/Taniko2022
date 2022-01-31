package model.pojo.business.other;
import dao.DaoFactory;
import model.Enum.FolderStatus;
import model.Enum.Observation;
import model.Enum.TypeDemande;
import view.Model.ViewObject.ConnexAffairForView;
import view.Model.ViewObject.EditorForView;
import view.Model.ViewObject.AttachementForView;
import javafx.collections.ObservableList;
import java.sql.Date;
import java.util.ArrayList;

public class Dossier {

    @Override public String toString(){
        return "Folder{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", dateDeFormulation=" + dateDeFormulation +
                ", typeDemande=" + typeDemande +
                ", redacteur=" + redacteur +
                ", status=" + status +
                ", demandeur=" + demandeur +
                ", propriete=" + propriete +
                '}';
    }

    public Dossier() {}

    public Dossier(int id,
                   String numero,
                   Date dateDeFormulation,
                   TypeDemande typeDemande,
                   User redacteur,
                   FolderStatus status,
                   PersonneMorale demandeur,
                   Propriete propriete){
        this.id = id;
        this.numero = numero;
        this.dateDeFormulation = dateDeFormulation;
        this.typeDemande = typeDemande;
        this.redacteur = redacteur;
        this.status = status;
        this.demandeur = demandeur;
        this.propriete = propriete;
    }

    public Dossier(String numero,
                   Date dateDeFormulation,
                   TypeDemande typeDemande,
                   User redacteur,
                   FolderStatus status,
                   PersonneMorale demandeur,
                   Propriete propriete){
        this.numero = numero;
        this.dateDeFormulation = dateDeFormulation;
        this.typeDemande = typeDemande;
        this.redacteur = redacteur;
        this.status = status;
        this.demandeur = demandeur;
        this.propriete = propriete;
    }

    public static String observation2String(Observation observation) {
        switch (observation) {
            case CONNEXE:
                return "connexe";
            case LITIGE:
                return "litige";
            case SANS_EMPIETEMENT:
                return "sans empietement";
            case EMPIETE:
                return "empieté";
            case AUTRE:
                return "autre";
        }
        return "";
    }

    public static String affaireStatus2String(FolderStatus status){
        switch (status){
            case RUNNING: return "EN_COURS";
            case SUCCEED: return "TERMINER";
            case SUSPEND: return "SUSPENDU";
            case REJECTED: return "REJETER";
        }
        return "";
    }

    public static FolderStatus string2AffaireStatus(String name){
       switch (name){
           case "EN_COURS": return FolderStatus.RUNNING;
           case "TERMINER": return FolderStatus.SUCCEED;
           case "SUSPENDU": return FolderStatus.SUSPEND;
           case "REJETER" : return FolderStatus.REJECTED;
       }
       return FolderStatus.RUNNING;
    }

    public TypeDemande getTypeDemande() {
        return typeDemande;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDateDeFormulation() {
        return dateDeFormulation;
    }

    public User getRedacteur() {
        return redacteur;
    }

    public void setRedacteur(User redacteur) {
        this.redacteur = redacteur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Observation string2Observation(String obs){
        switch (obs){
            case "connexe":
                return Observation.CONNEXE;
            case "litige":
                return Observation.LITIGE;
            case "empieté":
                return Observation.EMPIETE;
            case "sans empietement":
                return Observation.SANS_EMPIETEMENT;
            case "autre" :
                return Observation.AUTRE;
        }
        return Observation.SANS_EMPIETEMENT;
    }

    public FolderStatus getStatus() {
        return status;
    }

    public Propriete getTerrain() {
        return propriete;
    }

    public void setStatus(FolderStatus status) {
        this.status = status;
    }

    public void setTerrain(Propriete propriete) {
        this.propriete = propriete;
    }

    public User getActualEditor(){
       return DaoFactory.getDossierDao().getActualEditor(this);
    }

    public ObservableList<ConnexAffairForView> getAllAffaireConnexe(){ return  DaoFactory.getDossierDao().getAllAffaireConnexeWith(this); }
    public ArrayList<EditorForView> getAllEditor() {
        return DaoFactory.getUserDao().getAllEditorAndDateByThis(this);
    }
    public ObservableList<ArrayList<String>> getAllProcedureChecked() { return DaoFactory.getDossierDao().getAllProcedureAndDateConcernedByThis(this.getId()); }
    public ObservableList<AttachementForView> getAllPieceJointe(){return DaoFactory.getPieceJointeDao().getAllPieceJointe(this) ;}
    public void setDateDeFormulation(Date dateDeFormulation) {
        this.dateDeFormulation = dateDeFormulation;
    }
    public void setTypeDemande(TypeDemande typeDemande) {
        this.typeDemande = typeDemande;
    }
    public Personne getDemandeur() {
        return demandeur;
    }
    public void setDemandeur(Personne demandeur) { this.demandeur = demandeur; }
/**
 * @param id identifiant de l'affaire
 * @param numero numero de l'affaire
 * @param dateFormulation date de formulation de l'affaire
 * @param typeDemande le type de la demande  { DEMANDE D'ACQUISITION ,PRESCRIPTION ACQUISITIVE , DEMANDE D'AFFECTATION }
 * @param redacteur Agent responsable de l'affaire ou agent traitant
 * @param status c'est le situation de l'affaire { EN_COURS , REJETER , SUSPENDU , TERMINER }
 * @param demandeur c'est le demandeur du propriete
 * @param propriete propriete demandé
 * */
    private  int id;
    private String numero;
    private Date dateDeFormulation;
    private TypeDemande typeDemande;
    private User redacteur;
    private FolderStatus status;
    private Personne demandeur;
    private Propriete propriete;
}