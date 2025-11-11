package edu.oms.dao;

import edu.oms.modelo.Cliente;
import java.sql.*;
import java.time.LocalDate;   // ← FALTABA ESTE IMPORT
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/oms_db";
    private static final String USER = "cristian187dev";
    private static final String PASSWORD = "as0TEST2025";

    public void insertar(Cliente c) throws SQLException {
        String sql = "INSERT INTO Clientes " +
                "(nombre, apellido, telefono, direccion, cuil, estado_usuario, fecha_inicio, fecha_fin) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellido());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getDireccion());
            ps.setString(5, c.getCuil());
            ps.setString(6, c.getEstadoUsuario());
            ps.setDate(7, Date.valueOf(c.getFechaInicio()));

            if (c.getFechaFin() != null)
                ps.setDate(8, Date.valueOf(c.getFechaFin()));
            else
                ps.setNull(8, Types.DATE);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
            throw e;
        }
    }

    public List<Cliente> obtenerTodos() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Clientes ORDER BY id_cliente ASC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearCliente(rs));
            }
        }
        return lista;
    }

    public List<Cliente> obtenerPorEstado(String estado) throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Clientes WHERE estado_usuario = ? ORDER BY id_cliente ASC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapearCliente(rs));
            }
        }
        return lista;
    }

    public void actualizarEstado(int idCliente, String nuevoEstado, LocalDate fechaFin) throws SQLException {
        String sql = "UPDATE Clientes SET estado_usuario = ?, fecha_fin = ? WHERE id_cliente = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            if (fechaFin != null)
                ps.setDate(2, Date.valueOf(fechaFin));
            else
                ps.setNull(2, Types.DATE);

            ps.setInt(3, idCliente);
            ps.executeUpdate();
        }
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdCliente(rs.getInt("id_cliente"));
        c.setNombre(rs.getString("nombre"));
        c.setApellido(rs.getString("apellido"));
        c.setTelefono(rs.getString("telefono"));
        c.setDireccion(rs.getString("direccion"));
        c.setCuil(rs.getString("cuil"));
        c.setEstadoUsuario(rs.getString("estado_usuario"));

        Date fi = rs.getDate("fecha_inicio");
        Date ff = rs.getDate("fecha_fin");
        c.setFechaInicio(fi != null ? fi.toLocalDate() : null);
        c.setFechaFin(ff != null ? ff.toLocalDate() : null);

        return c;
    }
}
