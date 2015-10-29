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
    private static String url_all_empresas = "http://estacionamientos.esan.edu.pe/esconnect/get_fechas.php";
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
                //Se le da un valor al dominio según el indice obtenido
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

                //Se verifica la conexion a internet
                if (isNetworkAvailable() == false) {
                    //Si no hay conexion entonces saldra en pantalla un mensaje para que el ususario compruebe la conexion
                    Toast.makeText(Correo.this, R.string.compruebe, Toast.LENGTH_LONG).show();


                }else{
                    //Caso contrario se verificara el tamaño del dato ingresado en el cuadro de texto editable de correo
                    if(etmail.length()==0){
                        //Si esta vacion entoncesse mostrara un mensaje en pantalla en el que se pide que se ingrese
                        //un correo valido
                        AlertDialog.Builder builder = new AlertDialog.Builder(Correo.this);
                        builder.setMessage(R.string.cova)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();


                    }else{
                        //Caso contrario: si hay caracteres, se realizara el metodo del mismo nombre
                        new CreateUser3().execute();
                    }


                }

            }
        });
    }

    //Metodo que permite mostrar el PopUp en pantalla(segun el parametro que le sea dado)
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


    //Metodo que se realiza en segundo plano que verifica el envio de correo
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
            //Caso del menu en el que se regresa a la actividad anterior
            case R.id.regresar:
                //Se finaliza la actividad actual
                finish();
                //Sirve para que al cerrar la actividad se cierre tambien el teclado en pantalla
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

            //Se crea un nuevo dialogo de progreso en la actividad
            pDialog = new ProgressDialog(Correo.this);
            //Se le asigna un mensaje
            pDialog.setMessage("Enviando Correo...");
            //Se le da falso al indeterminado
            pDialog.setIndeterminate(false);
            //Se le da falso al cancelable
            pDialog.setCancelable(false);
            //Se muestra el dialogo de progreso
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            //Metodo que se hace en segundo plano

            // TODO Auto-generated method stub
            // Check for success tag

            //Obtiene el correo del usuario junto al dominio
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
            //Metodo despues de terminada la ejecucion
        //El dialogo de progreso desaparece
            pDialog.dismiss();

            //Se verifica el valor de la variable entera success
            if(success==0){
                //Se verifica si el cuadro de terminos y condiciones ha sido chequeado
                if(cbTyC.isChecked()){
                    //Se crea un random r
                    Random r = new Random();
                    //Se le asigna valor a la variable numerica aNumber
                    aNumber = 1000 + r.nextInt(9000-1000+1);

                    //Se le da valor a una cadena de texto llamada usM con el dato obtenido del correo  ingresado por el usuario
                    String usM = etmail.getText().toString();

                    //Se verifica el tipo de dominio escogido
                    if(dominio.equals("@ue.edu.pe")){
                        //Si el dominio es UE (pregrado) se verifica su longitud de texto del correo con 8 digitos
                        if(usM.length()==8){
                            //Si tiene 8 digitos se intenta
                            try{
                                //Convertir el dato a numeros para verificar que el correo ingresado solo tenga
                                //numeros puesto que los estudiantes de pregrado solo tienen 8 digitos numericos como correo esan
                                int usMN = Integer.parseInt(usM);

                                //Se intenta entonces pasar a la otra actividad que es el llenado de los otros datos
                                Intent i = new Intent(getApplicationContext(), Datos.class);

                                //Se crea un paquete de datos
                                Bundle b = new Bundle();
                                //Se da valores al paquete: el email del usuario, el codigo de verficiacion enviado a su corre y la fecha en la que se ha registrado
                                b.putString("email", usM+dominio);
                                b.putString("codigo", String.valueOf(aNumber));
                                b.putString("fecha", fechadia);
                                //Se mandan esos datos al intent
                                i.putExtras(b);
                                //se empieza la otra actividad con los datos enviados
                                startActivity(i);

                                //Se obtiene el correo del usuario junto al dominio escogido y se guarda en la variable cadena CORREO
                                //Comienzo de pasos para envio automatico de correo:
                                //Correo del usuario a enviar codigo
                                String correo = String.valueOf(etmail.getText())+dominio;
                                String[] recp = {correo};
                                //Se crea un nuevo asynctask de envio de correo
                                SendEmailAsyncTask email = new SendEmailAsyncTask();
                                //Correo y contrasena del correo que envia
                                email.m = new Mail("educacionadistancia@esan.edu.pe", "rthj6724");
                                //Se le da valor al FROM del mensaje
                                email.m.set_from("educacionadistancia@esan.edu.pe");
                                //Se le da valor al mensaje a enviar
                                email.m.setBody("Su codigo de verificacion es: " + String.valueOf(aNumber));
                                //Se le da valor al "PARA" (a quien se le envia)
                                email.m.set_to(recp);
                                //Se le da titulo al mensaje
                                email.m.set_subject("Codigo de Verificacion");
                                //Se ejecuta el asyncTask
                                email.execute();
                                //Se termina la actividad
                                finish();

                            }catch (Exception e){
                                //Caso en el que el correo ingresado obtenga alguna letra y no un numero
                                //Se le muestra al usuario un mensaje
                                e.printStackTrace();
                                Toast.makeText(Correo.this,R.string.solonum, Toast.LENGTH_LONG).show();
                            }

                        }else{
                            //Caso de ingresar correos con menos o mas de 8 digitos se muestra un mensaje al usuario
                            AlertDialog.Builder builder = new AlertDialog.Builder(Correo.this);
                            builder.setMessage(R.string.incorrecto)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        //Caso en el que el dominio del usuario es ESAN (Postgrado y otros menos pregrado)
                    }else if(dominio.equals("@esan.edu.pe")){

                        //Se crea un intento de paso a otra actividad de llenado de datos
                        Intent i = new Intent(getApplicationContext(), Datos.class);

                        //Se crea un paquete de datos
                        Bundle b = new Bundle();
                        //Se guardan los datos del correo ingresado, el codigo enviado y la fecha de registro
                        b.putString("email", usM+dominio);
                        b.putString("codigo", String.valueOf(aNumber));
                        b.putString("fecha", fechadia);
                        //Se manda el paquete con el intent
                        i.putExtras(b);
                        //Se empieza la actividad
                        startActivity(i);

                        //Se obtiene el correo del usuario
                        String correo = String.valueOf(etmail.getText())+dominio;
                        //Se crea la cadena tipo matriz con el correo
                        String[] recp = {correo};
                        //Se crea una nueva ejecucion de envio de mensaje
                        SendEmailAsyncTask email = new SendEmailAsyncTask();
                        //Se da el correo y contrasena que enviara el correo
                        email.m = new Mail("educacionadistancia@esan.edu.pe", "rthj6724");
                        //Se da valor al FROM del mensaje
                        email.m.set_from("educacionadistancia@esan.edu.pe");
                        //Se da valor al mensaje que se envie
                        email.m.setBody("Su codigo de verificacion es: " + String.valueOf(aNumber));
                        //Se da valor al TO del mensaje
                        email.m.set_to(recp);
                        //Se da valor al TITULO del mensaje
                        email.m.set_subject("Codigo de Verificacion");
                        //Se ejecuta el envio
                        email.execute();

                        //Se termina la actividad
                        finish();

                    }
                }else{

                    //Caso en que no este chequeado el terminos y condiciones aparecera en pantalla un mensaje que diga que los acepte
                    AlertDialog.Builder builder = new AlertDialog.Builder(Correo.this);
                    builder.setMessage(R.string.debeAceptarTC)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }else{
                //Caso en el que no se verifique el valor de success
                AlertDialog.Builder builder = new AlertDialog.Builder(Correo.this);
                builder.setMessage(R.string.existe)
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
    private boolean isNetworkAvailable() {
        //Verifica la conexion a internet
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}