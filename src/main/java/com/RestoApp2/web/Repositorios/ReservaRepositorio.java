
package com.RestoApp2.web.Repositorios;

import com.RestoApp2.web.Entidades.Carrito;
import com.RestoApp2.web.Entidades.Reserva;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ReservaRepositorio extends JpaRepository<Reserva, String> {
    
    
    
    @Query("SELECT r FROM Reserva r WHERE r.usuario.id = :idUsuario")
    public List<Reserva> buscarReservasUsuario(@Param("idUsuario") String idUsuario);
    
     @Query("SELECT r FROM Reserva r WHERE r.resto.id = :idResto")
    public List<Reserva> buscarReservasResto(@Param("idResto") String idResto);
}
