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
            return engine.render(new ModelAndView(model,"index"));
        }));

        get("/blog",(request, response) -> {
            Map<String,Object> model = new HashMap<>();
            return engine.render(new ModelAndView(model,"blog"));
        });

        get("/portfolio",(request, response) -> {
            Map<String,Object> model = new HashMap<>();
            return engine.render(new ModelAndView(model,"blog"));
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
