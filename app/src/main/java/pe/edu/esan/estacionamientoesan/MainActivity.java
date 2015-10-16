package pe.edu.esan.estacionamientoesan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class MainActivity extends ActionBarActivity {
    //Declaracion de variables

    //Texto para que se vea en la consola para verificacion del codigo
    private final String TAG= "APP";

    //Cuadros de textos editables
    EditText et1,et2;

    //Cadena de texto que obtiene el lenguaje del celular
    String langloc=Locale.getDefault().getDisplayLanguage();

    //Numero entero que define el lenguaje inicial del app
    int langinicial=0;

    //Numero entero que define el lenguaje final
    int lang=0;

    //Boton del login para acceder al app
    Button botonacceder; //SOLO SE USA PARA CAMBIAR LA FUENTE

    //Cadena de texto que dice el resultado del loggin
    String loggresult="";

    //Cadena de texto que muestra como mensaje el valor dado
    String mensaje="Usuario o password invalida";

    //Entero que manda el tipo de usuario
    int tipo=1;

    private static final String LOGIN_URL = "http://www.estacionamientoesan.site88.net/cas2/login.php";

    // La respuesta del JSON es
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    private ProgressDialog pDialog;

    // Clase JSONParser
    JSONParser jsonParser = new JSONParser();




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


        if(langloc.equals("español")){
            //Da valor al entero determinado para el lenguaje inicial
            langinicial=0;

        }
        if(langloc.equals("English")){
            //Da valor al entero determinado para el lenguaje inicial
            langinicial=1;

        }
        if(langloc.equals("français")){
            //Da valor al entero determinado para el lenguaje inicial
            langinicial=2;

        }

        //Se crea una lista de datos
        ArrayList<ItemData> list=new ArrayList<>();

        //Dependiendo del lenguaje inicial se veran los casos
        switch(langinicial){
            case 0:
                //Si el lenguaje inicial es Español el texto de la lista dira
                list.add(new ItemData("Español",R.drawable.es));
                list.add(new ItemData("Ingles",R.drawable.um));
                list.add(new ItemData("Frances",R.drawable.fr));
                break;
            case 1:
                //Si el lenguaje inicial es Ingles el texto de la lista dira
                list.add(new ItemData("Spanish",R.drawable.es));
                list.add(new ItemData("English",R.drawable.um));
                list.add(new ItemData("French",R.drawable.fr));
                break;
            case 2:
                //Si el lenguaje inicial es Frances el texto de la lista dira
                list.add(new ItemData("Espagnol",R.drawable.es));
                list.add(new ItemData("Anglais",R.drawable.um));
                list.add(new ItemData("Français",R.drawable.fr));
                break;
        }


        //Se crea un Spinner o seleccionador de lista corta y se le da su valor como elemento del layout
        Spinner sp=(Spinner)findViewById(R.id.spinner);


        //Se crea un nuevo adaptador a partir de la clase SpinnerAdapter y se le da sus parametros
        SpinnerAdapter adapter=new SpinnerAdapter(this,
                R.layout.spinerlayout,R.id.txt,list);

        //Se le asigna un adaptador al spinner
        sp.setAdapter(adapter);


        //Cuando el Spinner es clickeado
        sp.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                //Declaracion de numero entero que obtiene la posicion del item seleccionado
                int index = arg0.getSelectedItemPosition();

                //Se le da un valor al lenguaje
                lang = index;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Metodo cuando nada es seleccionado
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
        et1.setText("a");


        //Asingacion de valores de texto a los cuadros de texto
        et2.setText("a");


        //ESTO ES PARA LA FUENTE
        String font_path = "font/HelveticaNeue-Light.ttf"; //ruta de la fuente
        Typeface TF = Typeface.createFromAsset(getAssets(),font_path);//llamanos a la CLASS TYPEFACE y la definimos con un CREATE desde ASSETS con la ruta STRING

        //Se le asigna una fuente al cuadro de texto
        et1.setTypeface(TF);

        //Se le asigna una fuente al cuadro de texto
        et2.setTypeface(TF);

        //Se le asigna una fuente al texto del boton
        botonacceder.setTypeface(TF);

        //Se crea y da valor a una base de datos del tipo creado en la clase AdminBD






    }


    public void logstart(View v){

        new AttemptLogin().execute();

    }


    class AttemptLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            String username = et1.getText().toString();
            String password = et2.getText().toString();
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


                    Intent i = new Intent(getApplicationContext(), MainActivity2Activity.class);
                    //finish();
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
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(MainActivity.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void register(View v){


        Intent o = new Intent(getApplicationContext(), Formulario.class);
        startActivity(o);
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
            case R.id.salir:
                this.finish();
                return true;
            default:return super.onOptionsItemSelected(item);

        }
        //noinspection SimplifiableIfStatement
    }

}