package Model.other;

import Model.Enum.AffaireStatus;

public class RedactorAffair {

    private String numero;
    private String fullName;
    private String dispatchDate;
    private AffaireStatus status;

    public RedactorAffair(String numero, String fullName, String dispatchDate, AffaireStatus status) {
        this.numero = numero;
        this.fullName = fullName;
        this.dispatchDate = dispatchDate;
        this.status = status;
    }

    public AffaireStatus getStatus() {
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
}
