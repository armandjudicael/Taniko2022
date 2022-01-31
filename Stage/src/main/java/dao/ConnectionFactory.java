package dao;

import Main.Main;
import model.Enum.ConnectionType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory{

    private static Connection connection=null;
    final String localhost = "localhost";
    final String localPort = "4040";
    final String localPassword = "Aj30071999";

    final String user = "postgres";
    final String finalhost = "192.168.1.50";
    final String finalPort = "3306";
    final String finalDb = "taniko2022Opms";

    final String postgresqlDb = "taniko2022Opms";
    final String postGresqlPort = "5432";

    final String finalPassword = "Aj!30071999";
    final String localUrl = "jdbc:mysql://" + localhost + ":" + localPort + "/" + finalDb + "?useLegacyDatetimeCode=false&serverTimezone=UTC";
    final String finalUrl = "jdbc:mysql://" + finalhost + ":" + finalPort + "/" + finalDb + "?useLegacyDatetimeCode=false&serverTimezone=UTC";

    public ConnectionFactory() {


    }

    // pattern singleton
    public Connection getConnection(){
        if (connection == null){
            try {
                //connection = DriverManager.getConnection(finalUrl,user,finalPassword);
                connection = DriverManager.getConnection(localUrl,user,localPassword);
                connection.setAutoCommit(false);
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                return connection;
            } catch (SQLException e){
                Main.isReacheable = false;
                e.printStackTrace();
            }
        }
        return connection;
    }

    public Connection getConnection(ConnectionType type){
        if (connection == null){
            try {
                String url = "";
                if(type == ConnectionType.POSTGRESQL){
                    Class.forName("org.postgresql.Driver");
                    url =  "jdbc:postgresql://"+localhost+":"+postGresqlPort+"/"+postgresqlDb;
                }else url = "jdbc:mysql://" + localhost + ":" + localPort + "/" + finalDb + "?useLegacyDatetimeCode=false&serverTimezone=UTC";
                //connection = DriverManager.getConnection(finalUrl,user,finalPassword);
                connection = DriverManager.getConnection(url,user,localPassword);
                connection.setAutoCommit(false);
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                return connection;
            } catch (SQLException | ClassNotFoundException e){
                Main.isReacheable = false;
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static Connection getConnection(String user, String password, String host, String port, String databaseName) {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?useLegacyDatetimeCode=false&serverTimezone=UTC";
        if (connection == null){
            try {
                connection = DriverManager.getConnection(url,user,password);
                connection.setAutoCommit(false);
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                return connection;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
