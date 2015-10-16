package pe.edu.esan.estacionamientoesan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

public class MainActivity extends ActionBarActivity {
    //Declaracion de variables

    //Texto para que se vea en la consola para verificacion del codigo
    private final String TAG= "APP";

    //Cuadros de textos editables
    EditText et1,et2;

    //Cadena de texto que obtiene el lenguaje del celular
    String langloc=Locale.getDefault().getDisplayLanguage();

    //Numero entero que define el lenguaje inicial del app
    int langinicial=0;

    //Numero entero que define el lenguaje final
    int lang=0;

    //Boton del login para acceder al app
    Button botonacceder; //SOLO SE USA PARA CAMBIAR LA FUENTE

    //Cadena de texto que dice el resultado del loggin
    String loggresult="";

    //Cadena de texto que muestra como mensaje el valor dado
    String mensaje="Usuario o password invalida";

    //Entero que manda el tipo de usuario
    int tipo=1;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Se guarda el estado del fragmento actual
        super.onCreate(savedInstanceState);

        //Se obtiene la pantalla del movil y se oculta el teclado de la pantalla
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Manda el contenido al fragmento con la vista del layout correspondiente
        setContentView(R.layout.activity_main);

        //Obtiene el servicio de WiFi
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        //Activa el WiFi
        wifi.setWifiEnabled(true);






        Random r = new Random();

        int aNumber = 1000 + r.nextInt(9000-1000+1);
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





        if(langloc.equals("español")){
            //Da valor al entero determinado para el lenguaje inicial
            langinicial=0;

        }
        if(langloc.equals("English")){
            //Da valor al entero determinado para el lenguaje inicial
            langinicial=1;

        }
        if(langloc.equals("français")){
            //Da valor al entero determinado para el lenguaje inicial
            langinicial=2;

        }

        //Se crea una lista de datos
        ArrayList<ItemData> list=new ArrayList<>();

        //Dependiendo del lenguaje inicial se veran los casos
        switch(langinicial){
            case 0:
                //Si el lenguaje inicial es Español el texto de la lista dira
                list.add(new ItemData("Español",R.drawable.es));
                list.add(new ItemData("Ingles",R.drawable.um));
                list.add(new ItemData("Frances",R.drawable.fr));
                break;
            case 1:
                //Si el lenguaje inicial es Ingles el texto de la lista dira
                list.add(new ItemData("Spanish",R.drawable.es));
                list.add(new ItemData("English",R.drawable.um));
                list.add(new ItemData("French",R.drawable.fr));
                break;
            case 2:
                //Si el lenguaje inicial es Frances el texto de la lista dira
                list.add(new ItemData("Espagnol",R.drawable.es));
                list.add(new ItemData("Anglais",R.drawable.um));
                list.add(new ItemData("Français",R.drawable.fr));
                break;
        }


        //Se crea un Spinner o seleccionador de lista corta y se le da su valor como elemento del layout
        Spinner sp=(Spinner)findViewById(R.id.spinner);


        //Se crea un nuevo adaptador a partir de la clase SpinnerAdapter y se le da sus parametros
        SpinnerAdapter adapter=new SpinnerAdapter(this,
                R.layout.spinerlayout,R.id.txt,list);

        //Se le asigna un adaptador al spinner
        sp.setAdapter(adapter);


        //Cuando el Spinner es clickeado
        sp.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                //Declaracion de numero entero que obtiene la posicion del item seleccionado
                int index = arg0.getSelectedItemPosition();

