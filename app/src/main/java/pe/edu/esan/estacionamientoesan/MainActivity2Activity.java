package pe.edu.esan.estacionamientoesan;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.media.AudioManager;
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

    //Button btEsan, btPolo, btAlonso, btir;

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

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.hifi);

        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

        //FUENTE Y COLOR PARA TEXTVIEWS
        String font_pathE = "font/HelveticaNeue-Roman.ttf"; //ruta de la fuente
        Typeface TFE = Typeface.createFromAsset(this.getAssets(), font_pathE);
        //llamanos a la CLASS TYPEFACE y la definimos con un CREATE desde ASSETS con la ruta STRING
        textViewestareg = (TextView) findViewById(R.id.textViewestareg);
        textViewestareg.setTypeface(TFE);


        String font_pathL = "font/HelveticaNeue-Light.ttf"; //ruta de la fuente
        Typeface TFL = Typeface.createFromAsset(this.getAssets(), font_pathL);
        /*
        btEsan = (Button) findViewById(R.id.btEsan);
        btAlonso = (Button) findViewById(R.id.btAlonso);
        btPolo = (Button) findViewById(R.id.btPolo);
        btir = (Button) findViewById(R.id.btir);
         */


        final Handler h = new Handler();
        final int delay = 5000; //milliseconds

        final Handler hT = new Handler();

        //Separador 1
        View sep1 = (View)findViewById(R.id.sep1);
        //Titulo Esan
        TextView tvEsan = (TextView) findViewById(R.id.textView);
        //Texto lugar esan
        TextView tvLEsan = (TextView) findViewById(R.id.textView7);
        //Boton Esan
        final Button btEsan = (Button)findViewById(R.id.btEsan);


        //Separador 2
        View sep2 = (View)findViewById(R.id.sep2);
        sep2.setVisibility(View.VISIBLE);
        //Titulo Polo
        TextView tvPolo = (TextView) findViewById(R.id.textView3);
        tvPolo.setVisibility(View.GONE);
        //Texto lugar  Polo
        TextView tvLPolo = (TextView) findViewById(R.id.textView6);
        tvLPolo.setVisibility(View.GONE);
        //Boton Polo
        final Button btPolo = (Button)findViewById(R.id.btPolo);
        btPolo.setVisibility(View.GONE);
        //Boton ir Polo
        Button btIr = (Button)findViewById(R.id.btir);
        btIr.setVisibility(View.GONE);

        //Separador 3
        View sep3 = (View)findViewById(R.id.sep3);
        sep3.setVisibility(View.GONE);
        //Titulo Alonso de Molina
        TextView tvAlonso = (TextView) findViewById(R.id.textView4);
        tvAlonso.setVisibility(View.GONE);
        //Texto lugar Alonso
        TextView tvLAlonso= (TextView) findViewById(R.id.textView5);
        tvLAlonso.setVisibility(View.GONE);
        //Boton Alonso
        final Button btAlonso = (Button)findViewById(R.id.btAlonso);
        btAlonso.setVisibility(View.GONE);

        //Separador 4
        View sep4 = (View)findViewById(R.id.sep4);
        sep4.setVisibility(View.GONE);

        dialog.show();
        new LoadAllProducts().execute();

        hT.postDelayed(new Runnable() {
            @Override
            public void run() {
                new LoadTIME2().execute();
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
                //new LoadTIME().execute();

                h.postDelayed(this, delay);

            }
        }, delay);


        btIr.setOnClickListener(new View.OnClickListener() {
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
        //Pagina web real: http://www.timeanddate.com/worldclock/peru/lima
        //HTML DE WEB: view-source:http://www.timeanddate.com/worldclock/peru/lima

        //Separador 1
        View sep1 = (View)findViewById(R.id.sep1);
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


        String url = "http://www.timeanddate.com/worldclock/peru/lima";
        Document doc = null;
        String horac;
        String diac;

        @Override
        protected void onPreExecute() {
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
            // TODO Auto-generated method stub
            try {
                //TIEMPO HH:MM
                doc = Jsoup.connect(url).get();

                Elements hora = doc.select("span[id=fshrmin]");
                horac = hora.text();

                Elements dia = doc.select("span[id=ctdat]");
                diac = dia.text();

                Log.i("TIEMPO", "Hora: " + horac);
                Log.i("TIEMPO", "Dia : " + diac);


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
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // Here you can do any UI operations like textview.setText("test");

            if((diac.contains("Monday") || diac.contains("Tuesday") || diac.contains("Wednesday") || diac.contains("Thursday") || diac.contains("Friday"))){
                if(horac.contains("18:") || horac.contains("19:") || horac.contains("20:")||
                   horac.contains("21:") || horac.contains("22:") || horac.contains("23:")){

                    //APARECE EL POLO
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

                }else {
                    /* Si fuera else if:
                    if(horac.contains("00:") || horac.contains("01:") || horac.contains("02:") ||
                         horac.contains("03:") || horac.contains("04:") || horac.contains("05:") ||
                         horac.contains("06:") || horac.contains("07:") || horac.contains("08:") ||
                         horac.contains("09:") || horac.contains("10:") || horac.contains("11:") ||
                         horac.contains("12:") || horac.contains("13:") || horac.contains("14:") ||
                         horac.contains("15:") || horac.contains("16:") || horac.contains("17:") )
                     */

                    //SE VA EL POLO
                        tvPolo.setVisibility(View.GONE);
                        tvLPolo.setVisibility(View.GONE);
                        btPolo.setVisibility(View.GONE);
                        btIr.setVisibility(View.GONE);
                        sep3.setVisibility(View.GONE);

                    //SE VA ALONSO
                    tvAlonso.setVisibility(View.GONE);
                    tvLAlonso.setVisibility(View.GONE);
                    btAlonso.setVisibility(View.GONE);
                    sep4.setVisibility(View.GONE);
                }
            } else if( diac.contains("Saturday")){
                if( horac.contains("06:3")|| horac.contains("06:4")|| horac.contains("06:5")||
                        horac.contains("07:") || horac.contains("08:") || horac.contains("09:") ||
                        horac.contains("10:") || horac.contains("11:") || horac.contains("12:") ||
                        horac.contains("13:") || horac.contains("14:") || horac.contains("15:") ||
                        horac.contains("16:") || horac.contains("17:") || horac.contains("18:")){

                    //APARECE EL POLO
                    tvPolo.setVisibility(View.VISIBLE);
                    tvLPolo.setVisibility(View.VISIBLE);
                    btPolo.setVisibility(View.VISIBLE);
                    btIr.setVisibility(View.VISIBLE);
                    sep3.setVisibility(View.VISIBLE);

                }else{
                    //SE VA EL POLO
                    tvPolo.setVisibility(View.GONE);
                    tvLPolo.setVisibility(View.GONE);
                    btPolo.setVisibility(View.GONE);
                    btIr.setVisibility(View.GONE);
                    sep3.setVisibility(View.GONE);
                }


                if( horac.contains("07:3")|| horac.contains("07:4")|| horac.contains("07:5")||
                        horac.contains("08:") || horac.contains("09:") || horac.contains("10:") ||
                        horac.contains("11:") || horac.contains("12:") || horac.contains("13:") ||
                        horac.contains("14:") || horac.contains("15:") || horac.contains("16:") ||
                        horac.contains("17:") || horac.contains("18:")){

                    //APARECE ALONSO
                    tvAlonso.setVisibility(View.VISIBLE);
                    tvLAlonso.setVisibility(View.VISIBLE);
                    btAlonso.setVisibility(View.VISIBLE);
                    sep4.setVisibility(View.VISIBLE);


                }else{
                    //SE VA ALONSO
                    tvAlonso.setVisibility(View.GONE);
                    tvLAlonso.setVisibility(View.GONE);
                    btAlonso.setVisibility(View.GONE);
                    sep4.setVisibility(View.GONE);
                }


            } else if(diac.contains("Sunday")){
                //SE VA EL POLO
                tvPolo.setVisibility(View.GONE);
                tvLPolo.setVisibility(View.GONE);
                btPolo.setVisibility(View.GONE);
                btIr.setVisibility(View.GONE);
                sep3.setVisibility(View.GONE);

                //SE VA ALONSO
                tvAlonso.setVisibility(View.GONE);
                tvLAlonso.setVisibility(View.GONE);
                btAlonso.setVisibility(View.GONE);
                sep4.setVisibility(View.GONE);
            }


        }
    }
}
