package com.example.customer_service.app.service.impl;

import com.example.customer_service.app.mapper.CustomerMapper;
import com.example.customer_service.domain.model.Customer;
import com.example.customer_service.domain.model.CustomerRequestDto;
import com.example.customer_service.domain.model.CustomerResponseDto;
import com.example.customer_service.domain.model.MetricsDto;
import com.example.customer_service.infra.adapter.CustomerRepositoryAdapter;
import com.example.customer_service.infra.handle.exception.CustomerServiceException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepositoryAdapter repository;

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private Counter successCounter;

    @Mock
    private Counter errorCounter;

    @Mock
    private Timer timer;

    @Mock
    private Timer.Sample sample;


    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerRequestDto customerRequestDto;
    private Customer customer;

    // Guardaremos el mock estático para Timer
    private MockedStatic<Timer> mockedTimer;

    @BeforeEach
    public void setUp() {
        customerRequestDto = new CustomerRequestDto();
        customerRequestDto.setNombre("Juan");
        customerRequestDto.setApellido("Perez");
        customerRequestDto.setFechaNacimiento(LocalDate.of(1990, 1, 1));

        customer = CustomerMapper.toDomain(customerRequestDto);

        // Configuración de mock estático para Timer.start(...)
        mockedTimer = mockStatic(Timer.class);
        mockedTimer.when(() -> Timer.start(meterRegistry)).thenReturn(sample);
    }

    @AfterEach
    public void tearDown() {
        // Cerrar el mock estático
        if (mockedTimer != null) {
            mockedTimer.close();
        }
    }

    @Test
    public void testRegisterCustomerSuccess() {
        when(meterRegistry.counter("customer.registration.success")).thenReturn(successCounter);
        when(meterRegistry.timer("customer.registration.timer")).thenReturn(timer);
        when(repository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponseDto response = customerService.registerCustomer(customerRequestDto);

        assertNotNull(response);
        assertEquals("Juan", response.getNombre());
        verify(repository, times(1)).save(any(Customer.class));
        verify(successCounter, times(1)).increment();
        verify(sample, times(1)).stop(timer);
    }
    @Test
    public void testRegisterCustomerFailure() {
        when(meterRegistry.counter("customer.registration.error")).thenReturn(successCounter);
        when(meterRegistry.timer("customer.registration.timer")).thenReturn(timer);
        when(repository.save(any(Customer.class))).thenThrow(new RuntimeException("Database error"));

        CustomerServiceException exception = assertThrows(CustomerServiceException.class, () -> {
            customerService.registerCustomer(customerRequestDto);
        });

        assertEquals("Error registrando el cliente: Database error", exception.getMessage());
        verify(repository, times(1)).save(any(Customer.class));
        //verify(errorCounter, times(1)).increment();
    }

    @Test
    public void testListCustomers() {
        when(meterRegistry.counter("customer.list.success")).thenReturn(successCounter);
        when(repository.findAll()).thenReturn(Collections.singletonList(customer));

        List<CustomerResponseDto> response = customerService.listCustomers();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Juan", response.get(0).getNombre());
        verify(repository, times(1)).findAll();
        verify(successCounter, times(1)).increment();
    }

    @Test
    public void testGetCustomerByIdSuccess() {
        when(meterRegistry.counter(eq("customer.get.success"), any(String[].class))).thenReturn(successCounter);

        when(repository.findById(anyLong())).thenReturn(Optional.of(customer));

        CustomerResponseDto response = customerService.getCustomerById(1L);

        assertNotNull(response);
        assertEquals("Juan", response.getNombre());
        verify(repository, times(1)).findById(anyLong());
        verify(successCounter, times(1)).increment();
        //verify(timer, times(1)).stop();
    }

    @Test
    public void testGetCustomerByIdFailure() {
        when(meterRegistry.counter(eq("customer.get.error"), any(String[].class))).thenReturn(errorCounter);
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerServiceException.class, () -> {
            customerService.getCustomerById(1L);
        });

        verify(repository, times(1)).findById(1L);
        verify(errorCounter, times(1)).increment();
    }

    @Test
    public void testCalculateMetricsEmptyList() {
        when(meterRegistry.counter("customer.metrics.success")).thenReturn(successCounter);

        when(repository.findAll()).thenReturn(Collections.emptyList());

        MetricsDto metrics = customerService.getMetrics();

        assertNotNull(metrics);
        assertEquals(0, metrics.getTotalClientes());
        verify(repository, times(1)).findAll();
        verify(successCounter, times(1)).increment();
        //verify(timer, times(1)).stop();
    }
}
