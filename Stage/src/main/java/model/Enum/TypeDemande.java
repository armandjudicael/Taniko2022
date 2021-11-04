package model.Enum;

public enum TypeDemande {
    ACQUISITION("Acquisition"),PRESCRIPTION("Prescription"),AFFECTATION("Affectation");
    private String lablel ;
    public String getLablel() {
        return lablel;
    }
    TypeDemande(String lablel) {
        this.lablel = lablel;
    }
}
