package com.example.customer_service.app.mapper;

import com.example.customer_service.domain.model.Customer;
import com.example.customer_service.domain.model.CustomerRequestDto;
import com.example.customer_service.domain.model.CustomerResponseDto;
import com.example.customer_service.infra.adapter.CustomerEntity;

public class CustomerMapper {

    // De DTO de entrada a dominio
    public static Customer toDomain(CustomerRequestDto dto) {
        if (dto == null) return null;
        Customer customer = new Customer();
        customer.setNombre(dto.getNombre());
        customer.setApellido(dto.getApellido());
        customer.setEdad(dto.getEdad() != null ? dto.getEdad() : 0);
        customer.setFechaNacimiento(dto.getFechaNacimiento());
        return customer;
    }

    // De dominio a DTO de salida
    public static CustomerResponseDto toResponseDto(Customer customer) {
        if (customer == null) return null;
        CustomerResponseDto dto = new CustomerResponseDto();
        dto.setId(customer.getId());
        dto.setNombre(customer.getNombre());
        dto.setApellido(customer.getApellido());
        dto.setEdad(customer.getEdad());
        dto.setFechaNacimiento(customer.getFechaNacimiento());
        dto.setFechaEstimadoEvento(customer.getFechaEstimadoEvento());
        return dto;
    }

    // De entidad JPA a dominio
    public static Customer toDomainFromEntity(CustomerEntity entity) {
        if (entity == null) return null;
        Customer customer = new Customer();
        customer.setId(entity.getId());
        customer.setNombre(entity.getNombre());
        customer.setApellido(entity.getApellido());
        customer.setEdad(entity.getEdad());
        customer.setFechaNacimiento(entity.getFechaNacimiento());
        customer.setFechaEstimadoEvento(entity.getFechaEstimadoEvento());
        return customer;
    }

    // De dominio a entidad JPA
    public static CustomerEntity toEntityFromDomain(Customer customer) {
        if (customer == null) return null;
        CustomerEntity entity = new CustomerEntity();
        entity.setId(customer.getId());
        entity.setNombre(customer.getNombre());
        entity.setApellido(customer.getApellido());
        entity.setEdad(customer.getEdad());
        entity.setFechaNacimiento(customer.getFechaNacimiento());
        entity.setFechaEstimadoEvento(customer.getFechaEstimadoEvento());
        return entity;
    }
}
