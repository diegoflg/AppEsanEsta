package pe.edu.esan.estacionamientoesan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    /*Declaracion de variables generales de la actividad*/

    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    //Cadena de texto cuyo valor es el url del php correspondiente
    private static final String REGISTER_URL = "http://www.estacionamientoesan.net76.net/cas2/registroesan.php";
    //Cadena de texto cuyo valor es success
    private static final String TAG_SUCCESS = "success";
    //Cadena de texto cuyo valor es message
    private static final String TAG_MESSAGE = "message";
    //Dialogo de progreso
    private ProgressDialog pDialog;
    //Cadenas de texto cuyos valoes son nulos al inicio
    String mensaje="";
    String correo="";
    String fecha="";
    String codigo="";
    String nombre="";
    String apellidos="";

    //Cuadros de texto editables
    EditText etPlaca,etPlaca2, etTelefono, etContrasena, etCodigo, etApellido, etNombre;

    //Para fuentes
        //Cuadros de textos no editables para el usuario
    TextView tvPlaca, textView13, tvContrasena, tvTelefono, tvCodigo, tvApellido, tvNombre;
        //Boton aceptar
    Button aceptar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Se le asigna el layout correspondiente a la actividad
        setContentView(R.layout.lay_datos);

        //Se les da los valores de id a los elementos anteriores segun su layout

        etNombre=(EditText)findViewById(R.id.etNom);
        etApellido=(EditText)findViewById(R.id.etApe);
        etPlaca = (EditText) findViewById(R.id.etPlaca);
        etPlaca2 = (EditText) findViewById(R.id.editText5);
        etTelefono = (EditText) findViewById(R.id.etTelefono);
        etContrasena = (EditText) findViewById(R.id.etContraseña);
        etCodigo = (EditText) findViewById(R.id.etCodigo);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Se envio un codigo de confirmacion a el correo ingresado anteriormente")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();



        //Se filtra los cuadros editables para que contengan solo 3 valores como maximo y minimo
        etPlaca.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(3)});
        etPlaca2.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(3)});

        //S e crea un Intent para recibir los datos de correo, fecha y codigo obtenidos en la actividad anterior(Correo.java)
        Intent i = getIntent();
        Bundle b = i.getExtras();
        correo = b.getString("email");
        fecha = b.getString("fecha");
        codigo = b.getString("codigo");

        //Metodo que se activa al escribir algun texto en el cuadro
        etPlaca.addTextChangedListener(new TextWatcher() {
            //Metodo antes del cambio
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etPlaca.getText().toString().length() == 3)     //size as per your requirement
                {

                }

            }

            //Metodo durante el cambio
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (etPlaca.getText().toString().length() == 3)     //size as per your requirement
                {
                    //Cuando la cantidad de valores en el cuadro editable es 3 entonces el cursor automaticamente
                    //se manda al segundo cuadro editable
                    etPlaca2.requestFocus();

                }

            }

            //Metodo despues del cambio
            @Override
            public void afterTextChanged(Editable editable) {
                if (etPlaca.getText().toString().length() == 3)     //size as per your requirement
                {

                }

            }
        });


        //Fuentes:
        Typeface fontTit = Typeface.createFromAsset(getAssets(),"font/HelveticaNeue-Roman.ttf" );
        Typeface fontTex = Typeface.createFromAsset(getAssets(),"font/HelveticaNeue-Light.ttf" );

        //Se les da valor a los elementos segun el layout y se les asigna la fuente
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



        //Metodo al escribir texto en el cuadro editable
        etPlaca2.addTextChangedListener(new TextWatcher() {
            //Metodo antes del cambio
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(etPlaca.getText().toString().length()==3)     //size as per your requirement
                {

                }

            }

            //Metodo durante el cambio
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(etPlaca2.getText().toString().length()==3)     //size as per your requirement
                {
                    //Cuando el numero de valores ingresados en el cuadro es igual a 3 entonces
                    //el cursor pasa de ese cuadro al de escribir contraseña automaticamente
                    etContrasena.requestFocus();
                }

            }

            //Metodo despues del cambio
            @Override
            public void afterTextChanged(Editable editable) {
                if(etPlaca2.getText().toString().length()==3)     //size as per your requirement
                {

                }

            }
        });


    }

    //Metodo que se da al hacer click en el boton aceptar
    public void aceptar(View v) {
        //Se da nulo como valor inicial a la cadena de texto antes declarada
        mensaje="";

      // new CreateUser().execute();

        /*Verificacion de datos ingresados por el usuario*/

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
            mensaje=mensaje+"-El código ingresado es incorrecto"+ "\n";
        }

        Log.v("qwerty",mensaje);
        /*Fin de verificacion de datos ingresados por el usuario*/

        //Si se validan los datos entonces se crea un nuevo usuario en la base de datos
        if(mensaje.equals("")){
            new CreateUser().execute();

        }else{
            //Caso contrario se mostrara un mensaje en pantalla
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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


       // Intent i = new Intent(getApplicationContext(), MainActivity2Activity.class);
        //startActivity(i);
    }


    class CreateUser extends AsyncTask<String, String, String> {//Metodo que guarda los datos en la base de datos
        @Override
        protected void onPreExecute() {
            //Metodo antes de ser ejecutada la accion
            super.onPreExecute();

            //Se crea un dialogo de progreso
            pDialog = new ProgressDialog(Datos.this);
            //Se le da el valor del mensaje a aparecer en el dialogo
            pDialog.setMessage("Creating User...");
            //Se le da falso a ser indeterminado
            pDialog.setIndeterminate(false);
            //Se le da valor cancelable
            pDialog.setCancelable(true);
            //El dialogo aparece
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            //Metodo que se hace en segundo plano
            // TODO Auto-generated method stub
            // Check for success tag
            int success;

            //Creacion de variables y asignacion de valores obteniendo los datos introducidos por el usuario
            String password = String.valueOf(etContrasena.getText());
            String placa1 = String.valueOf(etPlaca.getText())+String.valueOf(etPlaca2.getText());
            String telefono = String.valueOf(etTelefono.getText());
            String nombre = String.valueOf(etNombre.getText());
            String apellidos = String.valueOf(etApellido.getText());

            //Se crea una cadena de texto y se le asigna valor falso
            String estado = "false";

            //Se intenta
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
                params.add(new BasicNameValuePair("nombre", nombre));
                params.add(new BasicNameValuePair("apellidos", apellidos));

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
            //La actividad se cierra
            finish();


            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            //Se crea un paquete de datos
            Bundle b = new Bundle();
            //Se mete un dato en el paquete
            b.putInt("registro", 1);
            b.putString("cor", correo);
            //
            //Se manda en el intento el paquete
            i.putExtras(b);
            //Se inicia el intento
            startActivity(i);
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

            //Siendo llamado el menu y dado click a la opcion de regresar
            case R.id.regresar:
                //Se termina la actividad principal
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

