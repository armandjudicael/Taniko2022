package view.Model.ViewObject;

public class MorcelementForView {
    private String nomProprieteParent;
    private String nomProprieteFils;
    private String valeur;
    private String typeMorcelement;
    public MorcelementForView(String nomProprieteParent, String nomProprieteFils, String valeur, String typeMorcelement) {
        this.nomProprieteParent = nomProprieteParent;
        this.nomProprieteFils = nomProprieteFils;
        this.valeur = valeur;
        this.typeMorcelement = typeMorcelement;
    }

    public String getNomProprieteParent() {
        return nomProprieteParent;
    }
    public void setNomProprieteParent(String nomProprieteParent) {
        this.nomProprieteParent = nomProprieteParent;
    }
    public String getNomProprieteFils() {
        return nomProprieteFils;
    }
    public void setNomProprieteFils(String nomProprieteFils) {
        this.nomProprieteFils = nomProprieteFils;
    }
    public String getValeur() {
        return valeur;
    }
    public void setValeur(String valeur) {
        this.valeur = valeur;
    }
    public String getTypeMorcelement() {
        return typeMorcelement;
    }
    public void setTypeMorcelement(String typeMorcelement) {
        this.typeMorcelement = typeMorcelement;
    }
}
