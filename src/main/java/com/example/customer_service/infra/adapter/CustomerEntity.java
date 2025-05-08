package com.example.customer_service.infra.adapter;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "customers")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private int edad;
    private LocalDate fechaNacimiento;
    private LocalDate fechaEstimadoEvento;

    // Constructores
    public CustomerEntity() {
    }

    public CustomerEntity(Long id, String nombre, String apellido, int edad, LocalDate fechaNacimiento, LocalDate fechaEstimadoEvento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaEstimadoEvento = fechaEstimadoEvento;
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
    public int getEdad() {
        return edad;
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public LocalDate getFechaEstimadoEvento() {
        return fechaEstimadoEvento;
    }
    public void setFechaEstimadoEvento(LocalDate fechaEstimadoEvento) {
        this.fechaEstimadoEvento = fechaEstimadoEvento;
    }
}
