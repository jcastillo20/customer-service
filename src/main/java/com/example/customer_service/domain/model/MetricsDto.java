package com.example.customer_service.domain.model;

import java.util.List;

public class MetricsDto {
    private double promedioEdad;
    private double desviacionEstandar;
    private int totalClientes;
    private int edadMinima;
    private int edadMaxima;
    private double medianaEdad;
    private double modaEdad;
    private PercentilesDto percentilesEdad;
    private List<RangoEtarioDto> distribucionEtaria;

    public MetricsDto() {
    }

    public MetricsDto(double promedioEdad, double desviacionEstandar, int totalClientes, int edadMinima, int edadMaxima,
                      double medianaEdad, double modaEdad, PercentilesDto percentilesEdad, List<RangoEtarioDto> distribucionEtaria) {
        this.promedioEdad = promedioEdad;
        this.desviacionEstandar = desviacionEstandar;
        this.totalClientes = totalClientes;
        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;
        this.medianaEdad = medianaEdad;
        this.modaEdad = modaEdad;
        this.percentilesEdad = percentilesEdad;
        this.distribucionEtaria = distribucionEtaria;
    }

    public double getPromedioEdad() {
        return promedioEdad;
    }

    public void setPromedioEdad(double promedioEdad) {
        this.promedioEdad = promedioEdad;
    }

    public double getDesviacionEstandar() {
        return desviacionEstandar;
    }

    public void setDesviacionEstandar(double desviacionEstandar) {
        this.desviacionEstandar = desviacionEstandar;
    }

    public int getTotalClientes() {
        return totalClientes;
    }

    public void setTotalClientes(int totalClientes) {
        this.totalClientes = totalClientes;
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(int edadMinima) {
        this.edadMinima = edadMinima;
    }

    public int getEdadMaxima() {
        return edadMaxima;
    }

    public void setEdadMaxima(int edadMaxima) {
        this.edadMaxima = edadMaxima;
    }

    public double getMedianaEdad() {
        return medianaEdad;
    }

    public void setMedianaEdad(double medianaEdad) {
        this.medianaEdad = medianaEdad;
    }

    public double getModaEdad() {
        return modaEdad;
    }

    public void setModaEdad(double modaEdad) {
        this.modaEdad = modaEdad;
    }

    public PercentilesDto getPercentilesEdad() {
        return percentilesEdad;
    }

    public void setPercentilesEdad(PercentilesDto percentilesEdad) {
        this.percentilesEdad = percentilesEdad;
    }

    public List<RangoEtarioDto> getDistribucionEtaria() {
        return distribucionEtaria;
    }

    public void setDistribucionEtaria(List<RangoEtarioDto> distribucionEtaria) {
        this.distribucionEtaria = distribucionEtaria;
    }
}
