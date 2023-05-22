package com.RestoApp2.web.Controladores;

import com.RestoApp2.web.Servicios.CarritoServicio;
import com.RestoApp2.web.Servicios.ErrorServicio;
import com.RestoApp2.web.Servicios.RestoServicio;
import com.RestoApp2.web.Servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainControlador {

    @Autowired
    UsuarioServicio usuServi;

    @Autowired
    RestoServicio restoServi;

    @Autowired
    CarritoServicio carriServi;

    @GetMapping("/")
    public String index(@RequestParam(required = false) String logout, @RequestParam(required = false) String id, ModelMap modelo, HttpSession session) {
        if (logout != null) {
            modelo.put("logout", "Ha cerrado sesión");
        }
        if (id != null) {

            try {
                carriServi.eliminarCarrito(id);

                modelo.put("exito", "Carrito eliminado con éxito junto con sus ordenes.");
            } catch (ErrorServicio ex) {
                modelo.put("error", ex.getMessage());
            }

        }
        modelo.put("restos", restoServi.listaResto());

        return "index";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registroIntermedio";
    }

    @GetMapping("/registroUsuario")
    public String registroUsuario() {
        return "registroUsuario";
    }

    @PostMapping("/registrarUsuario")
    public String registrarUsuario(ModelMap modeloUsu, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2) {
        try {
            usuServi.registroUsuarioUsuario(nombre, apellido, mail, clave1, clave2);
        } catch (ErrorServicio e) {
            modeloUsu.put("error", e.getMessage());
            modeloUsu.put("nombre", nombre);
            modeloUsu.put("apellido", apellido);
            modeloUsu.put("mail", mail);
            modeloUsu.put("clave1", clave1);
            modeloUsu.put("clave2", clave2);
            return "registroIntermedio";
        }
        modeloUsu.put("exito", "Usuario registrado exitorsamente");
        return "index";
    }

    @GetMapping("/registroUsuarioResto")
    public String registroUsuarioResto() {
        return "registroUsuarioResto";
    }

    @PostMapping("/registrarUsuarioResto")
    public String registrarUsuarioResto(ModelMap modeloUsu, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2) {
        try {
            usuServi.registroRestoUsuario(nombre, apellido, mail, clave1, clave2);
        } catch (ErrorServicio e) {
            modeloUsu.put("error", e.getMessage());
            modeloUsu.put("nombre", nombre);
            modeloUsu.put("apellido", apellido);
            modeloUsu.put("mail", mail);
            modeloUsu.put("clave1", clave1);
            modeloUsu.put("clave2", clave2);
            return "registroIntermedio";
        }
        modeloUsu.put("exito", "Usuario registrado exitorsamente");
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o clave incorreto");
        }

        return "login.html";
    }

}
