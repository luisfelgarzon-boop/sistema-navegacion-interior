/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.modelo.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Clase utilitaria que gestiona la conexión a MySQL.
 * Patrón Singleton: solo existe una conexión activa.
 * @author felip
 */
public class DatabaseConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/navegacion_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "";

    private static Connection instance = null;

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (instance == null || instance.isClosed()) {
            instance = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión a BD establecida.");
        }
        return instance;
    }

    public static void closeConnection() {
        try {
            if (instance != null && !instance.isClosed()) {
                instance.close();
                System.out.println("Conexión a BD cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}