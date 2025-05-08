package com.example.customer_service.infra.adapter;

import com.example.customer_service.domain.model.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerRepositoryPort {
    Customer save(Customer customer);
    List<Customer> findAll();
    Optional<Customer> findById(Long id);
}
