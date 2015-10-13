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

    private static String url_all_empresas = "http://www.estacionamientoesan.site88.net/esconnect/get_all_empresas.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "users";
    private static final String TAG_NOMBRE = "username";
    private static final String TAG_NOMBRE2 = "username2";
    private static final String TAG_NOMBRE3 = "username3";
    private ImageView sema1e, sema2e, sema3e;
    private String estado = "waa";
    private String estadoalonso = "waa";
    private String estadopolo = "waa";
    JSONArray products = null;
    JSONParser jParser = new JSONParser();
    TextView tvlibres, titulo, tit1, tit2;
    String estado2;
    String estado22;
    String estado23;
    private String tt;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    double longitude;
    double latitude;
    int contador = 0;

    Button btEsan, btPolo, btAlonso, btir;

    //PARA FUENTE:
    TextView textViewestareg;

    TextView textView3;

    //ELEMENTOS PERTENECIENTES AL SLIDING UP PANEL

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
        setContentView(R.layout.lay_estacionamiento);


        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait, Loading Page...", true);


        //FUENTE Y COLOR PARA TEXTVIEWS
        String font_pathE = "font/HelveticaNeue-Roman.ttf"; //ruta de la fuente
        Typeface TFE = Typeface.createFromAsset(this.getAssets(), font_pathE);
        //llamanos a la CLASS TYPEFACE y la definimos con un CREATE desde ASSETS con la ruta STRING
        textViewestareg = (TextView) findViewById(R.id.textViewestareg);
        textViewestareg.setTypeface(TFE);

        textView3= (TextView)findViewById(R.id.textView3);

        String font_pathL = "font/HelveticaNeue-Light.ttf"; //ruta de la fuente
        Typeface TFL = Typeface.createFromAsset(this.getAssets(), font_pathL);
        btEsan = (Button) findViewById(R.id.btEsan);
        btAlonso = (Button) findViewById(R.id.btAlonso);
        btPolo = (Button) findViewById(R.id.btPolo);
        btir = (Button) findViewById(R.id.btir);
        final MediaPlayer mp = MediaPlayer.create(this.getApplicationContext(), R.raw.hifi);
        final Handler h = new Handler();
        final int delay = 5000; //milliseconds

        final Handler hT = new Handler();


        dialog.show();
        new LoadAllProducts().execute();

        hT.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }, delay);


        h.postDelayed(new Runnable() {
            public void run() {
                Log.v("tipo", "timer");
                Log.v("es", estado);
                Log.v("es2", estadoalonso);

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

                if (estadoalonso.equals("amarillo")) {
                    btAlonso.setBackgroundResource(R.drawable.bamarillo);

                }
                if (estadopolo.equals("amarillo")) {
                    btPolo.setBackgroundResource(R.drawable.bamarillo);


                }

                if (estado.equals("verde")) {
                    btEsan.setBackgroundResource(R.drawable.bverde);

                }

                if (estadoalonso.equals("verde")) {
                    btAlonso.setBackgroundResource(R.drawable.bverde);

                }

                if (estadopolo.equals("verde")) {
                    btPolo.setBackgroundResource(R.drawable.bverde);

                }

                if (estado.equals(estado2)) {
                } else {
                    if (Datah.getInstance().getMenu() == 1) {


                        mp.start();
                    }
                }


                if (estadoalonso.equals(estado22)) {
                } else {
                    if (Datah.getInstance().getMenu() == 1) {


                        //MediaPlayer mp = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.hifi);
                        mp.start();
                    }
                }

                if (estadopolo.equals(estado23)) {
                } else {
                    if (Datah.getInstance().getMenu() == 1) {

                        //MediaPlayer mp = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.hifi);
                        mp.start();
                    }
                }

                if (isNetworkAvailable() == false) {

                }
                new LoadAllProducts().execute();
                new LoadTIME().execute();
                new LoadTIME2().execute();
                h.postDelayed(this, delay);

            }
        }, delay);


        btir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                // EstaMapas fragment = new EstaMapas();

                //  Bundle bundle = new Bundle();
                //  bundle.putString("lugar","polo");
                //  fragment.setArguments(bundle);
                // fragmentManager.beginTransaction().add(R.id.container, fragment, "Map1").commit();
                Log.v("detect", "sepudo");

                latitude = -12.098581;
                longitude = -76.970599;

                buildGoogleApiClient();

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
        Toast.makeText(this, "Connection suspended...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed to connect...", Toast.LENGTH_SHORT).show();

    }


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

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) MainActivity2Activity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class LoadTIME extends AsyncTask<Void, Void, Void> {
        //Sacado de: http://www.survivingwithandroid.com/2014/04/parsing-html-in-android-with-jsoup.html
        //Pagina web real: http://www.timeanddate.com/worldclock/fullscreen.html?n=131
        //HTML DE WEB: view-source:http://www.timeanddate.com/worldclock/fullscreen.html?n=131#
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // Here you can do any UI operations like textview.setText("test");
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String url = "http://www.timeanddate.com/worldclock/fullscreen.html?n=131#";
            Document doc = null;
            try {
                //TIEMPO CON HH:MM:SS
                doc = Jsoup.connect(url).get();
                Elements metaElem = doc.select("div[id=i_time]");
                String name = metaElem.text();
                Log.i("TIEMPO", "NAME : " + name);

                /*
                //Elements topicList = doc.select("h2.topic");
                //Log.i("TIEMPO", "META: " + metaElem);
                //Log.i("TIEMPO", "TOPICLIST : " + topicList);
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
    }


    //SEGUNDA OPCION:
    private class LoadTIME2 extends AsyncTask<Void, Void, Void> {
        //Sacado de: http://www.survivingwithandroid.com/2014/04/parsing-html-in-android-with-jsoup.html
        //Pagina web real: http://www.timeanddate.com/worldclock/fullscreen.html?n=131
        //HTML DE WEB: view-source:http://www.timeanddate.com/worldclock/fullscreen.html?n=131#
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // Here you can do any UI operations like textview.setText("test");
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String url = "http://www.timeanddate.com/worldclock/peru/lima";
            Document doc = null;
            try {
                //TIEMPO HH:MM
                doc = Jsoup.connect(url).get();
                Elements metaElem = doc.select("span[id=fshrmin]");
                String name = metaElem.text();


                //Elements topicList = doc.select("h2.topic");
                //Log.i("TIEMPO", "META: " + metaElem);
                Log.i("TIEMPO", "span : " + name);

                if(name.contains("14:")){
                    Log.i("TIEMPO", "SI SE PUEDE BUSCAR IGUALDAD");
                }else{
                    Log.i("TIEMPO", "NO ES POSIBLE");
                }
                //Log.i("TIEMPO", "TOPICLIST : " + topicList);

                /*
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
    }




}
