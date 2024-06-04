package com.elaparato.service;

import com.elaparato.model.Producto;
import com.elaparato.model.Venta;
import com.elaparato.repository.IVentaRepository;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class VentaService implements IVentaService{

    @Autowired
    private IVentaRepository ventaRepo;

    @Autowired
    private ProductoService prodService;


    @Override
    public List<Venta> getVentas() {
        return ventaRepo.findAll();
    }

    @Override
    public void saveVenta(Venta vent) {
        List<Producto> products = vent.getListaProductos();
        List<Producto> parsedProducts = new ArrayList<>();


        products.forEach(prod -> {
            log.info("Guardando producto: " + prod.toString());
            parsedProducts.add(prodService.findProducto(prod.getId()));
        });


        vent.setListaProductos(parsedProducts);

        if(vent.getFecha() == null) vent.setFecha(new Date());

        //log.info("Guardando venta: " + vent.toString());

        ventaRepo.save(vent);

    }

    @Override
    public void deleteVenta(int id) {
        ventaRepo.deleteById(id);
    }

    @Override
    public Venta findVenta(int id) {
       return ventaRepo.findById(id).orElse(null);
    }

    @Override
    public void editVenta(Venta vent) {
        this.saveVenta(vent);
    }

    }
