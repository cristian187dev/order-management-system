package edu.oms.dao;

import edu.oms.modelo.Pedido;
import edu.oms.modelo.PedidoResumen;
import edu.oms.modelo.TotalPorDia;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/oms_db";
    private static final String USER = "cristian187dev";
    private static final String PASSWORD = "as0TEST2025";

    public int guardarPedido(Pedido p) throws SQLException {
        String sql = "INSERT INTO Pedidos (id_cliente, fecha_pedido, hora_pedido, estado_pago, observaciones) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, p.getIdCliente());
            ps.setDate(2, Date.valueOf(p.getFechaPedido()));
            ps.setTime(3, Time.valueOf(p.getHoraPedido()));
            ps.setString(4, p.getEstadoPago());
            ps.setString(5, p.getObservaciones());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }

    public void modificarPedido(Pedido p) throws SQLException {
        String sql = "UPDATE Pedidos SET estado_pago=?, observaciones=? WHERE id_pedido=?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getEstadoPago());
            ps.setString(2, p.getObservaciones());
            ps.setInt(3, p.getIdPedido());
            ps.executeUpdate();
        }
    }

    public void eliminarPedido(int idPedido) throws SQLException {
        String sqlDetalles = "DELETE FROM Detalles WHERE id_pedido = ?";
        String sqlPedido   = "DELETE FROM Pedidos  WHERE id_pedido = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            conn.setAutoCommit(false);
            try (PreparedStatement psDet = conn.prepareStatement(sqlDetalles);
                 PreparedStatement psPed = conn.prepareStatement(sqlPedido)) {

                psDet.setInt(1, idPedido);
                psDet.executeUpdate();

                psPed.setInt(1, idPedido);
                psPed.executeUpdate();

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<Pedido> listarPedidos() throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre, c.apellido FROM Pedidos p " +
                "JOIN Clientes c ON p.id_cliente = c.id_cliente " +
                "ORDER BY p.id_pedido DESC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Pedido p = new Pedido();
                p.setIdPedido(rs.getInt("id_pedido"));
                p.setIdCliente(rs.getInt("id_cliente"));
                p.setNombreCliente(rs.getString("nombre") + " " + rs.getString("apellido"));
                p.setFechaPedido(rs.getDate("fecha_pedido").toLocalDate());
                p.setHoraPedido(rs.getTime("hora_pedido").toLocalTime());
                p.setEstadoPago(rs.getString("estado_pago"));
                p.setObservaciones(rs.getString("observaciones"));

                lista.add(p);
            }
        }
        return lista;
    }

    public List<Pedido> listarPedidosPorFecha(LocalDate fecha) throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre, c.apellido FROM Pedidos p " +
                "JOIN Clientes c ON p.id_cliente = c.id_cliente " +
                "WHERE p.fecha_pedido = ? " +
                "ORDER BY p.hora_pedido DESC, p.id_pedido DESC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(fecha));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Pedido p = new Pedido();
                p.setIdPedido(rs.getInt("id_pedido"));
                p.setIdCliente(rs.getInt("id_cliente"));
                p.setNombreCliente(rs.getString("nombre") + " " + rs.getString("apellido"));
                p.setFechaPedido(rs.getDate("fecha_pedido").toLocalDate());
                p.setHoraPedido(rs.getTime("hora_pedido").toLocalTime());
                p.setEstadoPago(rs.getString("estado_pago"));
                p.setObservaciones(rs.getString("observaciones"));

                lista.add(p);
            }
        }
        return lista;
    }

    public List<TotalPorDia> obtenerTotalesPorDia(int idCliente) throws SQLException {

        String sql = """
                SELECT fecha_pedido AS fecha,
                       SUM(d.cantidad * d.precio_unitario) AS total_dia
                FROM Pedidos p
                JOIN Detalles d ON p.id_pedido = d.id_pedido
                WHERE p.id_cliente = ?
                GROUP BY fecha_pedido
                ORDER BY fecha_pedido ASC
                """;

        List<TotalPorDia> lista = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LocalDate fecha = rs.getDate("fecha").toLocalDate();
                double total = rs.getDouble("total_dia");
                lista.add(new TotalPorDia(fecha, total));
            }
        }
        return lista;
    }

    public List<PedidoResumen> obtenerPedidosPorFecha(int idCliente, LocalDate fecha) throws SQLException {

        String sql = """
                SELECT p.id_pedido,
                       p.fecha_pedido,
                       p.hora_pedido,
                       SUM(d.cantidad * d.precio_unitario) AS total
                FROM Pedidos p
                JOIN Detalles d ON p.id_pedido = d.id_pedido
                WHERE p.id_cliente = ? AND p.fecha_pedido = ?
                GROUP BY p.id_pedido, p.fecha_pedido, p.hora_pedido
                ORDER BY p.hora_pedido ASC
                """;

        List<PedidoResumen> lista = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ps.setDate(2, Date.valueOf(fecha));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(
                        new PedidoResumen(
                                rs.getInt("id_pedido"),
                                rs.getDate("fecha_pedido").toLocalDate(),
                                rs.getTime("hora_pedido").toLocalTime(),
                                rs.getDouble("total")
                        )
                );
            }
        }

        return lista;
    }

    public double obtenerTotalDeuda(int idCliente) throws SQLException {

        String sql = """
                SELECT COALESCE(SUM(d.cantidad * d.precio_unitario), 0) AS total
                FROM Pedidos p
                JOIN Detalles d ON p.id_pedido = d.id_pedido
                WHERE p.id_cliente = ?
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getDouble("total");
        }
        return 0;
    }
}