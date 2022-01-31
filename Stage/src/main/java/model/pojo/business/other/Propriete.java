package model.pojo.business.other;

import javafx.collections.ObservableList;

import java.sql.Date;

public class Propriete {
    private int idPropriete;
    private String region;
    private String district;
    private String commune;
    private String parcelle;
    private String quartier;
    private String superficie;
    private String nomPropriete;
    private String numMorcelement;
    private String contenance;
    private String limite;
    private String numero;
    private Date dateCreation;
    private String consistance;
    private Propriete titreDependant;

    public Propriete(){

    }
    public Propriete(int idPropriete,
                     String region,
                     String district,
                     String commune,
                     String parcelle,
                     String quartier,
                     String superficie){
       this.superficie = superficie;
        this.idPropriete = idPropriete;
        this.region = region;
        this.district = district;
        this.commune = commune;
        this.parcelle = parcelle;
        this.quartier = quartier;
    }

    public Propriete(String region,
                     String district,
                     String commune,
                     String parcelle,
                     String quartier,
                     String superficie) {
        this.region = region;
        this.district = district;
        this.commune = commune;
        this.parcelle = parcelle;
        this.quartier = quartier;
        this.superficie = superficie;
    }

    public Propriete(int idPropriete,
                     String region,
                     String district,
                     String commune,
                     String parcelle,
                     String quartier,
                     String superficie,
                     Propriete titreDependant) {
        this.superficie = superficie;
        this.idPropriete = idPropriete;
        this.region = region;
        this.district = district;
        this.commune = commune;
        this.parcelle = parcelle;
        this.quartier = quartier;
        this.titreDependant = titreDependant;
    }


    public String getSuperficie() {
        return superficie;
    }

    public void setSuperficie(String superficie) {
        this.superficie = superficie;
    }

    public int getIdPropriete() {
        return idPropriete;
    }

    public void setIdPropriete(int idPropriete) {
        this.idPropriete = idPropriete;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getParcelle() {
        return parcelle;
    }

    public String getNomPropriete() {
        return nomPropriete;
    }

    public void setNomPropriete(String nomPropriete) {
        this.nomPropriete = nomPropriete;
    }

    public String getNumMorcelement() {
        return numMorcelement;
    }
    public void setNumMorcelement(String numMorcelement) {
        this.numMorcelement = numMorcelement;
    }
    public String getContenance() {
        return contenance;
    }
    public void setContenance(String contenance) {
        this.contenance = contenance;
    }
    public String getLimite() {
        return limite;
    }
    public void setLimite(String limite) {
        this.limite = limite;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public Date getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
    public void setParcelle(String parcelle) {
        this.parcelle = parcelle;
    }
    public String getQuartier() {
        return quartier;
    }
    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }
    public Propriete getTitreDependant() {
        return titreDependant;
    }
    public void setTitreDependant(Propriete titreDependant) {
        this.titreDependant = titreDependant;
    }
    public ObservableList<Modification> getAllModification(){
        return null;
    }
    public ObservableList<Prenotation> getAllPrenotation(){
        return null;
    }
    public ObservableList<ServitudeDePassage> getAllServitude(){
        return null;
    }
    public ObservableList<Bail> getAllBail(){
        return null;
    }
    public ObservableList<Hypotheque> getAllhypotheque(){
        return null;
    }
    public Personne getActualProprietary(){
        return null;
    }
    public ObservableList<Avis>getAllChargeAvis(){
        return null;
    }
    public String getActualSuperficie(){
        return "";
    }
    public void createCertificatSituationJuridique(){
    }
    public String getConsistance() {
        return consistance;
    }

    public void setConsistance(String consistance) {
        this.consistance = consistance;
    }
    @Override public String toString() {
        return "TN° " +numero+" - "+nomPropriete;
    }
}
