package dao;

import com.sun.jdi.request.ClassPrepareRequest;
import model.Enum.AttachementStatus;
import model.pojo.business.attachement.Attachement;
import model.pojo.business.other.Dossier;
import view.Model.ViewObject.AttachementForView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AttachementDao extends Dao implements DaoHelper<Attachement> {

    public AttachementDao(Connection connection) {
        super(connection);
    }

    @Override public int create(Attachement attachement){
        return 0;
    }
    public  int[] removeAll(ObservableList<Attachement> list){
        if (!list.isEmpty()){
            String query = " DELETE FROM piecejointe WHERE idPieceJointe = ? ";
            try(PreparedStatement ps = connection.prepareStatement(query)){
                list.forEach(pieceJointe -> {
                    try {
                        ps.setInt(1,pieceJointe.getIdPieceJointe());
                        ps.addBatch();
                    } catch (SQLException e) {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
                    }
                });
                int[] ints = ps.executeBatch();
                this.connection.commit();
                return ints;
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public int[] createAll(ObservableList<Attachement> list, Dossier dossier){
        if(!list.isEmpty()){
            String query ="INSERT INTO piecejointe(descriptionPiece,valeurPiece,extensionPiece,taillePiece,dossierId) VALUES(?,?,?,?,?);";
            try(PreparedStatement ps = connection.prepareStatement(query)){
                list.forEach(pieceJointe -> {
                    try {
                        ps.setString(1,pieceJointe.getDescription());
                        ps.setBlob(2,pieceJointe.getValeur());
                        ps.setString(3,pieceJointe.getExtensionPiece());
                        ps.setString(4,pieceJointe.getSize());
                        ps.setInt(5, dossier.getId());
                        ps.addBatch();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                int[] ints = ps.executeBatch();
                this.connection.commit();
                return ints;
            }catch (SQLException e){
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }

    public ObservableList<AttachementForView> getAllPieceJointe(Dossier dossier){
        ObservableList<AttachementForView> pieceJointeObservableList = FXCollections.observableArrayList();
        if (dossier !=null){
            String query ="SELECT * FROM piecejointe  pj " +
                    "INNER JOIN affaire a ON pj.affaireId = a.idAffaire " +
                    "WHERE idAffaire = ? ";
            try(PreparedStatement ps = connection.prepareStatement(query)){
                ps.setInt(1, dossier.getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next())
                    pieceJointeObservableList.add(new AttachementForView(createPieceJointe(rs),true));
            } catch (SQLException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return pieceJointeObservableList;
    }

    public ObservableList<Attachement> getAllAttachement(Dossier dossier){
        ObservableList<Attachement> attachementObservableList = FXCollections.observableArrayList();
        if (dossier !=null){
            String query ="SELECT * FROM piecejointe  pj " +
                    "INNER JOIN affaire a ON pj.affaireId = a.idAffaire " +
                    "WHERE idAffaire = ? ";
            try(PreparedStatement ps = connection.prepareStatement(query)){
                ps.setInt(1, dossier.getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next())
                    attachementObservableList.add(createPieceJointe(rs));
            } catch (SQLException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return attachementObservableList;
    }
    private Attachement createPieceJointe(ResultSet rs){
        try {
            String description = rs.getString("descriptionPiece");
            String extension = rs.getString("extensionPiece");
            String size = rs.getString("taillePiece");
            InputStream valeurPiece = rs.getBinaryStream("valeurPiece");
            int idPiece = rs.getInt("idPieceJointe");
            return new Attachement(idPiece,description,extension,size,valeurPiece);
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
      return null;
    }
    public InputStream getAttachementValue(Attachement attachement){
        String query = "SELECT valeurPiece FROM piecejointe WHERE idPieceJointe = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, attachement.getIdPieceJointe());
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                 return rs.getBinaryStream("valeurPiece");
        }catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
    @Override public int delete(Attachement attachement) {
        String query = " DELETE FROM piecejointe WHERE idPieceJointe = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, attachement.getIdPieceJointe());
            int i = ps.executeUpdate();
            this.connection.commit();
            return i;
        }catch (SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return 0;
    }
    @Override public int update(Attachement attachement) {
        return 0;
    }
    @Override public Attachement findById(int id) {
        return null;
    }
    @Override public Attachement finByNum(int num) {
        return null;
    }

}
