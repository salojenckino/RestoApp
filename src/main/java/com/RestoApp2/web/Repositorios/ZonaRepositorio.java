
package com.RestoApp2.web.Repositorios;

import com.RestoApp2.web.Entidades.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ZonaRepositorio extends JpaRepository<Zona, String> {
    
  
    
}
