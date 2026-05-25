/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Modelo.Algoritmo;

import Modelo.MapBuilder;
import Modelo.NavigationMap;
import Modelo.Node;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

/**
 *Pruebas unitarias para BFSStrategy.
 * Valida que BFS encuentra rutas correctas en diferentes escenarios.
 * 
 * @author felip
 */
class BFSStrategyTest {

    private BFSStrategy bfs;

    @BeforeEach
    void setUp() {
        bfs = new BFSStrategy();
    }

    @Test
    @DisplayName("BFS: ruta en pasillo lineal")
    void testLinearPath() {
        NavigationMap map = MapBuilder.buildCorridor(5);

        Node start = map.getNode(MapBuilder.nodeId(0, 0));
        Node end = map.getNode(MapBuilder.nodeId(4, 0));

        List<Node> path = bfs.findPath(map, start, end);

        assertFalse(path.isEmpty(), "Debe encontrar una ruta");
        assertEquals(5, path.size(), "La ruta debe tener 5 nodos");
        assertEquals(start, path.get(0), "Debe iniciar en el nodo de inicio");
        assertEquals(end, path.get(path.size() - 1), "Debe terminar en el destino");
    }

    @Test
    @DisplayName("BFS: ruta en cuadrícula sin obstáculos")
    void testGridPath() {
        NavigationMap map = MapBuilder.buildGrid(5, 5);

        Node start = map.getNode(MapBuilder.nodeId(0, 0));
        Node end = map.getNode(MapBuilder.nodeId(4, 4));

        List<Node> path = bfs.findPath(map, start, end);

        assertFalse(path.isEmpty(), "Debe encontrar una ruta");
        assertEquals(start, path.get(0));
        assertEquals(end, path.get(path.size() - 1));
        // BFS encuentra la ruta más corta en pasos: Manhattan distance = 8 + 1 = 9
        assertEquals(9, path.size(), "BFS debe encontrar ruta de 9 nodos (Manhattan)");
    }

    @Test
    @DisplayName("BFS: ruta evitando obstáculos")
    void testPathWithObstacles() {
        NavigationMap map = MapBuilder.buildGrid(5, 5, true);

        Node start = map.getNode(MapBuilder.nodeId(0, 0));
        Node end = map.getNode(MapBuilder.nodeId(4, 4));

        List<Node> path = bfs.findPath(map, start, end);

        assertFalse(path.isEmpty(), "Debe encontrar ruta aunque haya obstáculos");
        // Verificar que ningún nodo de la ruta es obstáculo
        for (Node node : path) {
            assertTrue(node.isTraversable(),
                    "Ningún nodo de la ruta debe ser obstáculo: " + node);
        }
    }

    @Test
    @DisplayName("BFS: inicio y destino iguales")
    void testSameStartAndEnd() {
        NavigationMap map = MapBuilder.buildCorridor(3);
        Node start = map.getNode(MapBuilder.nodeId(0, 0));

        List<Node> path = bfs.findPath(map, start, start);

        assertEquals(1, path.size(), "Ruta de un solo nodo");
        assertEquals(start, path.get(0));
    }

    @Test
    @DisplayName("BFS: sin ruta posible (nodo aislado)")
    void testNoPath() {
        NavigationMap map = new NavigationMap(3, 1);
        Node a = new Node("A", 0, 0);
        Node b = new Node("B", 2, 0);
        map.addNode(a);
        map.addNode(b);
        // No se agrega arista, nodos desconectados

        List<Node> path = bfs.findPath(map, a, b);
        assertTrue(path.isEmpty(), "No debe haber ruta entre nodos desconectados");
    }

    @Test
    @DisplayName("BFS: nodos nulos retornan lista vacía")
    void testNullNodes() {
        NavigationMap map = MapBuilder.buildCorridor(3);
        List<Node> path = bfs.findPath(map, null, null);
        assertTrue(path.isEmpty());
    }

    @Test
    @DisplayName("BFS: nombre del algoritmo correcto")
    void testAlgorithmName() {
        assertEquals("BFS (Breadth-First Search)", bfs.getAlgorithmName());
        assertNotNull(bfs.getDescription());
    }
}
