package pe.edu.esan.estacionamientoesan;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by educacionadistancia on 20/10/2015.
 */
public class Perfil extends ActionBarActivity {
    TextView tvPerfil, tvPlaca, tvGuion, tvGuion2,tvContraseña, tvTelefono;
    EditText etPlaca, etPlacaC, etPlaca2, etPlacaC2,etContraseña, etTelefono;
    Button actualizar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_perfil);
        tvPerfil     = (TextView)findViewById(R.id.tvPerfil);
        tvPlaca      = (TextView)findViewById(R.id.tvPlaca);
        tvGuion      = (TextView)findViewById(R.id.tvGuion);
        tvGuion2     = (TextView)findViewById(R.id.tvGuion2);
        tvContraseña = (TextView)findViewById(R.id.tvContraseña);
        tvTelefono   = (TextView)findViewById(R.id.tvTelefono);

        etPlaca      = (EditText)findViewById(R.id.etPlaca);
        etPlacaC     = (EditText)findViewById(R.id.etPlacaC);
        etPlaca2     = (EditText)findViewById(R.id.etPlaca2);
        etPlacaC2    = (EditText)findViewById(R.id.etPlacaC2);
        etContraseña = (EditText)findViewById(R.id.etContraseña);
        etTelefono   = (EditText)findViewById(R.id.etTelefono);

        actualizar = (Button)findViewById(R.id.actualizar);

        Typeface fuente = Typeface.createFromAsset(getAssets(),"font/HelveticaNeue-Light.ttf");
        tvPerfil.setTypeface(fuente);
        tvPlaca.setTypeface(fuente);
        tvGuion.setTypeface(fuente);
        tvGuion2.setTypeface(fuente);
        tvContraseña.setTypeface(fuente);
        tvTelefono.setTypeface(fuente);

        etPlaca.setTypeface(fuente);
        etPlacaC.setTypeface(fuente);
        etPlaca2.setTypeface(fuente);
        etPlacaC2.setTypeface(fuente);
        etContraseña.setTypeface(fuente);
        etTelefono.setTypeface(fuente);
    }
}
