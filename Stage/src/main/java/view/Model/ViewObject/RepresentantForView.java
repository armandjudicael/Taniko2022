package view.Model.ViewObject;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.ObservableList;
import model.pojo.business.PersonneMorale;
import java.sql.Date;

public class RepresentantForView extends RecursiveTreeObject<RepresentantForView>{
    public RepresentantForView(PersonneMorale personneMorale, Date dateFormulation, String numeroAffaire) {
        this.personneMorale = personneMorale;
        this.dateFormulation = dateFormulation;
        this.numeroAffaire = numeroAffaire;
    }
    public PersonneMorale getPersonneMorale() {
        return personneMorale;
    }
    public RepresentantForView(){}
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
    @Override public ObservableList<RepresentantForView> getChildren() {
        return super.getChildren();
    }
    @Override public void setChildren(ObservableList<RepresentantForView> children) {
        super.setChildren(children);
    }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RepresentantForView)) return false;
        RepresentantForView that = (RepresentantForView) o;
        if (!getPersonneMorale().equals(that.getPersonneMorale())) return false;
        if (!getDateFormulation().equals(that.getDateFormulation())) return false;
        return getNumeroAffaire().equals(that.getNumeroAffaire());
    }
    @Override public int hashCode() {
        int result = getPersonneMorale().hashCode();
        result = 31 * result + getDateFormulation().hashCode();
        result = 31 * result + getNumeroAffaire().hashCode();
        return result;
    }
}
