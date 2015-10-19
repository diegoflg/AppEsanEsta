package pe.edu.esan.estacionamientoesan;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    private Button b1,b2;
    private EditText et1;
    private TextView tv1;
    private ProgressDialog pDialog;
    int success=3;
    private static String url_all_empresas = "http://www.estacionamientoesan.site88.net/esconnect/get_fechas.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "registros";
    private static final String TAG_NOMBRE = "password";
    private static final String TAG_MESSAGE = "message";
    JSONArray products = null;
    JSONParser jsonParser = new JSONParser();
    String contra="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvido);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        b1=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button2);
        tv1=(TextView)findViewById(R.id.textView2);
        et1=(EditText)findViewById(R.id.et2);
        et1.setText("12100250@ue.edu.pe");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateUser3().execute();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }


    class CreateUser3 extends AsyncTask<String, String, String> {//Metodo que guarda el estado en la base de datos


        @Override
        protected void onPreExecute() {
            //Metodo antes de ser ejecutada la accion


            super.onPreExecute();

            pDialog = new ProgressDialog(Olvido.this);
            pDialog.setMessage("Enviando Correo...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected String doInBackground(String... args) {
            //Metodo que se hace en segundo plano

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

            if (success == 1) {


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

    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPostExecute(Boolean aBoolean) {
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


}
