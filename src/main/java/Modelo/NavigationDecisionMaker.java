/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.ArrayList;
import java.util.List; 

/**
 * Motor de toma de decisiones de navegación.
 * 
 * Analiza una ruta calculada (lista de nodos) y genera instrucciones
 * de navegación legibles que serán pronunciadas por el sistema TTS.
 * 
 * Responsabilidades:
 *   - Determinar la dirección de movimiento entre nodos consecutivos
 *   - Detectar giros (izquierda, derecha, recto)
 *   - Generar alertas especiales (escaleras, puertas, obstáculos cercanos)
 *   - Producir instrucciones paso a paso en español
 * 
 * SOBRECARGA: El método generateInstructions tiene dos versiones:
 *   - generateInstructions(path): instrucciones completas
 *   - generateInstructions(path, verbose): con nivel de detalle configurable
 * @author felip
 */
public class NavigationDecisionMaker {

    /**
     * Enumeración de direcciones cardinales para la navegación.
     */
    public enum Direction {
        NORTH("norte", 0, -1),
        SOUTH("sur", 0, 1),
        EAST("este", 1, 0),
        WEST("oeste", -1, 0),
        NORTHEAST("noreste", 1, -1),
        NORTHWEST("noroeste", -1, -1),
        SOUTHEAST("sureste", 1, 1),
        SOUTHWEST("suroeste", -1, 1),
        NONE("sin movimiento", 0, 0);

        private final String name;
        private final int dx;
        private final int dy;

        Direction(String name, int dx, int dy) {
            this.name = name;
            this.dx = dx;
            this.dy = dy;
        }

        public String getName() {
            return name;
        }

        public int getDx() {
            return dx;
        }

        public int getDy() {
            return dy;
        }
    }

    /**
     * Clase que representa una instrucción de navegación individual.
     */
    public static class NavigationInstruction {
        private final String message;
        private final InstructionType type;
        private final int stepNumber;

        public enum InstructionType {
            MOVE("Movimiento"),
            TURN("Giro"),
            ALERT("Alerta"),
            ARRIVAL("Llegada"),
            START("Inicio");

            private final String description;

            InstructionType(String description) {
                this.description = description;
            }

            public String getDescription() {
                return description;
            }
        }

        public NavigationInstruction(String message, InstructionType type, int stepNumber) {
            this.message = message;
            this.type = type;
            this.stepNumber = stepNumber;
        }

        public String getMessage() {
            return message;
        }

        public InstructionType getType() {
            return type;
        }

        public int getStepNumber() {
            return stepNumber;
        }

        @Override
        public String toString() {
            return String.format("[Paso %d - %s] %s", stepNumber, type.getDescription(), message);
        }
    }

    // ==================== Generación de instrucciones ====================

    /**
     * Genera instrucciones de navegación a partir de una ruta calculada.
     * 
     * @param path lista de nodos que forman la ruta
     * @return lista de instrucciones de navegación
     */
    public List<NavigationInstruction> generateInstructions(List<Node> path) {
        return generateInstructions(path, false);
    }

    /**
     * SOBRECARGA: Genera instrucciones con nivel de detalle configurable.
     * 
     * @param path    lista de nodos que forman la ruta
     * @param verbose true para instrucciones detalladas con coordenadas
     * @return lista de instrucciones de navegación
     */
    public List<NavigationInstruction> generateInstructions(List<Node> path, boolean verbose) {
        List<NavigationInstruction> instructions = new ArrayList<>();

        if (path == null || path.isEmpty()) {
            instructions.add(new NavigationInstruction(
                    "No hay ruta disponible.",
                    NavigationInstruction.InstructionType.ALERT, 0));
            return instructions;
        }

        if (path.size() == 1) {
            instructions.add(new NavigationInstruction(
                    "Ya se encuentra en el destino.",
                    NavigationInstruction.InstructionType.ARRIVAL, 1));
            return instructions;
        }

        int step = 1;

        // Instrucción de inicio
        Node startNode = path.get(0);
        instructions.add(new NavigationInstruction(
                verbose
                        ? String.format("Inicio de ruta en posición (%d, %d). Total de pasos: %d.",
                        startNode.getX(), startNode.getY(), path.size() - 1)
                        : String.format("Iniciando navegación. %d pasos hasta el destino.",
                        path.size() - 1),
                NavigationInstruction.InstructionType.START, step++));

        // Dirección anterior para detectar giros
        Direction previousDirection = Direction.NONE;

        for (int i = 0; i < path.size() - 1; i++) {
            Node current = path.get(i);
            Node next = path.get(i + 1);

            // Calcular dirección de movimiento
            Direction currentDirection = calculateDirection(current, next);

            // Detectar si hay un giro respecto al movimiento anterior
            if (previousDirection != Direction.NONE && previousDirection != currentDirection) {
                String turnInstruction = describeTurn(previousDirection, currentDirection);
                instructions.add(new NavigationInstruction(
                        turnInstruction,
                        NavigationInstruction.InstructionType.TURN, step++));
            }

            // Generar alerta especial según el tipo de nodo destino
            String alert = generateNodeAlert(next);
            if (alert != null) {
                instructions.add(new NavigationInstruction(
                        alert,
                        NavigationInstruction.InstructionType.ALERT, step++));
            }

            // Instrucción de movimiento
            String moveInstruction = verbose
                    ? String.format("Avance hacia el %s hasta la posición (%d, %d).",
                    currentDirection.getName(), next.getX(), next.getY())
                    : String.format("Avance hacia el %s.", currentDirection.getName());

            instructions.add(new NavigationInstruction(
                    moveInstruction,
                    NavigationInstruction.InstructionType.MOVE, step++));

            previousDirection = currentDirection;
        }

        // Instrucción de llegada
        Node lastNode = path.get(path.size() - 1);
        instructions.add(new NavigationInstruction(
                verbose
                        ? String.format("Ha llegado a su destino en posición (%d, %d).",
                        lastNode.getX(), lastNode.getY())
                        : "Ha llegado a su destino.",
                NavigationInstruction.InstructionType.ARRIVAL, step));

        return instructions;
    }

