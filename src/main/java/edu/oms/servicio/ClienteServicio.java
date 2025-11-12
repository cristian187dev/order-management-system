package edu.oms.servicio;

import edu.oms.dao.ClienteDAO;
import edu.oms.modelo.Cliente;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ClienteServicio {

    private final ClienteDAO dao = new ClienteDAO();

    public void guardarCliente(Cliente cliente) throws SQLException {
        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (cliente.getApellido() == null || cliente.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío.");
        }
        if (cliente.getTelefono() == null || cliente.getTelefono().isBlank()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío.");
        }

        if (cliente.getFechaInicio() == null) {
            cliente.setFechaInicio(LocalDate.now());
        }

        dao.insertar(cliente);
    }

    public List<Cliente> obtenerClientesActivos() throws SQLException {
        return dao.obtenerActivos();
    }

    public List<Cliente> obtenerClientesInactivos() throws SQLException {
        return dao.obtenerInactivos();
    }

    public void desactivarCliente(int idCliente) throws SQLException {
        dao.desactivarCliente(idCliente);
    }

    public void activarCliente(int idCliente) throws SQLException {
        dao.activarCliente(idCliente);
    }
}
