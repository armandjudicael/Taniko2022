package Model.Pojo;

public class Superficie{

    private int idSuperficie;
    private float hectare;
    private float are;
    private float centiAre;

    public Superficie() {

    }

    public Superficie(int idSuperficie, float hectare, float are, float centiAre){
        this.idSuperficie = idSuperficie;
        this.hectare = hectare;
        this.are = are;
        this.centiAre = centiAre;
    }

    public Superficie(float hectare, float are, float centiAre) {
        this.hectare = hectare;
        this.are = are;
        this.centiAre = centiAre;
    }

    public int getIdSuperficie() {
        return idSuperficie;
    }
    public void setIdSuperficie(int idSuperficie) {
        this.idSuperficie = idSuperficie;
    }
    public float getHectare() {
        return hectare;
    }
    public void setHectare(float hectare) {
        this.hectare = hectare;
    }
    public float getAre() {
        return are;
    }
    public void setAre(float are) {
        this.are = are;
    }
    public float getCentiAre() {
        return centiAre;
    }

    public void setCentiAre(float centiAre) {
        this.centiAre = centiAre;
    }
    @Override public String toString() {
        String superficie = "";
        if (centiAre == 0.0) {
            if (are == 0.0) {
                if (0.0!=hectare)
                    superficie = hectare+ " Ha";
            } else if (0.0!=are) {
                if (0.0!=hectare)
                    superficie = hectare + " Ha-" + are+ " A";
                else superficie = are + " A";
            }
        } else if (are == 0.0) {
            if (centiAre == 0.0) {
                if (0.0!=hectare)
                    superficie = hectare+ " Ha";
            } else if (0.0!=centiAre) {
                if (0.0!=hectare)
                    superficie = hectare + " Ha-" + centiAre + " Ca";
                else superficie = centiAre + " Ca";
            }
        } else if (0.0==hectare) {
            if (centiAre==0.0) {
                if (0.0!=are)
                    superficie = are + " A";
            } else if (0.0!=centiAre) {
                if (0.0!=are)
                    superficie = are + " A-" + centiAre + " Ca";
                else superficie = centiAre + " Ca";
            }
        } else if (0.0!=hectare && 0.0!=are && 0.0!=centiAre)
            superficie = hectare + " Ha-" + are + " A-" + centiAre+ " Ca";

return superficie;
    }
}
