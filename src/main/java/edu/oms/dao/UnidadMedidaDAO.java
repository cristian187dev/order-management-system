package edu.oms.dao;

import edu.oms.modelo.UnidadMedida;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnidadMedidaDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/oms_db";
    private static final String USER = "cristian187dev";
    private static final String PASSWORD = "as0TEST2025";

    public void insertar(UnidadMedida u) throws SQLException {
        String sql = "INSERT INTO Unidades_medida (nombre, abreviatura, activo) VALUES (?, ?, 1)";
        try (Connection c = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getAbreviatura());
            ps.executeUpdate();
        }
    }

    public List<UnidadMedida> obtenerActivas() throws SQLException {
        List<UnidadMedida> lista = new ArrayList<>();
        String sql = "SELECT * FROM Unidades_medida WHERE activo=1 ORDER BY nombre";
        try (Connection c = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new UnidadMedida(
                        rs.getInt("id_unidad"),
                        rs.getString("nombre"),
                        rs.getString("abreviatura"),
                        rs.getBoolean("activo")
                ));
            }
        }
        return lista;
    }
}
