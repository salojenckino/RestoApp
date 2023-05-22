
package com.RestoApp2.web.Servicios;

import com.RestoApp2.web.Entidades.Foto;
import com.RestoApp2.web.Repositorios.FotoRepositorio;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicio {
    
    @Autowired
    private FotoRepositorio fotoRepo;
    
    @Transactional
    public Foto guardarFoto(MultipartFile archivo){
        if (archivo != null && !archivo.isEmpty()) {
            try{
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setContenido(archivo.getBytes());
                return fotoRepo.save(foto);
            }catch(Exception e){
                System.out.println("Mensaje error: "+e.getMessage());
            }
        }
        return null;
    }
    
    @Transactional
    public Foto actualizarFoto(String idFoto, MultipartFile archivo){
        if (archivo!=null) {
            try{
                Foto foto = new Foto();
                Optional<Foto> rta = fotoRepo.findById(idFoto);
                if (rta.isPresent()) {
                    foto = rta.get();
                    foto.setMime(archivo.getContentType());
                    foto.setContenido(archivo.getBytes());
                    return fotoRepo.save(foto);
                }
                                                
            }catch(Exception e){
                System.out.println("Error al cargar la foto: "+e.getMessage());
            }
        }
        return null;
    }
    
}
