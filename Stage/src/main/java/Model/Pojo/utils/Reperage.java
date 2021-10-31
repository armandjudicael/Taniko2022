package Model.Pojo.utils;

import java.sql.Timestamp;

public class Reperage extends UtilsData{
    public Reperage(Timestamp date, String num) {
        super(date, num);
    }
    @Override
    public Timestamp getDate() {
        return super.getDate();
    }

    @Override
    public String getNum() {
        return super.getNum();
    }
}
