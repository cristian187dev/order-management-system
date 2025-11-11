package edu.oms.modelo;

public class Producto {
    private int idProducto;
    private String nombreProducto;
    private int idUnidad;
    private String nombreUnidad;
    private String abreviaturaUnidad;
    private double stockActual;

    public Producto() {}

    public Producto(String nombreProducto, int idUnidad, double stockActual) {
        this.nombreProducto = nombreProducto;
        this.idUnidad = idUnidad;
        this.stockActual = stockActual;
    }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public int getIdUnidad() { return idUnidad; }
    public void setIdUnidad(int idUnidad) { this.idUnidad = idUnidad; }

    public String getNombreUnidad() { return nombreUnidad; }
    public void setNombreUnidad(String nombreUnidad) { this.nombreUnidad = nombreUnidad; }

    public String getAbreviaturaUnidad() { return abreviaturaUnidad; }
    public void setAbreviaturaUnidad(String abreviaturaUnidad) { this.abreviaturaUnidad = abreviaturaUnidad; }

    public double getStockActual() { return stockActual; }
    public void setStockActual(double stockActual) { this.stockActual = stockActual; }
}
