package com.RestoApp2.web.Servicios;

import com.RestoApp2.web.Entidades.Foto;
import com.RestoApp2.web.Entidades.Plato;
import com.RestoApp2.web.Entidades.Resto;
import com.RestoApp2.web.Enums.Categoria;
import com.RestoApp2.web.Repositorios.PlatoRepositorio;
import com.RestoApp2.web.Repositorios.RestoRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PlatoServicio {

    @Autowired
    private FotoServicio fotoServi;

    @Autowired
    private PlatoRepositorio platoRepo;

    @Autowired
    private RestoRepositorio restoRepo;

    @Transactional
    public void nuevoPlato(MultipartFile archivo, String nombre, String descri, Double precio, Integer categoria, String idResto) throws ErrorServicio {

        Plato plato = new Plato();
        plato.setNombre(nombre);
        plato.setDescri(descri);
        plato.setPrecio(precio);
        plato.setAlta(Boolean.TRUE);

        Foto foto = fotoServi.guardarFoto(archivo);
        plato.setFoto(foto);

        switch (categoria) {
            case 1:
                plato.setCategoria(Categoria.ENTRADA);
                break;
            case 2:
                plato.setCategoria(Categoria.PRINCIPAL);
                break;
            case 3:
                plato.setCategoria(Categoria.POSTRE);
                break;
            case 4:
                plato.setCategoria(Categoria.BEBIDA);
                break;
            default:
                plato.setCategoria(Categoria.BEBIDA);
        }

        Resto resto = restoRepo.getById(idResto);
        plato.setResto(resto);

        platoRepo.save(plato);
    }

    @Transactional
    public void modificarPlato(MultipartFile archivo, String id,
            String nombre, Double precio, String descrip)
            throws ErrorServicio {

        Optional<Plato> rta = platoRepo.findById(id);
        if (rta.isPresent()) {
            Plato plato = rta.get();
            plato.setNombre(nombre);
            plato.setPrecio(precio);
            plato.setDescri(descrip);

            String idFoto = null;
            if (plato.getFoto() != null) {
                idFoto = plato.getFoto().getId();
            }

            Foto foto = fotoServi.actualizarFoto(idFoto, archivo);
            plato.setFoto(foto);
            platoRepo.save(plato);
        } else {
            throw new ErrorServicio("Plato NO ENCONTRADO");
        }
    }

    @Transactional
    public void bajaPlato(String id) throws ErrorServicio {
        Optional<Plato> rta = platoRepo.findById(id);
        if (rta.isPresent()) {
            Plato plato = rta.get();
            plato.setAlta(Boolean.FALSE);
            platoRepo.save(plato);
        } else {
            throw new ErrorServicio("Plato NO ENCOMTRADO");
        }
    }

    @Transactional
    public void altaPlato(String id) throws ErrorServicio {
        Optional<Plato> rta = platoRepo.findById(id);
        if (rta.isPresent()) {
            Plato plato = rta.get();
            plato.setAlta(Boolean.TRUE);
            platoRepo.save(plato);
        } else {
            throw new ErrorServicio("Plato NO ENCOMTRADO");
        }
    }

    public Plato buscarPorId(String id) throws ErrorServicio {
        Optional<Plato> rta = platoRepo.findById(id);
        if (rta.isPresent()) {
            Plato plato = rta.get();
            return plato;
        } else {
            throw new ErrorServicio("Plato NO ENCONTRADO");
        }
    }

    public List<Plato> listaPlato() {
        return platoRepo.findAll();
    }

    public List<Plato> listaPlatoResto(String idResto) {
        return platoRepo.buscarPlatoResto(idResto);
    }

    public List<Plato> listaPlatosInactivos(String idResto) {
        return platoRepo.buscarPlatosInactivos(idResto);
    }
    
    public List<Plato> buscarPlatosEnResto(String busquedaResto){
        return platoRepo.buscarPlatosEnResto(busquedaResto);
    }
    

}
