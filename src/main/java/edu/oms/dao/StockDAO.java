package edu.oms.dao;

import edu.oms.modelo.StockProductoDia;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StockDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/oms_db";
    private static final String USER = "cristian187dev";
    private static final String PASSWORD = "as0TEST2025";

    public List<StockProductoDia> obtenerDemandaPorFecha(LocalDate fecha) throws SQLException {

        String sql = """
            SELECT 
                p.id_producto,
                p.nombre_producto,
                p.stock_actual,
                COALESCE((
                    SELECT SUM(d.cantidad)
                    FROM Detalles d
                    JOIN Pedidos pe2 ON pe2.id_pedido = d.id_pedido
                    WHERE d.id_producto = p.id_producto
                      AND pe2.fecha_pedido = ?
                ), 0) AS cantidad_pedida
            FROM Productos p
            ORDER BY p.nombre_producto
            """;

        List<StockProductoDia> lista = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(fecha));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idProd = rs.getInt("id_producto");
                    String nombre = rs.getString("nombre_producto");
                    double stock = rs.getDouble("stock_actual");
                    double pedida = rs.getDouble("cantidad_pedida");

                    StockProductoDia spd = new StockProductoDia(idProd, nombre, stock, pedida);
                    lista.add(spd);
                }
            }
        }

        return lista;
    }

    public void agregarStock(int idProducto, double cantidad) throws SQLException {
        String sql = "UPDATE Productos " +
                "SET stock_actual = COALESCE(stock_actual, 0) + ? " +
                "WHERE id_producto = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, cantidad);
            ps.setInt(2, idProducto);
            ps.executeUpdate();
        }
    }
}