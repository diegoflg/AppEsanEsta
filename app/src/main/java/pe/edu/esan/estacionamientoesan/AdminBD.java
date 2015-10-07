package pe.edu.esan.estacionamientoesan;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminBD extends SQLiteOpenHelper {

    /**
     * Base de datos en donde estan almacenadas las credenciales de login de el personal de estacionamiento, que en este caso son
     * usuario: parking
     * password:esanmmxv
     */

    String sqlCreate="CREATE TABLE Persona (usuario TEXT, password TEXT)";
    /**
     * Aqui se ejecuta la sentencia SQL
     */

    public AdminBD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){

        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(sqlCreate);//Se ejecuta la sentencia SQL
    }

    @Override//Override que se ejecuta en actualizacion
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXIST Persona");
        db.execSQL(sqlCreate);//Se ejecuta la sentencia

    }
}


