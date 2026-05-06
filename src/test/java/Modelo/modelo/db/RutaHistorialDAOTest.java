/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Modelo.modelo.db;

import Modelo.RutaHistorial;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author felip
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RutaHistorialDAOTest {

    private static RutaHistorialDAO dao;

    @BeforeAll
    static void setUp() {
        dao = new RutaHistorialDAO();
    }

    @AfterAll
    static void tearDown() {
        DatabaseConnection.closeConnection();
    }

    @Test
    @Order(1)
    @DisplayName("POST: insertar ruta en historial")
    void testInsertar() {
        RutaHistorial r = new RutaHistorial(1, "N-0-0", "N-5-5", "Dijkstra");
        assertTrue(dao.insertar(r), "Debe insertar la ruta");
    }

    @Test
    @Order(2)
    @DisplayName("GET: obtener historial completo")
    void testObtenerTodos() {
        List<RutaHistorial> lista = dao.obtenerTodos();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("GET: historial por edificio")
    void testObtenerPorEdificio() {
        List<RutaHistorial> lista = dao.obtenerPorEdificio(1);
        assertNotNull(lista);
    }
}