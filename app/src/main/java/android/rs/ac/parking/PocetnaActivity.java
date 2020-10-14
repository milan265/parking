package android.rs.ac.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PocetnaActivity extends AppCompatActivity implements View.OnClickListener, LocationListener{

    private TextView labelPrognoza;
    private TextView labelInfo;
    private Button buttonKod;
    private Button buttonGaraze;
    private Button buttonIstorijaPlacanja;
    private Button buttonOdjaviSe;
    private double lat;
    private double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_pocetna);
        }else{
            setContentView(R.layout.activity_pocetna_landscape);
        }
        initComponents();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_pocetna);
        }else{
            setContentView(R.layout.activity_pocetna_landscape);
        }
    }

    private void initComponents(){
        labelPrognoza = (TextView) findViewById(R.id.labelPrognoza);
        labelInfo = (TextView) findViewById(R.id.labelInfo);
        buttonKod = (Button) findViewById(R.id.buttonKod);
        buttonGaraze = (Button) findViewById(R.id.buttonGaraze);
        buttonIstorijaPlacanja = (Button) findViewById(R.id.buttonIstorijaPlacanja);
        buttonOdjaviSe = (Button) findViewById(R.id.buttonOdjaviSe);

        initGps();

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCES_PREFIX, 0);
        int id = sharedPreferences.getInt(MainActivity.SHARED_PREFERENCES_ID_PRIJAVLJENOG_KORISNIKA, -1);
        if(id<1){
            odjaviSe();
        }
        Baza baza = new Baza(this);
        KorisnikModel korisnikModel = baza.vratiKorisnikModelPoId(id);
        popuniLabelInfo(korisnikModel);

        buttonKod.setOnClickListener(this);
        buttonGaraze.setOnClickListener(this);
        buttonIstorijaPlacanja.setOnClickListener(this);
        buttonOdjaviSe.setOnClickListener(this);
    }

    private void popuniLabelInfo(KorisnikModel korisnikModel){
        labelInfo.append("INFORMACIJE O KORISNIKU \n");
        labelInfo.append("Ime: " + korisnikModel.getIme() + "\n");
        labelInfo.append("Prezime: " + korisnikModel.getPrezime() + "\n");
        labelInfo.append("Korisničko ime: " + korisnikModel.getKorisnickoIme() + "\n");
        labelInfo.append("E-mail: " + korisnikModel.getEmail() + "\n");
        labelInfo.append("Stanje na računu: " + korisnikModel.getStanje()+ " RSD\n");
    }

    private void popuniLabelPrognoza(){
        String url ="http://api.openweathermap.org/data/2.5/weather?lat=" + this.lat + "&lon=" + this.lon +"&appid=88d9ebd968d3f6a9d5d283a8fdf0c768";
        ApiPrognoza.getJSON(url, new ReadDataHandler(){
            @Override
            public void handleMessage(Message msg) {
                String odgovor = getJson();
                try{
                    JSONObject jsonObject = new JSONObject(odgovor);
                    PrognozaModel prognozaModel = PrognozaModel.parseJSONObject(jsonObject);
                    labelPrognoza.append("Trenutna lokacija: " + prognozaModel.getMesto() + "\n");
                    double temp = prognozaModel.getTemperatura() - 273.15;
                    labelPrognoza.append(String.format("Temperatura: %4.1f C \n",temp));
                    labelPrognoza.append(String.format("Pritisak: %6.1f mb \n",prognozaModel.getPritisak()));
                    labelPrognoza.append(String.format("Vlažnost vazduha: %5.1f %% \n",prognozaModel.getVlaznost()));
                }catch (Exception e){
                }
            }
        });

    }

    private void kod(){
        startActivity(new Intent(this, KodActivity.class));
    }

    private void garaze(){
        startActivity(new Intent(this, GarazeActivity.class));
    }

    private void istorijaPlacanja(){
        startActivity(new Intent(this, SviRacuniActivity.class));
    }

    private void odjaviSe(){
        labelInfo.setText("");
        labelPrognoza.setText("");
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCES_PREFIX, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(MainActivity.SHARED_PREFERENCES_ID_PRIJAVLJENOG_KORISNIKA, -1);
        editor.commit();
        startActivity(new Intent(this, MainActivity.class));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonKod:
                kod();
                break;
            case R.id.buttonGaraze:
                garaze();
                break;
            case R.id.buttonIstorijaPlacanja:
                istorijaPlacanja();
                break;
            case R.id.buttonOdjaviSe:
                odjaviSe();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void initGps(){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.lat = location.getLatitude();
        this.lon = location.getLongitude();
        popuniLabelPrognoza();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}
