package edu.oms.servicio;

import edu.oms.dao.ProductoBolsaDAO;
import java.sql.SQLException;
import java.util.List;

public class ProductoBolsaServicio {

    private final ProductoBolsaDAO dao = new ProductoBolsaDAO();

    public List<Integer> obtenerProductosQueUsanBolsas() throws SQLException {
        return dao.obtenerProductosQueUsanBolsas();
    }

    public boolean usaBolsas(int idProducto) throws SQLException {
        return dao.usaBolsas(idProducto);
    }

    public void setUsaBolsas(int idProducto, boolean usaBolsas) throws SQLException {
        dao.setUsaBolsas(idProducto, usaBolsas);
    }
}