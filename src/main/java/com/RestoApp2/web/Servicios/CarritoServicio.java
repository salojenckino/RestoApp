package com.RestoApp2.web.Servicios;

import com.RestoApp2.web.Entidades.Carrito;
import com.RestoApp2.web.Entidades.Orden;
import com.RestoApp2.web.Repositorios.CarritoRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarritoServicio {

    @Autowired
    private CarritoRepositorio cR;
    @Autowired
    private UsuarioServicio uS;
    @Autowired
    private RestoServicio rS;
    @Autowired
    private OrdenServicio oS;

    @Transactional
    public Carrito crearCarrito(String idResto, String idUsuario) throws ErrorServicio {
        /*Cuando el usuario entra a ver un restaurante automaticamente se crea un carrito
         este solo tiene el idResto y idUsuario, , previendo que es un usuario User, y no vamos
         a poder acceder luego de fácil forma al idResto
        El carrito id lo vamos a tener que ir pasando en los parametros en los botones del html
        por eso preveo solo pasar este parametro y ya dejar seteado idResto e idUsuario 
        para mejor acceso.*/
        Carrito carrito = new Carrito();
        ArrayList<String> ordenes = new ArrayList();
        carrito.setIdOrden(ordenes);

        try {
            carrito.setUsuario(uS.buscarUsuarioPorId(idUsuario));
        } catch (ErrorServicio ex) {
            throw new ErrorServicio("El usuario no se encontró o debe estar logueado en la plataforma");
        }
        carrito.setResto(rS.buscarResto(idResto));
        cR.save(carrito);
        return carrito;
    }

    @Transactional
    public Carrito agregarPlato(String idCarrito, String idOrden) throws ErrorServicio {
        /*Cuando el usuario agrega platos a su reserva*/
        Optional<Carrito> respuesta = cR.findById(idCarrito);

        if (respuesta.isPresent()) {
            Carrito carrito = respuesta.get();

            Orden orden = oS.buscarOrden(idOrden);

            ArrayList<String> ordenes = carrito.getIdOrden();

            ordenes.add(orden.getId());

            carrito.setIdOrden(ordenes);

            cR.save(carrito);

            return carrito;
        } else {
            throw new ErrorServicio("No se encontró el carrito.");
        }
    }
    
    @Transactional
    public Carrito eliminarPlato(String idCarrito, String idOrden) throws ErrorServicio {
        Optional<Carrito> respuesta = cR.findById(idCarrito);
        System.out.println("idCarrito: "+respuesta);
        
        if (respuesta.isPresent()) {
            Carrito carrito = respuesta.get();

            Orden orden = oS.buscarOrden(idOrden);
            System.out.println("idOrden: "+idOrden);

            ArrayList<String> ordenes = carrito.getIdOrden();

            ordenes.remove(orden.getId());

            //carrito.setIdOrden(ordenes);

            cR.save(carrito);

            return carrito;
        } else {
            throw new ErrorServicio("No se encontró el carrito.");
        }
    }

    @Transactional
    public void eliminarCarritos(String idUsuario) throws ErrorServicio {
        /*Borra todos los carritos esto pasa cuando salis de algun restaurante y decidis buscar en otro.
        Borra solo el carrito del usuario logueado y sus ordenes relacionadas*/
        try {
            List<Carrito> carrito = cR.buscarCarritoUsuario(idUsuario);
            for (Carrito carrito1 : carrito) {

                ArrayList<String> ordenes = carrito1.getIdOrden();
                for (String ordene : ordenes) {
                    oS.borrarOrden(ordene);
                }
                cR.deleteById(carrito1.getId());
            }

        } catch (ErrorServicio e) {
            throw new ErrorServicio("no se encontro un o más carrito asociado a su usuario.");
        }

    }
    @Transactional
    public void eliminarCarrito(String idCarrito)throws ErrorServicio{
        try{
            Carrito carrito = cR.getById(idCarrito);
            ArrayList<String> ordenes= carrito.getIdOrden();
            for (String ordene : ordenes) {
                oS.borrarOrden(ordene);
            }
            cR.deleteById(idCarrito);
            
        }catch(Exception e){
            throw new ErrorServicio("No se pudo borrar el carrito");
        }
    }

    public List<Carrito> buscarCarritoIdUsuario(String idUsuario) throws ErrorServicio {
        try {
            List<Carrito> carrito = cR.buscarCarritoUsuario(idUsuario);
            return carrito;
        } catch (Exception e) {
            throw new ErrorServicio("No se encontró un carrito asociado a su usuario.");
        }

    }
    
    public Carrito buscarUnCarritoIdUsuario(String idUsuario) throws ErrorServicio {
        try {
            Carrito carrito = cR.buscarUnCarritoUsuario(idUsuario);
            return carrito;
        } catch (Exception e) {
            throw new ErrorServicio("No se encontró un carrito asociado a su usuario.");
        }

    }

    public Carrito buscarCarrito(String id) throws ErrorServicio {

        Optional<Carrito> respuesta = cR.findById(id);
        if (respuesta.isPresent()) {
            Carrito carrito = respuesta.get();
            return carrito;
        } else {
            throw new ErrorServicio("No se encontró el carrito");
        }

    }

}
