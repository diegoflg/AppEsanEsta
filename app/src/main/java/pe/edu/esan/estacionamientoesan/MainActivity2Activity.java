package pe.edu.esan.estacionamientoesan;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity2Activity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    /*Declaracion de variables generales a usar en la actividad*/

    //Se crea una cadena de texto privada... : (sus valores son dados de esa manera ya que asi estan definidos tambien en la base de datos y sus php)
    //cuyo valor es la URL del php
    private static String url_all_empresas = "http://www.estacionamientoesan.net76.net/essconnect/get_all_empresas.php";
    //cuyo valor es success
    private static final String TAG_SUCCESS = "success";
    //cuyo valor es users
    private static final String TAG_PRODUCTS = "users";
    //cuyo valor es username
    private static final String TAG_NOMBRE = "username";
    //cuyo valor es username2
    private static final String TAG_NOMBRE2 = "username2";
    //cuyo valor es username3
    private static final String TAG_NOMBRE3 = "username3";

    //Se crean cadenas de texto para los estados de cada semaforo
    private String estado = "waa";
    private String estadoalonso = "waa";
    private String estadopolo = "waa";
    //Se crea una variable de tipo JSONArray con valor inicial nulo
    JSONArray products = null;
    //Se crea una nueva variable del tipo JSONParser(clase)
    JSONParser jParser = new JSONParser();
    //Se crea cadenas de texto para los nuevos estados de cada semaforo
    String estado2;
    String estado22;
    String estado23;
    //Se crea una cadena de texto privada
    private String tt;
    //Se crea una variable para GoogleApiClient
    GoogleApiClient mGoogleApiClient;
    //Se crea una variable de locacion
    Location mLastLocation;
    //Se crean numeros reales para longitud y latidud
    double longitude;
    double latitude;
    //Se crea una cadena para el correo con valor inicial nulo
    String correo="";

    //Constructor que sirve para el API de Google
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Se le asigna el layout a la actividad
        setContentView(R.layout.lay_estacionamiento);

        //Se crea y da valores a un dialogo de progreso
        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait, Loading Page...", true);

        //Se crea y da valor a un reproductor de sonido
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.hifi);

        //Se hace un intent para obtener los datos de la otra actividad
        Intent p = getIntent();
        Bundle b = p.getExtras();
        correo = b.getString("correo");

        //Se crea una variable handler nueva
        final Handler h = new Handler();
        //Se crea y da valor a un numero para que sea el tiempo del handle en milisegundos
        final int delay = 5000; //milliseconds
        //Se crea una variable handle nueva
        final Handler hT = new Handler();


        /*SE CREAN VARIABLES Y SE LES ASIGNA SU ID CORRESPONDIENTE DEL LAYOUT Y SE LES INICIA UNA VISIBILIDAD(VISIBLES O GONE(NO APARECEN EN LO ABSOLUTO))*/
        //Separador 1

        //Titulo Esan
        final TextView tvEsan = (TextView) findViewById(R.id.textView);
        //Texto lugar esan
        final TextView tvLEsan = (TextView) findViewById(R.id.textView7);
        //Boton Esan
        final Button btEsan = (Button)findViewById(R.id.btEsan);


        //Separador 2
        View sep2 = (View)findViewById(R.id.sep2);
        sep2.setVisibility(View.VISIBLE);
        //Titulo Polo
        final TextView tvPolo = (TextView) findViewById(R.id.textView3);
        tvPolo.setVisibility(View.VISIBLE);
        //Texto lugar  Polo
        final TextView tvLPolo = (TextView) findViewById(R.id.textView6);
        tvLPolo.setVisibility(View.VISIBLE);
        //Boton Polo
        final Button btPolo = (Button)findViewById(R.id.btPolo);
        btPolo.setVisibility(View.VISIBLE);
        //Boton ir Polo
        final Button btIr = (Button)findViewById(R.id.btir);
        btIr.setVisibility(View.VISIBLE);

        //Separador 3
        final View sep3 = (View)findViewById(R.id.sep3);
        sep3.setVisibility(View.VISIBLE);
        //Titulo Alonso de Molina
        final TextView tvAlonso = (TextView) findViewById(R.id.textView4);
        tvAlonso.setVisibility(View.VISIBLE);
        //Texto lugar Alonso
        final TextView tvLAlonso= (TextView) findViewById(R.id.textView5);
        tvLAlonso.setVisibility(View.VISIBLE);
        //Boton Alonso
        final Button btAlonso = (Button)findViewById(R.id.btAlonso);
        btAlonso.setVisibility(View.VISIBLE);

        //Separador 4
        final View sep4 = (View)findViewById(R.id.sep4);
        sep4.setVisibility(View.VISIBLE);
        /*FIN DE CREACION DE VARIABLES Y ASIGNACION DE ID's JUNTO A SU VISIBILIDAD*/

        //Se muestra el dialogo de progreso
        dialog.show();
        //Se ejecuta la accion dada en el metodo con el mismo nombre

        if (isNetworkAvailable() == false) {

            tvPolo.setVisibility(View.GONE);
            tvLPolo.setVisibility(View.GONE);
            btPolo.setVisibility(View.GONE);
            btIr.setVisibility(View.GONE);
            sep3.setVisibility(View.GONE);

            //APARECE ALONSO
            tvAlonso.setVisibility(View.GONE);
            tvLAlonso.setVisibility(View.GONE);
            btAlonso.setVisibility(View.GONE);
            sep4.setVisibility(View.GONE);


            //APARECE ESAN
            tvEsan.setText("Compruebe su conexión a internet");
            tvLEsan.setVisibility(View.GONE);
            btEsan.setVisibility(View.GONE);


        }else{
            tvEsan.setText("ESAN");
            tvLEsan.setVisibility(View.VISIBLE);
            btEsan.setVisibility(View.VISIBLE);

            tvPolo.setVisibility(View.VISIBLE);
            tvLPolo.setVisibility(View.VISIBLE);
            btPolo.setVisibility(View.VISIBLE);
            btIr.setVisibility(View.VISIBLE);
            sep3.setVisibility(View.VISIBLE);

            //APARECE ALONSO
            tvAlonso.setVisibility(View.VISIBLE);
            tvLAlonso.setVisibility(View.VISIBLE);
            btAlonso.setVisibility(View.VISIBLE);
            sep4.setVisibility(View.VISIBLE);



            new LoadAllProductsIni().execute();

        }


        //Handler que ocurre despues de 5 segundos(delay): Verifica si se esta mostrando el dialogo de progreso y si es correcto, desaparece
        //caso contrario no hace nada
        hT.postDelayed(new Runnable() {
            @Override
            public void run() {


                if (isNetworkAvailable() == false) {

                    tvPolo.setVisibility(View.GONE);
                    tvLPolo.setVisibility(View.GONE);
                    btPolo.setVisibility(View.GONE);
                    btIr.setVisibility(View.GONE);
                    sep3.setVisibility(View.GONE);

                    //APARECE ALONSO
                    tvAlonso.setVisibility(View.GONE);
                    tvLAlonso.setVisibility(View.GONE);
                    btAlonso.setVisibility(View.GONE);
                    sep4.setVisibility(View.GONE);


                    //APARECE ESAN
                    tvEsan.setText("Compruebe su conexión a internet");
                    tvLEsan.setVisibility(View.GONE);
                    btEsan.setVisibility(View.GONE);


                }else {
                    tvEsan.setText("ESAN");
                    tvLEsan.setVisibility(View.VISIBLE);
                    btEsan.setVisibility(View.VISIBLE);


                    tvPolo.setVisibility(View.VISIBLE);
                    tvLPolo.setVisibility(View.VISIBLE);
                    btPolo.setVisibility(View.VISIBLE);
                    btIr.setVisibility(View.VISIBLE);
                    sep3.setVisibility(View.VISIBLE);

                    //APARECE ALONSO
                    tvAlonso.setVisibility(View.VISIBLE);
                    tvLAlonso.setVisibility(View.VISIBLE);
                    btAlonso.setVisibility(View.VISIBLE);
                    sep4.setVisibility(View.VISIBLE);

                    new LoadTIME2().execute();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }
        }, delay);


        //Handler que ocurre cada 5 segundos(delay):
            //1. Se verifica los estados para asignar una imagen a los botones
            //2. Se verifica el cambio de estado para que entonces se reproduzca el sonido
        h.postDelayed(new Runnable() {
            public void run() {

                if (estado.equals("rojo")) {
                    btEsan.setBackgroundResource(R.drawable.brojo);

                }

                if (estadoalonso.equals("rojo")) {
                    btAlonso.setBackgroundResource(R.drawable.brojo);

                }

                if (estadopolo.equals("rojo")) {
                    btPolo.setBackgroundResource(R.drawable.brojo);

                }


                if (estado.equals("amarillo")) {
                    btEsan.setBackgroundResource(R.drawable.bamarillo);

                }

                if(btAlonso.getText().toString().equals("Cerrado")){
                    btAlonso.setBackgroundResource(R.drawable.brojo);

                }else{

                    if (estadoalonso.equals("amarillo")) {
                        btAlonso.setBackgroundResource(R.drawable.bamarillo);

                    }

                }


                if(btPolo.getText().toString().equals("Cerrado")){
                    btPolo.setBackgroundResource(R.drawable.brojo);

                }else{

                    if (estadopolo.equals("amarillo")) {
                        btPolo.setBackgroundResource(R.drawable.bamarillo);


                    }

                }




                if (estado.equals("verde")) {
                    btEsan.setBackgroundResource(R.drawable.bverde);

                }


                if(btAlonso.getText().toString().equals("Cerrado")){
                    btAlonso.setBackgroundResource(R.drawable.brojo);

                }else{
                    if (estadoalonso.equals("verde")) {
                        btAlonso.setBackgroundResource(R.drawable.bverde);

                    }

                }





                if(btPolo.getText().toString().equals("Cerrado")){
                    btPolo.setBackgroundResource(R.drawable.brojo);

                }else{

                    if (estadopolo.equals("verde")) {
                        btPolo.setBackgroundResource(R.drawable.bverde);

                    }

                }

                if (estado.equals(estado2)) {
                } else {
                    if(estado.equals("verde")){
                        mp.start();
                    }else if(estado.equals("rojo")){
                        mp.start();
                    }else if(estado.equals("amarillo")){
                        mp.start();
                    }

                }


                if (estadoalonso.equals(estado22)) {
                } else {
                    if(tvPolo.getVisibility()==View.VISIBLE){
                        if(estadoalonso.equals("verde")){
                            mp.start();
                        }else if(estadoalonso.equals("rojo")){
                            mp.start();
                        }else if(estadoalonso.equals("amarillo")){
                            mp.start();
                        }

                    }else{

                    }
                }

                if (estadopolo.equals(estado23)) {
                } else {
                    if(tvPolo.getVisibility()==View.VISIBLE){
                        if(estadopolo.equals("verde")){
                            mp.start();
                        }else if(estadopolo.equals("rojo")){
                            mp.start();
                        }else if(estadopolo.equals("amarillo")){
                            mp.start();
                        }

                    }else{
                    }
                }



                //Se ejecuta la accion del mismo nombre

                if (isNetworkAvailable() == false) {

                    tvPolo.setVisibility(View.GONE);
                    tvLPolo.setVisibility(View.GONE);
                    btPolo.setVisibility(View.GONE);
                    btIr.setVisibility(View.GONE);
                    sep3.setVisibility(View.GONE);

                    //APARECE ALONSO
                    tvAlonso.setVisibility(View.GONE);
                    tvLAlonso.setVisibility(View.GONE);
                    btAlonso.setVisibility(View.GONE);
                    sep4.setVisibility(View.GONE);


                    //APARECE ESAN
                    tvEsan.setText("Compruebe su conexión a internet");
                    tvLEsan.setVisibility(View.GONE);
                    btEsan.setVisibility(View.GONE);


                }else{
                    tvEsan.setText("ESAN");
                    tvLEsan.setVisibility(View.VISIBLE);
                    btEsan.setVisibility(View.VISIBLE);


                    tvPolo.setVisibility(View.VISIBLE);
                    tvLPolo.setVisibility(View.VISIBLE);
                    btPolo.setVisibility(View.VISIBLE);
                    btIr.setVisibility(View.VISIBLE);
                    sep3.setVisibility(View.VISIBLE);

                    //APARECE ALONSO
                    tvAlonso.setVisibility(View.VISIBLE);
                    tvLAlonso.setVisibility(View.VISIBLE);
                    btAlonso.setVisibility(View.VISIBLE);
                    sep4.setVisibility(View.VISIBLE);


                    new LoadAllProducts().execute();

                }

                //Se sigue haciendo el handler cada 5 segundos
                h.postDelayed(this, delay);

            }
        }, delay);


        //Metodo que se activa cuando se da click al boton ir
        btIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                // EstaMapas fragment = new EstaMapas();

                //  Bundle bundle = new Bundle();
                //  bundle.putString("lugar","polo");
                //  fragment.setArguments(bundle);
                // fragmentManager.beginTransaction().add(R.id.container, fragment, "Map1").commit();

                //Se les da valores reales a las variables
                latitude = -12.098581;
                longitude = -76.970599;
                //Se llama al metodo
                buildGoogleApiClient();

                //Verifica la existencia del cliente API de Google
                if (mGoogleApiClient != null) {
                    mGoogleApiClient.connect();
                } else {
                    Toast.makeText(MainActivity2Activity.this, "Not connected...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onConnected(Bundle bundle) {
        //Muestra el mapa segun la posicion del usuario y traza la ruta hacia los datos dados
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            tt = "Latitude: " + String.valueOf(mLastLocation.getLatitude()) + "Longitude: " +
                    String.valueOf(mLastLocation.getLongitude());
            Log.v("location", tt);

            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + String.valueOf(mLastLocation.getLatitude()) + "," + String.valueOf(mLastLocation.getLongitude()) + "&daddr=" + String.valueOf(latitude) + "," + String.valueOf(longitude)));
            startActivity(intent);
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        //Metodo que muestra un mensaje en pantalla cuando la conexion ha sido suspendida
        Toast.makeText(this, "Connection suspended...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Metodo que muestra un mensaje en pantalla cuando la conexion ha fallado
        Toast.makeText(this, "Failed to connect...", Toast.LENGTH_SHORT).show();
    }


    //Clase que ocurre en segundo plano que obtiene los datos de cada semaforo
    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
        }

        protected String doInBackground(String... args) {
            List params = new ArrayList();
            JSONObject json = jParser.makeHttpRequest(url_all_empresas, "GET", params);
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    products = json.getJSONArray(TAG_PRODUCTS);
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        estado2=estado;
                        estado22=estadoalonso;
                        estado23=estadopolo;
                        estado=c.getString(TAG_NOMBRE);
                        estadoalonso=c.getString(TAG_NOMBRE2);
                        estadopolo=c.getString(TAG_NOMBRE3);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
        //Metodo despues de terminar la accion
        }
    }

    //Carga de los datos del semaforo
    class LoadAllProductsIni extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
        }

        protected String doInBackground(String... args) {
            List params = new ArrayList();
            JSONObject json = jParser.makeHttpRequest(url_all_empresas, "GET", params);
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    products = json.getJSONArray(TAG_PRODUCTS);
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        estado2=c.getString(TAG_NOMBRE);
                        estado22=c.getString(TAG_NOMBRE2);
                        estado23=c.getString(TAG_NOMBRE3);
                        estado=c.getString(TAG_NOMBRE);
                        estadoalonso=c.getString(TAG_NOMBRE2);
                        estadopolo=c.getString(TAG_NOMBRE3);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {

        }
    }

    //Metodo que verifica si hay conexion a internet
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) MainActivity2Activity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //Metodo de segundo plano que obtiene la hora a traves de una pagina web de las zonas horarias mundiales
    private class LoadTIME2 extends AsyncTask<Void, Void, Void> {
        //Sacado de: http://www.survivingwithandroid.com/2014/04/parsing-html-in-android-with-jsoup.html
        //Pagina web real: http://www.timeanddate.com/worldclock/peru/lima
        //HTML DE WEB: view-source:http://www.timeanddate.com/worldclock/peru/lima

        //Separador 1


        //Titulo Esan
        TextView tvEsan = (TextView) findViewById(R.id.textView);
        //Texto lugar esan
        TextView tvLEsan = (TextView) findViewById(R.id.textView7);
        //Boton Esan
        Button btEsan = (Button)findViewById(R.id.btEsan);


        //Separador 2
        View sep2 = (View)findViewById(R.id.sep2);
        //Titulo Polo
        TextView tvPolo = (TextView) findViewById(R.id.textView3);
        //Texto lugar  Polo
        TextView tvLPolo = (TextView) findViewById(R.id.textView6);
        //Boton Polo
        Button btPolo = (Button)findViewById(R.id.btPolo);
        //Boton ir Polo
        Button btIr = (Button)findViewById(R.id.btir);


        //Separador 3
        View sep3 = (View)findViewById(R.id.sep3);
        //Titulo Alonso de Molina
        TextView tvAlonso = (TextView) findViewById(R.id.textView4);
        //Texto lugar Alonso
        TextView tvLAlonso= (TextView) findViewById(R.id.textView5);
        //Boton Alonso
        Button btAlonso = (Button)findViewById(R.id.btAlonso);




        //Separador 4
        View sep4 = (View)findViewById(R.id.sep4);

        //Se crea una cadena de texto cuyo valor es la URL de la pagina web
        String url = "http://www.timeanddate.com/worldclock/peru/lima";
        //Se crea una variable de tipo Documento y se le da como valor inicial nulo
        Document doc = null;
        //Se crean dos cadenas de texto para la hora y el dia
        String horac;
        String diac;

        @Override
        protected void onPreExecute() {
            //Metodo antes de ejecutar la accion
            super.onPreExecute();
            /*
            sep2.setVisibility(View.GONE);
            tvAlonso.setVisibility(View.GONE);
            tvLAlonso.setVisibility(View.GONE);
            btAlonso.setVisibility(View.GONE);

            //SE VA EL POLO
            sep3.setVisibility(View.GONE);
            tvPolo.setVisibility(View.GONE);
            tvLPolo.setVisibility(View.GONE);
            btPolo.setVisibility(View.GONE);
            btIr.setVisibility(View.GONE);

            sep4.setVisibility(View.GONE);
             */
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Metodo que ocurre en segundo plano
            // TODO Auto-generated method stub

            //Intenta
            try {
                // obtener el documento de la pagina conectandose con el Jsoup
                //TIEMPO HH:MM
                doc = Jsoup.connect(url).get();
                //Se crea un elemento llamado hora cuyo valor sera el dato(hora y minutos) dentro del parametro dado
                Elements hora = doc.select("span[id=fshrmin]");
                //Se le da valor a la cadena de texto convirtiendo el dato encontrado a texto
                horac = hora.text();

                //Se crea un elemento cuyo valor sera el dato (fecha en ingles) dentro del parametro dado
                Elements dia = doc.select("span[id=ctdat]");
                //Se le da valor a la cadena de texto convirtiendo el dato encontrado a texto
                diac = dia.text();

                /*
                Elements topicList = doc.select("h2.topic");
                Log.i("TIEMPO", "META: " + metaElem);
                Log.i("TIEMPO", "TOPICLIST : " + topicList);
                Elements links = doc.select("a[href]"); // a with href
                Element masthead = doc.select("div.masthead").first();
                // div with class=masthead
                Elements resultLinks = doc.select("h3.r > a"); // direct a after h3
                Log.i("TIEMPO", "AHREF: " + links);
                Log.i("TIEMPO", "MASTHEAD: " + masthead);
                Log.i("TIEMPO", "ResultLinks : " + resultLinks);
                */


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.i("TIEMPO", "ERROR");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //Metodo que ocurre despues de terminada la accion
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            tvEsan.setText("ESAN");
            tvLEsan.setVisibility(View.VISIBLE);
            btEsan.setVisibility(View.VISIBLE);
            // Here you can do any UI operations like textview.setText("test");

            //Se verifica que el texto contenga los valores dado en parametros:
                //(Si contiene Lunes, Martes, Miercoles, Jueves o Viernes)
            if((diac.contains("Monday") || diac.contains("Tuesday") || diac.contains("Wednesday") || diac.contains("Thursday") || diac.contains("Friday"))){
                //Si la hora contiene 18, 19, 20, 21, 22 o 23 (se le pone dos puntos para que coja la hora y no lo confunda con los minutos)
                if(horac.contains("18:") || horac.contains("19:") || horac.contains("20:")||
                   horac.contains("21:") || horac.contains("22:") || horac.contains("23:")){
                //-----> Entonces:
                    //APARECE EL POLO




                    btPolo.setText("");

                        //APARECE ALONSO
                    btAlonso.setText("");






                }else {
                    /* Si fuera else if:
                    if(horac.contains("00:") || horac.contains("01:") || horac.contains("02:") ||
                         horac.contains("03:") || horac.contains("04:") || horac.contains("05:") ||
                         horac.contains("06:") || horac.contains("07:") || horac.contains("08:") ||
                         horac.contains("09:") || horac.contains("10:") || horac.contains("11:") ||
                         horac.contains("12:") || horac.contains("13:") || horac.contains("14:") ||
                         horac.contains("15:") || horac.contains("16:") || horac.contains("17:") )
                     */
                //---->Caso contrario de no contener esas horas
                    //SE VA EL POLO


                    btPolo.setText("Cerrado");

                    //SE VA ALONSO

                    btAlonso.setText("Cerrado");

                }
            } else if( diac.contains("Saturday")){
                //Caso contrario de que el dia sea Sabado:
                if( horac.contains("06:3")|| horac.contains("06:4")|| horac.contains("06:5")||
                        horac.contains("07:") || horac.contains("08:") || horac.contains("09:") ||
                        horac.contains("10:") || horac.contains("11:") || horac.contains("12:") ||
                        horac.contains("13:") || horac.contains("14:") || horac.contains("15:") ||
                        horac.contains("16:") || horac.contains("17:") || horac.contains("18:")){
                    //Si la hora es de 6:30 AM hasta las 18:59 PM entonces:

                    //APARECE EL POLO
                    btPolo.setText("");
                }else{
                    //Si no es esa hora el dia sabado entonces:
                    //SE VA EL POLO
                    btPolo.setText("Cerrado");
                }


                if( horac.contains("07:3")|| horac.contains("07:4")|| horac.contains("07:5")||
                        horac.contains("08:") || horac.contains("09:") || horac.contains("10:") ||
                        horac.contains("11:") || horac.contains("12:") || horac.contains("13:") ||
                        horac.contains("14:") || horac.contains("15:") || horac.contains("16:") ||
                        horac.contains("17:") || horac.contains("18:")){
                    //Si la hora es desde 7:30 am hasta las 18:59 PM entonces:

                    //APARECE ALONSO
                    btAlonso.setText("");


                }else{
                    //Caso que no sea esas horas del dia sabado:
                    //SE VA ALONSO
                    btAlonso.setText("Cerrado");
                }


            } else if(diac.contains("Sunday")){
                //Si el dia es Domingo:
                //SE VA EL POLO
                btPolo.setText("Cerrado");

                //SE VA ALONSO
                btAlonso.setText("Cerrado");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2,menu);
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
            //Cuando se de click a la opcion Cerrar del menu:
            case R.id.cerrars:
                //Se crea un intento de cambio de actividad de la actual a la principal
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                //Se crea un paquete de datos
                Bundle b = new Bundle();
                //Se mete un dato en el paquete
                b.putString("log", "no");
                //Se manda en el intento el paquete
                i.putExtras(b);
                //Se inicia el intento
                startActivity(i);
                //finish();
                //Se termina esta actividad
                this.finish();

                return true;

            //Cuando se de click a la opcion Informacion del menu:
            case R.id.info:
                //Se crea un intento de cambio de actividad de esta a Informacion
                Intent j = new Intent(getApplicationContext(), Info.class);
                //Se inicia el intento
                startActivity(j);
                return true;

            //Cuando se de click a la opcion Mi perfil del menu:
            case R.id.perfil:

                if (isNetworkAvailable() == false) {
                    Toast.makeText(MainActivity2Activity.this, "Compruebe su conexión a internet", Toast.LENGTH_LONG).show();

                }else {
                    //Se crea un intetno de cambio de actividad de esta a Perfil
                    Intent p = new Intent(getApplicationContext(), Perfil.class);
                    //Se crea un paquete de datos
                    Bundle k = new Bundle();
                    //Se mete un valor en el paquete
                    k.putString("correo", correo);
                    //Se manda el paquete al intento
                    p.putExtras(k);
                    //Se inicia el intento o cambio de actividad
                    startActivity(p);

                }
                return true;

            default:return super.onOptionsItemSelected(item);
        }
        //noinspection SimplifiableIfStatement
    }

}
