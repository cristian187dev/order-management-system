package edu.oms.modelo;

import java.time.LocalDate;

public class TotalPorDia {

    private LocalDate fecha;
    private double totalDia;

    public TotalPorDia(LocalDate fecha, double totalDia) {
        this.fecha = fecha;
        this.totalDia = totalDia;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getTotalDia() {
        return totalDia;
    }

    public void setTotalDia(double totalDia) {
        this.totalDia = totalDia;
    }
}