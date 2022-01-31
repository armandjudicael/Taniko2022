package model.pojo.business.other;

import java.sql.Date;
import java.util.List;

public class Hypotheque extends ProprieteOperation{
    private TypeHypotheque typeHypotheque;
    private Personne hypothecaire;
    private int idHypotheque;
    private Date dateHypotheque;

    public Hypotheque(String numBorderau, String numDepot, String numVolume, Double montant, TypeHypotheque typeHypotheque, Personne hypothecaire, int idHypotheque, Date dateHypotheque) {
        super(numBorderau, numDepot, numVolume, montant);
        this.typeHypotheque = typeHypotheque;
        this.hypothecaire = hypothecaire;
        this.idHypotheque = idHypotheque;
        this.dateHypotheque = dateHypotheque;
    }

    public Date getDateHypotheque() {
        return dateHypotheque;
    }

    public void setDateHypotheque(Date dateHypotheque) {
        this.dateHypotheque = dateHypotheque;
    }

    public Hypotheque(String numBorderau, String numDepot, String numVolume, Double montant) {
        super(numBorderau, numDepot, numVolume, montant);
    }

    public TypeHypotheque getTypeHypotheque() {
        return typeHypotheque;
    }

    public void setTypeHypotheque(TypeHypotheque typeHypotheque) {
        this.typeHypotheque = typeHypotheque;
    }

    public Personne getHypothecaire() {
        return hypothecaire;
    }

    public void setHypothecaire(Personne hypothecaire) {
        this.hypothecaire = hypothecaire;
    }

    public int getIdHypotheque() {
        return idHypotheque;
    }
    public void setIdHypotheque(int idHypotheque) {
        this.idHypotheque = idHypotheque;
    }
    private enum TypeHypotheque{
        CONVENTIONNELE,JUDICIAIRE,LEGALE
    }
}
