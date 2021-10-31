package View.Model.ViewObject;

import Model.Pojo.business.Titre;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

public class TitleForView extends HBox {

    public TitleForView(Titre titre) {
        this.titre = titre;
        // numero du titre
        numero = new Label();
        numero.setText(titre.getNumero());
        numero.setFont(new Font("Microsoft JhengHei",16));
        numero.setPrefWidth(120);
        numero.setPrefHeight(30);
        numero.setAlignment(Pos.CENTER);
        numero.setWrapText(true);
        // nom du propriete
        nomPropriete = new Label();
        nomPropriete.setText(titre.getNomPropriete());
        nomPropriete.setFont(new Font("Microsoft JhengHei",16));
        nomPropriete.setPrefWidth(280);
        nomPropriete.setPrefHeight(30);
        nomPropriete.setAlignment(Pos.CENTER);
        nomPropriete.setWrapText(true);
        // nom et prenom du titulaire du titre
        nomEtPrenom = new Label();
//        nomEtPrenom.setText(titre.getNomTitulaire()+" "+titre.getTitulaire());
        nomEtPrenom.setFont(new Font("Microsoft JhengHei", 15));
        nomEtPrenom.setPrefWidth(400);
        nomPropriete.setPrefHeight(30);
        nomEtPrenom.setAlignment(Pos.CENTER);
        nomEtPrenom.setWrapText(true);
        // date d'attribution
        dateAttribution = new Label();
        dateAttribution.setText(titre.getDateCreation().toString());
        dateAttribution.setFont(new Font("Microsoft JhengHei", 15));
        dateAttribution.setPrefWidth(240);
        nomPropriete.setPrefHeight(30);
        dateAttribution.setAlignment(Pos.CENTER);
        dateAttribution.setWrapText(true);
        // ex-affaire
        exAffaire = new Label();
//        exAffaire.setText(titre.getAffair().getNumero());
        exAffaire.setFont(new Font("Microsoft JhengHei", 12));
        exAffaire.setPrefWidth(200);
        exAffaire.setAlignment(Pos.CENTER);
        exAffaire.setWrapText(true);
        exAffaire.setFocusTraversable(true);
        exAffaire.setOnMouseEntered(event -> {
            exAffaire.setUnderline(true);
        });
        exAffaire.setOnMouseExited(event -> {
            exAffaire.setUnderline(false);
        });

        exAffaire.setOnMouseClicked(event -> {
            // AffairViewController.getInstance().showDetails(titre.getAffair(), true);
        });

        this.setHgrow(nomPropriete,Priority.ALWAYS);
        this.setHgrow(nomEtPrenom,Priority.ALWAYS);
        this.setHgrow(dateAttribution,Priority.ALWAYS);
        this.setHgrow(exAffaire,Priority.ALWAYS);
        this.setHgrow(numero,Priority.ALWAYS);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(15);
        this.setCursor(Cursor.HAND);
        this.getChildren().addAll(numero, nomPropriete, nomEtPrenom, dateAttribution, exAffaire);
    }

    private Label dateAttribution;
    private Titre titre;
    private Label numero;
    private Label nomPropriete;
    private Label nomEtPrenom;
    private Label exAffaire;

    public Label getExAffaire() {
        return exAffaire;
    }
    public void setExAffaire(Label exAffaire) {
        this.exAffaire = exAffaire;
    }
    public Label getNumero() {
        return numero;
    }
    public void setNumero(Label numero) {
        this.numero = numero;
    }
    public Label getNomPropriete() {
        return nomPropriete;
    }
    public void setNomPropriete(Label nomPropriete) {
        this.nomPropriete = nomPropriete;
    }
    public Label getNomEtPrenom() {
        return nomEtPrenom;
    }
    public void setNomEtPrenom(Label nomEtPrenom) {
        this.nomEtPrenom = nomEtPrenom;
    }
    public Label getDateAttribution() {
        return dateAttribution;
    }
    public void setDateAttribution(Label dateAttribution) {
        this.dateAttribution = dateAttribution;
    }
    public Titre getTitre() {
        return titre;
    }
    public void setTitre(Titre titre) {
        this.titre = titre;
    }
}
