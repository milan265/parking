package android.rs.ac.parking;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class KodActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String SHARED_PREFERENCES_KOD = "ParkingSharedPreferencesKod";
    public final static String SHARED_PREFERENCES_STANJE = "stanje";
    public final static String SHARED_PREFERENCES_DATUM1 = "datum1";

    private final int garazaId = 4;

    private ImageView imageViewQR;
    private Button buttonQR;
    private String vreme1;
    private String datum2;
    private String vreme2;
    private SimpleDateFormat sdf;
    private int korisnikId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kod);

        initComponents();
    }

    private void initComponents(){
        imageViewQR = (ImageView) findViewById(R.id.imageViewQR);
        buttonQR = (Button) findViewById(R.id.buttonQR);

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCES_PREFIX, 0);
        korisnikId =  sharedPreferences.getInt(MainActivity.SHARED_PREFERENCES_ID_PRIJAVLJENOG_KORISNIKA, -1);

        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        generisiKod();

        buttonQR.setOnClickListener(this);
    }

    private void generisiKod(){
        String vrednost = sdf.format(new Date()) + " id=" + korisnikId;

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        QRGEncoder qrgEncoder = new QRGEncoder(vrednost, null, QRGContents.Type.TEXT, smallerDimension);
        try {
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            imageViewQR.setImageBitmap(bitmap);
        } catch (Exception e) {
        }
    }

    private void prikaziKod(){
        String datum = sdf.format(new Date());
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KOD, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCES_DATUM1, datum);
        editor.commit();

        Baza baza = new Baza(this);
        GarazaModel garazaModel = baza.vratiGarazaModelPoId(garazaId);
        int brojSlobodnihMesta = garazaModel.getBrojSlobodnihMesta()-1;
        baza.izmeniGarazaModel(garazaId, garazaModel.getNaziv(), garazaModel.getAdresa(), brojSlobodnihMesta,
                garazaModel.getKapacitet(), garazaModel.getCena());
        Toast.makeText(getApplicationContext(), "Kod je očitan", Toast.LENGTH_SHORT).show();
    }

    private int izracunajVreme(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KOD, 0);
        String dat = sharedPreferences.getString(SHARED_PREFERENCES_DATUM1, datum2 +" "+ vreme2);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCES_DATUM1, "");
        editor.commit();
        vreme1 = dat.substring(11);
        int sat1 = Integer.parseInt(vreme1.substring(0,2));
        int minut1 = Integer.parseInt(vreme1.substring(3,5));
        int sekunda1 = Integer.parseInt(vreme1.substring(6));

        int sat2 = Integer.parseInt(vreme2.substring(0,2));
        int minut2 = Integer.parseInt(vreme2.substring(3,5));
        int sekunda2 = Integer.parseInt(vreme2.substring(6));

        float razlika = ((sat2*3600 + minut2*60 + sekunda2) - (sat1*3600 + minut1*60 + sekunda1))/60;
        return (int)razlika;
    }

    private void napraviRacun(){
        String datum = sdf.format(new Date());
        datum2 = datum.substring(0,10);
        vreme2 = datum.substring(11);

        Baza baza = new Baza(this);
        GarazaModel garazaModel = baza.vratiGarazaModelPoId(garazaId);
        int brojSlobodnihMesta = garazaModel.getBrojSlobodnihMesta()+1;
        baza.izmeniGarazaModel(garazaId, garazaModel.getNaziv(), garazaModel.getAdresa(), brojSlobodnihMesta,
                garazaModel.getKapacitet(), garazaModel.getCena());
        float garazaCena = garazaModel.getCena();
        int vreme = izracunajVreme();
        float cena = vreme * garazaCena/60;

        KorisnikModel korisnikModel = baza.vratiKorisnikModelPoId(korisnikId);
        float novoStanjeRacuna = korisnikModel.getStanje() - cena;
        baza.izmeniKorisnikModel(korisnikId, korisnikModel.getIme(), korisnikModel.getPrezime(), korisnikModel.getKorisnickoIme(),
                korisnikModel.getLozinka(), korisnikModel.getEmail(), novoStanjeRacuna);

        baza.dodajRacun(vreme1, vreme2, datum2, cena, garazaId, korisnikId);

        List<RacunModel> listaRacuna = baza.vratiSveRacunModel(korisnikId);
        RacunModel racunModel = listaRacuna.get(listaRacuna.size()-1);
        int racunId = racunModel.getRacunId();

        Intent i = new Intent(this, RacunActivity.class);
        Bundle extra = new Bundle();
        extra.putInt("racunId", racunId);
        i.putExtras(extra);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonQR){
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KOD, 0);
            boolean stanje =  sharedPreferences.getBoolean(SHARED_PREFERENCES_STANJE, true);
            if(stanje){
                Baza baza = new Baza(this);
                KorisnikModel korisnikModel = baza.vratiKorisnikModelPoId(korisnikId);
                if(korisnikModel.getStanje()>0) {
                    prikaziKod();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SHARED_PREFERENCES_STANJE, false);
                    editor.commit();
                }else{
                    Toast.makeText(getApplicationContext(), "Nemate dovoljno novca na računu", Toast.LENGTH_SHORT).show();
                }
            }else{
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SHARED_PREFERENCES_STANJE, true);
                editor.commit();
                napraviRacun();
            }
        }
    }
}
