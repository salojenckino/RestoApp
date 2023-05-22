package com.RestoApp2.web.Servicios;

import com.RestoApp2.web.Entidades.Resto;
import com.RestoApp2.web.Entidades.Usuario;
import com.RestoApp2.web.Enums.Role;
import com.RestoApp2.web.Repositorios.RestoRepositorio;
import com.RestoApp2.web.Repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Implementa palabras clave en el lenguaje de programación Java que se utiliza
 * para implementar una interfaz por una clase. Una interfaz en Java es un tipo
 * abstracto que se utiliza para especificar un contrato que debe ser
 * implementado por clases, que implementan esa interfaz. Por lo general, una
 * interfaz solo contendrá firmas de métodos y declaraciones constantes.
 * Cualquier interfaz que implemente una interfaz particular debe implementar
 * todos los métodos definidos en la interfaz, o debe declararse como una clase
 * abstracta. En Java, el tipo de una referencia de objeto se puede definir como
 * un tipo de interfaz. Pero ese objeto debe ser nulo o debe contener un objeto
 * de una clase, que implementa esa interfaz en particular. Al utilizar la
 * palabra clave Implements en Java, puede implementar múltiples interfaces en
 * una sola clase. Una interfaz no puede implementar otra interfaz. Sin embargo,
 * una interfaz puede extender una clase.
 *
 * La palabra clave Implements se usa para que una clase implemente una interfaz
 * determinada, mientras que la palabra clave Extends se usa para que una
 * subclase se extienda desde una súper clase. Cuando una clase implementa una
 * interfaz, esa clase necesita implementar todos los métodos definidos en la
 * interfaz, pero cuando una subclase extiende una súper clase, puede o no
 * reemplazar los métodos incluidos en la clase primaria.
 *
 * @author Federico
 */
@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio uR;
    @Autowired
    private RestoRepositorio rR;

    @Autowired
    private MailServicio mS;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {

        Usuario usuario = uR.buscarPorMail(mail);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList();
            permisos.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()));

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession();

            session.setAttribute("usuariosession", usuario);
            User user = new User(usuario.getMail(), usuario.getClave(), permisos);

            return user;
        } else {
            return null;
        }
    }

    @Transactional
    public void registroUsuarioUsuario(String nombre, String apellido, String mail, String clave1, String clave2) throws ErrorServicio {
        validar(clave1, clave2);

        Usuario usuario = uR.buscarPorMail(mail);
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setMail(mail);

            String encriptada = new BCryptPasswordEncoder().encode(clave1);
            usuario.setClave(encriptada);
            usuario.setAlta(new Date());

            usuario.setRol(Role.USER);
            uR.save(usuario);

            mS.enviarMail("Registro exitoso", "RestoApp", usuario.getMail());
        } else {
            throw new ErrorServicio("MAIL YA REGISTRADO");
        }
    }

    @Transactional
    public void registroRestoUsuario(String nombre, String apellido, String mail, String clave1, String clave2) throws ErrorServicio {
        validar(clave1, clave2);

        Usuario usuario = uR.buscarPorMail(mail);
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setMail(mail);

            String encriptada = new BCryptPasswordEncoder().encode(clave1);
            usuario.setClave(encriptada);
            usuario.setAlta(new Date());

            usuario.setRol(Role.SELLER);

            uR.save(usuario);
            /*Crear el nuevo resto, este resto tendra el mismo del id del usuario que se esta creando.*/
            String idUsuario = uR.buscarPorMail(mail).getId();
            Resto resto = new Resto();
            resto.setAbierto(true);
            resto.setFoto(null);
            resto.setId(idUsuario);
            rR.save(resto);

            mS.enviarMail("Registro exitoso", "RestoApp", usuario.getMail());
        } else {
            throw new ErrorServicio("MAIL YA REGISTRADO");
        }
    }

    @Transactional
    public void registroAdminUsuario(String nombre, String apellido, String mail, String clave1, String clave2) throws ErrorServicio {
        validar(clave1, clave2);

        Usuario usuario = uR.buscarPorMail(mail);
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setMail(mail);

            String encriptada = new BCryptPasswordEncoder().encode(clave1);
            usuario.setClave(encriptada);
            usuario.setAlta(new Date());

            usuario.setRol(Role.ADMIN);
            uR.save(usuario);

            mS.enviarMail("Registro exitoso", "RestoApp", usuario.getMail());
        } else {
            throw new ErrorServicio("MAIL YA REGISTRADO");
        }

    }

    @Transactional
    public void actualizarUsuario(String id, String nombre, String apellido, String clave1, String clave2) throws ErrorServicio {
        /*Optional es un objeto contenedor que puede o no contener un valor no nulo.
        Si el valor es presente, isPresent() devuelve true y get() retorna el valor.
        Si es  null y no utilizamos Optional<>, puede provocar la exception NullPointerException
        Otras utilidades del Optional en ves de utilizar el ifPresent-->
        
        orElse() retorna un valor por defecto si el valor no se encuentra presente
        String value = emptyOptional.orElse("default Value");
        
        orElseThrow() similar a orElseGet; pero lanza una exception en caso de que el valor sea null.
         String value = Optional.ofNullable(nullName).orElseThrow(NullPointerException::new);
         */
 /*Pongo la validación previa a todo el proceso para no continuar el codigo si ya no cumple con los requisitos minimos*/
        validar(clave1, clave1);
        Optional<Usuario> respuesta = uR.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            usuario.setNombre(nombre);
            usuario.setApellido(apellido);

            String encriptada = new BCryptPasswordEncoder().encode(clave1);
            usuario.setClave(encriptada);
            uR.save(usuario);
        } else {
            throw new ErrorServicio("El usuario no se encontró");
        }
    }

    @Transactional
    public void darBajaUsuario(String id) throws ErrorServicio {

        /*Pensaba que a la hora de trabajar con usuarios y listar los usuarios ,podria haber sido de utilidad poner un booleano que indique el estado de alta/baja
        pero podemos ver de trabajar esto: si el usuario tiene una fecha de baja != de null es porque esta dado de baja, 
        si nos complica manejarlo de esa forma si ya  podemos poner esta ayuda del booleano*/
        Optional<Usuario> respuesta = uR.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            uR.delete(usuario);
        } else {
            throw new ErrorServicio("El usuario buscado no fue encontrado");
        }

    }

    public Usuario buscarUsuarioPorId(String id) throws ErrorServicio {
        try {
            Usuario usuario = uR.getById(id);
            return usuario;
        } catch (Exception e) {
            throw new ErrorServicio("El usuario buscado no se encontro.");
        }

    }

    private void validar(String clave1, String clave2) throws ErrorServicio {

        if (clave1 == null || clave1.isEmpty()) {
            throw new ErrorServicio("La clave no puede estar vacia");
        }
        if (clave1.length() < 4) {
            throw new ErrorServicio("La clave no puede tener menos de 4 dígitos");
        }
        if (clave1.equals(clave2)) {

        } else {
            throw new ErrorServicio("Las claves deben ser iguales");
        }
    }
}
