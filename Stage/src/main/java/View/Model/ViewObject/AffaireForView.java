package View.Model.ViewObject;

import Model.Enum.AffaireStatus;
import Model.Enum.TypeDemande;
import Model.Pojo.*;

import java.sql.Timestamp;

public class AffaireForView extends Affaire{

    public AffaireForView(ProcedureForTableview procedure) {
        this.procedure = procedure;
    }
    public AffaireForView(int id,
                          String numero,
                          Timestamp dateDeFormulation,
                          TypeDemande typeDemande,
                          User redacteur,
                          AffaireStatus status,
                          PersonnePhysique demandeurPhysique,
                          Terrain terrain,
                          ProcedureForTableview procedure) {
        super(id,numero,dateDeFormulation,typeDemande,redacteur,status,demandeurPhysique, terrain);
        this.procedure = procedure;
    }

    public AffaireForView(String numero,
                          Timestamp dateDeFormulation,
                          TypeDemande typeDemande,
                          User redacteur,
                          AffaireStatus status,
                          PersonnePhysique demandeurPhysique,
                          Terrain terrain,
                          ProcedureForTableview procedure) {
        super(numero, dateDeFormulation, typeDemande, redacteur, status, demandeurPhysique, terrain);
        this.procedure = procedure;
    }

    public AffaireForView(int id,
                          String numero,
                          Timestamp dateDeFormulation,
                          TypeDemande typeDemande,
                          User redacteur,
                          AffaireStatus status,
                          PersonneMorale demandeurMorale,
                          Terrain terrain,
                          ProcedureForTableview procedure) {
        super(id, numero, dateDeFormulation, typeDemande, redacteur, status, demandeurMorale, terrain);
        this.procedure = procedure;
    }

    public AffaireForView(String numero,
                          Timestamp dateDeFormulation,
                          TypeDemande typeDemande,
                          User redacteur,
                          AffaireStatus status,
                          PersonneMorale demandeurMorale,
                          Terrain terrain,
                          ProcedureForTableview procedure) {
        super(numero, dateDeFormulation, typeDemande, redacteur, status, demandeurMorale, terrain);
        this.procedure = procedure;
    }

    private ProcedureForTableview procedure;
    public ProcedureForTableview getProcedureForTableView() {
        return procedure;
    }
    public void setProcedure(ProcedureForTableview procedure) {
        this.procedure = procedure;
    }
}
