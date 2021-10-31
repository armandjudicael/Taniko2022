package Model.Pojo.business;

import java.sql.Timestamp;

public class Titre {

    private int id;
    private String numTitreMere;
    private String numMorcelement;
    private String dateCreation;
    private String numExAffaire;
    private Timestamp date;
    private String numero;
    private String nomPropriete;
    private String titulaire;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Titre)) return false;
        Titre titre = (Titre) o;
        if (getId() != titre.getId()) return false;
        if (getNumTitreMere() != null ? !getNumTitreMere().equals(titre.getNumTitreMere()) : titre.getNumTitreMere() != null)
            return false;
        if (getNumMorcelement() != null ? !getNumMorcelement().equals(titre.getNumMorcelement()) : titre.getNumMorcelement() != null)
            return false;
        if (getDateCreation() != null ? !getDateCreation().equals(titre.getDateCreation()) : titre.getDateCreation() != null)
            return false;
        if (getNumExAffaire() != null ? !getNumExAffaire().equals(titre.getNumExAffaire()) : titre.getNumExAffaire() != null)
            return false;
        if (getDate() != null ? !getDate().equals(titre.getDate()) : titre.getDate() != null) return false;
        if (getNumero() != null ? !getNumero().equals(titre.getNumero()) : titre.getNumero() != null) return false;
        if (getNomPropriete() != null ? !getNomPropriete().equals(titre.getNomPropriete()) : titre.getNomPropriete() != null)
            return false;
        return getTitulaire() != null ? getTitulaire().equals(titre.getTitulaire()) : titre.getTitulaire() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getNumTitreMere() != null ? getNumTitreMere().hashCode() : 0);
        result = 31 * result + (getNumMorcelement() != null ? getNumMorcelement().hashCode() : 0);
        result = 31 * result + (getDateCreation() != null ? getDateCreation().hashCode() : 0);
        result = 31 * result + (getNumExAffaire() != null ? getNumExAffaire().hashCode() : 0);
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        result = 31 * result + (getNumero() != null ? getNumero().hashCode() : 0);
        result = 31 * result + (getNomPropriete() != null ? getNomPropriete().hashCode() : 0);
        result = 31 * result + (getTitulaire() != null ? getTitulaire().hashCode() : 0);
        return result;
    }

    public Titre() {

    }

    public Titre(String numero,
                 String nomPropriete,
                 String titulaire,
                 int id,
                 String numTitreMere,
                 String numMorcelement,
                 String dateCreation,
                 String numExAffaire) {
        this.numero = numero;
        this.nomPropriete = nomPropriete;
        this.titulaire = titulaire;
        this.id = id;
        this.numTitreMere = numTitreMere;
        this.numMorcelement = numMorcelement;
        this.dateCreation = dateCreation;
        this.numExAffaire = numExAffaire;
    }

    public Titre(String numero,
                 String nomPropriete,
                 String titulaire,
                 int id,
                 String dateAttribution,
                 String numMorcelement,
                 String numTitreMere) {
        this.numero = numero;
        this.nomPropriete = nomPropriete;
        this.titulaire = titulaire;
        this.id = id;
        this.dateCreation = dateAttribution;
        this.numMorcelement = numMorcelement;
        this.numTitreMere = numTitreMere;
    }

    public Titre(String numero,
                 String nomPropriete,
                 String titulaire,
                 String dateAttribution,
                 String numMorcelement,
                 String numTitreMere) {

        this.numero = numero;
        this.nomPropriete = nomPropriete;
        this.titulaire = titulaire;
        this.dateCreation = dateAttribution;
        this.numTitreMere = numTitreMere;
        this.numMorcelement = numMorcelement;
        this.numExAffaire = "";
    }

    public Titre(String numero,
                 String nomPropriete,
                 String numTitreMere,
                 String numMorcelement,
                 Timestamp dateCreation) {

        this.numero = numero;
        this.nomPropriete = nomPropriete;
        this.numTitreMere = numTitreMere;
        this.numMorcelement = numMorcelement;
        this.date = dateCreation;
    }

    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public String getNomPropriete() {
        return nomPropriete;
    }
    public void setNomPropriete(String nomPropriete) {
        this.nomPropriete = nomPropriete;
    }
    public String getTitulaire() {
        return titulaire;
    }
    public void setTitulaire(String titulaire) {
        this.titulaire = titulaire;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNumTitreMere() {
        return numTitreMere;
    }
    public void setNumTitreMere(String numTitreMere) {
        this.numTitreMere = numTitreMere;
    }
    public String getNumMorcelement() {
        return numMorcelement;
    }
    public void setNumMorcelement(String numMorcelement) {
        this.numMorcelement = numMorcelement;
    }
    public String getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }
    public String getNumExAffaire() {
        return numExAffaire;
    }
    public void setNumExAffaire(String numExAffaire) {
        this.numExAffaire = numExAffaire;
    }
    public Timestamp getDate() {
        return date;
    }
    public void setDate(Timestamp date) {
        this.date = date;
    }
    @Override
    public String toString() {
        return "TN° " +numero+" - "+nomPropriete;
    }
}
