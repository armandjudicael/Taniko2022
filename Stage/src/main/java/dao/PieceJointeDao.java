package dao;

import Model.Pojo.Affaire;
import Model.Pojo.PieceJointe;
import View.Model.ViewObject.PieceJointeForView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PieceJointeDao extends Dao implements DaoHelper<PieceJointe> {

    public PieceJointeDao(Connection connection) {
        super(connection);
    }
    @Override public int create(PieceJointe pieceJointe){
        return 0;
    }
    public  int[] removeAll(ObservableList<PieceJointe> list){
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

    public int[] createAll(ObservableList<PieceJointe> list, Affaire affaire){
        if(!list.isEmpty()){
            String query ="INSERT INTO piecejointe(descriptionPiece,valeurPiece,extensionPiece,taillePiece,affaireId) VALUES(?,?,?,?,?);";
            try(PreparedStatement ps = connection.prepareStatement(query)){
                list.forEach(pieceJointe -> {
                    try {
                        ps.setString(1,pieceJointe.getDescription());
                        ps.setBlob(2,pieceJointe.getValeur());
                        ps.setString(3,pieceJointe.getExtensionPiece());
                        ps.setString(4,pieceJointe.getSize());
                        ps.setInt(5,affaire.getId());
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

    public ObservableList<PieceJointeForView> getAllPieceJointe(Affaire affaire){
        ObservableList<PieceJointeForView> pieceJointeObservableList = FXCollections.observableArrayList();
        if (affaire!=null){
            String query ="SELECT * FROM piecejointe  pj " +
                    "INNER JOIN affaire a ON pj.affaireId = a.idAffaire " +
                    "WHERE idAffaire = ? ";
            try(PreparedStatement ps = connection.prepareStatement(query)){
                ps.setInt(1,affaire.getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next())
                    pieceJointeObservableList.add(new PieceJointeForView(createPieceJointe(rs),true));
            } catch (SQLException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return pieceJointeObservableList;
    }

    public ObservableList<PieceJointe> getAllAttachement(Affaire affaire){
        ObservableList<PieceJointe> pieceJointeObservableList = FXCollections.observableArrayList();
        if (affaire!=null){
            String query ="SELECT * FROM piecejointe  pj " +
                    "INNER JOIN affaire a ON pj.affaireId = a.idAffaire " +
                    "WHERE idAffaire = ? ";
            try(PreparedStatement ps = connection.prepareStatement(query)){
                ps.setInt(1,affaire.getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next())
                    pieceJointeObservableList.add(createPieceJointe(rs));
            } catch (SQLException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return pieceJointeObservableList;
    }


    private PieceJointe createPieceJointe(ResultSet rs){
        try {
            String description = rs.getString("descriptionPiece");
            String extension = rs.getString("extensionPiece");
            String size = rs.getString("taillePiece");
            InputStream valeurPiece = rs.getBinaryStream("valeurPiece");
            int idPiece = rs.getInt("idPieceJointe");
            return new PieceJointe(idPiece,description,extension,size,valeurPiece);
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
      return null;
    }

    public InputStream getAttachementValue(PieceJointe pieceJointe){
        String query = "SELECT valeurPiece FROM piecejointe WHERE idPieceJointe = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1,pieceJointe.getIdPieceJointe());
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                 return rs.getBinaryStream("valeurPiece");
        }catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override public int delete(PieceJointe pieceJointe) {
        String query = " DELETE FROM piecejointe WHERE idPieceJointe = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1,pieceJointe.getIdPieceJointe());
            int i = ps.executeUpdate();
            this.connection.commit();
            return i;
        }catch (SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return 0;
    }
    @Override public int update(PieceJointe pieceJointe) {
        return 0;
    }
    @Override public PieceJointe findById(int id) {
        return null;
    }
    @Override public PieceJointe finByNum(int num) {
        return null;
    }
}
