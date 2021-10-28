package Model.Pojo;

import dao.DaoFactory;
import Main.Main;
import javafx.application.HostServices;
import org.apache.commons.io.FileUtils;
import java.io.*;
import java.text.DecimalFormat;

public class PieceJointe {
    public PieceJointe() {}
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
            String extension = split[1].toLowerCase();
            this.setDescription(description);
            this.setExtensionPiece(extension);
            this.valeur = new FileInputStream(selectedFile);
            this.file = selectedFile;
            this.size = calculateFileSize(selectedFile);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public String calculateFileSize(File file) {
        double length = FileUtils.sizeOf(file);
        double kilo = length / FileUtils.ONE_KB;
        double mega = length / FileUtils.ONE_MB;
        if (kilo<FileUtils.ONE_KB){
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(kilo)+" Kb";
        }
        if(mega<FileUtils.ONE_MB){
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(mega)+" Mb";
        }
        return  "";
    }

    public void download() {
        try {
            this.file = new File(getDownloadDirectory()+"/"+this.getDescription()+"."+this.getExtensionPiece());
            if (!file.exists()) FileUtils.copyInputStreamToFile(DaoFactory.getPieceJointeDao().getAttachementValue(this), this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void visualize(){
        if (getFile()==null)
               this.download();
        HostServices hostServices = Main.getMainApplication().getHostServices();
        hostServices.showDocument(getFile().toURI().toString());
    }

    private static void initDownloadDirectory(){
        if (downDirectory ==null){
            String userName = System.getProperty("user.name");
            String osName = System.getProperty("os.name");
            if (osName.startsWith("Mac OS")){

            }else if (osName.startsWith("Windows")){
                  DOWNLOAD_DIRECTORY = "C:\\Users\\"+userName+"\\Documents\\Taniko";
            }else DOWNLOAD_DIRECTORY = "/home/"+userName+"/Documents/Taniko";
            downDirectory = new File(DOWNLOAD_DIRECTORY);
        }
        if (!downDirectory.exists()) downDirectory.mkdir();
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
    public static File getDownloadPathDirectory() {
        return downDirectory;
    }
    private int idPieceJointe;
    private String description;
    private String extensionPiece;
    private String size;
    private FileInputStream valeur;
    private InputStream inputStream;
    private File file;
    private static String DOWNLOAD_DIRECTORY;
    private static File downDirectory = null;
    static {
        initDownloadDirectory();
    }
}
