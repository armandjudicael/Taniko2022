package Model.Pojo;
import Model.Enum.TypeDemandeur;
import java.sql.Timestamp;

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
                            Timestamp dateDeNaissance,
                            String lieuDeNaissance,
                            PersonnePhysique conjoint,
                            String sexe,
                            Mariage mariage) {
        super(id, adresse, numTel, email, nom, type, nationalite);
        this.parcelle = parcelle;
        this.lot = lot;
        this.dateDeNaissance = dateDeNaissance;
        this.lieuDeNaissance = lieuDeNaissance;
        this.conjoint = conjoint;
        this.sexe = sexe;
        this.mariage = mariage;
    }

    public PersonnePhysique(String adresse,
                            String numTel,
                            String email,
                            String nom,
                            TypeDemandeur type,
                            String nationalite,
                            String parcelle,
                            String lot,
                            Timestamp dateDeNaissance,
                            String lieuDeNaissance,
                            PersonnePhysique conjoint,
                            String sexe,
                            Mariage mariage) {
        super(adresse, numTel, email, nom, type, nationalite);
        this.parcelle = parcelle;
        this.lot = lot;
        this.dateDeNaissance = dateDeNaissance;
        this.lieuDeNaissance = lieuDeNaissance;
        this.conjoint = conjoint;
        this.sexe = sexe;
        this.mariage = mariage;
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

    public Timestamp getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(Timestamp dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public String getLieuDeNaissance() {
        return lieuDeNaissance;
    }

    public void setLieuDeNaissance(String lieuDeNaissance) {
        this.lieuDeNaissance = lieuDeNaissance;
    }

    public PersonnePhysique getConjoint() {
        return conjoint;
    }

    public void setConjoint(PersonnePhysique conjoint) {
        this.conjoint = conjoint;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Mariage getMariage() {
        return mariage;
    }

    public void setMariage(Mariage mariage) {
        this.mariage = mariage;
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

    private String parcelle;
    private String lot;
    private Timestamp dateDeNaissance;
    private String lieuDeNaissance;
    private PersonnePhysique conjoint;
    private String sexe;
    private Mariage mariage;
    private String profession;
    private String situationMatrimoniale;
}
