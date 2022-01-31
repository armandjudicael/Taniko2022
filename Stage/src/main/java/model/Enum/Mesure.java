package model.Enum;

public enum Mesure{
    ARE,CENTI_ARE,HECTARE,METRE_CARRE,DECIMETRE_CARRE;
    public static String mesureToString(Mesure mesure){
        return  (mesure == HECTARE ? "Ha":
                (mesure == ARE ? "A" :
                        (mesure == CENTI_ARE ? "Ca":
                                ( mesure == METRE_CARRE ? "m²": "dm²"))));
    }
    public static Mesure string2Mesure(String value ){
        return  ( value.equals("Ha") ? HECTARE :
                (value.equals("A") ? ARE :
                        ( value.equals("Ca") ? CENTI_ARE :
                                ( value.equals("m²") ? METRE_CARRE : DECIMETRE_CARRE))));
    }
}
