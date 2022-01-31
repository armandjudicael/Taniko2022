package model.pojo.business.other;

public class Procedure {

    private  int id;
    private  int numero;
    private  String nom;

    public Procedure(int id, int numero, String nom) {
        this.id = id;
        this.numero = numero;
        this.nom = nom;
    }

    public Procedure(int numero, String nom) {
        this.numero = numero;
        this.nom = nom;
    }

    public Procedure(String nom) {
        this.nom = nom;
    }

    public Procedure() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

}
