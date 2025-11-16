package edu.oms.controlador;

import edu.oms.modelo.Pedido;
import edu.oms.modelo.PedidoResumen;
import edu.oms.modelo.TotalPorDia;
import edu.oms.servicio.PedidoServicio;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PedidoControlador {

    private final PedidoServicio servicio = new PedidoServicio();

    public int guardarPedido(Pedido pedido) throws SQLException {
        return servicio.guardarPedido(pedido);
    }

    public void modificarPedido(Pedido pedido) throws SQLException {
        servicio.modificarPedido(pedido);
    }

    public void eliminarPedido(int idPedido) throws SQLException {
        servicio.eliminarPedido(idPedido);
    }

    public List<Pedido> listarPedidos() throws SQLException {
        return servicio.listarPedidos();
    }

    public List<Pedido> listarPedidosPorFecha(LocalDate fecha) throws SQLException {
        return servicio.listarPedidosPorFecha(fecha);
    }

    public List<TotalPorDia> obtenerTotalesPorDia(int idCliente) throws SQLException {
        return servicio.obtenerTotalesPorDia(idCliente);
    }

    public double obtenerTotalDeuda(int idCliente) throws SQLException {
        return servicio.obtenerTotalDeuda(idCliente);
    }

    public List<PedidoResumen> obtenerPedidosPorFecha(int idCliente, LocalDate fecha) throws SQLException {
        return servicio.obtenerPedidosPorFecha(idCliente, fecha);
    }
}