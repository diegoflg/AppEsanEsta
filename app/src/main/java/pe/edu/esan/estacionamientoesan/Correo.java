package pe.edu.esan.estacionamientoesan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

/**
 * Created by educacionadistancia on 16/10/2015.
 */


public class Correo extends ActionBarActivity {
    /*Declaracion de variables generales de la actividad*/

    //Cadena de texto con valor inicial
    String dominio="@esan.edu.pe";
    //Dialogo de progreso privado (solo existe en esta actividad)
    private ProgressDialog pDialog;
    //Cuadro de texto editable(donde el usuario entra el valor de su correo electronico)
    EditText etmail;
    //Entero(para generar el numero random de 4 digitos)
    int aNumber;
    //Fecha actual en la que se registra el usuario
    String fechadia="";
    //Cuadros de texto no editables
    TextView tvCorreo, textCB;
    //Boton enviar
    Button btEnviar;
    //Checkbox de terminos y condiciones
    CheckBox cbTyC;
    //Creacion de variable de tipo JSON
    JSONParser jsonParser = new JSONParser();
    //Cadena de texto cuyo valor asignado es el url del php
    private static String url_all_empresas = "http://www.estacionamientoesan.net76.net/essconnect/get_fechas.php";
    //Cadena de texto que verifica si ha sido correcto el login
    private static final String TAG_SUCCESS = "success";
    //Numero entero con valor inicial 3
    int success=3;
    /*Fin de declaración de variables generales */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Se le asigna la vista de layout correspondiente
        setContentView(R.layout.lay_correo);

        //Se crea un spinner y se le da el valor correspondiente al layout
        Spinner spinnermail = (Spinner) findViewById(R.id.spinnermail);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.correos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnermail.setAdapter(adapter);

