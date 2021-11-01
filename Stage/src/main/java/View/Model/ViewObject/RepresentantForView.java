package View.Model.ViewObject;
import Model.Pojo.business.PersonneMorale;
import java.sql.Date;

public class RepresentantForView {

    public RepresentantForView(PersonneMorale personneMorale, Date dateFormulation, String numeroAffaire) {
        this.personneMorale = personneMorale;
        this.dateFormulation = dateFormulation;
        this.numeroAffaire = numeroAffaire;
    }
    public PersonneMorale getPersonneMorale() {
        return personneMorale;
    }

    public RepresentantForView() {
    }

    public void setPersonneMorale(PersonneMorale personneMorale) {
        this.personneMorale = personneMorale;
    }

    public Date getDateFormulation() {
        return dateFormulation;
    }

    public void setDateFormulation(Date dateFormulation) {
        this.dateFormulation = dateFormulation;
    }

    public String getNumeroAffaire() {
        return numeroAffaire;
    }

    public void setNumeroAffaire(String numeroAffaire) {
        this.numeroAffaire = numeroAffaire;
    }
    private PersonneMorale personneMorale;
    private Date dateFormulation;
    private String numeroAffaire;
}
