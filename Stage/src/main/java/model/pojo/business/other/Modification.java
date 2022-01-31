package model.pojo.business.other;

public class Modification{
    private int idModif;
    private String largeur;
    private String resultat;
    private ModifType modifType;
    private Mutation mutation;
    public int getIdModif(){
        return idModif;
    }
    public void setIdModif(int idModif){
        this.idModif = idModif;
    }

    public String getLargeur(){
        return largeur;
    }

    public void setLargeur(String largeur){
        this.largeur = largeur;
    }
    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }
    public ModifType getModifType() {
        return modifType;
    }
    public void setModifType(ModifType modifType) {
        this.modifType = modifType;
    }
    public Mutation getMutation() {
        return mutation;
    }
    public void setMutation(Mutation mutation) {
        this.mutation = mutation;
    }
    public ServitudeDePassage getServitudeDePassage() {
        return servitudeDePassage;
    }
    public void setServitudeDePassage(ServitudeDePassage servitudeDePassage) {
        this.servitudeDePassage = servitudeDePassage;
    }
    private ServitudeDePassage servitudeDePassage;
    private enum ModifType{
        AUGMENTATION,DIMINUTION
    }
}
