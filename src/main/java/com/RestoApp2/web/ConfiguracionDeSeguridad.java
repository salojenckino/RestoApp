
package com.RestoApp2.web;

import com.RestoApp2.web.Servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true) //esto es para preautorizar
public class ConfiguracionDeSeguridad extends WebSecurityConfigurerAdapter{
    
    //Instancia del servicio de usuario para buscar en la BD un usuario por su nombre de usuario
    @Autowired
    public UsuarioServicio usuarioServicio; 
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .userDetailsService(usuarioServicio)        //configuracion del manejador de seguridad de Spring Security al cual le decimos cual es el servicio que debe utilizar para poedr autentificar un usuario
                .passwordEncoder(new BCryptPasswordEncoder());   //una vez autenticado el usuario con el usuarioServicio dice cual es el enconder que va utilizar para comparar las contraseñas
                //como funciona: cuando registramos un usuario en usuarioServicio, la contraseña que
                //el usuario lleno en el form de registro, la encriptamos con un Encoder que es del mismo tipo
                //que le pasamos -new BCryptPasswordEncoder()- al manejador de seguridad
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.headers().frameOptions().sameOrigin().and()
                .authorizeRequests()                                    //en estas tres lineas digo que 
                            .antMatchers("/css/*","/js/*","/img/*")     //los recursos que estan en esas 3 carpetas
                            .permitAll()                                //los pueda acceder cualquier usuario sin necesidad de estar logueado
                .and().formLogin()
                            .loginPage("/login")                        //configuramos el metodo login de la app
                                .loginProcessingUrl("/logincheck")      //Url que usa Spring Secutiry para que validar un usuario, esa URL es la que hay q usar en el form de login en la pagina login.html
                                .usernameParameter("username")          //nombre del parametro con los que van a viajar el nombre de usuario
                                .passwordParameter("password")          //idem con password
                                .defaultSuccessUrl("/")           //URL  a la que va a ingresar si el usuario se autentico con exito
                                .permitAll()    
                            .and().logout()
                                .logoutUrl("/logout")                   //URl de salida, con que URL el usuario se va deslogear del sistema     
                                .logoutSuccessUrl("/?logout")
                                .permitAll();
        http.csrf().disable();
    }
}
