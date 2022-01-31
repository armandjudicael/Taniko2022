package model.Enum;

public enum TypeDemande {
    ACQUISITION,PRESCRIPTION,AFFECTATION;
    public static TypeDemande string2TypeDemande(String type) { return (type.equals("ACQUISITION")) ? TypeDemande.ACQUISITION : TypeDemande.PRESCRIPTION; }
    public static String typeDemande2String(TypeDemande typeDemande) { return (typeDemande.equals(TypeDemande.ACQUISITION)) ? "ACQUISITION" : "PRESCRIPTION_ACQUISITIVE"; }
}
