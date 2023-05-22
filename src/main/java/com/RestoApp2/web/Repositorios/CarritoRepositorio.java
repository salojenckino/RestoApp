 
package com.RestoApp2.web.Repositorios;

import com.RestoApp2.web.Entidades.Carrito;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CarritoRepositorio extends JpaRepository<Carrito,String> {
 
     
    @Query("SELECT c FROM Carrito c WHERE c.usuario.id = :idUsuario")
    public List<Carrito> buscarCarritoUsuario(@Param("idUsuario") String idUsuario);
    
    
     @Query("SELECT c FROM Carrito c WHERE c.usuario.id = :idUsuario")
    public Carrito buscarUnCarritoUsuario(@Param("idUsuario") String idUsuario);
    
    
}
