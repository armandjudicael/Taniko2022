package model.pojo.utils;

import java.sql.Timestamp;

public class Ordonance extends UtilsData{

    public Ordonance(Timestamp date, String num) {
        super(date, num);
    }

    @Override
    public Timestamp getDate() {
        return super.getDate();
    }

    @Override
    public void setDate(Timestamp date) {
        super.setDate(date);
    }

    @Override
    public String getNum() {
        return super.getNum();
    }

    @Override
    public void setNum(String num) {
        super.setNum(num);
    }
}
