package model.pojo.business.other;

public abstract class ProprieteOperation{

   private String numBorderau;
   private String numDepot;
   private String numVolume;
   private Double montant;
   private Dossier dossier;

    public String getNumBorderau() {
        return numBorderau;
    }

    public void setNumBorderau(String numBorderau) {
        this.numBorderau = numBorderau;
    }

    public String getNumDepot() {
        return numDepot;
    }

    public void setNumDepot(String numDepot) {
        this.numDepot = numDepot;
    }

    public String getNumVolume() {
        return numVolume;
    }

    public void setNumVolume(String numVolume) {
        this.numVolume = numVolume;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Dossier getDossier() {
        return dossier;
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    public ProprieteOperation(String numBorderau, String numDepot, String numVolume, Double montant) {
        this.numBorderau = numBorderau;
        this.numDepot = numDepot;
        this.numVolume = numVolume;
        this.montant = montant;
    }

    public ProprieteOperation(String numBorderau, String numDepot, String numVolume, Double montant, Dossier dossier) {
        this.numBorderau = numBorderau;
        this.numDepot = numDepot;
        this.numVolume = numVolume;
        this.montant = montant;
        this.dossier = dossier;
    }
}
