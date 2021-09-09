package Model.other;

import Model.Enum.ProcedureStatus;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProcedureForTableview {

    private ProcedureStatus status;
    private SimpleStringProperty description;

    public ProcedureForTableview(ProcedureStatus status, SimpleStringProperty description) {
        this.status = status;
        this.description = description;
    }

    public ProcedureForTableview() {

    }

    @Override
    public String toString() {
        return "ProcedureForTableview{" +
                "status=" + status +
                ", description=" + description +
                '}';
    }

    public ProcedureStatus getStatus() {
        return status;
    }

    public void setStatus(ProcedureStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
}
