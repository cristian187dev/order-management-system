package edu.oms.dao;

import edu.oms.modelo.Pago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/oms_db";
    private static final String USER = "cristian187dev";
    private static final String PASSWORD = "as0TEST2025";

    // INSERTAR PAGO
    public void registrarPago(Pago pago) throws SQLException {
        String sql = "INSERT INTO Pagos (id_cliente, id_factura, fecha_pago, monto, observaciones, metodo_pago) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pago.getIdCliente());

            if (pago.getIdFactura() != 0) {
                stmt.setInt(2, pago.getIdFactura());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            stmt.setDate(3, Date.valueOf(pago.getFechaPago()));
            stmt.setDouble(4, pago.getMonto());
            stmt.setString(5, pago.getObservaciones());
            stmt.setString(6, pago.getMetodoPago() != null ? pago.getMetodoPago() : "");

            stmt.executeUpdate();
        }
    }

    // LISTAR PAGOS POR CLIENTE
    public List<Pago> listarPagosPorCliente(int idCliente) throws SQLException {
        List<Pago> lista = new ArrayList<>();

        String sql = "SELECT id_pago, id_cliente, id_factura, fecha_pago, monto, observaciones, metodo_pago " +
                "FROM Pagos WHERE id_cliente = ? ORDER BY fecha_pago DESC, id_pago DESC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pago pago = new Pago();
                pago.setIdPago(rs.getInt("id_pago"));
                pago.setIdCliente(rs.getInt("id_cliente"));
                pago.setIdFactura(rs.getInt("id_factura"));
                pago.setFechaPago(rs.getDate("fecha_pago").toLocalDate());
                pago.setMonto(rs.getDouble("monto"));
                pago.setObservaciones(rs.getString("observaciones"));
                pago.setMetodoPago(rs.getString("metodo_pago"));

                lista.add(pago);
            }
        }

        return lista;
    }

    // TOTAL PAGADO POR CLIENTE
    public double obtenerTotalPagosPorCliente(int idCliente) throws SQLException {
        String sql = "SELECT SUM(monto) AS total FROM Pagos WHERE id_cliente = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return rs.getDouble("total");
        }
        return 0;
    }
}