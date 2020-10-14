package android.rs.ac.parking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Baza extends SQLiteOpenHelper {

    private static final String BAZA_FILENAME = "baza-parking";

    public Baza(Context context) {
        super(context, BAZA_FILENAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s FLOAT)", KorisnikModel.IME_TABELE, KorisnikModel.POLJE_KORISNIK_ID,
                KorisnikModel.POLJE_KORISNIK_IME, KorisnikModel.POLJE_KORISNIK_PREZIME, KorisnikModel.POLJE_KORISNIK_KORISNICKO_IME,
                KorisnikModel.POLJE_KORISNIK_LOZINKA, KorisnikModel.POLJE_KORISNIK_EMAIL, KorisnikModel.POLJE_KORISNIK_STANJE));
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT, %s TEXT, %s TEXT, %s FLOAT, %s INTEGER, %s INTEGER," +
                        "FOREIGN KEY(%s) REFERENCES %s (%s), " +
                        "FOREIGN KEY(%s) REFERENCES %s (%s))", RacunModel.IME_TABELE, RacunModel.POLJE_RACUN_ID,
                RacunModel.POLJE_RACUN_VREME_POCETKA, RacunModel.POLJE_RACUN_VREME_KRAJA, RacunModel.POLJE_RACUN_DATUM,
                RacunModel.POLJE_RACUN_CENA, RacunModel.POLJE_RACUN_KORISNIK_ID, RacunModel.POLJE_RACUN_GARAZA_ID,
                RacunModel.POLJE_RACUN_KORISNIK_ID, KorisnikModel.IME_TABELE, KorisnikModel.POLJE_KORISNIK_ID,
                RacunModel.POLJE_RACUN_GARAZA_ID, GarazaModel.IME_TABELE, GarazaModel.POLJE_GARAZA_ID));
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT, %s TEXT, %s INTEGER, %s INTEGER, %s FLOAT)", GarazaModel.IME_TABELE, GarazaModel.POLJE_GARAZA_ID,
                GarazaModel.POLJE_GARAZA_NAZIV, GarazaModel.POLJE_GARAZA_ADRESA, GarazaModel.POLJE_GARAZA_BROJ_SLOBODNIH_MESTA,
                GarazaModel.POLJE_GARAZA_KAPACITET, GarazaModel.POLJE_GARAZA_CENA));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s",KorisnikModel.IME_TABELE));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s",RacunModel.IME_TABELE));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s",GarazaModel.IME_TABELE));
        onCreate(db);
    }

    public void dodajKorisnika(String ime, String prezime, String korisnickoIme, String lozinka, String email, float stanje){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KorisnikModel.POLJE_KORISNIK_IME, ime);
        cv.put(KorisnikModel.POLJE_KORISNIK_PREZIME, prezime);
        cv.put(KorisnikModel.POLJE_KORISNIK_KORISNICKO_IME, korisnickoIme);
        cv.put(KorisnikModel.POLJE_KORISNIK_LOZINKA, lozinka);
        cv.put(KorisnikModel.POLJE_KORISNIK_EMAIL, email);
        cv.put(KorisnikModel.POLJE_KORISNIK_STANJE, stanje);
        db.insert(KorisnikModel.IME_TABELE, null, cv);
    }

    public KorisnikModel vratiKorisnikModelPoId(int korisnikId){
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL = String.format("SELECT * FROM %s WHERE %s=%d", KorisnikModel.IME_TABELE, KorisnikModel.POLJE_KORISNIK_ID, korisnikId);
        Cursor rezultat = db.rawQuery(SQL, null);
        if(rezultat.moveToFirst()){
            String ime = rezultat.getString(rezultat.getColumnIndex(KorisnikModel.POLJE_KORISNIK_IME));
            String prezime = rezultat.getString(rezultat.getColumnIndex(KorisnikModel.POLJE_KORISNIK_PREZIME));
            String korisnickoIme = rezultat.getString(rezultat.getColumnIndex(KorisnikModel.POLJE_KORISNIK_KORISNICKO_IME));
            String lozinka = rezultat.getString(rezultat.getColumnIndex(KorisnikModel.POLJE_KORISNIK_LOZINKA));
            String email = rezultat.getString(rezultat.getColumnIndex(KorisnikModel.POLJE_KORISNIK_EMAIL));
            float stanje = rezultat.getFloat(rezultat.getColumnIndex(KorisnikModel.POLJE_KORISNIK_STANJE));
            return new KorisnikModel(korisnikId, ime, prezime, korisnickoIme, lozinka, email, stanje);
        }else{
            return null;
        }
    }
    public int vratiKorisnikModelPoKorisnickoImeLozinka(String korisnickoIme, String lozinka){
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL = String.format("SELECT * FROM %s WHERE %s='%s' AND %s='%s'", KorisnikModel.IME_TABELE, KorisnikModel.POLJE_KORISNIK_KORISNICKO_IME,
                korisnickoIme, KorisnikModel.POLJE_KORISNIK_LOZINKA, lozinka);
        Cursor rezultat = db.rawQuery(SQL, null);
        if(rezultat.moveToFirst()){
            return rezultat.getInt(rezultat.getColumnIndex(KorisnikModel.POLJE_KORISNIK_ID));
        }else{
            return -1;
        }
    }

    public List<KorisnikModel> vratiSveKorisnikModel(){
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL = String.format("SELECT * FROM %s", KorisnikModel.IME_TABELE);
        Cursor rezultat = db.rawQuery(SQL, null);
        rezultat.moveToFirst();
        List<KorisnikModel> lista = new ArrayList<>(rezultat.getCount());
        while(rezultat.isAfterLast() == false){
            int korisnikId = rezultat.getInt(rezultat.getColumnIndex(KorisnikModel.POLJE_KORISNIK_ID));
            String ime = rezultat.getString(rezultat.getColumnIndex(KorisnikModel.POLJE_KORISNIK_IME));
            String prezime = rezultat.getString(rezultat.getColumnIndex(KorisnikModel.POLJE_KORISNIK_PREZIME));
            String lozinka = rezultat.getString(rezultat.getColumnIndex(KorisnikModel.POLJE_KORISNIK_LOZINKA));
            String korisnickoIme = rezultat.getString(rezultat.getColumnIndex(KorisnikModel.POLJE_KORISNIK_KORISNICKO_IME));
            String email = rezultat.getString(rezultat.getColumnIndex(KorisnikModel.POLJE_KORISNIK_EMAIL));
            float stanje = rezultat.getFloat(rezultat.getColumnIndex(KorisnikModel.POLJE_KORISNIK_STANJE));
            lista.add(new KorisnikModel(korisnikId, ime, prezime, korisnickoIme, lozinka, email, stanje));
            rezultat.moveToNext();
        }
        return lista;
    }
    public void izmeniKorisnikModel(int korisnikId, String ime, String prezime, String korisnickoIme, String lozinka, String email, float stanje){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KorisnikModel.POLJE_KORISNIK_IME, ime);
        cv.put(KorisnikModel.POLJE_KORISNIK_PREZIME, prezime);
        cv.put(KorisnikModel.POLJE_KORISNIK_KORISNICKO_IME, korisnickoIme);
        cv.put(KorisnikModel.POLJE_KORISNIK_LOZINKA, lozinka);
        cv.put(KorisnikModel.POLJE_KORISNIK_EMAIL, email);
        cv.put(KorisnikModel.POLJE_KORISNIK_STANJE, stanje);
        db.update(KorisnikModel.IME_TABELE, cv, KorisnikModel.POLJE_KORISNIK_ID + " = ?", new String[]{String.valueOf(korisnikId)});
    }

    public void dodajRacun(String vremePocetka, String vremeKraja, String datum, float cena, int garazaId, int korisnikId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(RacunModel.POLJE_RACUN_VREME_POCETKA, vremePocetka);
        cv.put(RacunModel.POLJE_RACUN_VREME_KRAJA, vremeKraja);
        cv.put(RacunModel.POLJE_RACUN_DATUM, datum);
        cv.put(RacunModel.POLJE_RACUN_CENA, cena);
        cv.put(RacunModel.POLJE_RACUN_GARAZA_ID, garazaId);
        cv.put(RacunModel.POLJE_RACUN_KORISNIK_ID, korisnikId);
        db.insert(RacunModel.IME_TABELE, null, cv);
    }

    public RacunModel vratiRacunModelPoId(int racunId){
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL = String.format("SELECT * FROM %s WHERE %s=%d", RacunModel.IME_TABELE, RacunModel.POLJE_RACUN_ID, racunId);
        Cursor rezultat = db.rawQuery(SQL, null);
        if(rezultat.moveToFirst()){
            String vremePocetka = rezultat.getString(rezultat.getColumnIndex(RacunModel.POLJE_RACUN_VREME_POCETKA));
            String vremeKraja = rezultat.getString(rezultat.getColumnIndex(RacunModel.POLJE_RACUN_VREME_KRAJA));
            String datum = rezultat.getString(rezultat.getColumnIndex(RacunModel.POLJE_RACUN_DATUM));
            float cena = rezultat.getFloat(rezultat.getColumnIndex(RacunModel.POLJE_RACUN_CENA));
            int garazaId = rezultat.getInt(rezultat.getColumnIndex(RacunModel.POLJE_RACUN_GARAZA_ID));
            int korisnikId = rezultat.getInt(rezultat.getColumnIndex(RacunModel.POLJE_RACUN_KORISNIK_ID));
            return new RacunModel(racunId, garazaId, korisnikId, vremePocetka, vremeKraja, datum, cena);
        }else{
            return null;
        }
    }

    public List<RacunModel> vratiSveRacunModel(int korisnikId){
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL = String.format("SELECT * FROM %s WHERE %s=%d", RacunModel.IME_TABELE, RacunModel.POLJE_RACUN_KORISNIK_ID, korisnikId);
        Cursor rezultat = db.rawQuery(SQL, null);
        rezultat.moveToFirst();
        List<RacunModel> lista = new ArrayList<>(rezultat.getCount());
        while(rezultat.isAfterLast() == false){
            int racunId = rezultat.getInt(rezultat.getColumnIndex(RacunModel.POLJE_RACUN_ID));
            int garazaId = rezultat.getInt(rezultat.getColumnIndex(RacunModel.POLJE_RACUN_GARAZA_ID));
            String vremePocetka = rezultat.getString(rezultat.getColumnIndex(RacunModel.POLJE_RACUN_VREME_POCETKA));
            String vremeKraja = rezultat.getString(rezultat.getColumnIndex(RacunModel.POLJE_RACUN_VREME_KRAJA));
            String datum = rezultat.getString(rezultat.getColumnIndex(RacunModel.POLJE_RACUN_DATUM));
            float cena = rezultat.getFloat(rezultat.getColumnIndex(RacunModel.POLJE_RACUN_CENA));
            lista.add(new RacunModel(racunId, garazaId, korisnikId, vremePocetka, vremeKraja, datum, cena));
            rezultat.moveToNext();
        }
        return lista;
    }

    public void dodajGarazu(String naziv, String adresa, int brojSlobodnihMesta, int kapacitet, float cena){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(GarazaModel.POLJE_GARAZA_NAZIV, naziv);
        cv.put(GarazaModel.POLJE_GARAZA_ADRESA, adresa);
        cv.put(GarazaModel.POLJE_GARAZA_BROJ_SLOBODNIH_MESTA, brojSlobodnihMesta);
        cv.put(GarazaModel.POLJE_GARAZA_KAPACITET, kapacitet);
        cv.put(GarazaModel.POLJE_GARAZA_CENA, cena);
        db.insert(GarazaModel.IME_TABELE, null, cv);
    }

    public GarazaModel vratiGarazaModelPoId(int garazaId){
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL = String.format("SELECT * FROM %s WHERE %s=%d", GarazaModel.IME_TABELE, GarazaModel.POLJE_GARAZA_ID, garazaId);
        Cursor rezultat = db.rawQuery(SQL, null);
        if(rezultat.moveToFirst()){
            String naziv = rezultat.getString(rezultat.getColumnIndex(GarazaModel.POLJE_GARAZA_NAZIV));
            String adresa = rezultat.getString(rezultat.getColumnIndex(GarazaModel.POLJE_GARAZA_ADRESA));
            int brojSlobodnihMesta = rezultat.getInt(rezultat.getColumnIndex(GarazaModel.POLJE_GARAZA_BROJ_SLOBODNIH_MESTA));
            int kapacitet = rezultat.getInt(rezultat.getColumnIndex(GarazaModel.POLJE_GARAZA_KAPACITET));
            float cena = rezultat.getFloat(rezultat.getColumnIndex(GarazaModel.POLJE_GARAZA_CENA));
            return new GarazaModel(garazaId, naziv, adresa, brojSlobodnihMesta, kapacitet, cena);
        }else{
            return null;
        }
    }

    public GarazaModel vratiGarazaModelPoNazivu(String naziv){
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL = String.format("SELECT * FROM %s WHERE %s='%s'", GarazaModel.IME_TABELE, GarazaModel.POLJE_GARAZA_NAZIV, naziv);
        Cursor rezultat = db.rawQuery(SQL, null);
        if(rezultat.moveToFirst()){
            int garazaId = rezultat.getInt(rezultat.getColumnIndex(GarazaModel.POLJE_GARAZA_ID));
            String adresa = rezultat.getString(rezultat.getColumnIndex(GarazaModel.POLJE_GARAZA_ADRESA));
            int brojSlobodnihMesta = rezultat.getInt(rezultat.getColumnIndex(GarazaModel.POLJE_GARAZA_BROJ_SLOBODNIH_MESTA));
            int kapacitet = rezultat.getInt(rezultat.getColumnIndex(GarazaModel.POLJE_GARAZA_KAPACITET));
            float cena = rezultat.getFloat(rezultat.getColumnIndex(GarazaModel.POLJE_GARAZA_CENA));
            return new GarazaModel(garazaId, naziv, adresa, brojSlobodnihMesta, kapacitet, cena);
        }else{
            return null;
        }
    }

    public List<GarazaModel> vratiSveGarazaModel(){
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL = String.format("SELECT * FROM %s", GarazaModel.IME_TABELE);
        Cursor rezultat = db.rawQuery(SQL, null);
        rezultat.moveToFirst();
        List<GarazaModel> lista = new ArrayList<>(rezultat.getCount());
        while(rezultat.isAfterLast() == false){
            int garazaId = rezultat.getInt(rezultat.getColumnIndex(GarazaModel.POLJE_GARAZA_ID));
            String naziv = rezultat.getString(rezultat.getColumnIndex(GarazaModel.POLJE_GARAZA_NAZIV));
            String adresa = rezultat.getString(rezultat.getColumnIndex(GarazaModel.POLJE_GARAZA_ADRESA));
            int brojSlobodnihMesta = rezultat.getInt(rezultat.getColumnIndex(GarazaModel.POLJE_GARAZA_BROJ_SLOBODNIH_MESTA));
            int kapacitet = rezultat.getInt(rezultat.getColumnIndex(GarazaModel.POLJE_GARAZA_KAPACITET));
            float cena = rezultat.getFloat(rezultat.getColumnIndex(GarazaModel.POLJE_GARAZA_CENA));
            lista.add(new GarazaModel(garazaId, naziv, adresa, brojSlobodnihMesta, kapacitet, cena));
            rezultat.moveToNext();
        }
        return lista;
    }

    public void izmeniGarazaModel(int garazaId, String naziv, String adresa, int brojSlobodnihMesta, int kapacitet, float cena){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(GarazaModel.POLJE_GARAZA_NAZIV, naziv);
        cv.put(GarazaModel.POLJE_GARAZA_ADRESA, adresa);
        cv.put(GarazaModel.POLJE_GARAZA_BROJ_SLOBODNIH_MESTA, brojSlobodnihMesta);
        cv.put(GarazaModel.POLJE_GARAZA_KAPACITET, kapacitet);
        cv.put(GarazaModel.POLJE_GARAZA_CENA, cena);
        db.update(GarazaModel.IME_TABELE, cv, GarazaModel.POLJE_GARAZA_ID + " = ?", new String[]{String.valueOf(garazaId)});
    }

    public void obrisiBazu(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("DELETE FROM '%s'",KorisnikModel.IME_TABELE));
        db.execSQL(String.format("DELETE FROM '%s'",RacunModel.IME_TABELE));
        db.execSQL(String.format("DELETE FROM '%s'",GarazaModel.IME_TABELE));
    }
}
