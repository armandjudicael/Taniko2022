package DAO;

import Model.Pojo.Terrain;

import java.sql.Connection;

public class DaoFactory{
    protected  static  final Connection connection = new ConnectionFactory().getConnection();
    public static AffairDao getAffaireDao() {
        return new AffairDao(connection);
    }
    public static ProcedureDao getProcedureDao(){
        return  new ProcedureDao(connection);
    }
    public static DemandeurDao getDemandeurDao() {
        return new DemandeurDao(connection);
    }
    public static TitleDao getTitreDao() {
        return new TitleDao(connection);
    }
    public static UserDao getUserDao(){
        return new UserDao(connection);
    }
    public static TerrainDao getTerrainDao(){return new TerrainDao(connection);}
}
