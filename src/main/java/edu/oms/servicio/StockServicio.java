package edu.oms.servicio;

import edu.oms.dao.StockDAO;
import edu.oms.modelo.DetallePedido;
import edu.oms.modelo.StockProductoDia;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class StockServicio {

    private final StockDAO dao = new StockDAO();

    public List<StockProductoDia> obtenerDemandaPorFecha(LocalDate fecha) throws SQLException {
        return dao.obtenerDemandaPorFecha(fecha);
    }

    public void agregarStock(int idProducto, double cantidad) throws SQLException {
        dao.agregarStock(idProducto, cantidad);
    }

    public void quitarStock(int idProducto, double cantidad) throws SQLException {
        dao.quitarStock(idProducto, cantidad);
    }

    public void registrarSalidaPorPedido(LocalDate fechaPedido,
                                         int idPedido,
                                         List<DetallePedido> detalles) throws SQLException {

        for (DetallePedido d : detalles) {
            dao.registrarSalidaPedido(
                    d.getIdProducto(),
                    d.getCantidad(),
                    fechaPedido,
                    idPedido
            );
        }
    }
}