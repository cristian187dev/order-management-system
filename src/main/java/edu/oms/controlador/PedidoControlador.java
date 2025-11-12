package edu.oms.controlador;

import edu.oms.modelo.Pedido;
import edu.oms.servicio.PedidoServicio;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PedidoControlador {

    private final PedidoServicio servicio = new PedidoServicio();

    public int agregarPedido(int idCliente, String estadoPago, String observaciones) throws SQLException {
        Pedido p = new Pedido(idCliente, estadoPago, LocalDate.now(), LocalTime.now(), observaciones);
        return servicio.guardarPedido(p);
    }

    public List<Pedido> listarPedidos() throws SQLException {
        return servicio.obtenerPedidos();
    }

    public List<Pedido> listarPedidosPorFecha(LocalDate fecha) throws SQLException {
        return servicio.obtenerPedidosPorFecha(fecha);
    }

    public void eliminarPedido(int idPedido) throws SQLException {
        servicio.eliminarPedido(idPedido);
    }

    public void modificarPedido(Pedido pedido) throws SQLException {
        servicio.actualizarPedido(pedido);
    }
}

