package model.pojo.business.other;

import javafx.collections.ObservableList;
import model.Enum.TypeDemandeur;
import view.Model.ViewObject.FolderForView;

public abstract class Personne{
     private int id;
     private String adresse;
     private String numTel;
     private String email;
     private String nom;
     private String nationalite;
     private TypeDemandeur type;

     public Personne() {
     }

     public Personne(int id, String adresse, String numTel, String email, String nom, String nationalite) {
         this.id = id;
         this.adresse = adresse;
         this.numTel = numTel;
         this.email = email;
         this.nom = nom;
         this.nationalite = nationalite;
     }

     public Personne(String adresse, String numTel, String email, String nom, String nationalite) {
         this.adresse = adresse;
         this.numTel = numTel;
         this.email = email;
         this.nom = nom;
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
     public String getNationalite() {
         return nationalite;
     }
     public void setNationalite(String nationalite) {
         this.nationalite = nationalite;
     }
     protected ObservableList<FolderForView> getAllAffaires(){
        return null;
     }
     public TypeDemandeur getType() {
         return type;
     }
     public void setType(TypeDemandeur type) {
         this.type = type;
     }
     @Override public String toString() {
         return "Personne{" +
                 "id=" + id +
                 ", adresse='" + adresse + '\'' +
                 ", numTel='" + numTel + '\'' +
                 ", email='" + email + '\'' +
                 ", nom='" + nom + '\'' +
                 ", nationalite='" + nationalite + '\'' +
                 ", type=" + type +
                 '}';
     }
 }
