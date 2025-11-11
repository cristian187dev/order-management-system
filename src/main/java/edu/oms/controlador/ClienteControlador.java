package edu.oms.controlador;

import edu.oms.modelo.Cliente;
import edu.oms.servicio.ClienteServicio;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ClienteControlador {

    private final ClienteServicio servicio = new ClienteServicio();

    public void agregarCliente(String nombre, String apellido, String telefono,
                               String direccion, String cuil) throws SQLException {

        Cliente c = new Cliente(
                nombre,
                apellido,
                telefono,
                direccion,
                cuil,
                "ACTIVO",
                LocalDate.now(),
                null
        );

        servicio.guardarCliente(c);
    }

    public List<Cliente> listarClientes() throws SQLException {
        return servicio.obtenerClientes();
    }

    public List<Cliente> listarClientesActivos() throws SQLException {
        return servicio.obtenerClientesPorEstado("ACTIVO");
    }

    public List<Cliente> listarClientesInactivos() throws SQLException {
        return servicio.obtenerClientesPorEstado("INACTIVO");
    }

    public void desactivarCliente(int idCliente) throws SQLException {
        servicio.actualizarEstadoCliente(idCliente, "INACTIVO", LocalDate.now());
    }

    public void activarCliente(int idCliente) throws SQLException {
        servicio.actualizarEstadoCliente(idCliente, "ACTIVO", null);
    }
}
