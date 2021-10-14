package Model.Pojo;

import DAO.DaoFactory;
import Main.Main;
import javafx.application.HostServices;
import org.apache.commons.io.FileUtils;
import java.io.*;

public class PieceJointe {

    public PieceJointe(int idPieceJointe,
                       String description,
                       String extensionPiece,
                       String size,
                       InputStream inputStream) {
        this.idPieceJointe = idPieceJointe;
        this.description = description;
        this.extensionPiece = extensionPiece;
        this.size = size;
        this.inputStream = inputStream;
    }

    public PieceJointe(File selectedFile){
        try {
            String name = selectedFile.getName();
            String[] split = name.split("\\.");
            String description = split[0];
            String extension = split[1];
            this.setDescription(description);
            this.setExtensionPiece(extension);
            this.valeur = new FileInputStream(selectedFile);
            this.file = selectedFile;
            this.size = calculateFileSize(selectedFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String calculateFileSize(File file){
        long length = FileUtils.sizeOf(file);
        long kilo = length / FileUtils.ONE_KB;
        if (kilo<FileUtils.ONE_KB) return kilo+" Kb";
        return  kilo/FileUtils.ONE_MB+" Mb";
    }

    public void download() {
        if (this.getFile()==null){
            try {
                InputStream valeur = this.getInputStream();
                if (valeur!=null){
                    File downDirectory = new File(getDownloadDirectory());
                    if (!downDirectory.exists()) downDirectory.mkdir();
                    this.file = new File(getDownloadDirectory()+"/"+this.getDescription()+"."+this.getExtensionPiece());
                    this.file.setReadOnly();
                    if (!this.file.exists()) FileUtils.copyInputStreamToFile(valeur,this.file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void visualize(){
        this.download();
        HostServices hostServices = Main.getMainApplication().getHostServices();
        hostServices.showDocument(getFile().toURI().toString());
    }

    public int delete(){
       return DaoFactory.getPieceJointeDao().delete(this);
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
    @Override public String toString() {
        return "PieceJointe{" +
                "idPieceJointe=" + idPieceJointe +
                ", description='" + description + '\'' +
                ", extensionPiece='" + extensionPiece + '\'' +
                ", size='" + size + '\''+
                '}';
    }
    public File getFile() {
        return file;
    }
    public static String getDownloadDirectory() {
        return DOWNLOAD_DIRECTORY;
    }
    private int idPieceJointe;
    private String description;
    private String extensionPiece;
    private String size;
    private FileInputStream valeur;
    private InputStream inputStream;
    private File file;
    private static String DOWNLOAD_DIRECTORY;
    static {
        String userName = System.getProperty("user.name");
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Mac OS")){
        }else if (osName.startsWith("Windows")){
            DOWNLOAD_DIRECTORY = "C:\\Users\\"+userName+"\\Documents\\Taniko";
        }else {
            DOWNLOAD_DIRECTORY = "C:\\Users\\"+userName+"\\Documents\\Taniko";
        }
    }
}
