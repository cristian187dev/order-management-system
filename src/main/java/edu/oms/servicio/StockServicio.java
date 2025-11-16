package edu.oms.servicio;

import edu.oms.dao.StockDAO;
import edu.oms.modelo.StockProductoDia;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class StockServicio {

    private final StockDAO stockDAO = new StockDAO();

    public List<StockProductoDia> obtenerDemandaPorFecha(LocalDate fecha) throws SQLException {
        return stockDAO.obtenerDemandaPorFecha(fecha);
    }

    public void agregarStock(int idProducto, double cantidad) throws SQLException {
        stockDAO.agregarStock(idProducto, cantidad);
    }
}