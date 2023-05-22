
package com.RestoApp2.web.Repositorios;

import com.RestoApp2.web.Entidades.Orden;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface OrdenRepositorio extends JpaRepository<Orden,String>{
    
    @Query("SELECT p FROM Orden p WHERE p.plato.id = :idPlato")
    public Orden buscarOrdenPorIdPlato(@Param("idPlato") String idPlato);
}
