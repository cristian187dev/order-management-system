package edu.oms.modelo;

import java.time.LocalDate;

public class PrecioCliente {

    private int idPrecioCliente;
    private int idCliente;
    private int idProducto;
    private String nombreCliente;
    private String nombreProducto;
    private double precioUnitario;
    private LocalDate fechaInicioPrecio;
    private LocalDate fechaFinPrecio;

    public PrecioCliente() {}

    public PrecioCliente(int idCliente, int idProducto, double precioUnitario,
                         LocalDate fechaInicioPrecio, LocalDate fechaFinPrecio) {
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        this.precioUnitario = precioUnitario;
        this.fechaInicioPrecio = fechaInicioPrecio;
        this.fechaFinPrecio = fechaFinPrecio;
    }

    public int getIdPrecioCliente() { return idPrecioCliente; }
    public void setIdPrecioCliente(int idPrecioCliente) { this.idPrecioCliente = idPrecioCliente; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public LocalDate getFechaInicioPrecio() { return fechaInicioPrecio; }
    public void setFechaInicioPrecio(LocalDate fechaInicioPrecio) { this.fechaInicioPrecio = fechaInicioPrecio; }

    public LocalDate getFechaFinPrecio() { return fechaFinPrecio; }
    public void setFechaFinPrecio(LocalDate fechaFinPrecio) { this.fechaFinPrecio = fechaFinPrecio; }
}
