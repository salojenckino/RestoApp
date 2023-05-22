package com.RestoApp2.web.Controladores;

import com.RestoApp2.web.Entidades.Plato;
import com.RestoApp2.web.Entidades.Resto;
import com.RestoApp2.web.Servicios.ErrorServicio;
import com.RestoApp2.web.Servicios.PlatoServicio;
import com.RestoApp2.web.Servicios.RestoServicio;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SELLER', 'ROLE_USER')")
@Controller
@RequestMapping("/buscador")
public class BuscadorControlador {

    @Autowired
    private PlatoServicio platoServicio;
    @Autowired
    private RestoServicio restoServicio;

    @GetMapping("/buscadorResto")
    public String buscarRestoPorPlato(ModelMap modelo) {

        return "buscador";
    }

    @PostMapping("/resultadoBusquedaResto")
    public String resultadoDbusquedaResto(ModelMap modelo, @RequestParam String busquedaResto) {
        List<Plato> platos = platoServicio.buscarPlatosEnResto(busquedaResto);
        TreeSet<String> idRestos = new TreeSet();
        for (Plato plato : platos) {
            idRestos.add(plato.getResto().getId());
        }
        List<Resto> restos = new ArrayList();
        for (String idResto : idRestos) {

            try {
                restos.add(restoServicio.buscarResto(idResto));
            } catch (ErrorServicio ex) {
                modelo.put("error", ex.getMessage());
            }
        }
        
        modelo.put("restosbuscados", restos);

        return "buscador";
    }
}
