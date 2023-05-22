
package com.RestoApp2.web.Repositorios;

import com.RestoApp2.web.Entidades.Mesa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MesaRepositorio extends JpaRepository<Mesa, String>{
    
    @Query("SELECT c FROM Mesa c WHERE c.id = :id")
    public List<Mesa> buscarMesaId(@Param("id") String id);
    
    @Query("SELECT c FROM Mesa c WHERE c.capacidad>=1")
    public List<Mesa> buscarMesaCapacidad(@Param("capacidad") Integer capacidad);
    
    @Query("SELECT c FROM Mesa c WHERE c.resto.id = :idResto and c.disponible = true")
    public List<Mesa> buscarMesaResto(@Param("idResto") String idResto); 
    
    @Query("SELECT c FROM Mesa c WHERE c.resto.id = :idResto and c.disponible = false")
    public List<Mesa> buscarMesasInactivas(@Param("idResto") String idResto); 
    
    @Query("SELECT c FROM Mesa c WHERE c.resto.id = :idResto")
    public String idResto(@Param("idResto") String idResto);
    
    
}
