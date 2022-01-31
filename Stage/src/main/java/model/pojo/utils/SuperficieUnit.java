package model.pojo.utils;

import javafx.beans.property.DoubleProperty;
import model.Enum.Mesure;

public class SuperficieUnit{
    private Mesure mesure;
    private DoubleProperty value;

    public DoubleProperty valueProperty() {
        return value;
    }

    public void setValue(double value) {
        this.value.set(value);
    }

    public SuperficieUnit(Mesure mesure, DoubleProperty value) {
        this.mesure = mesure;
        this.value = value;
        this.valueProperty().addListener((observable, oldValue, newValue) -> {
            int intValue = newValue.intValue();
            int tmp = intValue%2;
            switch (mesure){
                case HECTARE:{
                    if (tmp!=0){

                    }
                }break;
                case ARE:{

                }break;
                case CENTI_ARE:{

                }break;
                case METRE_CARRE:{

                }break;
                default:{

                }
            }
        });
    }

    public Mesure getMesure() {
        return mesure;
    }

    public void setMesure(Mesure mesure) {
        this.mesure = mesure;
    }
    public DoubleProperty getValue() {
        return value;
    }

    private final Double CENTI_ARE = 1.0;
    private final Double ARE = 100.0;
    private final Double HECTARE = 10000.0;

}
