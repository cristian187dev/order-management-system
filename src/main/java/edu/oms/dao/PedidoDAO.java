package edu.oms.dao;

import edu.oms.modelo.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/oms_db";
    private static final String USER = "cristian187dev";
    private static final String PASSWORD = "as0TEST2025";

    public int insertar(Pedido p) throws SQLException {
        String sql = "INSERT INTO Pedidos (id_cliente, estado_pago, fecha_pedido, hora_pedido, observaciones) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, p.getIdCliente());
            ps.setString(2, p.getEstadoPago());
            ps.setDate(3, Date.valueOf(p.getFechaPedido()));
            ps.setTime(4, Time.valueOf(p.getHoraPedido()));
            ps.setString(5, p.getObservaciones());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("No se pudo obtener el ID generado del pedido.");
    }

    public List<Pedido> obtenerTodos() throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.id_pedido, c.nombre AS nombre_cliente, p.estado_pago, p.fecha_pedido, p.hora_pedido, p.observaciones " +
                "FROM Pedidos p JOIN Clientes c ON p.id_cliente = c.id_cliente " +
                "ORDER BY p.fecha_pedido DESC, p.hora_pedido DESC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setIdPedido(rs.getInt("id_pedido"));
                p.setNombreCliente(rs.getString("nombre_cliente"));
                p.setEstadoPago(rs.getString("estado_pago"));
                p.setFechaPedido(rs.getDate("fecha_pedido").toLocalDate());
                p.setHoraPedido(rs.getTime("hora_pedido").toLocalTime());
                p.setObservaciones(rs.getString("observaciones"));
                lista.add(p);
            }
        }
        return lista;
    }

    public List<Pedido> obtenerPorFecha(java.time.LocalDate fecha) throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.id_pedido, c.nombre AS nombre_cliente, p.estado_pago, p.fecha_pedido, p.hora_pedido, p.observaciones " +
                "FROM Pedidos p JOIN Clientes c ON p.id_cliente = c.id_cliente " +
                "WHERE p.fecha_pedido = ? " +
                "ORDER BY p.hora_pedido DESC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fecha));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();
                    p.setIdPedido(rs.getInt("id_pedido"));
                    p.setNombreCliente(rs.getString("nombre_cliente"));
                    p.setEstadoPago(rs.getString("estado_pago"));
                    p.setFechaPedido(rs.getDate("fecha_pedido").toLocalDate());
                    p.setHoraPedido(rs.getTime("hora_pedido").toLocalTime());
                    p.setObservaciones(rs.getString("observaciones"));
                    lista.add(p);
                }
            }
        }
        return lista;
    }

    public void eliminar(int idPedido) throws SQLException {
        String sql = "DELETE FROM Pedidos WHERE id_pedido = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ps.executeUpdate();
        }
    }

    public void actualizar(Pedido p) throws SQLException {
        String sql = "UPDATE Pedidos SET estado_pago = ?, observaciones = ? WHERE id_pedido = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getEstadoPago());
            ps.setString(2, p.getObservaciones());
            ps.setInt(3, p.getIdPedido());
            ps.executeUpdate();
        }
    }
}
