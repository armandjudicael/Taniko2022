package View.Model;

import Model.Enum.AffaireStatus;
import Model.Enum.Observation;
import Model.Enum.TypeDemande;
import Model.Pojo.*;
import Model.other.ProcedureForTableview;

import java.sql.Timestamp;

public class AffaireForView extends Affaire{
    private ProcedureForTableview procedure;
    public AffaireForView(int idAffaire,
                          String numero,
                          Timestamp dateDeFormulation,
                          TypeDemande typeDemande,
                          User redacteur,
                          AffaireStatus status,
                          Demandeur demandeur,
                          Terrain terrain,
                          ProcedureForTableview procedure){
        super(idAffaire, numero, dateDeFormulation, typeDemande,redacteur, status, demandeur, terrain);
        this.procedure = procedure;

    }

    public AffaireForView(String numero,
                          Timestamp dateDeFormulation,
                          TypeDemande typeDemande,
                          User redacteur,
                          AffaireStatus status,
                          Demandeur demandeur,
                          Terrain terrain,
                          ProcedureForTableview procedure) {
        super(numero, dateDeFormulation, typeDemande, redacteur, status, demandeur, terrain);
        this.procedure = procedure;
    }

    public ProcedureForTableview getProcedure() {
        return procedure;
    }
    public void setProcedure(ProcedureForTableview procedure) {
        this.procedure = procedure;
    }
}
