package view.Model.ViewObject;

import model.Enum.AffaireStatus;
import model.Enum.TypeDemande;
import model.pojo.business.*;

import java.sql.Date;

public class AffaireForView extends Affaire{
    public AffaireForView(ProcedureForTableview procedure) {
        this.procedure = procedure;
    }
    public AffaireForView(int id,
                          String numero,
                          Date dateDeFormulation,
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
                          Date dateDeFormulation,
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
                          Date dateDeFormulation,
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
                          Date dateDeFormulation,
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
