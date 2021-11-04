package model.pojo.utils;
import model.Enum.RegimeMatrimoniale;
import model.pojo.business.PersonnePhysique;

import java.sql.Date;

public class Mariage {

    public Mariage() {

    }

    public Mariage(Date dateMariage, String lieuMariage, RegimeMatrimoniale regimeMatrimoniale) {
        this.dateMariage = dateMariage;
        this.lieuMariage = lieuMariage;
        this.regimeMatrimoniale = regimeMatrimoniale;
    }

    public Mariage(Date dateMariage, String lieuMariage, RegimeMatrimoniale regimeMatrimoniale, PersonnePhysique demandeur, PersonnePhysique conjoint) {
        this.dateMariage = dateMariage;
        this.lieuMariage = lieuMariage;
        this.regimeMatrimoniale = regimeMatrimoniale;
        this.demandeur = demandeur;
        this.conjoint = conjoint;
    }

    public PersonnePhysique getDemandeur() {
        return demandeur;
    }
    public void setDemandeur(PersonnePhysique demandeur) {
        this.demandeur = demandeur;
    }
    public PersonnePhysique getConjoint() {
        return conjoint;
    }
    public void setConjoint(PersonnePhysique conjoint) {
        this.conjoint = conjoint;
    }
    public Date getDateMariage() {
        return dateMariage;
    }
    public void setDateMariage(Date dateMariage) {
        this.dateMariage = dateMariage;
    }
    public String getLieuMariage() {
        return lieuMariage;
    }
    public void setLieuMariage(String lieuMariage) {
        this.lieuMariage = lieuMariage;
    }
    public RegimeMatrimoniale getRegimeMatrimoniale() {
        return regimeMatrimoniale;
    }
    public void setRegimeMatrimoniale(RegimeMatrimoniale regimeMatrimoniale) {
        this.regimeMatrimoniale = regimeMatrimoniale;
    }
    public static String regimeMatrimoniale2String(RegimeMatrimoniale regimeMatrimoniale){
        if (regimeMatrimoniale.equals(RegimeMatrimoniale.DROIT_COMMUN)) return "Droit commun";
        else return "séparation des biens";
    }
    public static RegimeMatrimoniale String2RegimeMatrimoniale(String regime){
        if (regime.equals("Droit commun")) return RegimeMatrimoniale.DROIT_COMMUN;
        else return RegimeMatrimoniale.SEPARATION_DES_BIENS;
    }
    private Date dateMariage;
    private String lieuMariage;
    private RegimeMatrimoniale regimeMatrimoniale;
    private PersonnePhysique demandeur,conjoint;
}
