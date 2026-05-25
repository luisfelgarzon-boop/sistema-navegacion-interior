/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Modelo.modelo.db;

import Modelo.Edificio;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author felip
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EdificioDAOTest {

    private static EdificioDAO dao;
    private static int idTest;

    @BeforeAll
    static void setUp() {
        dao = new EdificioDAO();
    }

    @AfterAll
    static void tearDown() {
        DatabaseConnection.closeConnection();
    }

    @Test
    @Order(1)
    @DisplayName("POST: insertar edificio")
    void testInsertar() {
        Edificio e = new Edificio("Test JUnit", "Prueba", "Test", 2);
        assertTrue(dao.insertar(e), "Debe insertar correctamente");
    }

    @Test
    @Order(2)
    @DisplayName("GET: obtener todos")
    void testObtenerTodos() {
        List<Edificio> lista = dao.obtenerTodos();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("GET: obtener por nombre")
    void testObtenerPorNombre() {
        Edificio e = dao.obtenerPorNombre("Test JUnit");
        assertNotNull(e, "Debe encontrar el edificio");
        assertEquals(2, e.getPisos());
        idTest = e.getId();
    }

    @Test
    @Order(4)
    @DisplayName("GET: obtener por ID")
    void testObtenerPorId() {
        Edificio e = dao.obtenerPorId(idTest);
        assertNotNull(e);
        assertEquals("Test JUnit", e.getNombre());
    }

    @Test
    @Order(5)
    @DisplayName("PUT: actualizar edificio")
    void testActualizar() {
        Edificio e = dao.obtenerPorId(idTest);
        assertNotNull(e);
        e.setNombre("Test Actualizado");
        e.setPisos(5);
        assertTrue(dao.actualizar(e));

        Edificio actualizado = dao.obtenerPorId(idTest);
        assertEquals("Test Actualizado", actualizado.getNombre());
        assertEquals(5, actualizado.getPisos());
    }

    @Test
    @Order(6)
    @DisplayName("DELETE: eliminar edificio")
    void testEliminar() {
        assertTrue(dao.eliminar(idTest));
        assertNull(dao.obtenerPorId(idTest));
    }
}