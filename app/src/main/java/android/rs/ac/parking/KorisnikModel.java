package android.rs.ac.parking;

public class KorisnikModel{
    public static final String IME_TABELE = "korisnik";
    public static final String POLJE_KORISNIK_ID = "korisnik_id";
    public static final String POLJE_KORISNIK_IME = "ime";
    public static final String POLJE_KORISNIK_PREZIME = "prezime";
    public static final String POLJE_KORISNIK_KORISNICKO_IME = "korisnicko_ime";
    public static final String POLJE_KORISNIK_LOZINKA = "lozinka";
    public static final String POLJE_KORISNIK_EMAIL= "email";
    public static final String POLJE_KORISNIK_STANJE= "stanje";

    private int korisnikId;
    private String ime;
    private String prezime;
    private String korisnickoIme;
    private String lozinka;
    private String email;
    private float stanje;

    public KorisnikModel(int korisnikId, String ime, String prezime, String korisnickoIme, String lozinka, String email, float stanje) {
        this.korisnikId = korisnikId;
        this.ime = ime;
        this.prezime = prezime;
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
        this.email = email;
        this.stanje = stanje;
    }

    public int getKorisnikId() {
        return korisnikId;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public String getEmail() {
        return email;
    }

    public float getStanje() {
        return stanje;
    }

    @Override
    public String toString() {
        return "KorisnikModel{" +
                "korisnikId=" + korisnikId +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", korisnickoIme='" + korisnickoIme + '\'' +
                ", lozinka='" + lozinka + '\'' +
                ", email='" + email + '\'' +
                ", stanje=" + stanje +
                '}';
    }
}
