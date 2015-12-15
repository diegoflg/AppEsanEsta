package pe.edu.esan.estacionamientoesan;
        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.location.Location;
        import android.media.MediaPlayer;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Handler;
        import android.support.v7.app.ActionBarActivity;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.location.LocationServices;
        import org.json.JSONArray;
        import org.json.JSONObject;
        import java.io.IOException;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.List;


public class MainActivity2Activity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static String url_all_empresas = "http://estacionamientos.esan.edu.pe/esconnect/get_all_empresas.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "users";
    private static final String TAG_NOMBRE = "username";
    private static final String TAG_NOMBRE2 = "username2";
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
    int prueba=0;
    int inter=204;

    private ProgressDialog dialog;







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

        //Se crea y da valor a un reproductor de sonido

        final MediaPlayer mp2 = MediaPlayer.create(this, R.raw.inter2);


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

        //Se ejecuta la accion dada en el metodo con el mismo nombre





        dialog = new ProgressDialog(MainActivity2Activity.this);
        dialog.setMessage("Cargando...");
        dialog.setCancelable(false);
        dialog.show();

            //Si hay conexion
            tvEsan.setText("ESAN-Campus");
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


            try{

                new LoadAllProductsIni().execute();


            }catch (Exception e){

            }







        //Handler que ocurre cada 5 segundos(delay):
        //1. Se verifica los estados para asignar una imagen a los botones
        //2. Se verifica el cambio de estado para que entonces se reproduzca el sonido
        h.postDelayed(new Runnable() {
            public void run() {





                if(prueba>0){

                    if (inter != 204) {
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
                        tvEsan.setText(R.string.CompruebeCon);
                        tvLEsan.setVisibility(View.GONE);
                        btEsan.setVisibility(View.GONE);
                        mp2.start();

                    } else {


                        tvEsan.setText("ESAN-Campus");
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

                    }



                    try {
                        new LoadAllProducts().execute();

                    } catch (Exception e) {

                    }

                    if (estado.equals("rojo")) {
                        btEsan.setBackgroundResource(R.drawable.brojo);
                        btEsan.setText("Solo \n profesores");
                    }
                    if (estado.equals("amarillo")) {
                        btEsan.setBackgroundResource(R.drawable.bamarillo);
                        btEsan.setText("");

                    }

                    if (estado.equals("verde")) {
                        btEsan.setBackgroundResource(R.drawable.bverde);
                        btEsan.setText("Abierto");

                    }


                    if (estadoalonso.equals("amarillo")) {
                        btAlonso.setBackgroundResource(R.drawable.bamarillo);
                        btAlonso.setText("");

                    }
                    if (estadoalonso.equals("rojo")) {
                        btAlonso.setBackgroundResource(R.drawable.brojo);
                        btAlonso.setText("Cerrado");
                    }
                    if (estadoalonso.equals("verde")) {
                        btAlonso.setBackgroundResource(R.drawable.bverde);
                        btAlonso.setText("Abierto");


                    }


                    if (estadopolo.equals("amarillo")) {
                        btPolo.setBackgroundResource(R.drawable.bamarillo);
                        btPolo.setText("");
                    }
                    if (estadopolo.equals("rojo")) {
                        btPolo.setBackgroundResource(R.drawable.brojo);
                        btPolo.setText("Cerrado");
                    }
                    if (estadopolo.equals("verde")) {
                        btPolo.setBackgroundResource(R.drawable.bverde);
                        btPolo.setText("Abierto");
                    }

                    if(dialog.isShowing()){
                        dialog.dismiss();

                    }
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
                latitude = -12.097523;
                longitude = -76.970253;
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

            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + String.valueOf(mLastLocation.getLatitude()) + "," + String.valueOf(mLastLocation.getLongitude()) + "&daddr=" + String.valueOf(latitude) + "," + String.valueOf(longitude)));
            startActivity(intent);
        }else{
            Toast.makeText(this, "Active los servicios de ubicación con redes movilies y/o Wi-Fi", Toast.LENGTH_SHORT).show();
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


//Se comprueba la conexion a internet, si hay conexion kk tendra el valor de 204
            int kk=0;

            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                kk= urlc.getResponseCode();
            } catch (IOException e) {

            }


            if(kk==204){
//Si hay conexion se obtiene el estado de los estacionamientos
                List params = new ArrayList();

                try {

                    JSONObject json = jParser.makeHttpRequest(url_all_empresas, "GET", params);
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
                            prueba=prueba + 1;




                        }
                    }




                }catch(Exception e){

                }


            }


          inter=kk;

            return null;
        }
        protected void onPostExecute(String file_url) {

        }
    }

    //Carga de los datos del semaforo
    class LoadAllProductsIni extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {

            //Se crea y da valores a un dialogo de progreso


        }

        protected String doInBackground(String... args) {

//Se comprueba la conexion a internet, si hay conexion kk tendra el valor de 204
            int kk=0;

            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                kk= urlc.getResponseCode();
            } catch (IOException e) {

            }


            if(kk==204){
                // si hay internet se obtienen los estados del estacionamiento

                List params = new ArrayList();

                try {

                    JSONObject json = jParser.makeHttpRequest(url_all_empresas, "GET", params);
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




                }catch(Exception e){

                }


            }

            inter=kk;


            return null;
        }
        protected void onPostExecute(String file_url) {
            prueba=prueba + 1;



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

                this.finish();

                return true;

            //Cuando se de click a la opcion Informacion del menu:
            case R.id.info:
                //Se crea un intento de cambio de actividad de esta a Informacion
                Intent j = new Intent(getApplicationContext(), Info.class);
                //Se inicia el intento
                startActivity(j);
                return true;

            case R.id.sug:
                //Se crea un intento de cambio de actividad de esta a Informacion
                Intent k = new Intent(getApplicationContext(), Feedback.class);
                //Se inicia el intento
                startActivity(k);
                return true;



            default:return super.onOptionsItemSelected(item);
        }
        //noinspection SimplifiableIfStatement
    }







}
