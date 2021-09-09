package DAO;

import java.sql.Connection;

public abstract class DAO <T> {
    public DAO(Connection connection) {
        this.connection = connection;
    }
    public  abstract int  create(T t);
    public abstract int  delete(T t);
    public  abstract int update(T t);
    public  abstract T findById(int id);
    public  abstract T finByNum(int num);
    protected Connection  connection = null;
}
