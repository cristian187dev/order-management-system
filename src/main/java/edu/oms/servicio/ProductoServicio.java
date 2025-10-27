package edu.oms.servicio;

import edu.oms.dao.ProductoDAO;
import edu.oms.modelo.Producto;
import java.sql.SQLException;
import java.util.List;

public class ProductoServicio {

    private final ProductoDAO dao = new ProductoDAO();

    public void guardarProducto(Producto p) throws SQLException {
        if (p.getNombreProducto() == null || p.getNombreProducto().isBlank())
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        dao.insertar(p);
    }

    public List<Producto> obtenerProductos() throws SQLException {
        return dao.obtenerTodos();
    }

    public void eliminarProducto(int idProducto) throws SQLException {
        dao.eliminar(idProducto);
    }
}