                //Se le da un valor al lenguaje
                lang = index;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Metodo cuando nada es seleccionado
            }
        });

        //Declaracion de variable
        SpannableString s = new SpannableString("ESAN");
        s.setSpan(new TypefaceSpan("HelveticaNeue-Roman.ttf"), 0 , s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(s);

        //Creacion de variable cuyo valor se le asigna como el soporte de la barra de actividades
        android.support.v7.app.ActionBar abLogin = getSupportActionBar();

        //Texto
        CharSequence titulo = abLogin.getTitle().toString();

        //Declaracion de variables de cuadros de texto con sus elementos determinados en el layout
        et1 = (EditText) findViewById(R.id.et1);

        //Declaracion de variables de cuadros de texto con sus elementos determinados en el layout
        et2 = (EditText) findViewById(R.id.et2);


        botonacceder = (Button) findViewById(R.id.button); //SOLO ES USADO PARA LA FUENTE

        //Asingacion de valores de texto a los cuadros de texto
        et1.setText("1005831");

        //Asingacion de valores de texto a los cuadros de texto
        et2.setText("esan1520");


        //ESTO ES PARA LA FUENTE
        String font_path = "font/HelveticaNeue-Light.ttf"; //ruta de la fuente
        Typeface TF = Typeface.createFromAsset(getAssets(),font_path);//llamanos a la CLASS TYPEFACE y la definimos con un CREATE desde ASSETS con la ruta STRING

        //Se le asigna una fuente al cuadro de texto
        et1.setTypeface(TF);

        //Se le asigna una fuente al cuadro de texto
        et2.setTypeface(TF);

        //Se le asigna una fuente al texto del boton
        botonacceder.setTypeface(TF);

        //Se crea y da valor a una base de datos del tipo creado en la clase AdminBD
        AdminBD admin = new AdminBD(this, "BDESAN4", null, 1);

        //Se crea una variable que permite la escritura en la base de datos
        SQLiteDatabase bd = admin.getWritableDatabase();


        //Se crea un cursor para la base de datos
        Cursor fila1 = bd.rawQuery("select usuario from Persona", null);
        if (fila1.moveToFirst()) {



        } else {


            //Se declara una variable de texto y se le da un valor
            String usuario = "alumno";

            //Se declara una variable de texto y se le da un valor
            String password = "alumno";


            //Se da un primer registro de datos
            ContentValues registro = new ContentValues();
            registro.put("usuario", usuario);
            registro.put("password", password);

            //Se insertan los datos
            bd.insert("Persona", null, registro);



            //Se declara una variable de texto y se le da un valor
            String usuario2 = "alumnodos";

            //Se declara una variable de texto y se le da un valor
            String password2 = "alumnodos";

            //Se da un segundo registro de datos
            ContentValues registro2 = new ContentValues();
            registro2.put("usuario", usuario2);
            registro2.put("password", password2);

            //Se insertan los datos
            bd.insert("Persona", null, registro2);


            //Se declara una variable de texto y se le da un valor
            String usuario3 = "parking";

            //Se declara una variable de texto y se le da un valor
            String password3 = "esanmmxv";

            //Se da un tercer registro de datos
            ContentValues registro3 = new ContentValues();
            registro3.put("usuario", usuario3);
            registro3.put("password", password3);

            //Se insertan los datos
            bd.insert("Persona", null, registro3);


        }

        //Cerramos la base de datos
        bd.close();




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

    public void acceder(){
        //Metodo para acceder a la aplicacion

        //Se crean cadenas de texto
        String usuario, password,todo,us,pass;

        //Se les da valor a dos variables de texto obtiendo de lo que se de en el login
        us=et1.getText().toString();
        pass=et2.getText().toString();

        if(us.contains("@")){
            Log.i("ACCEDER", "EL USUARIO CONTIENE UN ARROBA");
        }else{
            Log.i("ACCEDER", "EL USUARIO NO CONTIENE UN ARROBA");
        }

        //Se crea una base de datos del tipo AdminBD
        AdminBD admin= new AdminBD(this, "BDESAN4",null, 1);

        //Se permite la escritura y lectura de la base de datos
        SQLiteDatabase bd=admin.getWritableDatabase();

        //Se declaran y dan valores a dos cadenas de texto
        String mensaje2="Incorrect user or password";
        String mensaje3="Mot de passe ou utilisateur incorrect";

        //Se crea un nuevo paquete de datos
        Bundle b=new Bundle();

        //Se crea un nuevo cursor y se le da su valor correspondiente
        Cursor c=bd.rawQuery("SELECT usuario,password FROM Persona",null);

        //Se le da valor vacio al texto declarado anteriormente
        todo="";

        if(c.moveToFirst())
        {
            do {
                //La variable obtiene el dato dado en el login
                usuario=c.getString(0);
                Log.v("usuario", usuario);

                //La variable obtiene el dato dado en el login
                password=c.getString(1);
                Log.v("password", password);

                if(us.equals(usuario) && pass.equals(password)) {

                    mensaje = "correcto";

                    //Verifica el tipo de usuario y da un valor al tipo
                    if (us.equals("alumno")) {
                        tipo = 1;
                    }
                    if (us.equals("parking")) {
                        tipo = 2;
                    }

                    //Guarda en el paquete de datos el tipo de usuario como dato
                    b.putInt("tipo", tipo);

                    Log.v("tipo", String.valueOf(tipo));

                    //Intenta abrir la segunda pantalla de la aplicacion
                    Intent i = new Intent(this, MainActivity2Activity.class);

                    //Envia los datos del paquete de datos a la nueva pantalla
                    i.putExtras(b);




                    //Obtiene la instancia del lenguaje
                    Datah.getInstance().setLang(lang);



                    //Segun el lenguaje
                    switch(lang){
                        case 0:
                            //Declaracion de variable Locale y asignacion de valor
                            Locale locale = new Locale("es");

                            //Se da valor determinado a la variable
                            Locale.setDefault(locale);

                            //Crea una nueva variable de configuracion
                            Configuration mConfig = new Configuration();

                            //Da valor a la variable segun la otra variable
                            mConfig.locale = locale;

                            //Obtiene el contexto, sus recursos, y actualiza la configuracion segun los parametros
                            getBaseContext().getResources().updateConfiguration(mConfig,
                                    getBaseContext().getResources().getDisplayMetrics());

                            break;

                        case 1:
                            //Declaracion de variable Locale y asignacion de valor
                            Locale locale2 = new Locale("en");

                            //Se da valor determinado a la variable
                            Locale.setDefault(locale2);

                            //Crea una nueva variable de configuracion
                            Configuration mConfig2 = new Configuration();

                            //Da valor a la variable segun la otra variable
                            mConfig2.locale = locale2;

                            //Obtiene el contexto, sus recursos, y actualiza la configuracion segun los parametros
                            getBaseContext().getResources().updateConfiguration(mConfig2,
                                    getBaseContext().getResources().getDisplayMetrics());

                            break;

                        case 2:
                            //Declaracion de variable Locale y asignacion de valor
                            Locale locale3 = new Locale("fr");

                            //Se da valor determinado a la variable
                            Locale.setDefault(locale3);

                            //Crea una nueva variable de configuracion
                            Configuration mConfig3 = new Configuration();

                            //Da valor a la variable segun la otra variable
                            mConfig3.locale = locale3;

                            //Obtiene el contexto, sus recursos, y actualiza la configuracion segun los parametros
                            getBaseContext().getResources().updateConfiguration(mConfig3,
                                    getBaseContext().getResources().getDisplayMetrics());

                            break;

                    }




                    //Empieza el intento de abrir la nueva pantalla
                    startActivity(i);

                    //Termina la actividad actual
                    finish();


                }

                //Da valor a la cadena de texto anteriormente declarada
                todo=todo+usuario+" " +password+" " + "\n";
            }while(c.moveToNext());


            if(mensaje.equals("correcto")){

            }else{


                //Dependiendo del idioma
                switch(lang){
                    case 0:
                        //Crea un mensaje en pantalla
                        Toast t=Toast.makeText(this,mensaje, Toast.LENGTH_SHORT);

                        //Muestra el mensaje corto
                        t.show();



                        break;

                    case 1:
                        //Crea un mensaje en pantalla
                        Toast y=Toast.makeText(this,mensaje2, Toast.LENGTH_SHORT);

                        //Muestra el mensaje corto
                        y.show();



                        break;

                    case 2:
                        //Crea un mensaje en pantalla
                        Toast u=Toast.makeText(this,mensaje3, Toast.LENGTH_SHORT);

                        //Muestra el mensaje corto
                        u.show();



                        break;







                }





            }




        }


        //Intent i = new Intent(this,MainActivity2Activity.class);
        //AstartActivity(i);

    }

    public void register(View v){


        Intent o = new Intent(getApplicationContext(), Formulario.class);
        startActivity(o);
    }


    public void logstart(View v){
        //Verifica la conexion a internet
        if(isNetworkAvailable()==true){
            //Obtiene los datos ingresados del Login
            Datah.getInstance().setUser(et1.getText().toString());
            Datah.getInstance().setPass(et2.getText().toString());

            //Ejecuta la clase del mismo nombre

            showPopup(this);


        }else{
            //Se crea y da valor a un mensaje corto en pantalla
            Toast t=Toast.makeText(this,"No hay coneccion a internet", Toast.LENGTH_SHORT);

            //Se muestra el mensaje en pantalla
            t.show();

        }





    }

    private class logg extends AsyncTask<String, Void, String> {
        //Se crea un da valor a un dialogo de progreso
        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "", "Please wait, Loading Page...", true);

        @Override
        protected void onPreExecute() {
            //Metodo anterior a la ejecucion de la clase
            super.onPreExecute();



        }

        @Override
        protected String doInBackground(String... params) {
            //Metodo que se desarrolla en segundo plano

            Log.v("v1", "paso2");

            try {
                //Se crea y da valor a una variable para la conexion a la pagina web dada
                Connection.Response res1 = Jsoup.connect("http://esanvirtual.edu.pe/login/index.php").method(Connection.Method.GET).timeout(0).execute();

                //Se crea y da valor a una variable del tipo documento
                Document doc = res1.parse();

                //Se crea y da valor a una variable del tipo Mapa
                Map welcomCookies = res1.cookies();


                //Se crea una variable de la pagina web y se le da valor
                Connection.Response res2 = Jsoup.connect("http://esanvirtual.edu.pe/login/index.php")
                        .data("username", et1.getText().toString())
                        .data("password", et2.getText().toString())
                        .cookies(welcomCookies)
                        .timeout(10000)
                        .method(Connection.Method.POST)
                        .execute();

                //Se crea una variable del tipo documento y se le da valor
                Document docl = res2.parse();
                Log.v("titulo", docl.title());

                //Se verifica el texto de la pagina 1
                if(docl.title().equals("ESANVIRTUAL")){
                    loggresult="si";
                    mensaje="correcto";

                }

                //Se verifica el texto de la pagina 2
                if(docl.title().equals("ESANVIRTUAL: Entrar al sitio")){
                    loggresult="no";

                }






            } catch (IOException e) {
                //Se imprime el error en la consola
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //Metodo despues de ejecutada la clase
            super.onPostExecute(s);

            //Desaparece el dialogo de progreso
            dialog.dismiss();



            if(loggresult.equals("si")){


                //Desaparece el dialogo de progreso
                dialog.dismiss();


                //Se crea una variable de paquete de datos
                Bundle b=new Bundle();

                //Se le asigna un valor a la variable mensaje
                mensaje = "correcto";


                //Se guarda un valor en el paquete de datos
                b.putInt("tipo", tipo);

                Log.v("tipo", String.valueOf(tipo));

                //Se crea y da valor a un intento de cambio de pantalla: pasar de una a otra pantalla
                Intent i = new Intent(getApplicationContext(), MainActivity2Activity.class);

                //Se le manda el paquete de datos a la segunda pantalla
                i.putExtras(b);




                //Se obtiene la instancia y se le asigna un valor
                Datah.getInstance().setLang(lang);




                switch(lang){
                    case 0:
                        //Se crea una nueva variable de tipo Locale
                        Locale locale = new Locale("es");

                        //Se le asigna valor
                        Locale.setDefault(locale);

                        //Se crea una variable de configuracion
                        Configuration mConfig = new Configuration();

                        //Se le asigna valor a la variable
                        mConfig.locale = locale;

                        //Se obtiene el contexto, sus recursos y se actualiza su configuracion con los parametros
                        getBaseContext().getResources().updateConfiguration(mConfig,
                                getBaseContext().getResources().getDisplayMetrics());

                        break;

                    case 1:
                        //Se crea una nueva variable de tipo Locale
                        Locale locale2 = new Locale("en");

                        //Se le asigna valor
                        Locale.setDefault(locale2);

                        //Se crea una variable de configuracion
                        Configuration mConfig2 = new Configuration();

                        //Se le asigna valor a la variable
                        mConfig2.locale = locale2;

                        //Se obtiene el contexto, sus recursos y se actualiza su configuracion con los parametros
                        getBaseContext().getResources().updateConfiguration(mConfig2,
                                getBaseContext().getResources().getDisplayMetrics());

                        break;

                    case 2:
                        //Se crea una nueva variable de tipo Locale
                        Locale locale3 = new Locale("fr");

                        //Se le asigna valor
                        Locale.setDefault(locale3);

                        //Se crea una variable de configuracion
                        Configuration mConfig3 = new Configuration();

                        //Se le asigna valor a la variable
                        mConfig3.locale = locale3;

                        //Se obtiene el contexto, sus recursos y se actualiza su configuracion con los parametros
                        getBaseContext().getResources().updateConfiguration(mConfig3,
                                getBaseContext().getResources().getDisplayMetrics());

                        break;

                }




                //Empieza el intent
                startActivity(i);

                //Termina la actividad actual
                finish();



            }else{
                //Inicia el proceso de acceder al app
                acceder();
            }







        }







    }
    private boolean isNetworkAvailable() {
        //Verifica la conexion a internet
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void showPopup(final Activity context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        double width = displaymetrics.widthPixels;

        Log.v("tamano",String.valueOf(height));
        Log.v("tamano",String.valueOf(width));

        double popupHeight = height*0.8;
        double popupWidth = width*0.92;



        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup3);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        // FUENTE PARA TEXTO EN POPUP Y BOTONES:
        String font_pathPP = "font/HelveticaNeue-Light.ttf"; //ruta de la fuente
        Typeface TPP = Typeface.createFromAsset(MainActivity.this.getAssets(),font_pathPP);//llamanos a la CLASS TYPEFACE y la definimos
        // con un CREATE desde ASSETS con la ruta STRING

        // Getting a reference to Close button, and close the popup when clicked.
        //Buton de Cancelar
        Button close = (Button) layout.findViewById(R.id.call3);
        //Boton Aceptar
        Button call = (Button) layout.findViewById(R.id.close3);
        call.setTypeface(TPP);
        close.setTypeface(TPP);


        final TextView tv1 = (TextView) layout.findViewById(R.id.pop3tv1);
        tv1.setTypeface(TPP);
        tv1.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin varius ullamcorper bibendum. Suspendisse blandit sem non arcu consectetur, a tempor turpis finibus. Cras volutpat vel ligula id varius. Suspendisse eu tortor convallis, accumsan mi vitae, viverra tortor. Donec nec gravida libero. Proin fringilla porttitor ornare. Vivamus tempor ut orci quis aliquam. Nam blandit dictum urna. Phasellus quis sem finibus, lacinia sapien eu, vestibulum elit. Praesent mauris lorem, eleifend varius turpis ac, egestas gravida nulla. Cras quis malesuada tellus. Nam mollis nibh sit amet felis aliquet luctus. Cras ut leo nec turpis volutpat iaculis at id dui. Fusce viverra sed nunc sit amet mollis. Maecenas quis velit leo. Phasellus dui felis, tristique et leo vel, cursus elementum arcu.\n" +
                "\n" +
                "Aenean vitae libero viverra, ultrices magna eget, pulvinar leo. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque malesuada lorem id libero convallis, sed vestibulum risus interdum. Ut quis lobortis felis. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Curabitur in mauris ultrices, sodales sapien eget, tempus urna. Ut volutpat purus non tellus interdum, eget elementum sapien molestie. Quisque finibus dui eget purus ultrices, id dapibus lacus scelerisque. Pellentesque a porttitor nulla. Sed sit amet nunc in nulla accumsan lobortis. In velit est, cursus sit amet pharetra quis, maximus sed erat. Nulla sed magna turpis. Praesent vestibulum eros sed sollicitudin vehicula. Mauris eget aliquam dolor, non interdum orci. Vestibulum semper ornare efficitur. Sed sit amet pharetra massa.\n" +
                "\n" +
                "Maecenas commodo nisi id quam tincidunt, vitae auctor purus dignissim. Donec sem purus, hendrerit ac lacus sit amet, gravida ullamcorper tellus. Donec pharetra, quam vel porta pharetra, felis massa pulvinar nunc, nec iaculis ante tortor vel felis. Maecenas id venenatis lacus. Sed accumsan mattis nisl sit amet posuere. Nulla elementum eget diam ut consequat. Maecenas id nulla purus. Vivamus et diam massa. Sed molestie arcu neque, vitae tincidunt risus egestas quis. Cras nulla dolor, accumsan et magna at, aliquet efficitur mauris.\n" +
                "\n" +
                "Duis tempus, quam eget cursus rutrum, arcu leo elementum purus, a porttitor est odio sit amet nulla. Proin nisl risus, semper vitae sem sed, consequat semper turpis. Nunc vel ultricies lectus. Nunc justo dolor, interdum sed fringilla finibus, tempor in magna. Vestibulum eleifend, arcu ut volutpat convallis, enim mauris condimentum mauris, non semper orci metus quis erat. Vestibulum lacus quam, convallis vitae fringilla eget, tristique a turpis. Etiam eget cursus leo, ac cursus lorem. Quisque tellus ligula, semper at pulvinar in, porttitor in ligula. Ut pulvinar ipsum non aliquam lobortis. Donec at consequat orci. Pellentesque tempor imperdiet lacus, dignissim pellentesque libero efficitur non. Donec ut interdum nisl. Nulla ac hendrerit justo. Quisque volutpat, nibh et sagittis dapibus, odio metus sollicitudin sapien, non dapibus ex ligula vel ex.");




        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                new logg().execute();

            }
        });


    }


}