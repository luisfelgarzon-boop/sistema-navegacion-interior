/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Modelo;

import Modelo.Algoritmo.BFSStrategy;
import Modelo.NavigationDecisionMaker.Direction;
import Modelo.NavigationDecisionMaker.NavigationInstruction;
import Modelo.NavigationDecisionMaker.NavigationInstruction.InstructionType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

/**
 * Pruebas unitarias para NavigationDecisionMaker.
 * Valida la generación de instrucciones de navegación.
 * 
 * @author felip
 * 
 */
class NavigationDecisionMakerTest {

    private NavigationDecisionMaker decisionMaker;

    @BeforeEach
    void setUp() {
        decisionMaker = new NavigationDecisionMaker();
    }

    @Test
    @DisplayName("Instrucciones para ruta vacía")
    void testEmptyPath() {
        List<NavigationInstruction> instructions = decisionMaker.generateInstructions(List.of());
        assertEquals(1, instructions.size());
        assertEquals(InstructionType.ALERT, instructions.get(0).getType());
    }

    @Test
    @DisplayName("Instrucciones para ruta de un solo nodo (ya en destino)")
    void testSingleNodePath() {
        Node node = new Node("N1", 0, 0);
        List<NavigationInstruction> instructions = decisionMaker.generateInstructions(List.of(node));
        assertEquals(1, instructions.size());
        assertEquals(InstructionType.ARRIVAL, instructions.get(0).getType());
        assertTrue(instructions.get(0).getMessage().contains("destino"));
    }

    @Test
    @DisplayName("Instrucciones para pasillo lineal")
    void testCorridorInstructions() {
        NavigationMap map = MapBuilder.buildCorridor(4);
        Node start = map.getNode(MapBuilder.nodeId(0, 0));
        Node end = map.getNode(MapBuilder.nodeId(3, 0));

        List<Node> path = (List<Node>) new BFSStrategy().findPath(map, start, end);
        List<NavigationInstruction> instructions = decisionMaker.generateInstructions(path);

        assertFalse(instructions.isEmpty());

        // Debe empezar con instrucción de INICIO
        assertEquals(InstructionType.START, instructions.get(0).getType());

        // Debe terminar con instrucción de LLEGADA
        NavigationInstruction last = instructions.get(instructions.size() - 1);
        assertEquals(InstructionType.ARRIVAL, last.getType());

        // Debe tener instrucciones de MOVIMIENTO
        long moveCount = instructions.stream()
                .filter(i -> i.getType() == InstructionType.MOVE)
                .count();
        assertTrue(moveCount > 0, "Debe haber al menos una instrucción de movimiento");
    }

    @Test
    @DisplayName("Instrucciones detectan giros")
    void testTurnDetection() {
        // Crear ruta en L: derecha y luego abajo
        Node a = new Node("A", 0, 0);
        Node b = new Node("B", 1, 0);
        Node c = new Node("C", 1, 1); // Giro hacia abajo

        List<Node> path = List.of(a, b, c);
        List<NavigationInstruction> instructions = decisionMaker.generateInstructions(path);

        // Debe haber al menos una instrucción de GIRO
        long turnCount = instructions.stream()
                .filter(i -> i.getType() == InstructionType.TURN)
                .count();
        assertTrue(turnCount > 0, "Debe detectar el giro en la ruta en L");
    }

    @Test
    @DisplayName("Instrucciones generan alerta para puerta")
    void testDoorAlert() {
        Node a = new Node("A", 0, 0);
        DoorNode door = new DoorNode("D", 1, 0, true);

        List<Node> path = List.of(a, door);
        List<NavigationInstruction> instructions = decisionMaker.generateInstructions(path);

        long alertCount = instructions.stream()
                .filter(i -> i.getType() == InstructionType.ALERT)
                .count();
        assertTrue(alertCount > 0, "Debe generar alerta para la puerta");

        boolean hasDoorAlert = instructions.stream()
                .anyMatch(i -> i.getMessage().contains("puerta"));
        assertTrue(hasDoorAlert, "La alerta debe mencionar 'puerta'");
    }

    @Test
    @DisplayName("Instrucciones generan alerta para escalera")
    void testStairsAlert() {
        Node a = new Node("A", 0, 0);
        StairsNode stairs = new StairsNode("S", 1, 0, 15, StairsNode.Direction.DOWN);

        List<Node> path = List.of(a, stairs);
        List<NavigationInstruction> instructions = decisionMaker.generateInstructions(path);

        boolean hasStairsAlert = instructions.stream()
                .anyMatch(i -> i.getMessage().contains("escalera")
                        && i.getMessage().contains("15"));
        assertTrue(hasStairsAlert, "Debe alertar sobre escalera con 15 peldaños");
    }

    @Test
    @DisplayName("Modo verbose incluye coordenadas")
    void testVerboseMode() {
        Node a = new Node("A", 3, 7);
        Node b = new Node("B", 4, 7);

        List<NavigationInstruction> normal = decisionMaker.generateInstructions(List.of(a, b), false);
        List<NavigationInstruction> verbose = decisionMaker.generateInstructions(List.of(a, b), true);

        // El modo verbose debe incluir coordenadas
        boolean hasCoordinates = verbose.stream()
                .anyMatch(i -> i.getMessage().contains("posición"));
        assertTrue(hasCoordinates, "Modo verbose debe incluir posiciones");
    }

    @Test
    @DisplayName("Calcular dirección entre nodos")
    void testCalculateDirection() {
        Node center = new Node("C", 5, 5);

        assertEquals(Direction.NORTH, decisionMaker.calculateDirection(center, new Node("N", 5, 4)));
        assertEquals(Direction.SOUTH, decisionMaker.calculateDirection(center, new Node("S", 5, 6)));
        assertEquals(Direction.EAST, decisionMaker.calculateDirection(center, new Node("E", 6, 5)));
        assertEquals(Direction.WEST, decisionMaker.calculateDirection(center, new Node("W", 4, 5)));
    }

    @Test
    @DisplayName("Calcular distancia total de ruta")
    void testCalculateTotalDistance() {
        Node a = new Node("A", 0, 0);
        Node b = new Node("B", 3, 0);
        Node c = new Node("C", 3, 4);

        List<Node> path = List.of(a, b, c);

        // Distancia: sqrt(9) + sqrt(16) = 3 + 4 = 7
        assertEquals(7.0, decisionMaker.calculateTotalDistance(path), 0.001);
    }

    @Test
    @DisplayName("Sobrecarga: distancia total ponderada vs simple")
    void testWeightedDistance() {
        Node a = new Node("A", 0, 0, NodeType.FREE_SPACE);
        StairsNode stairs = new StairsNode("S", 1, 0, 10);

        List<Node> path = List.of(a, stairs);

        double simple = decisionMaker.calculateTotalDistance(path, false);
        double weighted = decisionMaker.calculateTotalDistance(path, true);

        // La distancia ponderada debe ser mayor porque la escalera tiene costo > 1
        assertTrue(weighted > simple,
                "Distancia ponderada debe ser mayor por el costo de la escalera");
    }
}
