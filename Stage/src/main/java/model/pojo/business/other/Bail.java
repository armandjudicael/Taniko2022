package model.pojo.business.other;

import java.sql.Date;

public class Bail  extends ProprieteOperation{
    private int idBail;
    private String dureBail;
    private Personne bailleur;
    private TypeBail typeBail;
    private Date dateBail;

    public TypeBail getTypeBail() {
        return typeBail;
    }

    public void setTypeBail(TypeBail typeBail) {
        this.typeBail = typeBail;
    }

    public Date getDateBail() {
        return dateBail;
    }

    public void setDateBail(Date dateBail) {
        this.dateBail = dateBail;
    }

    public Bail(String numBorderau, String numDepot, String numVolume, Double montant) {
        super(numBorderau, numDepot, numVolume, montant);
    }

    public Bail(String numBorderau, String numDepot, String numVolume, Double montant, int idBail, String dureBail, Personne bailleur, TypeBail typeBail, Date dateBail) {
        super(numBorderau, numDepot, numVolume, montant);
        this.idBail = idBail;
        this.dureBail = dureBail;
        this.bailleur = bailleur;
        this.typeBail = typeBail;
        this.dateBail = dateBail;
    }

    private enum TypeBail {
        AMIABLE,SIMPLE,EMPHYTEOTIQUE
    }
    public int getIdBail() {
        return idBail;
    }

    public void setIdBail(int idBail) {
        this.idBail = idBail;
    }

    public String getDureBail() {
        return dureBail;
    }

    public void setDureBail(String dureBail) {
        this.dureBail = dureBail;
    }

    public Personne getBailleur() {
        return bailleur;
    }

    public void setBailleur(Personne bailleur) {
        this.bailleur = bailleur;
    }
}
