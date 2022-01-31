package model.other;

import javafx.beans.property.SimpleDoubleProperty;
import model.Enum.Mesure;
import model.pojo.utils.SuperficieUnit;

import java.sql.SQLData;
import java.util.*;

public class Superficie extends ArrayList<SuperficieUnit>{

    public void add(Superficie sup){
        for (int i = 0; i < this.size(); i++) {
            SuperficieUnit thisUnit = this.get(i);
            if(sup.size()>=i){
                SuperficieUnit unit = sup.get(i);
                if (thisUnit.getMesure() == unit.getMesure()){
                    Double value = thisUnit.valueProperty().get();
                    Double newValue = value+unit.getValue().getValue();
                    thisUnit.setValue(newValue);
                }
            }
        }
    }

    public void remove(Superficie sup){
        for (int i = 0; i < this.size(); i++) {
            SuperficieUnit thisUnit = this.get(i);
            if(sup.size()>=i){
                SuperficieUnit unit = sup.get(i);
                if (thisUnit.getMesure() == unit.getMesure()){
                    Double value = thisUnit.valueProperty().get();
                    Double newValue = value-unit.getValue().getValue();
                    thisUnit.setValue(newValue);
                }
            }
        }
    }

    public static Superficie create(String value){
        String[] split = value.split("-");
        Superficie superficie = new Superficie();
        if(split.length!=0){
            for (String s : split) {
                String[] split1 = s.split("\\ ");
                if (split1.length!=0){
                    Double valeur = Double.valueOf(split1[0]);
                    Mesure mesure = Mesure.string2Mesure(split1[1]);
                    superficie.add(new SuperficieUnit(mesure,new SimpleDoubleProperty(valeur)));
                }
            }
        }
        return superficie;
    }
}
