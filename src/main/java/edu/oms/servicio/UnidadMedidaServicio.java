package edu.oms.servicio;

import edu.oms.dao.UnidadMedidaDAO;
import edu.oms.modelo.UnidadMedida;
import java.sql.SQLException;
import java.util.List;

public class UnidadMedidaServicio {
    private final UnidadMedidaDAO dao = new UnidadMedidaDAO();

    public void guardar(UnidadMedida u) throws SQLException {
        if (u.getNombre() == null || u.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre de la unidad es obligatorio.");
        if (u.getAbreviatura() == null || u.getAbreviatura().isBlank())
            throw new IllegalArgumentException("La abreviatura es obligatoria.");
        dao.insertar(u);
    }

    public List<UnidadMedida> listarActivas() throws SQLException {
        return dao.obtenerActivas();
    }
}
