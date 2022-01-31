package dao;

import model.pojo.business.other.Hypotheque;
import model.pojo.business.other.Personne;

import java.sql.Connection;

public class HypothequeDao extends Dao implements DaoHelper<Hypotheque> {

    public HypothequeDao(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Hypotheque hypotheque) {
        return 0;
    }

    @Override
    public int delete(Hypotheque hypotheque) {
        return 0;
    }

    @Override
    public int update(Hypotheque hypotheque) {
        return 0;
    }

    @Override
    public Hypotheque findById(int id) {
        return null;
    }

    @Override
    public Hypotheque finByNum(int num) {
        return null;
    }
}
