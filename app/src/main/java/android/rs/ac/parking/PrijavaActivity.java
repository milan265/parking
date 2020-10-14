package android.rs.ac.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PrijavaActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText inputPrijavaKorisnickoIme;
    private EditText inputPrijavaLozinka;
    private Button buttonPrijava;
    private TextView labelPrijavaPoruka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prijava);

        initComponents();
    }

    private void initComponents(){
        inputPrijavaKorisnickoIme = (EditText) findViewById(R.id.inputPrijavaKorisnickoIme);
        inputPrijavaLozinka = (EditText) findViewById(R.id.inputPrijavaLozinka);
        buttonPrijava = (Button) findViewById(R.id.buttonPrijava);
        labelPrijavaPoruka = (TextView) findViewById(R.id.labelPrijavaPoruka);

        buttonPrijava.setOnClickListener(this);
    }

    private void prijaviSe(){
        Baza baza = new Baza(this);
        int id = baza.vratiKorisnikModelPoKorisnickoImeLozinka(inputPrijavaKorisnickoIme.getText().toString(), inputPrijavaLozinka.getText().toString());
        if(id>0){
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCES_PREFIX, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(MainActivity.SHARED_PREFERENCES_ID_PRIJAVLJENOG_KORISNIKA, id);
            editor.commit();
            startActivity(new Intent(this, PocetnaActivity.class));
        }else{
            labelPrijavaPoruka.setText("Korisnik sa unetim podacima ne postoji");
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonPrijava){
            prijaviSe();
        }
    }
}
