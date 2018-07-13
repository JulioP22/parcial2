package util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


public class SQL {
    private static SessionFactory sessionFactory;
    private static String URL = "jdbc:h2:tcp://localhost/~/SocialMedia";

//    public static void initDatabase() throws Exception {
//        sessionFactory = new Configuration().configure()
//                .buildSessionFactory();
//        Session session = SQL.getSession();
//    }


//    Este método reemplaza al getConexion. Ahora se usa el concepto de sesion.
    public static Session getSession() {
        return sessionFactory.openSession();
    }




}
