package edu.oms.modelo;

public class StockProductoDia {

    private int idProducto;
    private String nombreProducto;
    private double stockActual;
    private double cantidadPedida;
    private double faltante; // > 0 si va a faltar

    public StockProductoDia(int idProducto, String nombreProducto,
                            double stockActual, double cantidadPedida) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.stockActual = stockActual;
        this.cantidadPedida = cantidadPedida;
        double diff = stockActual - cantidadPedida;
        this.faltante = diff < 0 ? -diff : 0;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public double getStockActual() {
        return stockActual;
    }

    public double getCantidadPedida() {
        return cantidadPedida;
    }

    public double getFaltante() {
        return faltante;
    }

    public void setStockActual(double stockActual) {
        this.stockActual = stockActual;
    }

    public void setCantidadPedida(double cantidadPedida) {
        this.cantidadPedida = cantidadPedida;
    }

    public void setFaltante(double faltante) {
        this.faltante = faltante;
    }
}