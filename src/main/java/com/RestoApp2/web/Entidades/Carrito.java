package com.RestoApp2.web.Entidades;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;


@Entity
public class Carrito implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private ArrayList<String> IdOrden;

    @ManyToOne
    private Resto resto;

    @ManyToOne
    private Usuario usuario;

    public String getId() {
        return id;
    }

    public ArrayList<String> getIdOrden() {
        return IdOrden;
    }

    public void setIdOrden(ArrayList<String> IdOrden) {
        this.IdOrden = IdOrden;
    }

    public Resto getResto() {
        return resto;
    }

    public void setResto(Resto resto) {
        this.resto = resto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
