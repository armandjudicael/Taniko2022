package Model.Pojo;

import Model.Enum.TypeDemandeur;

import java.util.ArrayList;

public class Demandeur {

    private ArrayList<Affaire> affaires;

    public Demandeur() {
    }

    public Demandeur(String nom, String prenom, String adresse, String parcelle, String lot) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.parcelle = parcelle;
        this.lot = lot;
    }

    public Demandeur(int id, String nom, String prenom, String adresse, String parcelle, String lot) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.parcelle = parcelle;
        this.lot = lot;
    }

    public String getFullName() {
        return getNom() + ' ' + getPrenom();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getParcelle() {
        return parcelle;
    }

    public void setParcelle(String parcelle) {
        this.parcelle = parcelle;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    @Override
    public String toString() {
        return getFullName();
    }
    public ArrayList<Affaire> getAffaires() {
        return affaires;
    }

    private int id;
    private String nom;
    private String prenom;
    private String numTel;
    private String adresse;
    private String parcelle;
    private String lot;
    private String ville;
    private TypeDemandeur type;
    private String nomInstitution;
    public void setAffaires(ArrayList<Affaire> affaires) {
        this.affaires = affaires;
    }
}
