package pe.edu.esan.estacionamientoesan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by educacionadistancia on 20/10/2015.
 */
public class Perfil extends ActionBarActivity {
    private static final String LOGIN_URL = "http://www.estacionamientoesan.net76.net/essconnect/get_registros.php";


    JSONParser jsonParser = new JSONParser();

    private static final String REGISTER_URL2 = "http://www.estacionamientoesan.net76.net/cas/registroactu.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    TextView tvPerfil, tvPlaca, tvGuion, tvGuion2, tvGuion3 , tvContraseña, tvTelefono;
    EditText etPlaca, etPlacaC, etPlaca2, etPlacaC2,etPlaca3, etPlacaC3, etContraseña, etTelefono;
    Button actualizar;
    private ProgressDialog pDialog;

    String correo;
    String mensaje="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_perfil);
        tvPerfil = (TextView) findViewById(R.id.tvPerfil);
        tvPlaca = (TextView) findViewById(R.id.tvPlaca);
        tvGuion = (TextView) findViewById(R.id.tvGuion);
        tvGuion2 = (TextView) findViewById(R.id.tvGuion2);
        tvGuion3 = (TextView) findViewById(R.id.tvGuion3);
        tvContraseña = (TextView) findViewById(R.id.tvContraseña);
        tvTelefono = (TextView) findViewById(R.id.tvTelefono);

        etPlaca = (EditText) findViewById(R.id.etPlaca);
        etPlacaC = (EditText) findViewById(R.id.etPlacaC);
        etPlaca2 = (EditText) findViewById(R.id.etPlaca2);
        etPlacaC2 = (EditText) findViewById(R.id.etPlacaC2);
        etPlaca3 = (EditText) findViewById(R.id.etPlaca3);
        etPlacaC3 = (EditText) findViewById(R.id.etPlacaC3);
        etContraseña = (EditText) findViewById(R.id.etContraseña);
        etTelefono = (EditText) findViewById(R.id.etTelefono);

        actualizar = (Button) findViewById(R.id.actualizar);

        Typeface fuente = Typeface.createFromAsset(getAssets(), "font/HelveticaNeue-Light.ttf");
        tvPerfil.setTypeface(fuente);
        tvPlaca.setTypeface(fuente);
        tvGuion.setTypeface(fuente);
        tvGuion2.setTypeface(fuente);
        tvGuion3.setTypeface(fuente);
        tvContraseña.setTypeface(fuente);
        tvTelefono.setTypeface(fuente);

        etPlaca.setTypeface(fuente);
        etPlacaC.setTypeface(fuente);
        etPlaca2.setTypeface(fuente);
        etPlacaC2.setTypeface(fuente);
        etPlaca3.setTypeface(fuente);
        etPlacaC3.setTypeface(fuente);
        etContraseña.setTypeface(fuente);
        etTelefono.setTypeface(fuente);

        try{
            Intent p = getIntent();
            Bundle b = p.getExtras();
            correo = b.getString("correo");
        }catch (Exception e){

        }



        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mensaje = "";

                // new CreateUser().execute();

                if (etPlaca.length() != 3 || etPlacaC.length() != 3) {
                    mensaje = mensaje + "-La placa ingresada no es correcta" + "\n";

                }

                if (etPlaca2.length() != 3 || etPlacaC2.length() != 3) {
                    mensaje = mensaje + "-La placa ingresada no es correcta" + "\n";

                }

                if (etPlaca3.length() != 3 || etPlacaC3.length() != 3) {
                    mensaje = mensaje + "-La placa ingresada no es correcta" + "\n";

                }
                if (etTelefono.length() != 7 && etTelefono.length() != 9) {
                    mensaje = mensaje + "-El telefono ingresado no es correcto" + "\n";

                }
                if (etContraseña.length() == 0) {
                    mensaje = mensaje + "-Ingrese una contraseña" + "\n";

                }


                Log.v("qwerty", mensaje);

                if (mensaje.equals("")) {
                    new CreateUser2().execute();

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Perfil.this);
                    builder.setMessage(mensaje)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();


                }


            }
        });

    }

    class CreateUser2 extends AsyncTask<String, String, String> {//Metodo que guarda el estado en la base de datos


        @Override
        protected void onPreExecute() {
            //Metodo antes de ser ejecutada la accion


            super.onPreExecute();
            pDialog = new ProgressDialog(Perfil.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            //Metodo que se hace en segundo plano

            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("correo", correo));
                params.add(new BasicNameValuePair("placa1", etPlaca.getText().toString()+etPlacaC.getText().toString()));
                params.add(new BasicNameValuePair("placa2", etPlaca2.getText().toString()+etPlacaC2.getText().toString()));
                params.add(new BasicNameValuePair("placa3", etPlaca3.getText().toString()+etPlacaC3.getText().toString()));
                params.add(new BasicNameValuePair("password", etContraseña.getText().toString()));
                params.add(new BasicNameValuePair("telefono", etTelefono.getText().toString()));


                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        REGISTER_URL2, "POST", params);

                // full json response
                Log.d("Registering attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.v("actualizacion correcta", json.toString());
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Registering Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            //Metodo que se hace terminada la ejecucion de la accion

            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(Perfil.this, file_url, Toast.LENGTH_LONG).show();
            }



        }
    }

}
