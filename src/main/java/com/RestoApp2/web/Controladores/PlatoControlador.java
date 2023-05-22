package com.RestoApp2.web.Controladores;

import com.RestoApp2.web.Entidades.Carrito;
import com.RestoApp2.web.Entidades.Orden;
import com.RestoApp2.web.Entidades.Plato;
import com.RestoApp2.web.Entidades.Usuario;
import com.RestoApp2.web.Servicios.CarritoServicio;
import com.RestoApp2.web.Servicios.ErrorServicio;
import com.RestoApp2.web.Servicios.OrdenServicio;
import com.RestoApp2.web.Servicios.PlatoServicio;
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
import org.springframework.web.multipart.MultipartFile;

@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SELLER', 'ROLE_USER')")
@Controller
@RequestMapping("/plato")
public class PlatoControlador {

    @Autowired
    private PlatoServicio platoServi;

    @Autowired
    private CarritoServicio carritoServi;

    @Autowired
    private OrdenServicio ordenServi;

    @GetMapping("/restoInicio")
    public String index(@RequestParam(required = false) String logout, ModelMap modelo) {
        if (logout != null) {
            modelo.put("logout", "Ha cerrado sesión");
        }

        return "restoInicio";
    }

    @GetMapping("/crearPlato")
    public String crearPlato() {
        return "platoCrear";
    }

    @PostMapping("/guardarPlato")
    public String guardarPlato(ModelMap modelo, MultipartFile archivo, @RequestParam String nombre,
            @RequestParam Double precio, @RequestParam String descri,
            @RequestParam Integer categoria, @RequestParam String idResto) {
        try {
            platoServi.nuevoPlato(archivo, nombre, descri, precio, categoria, idResto);
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("precio", precio);
            modelo.put("descri", descri);
            return "platoCrear";
        }
        modelo.put("exito", "Plato creado con exito");
        return "index";
    }

    @GetMapping("/listarPlatoResto/{idResto}")
    public String listaPlatoResto(ModelMap modelo, @PathVariable("idResto") String idResto) {
        List<Plato> platos = platoServi.listaPlatoResto(idResto);
        modelo.put("platos", platos);

        return "platoListar";
    }

    @GetMapping("/listarPlatoInactivos")
    public String listaPlatoInactivos(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        List<Plato> platos = platoServi.listaPlatosInactivos(usuario.getId());
        modelo.put("platos", platos);
        return "platoListarInactivos";
    }

    @GetMapping("/modPlato/{id}")
    public String modPlato(@PathVariable("id") String id, ModelMap model) {
        try {
            Plato plato = platoServi.buscarPorId(id);
            model.put("plato", plato);
            model.put("id", id);
            model.put("nombre1", plato.getNombre());
            model.put("precio1", plato.getPrecio());
            model.put("descri1", plato.getDescri());
        } catch (ErrorServicio ex) {
            model.put("error", ex.getMessage());
        }
        return "platoModificar";
    }

    @PostMapping("/modificarPlato")
    public String modifiPlato(ModelMap model, MultipartFile archivo, @RequestParam String idPlato,
            @RequestParam String nombre, @RequestParam Double precio,
            @RequestParam String descri) throws ErrorServicio {
        try {
            platoServi.modificarPlato(archivo, idPlato, nombre, precio, descri);
        } catch (ErrorServicio ex) {
            model.put("error", ex.getMessage());
            model.put("nombre", nombre);
            model.put("precio", precio);
            model.put("descri", descri);
            return "platoModificar";
            //en el form poner th:value="${nombre.variable}" para conservar los datos l
        }
        model.put("exito", "El plato fue modificado con exitosamente");
        return "index";
    }

    @GetMapping("/baja/{id}")
    public String baja(@PathVariable("id") String id, HttpSession session, ModelMap model) {
        try {
            platoServi.bajaPlato(id);
        } catch (ErrorServicio e) {
            model.put("error", e.getMessage());
            return "platoListar";
        }
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        model.put("exito", "Plato dado de baja correctamente");
        List<Plato> platos = platoServi.listaPlatoResto(usuario.getId());
        model.put("platos", platos);
        return "platoListar";
    }

    @GetMapping("/alta/{id}")
    public String alta(@PathVariable("id") String id, HttpSession session, ModelMap model) {
        try {
            platoServi.altaPlato(id);
        } catch (ErrorServicio e) {
            model.put("error", e.getMessage());
            return "platoListarInactivos";
        }
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        model.put("exito", "Plato dado de alta correctamente");
        List<Plato> platos = platoServi.listaPlatosInactivos(usuario.getId());
        model.put("platos", platos);
        return "platoListarInactivos";
    }

    @GetMapping("/verPlato/{idPlato}/{idCarrito}")
    public String verUnPlato(@PathVariable("idCarrito") String idCarrito, @PathVariable("idPlato") String idPlato, ModelMap modelo) {
        Plato plato;

        try {
            plato = platoServi.buscarPorId(idPlato);
            Carrito carrito = carritoServi.buscarCarrito(idCarrito);
            modelo.put("idCarrito", idCarrito);
            modelo.put("platos", plato);
            modelo.put("idResto", carrito.getResto().getId());
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            return "menu";
        }

        return "platoUnitario";
    }
}