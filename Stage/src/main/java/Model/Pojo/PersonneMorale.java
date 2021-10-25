package Model.Pojo;

import Model.Enum.TypeDemandeur;

public class PersonneMorale {

    public PersonneMorale(){

    }

    public PersonneMorale(int id,
                          String adresse,
                          String numTel,
                          String email,
                          String nom,
                          TypeDemandeur type,
                          String nationalite){
        this.id = id;
        this.adresse = adresse;
        this.numTel = numTel;
        this.email = email;
        this.nom = nom;
        this.type = type;
        this.nationalite = nationalite;
    }

    public PersonneMorale(String adresse,
                          String numTel,
                          String email,
                          String nom,
                          TypeDemandeur type,
                          String nationalite) {
        this.adresse = adresse;
        this.numTel = numTel;
        this.email = email;
        this.nom = nom;
        this.type = type;
        this.nationalite = nationalite;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getAdresse() {
        return adresse;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public String getNumTel() {
        return numTel;
    }
    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public TypeDemandeur getType() {
        return type;
    }
    public void setType(TypeDemandeur type) {
        this.type = type;
    }
    public String getNationalite() {
        return nationalite;
    }
    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    private int id;
    private String adresse;
    private String numTel;
    private String email;
    private String nom;
    private TypeDemandeur type;
    private String nationalite;
}
