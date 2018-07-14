package util;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class SQL {

    private static EntityManagerFactory emf;

    public static void initDatabase() throws Exception {
        emf =  Persistence.createEntityManagerFactory("Persistencia");
    }

    public static EntityManager getSession() {
        return emf.createEntityManager();
    }




}
