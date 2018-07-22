package routing;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import internalLogic.*;
import internalLogic.Request;
import spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import util.Cypher;
import util.SQL;

import javax.crypto.interfaces.PBEKey;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static spark.Spark.*;


public class Rutas {
    private static Rutas rut;
    private ThymeleafTemplateEngine engine;
    private Gson parser;

    public static Rutas getInstance() {
        if (rut == null) {
            rut = new Rutas();
        }
        return rut;
    }

    private Rutas() {
        engine = new ThymeleafTemplateEngine();
        parser = new Gson();
    }


    public void initRoutes() {
        port(4000);


        get("/",((request, response) -> {
            Map<String,Object> model = new HashMap<>();
            model.put("usuariosesion",request.session().attribute("user"));


            if (request.session().attribute("user")!=null){
                model.put("notifications",SQL.getUserNotifications(((User)request.session().attribute("user")).getId()));
                model.put("unknownUsers",SQL.getUnknownUsers(((User)request.session().attribute("user")).getId()));
                model.put("requests",SQL.getUserRequest(((User)request.session().attribute("user")).getId()));
                return engine.render(new ModelAndView(model,"authenticatedIndex"));
            }else{
                return engine.render(new ModelAndView(model,"index"));
            }

        }));

        get("/login",(request, response) -> {
            Map<String,Object> model = new HashMap<>();
            return engine.render(new ModelAndView(model,"login"));
        });


        post("/login",(request, response) -> {
            User user = parser.fromJson(request.body(),User.class);
            if (loginUser(user) && user!=null){
                Session session = request.session(true);
                session.maxInactiveInterval(600);
                session.attribute("user",SQL.getUserByEmail(user.getEmail()));//

                //Aqui va la lógica del cookie
            }else{
                halt(401,"Credenciales no válidas");
            }
           return "Logged In!";
        });

        post("/register",(request, response) -> {
            User user = parser.fromJson(request.body(),User.class);
            user.setPassword(Cypher.getInstance().encrypt(user.getPassword()));
            SQL.insert(user);
            response.status(200);
            response.redirect("/");
            return "";
        });

        get("/profile",(request, response) -> {
            Map<String,Object> model = new HashMap<>();
            model.put("usuariosesion",request.session().attribute("user"));
            if (request.session().attribute("user")!=null){
                long idUser = ((User)request.session().attribute("user")).getId();
                model.put("albums",SQL.getUserAlbums(((User)request.session().attribute("user")).getId()));
                model.put("notifications",SQL.getUserNotifications(idUser));
                model.put("requests",SQL.getUserRequest(idUser));
                return engine.render(new ModelAndView(model,"profile"));
            }
            else{
                return engine.render(new ModelAndView(model,"index"));
            }
        });

        get("/profile/:id",(request, response) ->{
            Map<String,Object> model = new HashMap<>();
            long idMuro = Integer.parseInt(request.params("id"));
            User usuariomuro = SQL.getUserByID(idMuro);
            model.put("usuariosesion",request.session().attribute("user"));
            model.put("usuariomuro",usuariomuro);
            if (usuariomuro!=null){
                model.put("albums",SQL.getUserAlbums(idMuro));
//                model.put("notifications",SQL.getUserNotifications(idMuro));
//                model.put("requests",SQL.getUserRequest(idMuro));
                return engine.render(new ModelAndView(model,"profileotro"));
            }
            else{
                return engine.render(new ModelAndView(model,"index"));
            }
        });

        get("/contact",(request, response) -> {
            Map<String,Object> model = new HashMap<>();
            return engine.render(new ModelAndView(model,"blog"));
        });

        get("/text",(request, response) -> {
            return "Ahora va este texto";
        });


        post("/insertPublication/:taggedUsers",(request, response) -> {
            Publication publication = parser.fromJson(request.body(),Publication.class);
            publication.setCreator(request.session().attribute("user"));
            publication.setReceiverUser(request.session().attribute("user"));//TODO esto debe cambiar al usuario en cuyo perfil se está.

            long[] taggedUsers = parser.fromJson(request.params("taggedUsers"),long[].class);

            Publication pub = SQL.insert(publication);
            SQL.insertTaggedUsers(pub.getId(),taggedUsers);
            generateNotifications(taggedUsers,pub.getId());

            System.out.println("Publicacion recibidia es: " + publication.getDescription() );
            return "Inserted";
        });

        post("/updateProfilePic",(request, response) -> {
            Base64Image base64Image = parser.fromJson(request.body(),Base64Image.class);
            byte[] bytesImage = base64ToByteArray(base64Image.getImage());
            User loggedInUser = request.session().attribute("user");
            loggedInUser.setProfilePhoto(bytesImage);
            SQL.update(loggedInUser);
            return "updated";
        });

        post("/saveComment/:idUser/:idPublication",(request, response) -> {
            Comment comment = parser.fromJson(request.body(), Comment.class);
            long id = Integer.parseInt(request.params("idUser"));
            long idPublication = Integer.parseInt(request.params("idPublication"));
            User user = SQL.getElementById(id, User.class);
            comment.setUser(user);
            comment.setDate(new Date());
            SQL.insertCommentIntoPublication(comment,idPublication);
            return "";
        });

        post("/saveLike/:idUser/:idPublication",(request, response) -> {
            MLike like = parser.fromJson(request.body(), MLike.class);
            long id = Integer.parseInt(request.params("idUser"));
            long idPublication = Integer.parseInt(request.params("idPublication"));
            User user = SQL.getElementById(id, User.class);
            like.setUser(user);
            like.setDate(new Date());
            SQL.insertLikeIntoPublication(like,idPublication);
            Publication pub = SQL.getElementById(idPublication,Publication.class);
            pub.verifyLike(request.session().attribute("user"));

            Map<String, Object> model = new HashMap<>();
            model.put("publication", pub);
            model.put("usuariosesion", request.session().attribute("user"));

            return engine.render(new ModelAndView(model,"THBasis/likeBasis"));
        });

        post("/quitLike/:idUser/:idPublication",(request, response) -> {
            long idUser = Integer.parseInt(request.params("idUser"));
            long idPublication = Integer.parseInt(request.params("idPublication"));
            MLike like = SQL.getLikeByUserId(idUser,idPublication);
            SQL.deleteLike(idPublication,like.getId());
            Publication pub = SQL.getElementById(idPublication,Publication.class);
            pub.verifyLike(request.session().attribute("user"));

            Map<String, Object> model = new HashMap<>();
            model.put("publication", pub);
            model.put("usuariosesion", request.session().attribute("user"));

            return engine.render(new ModelAndView(model,"THBasis/likeBasis"));
        });

        get("/getComments/:idPublication",(request, response) -> {
            long id = Integer.parseInt(request.params("idPublication"));
            Map<String,Object> model = new HashMap<>();
            model.put("publication",SQL.getElementById(id, Publication.class));
            model.put("usuariosesion", request.session().attribute("user"));
            return engine.render( new ModelAndView(model, "THBasis/commentBasis"));
        });

        get("/getFriend/:idFriend",(request, response) -> {
            long idFriend = Integer.parseInt(request.params("idFriend"));
            User user = SQL.getElementById(idFriend,User.class);
            user.setFriends(null);
            return parser.toJson(user);
        });

        post("/sendRequest/:idUser/:idFriend",(request, response) -> {
            long idUser = Integer.parseInt(request.params("idUser"));
            long idFriend = Integer.parseInt(request.params("idFriend"));
            User user = SQL.getElementById(idUser,User.class);
            User friend = SQL.getElementById(idFriend,User.class);
            Request rq = new Request(user, friend, user.getFullName()+" te ha enviado una solicitud de amistad", new Date());

            SQL.insert(rq);

            return "";
        });

        post("/acceptRequest/:idNot/:idSender/:idReceiver",(request, response) -> {
            long idNot = Integer.parseInt(request.params("idNot"));
            long idSender = Integer.parseInt(request.params("idSender"));
            long idReceiver = Integer.parseInt(request.params("idReceiver"));
            Request not = SQL.getElementById(idNot, Request.class);
            not.setState(1);
            SQL.update(not);

            SQL.insertFriend(idReceiver,idSender);

            User user = SQL.getElementById(idReceiver, User.class);
            request.session().attribute("user",user);

            return "";
        });

        post("/deleteRequest/:idNot",(request, response) -> {
            long idNot = Integer.parseInt(request.params("idNot"));

            Request not = SQL.getElementById(idNot, Request.class);
            SQL.delete(not);

            return "";
        });

        post("/deleteComment/:idPublication/:idComment",(request, response) -> {
            long idPublication = Integer.parseInt(request.params("idPublication"));
            long idComment = Integer.parseInt(request.params("idComment"));

            SQL.deleteComment(idComment,idPublication);

            return "";
        });

        post("/deletePublication/:idPublication",(request, response) -> {
            long idPublication = Integer.parseInt(request.params("idPublication"));

            SQL.deleteComments(idPublication);
            SQL.deleteLikes(idPublication);
            SQL.deleteTags(idPublication);

            Publication pub = SQL.getElementById(idPublication,Publication.class);
            SQL.delete(pub);

            return "";
        });

        get("/loadPosts/:idUser",(request, response) -> {

            long id = Integer.parseInt(request.params("idUser"));
            Map<String,Object> model = new HashMap<>();
            model.put("usuariosesion",request.session().attribute("user"));

            model.put("publications",SQL.getPublicationsFromUser(id));
            refreshPublications((List<Publication>) model.get("publications"), (User) model.get("usuariosesion"));

            return engine.render(new ModelAndView(model,"THBasis/publicationBasis"));

        });

        get("/loadHistory",(request, response) -> {
            Map<String,Object> model = new HashMap<>();
            model.put("usuariosesion",request.session().attribute("user"));

            model.put("publications",SQL.getPublications());
            refreshPublications((List<Publication>) model.get("publications"), (User) model.get("usuariosesion"));

            return engine.render(new ModelAndView(model,"THBasis/publicationBasisHistory"));

        });

        get("/logoff",(request, response) -> {
            request.session().attribute("user",null);
            response.redirect("/");
            return "";
        });

        get("/editPublication",(request, response) -> {

            if (request.session().attribute("user")!=null){
                Map<String,Object> model = new HashMap<>();
                model.put("usuariosesion",request.session().attribute("user"));

                return engine.render(new ModelAndView(model,"editPublication"));
            }
            else{
                response.redirect("/");
                return "";
            }
        });

        get("/getPublication/:id", (request, response) -> {
            long id = Integer.parseInt(request.params("id"));
            Publication pub = SQL.getElementById(id, Publication.class);

            pub.setLikeSet(null);
            pub.setCommentSet(null);
            pub.setCreator(null);
            pub.setReceiverUser(null);
            cleanUsers(pub.getTaggedUsers());

            return parser.toJson(pub);
        });

        post("/updatePublication/:taggedUsers",(request, response) -> {
            Publication publication = parser.fromJson(request.body(),Publication.class);

            System.out.println(publication.getDescription());

            Publication realOne = SQL.getElementById(publication.getId(),Publication.class);
            realOne.setDescription(publication.getDescription());

            long[] taggedUsers = parser.fromJson(request.params("taggedUsers"),long[].class);
            SQL.deleteTags(publication.getId());

            SQL.insertTaggedUsers(publication.getId(),taggedUsers);
            SQL.update(realOne);

            generateNotifications(taggedUsers,publication.getId());
            return "";
        });

        get("/loadPublicationOnModal/:idPublication",(request, response) -> {
            long idPublication = Integer.parseInt(request.params("idPublication"));
            Publication pub = SQL.getElementById(idPublication,Publication.class);
            pub.verifyLike((User)request.session().attribute("user"));
            Map<String,Object> model = new HashMap<>();

            model.put("publication",pub);
            model.put("usuariosesion",request.session().attribute("user"));

            return engine.render(new ModelAndView(model,"THBasis/publicationOnModal"));
        });

        post("/updateNotification/:idNot",(request, response) -> {
            long idNot = Integer.parseInt(request.params("idNot"));

            Notification not = SQL.getElementById(idNot,Notification.class);
            not.setState(1);
            SQL.update(not);

            return "";
        });

    }

