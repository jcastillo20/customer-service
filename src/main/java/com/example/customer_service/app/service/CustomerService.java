package com.example.customer_service.app.service;

import com.example.customer_service.domain.model.CustomerRequestDto;
import com.example.customer_service.domain.model.CustomerResponseDto;
import com.example.customer_service.domain.model.MetricsDto;

import java.util.List;

public interface CustomerService {
    CustomerResponseDto registerCustomer(CustomerRequestDto customerRequestDto);
    List<CustomerResponseDto> listCustomers();
    MetricsDto getMetrics();
    CustomerResponseDto getCustomerById(Long id);
}
