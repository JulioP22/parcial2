package routing;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import util.SQL;

import java.util.*;

import static spark.Spark.*;


public class Rutas {
    private static Rutas rut;
    private ThymeleafTemplateEngine engine;
    private Gson render;

    public static Rutas getInstance() {
        if (rut == null) {
            rut = new Rutas();
        }
        return rut;
    }

    private Rutas() {
        engine = new ThymeleafTemplateEngine();
        render = new Gson();
    }


    public void initRoutes() {
        port(4000);
        // Aqui se colocaran todas las rutas necesarias. Para la proxima implementacion, separar las rutas dependiendo de para que sirven

        get("/",((request, response) -> {
            Map<String,Object> model = new HashMap<>();
            model.put("usuariosesion",true); //Reemplazar esto on el objeto de usuario de la sesión, usado para validar que se muestra y que no.

            boolean usuariologeado = true;

            if (usuariologeado){ //Si el usuario está autenticado la página principal que se carga es el feed de noticias
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
           return "GREAT!";
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
            model.put("usuariosesion",true); //Reemplazar esto on el objeto de usuario de la sesión, usado para validar que se muestra y que no.
            return engine.render(new ModelAndView(model,"profile"));
        });

        get("/contact",(request, response) -> {
            Map<String,Object> model = new HashMap<>();
            return engine.render(new ModelAndView(model,"blog"));
        });

        get("/text",(request, response) -> {
            return "Ahora va este texto";
        });

    }

}
