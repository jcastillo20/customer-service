package com.example.customer_service.infra.adapter;

import com.example.customer_service.domain.model.Customer;
import com.example.customer_service.infra.adapter.CustomerRepositoryPort;
import com.example.customer_service.app.mapper.CustomerMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
@Slf4j
@Repository
public class CustomerRepositoryAdapter implements CustomerRepositoryPort {

    private final CustomerJpaRepository jpaRepository;
    private final MeterRegistry meterRegistry;

    public CustomerRepositoryAdapter(CustomerJpaRepository jpaRepository, MeterRegistry meterRegistry) {
        this.jpaRepository = jpaRepository;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public Customer save(Customer customer) {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            CustomerEntity entity = CustomerMapper.toEntityFromDomain(customer);
            CustomerEntity savedEntity = jpaRepository.save(entity);
            meterRegistry.counter("customer.repository.save.success").increment();
            log.info("Cliente guardado exitosamente en la BD.");
            return CustomerMapper.toDomainFromEntity(savedEntity);
        } catch (Exception e) {
            meterRegistry.counter("customer.repository.save.error").increment();
            log.error("Error al guardar el cliente en la BD: {}", e.getMessage(), e);
            throw e;
        } finally {
            sample.stop(meterRegistry.timer("customer.repository.save.timer"));
        }
    }

    @Override
    public List<Customer> findAll() {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            List<Customer> customers = jpaRepository.findAll().stream()
                    .map(CustomerMapper::toDomainFromEntity)
                    .collect(Collectors.toList());
            meterRegistry.counter("customer.repository.findAll.success").increment();
            log.info("Se consultaron {} clientes de la BD.", customers.size());
            return customers;
        } catch (Exception e) {
            meterRegistry.counter("customer.repository.findAll.error").increment();
            log.error("Error consultando clientes de la BD: {}", e.getMessage(), e);
            throw e;
        } finally {
            sample.stop(meterRegistry.timer("customer.repository.findAll.timer"));
        }
    }

    @Override
    public Optional<Customer> findById(Long id) {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            Optional<Customer> customer = jpaRepository.findById(id)
                    .map(CustomerMapper::toDomainFromEntity);
            meterRegistry.counter("customer.repository.findById.success").increment();
            log.info("Cliente con id {} consultado correctamente.", id);
            return customer;
        } catch (Exception e) {
            meterRegistry.counter("customer.repository.findById.error").increment();
            log.error("Error consultando cliente con id {}: {}", id, e.getMessage(), e);
            throw e;
        } finally {
            sample.stop(meterRegistry.timer("customer.repository.findById.timer"));
        }
    }
}
