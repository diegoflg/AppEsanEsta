package pe.edu.esan.estacionamientoesan;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;


public class Olvido extends ActionBarActivity {
    /*Declaracion de variables generales*/
    //Se crea una variable boton privada
    private Button b1,b2;
    //Se crea una variable de cuadro de texto editable
    private EditText et1;
    //Se crea una variable de cuadro de texto no editable
    private TextView tv1;
    //Se crea una variablde dialogo de progreso
    private ProgressDialog pDialog;
    //Se crea una variable de numero entero cuyo valor se le asigna a 3
    int success=3;
    //Se crea una cadena de texto cuyo valor es el URL del php
    private static String url_all_empresas = "http://www.estacionamientoesan.net76.net/essconnect/get_fechas.php";
    //Se crean varias cadenas de texto
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "registros";
    private static final String TAG_NOMBRE = "password";
    private static final String TAG_MESSAGE = "message";
    //Se crea una variable de tipo JSONArray cuyo valor inicial es nulo
    JSONArray products = null;
    //Se crea una variable nueva de JSONParser
    JSONParser jsonParser = new JSONParser();
    //Se crea una variable de cadena de texto cuyo valor inicial es nulo
    String contra="";
    /*Fin de declaracion de variables generales*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Se le asigna el layout a la actividad
        setContentView(R.layout.activity_olvido);
        //Se esconde el Teclado del celular
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //Se da el valor de id a las variables declaradas anteriormente con el id del layout respectivo
        b1=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button2);
        tv1=(TextView)findViewById(R.id.textView2);
        et1=(EditText)findViewById(R.id.et2);

        //Metodo que se activa al hacer click en el boton enviar
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se llama a la accion del mismo nombre


                if (isNetworkAvailable() == false) {
                    Toast.makeText(Olvido.this, "Compruebe su conexión a internet", Toast.LENGTH_LONG).show();

                }else{

                    if(et1.length()<0){
                        new CreateUser3().execute();

                    }else{
                        Toast.makeText(Olvido.this, "Ingrese un correo válido", Toast.LENGTH_LONG).show();
                    }


                }
            }
        });

        //Metodo que se activa al hacer click en regresar
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cierra la actividad actual y retorna a la anterior abierta(MainActivity)
                finish();
            }
        });

    }


    //Clase que se ejecuta
    class CreateUser3 extends AsyncTask<String, String, String> {//Metodo que guarda el estado en la base de datos


        @Override
        protected void onPreExecute() {
            //Metodo antes de ser ejecutada la accion


            super.onPreExecute();

            //Se da valores al dialogo de progreso declarado inicialmente
            //Se le da el contexto
            pDialog = new ProgressDialog(Olvido.this);
            //Se le da el mensaje
            pDialog.setMessage("Enviando Correo...");
            //Se le da faso a su valor indeterminado
            pDialog.setIndeterminate(false);
            //Se le da falso a su valor cancelable
            pDialog.setCancelable(false);
            //Aparece el dialogo de progreso
            pDialog.show();


        }

        @Override
        protected String doInBackground(String... args) {
            //Metodo que se hace en segundo plano


            //Este metodo hara que se verifique el correo ingresado en el cuadro de texto y obtendra de la
            //base de datos la contraseña del usuario

            // TODO Auto-generated method stub
            // Check for success tag

            String username3 = et1.getText().toString();
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("correo", username3));
                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        url_all_empresas, "POST", params);

                // full json response
                Log.d("Registering attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {



                    try {

                            products = json.getJSONArray(TAG_PRODUCTS);
                            for (int i = 0; i < products.length(); i++) {
                                JSONObject c = products.getJSONObject(i);

                                contra=c.getString(TAG_NOMBRE);


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
        //Despues de ser ejecutada la accion
            if (success == 1) {
                //Este metodo va a  enviar un correo con la contraseña encontrada en la base de datos
                //al correo dado en el cuadro de texto. El remitente es: educacionadistancia@esan.edu.pe
                //Esto es a traves de la clase SendEmailAsyncTask que se ejecuta en segundo plano

                Log.v("contra", contra);
                String correo = et1.getText().toString();
                String[] recp = {correo};
                SendEmailAsyncTask email = new SendEmailAsyncTask();
                email.m = new Mail("educacionadistancia@esan.edu.pe", "rthj6724");

			/*
			 * try { email.m.addAttachment(
			 * "storage/sdcard/Pictures/CameraAPIDemo/CameraDemo_20131030093049.jpg"
			 * ); } catch (Exception e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
                email.m.set_from("educacionadistancia@esan.edu.pe");
                email.m.setBody("Su password es: " + contra);
                email.m.set_to(recp);
                email.m.set_subject("Recuperacion de password");

                email.execute();

            }else{
                pDialog.dismiss();
            }

            if (file_url != null){
                Toast.makeText(Olvido.this, file_url, Toast.LENGTH_LONG).show();
            }



        }
    }

    //Clase que permite el envio de correo
    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //Metodo anterior a la ejecucion de la accion
            super.onPostExecute(aBoolean);
            pDialog.dismiss();

            tv1.setText("Se envio la contraseña a el correo : " + et1.getText().toString() + "\n" + "Revise su correo para poder recuperar su contraseña");
            b1.setVisibility(View.GONE);
            b2.setText("Ok");
            et1.setVisibility(View.GONE);


        }

        Mail m;

        public SendEmailAsyncTask() {
            if (BuildConfig.DEBUG)
                Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //Metodo realizado en segundo plano
            if (BuildConfig.DEBUG)
                Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");
            try {
                m.send();
                return true;
            } catch (AuthenticationFailedException e) {
                Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
                e.printStackTrace();
                return false;
            } catch (MessagingException e) {
                Log.e(SendEmailAsyncTask.class.getName(), m.get_to() + "failed");
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
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
