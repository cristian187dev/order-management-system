package edu.oms.controlador;

import edu.oms.modelo.Cliente;
import edu.oms.servicio.ClienteServicio;
import java.sql.SQLException;
import java.util.List;

public class ClienteControlador {

    private final ClienteServicio servicio = new ClienteServicio();

    public void agregarCliente(Cliente cliente) throws SQLException {
        servicio.insertar(cliente);
    }


    public List<Cliente> listarActivos() throws SQLException {
        return servicio.obtenerActivos();
    }

    public List<Cliente> listarInactivos() throws SQLException {
        return servicio.obtenerInactivos();
    }

    public void desactivarCliente(int idCliente) throws SQLException {
        servicio.desactivarCliente(idCliente);
    }

    public void activarCliente(int idCliente) throws SQLException {
        servicio.activarCliente(idCliente);
    }

    public List<Cliente> listarClientesActivos() throws SQLException {
        return listarActivos();
    }

    public List<Cliente> listarClientesInactivos() throws SQLException {
        return listarInactivos();
    }
}