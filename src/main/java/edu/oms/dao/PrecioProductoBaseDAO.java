package edu.oms.dao;

import edu.oms.modelo.PrecioProductoBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrecioProductoBaseDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/oms_db";
    private static final String USER = "cristian187dev";
    private static final String PASSWORD = "as0TEST2025";

    public void insertar(PrecioProductoBase pb) throws SQLException {
        String sql = "INSERT INTO Precios_Productos_Base " +
                "(id_producto, precio_base, fecha_inicio_precio, fecha_fin_precio) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pb.getIdProducto());
            ps.setDouble(2, pb.getPrecioBase());
            ps.setDate(3, Date.valueOf(pb.getFechaInicioPrecio()));

            if (pb.getFechaFinPrecio() != null) {
                ps.setDate(4, Date.valueOf(pb.getFechaFinPrecio()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar precio base: " + e.getMessage());
            throw e;
        }
    }

    public List<PrecioProductoBase> obtenerTodos() throws SQLException {
        List<PrecioProductoBase> lista = new ArrayList<>();
        String sql = "SELECT pb.id_precio_base, pb.id_producto, p.nombre_producto, " +
                "pb.precio_base, pb.fecha_inicio_precio, pb.fecha_fin_precio " +
                "FROM Precios_Productos_Base pb " +
                "JOIN Productos p ON pb.id_producto = p.id_producto " +
                "ORDER BY pb.id_precio_base ASC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                PrecioProductoBase pb = new PrecioProductoBase();
                pb.setIdPrecioBase(rs.getInt("id_precio_base"));
                pb.setIdProducto(rs.getInt("id_producto"));
                pb.setNombreProducto(rs.getString("nombre_producto"));
                pb.setPrecioBase(rs.getDouble("precio_base"));

                Date fi = rs.getDate("fecha_inicio_precio");
                Date ff = rs.getDate("fecha_fin_precio");
                pb.setFechaInicioPrecio(fi != null ? fi.toLocalDate() : null);
                pb.setFechaFinPrecio(ff != null ? ff.toLocalDate() : null);

                lista.add(pb);
            }
        }

        return lista;
    }

    public void eliminar(int idPrecioBase) throws SQLException {
        String sql = "DELETE FROM Precios_Productos_Base WHERE id_precio_base = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPrecioBase);
            ps.executeUpdate();
        }
    }
}
