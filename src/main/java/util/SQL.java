package util;


import com.sun.istack.internal.NotNull;
import internalLogic.Comment;
import internalLogic.User;
import org.hibernate.Transaction;
import org.omg.CORBA.INTERNAL;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SQL {

    private static EntityManagerFactory emf;

    public static void initDatabase() throws Exception {
        emf =  Persistence.createEntityManagerFactory("Persistencia");
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void insertUser(String data){
        try{
            Map<String, String> map = processData(data);
            User user = buildUser(map);
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            enf.persist(user);
            tr.commit();
            enf.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static <T> void insert (T comment){
        try{
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            enf.persist(comment);
            tr.commit();
            enf.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static <T> void delete (T comment){
        try{
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            enf.remove(enf.contains(comment) ? comment : enf.merge(comment));
            tr.commit();
            enf.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static <T> void update (T comment){
        try{
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            enf.merge(comment);
            tr.commit();
            enf.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<User> getUsers (){
        try{
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            List<User> list = enf.createQuery("select c from User c")
                    .getResultList();
            tr.commit();
            enf.close();
            return list;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List getPublications() {
        try {
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            List list = enf.createQuery("select c from Publication c")
                    .getResultList();
            tr.commit();
            enf.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Map<String, String> processData(String data){
        Map<String, String> map = new HashMap<>();
        String[] split = data.split("&");
        for(String i: split){
            String [] value = i.split("=");
            map.put(value[0], value[1]);
        }
        return map;
    }

    private static User buildUser(Map<String, String> map){
        User user = new User();
        user.setEmail(map.get("email"));
        user.setBornDate(getDateFromStr(map.get("birthdate")));
        user.setFullName(map.get("fullname"));
        user.setPassword(Cypher.getInstance().encryptCookie(map.get("password")));
        user.setSex(map.get("sex"));
        return user;
    }

    private static Date getDateFromStr(String dat) {
        try{
            String [] parts = dat.split("-");
            return new Date(Integer.parseInt(parts[0])-1900, Integer.parseInt(parts[1])-1,Integer.parseInt(parts[2]));
        }
        catch (Exception e){
            e.printStackTrace();
            return new Date();
        }
    }


}
