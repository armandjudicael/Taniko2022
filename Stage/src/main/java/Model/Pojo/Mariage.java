package Model.Pojo;

import Model.Enum.RegimeMatrimoniale;

import java.sql.Timestamp;

public class Mariage {
    public Mariage(Timestamp dateMariage, String lieuMariage, RegimeMatrimoniale regimeMatrimoniale) {
        this.dateMariage = dateMariage;
        this.lieuMariage = lieuMariage;
        this.regimeMatrimoniale = regimeMatrimoniale;
    }
    public Timestamp getDateMariage() {
        return dateMariage;
    }
    public void setDateMariage(Timestamp dateMariage) {
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

    private Timestamp dateMariage;
    private String lieuMariage;
    private RegimeMatrimoniale regimeMatrimoniale;
}
