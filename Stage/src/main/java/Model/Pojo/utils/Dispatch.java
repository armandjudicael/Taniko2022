package Model.Pojo.utils;

import java.sql.Timestamp;

public class Dispatch{
    private int userId;
    private int affaireId;
    private Timestamp dateDispatch;

    public Dispatch(int userId, int affaireId, Timestamp dateDispatch) {
        this.userId = userId;
        this.affaireId = affaireId;
        this.dateDispatch = dateDispatch;
    }

    public int getUserId() {
        return userId;
    }

    public int getAffaireId() {
        return affaireId;
    }

    public Timestamp getDateDispatch() {
        return dateDispatch;
    }
}