    private boolean loginUser(User user){
        boolean loggedIn = false;
        List<User> usuarios = SQL.getUsers();
        for (User usuario: usuarios){
            System.out.println("Entre a el usuario: " + usuario.getEmail());
            if (usuario.getEmail().trim().equalsIgnoreCase(user.getEmail().trim()) && Cypher.getInstance().checkPassword(user.getPassword().trim(),usuario.getPassword().trim())){
                loggedIn = true;
            }
        }

        return loggedIn;
    }

    public byte[] base64ToByteArray(String base64Image){
        byte[] decodedString  = "default".getBytes();
        try {
            byte[] encoded = Base64.getEncoder().encode(base64Image.getBytes());
            decodedString = Base64.getDecoder().decode(new String(encoded).getBytes("UTF-8"));
            return decodedString;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return decodedString;
    }

    private void refreshPublications(List<Publication> pubs, User user){
        for(Publication i: pubs){
            i.verifyLike(user);
        }
    }

    private void generateNotifications(long[] ids, long idPublication){
        Publication pub = SQL.getElementById(idPublication, Publication.class);
        for(long i: ids){
            User user = SQL.getElementById(i, User.class);
            Notification not = new Notification(pub.getCreator().getFullName()+" te ha etiquetado en una publicación",user,pub,new Date());
            SQL.insert(not);
        }
    }

    private void cleanUsers(Set<User> users){
        for(User i: users){
            i.setPassword(null);
            i.setFriends(null);
            i.setProfilePhoto(null);
            i.setSex(null);
            i.setBornDate(null);
            i.setEmail(null);
            i.setJobs(null);
            i.setLocation(null);
            i.setPortraitPhoto(null);
            i.setRole(null);
        }
    }

}

    //
