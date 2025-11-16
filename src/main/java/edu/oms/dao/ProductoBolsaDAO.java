package edu.oms.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoBolsaDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/oms_db";
    private static final String USER = "cristian187dev";
    private static final String PASSWORD = "as0TEST2025";

    public List<Integer> obtenerProductosQueUsanBolsas() throws SQLException {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT id_producto FROM Productos_Bolsas WHERE usa_bolsas = 1";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getInt("id_producto"));
            }
        }
        return lista;
    }

    public boolean usaBolsas(int idProducto) throws SQLException {
        String sql = "SELECT usa_bolsas FROM Productos_Bolsas WHERE id_producto = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("usa_bolsas") == 1;
                }
            }
        }
        return false;
    }

    public void setUsaBolsas(int idProducto, boolean usaBolsas) throws SQLException {
        String sql = "INSERT INTO Productos_Bolsas (id_producto, usa_bolsas) " +
                "VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE usa_bolsas = VALUES(usa_bolsas)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            ps.setInt(2, usaBolsas ? 1 : 0);
            ps.executeUpdate();
        }
    }
}