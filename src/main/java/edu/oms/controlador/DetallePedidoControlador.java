package edu.oms.controlador;

import edu.oms.modelo.DetallePedido;
import edu.oms.servicio.DetallePedidoServicio;
import java.sql.SQLException;
import java.util.List;

public class DetallePedidoControlador {

    private final DetallePedidoServicio servicio = new DetallePedidoServicio();

    public void agregarDetalle(int idPedido, int idProducto, double cantidad, double precioUnitario) throws SQLException {
        DetallePedido d = new DetallePedido(idPedido, idProducto, cantidad, precioUnitario);
        servicio.agregarDetalle(d);
    }

    public List<DetallePedido> listarDetalles(int idPedido) throws SQLException {
        return servicio.obtenerPorPedido(idPedido);
    }

    public void eliminarDetalle(int idDetalle) throws SQLException {
        servicio.eliminarDetalle(idDetalle);
    }
}
