package android.rs.ac.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GarazeActivity extends AppCompatActivity {

    private Spinner inputGaraze;
    private TextView labelGaraze;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_garaze);
        }else{
            setContentView(R.layout.activity_garaze_landscape);
        }

        initComponents();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_garaze);
        }else{
            setContentView(R.layout.activity_garaze_landscape);
        }
    }

    private void initComponents(){
        inputGaraze = (Spinner) findViewById(R.id.inputGaraze);
        labelGaraze = (TextView) findViewById(R.id.labelGaraze);

        final Baza baza = new Baza(this);
        List<GarazaModel> listaGaraza = baza.vratiSveGarazaModel();
        ArrayList<String> lista = new ArrayList<>();
        for(GarazaModel garazaModel : listaGaraza){
            lista.add(garazaModel.getNaziv());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
        inputGaraze.setAdapter(adapter);

        inputGaraze.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                labelGaraze.setText("");
                String garazaNaziv = (String) ((Spinner) findViewById(R.id.inputGaraze)).getSelectedItem();
                GarazaModel garaza = baza.vratiGarazaModelPoNazivu(garazaNaziv);
                labelGaraze.append("Naziv: " + garaza.getNaziv() + "\n");
                labelGaraze.append("Adresa: " + garaza.getAdresa() + "\n");
                labelGaraze.append("Broj slobodnih mesta: " + garaza.getBrojSlobodnihMesta() + "\n");
                labelGaraze.append("Kapacitet: " + garaza.getKapacitet() + "\n");
                labelGaraze.append("Cena po satu: " + garaza.getCena() + "RSD \n");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
