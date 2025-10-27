package edu.oms.modelo;

import java.time.LocalDate;

public class PrecioProductoBase {

    private int idPrecioBase;
    private int idProducto;
    private String nombreProducto;
    private double precioBase;
    private LocalDate fechaInicioPrecio;
    private LocalDate fechaFinPrecio;

    public PrecioProductoBase() {}

    public PrecioProductoBase(int idPrecioBase, int idProducto, String nombreProducto,
                              double precioBase, LocalDate fechaInicioPrecio, LocalDate fechaFinPrecio) {
        this.idPrecioBase = idPrecioBase;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precioBase = precioBase;
        this.fechaInicioPrecio = fechaInicioPrecio;
        this.fechaFinPrecio = fechaFinPrecio;
    }

    public PrecioProductoBase(int idProducto, double precioBase, LocalDate fechaInicioPrecio, LocalDate fechaFinPrecio) {
        this.idProducto = idProducto;
        this.precioBase = precioBase;
        this.fechaInicioPrecio = fechaInicioPrecio;
        this.fechaFinPrecio = fechaFinPrecio;
    }

    public int getIdPrecioBase() { return idPrecioBase; }
    public void setIdPrecioBase(int idPrecioBase) { this.idPrecioBase = idPrecioBase; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public double getPrecioBase() { return precioBase; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }

    public LocalDate getFechaInicioPrecio() { return fechaInicioPrecio; }
    public void setFechaInicioPrecio(LocalDate fechaInicioPrecio) { this.fechaInicioPrecio = fechaInicioPrecio; }

    public LocalDate getFechaFinPrecio() { return fechaFinPrecio; }
    public void setFechaFinPrecio(LocalDate fechaFinPrecio) { this.fechaFinPrecio = fechaFinPrecio; }
}
