/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Modelo.Algoritmo;

import Modelo.DoorNode;
import Modelo.MapBuilder;
import Modelo.NavigationMap;
import Modelo.Node;
import Modelo.NodeType;
import Modelo.StairsNode;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

/**
 * Pruebas unitarias para DijkstraStrategy.
 * Valida que Dijkstra encuentra rutas óptimas considerando pesos.
 * 
 * @author felip
 */
class DijkstraStrategyTest {

    private DijkstraStrategy dijkstra;

    @BeforeEach
    void setUp() {
        dijkstra = new DijkstraStrategy();
    }

    @Test
    @DisplayName("Dijkstra: ruta en pasillo lineal")
    void testLinearPath() {
        NavigationMap map = MapBuilder.buildCorridor(5);

        Node start = map.getNode(MapBuilder.nodeId(0, 0));
        Node end = map.getNode(MapBuilder.nodeId(4, 0));

        List<Node> path = dijkstra.findPath(map, start, end);

        assertFalse(path.isEmpty(), "Debe encontrar una ruta");
        assertEquals(start, path.get(0));
        assertEquals(end, path.get(path.size() - 1));
    }

    @Test
    @DisplayName("Dijkstra: ruta en cuadrícula sin obstáculos")
    void testGridPath() {
        NavigationMap map = MapBuilder.buildGrid(5, 5);

        Node start = map.getNode(MapBuilder.nodeId(0, 0));
        Node end = map.getNode(MapBuilder.nodeId(4, 4));

        List<Node> path = dijkstra.findPath(map, start, end);

        assertFalse(path.isEmpty(), "Debe encontrar una ruta");
        assertEquals(start, path.get(0));
        assertEquals(end, path.get(path.size() - 1));
    }

    @Test
    @DisplayName("Dijkstra: prefiere ruta de menor costo (evita escaleras)")
    void testPrefersLowerCostPath() {
        // Crear mapa con dos rutas:
        // Ruta 1: A -> B -> D (pasando por escalera, costo alto)
        // Ruta 2: A -> C -> D (espacios libres, costo bajo)
        NavigationMap map = new NavigationMap(4, 2);

        Node a = new Node("A", 0, 0, NodeType.START);
        StairsNode b = new StairsNode("B", 1, 0, 20); // Escalera con 20 peldaños = costo alto
        Node c = new Node("C", 0, 1, NodeType.FREE_SPACE);
        Node d = new Node("D", 1, 1, NodeType.DESTINATION);

        map.addNode(a);
        map.addNode(b);
        map.addNode(c);
        map.addNode(d);

        map.addEdge("A", "B", 1.0);
        map.addEdge("B", "D", 1.0);
        map.addEdge("A", "C", 1.0);
        map.addEdge("C", "D", 1.0);

        List<Node> path = dijkstra.findPath(map, a, d);

        assertFalse(path.isEmpty());
        // Dijkstra debe preferir A -> C -> D (costo menor) sobre A -> B -> D (escalera)
        assertEquals("A", path.get(0).getId());
        assertEquals("C", path.get(1).getId(), "Dijkstra debe evitar la escalera costosa");
        assertEquals("D", path.get(2).getId());
    }

    @Test
    @DisplayName("Dijkstra: evita puertas cerradas")
    void testAvoidsClosedDoors() {
        NavigationMap map = new NavigationMap(3, 2);

        Node a = new Node("A", 0, 0, NodeType.START);
        DoorNode closedDoor = new DoorNode("DOOR", 1, 0, false); // Cerrada = no transitable
        Node c = new Node("C", 0, 1, NodeType.FREE_SPACE);
        Node d = new Node("D", 1, 1, NodeType.FREE_SPACE);
        Node e = new Node("E", 2, 0, NodeType.DESTINATION);

        map.addNode(a);
        map.addNode(closedDoor);
        map.addNode(c);
        map.addNode(d);
        map.addNode(e);

        map.addEdge("A", "C", 1.0);
        map.addEdge("C", "D", 1.0);
        map.addEdge("D", "E", 1.0);
        // No conectar a la puerta cerrada porque isTraversable() = false
        // connectAdjacentNodes lo maneja, pero aquí lo hacemos manualmente

        List<Node> path = dijkstra.findPath(map, a, e);

        assertFalse(path.isEmpty(), "Debe encontrar ruta alternativa");
        // La ruta no debe pasar por la puerta cerrada
        for (Node node : path) {
            assertNotEquals("DOOR", node.getId(), "No debe pasar por puerta cerrada");
        }
    }

    @Test
    @DisplayName("Dijkstra: ruta con obstáculos en cuadrícula")
    void testPathWithObstacles() {
        NavigationMap map = MapBuilder.buildGrid(5, 5, true);

        Node start = map.getNode(MapBuilder.nodeId(0, 0));
        Node end = map.getNode(MapBuilder.nodeId(4, 4));

        List<Node> path = dijkstra.findPath(map, start, end);

        assertFalse(path.isEmpty(), "Debe encontrar ruta evitando obstáculos");
        for (Node node : path) {
            assertTrue(node.isTraversable(), "Ningún nodo debe ser obstáculo: " + node);
        }
    }

    @Test
    @DisplayName("Dijkstra: inicio y destino iguales")
    void testSameStartAndEnd() {
        NavigationMap map = MapBuilder.buildCorridor(3);
        Node start = map.getNode(MapBuilder.nodeId(0, 0));

        List<Node> path = dijkstra.findPath(map, start, start);
        assertEquals(1, path.size());
    }

    @Test
    @DisplayName("Dijkstra: sin ruta posible")
    void testNoPath() {
        NavigationMap map = new NavigationMap(3, 1);
        Node a = new Node("A", 0, 0);
        Node b = new Node("B", 2, 0);
        map.addNode(a);
        map.addNode(b);

        List<Node> path = dijkstra.findPath(map, a, b);
        assertTrue(path.isEmpty());
    }

    @Test
    @DisplayName("Dijkstra: nombre del algoritmo correcto")
    void testAlgorithmName() {
        assertEquals("Dijkstra", dijkstra.getAlgorithmName());
        assertNotNull(dijkstra.getDescription());
    }

    @Test
    @DisplayName("Polimorfismo: BFS y Dijkstra con la misma interfaz")
    void testPolymorphism() {
        NavigationMap map = MapBuilder.buildCorridor(5);
        Node start = map.getNode(MapBuilder.nodeId(0, 0));
        Node end = map.getNode(MapBuilder.nodeId(4, 0));

        // Usar la interfaz NavigationStrategy polimórficamente
        NavigationStrategy strategy1 = new BFSStrategy();
        NavigationStrategy strategy2 = new DijkstraStrategy();

        List<Node> path1 = strategy1.findPath(map, start, end);
        List<Node> path2 = strategy2.findPath(map, start, end);

        // Ambos deben encontrar una ruta válida
        assertFalse(path1.isEmpty());
        assertFalse(path2.isEmpty());
        assertEquals(path1.get(0), path2.get(0), "Mismo inicio");
        assertEquals(path1.get(path1.size() - 1), path2.get(path2.size() - 1), "Mismo destino");
    }
}