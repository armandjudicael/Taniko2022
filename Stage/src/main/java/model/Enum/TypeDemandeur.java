package model.Enum;
public enum TypeDemandeur {
    PERSONNE_MORALE_PRIVE,PERSONNE_PHYSIQUE,PERSONNE_MORALE_PUBLIQUE,SOCIETE,ASSOCIATION,EGLISE,ONG;
    TypeDemandeur() {
    }
    public static TypeDemandeur string2TypeDemandeur(String type){
        if (type.equals("Privé")) return TypeDemandeur.PERSONNE_MORALE_PRIVE;
        else if(type.equals("publique")) return TypeDemandeur.PERSONNE_MORALE_PUBLIQUE;
        else return TypeDemandeur.PERSONNE_PHYSIQUE;
    }
    public static String typeDemandeur2String(TypeDemandeur typeDemandeur){
        if (typeDemandeur.equals(TypeDemandeur.PERSONNE_MORALE_PRIVE)) return "morale privé";
        else if (typeDemandeur.equals(TypeDemandeur.PERSONNE_MORALE_PUBLIQUE)) return "morale publique";
        else return "physique";
    }
}
