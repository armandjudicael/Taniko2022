package dao;

interface DaoHelper<T> {
    public  abstract int  create(T t);
    public abstract int  delete(T t);
    public  abstract int update(T t);
    public  abstract T findById(int id);
    public  abstract T finByNum(int num);
}
