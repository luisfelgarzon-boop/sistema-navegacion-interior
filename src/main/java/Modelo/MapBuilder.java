/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 * Clase utilitaria para construir mapas de navegación de ejemplo.
 * Proporciona métodos estáticos (factory) para crear mapas predefinidos
 * que permiten probar los algoritmos de navegación sin necesidad de
 * un sistema de visión real.
 * 
 * SOBRECARGA: buildGrid tiene dos versiones:
 *   - buildGrid(width, height): cuadrícula simple
 *   - buildGrid(width, height, addObstacles): con obstáculos opcionales
 * @author felip
 */
public final class MapBuilder {

    // Constructor privado — clase utilitaria no instanciable
    private MapBuilder() {
        throw new UnsupportedOperationException("Clase utilitaria, no instanciable.");
    }

    // ==================== Mapas predefinidos ====================

    /**
     * Construye un mapa de ejemplo que simula un piso de edificio.
     * 
     * Layout (10x8):
     * 
     *   S · · · D · · · · G
     *   · ■ ■ · · · ■ ■ · ·
     *   · ■ ■ · · · ■ ■ · ·
     *   · · · · E · · · · ·
     *   · · · · · · · · · ·
     *   · ■ ■ · · · ■ ■ · ·
     *   · ■ ■ · · · ■ ■ · ·
     *   · · · · · · · · · G
     * 
     *   S = Inicio, G = Destino, D = Puerta, E = Escalera, ■ = Obstáculo
     * 
     * @return mapa de navegación listo para usar
     */
    public static NavigationMap buildSampleFloor() {
        NavigationMap map = new NavigationMap(10, 8);

        // ===== Agregar nodos =====

        // Nodo de inicio
        Node start = new Node("start", 0, 0, NodeType.START);
        map.addNode(start);

        // Nodo destino 1 (esquina superior derecha)
        Node goal1 = new Node("goal1", 9, 0, NodeType.DESTINATION);
        map.addNode(goal1);

        // Nodo destino 2 (esquina inferior derecha)
        Node goal2 = new Node("goal2", 9, 7, NodeType.DESTINATION);
        map.addNode(goal2);

        // Puerta en posición (4, 0)
        DoorNode door = new DoorNode("door1", 4, 0, true);
        map.addNode(door);

        // Escalera en posición (4, 3)
        StairsNode stairs = new StairsNode("stairs1", 4, 3, 12, StairsNode.Direction.UP);
        map.addNode(stairs);

        // Obstáculos (simulan muebles/paredes interiores)
        map.addNode(new ObstacleNode("obs1", 1, 1, "Mesa de oficina"));
        map.addNode(new ObstacleNode("obs2", 2, 1, "Mesa de oficina"));
        map.addNode(new ObstacleNode("obs3", 1, 2, "Silla"));
        map.addNode(new ObstacleNode("obs4", 2, 2, "Silla"));
        map.addNode(new ObstacleNode("obs5", 6, 1, "Estantería"));
        map.addNode(new ObstacleNode("obs6", 7, 1, "Estantería"));
        map.addNode(new ObstacleNode("obs7", 6, 2, "Archivo"));
        map.addNode(new ObstacleNode("obs8", 7, 2, "Archivo"));
        map.addNode(new ObstacleNode("obs9", 1, 5, "Sofá"));
        map.addNode(new ObstacleNode("obs10", 2, 5, "Sofá"));
        map.addNode(new ObstacleNode("obs11", 1, 6, "Maceta"));
        map.addNode(new ObstacleNode("obs12", 2, 6, "Maceta"));
        map.addNode(new ObstacleNode("obs13", 6, 5, "Escritorio"));
        map.addNode(new ObstacleNode("obs14", 7, 5, "Escritorio"));
        map.addNode(new ObstacleNode("obs15", 6, 6, "Silla de oficina"));
        map.addNode(new ObstacleNode("obs16", 7, 6, "Silla de oficina"));

        // Espacios libres (pasillos y áreas transitables)
        addFreeNodes(map);

        // ===== Conectar nodos adyacentes =====
        connectAdjacentNodes(map);

        return map;
    }

    /**
     * Construye una cuadrícula simple sin obstáculos.
     * 
     * @param width  ancho de la cuadrícula
     * @param height alto de la cuadrícula
     * @return mapa tipo cuadrícula
     */
    public static NavigationMap buildGrid(int width, int height) {
        return buildGrid(width, height, false);
    }

