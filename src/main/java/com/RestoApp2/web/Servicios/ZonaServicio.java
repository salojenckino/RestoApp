
package com.RestoApp2.web.Servicios;

import com.RestoApp2.web.Entidades.Zona;
import com.RestoApp2.web.Repositorios.ZonaRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ZonaServicio {

    @Autowired
    private ZonaRepositorio zR;

    @Transactional
    public void registroZona(String nombre) throws ErrorServicio {
         
        List<Zona> zonas = zR.findAll();
        for (Zona zona : zonas) {
            if (zona.getNombre().equals(nombre)) {
                throw new ErrorServicio("La zona ya se encuentra en la base de datos.");
            }
        }
       
        Zona zona = new Zona();
        zona.setNombre(nombre);
        zona.setAlta(true);
        zR.save(zona);
    }

    @Transactional
    public void actualizarZona(String id, String nombre) throws ErrorServicio {

      
        List<Zona> zonas = zR.findAll();
        for (Zona zona : zonas) {
            if (zona.getNombre().equals(nombre)) {
                throw new ErrorServicio("La zona ya se encuentra en la base de datos.");
            }
        }
        Optional<Zona> respuesta = zR.findById(id);

        if (respuesta.isPresent()) {
         Zona zona =respuesta.get();
         zona.setNombre(nombre);
         
         zR.save(zona);
        }
    }

    @Transactional
    public void darBajaZona(String id) throws ErrorServicio {

        Optional<Zona> respuesta = zR.findById(id);
        if (respuesta.isPresent()) {
            Zona zona = respuesta.get();
            zona.setAlta(false);
            zR.save(zona);
        }
    }
    @Transactional
    public void eliminarZona(String id) throws ErrorServicio {

        Optional<Zona> respuesta = zR.findById(id);
        if (respuesta.isPresent()) {
            Zona zona = respuesta.get();
           
            zR.delete(zona);
        }else{
            throw new ErrorServicio("No se encontró la zona buscada");
        }
            
    }
    
    @Transactional
    public void darAltaZona(String id) throws ErrorServicio {

        Optional<Zona> respuesta = zR.findById(id);
        if (respuesta.isPresent()) {
            Zona zona = respuesta.get();
            zona.setAlta(true);
            zR.save(zona);
        }
    }
    
     public Zona buscarPorId(String id) throws ErrorServicio{
        Optional<Zona> rta = zR.findById(id);
        if (rta.isPresent()) {
            Zona zona = rta.get();
            return zona;
        }else{
            throw new ErrorServicio("Zona no encontrada");
        }
    }
    public List<Zona> listarZonas(){
        
       return zR.findAll();
    }

    
   
}
