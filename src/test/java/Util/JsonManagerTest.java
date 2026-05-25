/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Util;

import Modelo.Edificio;
import Modelo.RutaHistorial;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author felip
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JsonManagerTest {

    @BeforeAll
    static void setUp() {
        JsonManager.inicializar();
    }

    @Test
    @Order(1)
    @DisplayName("JSON: guardar y leer edificio")
    void testGuardarEdificio() {
        Edificio e = new Edificio(99, "Edificio JSON", "Test", "Test", 1);
        JsonManager.guardarEdificio(e);

        List<Edificio> lista = JsonManager.obtenerEdificios();
        assertNotNull(lista);
        assertTrue(lista.stream().anyMatch(ed -> ed.getId() == 99));
    }

    @Test
    @Order(2)
    @DisplayName("JSON: actualizar edificio")
    void testActualizarEdificio() {
        Edificio e = new Edificio(99, "JSON Actualizado", "Act", "Test", 3);
        assertTrue(JsonManager.actualizarEdificio(e));

        List<Edificio> lista = JsonManager.obtenerEdificios();
        assertTrue(lista.stream()
                .anyMatch(ed -> ed.getNombre().equals("JSON Actualizado")));
    }

    @Test
    @Order(3)
    @DisplayName("JSON: eliminar edificio")
    void testEliminarEdificio() {
        assertTrue(JsonManager.eliminarEdificio(99));
        List<Edificio> lista = JsonManager.obtenerEdificios();
        assertFalse(lista.stream().anyMatch(ed -> ed.getId() == 99));
    }

    @Test
    @Order(4)
    @DisplayName("JSON: guardar y leer historial")
    void testHistorial() {
        RutaHistorial r = new RutaHistorial(1, "N-1-1", "N-9-9", "BFS");
        JsonManager.guardarRuta(r);

        List<RutaHistorial> lista = JsonManager.obtenerHistorial();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }
}