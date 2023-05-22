package com.RestoApp2.web.Controladores;

import com.RestoApp2.web.Entidades.Carrito;
import com.RestoApp2.web.Entidades.Mesa;
import com.RestoApp2.web.Entidades.Orden;
import com.RestoApp2.web.Entidades.Reserva;
import com.RestoApp2.web.Entidades.Usuario;
import com.RestoApp2.web.Servicios.CarritoServicio;
import com.RestoApp2.web.Servicios.ErrorServicio;
import com.RestoApp2.web.Servicios.MesaServicio;
import com.RestoApp2.web.Servicios.OrdenServicio;
import com.RestoApp2.web.Servicios.ReservaServicio;
import java.util.ArrayList;
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

@PreAuthorize("hasAnyRole('ROLE_SELLER','ROLE_ADMIN','ROLE_USER')")
@Controller
@RequestMapping("/reserva")
public class ReservaControlador {

    @Autowired
    private CarritoServicio carritoServicio;
    @Autowired
    private OrdenServicio ordenServicio;
    @Autowired
    private MesaServicio mesaServicio;
    @Autowired
    private ReservaServicio reservaServicio;

    @GetMapping("/crearReserva/{carritoId}")
    public String crearReserva(HttpSession session, ModelMap model, @PathVariable("carritoId") String carritoId) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        try {

            Carrito carrito = carritoServicio.buscarCarrito(carritoId);
            if (!login.getId().equals(carrito.getUsuario().getId())) {
                /*Para verificar que el carrito es del mismo del usuario que esta en la pagina*/
                return "index.html";
            }
            String idResto = carrito.getResto().getId();
            String nombreResto = carrito.getResto().getNombre();
            String idCarrito = carrito.getId();
            ArrayList<String> IdOrdenes = carrito.getIdOrden();
            ArrayList<Orden> ordenes = new ArrayList();
            Double total = 0.0;
            /*Mesa*/
            List<Mesa> mesas = mesaServicio.listarMesaResto(idResto);

            for (String IdOrdene : IdOrdenes) {
                Orden orden = ordenServicio.buscarOrden(IdOrdene);
                ordenes.add(orden);
                total = total + orden.getPlato().getPrecio();
            }
            model.put("mesas", mesas);
            model.put("total", total);
            model.put("listaOrdenes", ordenes);
            model.put("idResto", idResto);
            model.put("nombreResto", nombreResto);
            model.put("idCarrito", idCarrito); //no me mostraba el carrito xq faltaba esta linea.

            return "reservaNuevo.html";

        } catch (ErrorServicio ex) {
            model.put("error", ex.getMessage());

            return "menu.html";
        }

    }

    @PostMapping("/guardarReserva")
    public String guardarReserva(ModelMap modelo, @RequestParam String idCarrito,
            @RequestParam Integer cantidad,
            @RequestParam String idMesa,
            @RequestParam String dia,
            @RequestParam String hora) {
        System.out.println(dia);
        System.out.println(hora);
        try {

            Reserva reserva = reservaServicio.crearReserva(idCarrito, idCarrito, cantidad, idMesa, dia, hora);

            ArrayList<String> IdOrdenes = reserva.getCarro().getIdOrden();
            ArrayList<Orden> ordenes = new ArrayList();
            Double total = 0.0;
            for (String IdOrdene : IdOrdenes) {
                Orden orden = ordenServicio.buscarOrden(IdOrdene);
                ordenes.add(orden);
                total = total + orden.getPlato().getPrecio();
            }

            modelo.put("reserva", reserva);
            modelo.put("listaOrdenes", ordenes);
            modelo.put("total", total);
            modelo.put("exito", "reserva realizada con éxito.");
            return "reservaConfirmada";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
        }
        return "reservaConfirmada";
    }

    @GetMapping("/verReserva/{idReserva}")
    public String verReserva(ModelMap modelo, @PathVariable("idReserva") String idReserva) {

        try {
            Reserva reserva = reservaServicio.buscarReserva(idReserva);
            ArrayList<String> IdOrdenes = reserva.getCarro().getIdOrden();
            ArrayList<Orden> ordenes = new ArrayList();
            Double total = 0.0;
            for (String IdOrdene : IdOrdenes) {
                Orden orden = ordenServicio.buscarOrden(IdOrdene);
                ordenes.add(orden);
                total = total + orden.getPlato().getPrecio();
            }

            modelo.put("reserva", reserva);
            modelo.put("listaOrdenes", ordenes);
            modelo.put("total", total);

        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
        }
        return "ReservaVer";

    }

    @GetMapping("/listarReservas")
    public String listarReservas(HttpSession session, ModelMap modelo) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "index.html";
        }
        try {
            List<Reserva> reservas = reservaServicio.listaDeReservas(login.getId());
            modelo.put("reservas", reservas);
        } catch (ErrorServicio ex) {
            String error = ex.getMessage();
            modelo.put("error", error);
        }
        return "listarReservas";

    }

    @GetMapping("/darBajaReserva/{idReserva}")
    public String darBajaReserva(ModelMap modelo, @PathVariable("idReserva") String idReserva) {
        /*Para eliminar la reserva, no basta con eliminar la reserva, si no vamos a dejar un carrito con sus reservas en la BD
        por esto primero eliminamos la reserva, luego las ordenes y luego el carrito(si eliminaramos en otro orden no podriamos ya que la reserva
        tiene clave foranea el carrito, MySql no me permite eliminar una clave foraena*/

        try {
            reservaServicio.eliminarReserva(idReserva);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
        }
        modelo.put("exito","La reserva fue eliminada con éxito.");
      return "listarReservas";
    }
    
    @GetMapping("/verReservasResto")
    public String verReservasResto (ModelMap modelo,HttpSession session){
        
        try{
         Usuario login = (Usuario) session.getAttribute("usuariosession");
         List<Reserva> reservas = reservaServicio.buscarReservaResto(login.getId());
         modelo.put("reservas",reservas);
         return "verReservasResto";
        }catch(ErrorServicio e){
            modelo.put("error",e);
            return "verReservasResto";
        } 
    }
    
    @GetMapping("/darBajaReservaResto/{idReserva}")
    public String darBajaReservaResto(ModelMap modelo, @PathVariable("idReserva") String idReserva) {
        /*Para eliminar la reserva, no basta con eliminar la reserva, si no vamos a dejar un carrito con sus reservas en la BD
        por esto primero eliminamos la reserva, luego las ordenes y luego el carrito(si eliminaramos en otro orden no podriamos ya que la reserva
        tiene clave foranea el carrito, MySql no me permite eliminar una clave foraena*/

        try {
            reservaServicio.eliminarReserva(idReserva);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
        }
        modelo.put("exito","La reserva fue eliminada con éxito.");
        return "redirect:/reserva/verReservasResto";
     
    }

}
