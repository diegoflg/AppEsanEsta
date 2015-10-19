package pe.edu.esan.estacionamientoesan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

/**
 * Created by educacionadistancia on 16/10/2015.
 */


public class Correo extends ActionBarActivity {
    String dominio="@esan.edu.pe";
    EditText etmail;
    int aNumber;
    String fechadia="";

    TextView tvCorreo;
    CheckBox cbTyC;
    Button btEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_correo);


        Spinner spinnermail = (Spinner) findViewById(R.id.spinnermail);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.correos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnermail.setAdapter(adapter);


        spinnermail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //Declaracion de numero entero que obtiene la posicion del item seleccionado
                int index = arg0.getSelectedItemPosition();
                //Se le da un valor al lenguaje
                if (index == 0) {
                    dominio = "@esan.edu.pe";
                }
                if (index == 1) {
                    dominio = "@ue.edu.pe";
                }
                Log.v("dominio", dominio);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Metodo cuando nada es seleccionado
            }
        });

        etmail=(EditText)findViewById(R.id.etmail);

        Calendar diaI = Calendar.getInstance();
        SimpleDateFormat form = new SimpleDateFormat("dd:MM:yyyy");
        String date = form.format(diaI.getTime());

        fechadia=date;
        Log.v("fecha", "I: "+date);

        //Date diaF = new Date(diaI.getTimeInMillis() + 604800000L); //7*24*60*60*1000
        String f2 = form.format(new Date(diaI.getTimeInMillis() + 604800000L));
        Log.i("fecha", "F: " + f2);

        //Esto es solo para determinar el tipo de fuente
        tvCorreo = (TextView)findViewById(R.id.tvCorreo);
        cbTyC = (CheckBox)findViewById(R.id.cbTyC);
        btEnviar = (Button)findViewById(R.id.btEnviar);
        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "font/HelveticaNeue-Light.ttf");
        tvCorreo.setTypeface(font);

        etmail.setTypeface(font);
        cbTyC.setTypeface(font);
        btEnviar.setTypeface(font);


    }

    public void enviaCodigo(View v){
        Random r = new Random();

        aNumber = 1000 + r.nextInt(9000-1000+1);
        Log.v("random", String.valueOf(aNumber));

        String usM = etmail.getText().toString();

        if(dominio.equals("@ue.edu.pe")){
            if(usM.length()==8){
                try{
                    int usMN = Integer.parseInt(usM);

                    Intent i = new Intent(getApplicationContext(), Datos.class);

                    Bundle b = new Bundle();
                    b.putString("email", usM);
                    b.putString("codigo", String.valueOf(aNumber));
                    b.putString("fecha", fechadia);
                    i.putExtras(b);

                    startActivity(i);

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

                    Intent i = new Intent(getApplicationContext(), Datos.class);
                    startActivity(i);

                }else{
                    Toast.makeText(this,"El correo solo debe contener 7 digitos", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){

                //FALTA VALIDAR LOS SIGNOS
                Intent i = new Intent(getApplicationContext(), Datos.class);
                startActivity(i);
            }
        }


        String correo = String.valueOf(etmail.getText())+dominio;
        String[] recp = {correo};
        SendEmailAsyncTask email = new SendEmailAsyncTask();
        email.m = new Mail("educacionadistancia@esan.edu.pe", "rthj6724");
        email.m.set_from("educacionadistancia@esan.edu.pe");
        email.m.setBody("Su codigo de verificacion es: " + String.valueOf(aNumber));
        email.m.set_to(recp);
        email.m.set_subject("Codigo de Verificacion");
        email.execute();

        //Intent i = new Intent(getApplicationContext(), MainActivity2Activity.class);
        //startActivity(i);
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


}
