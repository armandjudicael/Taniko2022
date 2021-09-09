package Model.Pojo;

public class Terrain {

    private int idTerrain;
    private String region;
    private String district;
    private String commune;
    private String parcelle;
    private String quartier;
    private String superficie;
    private Titre titreDependant;

    @Override
    public String toString() {
        return "Terrain{" +
                "idTerrain=" + idTerrain +
                ", region='" + region + '\'' +
                ", district='" + district + '\'' +
                ", commune='" + commune + '\'' +
                ", parcelle='" + parcelle + '\'' +
                ", quartier='" + quartier + '\'' +
                ", superficie='" + superficie + '\'' +
                ", titreDependant=" + titreDependant +
                '}';
    }

    public Terrain() {
    }

    public Terrain(int idTerrain,
                   String region,
                   String district,
                   String commune,
                   String parcelle,
                   String quartier,
                   String superficie){
       this.superficie = superficie;
        this.idTerrain = idTerrain;
        this.region = region;
        this.district = district;
        this.commune = commune;
        this.parcelle = parcelle;
        this.quartier = quartier;
    }

    public Terrain(String region,
                   String district,
                   String commune,
                   String parcelle,
                   String quartier,
                   String superficie) {
        this.region = region;
        this.district = district;
        this.commune = commune;
        this.parcelle = parcelle;
        this.quartier = quartier;
        this.superficie = superficie;
    }

    public Terrain(int idTerrain,
                   String region,
                   String district,
                   String commune,
                   String parcelle,
                   String quartier,
                   String superficie,
                   Titre titreDependant) {
        this.superficie = superficie;
        this.idTerrain = idTerrain;
        this.region = region;
        this.district = district;
        this.commune = commune;
        this.parcelle = parcelle;
        this.quartier = quartier;
        this.titreDependant = titreDependant;
    }

    public String getSuperficie() {
        return superficie;
    }

    public void setSuperficie(String superficie) {
        this.superficie = superficie;
    }

    public int getIdTerrain() {
        return idTerrain;
    }

    public void setIdTerrain(int idTerrain) {
        this.idTerrain = idTerrain;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getParcelle() {
        return parcelle;
    }

    public void setParcelle(String parcelle) {
        this.parcelle = parcelle;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public Titre getTitreDependant() {
        return titreDependant;
    }

    public void setTitreDependant(Titre titreDependant) {
        this.titreDependant = titreDependant;
    }
}
