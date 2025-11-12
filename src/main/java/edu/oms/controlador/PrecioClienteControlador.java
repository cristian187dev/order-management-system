package edu.oms.controlador;

import edu.oms.modelo.PrecioCliente;
import edu.oms.servicio.PrecioClienteServicio;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PrecioClienteControlador {

    private final PrecioClienteServicio servicio = new PrecioClienteServicio();

    public void agregarPrecioCliente(int idCliente, int idProducto, double precioUnitario,
                                     LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        PrecioCliente pc = new PrecioCliente(idCliente, idProducto, precioUnitario, fechaInicio, fechaFin);
        servicio.guardarPrecioCliente(pc);
    }

    public List<PrecioCliente> listarPreciosClientes() throws SQLException {
        return servicio.obtenerPreciosClientes();
    }

    public void eliminarPrecioCliente(int idPrecioCliente) throws SQLException {
        servicio.eliminarPrecioCliente(idPrecioCliente);
    }

    public Double obtenerPrecioVigente(int idCliente, int idProducto, LocalDate fecha) throws SQLException {
        return servicio.obtenerPrecioVigente(idCliente, idProducto, fecha);
    }
}
