package edu.oms.servicio;

import edu.oms.dao.ClienteDAO;
import edu.oms.modelo.Cliente;

import java.sql.SQLException;
import java.util.List;

public class ClienteServicio {

    private final ClienteDAO dao = new ClienteDAO();

    public void insertar(Cliente c) throws SQLException {
        dao.insertar(c);
    }

    public List<Cliente> obtenerActivos() throws SQLException {
        return dao.obtenerActivos();
    }

    public List<Cliente> obtenerInactivos() throws SQLException {
        return dao.obtenerInactivos();
    }

    public void desactivarCliente(int idCliente) throws SQLException {
        dao.desactivarCliente(idCliente);
    }

    public void activarCliente(int idCliente) throws SQLException {
        dao.activarCliente(idCliente);
    }
}