        //Metodo que se activa cuando se da click al spinner dependiendo del valor
        spinnermail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //Declaracion de numero entero que obtiene la posicion del item seleccionado
                int index = arg0.getSelectedItemPosition();
                //Se le da un valor al dominio
                if (index == 0) {
                    dominio = "@esan.edu.pe";
                }
                if (index == 1) {
                    dominio = "@ue.edu.pe";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Metodo cuando nada es seleccionado
            }
        });

        //Se da valor al cuadro de texto con el id del elemento correspondiente del layout
        etmail = (EditText) findViewById(R.id.etmail);

        //Se crea un dato de tipo calendario
        Calendar diaI = Calendar.getInstance();
        //Se crea un formato para fecha
        SimpleDateFormat form = new SimpleDateFormat("dd:MM:yyyy");
        //Se crea una cadena y se le asigna un valor al que se le da un formato
        String date = form.format(diaI.getTime());

        //Se le asigna el valor de la cadena al dato inicial
        fechadia = date;

        //Date diaF = new Date(diaI.getTimeInMillis() + 604800000L); //7*24*60*60*1000
        String f2 = form.format(new Date(diaI.getTimeInMillis() + 604800000L));

        //Se le da valor al checkbox con el id correspondiente en el layout
        cbTyC = (CheckBox)findViewById(R.id.cbTyC);

        /*Esto es solo para determinar el tipo de fuente*/
        //Se les asigna el id correspondiente a los elementos segun el layout
        tvCorreo = (TextView) findViewById(R.id.tvCorreo);
        textCB = (TextView) findViewById(R.id.textCB);
        btEnviar = (Button) findViewById(R.id.btEnviar);

        //Se crean la fuentes dandoles la fuentes que se encuentran en la carpeta assets/font
        Typeface fontBold = Typeface.createFromAsset(getApplicationContext().getAssets(), "font/HelveticaNeue-Bold.ttf");
        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "font/HelveticaNeue-Light.ttf");

        //Se les da el tipo de fuente a los elementos
        tvCorreo.setTypeface(fontBold);
        etmail.setTypeface(font);
        textCB.setTypeface(fontBold);
        btEnviar.setTypeface(fontBold);
        /* Fin de aplicacion de fuentes*/

        //Metodo que se activa cuando se da click al texto de Aceptar terminos y condiciones
        textCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se llama al metodo que muestra un popup
                showPopup(Correo.this);
            }
        });

        //Metodo que se activa cuando se da click al boton enviar
        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se llama al metodo que manda el correo al usuario recien creado para luego abrir la otra pantalla

                if (isNetworkAvailable() == false) {
                    Toast.makeText(Correo.this, "Compruebe su conexión a internet", Toast.LENGTH_LONG).show();

                }else{
                    if(etmail.length()==0){

                        Toast.makeText(Correo.this,"Ingrese un correo valido", Toast.LENGTH_LONG).show();


                    }else{
                        new CreateUser3().execute();
                    }


                }

            }
        });
    }

    //Metodo que permite mostrar el PopUp en pantalla(segun el parametro que le sea dado)
    private void showPopup(final Activity context) {
        //Variables que permiten obtener las medidas de la pantalla del celular
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        double width = displaymetrics.widthPixels;

        double popupHeight = height*0.8;
        double popupWidth = width*0.92;

        //Asignacion de la vista de layout
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup3);
        //Se infla el layout
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Se da valor a la vista inflada
        View layout = layoutInflater.inflate(R.layout.popup3, viewGroup);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth((int) Math.round(popupWidth));
        popup.setHeight((int) Math.round(popupHeight));
        popup.setFocusable(true);
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
                //Desaparece el PopUp
                popup.dismiss();
                //Se da check automatico al checkbox por haber aceptado los terminos y condiciones
                cbTyC.setChecked(true);
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

    class CreateUser3 extends AsyncTask<String, String, String> {//Metodo que guarda el estado en la base de datos

        @Override
        protected void onPreExecute() {
            //Metodo antes de ser ejecutada la accion
            super.onPreExecute();

            pDialog = new ProgressDialog(Correo.this);
            pDialog.setMessage("Enviando Correo...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            //Metodo que se hace en segundo plano

            // TODO Auto-generated method stub
            // Check for success tag

            String username3 = etmail.getText().toString()+dominio;
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {

            pDialog.dismiss();

            if(success==0){

                if(cbTyC.isChecked()){
                    Random r = new Random();

                    aNumber = 1000 + r.nextInt(9000-1000+1);

                    String usM = etmail.getText().toString();

                    if(dominio.equals("@ue.edu.pe")){
                        if(usM.length()==8){
                            try{
                                int usMN = Integer.parseInt(usM);



                                Intent i = new Intent(getApplicationContext(), Datos.class);

                                Bundle b = new Bundle();
                                b.putString("email", usM+dominio);
                                b.putString("codigo", String.valueOf(aNumber));
                                b.putString("fecha", fechadia);
                                i.putExtras(b);
                                startActivity(i);

                                String correo = String.valueOf(etmail.getText())+dominio;
                                String[] recp = {correo};
                                SendEmailAsyncTask email = new SendEmailAsyncTask();
                                email.m = new Mail("educacionadistancia@esan.edu.pe", "rthj6724");
                                email.m.set_from("educacionadistancia@esan.edu.pe");
                                email.m.setBody("Su codigo de verificacion es: " + String.valueOf(aNumber));
                                email.m.set_to(recp);
                                email.m.set_subject("Codigo de Verificacion");
                                email.execute();
                                finish();

                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(Correo.this,R.string.solonum, Toast.LENGTH_LONG).show();
                            }

                        }else{
                            Toast.makeText(Correo.this,R.string.incorrecto, Toast.LENGTH_LONG).show();
                        }


                    }else if(dominio.equals("@esan.edu.pe")){

                        Intent i = new Intent(getApplicationContext(), Datos.class);

                        Bundle b = new Bundle();
                        b.putString("email", usM+dominio);
                        b.putString("codigo", String.valueOf(aNumber));
                        b.putString("fecha", fechadia);
                        i.putExtras(b);
                        startActivity(i);

                        String correo = String.valueOf(etmail.getText())+dominio;
                        String[] recp = {correo};
                        SendEmailAsyncTask email = new SendEmailAsyncTask();
                        email.m = new Mail("educacionadistancia@esan.edu.pe", "rthj6724");
                        email.m.set_from("educacionadistancia@esan.edu.pe");
                        email.m.setBody("Su codigo de verificacion es: " + String.valueOf(aNumber));
                        email.m.set_to(recp);
                        email.m.set_subject("Codigo de Verificacion");
                        email.execute();



                        finish();

                    }
                }else{
                    Toast.makeText(Correo.this,R.string.debeAceptarTC , Toast.LENGTH_LONG).show();
                }

            }else{
                Toast.makeText(Correo.this, R.string.existe , Toast.LENGTH_LONG).show();

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