package DAO;

import java.sql.Connection;

public abstract class Dao {
    protected Connection connection;
    public Dao(Connection connection) {
     this.connection = connection;
    }
}
