package model.pojo.utils;

import java.sql.Date;

public  class UtilsData {
    protected Date date;
    protected String num;
    public UtilsData(Date date, String num) {
        this.date = date;
        this.num = num;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getNum() {
        return num;
    }
    public void setNum(String num) {
        this.num = num;
    }
}
