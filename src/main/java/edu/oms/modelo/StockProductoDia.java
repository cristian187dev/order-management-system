package edu.oms.modelo;

public class StockProductoDia {

    private int idProducto;
    private String nombreProducto;
    private double stockRemanente;
    private double stockProducidoHoy;
    private double stockQuitadoHoy;
    private double stockTotal;
    private double cantidadPedida;
    private double diferencia;

    public StockProductoDia(int idProducto,
                            String nombreProducto,
                            double stockRemanente,
                            double stockProducidoHoy,
                            double stockQuitadoHoy,
                            double cantidadPedida) {

        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.stockRemanente = stockRemanente;
        this.stockProducidoHoy = stockProducidoHoy;
        this.stockQuitadoHoy = stockQuitadoHoy;
        this.cantidadPedida = cantidadPedida;

        this.stockTotal = stockRemanente + stockProducidoHoy - stockQuitadoHoy;
        this.diferencia = this.stockTotal - this.cantidadPedida;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public double getStockRemanente() {
        return stockRemanente;
    }

    public double getStockProducidoHoy() {
        return stockProducidoHoy;
    }

    public double getStockQuitadoHoy() {
        return stockQuitadoHoy;
    }

    public double getStockTotal() {
        return stockTotal;
    }

    public double getCantidadPedida() {
        return cantidadPedida;
    }

    public double getDiferencia() {
        return diferencia;
    }

    public void setStockRemanente(double stockRemanente) {
        this.stockRemanente = stockRemanente;
        recalcularTotales();
    }

    public void setStockProducidoHoy(double stockProducidoHoy) {
        this.stockProducidoHoy = stockProducidoHoy;
        recalcularTotales();
    }

    public void setStockQuitadoHoy(double stockQuitadoHoy) {
        this.stockQuitadoHoy = stockQuitadoHoy;
        recalcularTotales();
    }

    public void setCantidadPedida(double cantidadPedida) {
        this.cantidadPedida = cantidadPedida;
        recalcularTotales();
    }

    private void recalcularTotales() {
        this.stockTotal = stockRemanente + stockProducidoHoy - stockQuitadoHoy;
        this.diferencia = this.stockTotal - this.cantidadPedida;
    }
}