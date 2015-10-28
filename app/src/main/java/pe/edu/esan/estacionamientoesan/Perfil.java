package pe.edu.esan.estacionamientoesan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
 * Created by educacionadistancia on 20/10/2015.
 */
public class Perfil extends ActionBarActivity {
    /*Declaracion de variables generales de la actividad(algunas son PRIVATE, es decir solo existen y funcionan para la actividad actual)*/

    //Creacion de nueva variable del tipo JSONParser
    JSONParser jsonParser = new JSONParser();
    //Se crea una variable de arreglo de JSON cuyo valor inicial es nulo
    JSONArray products = null;

    //Se crea una cadena de texto cuyo valor es el URL del php para actualizar
    private static final String REGISTER_URL2 = "http://estacionamientoesan.pe.hu/cas/registroactu.php";
    //Se crea una cadena de texto cuyo valor es el URL del php para obtener
    private static String url_all_empresas = "http://estacionamientoesan.pe.hu/essconnect/get_datos.php";
    //Se crean cadenas de textos con valores
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "registros";
    private static final String TAG_MESSAGE = "message";
    //Se crean cadenas de textos cuyos valores son los campos que pertenecen a la base de datos
    private static final String TAG_Placa = "placa1";
    private static final String TAG_Placa2 = "placa2";
    private static final String TAG_Placa3 = "placa3";
    private static final String TAG_Contra = "password";
    private static final String TAG_Telf = "telefono";
    int success=3;

    //Se crean variables de cuadros de textos no editables
    TextView tvPerfil, tvPlaca, tvGuion, tvGuion2, tvGuion3 , tvContraseña, tvTelefono;
    //Se crean varialbes de cuadros de textos editables
    EditText etPlaca, etPlacaC, etPlaca2, etPlacaC2,etPlaca3, etPlacaC3, etContraseña, etTelefono;
    //Se crea una variable para un elemento de tipo Boton
    Button actualizar;
    //Se crea un dialogo de progreso
    private ProgressDialog pDialog;

    //Se crean cadenas de textos cuyos valores iniciales son numeros
    String varContra="", varPlaca11="", varPlaca12="", varTelf="", varPlaca21="", varPlaca22="", varPlaca31="", varPlaca32="";

    //Se crea una cadena de texto para el correo
    String correo;
    //Se crea una cadena de texto cuyo valor inicial es nulo
    String mensaje="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Se le asigna el layout respectivo a la actividad
        setContentView(R.layout.lay_perfil);

        //Se asignan los valores de id respectivos en el layout a los cuadros de textos no editables
        tvPerfil = (TextView) findViewById(R.id.tvPerfil);
        tvPlaca = (TextView) findViewById(R.id.tvPlaca);
        tvGuion = (TextView) findViewById(R.id.tvGuion);
        tvGuion2 = (TextView) findViewById(R.id.tvGuion2);
        tvGuion3 = (TextView) findViewById(R.id.tvGuion3);
        tvContraseña = (TextView) findViewById(R.id.tvContraseña);
        tvTelefono = (TextView) findViewById(R.id.tvTelefono);

        //Se asignan los valores de id respectivos en el layout a los cuadros de texto editables
        etPlaca = (EditText) findViewById(R.id.etPlaca);
        etPlacaC = (EditText) findViewById(R.id.etPlacaC);
        etPlaca2 = (EditText) findViewById(R.id.etPlaca2);
        etPlacaC2 = (EditText) findViewById(R.id.etPlacaC2);
        etPlaca3 = (EditText) findViewById(R.id.etPlaca3);
        etPlacaC3 = (EditText) findViewById(R.id.etPlacaC3);
        etContraseña = (EditText) findViewById(R.id.etContraseña);
        etTelefono = (EditText) findViewById(R.id.etTelefono);

        //Se da el valor de id al boton segun el id dado en el layout respectivo
        actualizar = (Button) findViewById(R.id.actualizar);

        //Se crea el tipo de fuente dandole como valor de fuente el archivo que se encuentra en Assets/font
        Typeface fuente = Typeface.createFromAsset(getAssets(), "font/HelveticaNeue-Light.ttf");

