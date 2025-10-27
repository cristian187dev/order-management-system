package edu.oms.dao;

import edu.oms.modelo.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/oms_db";
    private static final String USER = "cristian187dev";
    private static final String PASSWORD = "as0TEST2025";

    public void insertar(Producto p) throws SQLException {
        String sql = "INSERT INTO Productos (nombre_producto, unidad_medida, stock_actual) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombreProducto());
            ps.setString(2, p.getUnidadMedida());
            ps.setDouble(3, p.getStockActual());
            ps.executeUpdate();
        }
    }

    public List<Producto> obtenerTodos() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Productos ORDER BY id_producto ASC";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombreProducto(rs.getString("nombre_producto"));
                p.setUnidadMedida(rs.getString("unidad_medida"));
                p.setStockActual(rs.getDouble("stock_actual"));
                lista.add(p);
            }
        }
        return lista;
    }

    public void eliminar(int idProducto) throws SQLException {
        String sql = "DELETE FROM Productos WHERE id_producto = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ps.executeUpdate();
        }
    }
}
