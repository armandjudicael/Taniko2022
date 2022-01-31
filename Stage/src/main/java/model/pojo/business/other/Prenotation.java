package model.pojo.business.other;
import java.util.List;
public class Prenotation extends ProprieteOperation{
    private int idPrenotation;
    private String libele;
    private List<PersonnePhysique> personList;
    public Prenotation(String numBorderau, String numDepot, String numVolume, Double montant, int idPrenotation, String libele) {
        super(numBorderau, numDepot, numVolume, montant);
        this.idPrenotation = idPrenotation;
        this.libele = libele;
    }
    public Prenotation(String numBorderau, String numDepot, String numVolume, Double montant) {
        super(numBorderau, numDepot, numVolume, montant);
    }
    public int getIdPrenotation() {
        return idPrenotation;
    }
    public void setIdPrenotation(int idPrenotation) {
        this.idPrenotation = idPrenotation;
    }
    public String getLibele() {
        return libele;
    }
    public void setLibele(String libele) {
        this.libele = libele;
    }
}
