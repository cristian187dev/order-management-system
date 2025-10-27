package edu.oms.dao;

import edu.oms.modelo.DetallePedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetallePedidoDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/oms_db";
    private static final String USER = "cristian187dev";
    private static final String PASSWORD = "as0TEST2025";

    public void insertar(DetallePedido d) throws SQLException {
        String sql = "INSERT INTO Detalles (id_pedido, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, d.getIdPedido());
            ps.setInt(2, d.getIdProducto());
            ps.setDouble(3, d.getCantidad());
            ps.setDouble(4, d.getPrecioUnitario());
            ps.executeUpdate();
        }
    }

    public List<DetallePedido> obtenerPorPedido(int idPedido) throws SQLException {
        List<DetallePedido> lista = new ArrayList<>();
        String sql = """
            SELECT d.id_detalle, d.id_producto, p.nombre_producto, d.cantidad, d.precio_unitario
            FROM Detalles d
            JOIN Productos p ON d.id_producto = p.id_producto
            WHERE d.id_pedido = ?
            ORDER BY d.id_detalle ASC
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DetallePedido d = new DetallePedido();
                d.setIdDetalle(rs.getInt("id_detalle"));
                d.setIdProducto(rs.getInt("id_producto"));
                d.setNombreProducto(rs.getString("nombre_producto"));
                d.setCantidad(rs.getDouble("cantidad"));
                d.setPrecioUnitario(rs.getDouble("precio_unitario"));
                lista.add(d);
            }
        }

        return lista;
    }

    public void eliminar(int idDetalle) throws SQLException {
        String sql = "DELETE FROM Detalles WHERE id_detalle = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDetalle);
            ps.executeUpdate();
        }
    }
}
