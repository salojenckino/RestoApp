
package com.RestoApp2.web.Entidades;

import com.RestoApp2.web.Enums.Categoria;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Plato implements Serializable{
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private String nombre;
    private String descri;
    private Double precio;
    private Boolean alta;
    
    @OneToOne
    private Foto foto;
        
    //@Enumerated(EnumType.STRING)
    private Categoria categoria;
    
    @ManyToOne
    private Resto resto;
    
        
    public Plato() {
    }

    public Plato( String nombre, String descri, Double precio, Foto foto, Boolean alta, Categoria categoria, Resto resto) {
        
        this.nombre = nombre;
        this.descri = descri;
        this.precio = precio;
        this.foto = foto;
        this.alta = alta;
        this.categoria = categoria;
        this.resto = resto;
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

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Resto getResto() {
        return resto;
    }

    public void setResto(Resto resto) {
        this.resto = resto;
    }

}
