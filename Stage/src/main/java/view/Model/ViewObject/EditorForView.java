package view.Model.ViewObject;
import javafx.scene.image.Image;
import java.sql.Timestamp;

public class EditorForView {

    private Timestamp dateEtHeure;
    private String fullName;
    private Image photo;
    private int idUser;

    public EditorForView(Timestamp dateEtHeure, String fullName, Image photo, int id) {
        this.dateEtHeure = dateEtHeure;
        this.fullName = fullName;
        this.photo = photo;
        this.idUser = id;
    }

    public int getId() {
        return idUser;
    }
    public Timestamp getDateEtHeure() {
        return dateEtHeure;
    }
    public void setDateEtHeure(Timestamp dateEtHeure) {
        this.dateEtHeure = dateEtHeure;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public Image getPhoto() {
        return photo;
    }
    public void setPhoto(Image photo) {
        this.photo = photo;
    }
}
