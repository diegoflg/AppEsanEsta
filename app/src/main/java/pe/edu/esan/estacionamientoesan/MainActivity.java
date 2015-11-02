package pe.edu.esan.estacionamientoesan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
    Button start;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Se guarda el estado del fragmento actual
        super.onCreate(savedInstanceState);

        //Se obtiene la pantalla del movil y se oculta el teclado de la pantalla
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //Manda el contenido al fragmento con la vista del layout correspondiente
        setContentView(R.layout.activity_main);
        start = (Button) findViewById(R.id.button);

        //Obtiene el servicio de WiFi
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //Activa el WiFi
        wifi.setWifiEnabled(true);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            Intent i = new Intent(getApplicationContext(), MainActivity2Activity.class);
            startActivity(i);
            finish();

        }


    }


    public void logstart(View v){
        //No funcionaba antes el OnClick desde el layout porque faltaba ponerle el "View v" en el parentesis
        if(isNetworkAvailable()){
            showPopup(MainActivity.this);
        }else{
            Toast.makeText(MainActivity.this, "Verifique su conexión a internet", Toast.LENGTH_LONG).show();
        }


    }


    private boolean isNetworkAvailable() {
        //Verifica la conexion a internet
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




    private void showPopup(final Activity context) {
        //Variables que permiten obtener las medidas de la pantalla del celular
        //Nueva variable de medida
        DisplayMetrics displaymetrics = new DisplayMetrics();
        //Obtiene la medida general de la pantalla del celular del usuario
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        //Variable entera cuyo valor es el alto de la pantalla
        int height = displaymetrics.heightPixels;
        //Variable real cuyo valor es el ancho de la pantalla
        double width = displaymetrics.widthPixels;

        //Variable real cuyo valor es el alto por un numero
        double popupHeight = height*0.8;
        //Variable real cuyo valor es el ancho por un numero
        double popupWidth = width*0.92;

        //Asignacion de la vista de layout
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup3);
        //Se infla el layout
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Se da valor a la vista inflada
        View layout = layoutInflater.inflate(R.layout.popup3, viewGroup);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        //Se le da el layout vista al PopUp
        popup.setContentView(layout);
        //Se le asigna un ancho
        popup.setWidth((int) Math.round(popupWidth));
        //Se le asigna un alto
        popup.setHeight((int) Math.round(popupHeight));
        //Se enfoca el pop up
        popup.setFocusable(true);
        //Se prohibe el toque fuera del popup
        popup.setOutsideTouchable(false);

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        // Creacion de fuentes
        Typeface TVT = Typeface.createFromAsset(getAssets(),"font/HelveticaNeue-Bold.ttf" );
        Typeface TPP = Typeface.createFromAsset(getAssets(),"font/HelveticaNeue-Light.ttf" );

        // Getting a reference to Close button, and close the popup when clicked.
        //Se crean un cuadro de texto y dos botones y se les asigna su id correspondiente al layout
        TextView tvTCTit = (TextView)layout.findViewById(R.id.tvTCTit);
        Button aceptar = (Button) layout.findViewById(R.id.aceptar);
        Button cancelar = (Button) layout.findViewById(R.id.cancelar);

        //Se les da el tipo de fuente a los textos de los elementos
        tvTCTit.setTypeface(TVT);
        cancelar.setTypeface(TPP);
        aceptar.setTypeface(TPP);

        //Se crea y da valor al cuadro de texto que contiene los terminos y condiciones
        final TextView tv1 = (TextView) layout.findViewById(R.id.tvTCT);
        //Se le asigna el tipo de fuente al cuadro de texto
        tv1.setTypeface(TPP);
        //Se le da la cadena de texto declarada en Strings al cuadro
        tv1.setText(R.string.terminos);

        //Metodo que se activa cuando se da clic al boton aceptar
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.commit();
                //Desaparece el PopUp
                popup.dismiss();
                Intent i = new Intent(getApplicationContext(), MainActivity2Activity.class);
                startActivity(i);
                finish();



            }
        });

        //Metodo que se activa al dar click al boton cancelar
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se cierra el PopUp (en este caso no se da un check automatico)
                popup.dismiss();

            }
        });
    }
}