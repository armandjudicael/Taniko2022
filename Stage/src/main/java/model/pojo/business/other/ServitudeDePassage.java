package model.pojo.business.other;

public class ServitudeDePassage extends ProprieteOperation {
    private int idServitude;
    private double largeur;
    public int getIdServitude() {
        return idServitude;
    }
    public void setIdServitude(int idServitude) {
        this.idServitude = idServitude;
    }
    public double getLargeur() {
        return largeur;
    }
    public void setLargeur(double largeur) {
        this.largeur = largeur;
    }
    public ServitudeDePassage(String numBorderau, String numDepot, String numVolume, Double montant, int idServitude, double largeur) {
        super(numBorderau, numDepot, numVolume, montant);
        this.idServitude = idServitude;
        this.largeur = largeur;
    }
    public ServitudeDePassage(String numBorderau, String numDepot, String numVolume, Double montant, Dossier dossier, int idServitude, double largeur) {
        super(numBorderau, numDepot, numVolume, montant, dossier);
        this.idServitude = idServitude;
        this.largeur = largeur;
    }
    public ServitudeDePassage(String numBorderau, String numDepot, String numVolume, Double montant) {
        super(numBorderau, numDepot, numVolume, montant);
    }
    public ServitudeDePassage(String numBorderau, String numDepot, String numVolume, Double montant, Dossier dossier) {
        super(numBorderau, numDepot, numVolume, montant, dossier);
    }
}
