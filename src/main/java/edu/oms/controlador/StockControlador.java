package edu.oms.controlador;

import edu.oms.modelo.DetallePedido;
import edu.oms.modelo.StockProductoDia;
import edu.oms.servicio.StockServicio;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class StockControlador {

    private final StockServicio servicio = new StockServicio();

    public List<StockProductoDia> obtenerDemandaPorFecha(LocalDate fecha) throws SQLException {
        return servicio.obtenerDemandaPorFecha(fecha);
    }

    public void agregarStock(int idProducto, double cantidad) throws SQLException {
        servicio.agregarStock(idProducto, cantidad);
    }

    public void quitarStock(int idProducto, double cantidad) throws SQLException {
        servicio.quitarStock(idProducto, cantidad);
    }

    public void registrarSalidaPorPedido(LocalDate fechaPedido,
                                         int idPedido,
                                         List<DetallePedido> detalles) throws SQLException {
        servicio.registrarSalidaPorPedido(fechaPedido, idPedido, detalles);
    }
}