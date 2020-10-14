package android.rs.ac.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public final static String SHARED_PREFERENCES_PREFIX = "ParkingSharedPreferencesPrijavljen";
    public final static String SHARED_PREFERENCES_ID_PRIJAVLJENOG_KORISNIKA = "id_korisnika";

    private Button buttonPrijaviSe;
    private Button buttonRegistrujSe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Baza baza = new Baza(this);
        //baza.izmeniKorisnikModel(8, "Milan", "Savic", "milansavic", "milan123", "milan@mail.com", 1000);
        int id = proveraPrijave();
        if(id>0){
            startActivity(new Intent(this, PocetnaActivity.class));
        }
        initComponents();
    }

    private int proveraPrijave(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_PREFIX, 0);
        return sharedPreferences.getInt(SHARED_PREFERENCES_ID_PRIJAVLJENOG_KORISNIKA, -1);
    }

    private void initComponents(){
        buttonPrijaviSe = (Button) findViewById(R.id.buttonPrijaviSe);
        buttonRegistrujSe = (Button) findViewById(R.id.buttonRegistrujSe);

        buttonPrijaviSe.setOnClickListener(this);
        buttonRegistrujSe.setOnClickListener(this);
    }

    private void prijaviSe(){
        startActivity(new Intent(this, PrijavaActivity.class));
    }

    private void registrujSe(){
        startActivity(new Intent(this, RegistracijaActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonPrijaviSe:
                prijaviSe();
                break;
            case R.id.buttonRegistrujSe:
                registrujSe();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
