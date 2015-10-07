package pe.edu.esan.estacionamientoesan;
/**
 * Clase en donde se guardan datos para ser usados entre dos o mas clases.
 * Se guara el el user y el pass, idioma del app en lang,data para manejos de menu(ya no se usa) y un entero en menu que sirve para saber si has hecho click en impresiones
 */
public class Datah {
    private int data=0;//se crea el entero data con el valor de 0
    private int lang=0;//se crea el entero lang con el valor de 0
    private int menu=0;//se crea el entero menu con el valor de 0
    private String user="";//Se crea el String user con el valor de ""
    private String pass="";//Se crea el String pass con el valor de ""


    public int getData() {
        return data;
    }//metodo para obtener data
    public int getMenu() {
        return menu;
    }///metodo para obtener menu

    public int getLang() {
        return lang;
    }//metodo para obtener lang

    public String getUser() {
        return user;
    }//metodo para obtener user

    public String getPass() {
        return pass;
    }//metodo para obtener pass

    public void setData(int data) {
        this.data = data;
    }//metodo para ingresar data
    public void setMenu(int  menu) {
        this.menu = menu;
    }//metodo para ingresar menu

    public void setLang(int lang) {
        this.lang = lang;
    }//metodo para ingresar lang

    public void setUser(String user) {
        this.user = user;
    }//metodo para ingresar user

    public void setPass(String pass) {
        this.pass = pass;
    }//metodo para ingresar pass

    private static final Datah holder = new Datah();
    public static Datah getInstance() {return holder;}
}