import routing.Rutas;
import spark.Spark;
import util.SQL;
import util.Servidor;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        try{
            Servidor.startDb();
            SQL.initDatabase();
            Spark.staticFiles.location("/public");
//            Spark.staticFiles.externalLocation("src/main/resources/public/");
//            Spark.externalStaticFileLocation("src/main/resources/public/");
            Rutas.getInstance().initRoutes();
        }
        catch (Exception e){
            Servidor.stopDb();
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al subir el servidor");
        }

    }
}
