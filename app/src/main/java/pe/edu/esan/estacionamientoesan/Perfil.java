package pe.edu.esan.estacionamientoesan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    private static final String LOGIN_URL = "http://www.estacionamientoesan.site88.net/esconnect/get_registros.php";
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "Registro";
    private static final String TAG_CORREO = "correo";
    private static final String TAG_PLACA= "placa1";
    private static final String TAG_PLACA2 = "placa2";
    private static final String TAG_PLACA3 = "placa3";
    private static final String TAG_TELEFONO = "telefono";
    private static final String TAG_CONTRASENA = "password";
    private static final String url_update = "http://www.estacionamientoesan.site88.net/cas/registroactu.php";

    TextView tvPerfil, tvPlaca, tvGuion, tvGuion2, tvGuion3 , tvContraseña, tvTelefono;
    EditText etPlaca, etPlacaC, etPlaca2, etPlacaC2,etPlaca3, etPlacaC3, etContraseña, etTelefono;
    Button actualizar;

    String correo;

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

        Intent p = getIntent();
        Bundle b = p.getExtras();
        correo = b.getString("correo");
        Log.i("CORREO", correo);
        new GetUserDetails().execute();
    }


    class GetUserDetails extends AsyncTask<String, String, String> {

        /**
         * http://makersofandroid.blogspot.pe/2014/03/connecting-android-with-mysqlremote.html
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Perfil.this);
            pDialog.setMessage("Cargando datos. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         */
        protected String doInBackground(String... args) {
            List params = new ArrayList();
            JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "GET", params);
            int success;
            try {
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // successfully received product details
                    JSONArray productObj = json.getJSONArray(TAG_PRODUCTS); // JSON Array

                    for (int i = 0; i < productObj.length(); i++) {
                        JSONObject c = productObj.getJSONObject(i);
                        String gmail = c.getString(TAG_CORREO);


                    }

                    /*
                    for (int i = 0; i < productObj.length(); i++) {
                        JSONObject c = productObj.getJSONObject(i);
                        Log.i("FUNCIONA", "OBJETO: "+c);
                        String Correos =c.getString(TAG_CORREO);
                        Log.i("FUNCIONA", "CORREO: "+Correos);
                    }
                     */

                } else {
                    // product with pid not found
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }


    class SaveProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Perfil.this);
            pDialog.setMessage("Saving product ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving product
         */
        protected String doInBackground(String... args) {

            // getting updated data from EditTexts
            String telefono = etTelefono.getText().toString();
            String contraseña = etContraseña.getText().toString();


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_PRODUCTS, correo));
            params.add(new BasicNameValuePair(TAG_TELEFONO, telefono));
            params.add(new BasicNameValuePair(TAG_CONTRASENA, contraseña));


            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update, "POST", params);

            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about product update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }
}
