
package com.RestoApp2.web.Entidades;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;


@Entity
public class Resto implements Serializable{
    
    @Id
    private String id;
    private String nombre;
    private Zona zona;
  
    @OneToOne
    private Foto foto;
    
    
    private Boolean abierto;

    public Resto() {
    }

    public Resto(String id, String nombre, Zona zona, Foto foto, Boolean abierto) {
        this.id = id;
        this.nombre = nombre;
        this.zona = zona;
        this.foto = foto;
        this.abierto = abierto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    public Boolean getAbierto() {
        return abierto;
    }

    public void setAbierto(Boolean abierto) {
        this.abierto = abierto;
    }

    
}
