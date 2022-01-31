package view.Model.ViewObject;

import model.pojo.business.other.Dossier;

public class FolderForView extends Dossier {
    public FolderForView() {
    }

    public FolderForView(ProcedureForTableview procedure) {
        this.procedure = procedure;
    }
    @Override public String toString() {
      return   super.toString();
    }
    private ProcedureForTableview procedure;
    public ProcedureForTableview getProcedureForTableView() {
        return procedure;
    }
    public void setProcedure(ProcedureForTableview procedure) {
        this.procedure = procedure;
    }
}