        //Se les da el tipo de fuente a los cuadros de texto no editables
        tvPerfil.setTypeface(fuente);
        tvPlaca.setTypeface(fuente);
        tvGuion.setTypeface(fuente);
        tvGuion2.setTypeface(fuente);
        tvGuion3.setTypeface(fuente);
        tvContraseña.setTypeface(fuente);
        tvTelefono.setTypeface(fuente);

        //Se les da el tipo de fuente a los cuadros de texto editables
        etPlaca.setTypeface(fuente);
        etPlacaC.setTypeface(fuente);
        etPlaca2.setTypeface(fuente);
        etPlacaC2.setTypeface(fuente);
        etPlaca3.setTypeface(fuente);
        etPlacaC3.setTypeface(fuente);
        etContraseña.setTypeface(fuente);
        etTelefono.setTypeface(fuente);

        //Se les da filtro a los cuadros de textos editables correspondientes a las placas con un minimo y maximo de 3 valores
        etPlaca.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(3)});
        etPlacaC.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(3)});
        etPlaca2.setFilters(new InputFilter[] {new InputFilter.AllCaps(),new InputFilter.LengthFilter(3)});
        etPlacaC2.setFilters(new InputFilter[] {new InputFilter.AllCaps(),new InputFilter.LengthFilter(3)});
        etPlaca3.setFilters(new InputFilter[] {new InputFilter.AllCaps(),new InputFilter.LengthFilter(3)});
        etPlacaC3.setFilters(new InputFilter[] {new InputFilter.AllCaps(),new InputFilter.LengthFilter(3)});


        //Metodo que se activa cuando el texto es cambiado o agregado en el cuadro
        etPlaca.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Metodo antes del cambio
                if (etPlaca.getText().toString().length() == 3)     //size as per your requirement
                {

                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Metodo durante el cambio

                if (etPlaca.getText().toString().length() == 3)     //size as per your requirement
                {
                    //Cuando el tamaño de la cadena de texto ingresada sea de 3 digitos o valores entonces
                    //el cursor pasara al segundo cuadro de texto editable para la continuacion de la placa
                    etPlacaC.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Metodo despues del cambio
                if (etPlaca.getText().toString().length() == 3)     //size as per your requirement
                {

                }

            }
        });


        //Metodo que se activa cuando el texto es cambiado o agregado en el cuadro
        etPlacaC.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Metodo antes del cambio
                if (etPlacaC.getText().toString().length() == 3)     //size as per your requirement
                {

                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Metodo durante el cambio
                if (etPlacaC.getText().toString().length() == 3)     //size as per your requirement
                {
                    //Cuando el tamaño de la cadena de texto ingresada sea de 3 digitos o valores entonces
                    //el cursor pasara al segundo cuadro de texto editable para la continuacion de la placa
                    etPlaca2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Metodo despues del cambio
                if (etPlacaC.getText().toString().length() == 3)     //size as per your requirement
                {

                }

            }
        });


        //Metodo que se activa cuando el texto es cambiado o agregado en el cuadro
        etPlaca2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Metodo antes del cambio
                if (etPlaca2.getText().toString().length() == 3)     //size as per your requirement
                {

                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Metodo durante el cambio
                if (etPlaca2.getText().toString().length() == 3)     //size as per your requirement
                {
                    //Cuando el tamaño de la cadena de texto ingresada sea de 3 digitos o valores entonces
                    //el cursor pasara al segundo cuadro de texto editable para la continuacion de la placa
                    etPlacaC2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Metodo despues del cambio
                if (etPlaca2.getText().toString().length() == 3)     //size as per your requirement
                {

                }

            }
        });

        //Metodo que se activa cuando el texto es cambiado o agregado en el cuadro
        etPlacaC2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Metodo antes del cambio
                if (etPlacaC2.getText().toString().length() == 3)     //size as per your requirement
                {

                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Metodo durante el cambio
                if (etPlacaC2.getText().toString().length() == 3)     //size as per your requirement
                {
                    //Cuando el tamaño de la cadena de texto ingresada sea de 3 digitos o valores entonces
                    //el cursor pasara al segundo cuadro de texto editable para la continuacion de la placa
                    etPlaca3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Metodo despues del cambio
                if (etPlacaC2.getText().toString().length() == 3)     //size as per your requirement
                {

                }

            }
        });

        //Metodo que se activa cuando el texto es cambiado o agregado en el cuadro
        etPlaca3.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Metodo antes del cambio
                if (etPlaca3.getText().toString().length() == 3)     //size as per your requirement
                {

                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Metodo durante el cambio

                if (etPlaca3.getText().toString().length() == 3)     //size as per your requirement
                {
                    //Cuando el tamaño de la cadena de texto ingresada sea de 3 digitos o valores entonces
                    //el cursor pasara al segundo cuadro de texto editable para la continuacion de la placa
                    etPlacaC3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Metodo despues del cambio
                if (etPlaca3.getText().toString().length() == 3)     //size as per your requirement
                {

                }

            }
        });

        //Metodo que se activa cuando el texto es cambiado o agregado en el cuadro
        etPlacaC3.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Metodo antes del cambio
                if (etPlacaC3.getText().toString().length() == 3)     //size as per your requirement
                {

                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Metodo durante el cambio

                if (etPlacaC3.getText().toString().length() == 3)     //size as per your requirement
                {
                    //Cuando el tamaño de la cadena de texto ingresada sea de 3 digitos o valores entonces
                    //el cursor pasara al segundo cuadro de texto editable para la continuacion de la placa
                    etContraseña.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Metodo despues del cambio
                if (etPlacaC3.getText().toString().length() == 3)     //size as per your requirement
                {

                }

            }
        });




        //Se crea un intento que se obtiene del cambio de actividad
        Intent p = getIntent();
        //Se obtiene el paquete de datos
            Bundle b = p.getExtras();
        //Se da valor a la cadena con el dato obtenido del cambio de actividad
            correo = b.getString("correo");

        //Se ejecuta la accion del mismo nombre
        new CreateUser3().execute();



        //Metodo que se da al dar click al boton actualizar
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable() == false) {
                    Toast.makeText(Perfil.this, R.string.compP, Toast.LENGTH_LONG).show();

                }else{

                    //Se da valor inicial nulo a la cadena de texto
                    mensaje = "";

                    // new CreateUser().execute();

                /*Se verifican datos*/
                    if(etPlaca.length()==0 && etPlacaC.length()==0 && etPlaca2.length()==0 && etPlacaC2.length()==0 && etPlaca3.length()==0 && etPlacaC3.length()==0 )
                    {
                        mensaje = mensaje + R.string.debePlaca + "\n";
                    }else{

                        if(etPlaca.length()!=0 && etPlacaC.length()!=0){
                            if (etPlaca.length() != 3 || etPlacaC.length() != 3) {
                                mensaje = mensaje + R.string.placaInc + "\n";

                            }


                        }

                        if(etPlaca2.length()!=0 && etPlacaC2.length()!=0){

                            if (etPlaca2.length() != 3 || etPlacaC2.length() != 3) {
                                mensaje = mensaje + R.string.placaInc + "\n";

                            }

                        }


                        if(etPlaca3.length()!=0 && etPlacaC3.length()!=0){
                            if (etPlaca3.length() != 3 || etPlacaC3.length() != 3) {
                                mensaje = mensaje + R.string.placaInc + "\n";

                            }


                        }


                    }




                    if (etTelefono.length() != 7 && etTelefono.length() != 9) {
                        mensaje = mensaje + R.string.telInc + "\n";

                    }
                    if (etContraseña.length() == 0) {
                        mensaje = mensaje + R.string.ingContra + "\n";

                    }


                    Log.v("qwerty", mensaje);
                /*Se termina la verificacion de datos*/

                    //Se verifica el contenido de la cadena de texto, si es nula entonces
                    //realizara la accion dada
                    if (mensaje.equals("")) {
                        new CreateUser2().execute();

                    } else {
                        //Caso contrario el usuario vera un mensaje en pantalla sobre los datos faltantes o incorrectos
                        AlertDialog.Builder builder = new AlertDialog.Builder(Perfil.this);
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


                }




            }
        });

    }

    //Clase que se ejecuta en segundo plano que permite coger los datos ingresados del usuario a la base de datos
    class CreateUser3 extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            //Metodo antes de ser ejecutada la accion


            super.onPreExecute();

            //Se crea un nuevo dialogo de progreso en la actividad
            pDialog = new ProgressDialog(Perfil.this);
            //Se le asigna un mensaje al dialogo de progreso
            pDialog.setMessage("Cargando datos...");
            //Se le da valor falso al indeterminado
            pDialog.setIndeterminate(false);
            //Se le da valor falso al cancelable
            pDialog.setCancelable(false);
            //Aparece el dialogo de progreso
            pDialog.show();


        }

        @Override
        protected String doInBackground(String... args) {
            //Metodo que se hace en segundo plano

            // TODO Auto-generated method stub
            // Check for success tag

            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("correo", correo));
                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        url_all_empresas, "POST", params);

                // full json response
                Log.d("Registering attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {

                    //Intenta...
                    try {
                        //Obtiene los datos
                        products = json.getJSONArray(TAG_PRODUCTS);
                        for (int i = 0; i < products.length(); i++) {
                            JSONObject c = products.getJSONObject(i);

                            //etPlaca2.setText(c.getString(TAG_Placa2));
                            //etPlaca3.setText(c.getString(TAG_Placa3));
                            varContra=c.getString(TAG_Contra);
                            varTelf=c.getString(TAG_Telf);

                            //Intenta...(dentro del anterior intenta)
                            try{
                                varPlaca11=c.getString(TAG_Placa).substring(0, 3);
                                varPlaca12=c.getString(TAG_Placa).substring(3, 6);
                                varPlaca21=c.getString(TAG_Placa2).substring(0,3);
                                varPlaca22=c.getString(TAG_Placa2).substring(3,6);
                                varPlaca31=c.getString(TAG_Placa3).substring(0,3);
                                varPlaca32=c.getString(TAG_Placa3).substring(3,6);

                            }catch (Exception e){

                            }





                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("Correo enviado!", json.toString());
                    return json.getString(TAG_MESSAGE);


                }else{
                    Log.d("Registering Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
        //Metodo despues de ejecutada la accion
            //Manda los datos del usuario a los cuadros de textos
            etContraseña.setText(varContra);
            etTelefono.setText(varTelf);
            etPlaca.setText(varPlaca11);
            etPlacaC.setText(varPlaca12);
            etPlaca2.setText(varPlaca21);
            etPlacaC2.setText(varPlaca22);
            etPlaca3.setText(varPlaca31);
            etPlacaC3.setText(varPlaca32);

            //El dialogo de progreso desaparece
            pDialog.dismiss();


            if (file_url != null){
                Toast.makeText(Perfil.this, file_url, Toast.LENGTH_LONG).show();
            }



        }
    }

    //Clase realizada en segundo plano
    class CreateUser2 extends AsyncTask<String, String, String> {//Metodo que guarda el estado en la base de datos


        @Override
        protected void onPreExecute() {
            //Metodo antes de ser ejecutada la accion


            super.onPreExecute();
            //Se crea un nuevo dialogo de progreso en la actividad actual
            pDialog = new ProgressDialog(Perfil.this);
            //Se le da el mensaje al dialogo de progreso
            pDialog.setMessage("Creating User...");
            //Se le da falso al indeterminado
            pDialog.setIndeterminate(false);
            //Se le da valor falso al cancelable
            pDialog.setCancelable(true);
            //Aparece el dialogo de progreso
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            //Metodo que se hace en segundo plano

            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("correo", correo));
                params.add(new BasicNameValuePair("placa1", etPlaca.getText().toString()+etPlacaC.getText().toString()));
                params.add(new BasicNameValuePair("placa2", etPlaca2.getText().toString()+etPlacaC2.getText().toString()));
                params.add(new BasicNameValuePair("placa3", etPlaca3.getText().toString()+etPlacaC3.getText().toString()));
                params.add(new BasicNameValuePair("password", etContraseña.getText().toString()));
                params.add(new BasicNameValuePair("telefono", etTelefono.getText().toString()));


                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        REGISTER_URL2, "POST", params);

                // full json response
                Log.d("Registering attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.v("actualizacion correcta", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);
                }else{
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
            if (file_url != null){
                Toast.makeText(Perfil.this, file_url, Toast.LENGTH_LONG).show();
            }



        }
    }

    private boolean isNetworkAvailable() {
        //Verifica la conexion a internet
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
