package Model.Pojo;

import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.io.FileUtils;

import java.io.FileInputStream;
import java.io.InputStream;

public class PieceJointe {

    private int idPieceJointe;
    private String description;
    private String extensionPiece;
    private String size;
    private FileInputStream valeur;
    private InputStream inputStream;

    public PieceJointe(int idPieceJointe,
                       String description,
                       String extensionPiece,
                       InputStream valeur) {
        this.idPieceJointe = idPieceJointe;
        this.description = description;
        this.extensionPiece = extensionPiece.toLowerCase();
        this.inputStream = valeur;
    }

    public PieceJointe(String description,
                       String extensionPiece,
                       FileInputStream valeur){
        this.description = description;
        this.extensionPiece = extensionPiece.toLowerCase();
        this.valeur = valeur;
    }

    public PieceJointe(String description,
                       String extensionPiece,
                       String fileSize,
                       FileInputStream valeur){
        this.description = description;
        this.extensionPiece = extensionPiece.toLowerCase();
        this.valeur = valeur;
        this.size = fileSize;
    }

    public PieceJointe() {
    }

    public int getIdPieceJointe() {
        return idPieceJointe;
    }
    public void setIdPieceJointe(int idPieceJointe) {
        this.idPieceJointe = idPieceJointe;
    }
    public InputStream getInputStream() {
        return inputStream;
    }
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getExtensionPiece() {
        return extensionPiece;
    }
    public void setExtensionPiece(String extensionPiece) {
        this.extensionPiece = extensionPiece;
    }
    public FileInputStream getValeur() {
        return valeur;
    }
    public String getSize() {
        return size;
    }
    public void setValeur(FileInputStream valeur) {
        this.valeur = valeur;
    }

    @Override
    public String toString() {
        return "PieceJointe{" +
                "idPieceJointe=" + idPieceJointe +
                ", description='" + description + '\'' +
                ", extensionPiece='" + extensionPiece + '\'' +
                ", size='" + size + '\''+
                '}';
    }
}
