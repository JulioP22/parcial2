import routing.Rutas;
import spark.Spark;
import util.SQL;
import util.Servidor;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        try{
//            Servidor.startDb();
//            SQL.initDatabase();
            Spark.staticFiles.location("/public");//Indica recursos servidos de forma estática.
            Rutas.getInstance().initRoutes();
        }
        catch (Exception e){
//            Servidor.stopDb();
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al subir el servidor");
        }

    }
}
