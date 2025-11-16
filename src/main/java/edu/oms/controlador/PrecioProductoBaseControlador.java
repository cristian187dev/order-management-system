package edu.oms.controlador;

import edu.oms.modelo.PrecioProductoBase;
import edu.oms.servicio.PrecioProductoBaseServicio;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PrecioProductoBaseControlador {

    private final PrecioProductoBaseServicio servicio = new PrecioProductoBaseServicio();

    public void agregarPrecioBase(int idProducto, double precioBase, LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        PrecioProductoBase pb = new PrecioProductoBase(idProducto, precioBase, fechaInicio, fechaFin);
        servicio.guardarPrecioBase(pb);
    }

    public List<PrecioProductoBase> listarPreciosBase() throws SQLException {
        return servicio.obtenerPreciosBase();
    }

    public void eliminarPrecioBase(int idPrecioBase) throws SQLException {
        servicio.eliminarPrecioBase(idPrecioBase);
    }

    public Double obtenerPrecioBaseVigente(int idProducto, LocalDate fecha) throws SQLException {
        return servicio.obtenerPrecioBaseVigente(idProducto, fecha);
    }
}

