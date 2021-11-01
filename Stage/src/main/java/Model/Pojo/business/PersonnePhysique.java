package Model.Pojo.business;
import Model.Enum.TypeDemandeur;
import java.sql.Date;

public class PersonnePhysique extends PersonneMorale{

    public PersonnePhysique(){}

    public PersonnePhysique(int id,
                            String adresse,
                            String numTel,
                            String email,
                            String nom,
                            TypeDemandeur type,
                            String nationalite,
                            String parcelle,
                            String lot,
                            Date dateDeNaissance,
                            String lieuDeNaissance,
                            String sexe,
                            String profession,
                            String situationMatrimoniale,
                            String pere,
                            String mere) {
        super(id, adresse, numTel, email, nom, type, nationalite);
        this.parcelle = parcelle;
        this.lot = lot;
        this.dateDeNaissance = dateDeNaissance;
        this.lieuDeNaissance = lieuDeNaissance;
        this.sexe = sexe;
        this.profession = profession;
        this.situationMatrimoniale = situationMatrimoniale;
        this.pere = pere;
        this.mere = mere;
    }

    public PersonnePhysique(String nom,
                            String adresse,
                            String numTel,
                            String email,
                            TypeDemandeur type,
                            String nationalite,
                            String parcelle,
                            String lot,
                            Date dateDeNaissance,
                            String lieuDeNaissance,
                            String sexe,
                            String pere,
                            String mere,
                            String situationMatrimoniale,String profession){
        super(adresse, numTel, email, nom, type, nationalite);
        this.parcelle = parcelle;
        this.lot = lot;
        this.dateDeNaissance = dateDeNaissance;
        this.lieuDeNaissance = lieuDeNaissance;
        this.sexe = sexe;
        this.pere = pere;
        this.mere = mere;
        this.profession = profession;
        this.situationMatrimoniale = situationMatrimoniale;
    }

    public String getParcelle() {
        return parcelle;
    }
    public void setParcelle(String parcelle) {
        this.parcelle = parcelle;
    }
    public String getLot() {
        return lot;
    }
    public void setLot(String lot) {
        this.lot = lot;
    }
    public Date getDateDeNaissance() {
        return dateDeNaissance;
    }
    public void setDateDeNaissance(Date dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }
    public String getLieuDeNaissance() {
        return lieuDeNaissance;
    }
    public void setLieuDeNaissance(String lieuDeNaissance) {
        this.lieuDeNaissance = lieuDeNaissance;
    }
    public String getSexe() {
        return sexe;
    }
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }
    public String getSituationMatrimoniale() {
        return situationMatrimoniale;
    }
    public void setSituationMatrimoniale(String situationMatrimoniale) {
        this.situationMatrimoniale = situationMatrimoniale;
    }
    public String getProfession() {
        return profession;
    }
    public void setProfession(String profession) {
        this.profession = profession;
    }
    public String getPere() {
        return pere;
    }
    public void setPere(String pere) {
        this.pere = pere;
    }
    public String getMere() {
        return mere;
    }
    public void setMere(String mere) {
        this.mere = mere;
    }
    @Override public String toString() {
        return "PersonnePhysique{" +
                "parcelle='" + parcelle + '\'' +
                ", lot='" + lot + '\'' +
                ", dateDeNaissance=" + dateDeNaissance +
                ", lieuDeNaissance='" + lieuDeNaissance + '\'' +
                ", sexe='" + sexe + '\'' +
                ", profession='" + profession + '\'' +
                ", situationMatrimoniale='" + situationMatrimoniale + '\'' +
                ", pere='" + pere + '\'' +
                ", mere='" + mere + '\'' +
                '}';
    }
    private String parcelle;
    private String lot;
    private Date dateDeNaissance;
    private String lieuDeNaissance;
    private String sexe;
    private String profession;
    private String situationMatrimoniale;
    private String pere;
    private String mere;
}
