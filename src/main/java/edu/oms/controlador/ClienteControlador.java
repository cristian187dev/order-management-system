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

        Cliente c = new Cliente();
        c.setNombre(nombre);
        c.setApellido(apellido);
        c.setTelefono(telefono);
        c.setDireccion(direccion);
        c.setCuil(cuil);
        c.setEstadoUsuario("ACTIVO");  // Por defecto activo al crearlo
        c.setFechaInicio(LocalDate.now());
        c.setFechaFin(null);

        servicio.guardarCliente(c);
    }


    public List<Cliente> listarClientesActivos() throws SQLException {
        return servicio.obtenerClientesActivos();
    }


    public List<Cliente> listarClientesInactivos() throws SQLException {
        return servicio.obtenerClientesInactivos();
    }


    public void desactivarCliente(int idCliente) throws SQLException {
        servicio.desactivarCliente(idCliente);
    }


    public void activarCliente(int idCliente) throws SQLException {
        servicio.activarCliente(idCliente);
    }
}
