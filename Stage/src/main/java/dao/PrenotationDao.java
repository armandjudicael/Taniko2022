package dao;

import model.pojo.business.other.Personne;
import model.pojo.business.other.Prenotation;

import java.sql.Connection;

public class PrenotationDao extends Dao implements DaoHelper<Prenotation>{

    public PrenotationDao(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Prenotation prenotation) {
        return 0;
    }

    @Override
    public int delete(Prenotation prenotation) {
        return 0;
    }

    @Override
    public int update(Prenotation prenotation) {
        return 0;
    }

    @Override
    public Prenotation findById(int id) {
        return null;
    }

    @Override
    public Prenotation finByNum(int num) {
        return null;
    }
}
