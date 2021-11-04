package view.Model.ViewObject;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProcedureForView {

    private int idProcedure;
    private SimpleStringProperty procedureName;
    private SimpleStringProperty numDepart;
    private SimpleBooleanProperty checked;
    private SimpleStringProperty dateArrive;
    private SimpleStringProperty dateDepart;
    private SimpleStringProperty numArrive;

    public ProcedureForView(int idProcedure, SimpleStringProperty procedureName, SimpleStringProperty numDepart, SimpleBooleanProperty checked, SimpleStringProperty dateArrive) {
        this.idProcedure = idProcedure;
        this.procedureName = procedureName;
        this.numDepart = numDepart;
        this.checked = checked;
        this.dateArrive = dateArrive;
    }

    public ProcedureForView(SimpleStringProperty procedureName, SimpleStringProperty numDepart, SimpleBooleanProperty checked, SimpleStringProperty dateArrive) {
        this.procedureName = procedureName;
        this.numDepart = numDepart;
        this.checked = checked;
        this.dateArrive = dateArrive;
    }

    public ProcedureForView(SimpleStringProperty procedureName, SimpleStringProperty numDepart, SimpleBooleanProperty checked, SimpleStringProperty dateArrive, SimpleStringProperty dateDepart, SimpleStringProperty numArrive) {
        this.procedureName = procedureName;
        this.numDepart = numDepart;
        this.checked = checked;
        this.dateArrive = dateArrive;
        this.dateDepart = dateDepart;
        this.numArrive = numArrive;
    }

    public ProcedureForView(int idProcedure, SimpleStringProperty procedureName, SimpleStringProperty numDepart, SimpleBooleanProperty checked, SimpleStringProperty dateArrive, SimpleStringProperty dateDepart, SimpleStringProperty numArrive) {
        this.idProcedure = idProcedure;
        this.procedureName = procedureName;
        this.numDepart = numDepart;
        this.checked = checked;
        this.dateArrive = dateArrive;
        this.dateDepart = dateDepart;
        this.numArrive = numArrive;
    }

    public void setIdProcedure(int idProcedure) {
        this.idProcedure = idProcedure;
    }

    public String getDateDepart() {
        return dateDepart.get();
    }

    public SimpleStringProperty dateDepartProperty() {
        return dateDepart;
    }

    public void setDateDepart(String dateDepart) {
        this.dateDepart.set(dateDepart);
    }

    public String getNumArrive() {
        return numArrive.get();
    }

    public SimpleStringProperty numArriveProperty() {
        return numArrive;
    }

    public void setNumArrive(String numArrive) {
        this.numArrive.set(numArrive);
    }

    @Override
    public String toString() {
        return "ProcedureForView{" +
                "idProcedure=" + idProcedure +
                ", procedureName=" + procedureName +
                ", numDepart=" + numDepart +
                ", checked=" + checked +
                ", dateArrive=" + dateArrive +
                '}';
    }

    public String getProcedureName() {
        return procedureName.get();
    }

    public void setProcedureName(String procedureName) {
        this.procedureName.set(procedureName);
    }

    public SimpleStringProperty procedureNameProperty() {
        return procedureName;
    }

    public String getDateArrive() {
        return dateArrive.get();
    }

    public void setDateArrive(String dateArrive) {
        this.dateArrive.set(dateArrive);
    }

    public SimpleStringProperty dateArriveProperty() {
        return dateArrive;
    }

    public String getNumDepart() {
        return numDepart.get();
    }

    public void setNumDepart(String numDepart) {
        this.numDepart.set(numDepart);
    }

    public SimpleStringProperty numDepartProperty() {
        return numDepart;
    }

    public boolean isChecked() {
        return checked.get();
    }

    public void setChecked(boolean checked) {
        this.checked.set(checked);
    }

    public SimpleBooleanProperty checkedProperty() {
        return checked;
    }

    public int getIdProcedure() {
        return idProcedure;
    }
}