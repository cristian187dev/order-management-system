package edu.oms.modelo;

public class Producto {

    private int idProducto;
    private String nombreProducto;
    private String unidadMedida;
    private double stockActual;

    public Producto() {}

    public Producto(String nombreProducto, String unidadMedida, double stockActual) {
        this.nombreProducto = nombreProducto;
        this.unidadMedida = unidadMedida;
        this.stockActual = stockActual;
    }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public double getStockActual() { return stockActual; }
    public void setStockActual(double stockActual) { this.stockActual = stockActual; }
}
