package edu.oms.controlador;

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
}