package Model.Pojo;

import dao.DaoFactory;
import Model.Enum.AffaireStatus;
import Model.Enum.Observation;
import Model.Enum.TypeDemande;
import View.Model.ViewObject.ConnexAffairForView;
import View.Model.ViewObject.EditorForView;
import View.Model.ViewObject.PieceJointeForView;
import javafx.collections.ObservableList;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Affaire {

    public Affaire() {}

    public Affaire(int id,
                   String numero,
                   Timestamp dateDeFormulation,
                   TypeDemande typeDemande,
                   User redacteur,
                   AffaireStatus status,
                   PersonneMorale demandeur,
                   Terrain terrain){
        this.id = id;
        this.numero = numero;
        this.dateDeFormulation = dateDeFormulation;
        this.typeDemande = typeDemande;
        this.redacteur = redacteur;
        this.status = status;
        this.demandeur = demandeur;
        this.terrain = terrain;
    }

    public Affaire(String numero,
                   Timestamp dateDeFormulation,
                   TypeDemande typeDemande,
                   User redacteur,
                   AffaireStatus status,
                   PersonneMorale demandeur,
                   Terrain terrain){
        this.numero = numero;
        this.dateDeFormulation = dateDeFormulation;
        this.typeDemande = typeDemande;
        this.redacteur = redacteur;
        this.status = status;
        this.demandeur = demandeur;
        this.terrain = terrain;
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
    public static String affaireStatus2String(AffaireStatus status){
        switch (status){
            case RUNNING: return "EN_COURS";
            case SUCCEED: return "TERMINER";
            case SUSPEND: return "SUSPENDU";
            case REJECTED: return "REJETER";
        }
        return "";
    }
    public static AffaireStatus string2AffaireStatus(String name){
       switch (name){
           case "EN_COURS": return AffaireStatus.RUNNING;
           case "TERMINER": return AffaireStatus.SUCCEED;
           case "SUSPENDU": return AffaireStatus.SUSPEND;
           case "REJETER" : return AffaireStatus.REJECTED;
       }
       return AffaireStatus.RUNNING;
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
    public Timestamp getDateDeFormulation() {
        return dateDeFormulation;
    }
    public static TypeDemande string2TypeDemande(String type) { return (type.equals("ACQUISITION")) ? TypeDemande.ACQUISITION : TypeDemande.PRESCRIPTION; }
    public static String typeDemande2String(TypeDemande typeDemande) { return (typeDemande.equals(TypeDemande.ACQUISITION)) ? "ACQUISITION" : "PRESCRIPTION_ACQUISITIVE"; }
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

    public AffaireStatus getStatus() {
        return status;
    }
    public Terrain getTerrain() {
        return terrain;
    }
    public void setStatus(AffaireStatus status) {
        this.status = status;
    }
    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }
    public User getActualEditor(){
       return DaoFactory.getAffaireDao().getActualEditor(this);
    }
    public ObservableList<ConnexAffairForView> getAllAffaireConnexe(){ return  DaoFactory.getAffaireDao().getAllAffaireConnexeWith(this); }
    public ArrayList<EditorForView> getAllEditor() {
        return DaoFactory.getUserDao().getAllEditorAndDateByThis(this);
    }
    public ObservableList<ArrayList<String>> getAllProcedureChecked() { return DaoFactory.getAffaireDao().getAllProcedureAndDateConcernedByThis(this.getId()); }
    public ObservableList<PieceJointeForView> getAllPieceJointe(){return DaoFactory.getPieceJointeDao().getAllPieceJointe(this) ;}
    public void setDateDeFormulation(Timestamp dateDeFormulation) {
        this.dateDeFormulation = dateDeFormulation;
    }
    public void setTypeDemande(TypeDemande typeDemande) {
        this.typeDemande = typeDemande;
    }
    public PersonneMorale getDemandeur() {
        return demandeur;
    }
    public void setDemandeur(PersonneMorale demandeur) {
        this.demandeur = demandeur; }

    private  int id;
    private String numero;
    private Timestamp dateDeFormulation;
    private TypeDemande typeDemande;
    private User redacteur;
    private AffaireStatus status;
    private PersonneMorale demandeur;
    private Terrain terrain;
}