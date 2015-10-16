package pe.edu.esan.estacionamientoesan;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;


public class Formulario extends ActionBarActivity {


    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String REGISTER_URL = "http://www.estacionamientoesan.site88.net/cas2/registroesan.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private ProgressDialog pDialog;
    private static final String TAG_PRODUCTS = "registros";
    private static final String TAG_NOMBRE = "correo";
    JSONArray products = null;
    JSONParser jParser = new JSONParser();

    EditText et1,et2,et3,et4,et5,et6,et7;
    int aNumber;

    String fechadia="";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);


        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.correos, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        fechadia=date;
        Log.v("fecha",date);


        et1=(EditText)findViewById(R.id.editText);
        et2=(EditText)findViewById(R.id.editText2);
        et3=(EditText)findViewById(R.id.editText3);
        et4=(EditText)findViewById(R.id.editText4);






        Random r = new Random();

        aNumber = 1000 + r.nextInt(9000-1000+1);
        Log.v("random",String.valueOf(aNumber));


        String[] recp = { "diegoflg3@gmail.com","fiorela2496@gmail.com" };
        SendEmailAsyncTask email = new SendEmailAsyncTask();
        email.m = new Mail("diegoflg6", "ghostwhisperer");

			/*
			 * try { email.m.addAttachment(
			 * "storage/sdcard/Pictures/CameraAPIDemo/CameraDemo_20131030093049.jpg"
			 * ); } catch (Exception e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
        email.m.set_from("diegoflg6@gmail.com");
        email.m.setBody("Su codigo de verificacion es: " + String.valueOf(aNumber));
        email.m.set_to(recp);
        email.m.set_subject("Codigo de Verificacion");

        //email.execute();

    }

    public void gogogo(View v){
        new CreateUser().execute();
    }

    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
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


    class CreateUser extends AsyncTask<String, String, String> {//Metodo que guarda el estado en la base de datos


        @Override
        protected void onPreExecute() {
            //Metodo antes de ser ejecutada la accion


            super.onPreExecute();
            pDialog = new ProgressDialog(Formulario.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            //Metodo que se hace en segundo plano

            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String correo = String.valueOf(et1.getText());
            String password = String.valueOf(et2.getText());
            String placa1 = String.valueOf(et3.getText());
            String telefono = String.valueOf(et4.getText());
            String fecha = fechadia;
            String codigo = String.valueOf(aNumber);
            String estado = "false";
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("correo", correo));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("placa1", placa1));
                params.add(new BasicNameValuePair("telefono", telefono));
                params.add(new BasicNameValuePair("fecha", fecha));
                params.add(new BasicNameValuePair("codigo", codigo));
                params.add(new BasicNameValuePair("estado", estado));



                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        REGISTER_URL, "POST", params);

                // full json response
                Log.d("Registering attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("User Created!", json.toString());
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
                Toast.makeText(Formulario.this, file_url, Toast.LENGTH_LONG).show();
            }



        }
    }






}