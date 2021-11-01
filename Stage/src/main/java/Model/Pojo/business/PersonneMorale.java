package Model.Pojo.business;

import Model.Enum.TypeDemandeur;
import View.Model.ViewObject.AffaireForView;
import View.Model.ViewObject.RepresentantForView;
import dao.DaoFactory;
import javafx.collections.ObservableList;

public class PersonneMorale{
    @Override public String toString() {
        return nom;
    }

    public PersonneMorale(){}

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
    public ObservableList<RepresentantForView> getAllRepresentant(){
        return DaoFactory.getDemandeurMoraleDao().getAllRepresentant(this);
    }
    public RepresentantForView getActualRepresentant(){
        return DaoFactory.getDemandeurMoraleDao().getActualRepresentant(this);
    }
    public ObservableList<AffaireForView> getAllAffaires(){
        return null;
    }
    private int id;
    private String adresse;
    private String numTel;
    private String email;
    private String nom;
    private TypeDemandeur type;
    private String nationalite;
}