    /**
     * SOBRECARGA: Construye una cuadrícula con obstáculos opcionales en el centro.
     * 
     * @param width        ancho de la cuadrícula
     * @param height       alto de la cuadrícula
     * @param addObstacles true para agregar obstáculos en el centro
     * @return mapa tipo cuadrícula
     */
    public static NavigationMap buildGrid(int width, int height, boolean addObstacles) {
        NavigationMap map = new NavigationMap(width, height);

        // Crear todos los nodos
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                String id = nodeId(x, y);

                if (addObstacles && isObstaclePosition(x, y, width, height)) {
                    map.addNode(new ObstacleNode(id, x, y, "Obstáculo central"));
                } else {
                    map.addNode(new Node(id, x, y, NodeType.FREE_SPACE));
                }
            }
        }

        // Marcar inicio y destino
        map.getNode(nodeId(0, 0)).setType(NodeType.START);
        map.getNode(nodeId(width - 1, height - 1)).setType(NodeType.DESTINATION);

        // Conectar nodos adyacentes
        connectAdjacentNodes(map);

        return map;
    }

    /**
     * Construye un pasillo simple (mapa lineal) ideal para pruebas básicas.
     * 
     * @param length longitud del pasillo
     * @return mapa lineal
     */
    public static NavigationMap buildCorridor(int length) {
        NavigationMap map = new NavigationMap(length, 1);

        for (int x = 0; x < length; x++) {
            String id = nodeId(x, 0);
            if (x == 0) {
                map.addNode(new Node(id, x, 0, NodeType.START));
            } else if (x == length - 1) {
                map.addNode(new Node(id, x, 0, NodeType.DESTINATION));
            } else {
                map.addNode(new Node(id, x, 0, NodeType.FREE_SPACE));
            }
        }

        // Conectar linealmente
        for (int x = 0; x < length - 1; x++) {
            map.addEdge(nodeId(x, 0), nodeId(x + 1, 0));
        }

        return map;
    }

    // ==================== Métodos auxiliares ====================

    /**
     * Genera un ID único para un nodo basado en sus coordenadas.
     * 
     * @param x coordenada X
     * @param y coordenada Y
     * @return ID en formato "n_X_Y"
     */
    public static String nodeId(int x, int y) {
        return String.format("n_%d_%d", x, y);
    }

    /**
     * Agrega nodos de espacio libre a las posiciones no ocupadas del mapa de ejemplo.
     */
    private static void addFreeNodes(NavigationMap map) {
        int width = map.getWidth();
        int height = map.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                String id = nodeId(x, y);
                // Solo agregar si la posición no tiene nodo aún
                if (!map.containsNode(id) && !hasNamedNodeAt(map, x, y)) {
                    map.addNode(new Node(id, x, y, NodeType.FREE_SPACE));
                }
            }
        }
    }

    /**
     * Verifica si ya existe un nodo con nombre específico en la posición dada.
     */
    private static boolean hasNamedNodeAt(NavigationMap map, int x, int y) {
        for (Node node : map.getAllNodes()) {
            if (node.getX() == x && node.getY() == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Conecta nodos adyacentes en el mapa (4 direcciones: arriba, abajo, izquierda, derecha).
     * Solo conecta nodos que existan y sean transitables.
     */
    private static void connectAdjacentNodes(NavigationMap map) {
        int width = map.getWidth();
        int height = map.getHeight();

        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}}; // N, S, W, E

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                String currentId = findNodeIdAt(map, x, y);
                if (currentId == null) continue;

                Node currentNode = map.getNode(currentId);
                if (currentNode == null || !currentNode.isTraversable()) continue;

                for (int[] dir : directions) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];

                    if (nx < 0 || nx >= width || ny < 0 || ny >= height) continue;

                    String neighborId = findNodeIdAt(map, nx, ny);
                    if (neighborId == null) continue;

                    Node neighborNode = map.getNode(neighborId);
                    if (neighborNode == null || !neighborNode.isTraversable()) continue;

                    // Verificar que la arista no exista ya
                    boolean alreadyConnected = map.getNeighbors(currentId).stream()
                            .anyMatch(e -> e.getTargetNodeId().equals(neighborId));

                    if (!alreadyConnected) {
                        map.addEdge(currentId, neighborId);
                    }
                }
            }
        }
    }

    /**
     * Busca el ID de un nodo en una posición (x, y).
     * Primero intenta con el ID estándar, luego busca por coordenadas.
     */
    private static String findNodeIdAt(NavigationMap map, int x, int y) {
        // Intentar con el ID estándar primero (más eficiente)
        String standardId = nodeId(x, y);
        if (map.containsNode(standardId)) {
            return standardId;
        }

        // Buscar por coordenadas (para nodos con nombres especiales)
        for (Node node : map.getAllNodes()) {
            if (node.getX() == x && node.getY() == y) {
                return node.getId();
            }
        }

        return null;
    }

    /**
     * Determina si una posición debe ser un obstáculo (para cuadrículas con obstáculos).
     * Coloca obstáculos en una franja central.
     */
    private static boolean isObstaclePosition(int x, int y, int width, int height) {
        int centerX = width / 2;
        int centerY = height / 2;

        // Crear una pared horizontal en el centro con un hueco
        return y == centerY && x >= centerX - 2 && x <= centerX + 2 && x != centerX;
    }
}

