package util;


import com.sun.istack.internal.NotNull;
import internalLogic.Comment;
import internalLogic.MLike;
import internalLogic.User;
import org.hibernate.EntityNameResolver;
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
        if (!userCreated()) {
            createDefaultUser();
        }
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }



    private static boolean userCreated(){
        boolean created = false;
        EntityManager enf = getEntityManager();
        EntityTransaction tr = enf.getTransaction();
        tr.begin();
        long count = (long)enf.createQuery("SELECT count (*) from User ").getSingleResult();
        if (count>0){
            created = true;
        }
        return created;
    }


    private static void createDummyData(){

    };

    private static void createDefaultUser(){
        try {
            User user = new User("Generico Generalizado","a@a.com","j9hp49DuFChzXndy9SVmXtPYDCBKjkoCBxz49vhuUvI8MnwEbsm+0M/AHIQDpX/w",new Date(),"M","Santiago","PUCMM",null,"admin");
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            enf.persist(user);
            tr.commit();
            enf.close();
        }catch (Exception e){
            e.printStackTrace();
        }
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

    public static void insertFriend(User friend, long idUser){
        try{
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            enf.persist(friend);
            enf.flush();
            enf.createNativeQuery("insert into user_user (user_id, FRIENDS_ID) values(:USER_ID, :FRIENDS_ID)")
                    .setParameter("FRIENDS_ID", friend.getId())
                    .setParameter("USER_ID", idUser)
                    .executeUpdate();
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

    public static void insertCommentIntoPublication(Comment comment, long idPublication){
        try{
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            enf.persist(comment);
            enf.flush();
            enf.createNativeQuery("insert into publication_comment (publication_id, commentset_id) values(:PUBLICATION_ID, :COMMENTSET_ID)")
                    .setParameter("PUBLICATION_ID", idPublication)
                    .setParameter("COMMENTSET_ID", comment.getId())
                    .executeUpdate();
            tr.commit();
            enf.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void insertLikeIntoPublication(MLike like, long idPublication){
        try{
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            enf.persist(like);
            enf.flush();
            enf.createNativeQuery("insert into PUBLICATION_MLIKE (publication_id, LIKESET_ID) values(:PUBLICATION_ID, :LIKESET_ID)")
                    .setParameter("PUBLICATION_ID", idPublication)
                    .setParameter("LIKESET_ID", like.getId())
                    .executeUpdate();
            tr.commit();
            enf.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List getPublicationsFromUser(long idUser){
        try {
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            List list = enf.createQuery("select c from Publication c where receiverUser_id = :id")
                    .setParameter("id",idUser)
                    .getResultList();
            tr.commit();
            enf.close();
            return list;
        } catch (Exception e) {
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

    public static void deleteComment(long idComment, long idPublication){
        try{
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            enf.createNativeQuery("delete from PUBLICATION_COMMENT where PUBLICATION_ID = :PUBLICATION_ID and COMMENTSET_ID = :COMMENTSET_ID")
                    .setParameter("PUBLICATION_ID", idPublication)
                    .setParameter("COMMENT_ID", idComment)
                    .executeUpdate();
            tr.commit();
            enf.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void deleteLike(long idLike, long idPublication){
        try{
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            enf.createNativeQuery("delete from PUBLICATION_MLIKE where PUBLICATION_ID = :PUBLICATION_ID and LIKESET_ID = :LIKESET_ID")
                    .setParameter("PUBLICATION_ID", idPublication)
                    .setParameter("LIKESET_ID", idLike)
                    .executeUpdate();
            tr.commit();
            enf.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static User getUserByEmail(String email){
        try {
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            User user = (User) enf.createQuery("select c from User c where lower(c.email) like lower(:email)")
                    .setParameter("email",email)
                    .getSingleResult();
            tr.commit();
            enf.close();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
