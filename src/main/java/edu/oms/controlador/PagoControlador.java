package edu.oms.controlador;

import edu.oms.modelo.Pago;
import edu.oms.servicio.PagoServicio;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PagoControlador {

    private final PagoServicio servicio = new PagoServicio();

    // Usado desde VentanaPagos
    public void registrarPago(int idCliente, double monto, String observaciones) throws SQLException {
        Pago p = new Pago();
        p.setIdCliente(idCliente);
        p.setIdFactura(0);
        p.setFechaPago(LocalDate.now());
        p.setMonto(monto);
        p.setObservaciones(observaciones);
        p.setMetodoPago("");

        servicio.registrarPago(p);
    }

    // Usado para mostrar historial en VentanaPagos
    public List<Pago> listarPagos(int idCliente) throws SQLException {
        return servicio.listarPagosPorCliente(idCliente);
    }

    // Usado en VentanaFacturacion para calcular Total Pagos
    public double obtenerTotalPagos(int idCliente) throws SQLException {
        return servicio.obtenerTotalPagos(idCliente);
    }
}