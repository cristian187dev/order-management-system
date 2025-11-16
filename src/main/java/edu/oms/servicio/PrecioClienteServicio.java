package edu.oms.servicio;

import edu.oms.dao.PrecioClienteDAO;
import edu.oms.modelo.PrecioCliente;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PrecioClienteServicio {

    private final PrecioClienteDAO dao = new PrecioClienteDAO();

    public void guardarPrecioCliente(PrecioCliente pc) throws SQLException {
        if (pc.getIdCliente() <= 0)
            throw new IllegalArgumentException("Debe seleccionar un cliente válido.");
        if (pc.getIdProducto() <= 0)
            throw new IllegalArgumentException("Debe seleccionar un producto válido.");
        if (pc.getPrecioUnitario() <= 0)
            throw new IllegalArgumentException("El precio debe ser mayor a 0.");
        if (pc.getFechaInicioPrecio() == null)
            pc.setFechaInicioPrecio(LocalDate.now());
        dao.insertar(pc);
    }

    public List<PrecioCliente> obtenerPreciosClientes() throws SQLException {
        return dao.obtenerTodos();
    }

    public void eliminarPrecioCliente(int idPrecioCliente) throws SQLException {
        dao.eliminar(idPrecioCliente);
    }

    public Double obtenerPrecioVigente(int idCliente, int idProducto, LocalDate fecha) throws SQLException {
        return dao.obtenerPrecioVigente(idCliente, idProducto, fecha);
    }
}
