package com.elaparato;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.elaparato.model.Producto;
import com.elaparato.model.Venta;
import com.elaparato.repository.IProductoRepository;
import com.elaparato.repository.IVentaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@RequiredArgsConstructor
@Log4j2
public class ElaparatoApplication implements CommandLineRunner {

  @Autowired
	private 	IProductoRepository productRepository;

  @Autowired
	private IVentaRepository ventaRepository;

	public static void main(String[] args) {
		SpringApplication.run(ElaparatoApplication.class, args);


	}


	@Override
	public void run(String... args) throws Exception {


        Producto prod1 = new Producto(1, "Lavadora Samsung", "Lavadora de carga frontal con capacidad de 15 kg", 1299.99, 10, new ArrayList<>());
        Producto prod2 = new Producto(2, "Refrigeradora LG", "Refrigeradora de 3 puertas con capacidad de 25 pies cúbicos", 1599.99, 5, new ArrayList<>());
        Producto prod3 = new Producto(3, "Televisor Sony", "Televisor LED 4K de 55 pulgadas con Android TV", 899.99, 15, new ArrayList<>());
        Producto prod4 = new Producto(4, "Horno eléctrico Whirlpool", "Horno eléctrico de acero inoxidable con capacidad de 6.4 pies cúbicos", 799.99, 7, new ArrayList<>());
        Producto prod5 = new Producto(5, "Aspiradora Dyson", "Aspiradora inalámbrica con tecnología Cyclone V10", 499.99, 20, new ArrayList<>());

        // Guardamos los productos en la base de datos
        productRepository.save(prod1);
        productRepository.save(prod2);
        productRepository.save(prod3);
        productRepository.save(prod4);
        productRepository.save(prod5);

         // Crear ventas y asociar productos
        Venta venta1 = new Venta(new Date(), new ArrayList<>(List.of(prod1, prod2, prod4)));
        Venta venta2 = new Venta(new Date(), new ArrayList<>(List.of(prod3, prod4, prod1)));
        Venta venta3 = new Venta(new Date(), new ArrayList<>(List.of(prod1, prod3, prod5)));
        Venta venta4 = new Venta(new Date(), new ArrayList<>(List.of(prod2, prod5)));
        Venta venta5 = new Venta(new Date(), new ArrayList<>(List.of(prod3, prod4)));

        ventaRepository.save(venta1);
        ventaRepository.save(venta2);
        ventaRepository.save(venta3);
        ventaRepository.save(venta4);
        ventaRepository.save(venta5);


        log.info("Datos iniciales insertados.");
	

	}
	/*
	 * (1, 'Lavadora Samsung', 'Lavadora de carga frontal con capacidad de 15 kg', 1299.99, 10),
  (2, 'Refrigeradora LG', 'Refrigeradora de 3 puertas con capacidad de 25 pies cúbicos', 1599.99, 5),
  (3, 'Televisor Sony', 'Televisor LED 4K de 55 pulgadas con Android TV', 899.99, 15),
  (4, 'Horno eléctrico Whirlpool', 'Horno eléctrico de acero inoxidable con capacidad de 6.4 pies cúbicos', 799.99, 7),
  (5, 'Aspiradora Dyson', 'Aspiradora inalámbrica con tecnología Cyclone V10', 499.99, 20);

--Insertar Ventas
INSERT INTO venta (id_venta, fecha)
VALUES
  (1, '2023-01-15'),
  (2, '2023-02-05'),
  (3, '2023-03-10'),
  (4, '2023-04-20'),
  (5, '2023-05-01');

	  (1, 1),
		(1, 2),
		(1, 4),
		(2, 3),
		(2, 4),
  (2, 1),
  (3, 1),
  (3, 3),
  (3, 5),
  (4, 2),
  (4, 5);
  (5, 3),
  (5, 4),
	 */

}
