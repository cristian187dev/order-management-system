package edu.oms.servicio;

import edu.oms.dao.PedidoDAO;
import edu.oms.modelo.Pedido;
import edu.oms.modelo.PedidoResumen;
import edu.oms.modelo.TotalPorDia;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PedidoServicio {

    private final PedidoDAO dao = new PedidoDAO();

    public int guardarPedido(Pedido pedido) throws SQLException {
        return dao.guardarPedido(pedido);
    }

    public void modificarPedido(Pedido pedido) throws SQLException {
        dao.modificarPedido(pedido);
    }

    public void eliminarPedido(int idPedido) throws SQLException {
        dao.eliminarPedido(idPedido);
    }

    public List<Pedido> listarPedidos() throws SQLException {
        return dao.listarPedidos();
    }

    public List<Pedido> listarPedidosPorFecha(LocalDate fecha) throws SQLException {
        return dao.listarPedidosPorFecha(fecha);
    }

    public List<TotalPorDia> obtenerTotalesPorDia(int idCliente) throws SQLException {
        return dao.obtenerTotalesPorDia(idCliente);
    }

    public double obtenerTotalDeuda(int idCliente) throws SQLException {
        return dao.obtenerTotalDeuda(idCliente);
    }

    public List<PedidoResumen> obtenerPedidosPorFecha(int idCliente, LocalDate fecha) throws SQLException {
        return dao.obtenerPedidosPorFecha(idCliente, fecha);
    }
}