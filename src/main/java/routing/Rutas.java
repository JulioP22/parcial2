package routing;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import internalLogic.*;
import spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import util.Cypher;
import util.SQL;

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
        // Aqui se colocaran todas las rutas necesarias. Para la proxima implementacion, separar las rutas dependiendo de para que sirven

        get("/",((request, response) -> {
            Map<String,Object> model = new HashMap<>();
            model.put("usuariosesion",request.session().attribute("user")); //Reemplazar esto on el objeto de usuario de la sesión, usado para validar que se muestra y que no.


            if (request.session().attribute("user")!=null){ //Si el usuario está autenticado la página principal que se carga es el feed de noticias
                model.put("notifications",SQL.getUserNotifications(((User)request.session().attribute("user")).getId()));
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
            System.out.println("El cuerpo de registro recibido");
            System.out.println(request.body());
            SQL.insertUser(request.body());
            response.status(200);
            response.redirect("/");
            return "";
        });

        get("/profile",(request, response) -> {
            Map<String,Object> model = new HashMap<>();
            long idUser = ((User)request.session().attribute("user")).getId();
            model.put("usuariosesion",request.session().attribute("user")); //Reemplazar esto on el objeto de usuario de la sesión, usado para validar que se muestra y que no.
            model.put("publications",SQL.getPublicationsFromUser(idUser));
            model.put("notifications",SQL.getUserNotifications(idUser));
            refreshPublications((List<Publication>) model.get("publications"), (User) model.get("usuariosesion"));
            return engine.render(new ModelAndView(model,"profile"));
        });

        get("/contact",(request, response) -> {
            Map<String,Object> model = new HashMap<>();
            return engine.render(new ModelAndView(model,"blog"));
        });

        get("/text",(request, response) -> {
            return "Ahora va este texto";
        });

        // Esta es una ruta para probar funciones usando postman
        post("/insertUser",(request, response) -> {

            List<User> list = SQL.getUnknownUsers(1);

            return "";
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
            System.out.println(pub.isLiked());

            Map<String, Object> model = new HashMap<>();
            model.put("publication", pub);
            model.put("usuariosesion", request.session().attribute("user"));

            return engine.render(new ModelAndView(model,"THBasis/likeBasis"));
        });

        get("/getComments/:idPublication",(request, response) -> {
            long id = Integer.parseInt(request.params("idPublication"));
            Map<String,Object> model = new HashMap<>();
            model.put("publication",SQL.getElementById(id, Publication.class));
            return engine.render( new ModelAndView(model, "THBasis/commentBasis"));
        });

        get("/getFriend/:idFriend",(request, response) -> {
            long idFriend = Integer.parseInt(request.params("idFriend"));
            User user = SQL.getElementById(idFriend,User.class);
            user.setFriends(null);
            return parser.toJson(user);
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

    public byte[] getImageInBytes(){
        try{
            BufferedImage originalImage =
                    ImageIO.read(new File("C:\\Users\\Julio\\Pictures\\prueba.jpg"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( originalImage, "jpg", baos );
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
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

}

    //
