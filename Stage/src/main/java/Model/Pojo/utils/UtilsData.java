package Model.Pojo.utils;

import java.sql.Timestamp;

public abstract class UtilsData {
    protected Timestamp date;
    protected String num;
    public UtilsData(Timestamp date, String num) {
        this.date = date;
        this.num = num;
    }
    public Timestamp getDate() {
        return date;
    }
    public void setDate(Timestamp date) {
        this.date = date;
    }
    public String getNum() {
        return num;
    }
    public void setNum(String num) {
        this.num = num;
    }
}
