/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Modelo;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

/**
 * Pruebas unitarias para NavigationMap.
 * Valida gestión de nodos, aristas y sobrecarga de addEdge.
 * @author felip
 */
class NavigationMapTest {

    private NavigationMap map;

    @BeforeEach
    void setUp() {
        map = new NavigationMap(10, 10);
    }

    @Test
    @DisplayName("Agregar y obtener nodos")
    void testAddAndGetNode() {
        Node node = new Node("N1", 0, 0);
        map.addNode(node);

        assertEquals(1, map.getNodeCount());
        assertNotNull(map.getNode("N1"));
        assertTrue(map.containsNode("N1"));
    }

    @Test
    @DisplayName("Agregar arista con peso explícito")
    void testAddEdgeWithWeight() {
        map.addNode(new Node("A", 0, 0));
        map.addNode(new Node("B", 1, 1));
        map.addEdge("A", "B", 2.5);

        assertEquals(1, map.getEdgeCount());
        assertEquals(1, map.getNeighbors("A").size());
        assertEquals(1, map.getNeighbors("B").size());
    }

    @Test
    @DisplayName("Agregar arista con peso automático (sobrecarga)")
    void testAddEdgeAutoWeight() {
        map.addNode(new Node("A", 0, 0));
        map.addNode(new Node("B", 3, 4));
        map.addEdge("A", "B"); // Peso calculado automáticamente

        assertEquals(1, map.getEdgeCount());
        // Peso = distancia(5.0) * costo_navegación(1.0) = 5.0
        double weight = map.getNeighbors("A").get(0).getWeight();
        assertEquals(5.0, weight, 0.001);
    }

    @Test
    @DisplayName("Eliminar nodo elimina también sus aristas")
    void testRemoveNode() {
        map.addNode(new Node("A", 0, 0));
        map.addNode(new Node("B", 1, 0));
        map.addNode(new Node("C", 2, 0));
        map.addEdge("A", "B", 1.0);
        map.addEdge("B", "C", 1.0);

        map.removeNode("B");

        assertEquals(2, map.getNodeCount());
        assertFalse(map.containsNode("B"));
        assertTrue(map.getNeighbors("A").isEmpty());
        assertTrue(map.getNeighbors("C").isEmpty());
    }

    @Test
    @DisplayName("Arista con nodos inexistentes lanza excepción")
    void testAddEdgeInvalidNodes() {
        map.addNode(new Node("A", 0, 0));
        assertThrows(IllegalArgumentException.class, () -> map.addEdge("A", "Z", 1.0));
    }
}

