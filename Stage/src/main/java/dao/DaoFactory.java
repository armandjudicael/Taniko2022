package dao;
import model.Enum.ConnectionType;
import java.sql.Connection;
public class DaoFactory {
    protected  static  final Connection connection = new ConnectionFactory().getConnection(ConnectionType.POSTGRESQL);
    public static DossierDao getDossierDao() { return new DossierDao(connection); }
    public static ProcedureDao getProcedureDao(){
        return  new ProcedureDao(connection);
    }
    public static PersonnePhysiqueDao getDemandeurPhysiqueDao() {
        return new PersonnePhysiqueDao(connection);
    }
    public static PersonneMoraleDao getDemandeurMoraleDao(){return new PersonneMoraleDao(connection);}
    public static TitleDao getTitreDao() { return new TitleDao(connection); }
    public static UserDao getUserDao(){
        return new UserDao(connection);
    }
    public static ProprieteDao getTerrainDao(){return new ProprieteDao(connection);}
    public static AttachementDao getPieceJointeDao(){return new AttachementDao(connection);}
    public static HypothequeDao getHypothequeDao(){ return new HypothequeDao(connection);}
    public static BailDao getBailDao(){return  new BailDao(connection);}
    public static MutationDao getMutationDao(){return new MutationDao(connection);}
    public static PrenotationDao getPrenotationDao(){return new PrenotationDao(connection);}
    public static ServitudeDePassageDao getServitudeDePassageDao(){return new ServitudeDePassageDao(connection);}
}
