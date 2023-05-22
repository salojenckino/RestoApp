package com.RestoApp2.web.Controladores;

import com.RestoApp2.web.Entidades.Mesa;
import com.RestoApp2.web.Entidades.Usuario;
import com.RestoApp2.web.Servicios.ErrorServicio;
import com.RestoApp2.web.Servicios.MesaServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@PreAuthorize("hasAnyRole('ROLE_SELLER','ROLE_ADMIN')")
@Controller
@RequestMapping("/mesa")
public class MesaControlador {

    @Autowired
    private MesaServicio mesaServi;

    @GetMapping("/restoInicio")
    public String index(@RequestParam(required = false) String logout, ModelMap modelo) {
        if (logout != null) {
            modelo.put("logout", "Ha cerrado sesión");
        }

        return "index";
    }

    @GetMapping("/crearMesa")
    public String crearMesa(ModelMap modelo) {
        return "mesaCrear";
    }

    @PostMapping("/guardarMesa")
    public String guardarMesa(ModelMap modelo, @RequestParam Integer capacidad,@RequestParam String denominacion, @RequestParam String idResto) {
        try {
            mesaServi.crearMesa(capacidad,denominacion, idResto);
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            return "crearMesa";
        }
        modelo.put("exito", "La mesa fue ingresada con éxito.");
        return "mesaCrear";

    }

    @GetMapping("/listarMesaResto/{idResto}")
    public String listarMesaResto(ModelMap modelo, @PathVariable("idResto") String idResto) {
        List<Mesa> mesas = mesaServi.listarMesaResto(idResto);
        modelo.put("mesas", mesas);
        return "mesaListar";
    }

    @GetMapping("/listarMesasInactivas")
    public String listaMesasInactivas(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        List<Mesa> mesas = mesaServi.buscarMesasInactivas(usuario.getId());
        modelo.put("mesas", mesas);
        return "mesaListarInactivas";
    }

    @GetMapping("/baja/{id}")
    public String baja(@PathVariable("id") String id, HttpSession session, ModelMap model) {
        try {
            mesaServi.bajaMesa(id);
        } catch (ErrorServicio e) {
            model.put("error", e.getMessage());
            return "index";
        }

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        model.put("exito", "Mesa dada de baja correctamente");
        List<Mesa> mesas = mesaServi.listarMesaResto(usuario.getId());
        model.put("mesas", mesas);

        return "mesaListar";
        //return "redirect:/mesa/listarMesaResto/{idResto}";
    }

    @GetMapping("/alta/{id}")
    public String alta(@PathVariable("id") String id, HttpSession session, ModelMap model) {
        try {
            mesaServi.altaMesa(id);
        } catch (ErrorServicio e) {
            model.put("error", e.getMessage());
            return "mesaListarInactivas";
        }
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        model.put("exito", "Mesa dada de alta correctamente");
        List<Mesa> mesas = mesaServi.buscarMesasInactivas(usuario.getId());
        model.put("mesas", mesas);

        return "mesaListarInactivas";
    }
}
