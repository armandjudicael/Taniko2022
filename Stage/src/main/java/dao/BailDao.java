package dao;

import model.pojo.business.other.Bail;
import model.pojo.business.other.Personne;

import java.sql.Connection;

public class BailDao extends Dao implements DaoHelper<Bail>{
    public BailDao(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Bail bail) {
        return 0;
    }

    @Override
    public int delete(Bail bail) {
        return 0;
    }

    @Override
    public int update(Bail bail) {
        return 0;
    }

    @Override
    public Bail findById(int id) {
        return null;
    }

    @Override
    public Bail finByNum(int num) {
        return null;
    }
}
