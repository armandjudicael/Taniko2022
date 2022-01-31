package dao;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class ConnectionFactoryTest {
    @Test public void createConnection(){
        final String postgresqlDb = "taniko2022Opms";
        final String postGresqlPort = "5432";
        final String postGresUrl = "jdbc:postgresql://"+"127.0.0.1"+":"+postGresqlPort+"/"+postgresqlDb;
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(postGresUrl, "postgres", "Aj30071999");
            assertTrue(connection!=null);
        } catch (SQLException | ClassNotFoundException throwables){
            throwables.printStackTrace();
        }
    }
}