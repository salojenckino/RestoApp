
package com.RestoApp2.web.Servicios;

import com.RestoApp2.web.Entidades.Foto;
import com.RestoApp2.web.Entidades.Resto;
import com.RestoApp2.web.Entidades.Zona;
import com.RestoApp2.web.Repositorios.RestoRepositorio;
import com.RestoApp2.web.Repositorios.ZonaRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class RestoServicio {

    @Autowired
    private ZonaRepositorio zR;
    @Autowired
    private RestoRepositorio rR;
    @Autowired
    private FotoServicio fS;

    @Transactional
    public void registroResto(String idUsuario, String nombre, String idZona, MultipartFile archivo, Boolean abierto) throws ErrorServicio {
        validacion(nombre, idZona);

        Optional<Resto> respuesta = rR.findById(idUsuario);
        if (respuesta.isPresent()) {
            Resto resto = respuesta.get();
            resto.setNombre(nombre);
            Optional<Zona> respZona = zR.findById(idZona);
            if (respZona.isPresent()) {
                Zona zona = respZona.get();
                resto.setZona(zona);
            }
            Foto foto = fS.guardarFoto(archivo);
            resto.setFoto(foto);
            resto.setAbierto(true);
            rR.save(resto);
        }

    }

    @Transactional
    public void modificarResto(String idUsuario, String nombre, String idZona, MultipartFile archivo, Boolean abierto) throws ErrorServicio {
        validacion(nombre, idZona);

        Optional<Resto> respuesta = rR.findById(idUsuario);
        if (respuesta.isPresent()) {
            Resto resto = respuesta.get();
            resto.setNombre(nombre);
            Optional<Zona> respuestaZona = zR.findById(idZona);
            if (respuestaZona.isPresent()) {
                Zona zona = respuestaZona.get();
                resto.setZona(zona);

                
                if(resto.getFoto()!=null){
                Foto foto = fS.actualizarFoto(resto.getFoto().getId(), archivo);
                resto.setFoto(foto);
                
                }else{
                Foto primeraFoto = fS.guardarFoto(archivo);
                resto.setFoto(primeraFoto);
                }
            }
            rR.save(resto);
        }

    }

    @Transactional
    public void darDeBaja(String idResto) throws ErrorServicio {
        Optional<Resto> respuesta = rR.findById(idResto);
        if (respuesta.isPresent()) {
            Resto resto = respuesta.get();
            resto.setAbierto(false);
            rR.save(resto);
        }

    }

    public Resto buscarResto(String id) throws ErrorServicio {

        Optional<Resto> respuesta = rR.findById(id);
        if (respuesta.isPresent()) {
            Resto resto = respuesta.get();
            return resto;
        } else {
            throw new ErrorServicio("No se encontro el restaurante búscado.");
        }

    }

    private void validacion(String nombre, String idZona) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede estar vacío");
        }
        /*No estoy seguro de esto, si la zona no esta presente tendria que tirar ese error(pero como con el html
        si o si vamos a traer una id de alguna zona si no va a ser posible visualizarla en el desplegable
        ,esto puede servir solamente en el caso que queramos guardar un resto y no haber creado las zonas antes supongo
        y no nos permita poner zona null*/
        Optional<Zona> respuesta = zR.findById(idZona);
        if (!respuesta.isPresent()) {
            throw new ErrorServicio("La zona seleccionada no se encontró");
        }
    }
    
    public List<Resto> listaResto(){
        return rR.findAll();
    }
    
}
