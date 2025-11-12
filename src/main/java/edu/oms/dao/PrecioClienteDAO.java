package edu.oms.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import edu.oms.modelo.PrecioCliente;

public class PrecioClienteDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/oms_db";
    private static final String USER = "cristian187dev";
    private static final String PASSWORD = "as0TEST2025";

    public void insertar(PrecioCliente pc) throws SQLException {
        String sql = "INSERT INTO Precios_Clientes (id_cliente, id_producto, precio_unitario, fecha_inicio_precio, fecha_fin_precio) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pc.getIdCliente());
            ps.setInt(2, pc.getIdProducto());
            ps.setDouble(3, pc.getPrecioUnitario());
            ps.setDate(4, Date.valueOf(pc.getFechaInicioPrecio()));
            if (pc.getFechaFinPrecio() != null) {
                ps.setDate(5, Date.valueOf(pc.getFechaFinPrecio()));
            } else {
                ps.setNull(5, Types.DATE);
            }
            ps.executeUpdate();
        }
    }

    public List<PrecioCliente> obtenerTodos() throws SQLException {
        List<PrecioCliente> lista = new ArrayList<>();
        String sql = "SELECT pc.id_precio_cliente, c.nombre AS nombre_cliente, p.nombre_producto, pc.precio_unitario, pc.fecha_inicio_precio, pc.fecha_fin_precio " +
                "FROM Precios_Clientes pc " +
                "JOIN Clientes c ON pc.id_cliente = c.id_cliente " +
                "JOIN Productos p ON pc.id_producto = p.id_producto " +
                "ORDER BY pc.id_precio_cliente ASC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                PrecioCliente pc = new PrecioCliente();
                pc.setIdPrecioCliente(rs.getInt("id_precio_cliente"));
                pc.setNombreCliente(rs.getString("nombre_cliente"));
                pc.setNombreProducto(rs.getString("nombre_producto"));
                pc.setPrecioUnitario(rs.getDouble("precio_unitario"));
                Date fi = rs.getDate("fecha_inicio_precio");
                Date ff = rs.getDate("fecha_fin_precio");
                pc.setFechaInicioPrecio(fi != null ? fi.toLocalDate() : null);
                pc.setFechaFinPrecio(ff != null ? ff.toLocalDate() : null);
                lista.add(pc);
            }
        }
        return lista;
    }

    public void eliminar(int idPrecioCliente) throws SQLException {
        String sql = "DELETE FROM Precios_Clientes WHERE id_precio_cliente = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPrecioCliente);
            ps.executeUpdate();
        }
    }

    public Double obtenerPrecioVigente(int idCliente, int idProducto, LocalDate fecha) throws SQLException {
        String sql = "SELECT pc.precio_unitario " +
                "FROM Precios_Clientes pc " +
                "WHERE pc.id_cliente = ? AND pc.id_producto = ? " +
                "AND pc.fecha_inicio_precio <= ? " +
                "AND (pc.fecha_fin_precio IS NULL OR pc.fecha_fin_precio >= ?) " +
                "ORDER BY pc.fecha_inicio_precio DESC, pc.id_precio_cliente DESC " +
                "LIMIT 1";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ps.setInt(2, idProducto);
            ps.setDate(3, Date.valueOf(fecha));
            ps.setDate(4, Date.valueOf(fecha));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        }
        return null;
    }
}
