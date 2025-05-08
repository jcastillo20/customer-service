package com.example.customer_service.domain.model;

import java.time.LocalDate;
import lombok.Data;

public class CustomerResponseDto {
    private Long id;
    private String nombre;
    private String apellido;
    private Integer edad;
    private LocalDate fechaNacimiento;
    private LocalDate fechaFallecimientoEstimado;

    // Constructores
    public CustomerResponseDto() {
    }

    public CustomerResponseDto(Long id, String nombre, String apellido, Integer edad, LocalDate fechaNacimiento, LocalDate fechaEstimadoEvento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaFallecimientoEstimado = fechaEstimadoEvento;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public Integer getEdad() {
        return edad;
    }
    public void setEdad(Integer edad) {
        this.edad = edad;
    }
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public LocalDate getFechaEstimadoEvento() {
        return fechaFallecimientoEstimado;
    }
    public void setFechaEstimadoEvento(LocalDate fechaEstimadoEvento) {
        this.fechaFallecimientoEstimado = fechaEstimadoEvento;
    }
}
