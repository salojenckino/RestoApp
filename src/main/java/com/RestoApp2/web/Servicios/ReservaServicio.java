package com.RestoApp2.web.Servicios;

import com.RestoApp2.web.Entidades.Carrito;
import com.RestoApp2.web.Entidades.Mesa;
import com.RestoApp2.web.Entidades.Reserva;
import com.RestoApp2.web.Entidades.Resto;
import com.RestoApp2.web.Entidades.Usuario;
import com.RestoApp2.web.Repositorios.CarritoRepositorio;
import com.RestoApp2.web.Repositorios.MesaRepositorio;
import com.RestoApp2.web.Repositorios.ReservaRepositorio;
import com.RestoApp2.web.Repositorios.RestoRepositorio;
import com.RestoApp2.web.Repositorios.UsuarioRepositorio;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaServicio implements Serializable {

    @Autowired
    private ReservaRepositorio reservaRepo;
    @Autowired
    private CarritoRepositorio carritoRepo;
    @Autowired
    private UsuarioRepositorio usuarioRepo;
    @Autowired
    private RestoRepositorio restoRepo;
    @Autowired
    private MesaRepositorio mesaRepo;
    @Autowired
    private CarritoServicio carritoServicio;

    @Transactional
    public Reserva crearReserva(String idCarrito, String idUsuario, Integer cantidad, String idMesa, String dia, String hora) throws ErrorServicio {

        System.out.println(dia);
        formatearFecha(dia, hora);
        Reserva reserva = new Reserva();
        try {

            Carrito carrito = carritoRepo.getById(idCarrito);
            Usuario usuario = usuarioRepo.getById(carrito.getUsuario().getId());
            Resto resto = restoRepo.getById(carrito.getResto().getId());
            Mesa mesa = mesaRepo.getById(idMesa);

            reserva.setCarro(carrito);
            reserva.setMesa(mesa);
            reserva.setResto(resto);
            reserva.setUsuario(usuario);
            reserva.setCantidad(cantidad);
            reserva.setDia(formatearFecha(dia, hora));

            reservaRepo.save(reserva);
        } catch (Exception ex) {
            throw new ErrorServicio(ex.getMessage());
        }
        return reserva;
    }

    public Date formatearFecha(String dia, String hora) throws ErrorServicio {

        SimpleDateFormat fecha = new SimpleDateFormat("yyy-MM-dd HH:mm");
        String concatenada = dia.concat(" ");
        concatenada = concatenada.concat(hora);
        try {
            Date fechaCorrecta = fecha.parse(concatenada);
            return fechaCorrecta;
        } catch (ParseException ex) {
            throw new ErrorServicio(ex.getMessage());
        }

    }

    public Reserva buscarReserva(String idReserva) throws ErrorServicio {

        try {
            Reserva reserva = reservaRepo.getById(idReserva);
            return reserva;
        } catch (Exception a) {
            throw new ErrorServicio(a.getMessage());
        }

    }

    public List<Reserva> listaDeReservas(String idUsuario) throws ErrorServicio {

        List<Reserva> listaReservas = reservaRepo.buscarReservasUsuario(idUsuario);
        if (listaReservas.isEmpty() && listaReservas == null) {
            throw new ErrorServicio("No se encontró ninguna reserva para el usuario.");
        }
        return listaReservas;

    }
    @Transactional
    public void eliminarReserva(String idReserva)throws ErrorServicio{
        try{ 
            
        Reserva reserva = reservaRepo.getById(idReserva);
        
        carritoServicio.eliminarCarrito(reserva.getCarro().getId());
        reservaRepo.deleteById(idReserva);
        }catch(Exception e){
            throw new ErrorServicio("No se encontro o no se pudo borrar la reserva.");
        }
    }
    public List<Reserva> buscarReservaResto(String idresto)throws ErrorServicio{
        
        try{
            List<Reserva> reservas = reservaRepo.buscarReservasResto(idresto);
            return reservas;
        }catch(Exception e){
        throw new ErrorServicio("No se encontraron reservas relacionadas a su restorán.");
        }
        
    }
}
