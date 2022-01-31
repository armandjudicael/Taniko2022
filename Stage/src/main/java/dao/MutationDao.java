package dao;

import model.pojo.business.other.Mutation;
import model.pojo.business.other.Personne;

import java.sql.Connection;

public class MutationDao extends Dao implements DaoHelper<Mutation>{
    public MutationDao(Connection connection) {
        super(connection);
    }

    @Override public int create(Mutation mutation) {
        return 0;
    }

    @Override
    public int delete(Mutation mutation) {
        return 0;
    }

    @Override
    public int update(Mutation mutation) {
        return 0;
    }

    @Override
    public Mutation findById(int id) {
        return null;
    }

    @Override
    public Mutation finByNum(int num) {
        return null;
    }
}
