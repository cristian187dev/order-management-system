package edu.oms.servicio;

import edu.oms.dao.PagoDAO;
import edu.oms.modelo.Pago;
import java.sql.SQLException;
import java.util.List;

public class PagoServicio {

    private final PagoDAO dao = new PagoDAO();

    public void registrarPago(Pago pago) throws SQLException {
        dao.registrarPago(pago);
    }

    public List<Pago> listarPagosPorCliente(int idCliente) throws SQLException {
        return dao.listarPagosPorCliente(idCliente);
    }

    public double obtenerTotalPagos(int idCliente) throws SQLException {
        return dao.obtenerTotalPagosPorCliente(idCliente);
    }
}