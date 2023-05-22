package com.RestoApp2.web.Servicios;

import com.RestoApp2.web.Entidades.Mesa;
import com.RestoApp2.web.Entidades.Resto;
import com.RestoApp2.web.Repositorios.MesaRepositorio;
import com.RestoApp2.web.Repositorios.RestoRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MesaServicio {

    @Autowired
    private RestoRepositorio restoRepo;
    @Autowired
    private MesaRepositorio mesaRepo;

    @Transactional
    public void crearMesa(Integer capacidad,String denominacion, String idResto) throws ErrorServicio {

        Mesa mesa = new Mesa();
        mesa.setCapacidad(capacidad);
        mesa.setDisponible(true);
        mesa.setDenominacion(denominacion);
        Resto resto = restoRepo.getById(idResto);
        mesa.setResto(resto);
        
        mesaRepo.save(mesa);
    }
    
    @Transactional
    public void modificarMesa(String idMesa, Integer capacidad,String denominacion)throws ErrorServicio{
        Optional<Mesa> respuesta = mesaRepo.findById(idMesa);
        if (respuesta.isPresent()) {
            Mesa mesa = respuesta.get();
            mesa.setCapacidad(capacidad);
            mesa.setDenominacion(denominacion);
            mesaRepo.save(mesa);
        }else{
            throw new ErrorServicio("No se encontro la mesa a modificar");
        }
    }
    
    public List<Mesa> listarMesaResto(String idResto){
        return mesaRepo.buscarMesaResto(idResto);
    }
    
    public List<Mesa> buscarMesasInactivas(String idResto) {
        return mesaRepo.buscarMesasInactivas(idResto);
    }
    
    @Transactional
    public void bajaMesa(String id) throws ErrorServicio {
        Optional<Mesa> rta = mesaRepo.findById(id);
        if (rta.isPresent()) {
            Mesa mesa = rta.get();
            mesa.setDisponible(Boolean.FALSE);
            mesaRepo.save(mesa);
        } else {
            throw new ErrorServicio("Mesa NO ENCOMTRADA");
        }
    }   

    @Transactional
    public void altaMesa(String id) throws ErrorServicio {
        Optional<Mesa> rta = mesaRepo.findById(id);
        if (rta.isPresent()) {
            Mesa mesa = rta.get();
            mesa.setDisponible(Boolean.TRUE);
            mesaRepo.save(mesa);
        } else {
            throw new ErrorServicio("Mesa NO ENCOMTRADA");
        }
    }
    
    public Mesa buscarPorId(String id) throws ErrorServicio {
        Optional<Mesa> rta = mesaRepo.findById(id);
        if (rta.isPresent()) {
            Mesa mesa = rta.get();
            return mesa;
        } else {
            throw new ErrorServicio("Mesa NO ENCOMTRADA");
        }
    }
    
}
