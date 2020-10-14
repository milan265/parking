package android.rs.ac.parking;

public class GarazaModel {
    public static final String IME_TABELE = "garaza";
    public static final String POLJE_GARAZA_ID = "garaza_id";
    public static final String POLJE_GARAZA_NAZIV = "naziv";
    public static final String POLJE_GARAZA_ADRESA = "adresa";
    public static final String POLJE_GARAZA_BROJ_SLOBODNIH_MESTA = "broj_slobodnih_mesta";
    public static final String POLJE_GARAZA_KAPACITET = "kapacitet";
    public static final String POLJE_GARAZA_CENA = "cena";

    private int garazaId;
    private String naziv;
    private String adresa;
    private int brojSlobodnihMesta;
    private int kapacitet;
    private float cena;

    public GarazaModel(int garazaId, String naziv, String adresa, int brojSlobodnihMesta, int kapacitet, float cena) {
        this.garazaId = garazaId;
        this.naziv = naziv;
        this.adresa = adresa;
        this.brojSlobodnihMesta = brojSlobodnihMesta;
        this.kapacitet = kapacitet;
        this.cena = cena;
    }

    public int getGarazaId() {
        return garazaId;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public int getBrojSlobodnihMesta() {
        return brojSlobodnihMesta;
    }

    public int getKapacitet() {
        return kapacitet;
    }

    public float getCena() {
        return cena;
    }

    @Override
    public String toString() {
        return "GarazaModel{" +
                "garazaId=" + garazaId +
                ", naziv='" + naziv + '\'' +
                ", adresa='" + adresa + '\'' +
                ", brojSlobodnihMesta=" + brojSlobodnihMesta +
                ", kapacitet=" + kapacitet +
                ", cena=" + cena +
                '}';
    }
}
