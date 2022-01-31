package model.other;

import model.Enum.FolderStatus;
import view.Model.ViewObject.ProcedureForTableview;

public class RedactorAffair{
    private String numero;
    private String fullName;
    private String dispatchDate;
    private FolderStatus status;
    private ProcedureForTableview situationProcedure;

    public RedactorAffair(String numero, String fullName, String dispatchDate, FolderStatus status, ProcedureForTableview situationProcedure) {
        this.numero = numero;
        this.fullName = fullName;
        this.dispatchDate = dispatchDate;
        this.status = status;
        this.situationProcedure = situationProcedure;
    }
    public RedactorAffair(String numero, String fullName, String dispatchDate, FolderStatus status) {
        this.numero = numero;
        this.fullName = fullName;
        this.dispatchDate = dispatchDate;
        this.status = status;
    }

    public FolderStatus getStatus() {
        return status;
    }
    public String getNumero() {
        return numero;
    }
    public String getFullName() {
        return fullName;
    }
    public String getDispatchDate() {
        return dispatchDate;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setDispatchDate(String dispatchDate) {
        this.dispatchDate = dispatchDate;
    }
    public void setStatus(FolderStatus status) {
        this.status = status;
    }
    public void setSituationProcedure(ProcedureForTableview situationProcedure) {
        this.situationProcedure = situationProcedure;
    }
    public ProcedureForTableview getSituationProcedure() {
        return situationProcedure;
    }
    @Override public String toString() {
        return "RedactorAffair{" +
                "numero='" + numero + '\'' +
                ", fullName='" + fullName + '\'' +
                ", dispatchDate='" + dispatchDate + '\'' +
                ", status=" + status +
                ", situationProcedure=" + situationProcedure +
                '}';
    }
}
