package edu.oms.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class PedidoResumen {

    private int idPedido;
    private LocalDate fecha;
    private LocalTime hora;
    private double totalPedido;

    public PedidoResumen(int idPedido, LocalDate fecha, LocalTime hora, double totalPedido) {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.hora = hora;
        this.totalPedido = totalPedido;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public double getTotalPedido() {
        return totalPedido;
    }

    public void setTotalPedido(double totalPedido) {
        this.totalPedido = totalPedido;
    }
}