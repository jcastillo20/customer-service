package com.example.customer_service.app.resources;

import com.example.customer_service.app.service.CustomerService;
import com.example.customer_service.domain.model.CustomerRequestDto;
import com.example.customer_service.domain.model.CustomerResponseDto;
import com.example.customer_service.domain.model.MetricsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/customers")
public class CustomerController {


    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(
            summary = "Registrar cliente",
            description = "Se requiere enviar un header 'Authorization' con un token Bearer válido. El usuario debe tener alguno de los roles: USER o ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    public ResponseEntity<CustomerResponseDto> registerCustomer(@RequestBody @Valid CustomerRequestDto customerRequestDto) {
        CustomerResponseDto response = customerService.registerCustomer(customerRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar clientes",
            description = "Se requiere enviar un header 'Authorization' con un token Bearer válido. El usuario debe tener alguno de los roles: USER o ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> listCustomers() {
        List<CustomerResponseDto> customers = customerService.listCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
    @Operation(
            summary = "Obtener métricas",
            description = "Se requiere enviar un header 'Authorization' con un token Bearer válido. El usuario debe tener alguno de los roles: USER o ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/metrics")
    public ResponseEntity<MetricsDto> getMetrics() {
        MetricsDto metrics = customerService.getMetrics();
        return new ResponseEntity<>(metrics, HttpStatus.OK);
    }
    @Operation(
            summary = "Obtener cliente por ID",
            description = "Se requiere enviar un header 'Authorization' con un token Bearer válido. El usuario debe tener alguno de los roles: USER o ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable("id") Long id) {
        CustomerResponseDto response = customerService.getCustomerById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
