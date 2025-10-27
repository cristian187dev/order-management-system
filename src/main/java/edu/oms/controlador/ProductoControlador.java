package edu.oms.controlador;

import edu.oms.modelo.Producto;
import edu.oms.servicio.ProductoServicio;
import java.sql.SQLException;
import java.util.List;

public class ProductoControlador {

    private final ProductoServicio servicio = new ProductoServicio();

    public void agregarProducto(String nombre, String unidad, double stock) throws SQLException {
        Producto p = new Producto(nombre, unidad, stock);
        servicio.guardarProducto(p);
    }

    public List<Producto> listarProductos() throws SQLException {
        return servicio.obtenerProductos();
    }

    public void eliminarProducto(int idProducto) throws SQLException {
        servicio.eliminarProducto(idProducto);
    }
}
