package android.rs.ac.parking;

public class RacunModel{
    public static final String IME_TABELE = "racun";
    public static final String POLJE_RACUN_ID = "racun_id";
    public static final String POLJE_RACUN_GARAZA_ID = "garaza_id";
    public static final String POLJE_RACUN_VREME_POCETKA = "vreme_pocetka";
    public static final String POLJE_RACUN_VREME_KRAJA = "vreme_kraja";
    public static final String POLJE_RACUN_DATUM = "datum";
    public static final String POLJE_RACUN_KORISNIK_ID = "korisnik_id";
    public static final String POLJE_RACUN_CENA = "cena";


    private int racunId;
    private int garazaId;
    private int korisnikId;
    private String vremePocetka;
    private String vremeKraja;
    private String datum;
    private float cena;

    public RacunModel(int racunId, int garazaId, int korisnikId, String vremePocetka, String vremeKraja, String datum, float cena) {
        this.racunId = racunId;
        this.garazaId = garazaId;
        this.korisnikId = korisnikId;
        this.vremePocetka = vremePocetka;
        this.vremeKraja = vremeKraja;
        this.datum = datum;
        this.cena = cena;
    }

    public int getRacunId() {
        return racunId;
    }

    public int getGarazaId() {
        return garazaId;
    }

    public int getKorisnikId() {
        return korisnikId;
    }

    public String getVremePocetka() {
        return vremePocetka;
    }

    public String getVremeKraja() {
        return vremeKraja;
    }

    public String getDatum() {
        return datum;
    }

    public float getCena() {
        return cena;
    }

    @Override
    public String toString() {
        return "RacunModel{" +
                "racunId=" + racunId +
                ", garazaId=" + garazaId +
                ", korisnikId=" + korisnikId +
                ", vremePocetka='" + vremePocetka + '\'' +
                ", vremeKraja='" + vremeKraja + '\'' +
                ", datum='" + datum + '\'' +
                ", cena=" + cena +
                '}';
    }
}
