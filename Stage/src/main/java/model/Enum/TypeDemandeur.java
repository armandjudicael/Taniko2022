package model.Enum;

public enum TypeDemandeur {
    PERSONNE_MORALE,PERSONNE_PHYSIQUE;
    TypeDemandeur() {
    }
    public static TypeDemandeur string2TypeDemandeur(String type){
        if (type.equals("morale")) return TypeDemandeur.PERSONNE_MORALE;
        else return TypeDemandeur.PERSONNE_PHYSIQUE;
    }
    public static String typeDemandeur2String(TypeDemandeur typeDemandeur){
        if (typeDemandeur.equals(TypeDemandeur.PERSONNE_MORALE)) return "morale";
        else return "physique";
    }
}
