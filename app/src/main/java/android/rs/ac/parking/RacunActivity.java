package android.rs.ac.parking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RacunActivity extends AppCompatActivity {

    private LinearLayout linearLayoutRacun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_racun);

        initComponents();
    }

    private void initComponents(){
        linearLayoutRacun = (LinearLayout) findViewById(R.id.linearLayoutRacun);

        Bundle extra = getIntent().getExtras();
        int racunId = extra.getInt("racunId");

        Baza baza = new Baza(this);
        RacunModel racunModel = baza.vratiRacunModelPoId(racunId);
        KorisnikModel korisnikModel = baza.vratiKorisnikModelPoId(racunModel.getKorisnikId());
        GarazaModel garazaModel = baza.vratiGarazaModelPoId(racunModel.getGarazaId());

        String tekst = "RACUN \n " +
                "ID računa: " + racunModel.getRacunId() + "\n" +
                "Vreme početka: " + racunModel.getVremePocetka() + "\n" +
                "Vreme kraja: " + racunModel.getVremeKraja() + "\n" +
                "Datum: " + racunModel.getDatum() + "\n" +
                "Korisničko ime: " + korisnikModel.getKorisnickoIme() + "\n" +
                "Garaža: " + garazaModel.getNaziv() + "\n" +
                "Cena: " + racunModel.getCena() + "RSD \n";

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ConstraintLayout item = (ConstraintLayout) inflater.inflate(R.layout.activity_racun_model, null);
        ((TextView) item.findViewById(R.id.labelRacunModel)).setText(tekst);

        linearLayoutRacun.addView(item);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, PocetnaActivity.class));
    }
}
