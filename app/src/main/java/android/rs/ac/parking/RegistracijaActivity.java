package android.rs.ac.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistracijaActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputIme;
    private EditText inputPrezime;
    private EditText inputKorisnickoIme;
    private EditText inputLozinka;
    private EditText inputLozinka2;
    private EditText inputEmail;
    private Button buttonRegistracija;
    private TextView labelRegistracijaPoruka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija);

        initComponents();
    }

    private void initComponents(){
        inputIme = (EditText) findViewById(R.id.inputIme);
        inputPrezime = (EditText) findViewById(R.id.inputPrezime);
        inputKorisnickoIme = (EditText) findViewById(R.id.inputKorisnickoIme);
        inputLozinka = (EditText) findViewById(R.id.inputLozinka);
        inputLozinka2 = (EditText) findViewById(R.id.inputLozinka2);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        buttonRegistracija = (Button) findViewById(R.id.buttonRegistracija);
        labelRegistracijaPoruka = (TextView) findViewById(R.id.labelRegistracijaPoruka);


        buttonRegistracija.setOnClickListener(this);
    }

    private void registrujSe(){
        Boolean popunjeniPodaci = false;
        Boolean lozinkaDobra = false;
        Boolean lozinkaDuzina = false;
        Boolean korisnickoImeDobro = true;
        Boolean emailDobro = false;

        labelRegistracijaPoruka.setText("");

        String paternEmail = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,4})+$";
        Pattern p = Pattern.compile(paternEmail);
        Matcher m = p.matcher(inputEmail.getText().toString());
        if(m.find()){
            emailDobro = true;
        }else{
            labelRegistracijaPoruka.append("Email nije u dobrom formatu \n");
        }
        if(inputIme.getText().toString().isEmpty() || inputPrezime.getText().toString().isEmpty() ||
                inputKorisnickoIme.getText().toString().isEmpty() || inputLozinka.getText().toString().isEmpty() ||
                inputLozinka2.getText().toString().isEmpty() || inputEmail.getText().toString().isEmpty()){
            labelRegistracijaPoruka.append("Sva polja moraju biti popunjena \n");
        }else{
            popunjeniPodaci = true;
        }
        if(inputLozinka.getText().toString().equals(inputLozinka2.getText().toString())){
            lozinkaDobra = true;
            if(inputLozinka.getText().toString().length() >= 6 ){
                lozinkaDuzina = true;
            }else{
                labelRegistracijaPoruka.append("Lozinka mora imati najmanje 6 karaktera \n");
            }
        }else{
            labelRegistracijaPoruka.append("Lozinke se ne poklapaju \n");
        }

        Baza baza = new Baza(this);
        List<KorisnikModel> listaKorisinkModel = baza.vratiSveKorisnikModel();

        for(KorisnikModel korisnikModel : listaKorisinkModel){
            if(inputKorisnickoIme.getText().toString().equals(korisnikModel.getKorisnickoIme())){
                korisnickoImeDobro = false;
                labelRegistracijaPoruka.append("Korisničko ime je zauzeto \n");
                break;
            }
        }
        if(popunjeniPodaci && lozinkaDobra && lozinkaDuzina && korisnickoImeDobro && emailDobro){
            baza.dodajKorisnika(inputIme.getText().toString(), inputPrezime.getText().toString(), inputKorisnickoIme.getText().toString(),
                    inputLozinka.getText().toString(), inputEmail.getText().toString(), 0);
            int id = baza.vratiKorisnikModelPoKorisnickoImeLozinka(inputKorisnickoIme.getText().toString(), inputLozinka.getText().toString());
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCES_PREFIX, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(MainActivity.SHARED_PREFERENCES_ID_PRIJAVLJENOG_KORISNIKA, id);
            editor.commit();
            startActivity(new Intent(this, PocetnaActivity.class));
        }


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonRegistracija){
            registrujSe();
        }
    }
}
