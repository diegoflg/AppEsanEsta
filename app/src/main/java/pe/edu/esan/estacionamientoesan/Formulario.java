package pe.edu.esan.estacionamientoesan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import java.util.Calendar;
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
    String dominio="@esan.edu.pe";

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


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                //Declaracion de numero entero que obtiene la posicion del item seleccionado
                int index = arg0.getSelectedItemPosition();

                //Se le da un valor al lenguaje
                if(index==0){

                    dominio="@esan.edu.pe";

                }

                if(index==1){

                    dominio="@ue.edu.pe";

                }

                Log.v("dominio",dominio);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Metodo cuando nada es seleccionado
            }
        });


        //String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Calendar diaI = Calendar.getInstance();
        SimpleDateFormat form = new SimpleDateFormat("dd:MM:yyyy");
        String date = form.format(diaI.getTime());

        fechadia=date;
        Log.v("fecha", "I: "+date);


        //Date diaF = new Date(diaI.getTimeInMillis() + 604800000L); //7*24*60*60*1000
        String f2 = form.format(new Date(diaI.getTimeInMillis() + 604800000L));
        Log.i("fecha", "F: " + f2);



        et1=(EditText)findViewById(R.id.editText);
        et2=(EditText)findViewById(R.id.editText2);
        et3=(EditText)findViewById(R.id.editText3);
        et4=(EditText)findViewById(R.id.editText4);
    }

    public void gogogo(View v){
        Random r = new Random();

        aNumber = 1000 + r.nextInt(9000-1000+1);
        Log.v("random", String.valueOf(aNumber));

        String usM = et1.getText().toString();




        if(dominio.equals("@ue.edu.pe")){
            if(usM.length()==8){
                try{
                    int usMN = Integer.parseInt(usM);
                    new CreateUser().execute();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "El correo solo debe contener numeros", Toast.LENGTH_LONG).show();
                }

            }else{
                Toast.makeText(this,"El correo ingresado es incorrecto", Toast.LENGTH_LONG).show();
            }
        }else if(dominio.equals("@esan.edu.pe")){
            try{
                int usMN = Integer.parseInt(usM);
                String usMn = String.valueOf(usMN);
                if(usMn.length()==7){
                    new CreateUser().execute();
                }else{
                    Toast.makeText(this,"El correo solo debe contener 7 digitos", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){

             //FALTA VALIDAR LOS SIGNOS
                new CreateUser().execute();


            }


        }




        String correo = String.valueOf(et1.getText())+dominio;
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
        email.m.setBody("Su codigo de verificacion es: " + String.valueOf(aNumber));
        email.m.set_to(recp);
        email.m.set_subject("Codigo de Verificacion");

        //email.execute();


        Intent i = new Intent(getApplicationContext(), MainActivity2Activity.class);
        //finish();
        startActivity(i);
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
                params.add(new BasicNameValuePair("correo", correo+dominio));
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formulario,menu);
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

            case R.id.regresar:
                finish();
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return true;
            default:return super.onOptionsItemSelected(item);

        }
        //noinspection SimplifiableIfStatement
    }




}
