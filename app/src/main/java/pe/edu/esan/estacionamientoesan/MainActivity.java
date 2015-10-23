package pe.edu.esan.estacionamientoesan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends ActionBarActivity {

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private Boolean saveEntro;
    //Texto para que se vea en la consola para verificacion del codigo
    private final String TAG= "APP";
    //Cuadros de textos editables
    EditText et1,et2;
    //Cadena de texto que obtiene el lenguaje del celular

    //Boton del login para acceder al app
    Button botonacceder; //SOLO SE USA PARA CAMBIAR LA FUENTE
    //Cadena de texto que dice el resultado del loggin
    String loggresult="";
    //Cadena de texto que muestra como mensaje el valor dado
    String mensaje="Usuario o password invalida";
    //Entero que manda el tipo de usuario
    int tipo=1;
    private static final String LOGIN_URL = "http://www.estacionamientoesan.net76.net/cas2/login.php";
    // La respuesta del JSON es
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private ProgressDialog pDialog;
    // Clase JSONParser
    JSONParser jsonParser = new JSONParser();
    CheckBox cb1;
    Button bstart;
    TextView heol;
    String logestado="asd";

    String correo="asd";

    String cor="asd";
    int reg=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Se guarda el estado del fragmento actual
        super.onCreate(savedInstanceState);
        //Se obtiene la pantalla del movil y se oculta el teclado de la pantalla
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //Manda el contenido al fragmento con la vista del layout correspondiente
        setContentView(R.layout.activity_main);
        //Obtiene el servicio de WiFi
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //Activa el WiFi
        wifi.setWifiEnabled(true);
        //Se le asigna el id a la variable segun el layout
        bstart = (Button) findViewById(R.id.button);
        //Se le asigna el id a la variable segun el layout
        cb1 = (CheckBox) findViewById(R.id.cb1);
        //Se le asigna el id a la variable segun el layout
        heol=(TextView)findViewById(R.id.tvhe);
        //Se le da una cadena de texto al cuadro de texto
        heol.setText(Html.fromHtml("<u>He olvidado mi contraseña</u>"));

        //Se da un intento de obtencion de variables del main
        try{
            Intent i = getIntent();
            Bundle b = i.getExtras();
            logestado = b.getString("log");
        }catch (Exception e){

        }


        try{
            Intent i2 = getIntent();
            Bundle b2 = i2.getExtras();
            reg = b2.getInt("registro");
            cor = b2.getString("cor");
        }catch (Exception e){

        }




        //Metodo que se activa cuando se da click al checkbox
        cb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Si el checkbox es chequeado
                if (cb1.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", et1.getText().toString());
                    loginPrefsEditor.putString("password", et2.getText().toString());
                    loginPrefsEditor.putString("correo", et1.getText().toString());
                    loginPrefsEditor.commit();
                } else {
                    //Caso contrario
                    loginPrefsEditor.putBoolean("saveLogin", false);
                    loginPrefsEditor.commit();
                }
            }
        });


        //Declaracion de variable
        SpannableString s = new SpannableString("ESAN");
        s.setSpan(new TypefaceSpan("HelveticaNeue-Roman.ttf"), 0 , s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(s);

        //Creacion de variable cuyo valor se le asigna como el soporte de la barra de actividades
        android.support.v7.app.ActionBar abLogin = getSupportActionBar();

        //Texto
        CharSequence titulo = abLogin.getTitle().toString();

        //Declaracion de variables de cuadros de texto con sus elementos determinados en el layout
        et1 = (EditText) findViewById(R.id.et1);

        //Declaracion de variables de cuadros de texto con sus elementos determinados en el layout
        et2 = (EditText) findViewById(R.id.et2);


        botonacceder = (Button) findViewById(R.id.button); //SOLO ES USADO PARA LA FUENTE

        //Asingacion de valores de texto a los cuadros de texto




        //ESTO ES PARA LA FUENTE
        String font_path = "font/HelveticaNeue-Light.ttf"; //ruta de la fuente
        Typeface TF = Typeface.createFromAsset(getAssets(),font_path);//llamanos a la CLASS TYPEFACE y la definimos con un CREATE desde ASSETS con la ruta STRING

        //Se le asigna una fuente al cuadro de texto
        et1.setTypeface(TF);

        //Se le asigna una fuente al cuadro de texto
        et2.setTypeface(TF);

        //Se le asigna una fuente al texto del boton
        botonacceder.setTypeface(TF);


        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();


        if(reg==1){

            Intent i = new Intent(getApplicationContext(), MainActivity2Activity.class);
            Bundle o=new Bundle();

            loginPrefsEditor.putBoolean("entro", true);
            loginPrefsEditor.putString("correo", cor);
            loginPrefsEditor.putString("logged", "yes");
            loginPrefsEditor.commit();

            o.putString("correo",cor);
            i.putExtras(o);
            finish();
            startActivity(i);

            Log.v("entroo","reg==1"+cor);


        }


        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        saveEntro = loginPreferences.getBoolean("entro", false);
        if (saveLogin == true) {
            correo=loginPreferences.getString("correo", "");
            et1.setText(loginPreferences.getString("username", ""));
            et2.setText(loginPreferences.getString("password", ""));
            cb1.setChecked(true);

        }




            try{
                if(logestado.equals("no")){
                    loginPrefsEditor.putString("logged", logestado);
                    loginPrefsEditor.commit();
                }
            }catch (Exception e){

            }








        if (saveEntro == true) {
            if(loginPreferences.getString("logged", "").equals("yes")){
                correo=loginPreferences.getString("correo", "");

                //Intento de cambio de actividad y de paso de valores
                Intent i = new Intent(getApplicationContext(), MainActivity2Activity.class);
                Bundle o=new Bundle();
                o.putString("correo",correo);
                i.putExtras(o);
                finish();
                startActivity(i);
            }
        }


        //Cuando el boton es presionado se ejecuta la clase
        bstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable() == false) {
                    Toast.makeText(MainActivity.this, "Compruebe su conexión a internet", Toast.LENGTH_LONG).show();

                }else{
                    new AttemptLogin().execute();
                }

            }
        });


        //Cuando el texto es seleccionado se abre la actividad de Olvido
        heol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable() == false) {
                    Toast.makeText(MainActivity.this, "Compruebe su conexión a internet", Toast.LENGTH_LONG).show();

                }else{
                    Intent u = new Intent(getApplicationContext(), Olvido.class);
                    //finish();
                    startActivity(u);


                }


            }
        });
    }



    class AttemptLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            //Metodo que ocurre antes de ejecutar la accion
            super.onPreExecute();
            //Se da valor al dialogo de progreso y se da el contexto
            pDialog = new ProgressDialog(MainActivity.this);
            //Se le asigna el mensaje
            pDialog.setMessage("Attempting login...");
            //Se da indeterminado falso
            pDialog.setIndeterminate(false);
            //Se da valor falso al cancelable
            pDialog.setCancelable(false);
            //Se muestra el dialogo de progreso
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            //Metodo que ocurre en segundo plano
            //Declaracion de variables
            int success;
            //Cadenas de texto que son obtenidas a traves de lo que es ingresado por el usuario en los campos de usuario y contraseña
            String username = et1.getText().toString();
            String password = et2.getText().toString();

            //Se intenta
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("correo", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
                        params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());
                    // save user data


                        loginPrefsEditor.putBoolean("entro", true);
                        loginPrefsEditor.putString("correo", et1.getText().toString());
                        loginPrefsEditor.putString("logged", "yes");
                        loginPrefsEditor.commit();



                    Intent i = new Intent(getApplicationContext(), MainActivity2Activity.class);
                    Bundle o=new Bundle();
                    o.putString("correo", et1.getText().toString());
                    i.putExtras(o);
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            //Metodo que se da al terminar la ejecucion

            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(MainActivity.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void register(View v){
        //Metodo que se da al dar click al boton registrar
        //Se intenta ir a la actividad de Correo para ingresar datos de registro

        if (isNetworkAvailable() == false) {
            Toast.makeText(MainActivity.this, "Compruebe su conexión a internet", Toast.LENGTH_LONG).show();

        }else{

            Intent o = new Intent(getApplicationContext(), Correo.class);
            startActivity(o);
        }


    }



    private boolean isNetworkAvailable() {
        //Verifica la conexion a internet
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id)
        {
            //Si se da a la opcion Salir en el menu de la actividad
            case R.id.salir:
                //Se termina la actividad
                this.finish();
                return true;

            default:return super.onOptionsItemSelected(item);

        }
        //noinspection SimplifiableIfStatement
    }


}