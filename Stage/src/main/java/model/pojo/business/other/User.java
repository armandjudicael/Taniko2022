package model.pojo.business.other;

import dao.DaoFactory;
import model.other.RedactorAffair;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.FileInputStream;

public class User extends PersonnePhysique{

    private Image photo;
    private String userName;
    private String password;
    private String typeUtilisateur;
    private FileInputStream insertPhoto;
    private int status = 0;

    public User(Image photo,
                String userName,
                String password,
                String type,
                FileInputStream insertPhoto,
                int status) {
        this.photo = photo;
        this.userName = userName;
        this.password = password;
        this.typeUtilisateur = type;
        this.insertPhoto = insertPhoto;
        this.status = status;
    }

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setInsertPhoto(FileInputStream insertPhoto) {
        this.insertPhoto = insertPhoto;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User() {
    }

    public String getFullName() {
          return super.getNom();
    }

    @Override
    public String toString() {
        return getFullName();
    }

    public String getUtilisateurType(){
        return typeUtilisateur;
    }

    public void setTypeUtilisateur(String typeUtilisateur) {
        this.typeUtilisateur = typeUtilisateur;
    }

    public FileInputStream getInsertPhoto() {
        return insertPhoto;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String getNumTel() {
        return super.getNumTel();
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    public ObservableList<RedactorAffair> getAllAffairTraited() {
        return DaoFactory.getUserDao().getAllActualAffairTraitedBy(this, false);
    }

    public ObservableList<RedactorAffair> getActualAffairTraited() {
        return DaoFactory.getUserDao().getAllActualAffairTraitedBy(this, true);
    }

    public int getNbAffaire(){
        return DaoFactory.getDossierDao().getNbAffaireWhereActualEditorIs(this);
    }
}
