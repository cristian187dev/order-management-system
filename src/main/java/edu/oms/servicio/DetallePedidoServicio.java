package edu.oms.servicio;

import edu.oms.dao.DetallePedidoDAO;
import edu.oms.modelo.DetallePedido;
import java.sql.SQLException;
import java.util.List;

public class DetallePedidoServicio {

    private final DetallePedidoDAO dao = new DetallePedidoDAO();

    public void agregarDetalle(DetallePedido d) throws SQLException {
        if (d.getCantidad() <= 0)
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        dao.insertar(d);
    }

    public List<DetallePedido> obtenerPorPedido(int idPedido) throws SQLException {
        return dao.obtenerPorPedido(idPedido);
    }

    public void eliminarDetalle(int idDetalle) throws SQLException {
        dao.eliminar(idDetalle);
    }
}
