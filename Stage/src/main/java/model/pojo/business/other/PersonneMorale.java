package model.pojo.business.other;

import model.Enum.TypeDemandeur;
import view.Model.ViewObject.FolderForView;
import view.Model.ViewObject.RepresentantForView;
import dao.DaoFactory;
import javafx.collections.ObservableList;

public class PersonneMorale extends Personne{
    private String numIdentificationFiscal;
    private String rcs;
    private String statistique;
    private String capital;
    private PersonnePhysique representant;
    public PersonneMorale(){
        this.setType(TypeDemandeur.PERSONNE_MORALE_PUBLIQUE);
    }
    public ObservableList<RepresentantForView> getAllRepresentant(){
        return DaoFactory.getDemandeurMoraleDao().getAllRepresentant(this);
    }
    public RepresentantForView getActualRepresentant(){
        return DaoFactory.getDemandeurMoraleDao().getActualRepresentant(this);
    }
    @Override public String toString() {
        return super.toString();
    }
    @Override public int getId() {
        return super.getId();
    }
    @Override public void setId(int id) {
        super.setId(id);
    }
    @Override public String getAdresse() {
        return super.getAdresse();
    }
    @Override public void setAdresse(String adresse) {
        super.setAdresse(adresse);
    }
    @Override public String getNumTel() {
        return super.getNumTel();
    }
    @Override public void setNumTel(String numTel) {
        super.setNumTel(numTel);
    }
    @Override public String getEmail() {
        return super.getEmail();
    }
    @Override public void setEmail(String email) {
        super.setEmail(email);
    }
    @Override public String getNom() {
        return super.getNom();
    }
    @Override public void setNom(String nom) {
        super.setNom(nom);
    }
    @Override public String getNationalite() {
        return super.getNationalite();
    }
    public PersonnePhysique getRepresentant() {
        return representant;
    }
    public void setRepresentant(PersonnePhysique representant) {
        this.representant = representant;
    }
    @Override public void setNationalite(String nationalite) {
        super.setNationalite(nationalite);
    }
    @Override protected ObservableList<FolderForView> getAllAffaires() {
        return super.getAllAffaires();
    }
    @Override public int hashCode() {
        return super.hashCode();
    }
    @Override public boolean equals(Object obj) {
        return super.equals(obj);
    }
    @Override protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    @Override protected void finalize() throws Throwable {
        super.finalize();
    }
    public String getNumIdentificationFiscal() {
        return numIdentificationFiscal;
    }
    public void setNumIdentificationFiscal(String numIdentificationFiscal) {
        this.numIdentificationFiscal = numIdentificationFiscal;
    }
    public String getRcs() {
        return rcs;
    }
    public void setRcs(String rcs) {
        this.rcs = rcs;
    }
    public String getStatistique() {
        return statistique;
    }
    public void setStatistique(String statistique) {
        this.statistique = statistique;
    }
    public String getCapital() {
        return capital;
    }
    public void setCapital(String capital) {
        this.capital = capital;
    }
}
