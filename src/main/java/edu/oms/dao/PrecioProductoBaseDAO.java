package edu.oms.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import edu.oms.modelo.PrecioProductoBase;

public class PrecioProductoBaseDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/oms_db";
    private static final String USER = "cristian187dev";
    private static final String PASSWORD = "as0TEST2025";

    public void insertar(PrecioProductoBase pb) throws SQLException {
        String sql = "INSERT INTO Precios_Productos_Base (id_producto, precio_base, fecha_inicio_precio, fecha_fin_precio) VALUES (?, ?, ?, ?)";

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
        }
    }

    public List<PrecioProductoBase> obtenerTodos() throws SQLException {
        List<PrecioProductoBase> lista = new ArrayList<>();
        String sql = "SELECT pb.id_precio_base, pb.id_producto, p.nombre_producto, pb.precio_base, pb.fecha_inicio_precio, pb.fecha_fin_precio " +
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

    public Double obtenerPrecioBaseVigente(int idProducto, LocalDate fecha) throws SQLException {
        String sql = "SELECT pb.precio_base " +
                "FROM Precios_Productos_Base pb " +
                "WHERE pb.id_producto = ? " +
                "AND pb.fecha_inicio_precio <= ? " +
                "AND (pb.fecha_fin_precio IS NULL OR pb.fecha_fin_precio >= ?) " +
                "ORDER BY pb.fecha_inicio_precio DESC, pb.id_precio_base DESC " +
                "LIMIT 1";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ps.setDate(2, Date.valueOf(fecha));
            ps.setDate(3, Date.valueOf(fecha));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        }
        return null;
    }
}
