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
                COALESCE((
                    SELECT SUM(m.cantidad)
                    FROM Movimientos_Stock m
                    WHERE m.id_producto = p.id_producto
                      AND m.fecha < ?
                ), 0) AS stock_remanente,
                COALESCE((
                    SELECT SUM(m2.cantidad)
                    FROM Movimientos_Stock m2
                    WHERE m2.id_producto = p.id_producto
                      AND m2.fecha = ?
                      AND m2.cantidad > 0
                ), 0) AS stock_producido_hoy,
                COALESCE((
                    SELECT -SUM(m3.cantidad)
                    FROM Movimientos_Stock m3
                    WHERE m3.id_producto = p.id_producto
                      AND m3.fecha = ?
                      AND m3.cantidad < 0
                      AND m3.tipo = 'SALIDA_MANUAL'
                ), 0) AS stock_quitado_hoy,
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
            ps.setDate(2, Date.valueOf(fecha));
            ps.setDate(3, Date.valueOf(fecha));
            ps.setDate(4, Date.valueOf(fecha));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idProd = rs.getInt("id_producto");
                    String nombre = rs.getString("nombre_producto");
                    double remanente = rs.getDouble("stock_remanente");
                    double producidoHoy = rs.getDouble("stock_producido_hoy");
                    double quitadoHoy = rs.getDouble("stock_quitado_hoy");
                    double pedida = rs.getDouble("cantidad_pedida");

                    StockProductoDia spd = new StockProductoDia(
                            idProd,
                            nombre,
                            remanente,
                            producidoHoy,
                            quitadoHoy,
                            pedida
                    );
                    lista.add(spd);
                }
            }
        }

        return lista;
    }

    public void agregarStock(int idProducto, double cantidad) throws SQLException {

        String sqlUpdate = "UPDATE Productos " +
                "SET stock_actual = COALESCE(stock_actual, 0) + ? " +
                "WHERE id_producto = ?";

        String sqlMovimiento = """
                INSERT INTO Movimientos_Stock
                    (id_producto, fecha, tipo, cantidad, observacion)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            conn.setAutoCommit(false);
            try (PreparedStatement psUpd = conn.prepareStatement(sqlUpdate);
                 PreparedStatement psMov = conn.prepareStatement(sqlMovimiento)) {

                psUpd.setDouble(1, cantidad);
                psUpd.setInt(2, idProducto);
                psUpd.executeUpdate();

                psMov.setInt(1, idProducto);
                psMov.setDate(2, Date.valueOf(LocalDate.now()));
                psMov.setString(3, "INGRESO");
                psMov.setDouble(4, cantidad);
                psMov.setString(5, "Ingreso manual de stock");
                psMov.executeUpdate();

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void quitarStock(int idProducto, double cantidad) throws SQLException {

        String sqlUpdate = "UPDATE Productos " +
                "SET stock_actual = COALESCE(stock_actual, 0) - ? " +
                "WHERE id_producto = ?";

        String sqlMovimiento = """
                INSERT INTO Movimientos_Stock
                    (id_producto, fecha, tipo, cantidad, observacion)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            conn.setAutoCommit(false);
            try (PreparedStatement psUpd = conn.prepareStatement(sqlUpdate);
                 PreparedStatement psMov = conn.prepareStatement(sqlMovimiento)) {

                psUpd.setDouble(1, cantidad);
                psUpd.setInt(2, idProducto);
                psUpd.executeUpdate();

                psMov.setInt(1, idProducto);
                psMov.setDate(2, Date.valueOf(LocalDate.now()));
                psMov.setString(3, "SALIDA_MANUAL");
                psMov.setDouble(4, -cantidad);
                psMov.setString(5, "Salida manual de stock");
                psMov.executeUpdate();

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void registrarSalidaPedido(int idProducto,
                                      double cantidad,
                                      LocalDate fechaPedido,
                                      int idPedido) throws SQLException {

        String sqlUpdate = "UPDATE Productos " +
                "SET stock_actual = COALESCE(stock_actual, 0) - ? " +
                "WHERE id_producto = ?";

        String sqlMovimiento = """
                INSERT INTO Movimientos_Stock
                    (id_producto, fecha, tipo, cantidad, observacion)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            conn.setAutoCommit(false);
            try (PreparedStatement psUpd = conn.prepareStatement(sqlUpdate);
                 PreparedStatement psMov = conn.prepareStatement(sqlMovimiento)) {

                psUpd.setDouble(1, cantidad);
                psUpd.setInt(2, idProducto);
                psUpd.executeUpdate();

                psMov.setInt(1, idProducto);
                psMov.setDate(2, Date.valueOf(fechaPedido));
                psMov.setString(3, "SALIDA_PEDIDO");
                psMov.setDouble(4, -cantidad);
                psMov.setString(5, "Salida por pedido ID " + idPedido);
                psMov.executeUpdate();

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}