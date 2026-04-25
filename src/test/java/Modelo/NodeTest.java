/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Modelo;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

/**
 * Pruebas unitarias para la clase Node y sus subclases.
 * Valida herencia, polimorfismo, sobrecarga y sobreescritura.
 * @author felip
 */
class NodeTest {

    @Test
    @DisplayName("Node: creación con constructor completo")
    void testNodeCreation() {
        Node node = new Node("N1", 5, 10, NodeType.FREE_SPACE);
        assertEquals("N1", node.getId());
        assertEquals(5, node.getX());
        assertEquals(10, node.getY());
        assertEquals(NodeType.FREE_SPACE, node.getType());
    }

    @Test
    @DisplayName("Node: constructor sobrecargado (tipo por defecto)")
    void testNodeOverloadedConstructor() {
        Node node = new Node("N2", 3, 7);
        assertEquals(NodeType.FREE_SPACE, node.getType());
    }

    @Test
    @DisplayName("Node: distanceTo sobrecargado (Node y coordenadas)")
    void testDistanceToOverload() {
        Node a = new Node("A", 0, 0);
        Node b = new Node("B", 3, 4);

        // distanceTo(Node)
        assertEquals(5.0, a.distanceTo(b), 0.001);

        // distanceTo(int, int) - sobrecarga
        assertEquals(5.0, a.distanceTo(3, 4), 0.001);
    }

    @Test
    @DisplayName("DoorNode: herencia y sobreescritura de getNavigationCost")
    void testDoorNode() {
        DoorNode closedDoor = new DoorNode("D1", 2, 3, false);
        DoorNode openDoor = new DoorNode("D2", 4, 5, true);

        // Puerta cerrada: no transitable, costo alto
        assertFalse(closedDoor.isTraversable());
        assertEquals(5.0, closedDoor.getNavigationCost(), 0.001);

        // Puerta abierta: transitable, costo bajo
        assertTrue(openDoor.isTraversable());
        assertEquals(1.2, openDoor.getNavigationCost(), 0.001);
    }

    @Test
    @DisplayName("StairsNode: herencia y costo proporcional a peldaños")
    void testStairsNode() {
        StairsNode stairs = new StairsNode("S1", 1, 1, 10, StairsNode.Direction.UP);

        assertTrue(stairs.isTraversable());
        assertEquals(10, stairs.getNumberOfSteps());
        // Costo: 2.0 + (10 * 0.3) = 5.0
        assertEquals(5.0, stairs.getNavigationCost(), 0.001);
    }

    @Test
    @DisplayName("ObstacleNode: herencia, no transitable y costo infinito")
    void testObstacleNode() {
        ObstacleNode obstacle = new ObstacleNode("O1", 6, 8, "Silla");

        assertFalse(obstacle.isTraversable());
        assertEquals(Double.MAX_VALUE, obstacle.getNavigationCost());
        assertEquals("Silla", obstacle.getObstacleDescription());
    }

    @Test
    @DisplayName("Polimorfismo: getNavigationCost varía según el tipo de nodo")
    void testPolymorphism() {
        Node freeNode = new Node("F1", 0, 0, NodeType.FREE_SPACE);
        Node door = new DoorNode("D1", 1, 1, true);
        Node stairs = new StairsNode("S1", 2, 2, 5);
        Node obstacle = new ObstacleNode("O1", 3, 3, "Mesa");

        // Cada subclase retorna un costo diferente
        assertEquals(1.0, freeNode.getNavigationCost(), 0.001);
        assertEquals(1.2, door.getNavigationCost(), 0.001);
        assertEquals(3.5, stairs.getNavigationCost(), 0.001); // 2.0 + 5*0.3
        assertEquals(Double.MAX_VALUE, obstacle.getNavigationCost());
    }

    @Test
    @DisplayName("Node: equals y hashCode basados en ID")
    void testEqualsAndHashCode() {
        Node a = new Node("Same", 0, 0);
        Node b = new Node("Same", 5, 5, NodeType.DOOR);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
