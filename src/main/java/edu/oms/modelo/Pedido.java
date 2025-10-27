package edu.oms.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Pedido {
    private int idPedido;
    private int idCliente;
    private String nombreCliente;
    private String estadoPago;
    private LocalDate fechaPedido;
    private LocalTime horaPedido;
    private String observaciones;

    public Pedido() {}

    public Pedido(int idCliente, String estadoPago, LocalDate fechaPedido, LocalTime horaPedido, String observaciones) {
        this.idCliente = idCliente;
        this.estadoPago = estadoPago;
        this.fechaPedido = fechaPedido;
        this.horaPedido = horaPedido;
        this.observaciones = observaciones;
    }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }

    public LocalDate getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDate fechaPedido) { this.fechaPedido = fechaPedido; }

    public LocalTime getHoraPedido() { return horaPedido; }
    public void setHoraPedido(LocalTime horaPedido) { this.horaPedido = horaPedido; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
