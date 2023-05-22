
package com.RestoApp2.web.Controladores;

import com.RestoApp2.web.Entidades.Usuario;
import com.RestoApp2.web.Servicios.ErrorServicio;
import com.RestoApp2.web.Servicios.UsuarioServicio;
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


@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio uS;

    @GetMapping("/usuarioUser")
    public String usuarioUser() {
        return "registroUsuario.html";
    }

    @PostMapping("/registroUsuario")
    public String registroUsuario(ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String email, @RequestParam String clave1, @RequestParam String clave2) {

        try {
            uS.registroUsuarioUsuario(nombre, apellido, email, clave1, clave2);
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("email", email);
            return "registroUsuario.html";
        }
        modelo.put("exito", "Su usuario fue registrado con éxito");
        return "registroUsuario.html";

    }

    @GetMapping("/usuarioSeller")
    public String usuarioResto() {
        return "registroUsuarioResto.html";
    }

    @PostMapping("/registroUsuarioResto")
    public String registroUsuarioResto(ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String email, @RequestParam String clave1, @RequestParam String clave2) {

        try {
            uS.registroRestoUsuario(nombre, apellido, email, clave1, clave2);
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("email", email);
            return "registroUsuarioResto.html";
        }
        modelo.put("exito", "Su usuario fue registrado con éxito");
        return "registroUsuarioResto.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_SELLER')")
    @GetMapping("/editarPerfil/{id}")
    public String editarPerfil(HttpSession session,ModelMap model,@PathVariable("id")String id) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "index.html";/*Medida de seguridad*/
        }
       
        try {
            Usuario usuario = uS.buscarUsuarioPorId(login.getId());
            
            model.put("usuario", usuario);
            return "modificarUsuario.html";
        } catch (ErrorServicio error) {
            model.put("error", error.getMessage());
            return "index.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_SELLER')")
    @PostMapping("/actualizarPerfil")
    public String actualizarPerfil(HttpSession session,ModelMap modelo,@RequestParam String id,@RequestParam String nombre,@RequestParam String apellido,@RequestParam String clave1,@RequestParam String clave2){
        
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "index.html";/*Medida de seguridad*/
        }
        try{
            uS.actualizarUsuario(id, nombre,apellido, clave1, clave2);
            modelo.put("exito","El usuario fue modificado con éxito");
            Usuario usuario = uS.buscarUsuarioPorId(id); /*Esto se hace para que al momento de actualizar, tambien se actualice el usuario que esta cargando en la session
                                                         ya que por defecto estan cargados los datos del usuario que ingreso a modificar sus atributos*/
            
            session.setAttribute("usuariosession",usuario);
            return "redirect:/";
        }catch(ErrorServicio error){
            modelo.put("error",error);
            return "index.html";
        }
        
        
    }
     @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_SELLER')")
     @GetMapping("/bajaPerfil/{id}")
     public String bajaPerfil(HttpSession session,ModelMap modelo,@PathVariable("id") String id){
         
          Usuario login = (Usuario) session.getAttribute("usuariosession");
          if (login == null || !login.getId().equals(id)) {
            return "index.html";/*Medida de seguridad*/
          }
          
         try{
             uS.darBajaUsuario(id);
             return "confirmacionDeUsuario.html";
         }catch(ErrorServicio error){
             modelo.put("error",error.getMessage());
             return "redirect:/usuario/editarPerfil";
         }
     }

    

}
