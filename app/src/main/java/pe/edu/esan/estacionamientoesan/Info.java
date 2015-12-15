package pe.edu.esan.estacionamientoesan;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class Info extends ActionBarActivity {
    //Se crea una bariable boton privada
    private Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Se le asigna el contenido del layout a la actividad
        setContentView(R.layout.info_activity);
        //Se le da valor al boton con el id del elemento correspondiente en el layout
        b1=(Button)findViewById(R.id.button2);
        //Metodo que se activa cuando se da click al boton
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //La actividad Info se termina y regresa a la anterior
               finish();
            }
        });
    }
}
