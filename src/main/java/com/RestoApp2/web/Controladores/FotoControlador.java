package com.RestoApp2.web.Controladores;


import com.RestoApp2.web.Entidades.Plato;
import com.RestoApp2.web.Entidades.Resto;
import com.RestoApp2.web.Servicios.ErrorServicio;
import com.RestoApp2.web.Servicios.PlatoServicio;
import com.RestoApp2.web.Servicios.RestoServicio;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/foto")
public class FotoControlador {

    @Autowired
    private PlatoServicio platoServi;

    @Autowired
    private RestoServicio restoServi;

    @GetMapping("/platoFoto/{id}")
    public ResponseEntity<byte[]> platoFoto(@PathVariable("id") String id) {
        try {
            Plato plato = platoServi.buscarPorId(id);
            if (plato.getFoto() == null) {
                throw new ErrorServicio("El plato no tiene foto");
            }
            byte[] foto = plato.getFoto().getContenido();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            java.util.logging.Logger.getLogger(FotoControlador.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/restoFoto/{id}")
    public ResponseEntity<byte[]> fotoResto(@PathVariable("id") String id) {     
        try {
            Resto resto = restoServi.buscarResto(id);
            if (resto.getFoto() == null) {
                throw new ErrorServicio("El restaurante no tiene una foto asignada");
            }
            byte[] foto = resto.getFoto().getContenido();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
                    
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            java.util.logging.Logger.getLogger(FotoControlador.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

