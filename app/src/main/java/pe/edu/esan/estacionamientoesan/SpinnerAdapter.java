package pe.edu.esan.estacionamientoesan;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Este es el adaptador de la lista que va en el login para seleccionar el idioma
 */

public class SpinnerAdapter extends ArrayAdapter<ItemData> {

    int groupid;
    Activity context;
    ArrayList<ItemData> list;
    LayoutInflater inflater;

    //PARA LA FUENTE DE LA LISTA DEL SPINNER
    Typeface TF = Typeface.createFromAsset(getContext().getAssets(), "font/HelveticaNeue-Light.ttf");


    public SpinnerAdapter(Activity context, int groupid, int id, ArrayList<ItemData> list){
        super(context,id,list);
        this.list=list;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid=groupid;
    }

    public View getView(int position, View convertView, ViewGroup parent ){
        View itemView=inflater.inflate(groupid,parent,false);
        //Se obtienen los elementos a traves del id dados en el layout de la clase
        ImageView imageView=(ImageView)itemView.findViewById(R.id.img);

        //Se da una imagen al ImageView segun su posicion
        imageView.setImageResource(list.get(position).getImageId());

        //Se obtienen los elementos a traves del id dados en el layout de la clase
        TextView textView=(TextView)itemView.findViewById(R.id.txt);

        //Se da un texto como valor segun su posicion
        textView.setText(list.get(position).getText());

        //Se da el estilo de la fuente
        textView.setTypeface(TF);

        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent){
        //TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        //view.setTypeface(TF);
        return getView(position,convertView,parent);
    }


}

