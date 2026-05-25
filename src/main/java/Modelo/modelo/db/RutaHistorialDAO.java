/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.modelo.db;
import Modelo.RutaHistorial;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *DAO para la tabla rutas_historial.
 * @author felip
 */
public class RutaHistorialDAO {

    public boolean insertar(RutaHistorial r) {
        String sql = "INSERT INTO rutas_historial (edificio_id, nodo_inicio, nodo_destino, algoritmo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, r.getEdificioId());
            ps.setString(2, r.getNodoInicio());
            ps.setString(3, r.getNodoDestino());
            ps.setString(4, r.getAlgoritmo());
            ps.executeUpdate();
            System.out.println("Ruta guardada en historial.");
            return true;
        } catch (SQLException ex) {
            System.err.println("Error al guardar ruta: " + ex.getMessage());
            return false;
        }
    }

    public List<RutaHistorial> obtenerTodos() {
        List<RutaHistorial> lista = new ArrayList<>();
        String sql = "SELECT * FROM rutas_historial ORDER BY fecha DESC";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new RutaHistorial(
                        rs.getInt("id"),
                        rs.getInt("edificio_id"),
                        rs.getString("nodo_inicio"),
                        rs.getString("nodo_destino"),
                        rs.getString("algoritmo"),
                        rs.getString("fecha")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtener historial: " + ex.getMessage());
        }
        return lista;
    }

    public List<RutaHistorial> obtenerPorEdificio(int edificioId) {
        List<RutaHistorial> lista = new ArrayList<>();
        String sql = "SELECT * FROM rutas_historial WHERE edificio_id = ? ORDER BY fecha DESC";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, edificioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new RutaHistorial(
                        rs.getInt("id"),
                        rs.getInt("edificio_id"),
                        rs.getString("nodo_inicio"),
                        rs.getString("nodo_destino"),
                        rs.getString("algoritmo"),
                        rs.getString("fecha")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtener historial: " + ex.getMessage());
        }
        return lista;
    }
}
