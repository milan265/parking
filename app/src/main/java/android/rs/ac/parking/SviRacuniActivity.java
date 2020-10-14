package android.rs.ac.parking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SviRacuniActivity extends AppCompatActivity implements View.OnClickListener{

    private DatePicker inputDatumOd;
    private DatePicker inputDatumDo;
    private Button buttonPrikaziRacune;
    private LinearLayout linearLayoutSviRacuni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_svi_racuni);
        }else{
            setContentView(R.layout.activity_svi_racuni_landscape);
        }

        initComponents();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_svi_racuni);
        }else{
            setContentView(R.layout.activity_svi_racuni_landscape);
        }
    }

    private void initComponents(){
        linearLayoutSviRacuni = (LinearLayout) findViewById(R.id.linearLayoutSviRacuni);
        inputDatumOd = (DatePicker) findViewById(R.id.inputDatumOd);
        inputDatumDo = (DatePicker) findViewById(R.id.inputDatumDo);
        buttonPrikaziRacune = (Button) findViewById(R.id.buttonPrikaziRacune);

        buttonPrikaziRacune.setOnClickListener(this);
    }

    private void prikaziRacune(){
        linearLayoutSviRacuni.removeAllViews();

        int danOd = inputDatumOd.getDayOfMonth();
        int mesecOd = inputDatumOd.getMonth();
        int godinaOd = inputDatumOd.getYear();

        int danDo = inputDatumDo.getDayOfMonth();
        int mesecDo = inputDatumDo.getMonth();
        int godinaDo = inputDatumDo.getYear();

        mesecOd++;
        mesecDo++;

        Calendar kalendarOd = Calendar.getInstance();
        kalendarOd.set(godinaOd, mesecOd, danOd);
        Calendar kalendarDo = Calendar.getInstance();
        kalendarDo.set(godinaDo, mesecDo, danDo);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCES_PREFIX, 0);
        int korisnikId =  sharedPreferences.getInt(MainActivity.SHARED_PREFERENCES_ID_PRIJAVLJENOG_KORISNIKA, -1);

        Baza baza = new Baza(this);
        List<RacunModel> listaRacuna = baza.vratiSveRacunModel(korisnikId);

        String dan;
        String mesec;
        String godina;
        String datum;
        int d;
        int m;
        int g;

        for(RacunModel racunModel : listaRacuna){
            datum = racunModel.getDatum();
            godina = datum.substring(0,4);
            mesec = datum.substring(5,7);
            dan = datum.substring(8);

            g = Integer.parseInt(godina);
            m = Integer.parseInt(mesec);
            d = Integer.parseInt(dan);

            Calendar kalendar = Calendar.getInstance();
            kalendar.set(g, m, d);

            if(kalendar.getTime().after(kalendarOd.getTime()) && (kalendar.getTime().before(kalendarDo.getTime())) || kalendar.getTime().toString().equals(kalendarDo.getTime().toString())){
                KorisnikModel korisnikModel = baza.vratiKorisnikModelPoId(racunModel.getKorisnikId());
                GarazaModel garazaModel = baza.vratiGarazaModelPoId(racunModel.getGarazaId());
                String tekst = "RACUN \n " +
                        "ID računa: " + racunModel.getRacunId() + "\n" +
                        "Vreme početka: " + racunModel.getVremePocetka() + "\n" +
                        "Vreme kraja: " + racunModel.getVremeKraja() + "\n" +
                        "Datum: " + datum + "\n" +
                        "Korisničko ime: " + korisnikModel.getKorisnickoIme() + "\n" +
                        "Garaža: " + garazaModel.getNaziv() + "\n" +
                        "Cena: " + racunModel.getCena() + "RSD \n";
                ConstraintLayout item = (ConstraintLayout) inflater.inflate(R.layout.activity_racun_model, null);
                ((TextView)item.findViewById(R.id.labelRacunModel)).setText(tekst);
                linearLayoutSviRacuni.addView(item);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonPrikaziRacune){
            prikaziRacune();
        }
    }
}
