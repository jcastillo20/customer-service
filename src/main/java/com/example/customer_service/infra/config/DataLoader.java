package com.example.customer_service.infra.config;

import java.time.LocalDate;

import com.example.customer_service.domain.model.Customer;
import com.example.customer_service.infra.adapter.CustomerRepositoryPort;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final CustomerRepositoryPort customerRepository;

    public DataLoader(CustomerRepositoryPort customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {
        Customer c1 = new Customer(
                null,
                "Juan",
                "Pérez",
                30,
                LocalDate.of(1995, 5, 15),
                LocalDate.of(1995, 5, 15).plusYears(80)
        );

        Customer c2 = new Customer(
                null,
                "María",
                "Gómez",
                25,
                LocalDate.of(2000, 3, 20),
                LocalDate.of(2000, 3, 20).plusYears(80)
        );

        Customer c3 = new Customer(
                null,
                "Carlos",
                "López",
                40,
                LocalDate.of(1983, 10, 5),
                LocalDate.of(1983, 10, 5).plusYears(80)
        );

        Customer c4 = new Customer(
                null,
                "Ana",
                "Martínez",
                35,
                LocalDate.of(1988, 7, 10),
                LocalDate.of(1988, 7, 10).plusYears(80)
        );

        Customer c5 = new Customer(
                null,
                "Luis",
                "Ramírez",
                50,
                LocalDate.of(1973, 1, 20),
                LocalDate.of(1973, 1, 20).plusYears(80)
        );

        // Guardamos todos los registros en el repositorio
        customerRepository.save(c1);
        customerRepository.save(c2);
        customerRepository.save(c3);
        customerRepository.save(c4);
        customerRepository.save(c5);

        System.out.println("Datos de prueba cargados exitosamente en H2.");
    }
}
