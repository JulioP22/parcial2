package util;


import com.sun.istack.internal.NotNull;
import internalLogic.*;
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

    }

    private static void createDefaultUser(){
        try {
            User user = new User("Generico Generalizado","a@a.com","j9hp49DuFChzXndy9SVmXtPYDCBKjkoCBxz49vhuUvI8MnwEbsm+0M/AHIQDpX/w",new Date(),"M","Santiago","PUCMM",null,"admin",null,null);
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

    public static void insertFriend(long idFriend, long idUser){
        try{
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            enf.createNativeQuery("insert into user_user (user_id, FRIENDS_ID) values(:USER_ID, :FRIENDS_ID)")
                    .setParameter("FRIENDS_ID", idFriend)
                    .setParameter("USER_ID", idUser)
                    .executeUpdate();
            enf.createNativeQuery("insert into user_user (user_id, FRIENDS_ID) values(:USER_ID, :FRIENDS_ID)")
                    .setParameter("FRIENDS_ID", idUser)
                    .setParameter("USER_ID", idFriend)
                    .executeUpdate();
            tr.commit();
            enf.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static <T> T insert (T comment){
        try{
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            enf.persist(comment);
            tr.commit();
            enf.close();
            return comment;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
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

    public static List<UserAlbum> getUserAlbums(long userId){
        try {
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            List list = enf.createQuery("select c from UserAlbum c where creator.id = :id order by id desc")
                    .setParameter("id",userId)
                    .getResultList();
            tr.commit();
            enf.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
            List list = enf.createQuery("select c from Publication c where receiverUser_id = :id order by id desc")
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
            List list = enf.createQuery("select c from Publication c order by id desc")
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
        user.setEmail(map.get("email").replace("%40", "@"));
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


    public static <T> T getElementById(long id, Class cls){
        try{
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            T element = (T) enf.find(cls, id);
            tr.commit();
            enf.close();
            return element;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
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

    public static List getUnknownUsers(long idUser){
        try {
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            List<User> user = enf.createNativeQuery("select * from user where id not in (select user_user.FRIENDS_ID from USER_USER where user_id = :idUser) and id <> :idUser " +
                    "AND id not in (select REQUEST.RECEIVER_ID\n" +
                    "from REQUEST\n" +
                    "where REQUEST.SENDER_ID = :idUser)", User.class)
                    .setParameter("idUser",idUser)
                    .setMaxResults(10)
                    .getResultList();
            tr.commit();
            enf.close();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MLike getLikeByUserId(long idUser, long idPublication){
        try {
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            MLike user = (MLike) enf.createNativeQuery("SELECT * from MLIKE inner join publication_mlike on PUBLICATION_MLIKE.LIKESET_ID = MLIKE.ID where user_id = :idUser and PUBLICATION_ID = :idPublication", MLike.class)
                    .setParameter("idUser",idUser)
                    .setParameter("idPublication",idPublication)
                    .getSingleResult();
            tr.commit();
            enf.close();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteLike(long idPublication, long idLike){
        try {
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            enf.createNativeQuery("delete from publication_mlike where LIKESET_ID = :idLike and PUBLICATION_ID = :idPublication")
                    .setParameter("idLike",idLike)
                    .setParameter("idPublication",idPublication)
                    .executeUpdate();
            enf.createNativeQuery("delete from MLIKE where id = :id")
                    .setParameter("id",idLike)
                    .executeUpdate();
            tr.commit();
            enf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertTaggedUsers(long idPublication, long[] usersIds){
        try {
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            for(long i: usersIds){
                enf.createNativeQuery("insert into publication_user (publication_id, TAGGEDUSERS_ID) values (:idPublication, :idUser)")
                        .setParameter("idPublication", idPublication)
                        .setParameter("idUser", i)
                        .executeUpdate();
            }
            tr.commit();
            enf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Notification> getUserNotifications(long idUser){
        try {
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            List<Notification> list = enf.createQuery("Select c from Notification c where user.id = :idUser and state = 0")
                    .setParameter("idUser", idUser)
                    .getResultList();
            tr.commit();
            enf.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Request> getUserRequest(long idUser){
        try {
            EntityManager enf = getEntityManager();
            EntityTransaction tr = enf.getTransaction();
            tr.begin();
            List<Request> list = enf.createQuery("Select c from Request c where sender.id = :idUser and state = 0")
                    .setParameter("idUser", idUser)
                    .getResultList();
            tr.commit();
            enf.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
