package View.Model;

import Model.Enum.AffaireStatus;

import java.util.Objects;

public class ConnexAffairForView {

    private String numero;
    private String nomDemandeur;
    private int id;
    private AffaireStatus status;
    private int idTerrain;


    public ConnexAffairForView(int idAffaire,String numero, String nomDemandeur, AffaireStatus status) {
        this.numero = numero;
        this.nomDemandeur = nomDemandeur;
        this.status = status;
        this.id = idAffaire;
    }


    public ConnexAffairForView(String numero, String nomDemandeur,int idTerrain) {
        this.numero = numero;
        this.nomDemandeur = nomDemandeur;
        this.idTerrain = idTerrain;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) { this.id = id; }
    public int getIdTerrain() { return idTerrain; }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public String getNomDemandeur() {
        return nomDemandeur;
    }
    @Override public String toString() {
        return " N° "+ numero + " - " + nomDemandeur;
    }
    public AffaireStatus getStatus() {
        return status;
    }
    public void setStatus(AffaireStatus status) {
        this.status = status;
    }
    public void setIdTerrain(int idTerrain) {
        this.idTerrain = idTerrain;
    }
}

