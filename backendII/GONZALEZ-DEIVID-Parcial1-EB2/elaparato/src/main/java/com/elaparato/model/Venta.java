package com.elaparato.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter @Setter
@ToString
@Entity
@NoArgsConstructor
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id_venta;
    private Date fecha;
    //@OneToMany(mappedBy="venta")
    @ManyToMany (cascade = CascadeType.MERGE)
    private List<Producto> listaProductos = new ArrayList<>();

    public Venta(Date date, List<Producto> listaProductos) {
        this.fecha = date;
        this.listaProductos = listaProductos;
    }

    public Venta(Date date) {
        this.fecha = date;
    }
}
