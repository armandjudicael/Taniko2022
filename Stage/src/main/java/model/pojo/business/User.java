package model.pojo.business;

import dao.DaoFactory;
import model.other.RedactorAffair;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.FileInputStream;

public class User {

    private int id;
    private String nom;
    private String prenom;
    private Image photo;
    private String userName;
    private String password;
    private String fonction;
    private String type;
    private FileInputStream insertPhoto;
    private int status = 0;

    public User(int id, String nom, String prenom, Image photo, String userName, String password, String type, String fonction, int userStatus) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.photo = photo;
        this.userName = userName;
        this.password = password;
        this.type = type;
        this.fonction = fonction;
        this.status = userStatus;
    }

    public User(String nom, String prenom, Image photo, String userName, String password, String type, String fonction, int userStatus) {
        this.nom = nom;
        this.prenom = prenom;
        this.photo = photo;
        this.userName = userName;
        this.password = password;
        this.type = type;
        this.fonction = fonction;
        this.status = userStatus;
    }

    public User(String nom, String prenom, FileInputStream photo, String userName, String password, String type, String fonction) {
        this.nom = nom;
        this.prenom = prenom;
        this.insertPhoto = photo;
        this.userName = userName;
        this.password = password;
        this.type = type;
        this.fonction = fonction;
    }

    public User(String nom, String prenom, FileInputStream photo, String userName, String password, String type, String fonction, int userStatus) {
        this.nom = nom;
        this.prenom = prenom;
        this.insertPhoto = photo;
        this.userName = userName;
        this.password = password;
        this.type = type;
        this.fonction = fonction;
        this.status = userStatus;
    }

    public User() {
    }

    public User(Image photo, String userName, String password) {
        this.photo = photo;
        this.userName = userName;
        this.password = password;
    }

    public String getFullName() {
        return nom + " " + prenom;
    }

    @Override
    public String toString() {
        return getFullName();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
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
    public String getFonction() {
        return fonction;
    }
    public void setFonction(String fonction) {
        this.fonction = fonction;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public FileInputStream getInsertPhoto() {
        return insertPhoto;
    }

    public int getStatus() {
        return status;
    }

    public ObservableList<RedactorAffair> getAllAffairTraited() {
        return DaoFactory.getUserDao().getAllActualAffairTraitedBy(this, false);
    }

    public ObservableList<RedactorAffair> getActualAffairTraited() {
        return DaoFactory.getUserDao().getAllActualAffairTraitedBy(this, true);
    }
}
