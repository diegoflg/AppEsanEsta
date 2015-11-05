package pe.edu.esan.estacionamientoesan;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;


public class Feedback extends ActionBarActivity {


    EditText et1;
    Button b1, b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_feed);

        et1=(EditText)findViewById(R.id.etsug);
        b1=(Button)findViewById(R.id.button2);
        b2=(Button)findViewById(R.id.button3);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();


            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] recp = {"estacionamientos@esan.edu.pe"};


                SendEmailAsyncTask email = new SendEmailAsyncTask();
                //Correo y contrasena del correo que envia
                email.m = new Mail("estacionamientos@esan.edu.pe", "2345wertSDFG61");
                //Se le da valor al FROM del mensaje
                email.m.set_from("estacionamientos@esan.edu.pe");
                //Se le da valor al mensaje a enviar
                email.m.setBody(et1.getText().toString());
                //Se le da valor al "PARA" (a quien se le envia)
                email.m.set_to(recp);
                //Se le da titulo al mensaje
                email.m.set_subject("FeedBack");
                //Se ejecuta el asyncTask
                email.execute();

                finish();


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




}
