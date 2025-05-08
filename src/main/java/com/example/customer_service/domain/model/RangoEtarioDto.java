package com.example.customer_service.domain.model;

public class RangoEtarioDto {
    private String rango;
    private double porcentaje;

    public RangoEtarioDto() {
    }

    public RangoEtarioDto(String rango, double porcentaje) {
        this.rango = rango;
        this.porcentaje = porcentaje;
    }

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }
}
