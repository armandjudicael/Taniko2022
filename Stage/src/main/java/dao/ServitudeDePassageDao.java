package dao;

import model.pojo.business.other.Personne;
import model.pojo.business.other.ServitudeDePassage;

import java.sql.Connection;

public class ServitudeDePassageDao extends Dao implements DaoHelper<ServitudeDePassage>{
    public ServitudeDePassageDao(Connection connection) {
        super(connection);
    }

    @Override
    public int create(ServitudeDePassage servitudeDePassage) {
        return 0;
    }

    @Override
    public int delete(ServitudeDePassage servitudeDePassage) {
        return 0;
    }

    @Override
    public int update(ServitudeDePassage servitudeDePassage) {
        return 0;
    }

    @Override
    public ServitudeDePassage findById(int id) {
        return null;
    }

    @Override
    public ServitudeDePassage finByNum(int num) {
        return null;
    }
}
