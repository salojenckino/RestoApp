
package com.RestoApp2.web.Controladores;

import com.RestoApp2.web.Entidades.Zona;
import com.RestoApp2.web.Repositorios.ZonaRepositorio;
import com.RestoApp2.web.Servicios.ErrorServicio;
import com.RestoApp2.web.Servicios.ZonaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@Controller
@RequestMapping("/zona")
public class ZonaControlador {

    @Autowired
    private ZonaServicio zS;
    @Autowired
    private ZonaRepositorio zR;

    @GetMapping("/crearZona")
    public String creaZona() {

        return "zonaCrear.html";
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/persistirZona")
    public String Zona(ModelMap modelo, @RequestParam String nombre) {

        try {
            zS.registroZona(nombre);
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
        return "zonaCrear";
        }
        modelo.put("exito","La zona fue ingresada con éxito.");
        return "zonaCrear";
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/listarZonas")
    public String listarZonas(ModelMap modelo){
        List<Zona> zonas = zR.findAll();
        modelo.put("zonas",zonas);
        return "zonaListar.html";
        
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/modificarZona/{id}")
    public String modificarZona(ModelMap modelo,@PathVariable("id") String id){
        try{
            Zona zona =zS.buscarPorId(id);
            modelo.put("zona",zona);
        }catch(ErrorServicio e){
            modelo.put("error",e.getMessage());
            
        }
        return "zonaEditar.html";
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/actualizarZona")
    public String acutalizarZona(ModelMap modelo, @RequestParam String id, @RequestParam String nombre) {

       try{
                zS.actualizarZona(id, nombre);
                List<Zona> zonas = zR.findAll();
            modelo.put("exito","La zona fue modificada con éxito");
            modelo.put("zonas",zonas);
            return "zonaListar.html";
           
            
            
        } catch (ErrorServicio e) {
            modelo.put("error",e.getMessage());
            List<Zona> zonas = zR.findAll();
            modelo.put("zonas",zonas);
            return "zonaListar.html";
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/bajaZona/{id}")
    public String bajaZona(ModelMap modelo,@PathVariable("id")String id){
         try{
            zS.darBajaZona(id);
             return "redirect:/zona/listarZonas/";
        }catch(ErrorServicio e){
            modelo.put("error",e.getMessage());
             return "redirect:/zona/listarZonas/";
        }
         
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/altaZona/{id}")
    public String altaZona(ModelMap modelo,@PathVariable("id")String id){
         try{
            zS.darAltaZona(id);
             return "redirect:/zona/listarZonas/";
        }catch(ErrorServicio e){
            modelo.put("error",e.getMessage());
             return "redirect:/zona/listarZonas/";
        }
         
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/eliminarZona/{id}")
    public String eliminarZona(ModelMap modelo,@PathVariable("id")String id){
        try{
            zS.eliminarZona(id);
            modelo.put("exito","Se eliminó la zona con éxito");
             return "redirect:/zona/listarZonas/";
        }catch(ErrorServicio error){
            modelo.put("error",error.getMessage());
             return "redirect:/zona/listarZonas/";
        }
    }
    
}
