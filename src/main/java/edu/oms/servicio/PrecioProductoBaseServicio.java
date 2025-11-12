package edu.oms.servicio;

import edu.oms.dao.PrecioProductoBaseDAO;
import edu.oms.modelo.PrecioProductoBase;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PrecioProductoBaseServicio {

    private final PrecioProductoBaseDAO dao = new PrecioProductoBaseDAO();

    public void guardarPrecioBase(PrecioProductoBase pb) throws SQLException {
        if (pb.getIdProducto() <= 0)
            throw new IllegalArgumentException("Debe seleccionar un producto válido.");
        if (pb.getPrecioBase() <= 0)
            throw new IllegalArgumentException("El precio base debe ser mayor a 0.");
        if (pb.getFechaInicioPrecio() == null)
            pb.setFechaInicioPrecio(LocalDate.now());
        dao.insertar(pb);
    }

    public List<PrecioProductoBase> obtenerPreciosBase() throws SQLException {
        return dao.obtenerTodos();
    }

    public void eliminarPrecioBase(int idPrecioBase) throws SQLException {
        dao.eliminar(idPrecioBase);
    }

    public Double obtenerPrecioBaseVigente(int idProducto, LocalDate fecha) throws SQLException {
        return dao.obtenerPrecioBaseVigente(idProducto, fecha);
    }
}
