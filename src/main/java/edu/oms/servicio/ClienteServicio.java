package edu.oms.servicio;

import edu.oms.dao.ClienteDAO;
import edu.oms.modelo.Cliente;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ClienteServicio {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    public void guardarCliente(Cliente c) throws SQLException {
        if (c.getNombre() == null || c.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (c.getApellido() == null || c.getApellido().isBlank())
            throw new IllegalArgumentException("El apellido es obligatorio.");
        if (c.getTelefono() == null || c.getTelefono().isBlank())
            throw new IllegalArgumentException("El teléfono es obligatorio.");

        if (c.getFechaInicio() == null)
            c.setFechaInicio(LocalDate.now());

        c.setFechaFin(null);
        clienteDAO.insertar(c);
    }

    public List<Cliente> obtenerClientes() throws SQLException {
        return clienteDAO.obtenerTodos();
    }

    public List<Cliente> obtenerClientesPorEstado(String estado) throws SQLException {
        return clienteDAO.obtenerPorEstado(estado);
    }

    public void actualizarEstadoCliente(int idCliente, String nuevoEstado, LocalDate fechaFin) throws SQLException {
        clienteDAO.actualizarEstado(idCliente, nuevoEstado, fechaFin);
    }
}
