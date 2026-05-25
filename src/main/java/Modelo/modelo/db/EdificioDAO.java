/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.modelo.db;
import Modelo.Edificio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *DAO (Data Access Object) para la tabla edificios.
 * Aplica sobrecarga en los métodos de búsqueda.
 * 
 * @author felip
 */
public class EdificioDAO {

    // CREATE
    public boolean insertar(Edificio e) {
        String sql = "INSERT INTO edificios (nombre, descripcion, tipo, pisos) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getDescripcion());
            ps.setString(3, e.getTipo());
            ps.setInt(4, e.getPisos());
            ps.executeUpdate();
            System.out.println("Edificio insertado: " + e.getNombre());
            return true;
        } catch (SQLException ex) {
            System.err.println("Error al insertar edificio: " + ex.getMessage());
            return false;
        }
    }

    // READ - todos
    public List<Edificio> obtenerTodos() {
        List<Edificio> lista = new ArrayList<>();
        String sql = "SELECT * FROM edificios";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Edificio(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("tipo"),
                        rs.getInt("pisos")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtener edificios: " + ex.getMessage());
        }
        return lista;
    }

    // READ - por ID (sobrecarga)
    public Edificio obtenerPorId(int id) {
        String sql = "SELECT * FROM edificios WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Edificio(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("tipo"),
                        rs.getInt("pisos")
                );
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtener edificio: " + ex.getMessage());
        }
        return null;
    }

    // READ - por nombre (sobrecarga)
    public Edificio obtenerPorNombre(String nombre) {
        String sql = "SELECT * FROM edificios WHERE nombre = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Edificio(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("tipo"),
                        rs.getInt("pisos")
                );
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar edificio: " + ex.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean actualizar(Edificio e) {
        String sql = "UPDATE edificios SET nombre=?, descripcion=?, tipo=?, pisos=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getDescripcion());
            ps.setString(3, e.getTipo());
            ps.setInt(4, e.getPisos());
            ps.setInt(5, e.getId());
            ps.executeUpdate();
            System.out.println("Edificio actualizado: " + e.getNombre());
            return true;
        } catch (SQLException ex) {
            System.err.println("Error al actualizar edificio: " + ex.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean eliminar(int id) {
        String sql = "DELETE FROM edificios WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Edificio eliminado ID: " + id);
            return true;
        } catch (SQLException ex) {
            System.err.println("Error al eliminar edificio: " + ex.getMessage());
            return false;
        }
    }
}
