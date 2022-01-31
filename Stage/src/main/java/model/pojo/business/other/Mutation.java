package model.pojo.business.other;
import model.Enum.TypeMutation;

public class Mutation extends ProprieteOperation{
    private int idMutation;
    private String nomMutation;
    private TypeMutation typeMutation;
    public Mutation(String numBorderau, String numDepot, String numVolume, Double montant, int idMutation, String nomMutation, TypeMutation typeMutation) {
        super(numBorderau, numDepot, numVolume, montant);
        this.idMutation = idMutation;
        this.nomMutation = nomMutation;
        this.typeMutation = typeMutation;
    }
    public Mutation(String numBorderau, String numDepot, String numVolume, Double montant, Dossier dossier, int idMutation, String nomMutation, TypeMutation typeMutation) {
        super(numBorderau, numDepot, numVolume, montant, dossier);
        this.idMutation = idMutation;
        this.nomMutation = nomMutation;
        this.typeMutation = typeMutation;
    }
    public Mutation(String numBorderau, String numDepot, String numVolume, Double montant){
        super(numBorderau, numDepot, numVolume, montant);
    }
    public Mutation(String numBorderau, String numDepot, String numVolume, Double montant, Dossier dossier) {
        super(numBorderau, numDepot, numVolume, montant, dossier);
    }

    public int getIdMutation() {
        return idMutation;
    }
    public void setIdMutation(int idMutation) {
        this.idMutation = idMutation;
    }
    public String getNomMutation() {
        return nomMutation;
    }
    public void setNomMutation(String nomMutation) {
        this.nomMutation = nomMutation;
    }
}
