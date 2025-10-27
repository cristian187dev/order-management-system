package edu.oms.servicio;

import edu.oms.dao.PedidoDAO;
import edu.oms.modelo.Pedido;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PedidoServicio {

    private final PedidoDAO dao = new PedidoDAO();

    public void guardarPedido(Pedido p) throws SQLException {
        if (p.getIdCliente() <= 0)
            throw new IllegalArgumentException("Debe seleccionar un cliente.");
        if (p.getEstadoPago() == null || p.getEstadoPago().isBlank())
            throw new IllegalArgumentException("Debe indicar el estado de pago.");
        if (p.getFechaPedido() == null)
            p.setFechaPedido(LocalDate.now());
        if (p.getHoraPedido() == null)
            p.setHoraPedido(LocalTime.now());

        dao.insertar(p);
    }

    public List<Pedido> obtenerPedidos() throws SQLException {
        return dao.obtenerTodos();
    }

    public void eliminarPedido(int idPedido) throws SQLException {
        dao.eliminar(idPedido);
    }

    public void actualizarPedido(Pedido p) throws SQLException {
        dao.actualizar(p);
    }
}
