package com.example.customer_service.domain.model;

public class PercentilesDto {
    private double p25;
    private double p50;
    private double p75;

    public PercentilesDto() {
    }

    public PercentilesDto(double p25, double p50, double p75) {
        this.p25 = p25;
        this.p50 = p50;
        this.p75 = p75;
    }

    public double getP25() {
        return p25;
    }

    public void setP25(double p25) {
        this.p25 = p25;
    }

    public double getP50() {
        return p50;
    }

    public void setP50(double p50) {
        this.p50 = p50;
    }

    public double getP75() {
        return p75;
    }

    public void setP75(double p75) {
        this.p75 = p75;
    }
}