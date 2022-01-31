package model.pojo.business.attachement;

public class AttachComment{

    private int idAttach;
    private String comment;

    public AttachComment(int idAttach, String comment) {
        this.idAttach = idAttach;
        this.comment = comment;
    }

    public AttachComment(String comment) {
        this.comment = comment;
    }

    public int getIdAttach(){
        return idAttach;
    }

    public void setIdAttach(int idAttach){
        this.idAttach = idAttach;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