    // ==================== Métodos auxiliares ====================

    /**
     * Calcula la dirección cardinal entre dos nodos.
     * 
     * @param from nodo origen
     * @param to   nodo destino
     * @return dirección del movimiento
     */
    public Direction calculateDirection(Node from, Node to) {
        int dx = Integer.signum(to.getX() - from.getX());
        int dy = Integer.signum(to.getY() - from.getY());

        for (Direction dir : Direction.values()) {
            if (dir.getDx() == dx && dir.getDy() == dy) {
                return dir;
            }
        }
        return Direction.NONE;
    }

    /**
     * Describe el giro necesario entre dos direcciones.
     * 
     * @param from dirección actual
     * @param to   nueva dirección
     * @return descripción del giro en español
     */
    private String describeTurn(Direction from, Direction to) {
        // Calcular el ángulo relativo usando los vectores de dirección
        // Producto cruzado: from.dx * to.dy - from.dy * to.dx
        int cross = from.getDx() * to.getDy() - from.getDy() * to.getDx();

        if (cross > 0) {
            return String.format("Gire a la derecha (hacia el %s).", to.getName());
        } else if (cross < 0) {
            return String.format("Gire a la izquierda (hacia el %s).", to.getName());
        } else {
            // Producto punto negativo = giro de 180°
            int dot = from.getDx() * to.getDx() + from.getDy() * to.getDy();
            if (dot < 0) {
                return "Dé media vuelta.";
            }
            return String.format("Continúe hacia el %s.", to.getName());
        }
    }

    /**
     * Genera una alerta especial basada en el tipo de nodo.
     * 
     * @param node el nodo a evaluar
     * @return mensaje de alerta, o null si no requiere alerta
     */
    private String generateNodeAlert(Node node) {
        // POLIMORFISMO: El comportamiento varía según el tipo real del nodo
        if (node instanceof DoorNode doorNode) {
            return doorNode.isOpen()
                    ? "Atención: puerta detectada adelante. Está abierta."
                    : "Atención: puerta cerrada adelante. Necesita ser abierta.";
        }

        if (node instanceof StairsNode stairsNode) {
            return String.format("Precaución: escalera %s con %d peldaños.",
                    stairsNode.getDirection().getDescription(),
                    stairsNode.getNumberOfSteps());
        }

        if (node instanceof ObstacleNode obstacleNode) {
            return String.format("¡Alerta! Obstáculo detectado: %s.",
                    obstacleNode.getObstacleDescription());
        }

        return null; // Sin alerta para nodos normales
    }

    /**
     * Calcula la distancia total de una ruta.
     * 
     * @param path lista de nodos de la ruta
     * @return distancia total en unidades del mapa
     */
    public double calculateTotalDistance(List<Node> path) {
        if (path == null || path.size() < 2) {
            return 0.0;
        }

        double total = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            total += path.get(i).distanceTo(path.get(i + 1));
        }
        return total;
    }

    /**
     * SOBRECARGA: Calcula la distancia total ponderada (incluye costos de navegación).
     * 
     * @param path     lista de nodos de la ruta
     * @param weighted true para incluir costos de navegación en el cálculo
     * @return distancia total (ponderada si weighted es true)
     */
    public double calculateTotalDistance(List<Node> path, boolean weighted) {
        if (!weighted) {
            return calculateTotalDistance(path);
        }

        if (path == null || path.size() < 2) {
            return 0.0;
        }

        double total = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            Node current = path.get(i);
            Node next = path.get(i + 1);
            total += current.distanceTo(next) * next.getNavigationCost();
        }
        return total;
    }
}

