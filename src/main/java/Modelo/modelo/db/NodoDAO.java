/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.modelo.db;
import Modelo.NodoDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * DAO para la tabla nodos.
 * @author felip
 */
public class NodoDAO {

    public boolean insertar(NodoDB n) {
        String sql = "INSERT INTO nodos (nodo_id, x, y, tipo, edificio_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, n.getNodoId());
            ps.setInt(2, n.getX());
            ps.setInt(3, n.getY());
            ps.setString(4, n.getTipo());
            ps.setInt(5, n.getEdificioId());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("Error al insertar nodo: " + ex.getMessage());
            return false;
        }
    }

    public List<NodoDB> obtenerPorEdificio(int edificioId) {
        List<NodoDB> lista = new ArrayList<>();
        String sql = "SELECT * FROM nodos WHERE edificio_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, edificioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new NodoDB(
                        rs.getInt("id"),
                        rs.getString("nodo_id"),
                        rs.getInt("x"),
                        rs.getInt("y"),
                        rs.getString("tipo"),
                        rs.getInt("edificio_id")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtener nodos: " + ex.getMessage());
        }
        return lista;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM nodos WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("Error al eliminar nodo: " + ex.getMessage());
            return false;
        }
    }
}
