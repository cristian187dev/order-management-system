package edu.oms.controlador;

import edu.oms.servicio.ProductoBolsaServicio;
import java.sql.SQLException;
import java.util.List;

public class ProductoBolsaControlador {

    private final ProductoBolsaServicio servicio = new ProductoBolsaServicio();

    public List<Integer> obtenerProductosQueUsanBolsas() throws SQLException {
        return servicio.obtenerProductosQueUsanBolsas();
    }

    public boolean usaBolsas(int idProducto) throws SQLException {
        return servicio.usaBolsas(idProducto);
    }

    public void setUsaBolsas(int idProducto, boolean usaBolsas) throws SQLException {
        servicio.setUsaBolsas(idProducto, usaBolsas);
    }
}