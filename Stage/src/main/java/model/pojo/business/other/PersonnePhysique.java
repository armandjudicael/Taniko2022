package model.pojo.business.other;
import model.Enum.SituationMatrimoniale;
import model.Enum.TypeDemandeur;
import model.pojo.utils.Mariage;
import java.sql.Date;

public class PersonnePhysique extends Personne{
    public PersonnePhysique(){
        this.setType(TypeDemandeur.PERSONNE_PHYSIQUE);
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
    public SituationMatrimoniale getSituationMatrimoniale() {
        return situationMatrimoniale;
    }
    public void setSituationMatrimoniale(SituationMatrimoniale sm) {
        this.situationMatrimoniale = sm;
    }
    public String getNumCin() {
        return numCin;
    }
    public void setNumCin(String numCin) {
        this.numCin = numCin;
    }
    public Date getDateDelivranceCin() {
        return dateDelivranceCin;
    }
    public void setDateDelivranceCin(Date dateDelivranceCin) {
        this.dateDelivranceCin = dateDelivranceCin;
    }
    public String getLieuDelivranceCin() {
        return lieuDelivranceCin;
    }
    public void setLieuDelivranceCin(String lieuDelivranceCin) {
        this.lieuDelivranceCin = lieuDelivranceCin;
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
    public PersonnePhysique getConjoint() {
        return conjoint;
    }
    public void setConjoint(PersonnePhysique conjoint) {
        this.conjoint = conjoint;
    }
    public Mariage getMariage() {
        return mariage;
    }
    public void setMariage(Mariage mariage) {
        this.mariage = mariage;
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
    SituationMatrimoniale situationMatrimoniale;
    private String pere;
    private String mere;
    private String numCin;
    private Date dateDelivranceCin;
    private String lieuDelivranceCin;
    private PersonnePhysique conjoint;
    private Mariage mariage;
}
