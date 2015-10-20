package pe.edu.esan.estacionamientoesan;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by educacionadistancia on 16/10/2015.
 */
public class Datos extends ActionBarActivity {


    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String REGISTER_URL = "http://www.estacionamientoesan.site88.net/cas2/registroesan.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private ProgressDialog pDialog;
    private static final String TAG_PRODUCTS = "registros";
    private static final String TAG_NOMBRE = "correo";
    JSONArray products = null;
    JSONParser jParser = new JSONParser();
    String mensaje="";
    String correo="";
    String fecha="";
    String codigo="";

    EditText etPlaca,etPlaca2, etTelefono, etContrasena, etCodigo;
    CheckBox cbTyC;


    //Para fuentes
    TextView tvPlaca, textView13, tvContrasena, tvTelefono, tvCodigo;
    Button aceptar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_datos);
        etPlaca = (EditText) findViewById(R.id.etPlaca);
        etPlaca2 = (EditText) findViewById(R.id.editText5);
        etTelefono = (EditText) findViewById(R.id.etTelefono);
        etContrasena = (EditText) findViewById(R.id.etContraseña);
        etCodigo = (EditText) findViewById(R.id.etCodigo);

        etPlaca.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        etPlaca2.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        Intent i = getIntent();
        Bundle b = i.getExtras();
        correo = b.getString("email");
        fecha = b.getString("fecha");
        codigo = b.getString("codigo");
        Log.v("qwertycodigo", codigo);

        etPlaca.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etPlaca.getText().toString().length() == 3)     //size as per your requirement
                {

                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (etPlaca.getText().toString().length() == 3)     //size as per your requirement
                {
                    etPlaca2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etPlaca.getText().toString().length() == 3)     //size as per your requirement
                {

                }

            }
        });

        //Fuentes:
        Typeface fontTit = Typeface.createFromAsset(getAssets(),"font/HelveticaNeue-Bold.ttf" );
        Typeface fontTex = Typeface.createFromAsset(getAssets(),"font/HelveticaNeue-Light.ttf" );
        tvPlaca = (TextView)findViewById(R.id.tvPlaca);
        tvPlaca.setTypeface(fontTit);
        etPlaca.setTypeface(fontTex);
        textView13 = (TextView)findViewById(R.id.textView13);
        textView13.setTypeface(fontTit);
        etPlaca2.setTypeface(fontTex);
        tvContrasena =(TextView)findViewById(R.id.tvContraseña);
        tvContrasena.setTypeface(fontTit);
        etContrasena.setTypeface(fontTex);
        tvTelefono= (TextView)findViewById(R.id.tvTelefono);
        tvTelefono.setTypeface(fontTit);
        etTelefono.setTypeface(fontTex);
        tvCodigo= (TextView)findViewById(R.id.tvCodigo);
        tvCodigo.setTypeface(fontTit);
        etCodigo.setTypeface(fontTex);
        aceptar =(Button)findViewById(R.id.aceptar);
        aceptar.setTypeface(fontTit);
    }

    public void aceptar(View v) {
        mensaje="";

      // new CreateUser().execute();

        if(etPlaca.length()!=3 || etPlaca2.length()!=3){
            mensaje=mensaje+"-La placa ingresada no es correcta"+ "\n";

        }
        if(etTelefono.length()!=7 && etTelefono.length()!=9){
            mensaje=mensaje+"-El telefono ingresado no es correcto"+ "\n";

        }
        if(etContrasena.length()==0){
            mensaje=mensaje+"-Ingrese una contraseña"+ "\n";

        }

        if(!etCodigo.getText().toString().equals(codigo)){
            mensaje=mensaje+"-El codigo ingresado es incorrecto"+ "\n";

        }

        Log.v("qwerty",mensaje);

        if(mensaje.equals("")){
            new CreateUser().execute();

        }


       // Intent i = new Intent(getApplicationContext(), MainActivity2Activity.class);
        //startActivity(i);
    }


    class CreateUser extends AsyncTask<String, String, String> {//Metodo que guarda el estado en la base de datos
        @Override
        protected void onPreExecute() {
            //Metodo antes de ser ejecutada la accion
            super.onPreExecute();
            pDialog = new ProgressDialog(Datos.this);
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

            String password = String.valueOf(etContrasena.getText());
            String placa1 = String.valueOf(etPlaca.getText())+String.valueOf(etPlaca2.getText());
            String telefono = String.valueOf(etTelefono.getText());

            String estado = "false";
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("correo", correo));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("placa1", placa1));
                params.add(new BasicNameValuePair("telefono", telefono));
                params.add(new BasicNameValuePair("fecha", fecha));
                params.add(new BasicNameValuePair("codigo", codigo));
                params.add(new BasicNameValuePair("estado", estado));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        REGISTER_URL, "POST", params);

                // full json response
                Log.d("Registering attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("User Created!", json.toString());
                    return json.getString(TAG_MESSAGE);
                } else {
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
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formulario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {

            case R.id.regresar:
                finish();
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
        //noinspection SimplifiableIfStatement
    }

}

