/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import java.util.*;
/** 
 ***
 * Representa el mapa del entorno interior como un grafo de nodos.
 * 
 * El mapa contiene nodos (posiciones) y aristas (conexiones entre posiciones).
 * Implementa un grafo no dirigido con pesos, donde los nodos representan
 * ubicaciones físicas y las aristas representan caminos transitables.
 * 
 * Este es el componente central del MODELO en la arquitectura MVC.
 *
 * @author felip
 */
public class NavigationMap {

    private final Map<String, Node> nodes;
    private final Map<String, List<Edge>> adjacencyList;
    private final int width;
    private final int height;

    /**
     * Clase interna que representa una arista (conexión) entre dos nodos.
     */
    public static class Edge {
        private final String targetNodeId;
        private final double weight;

        public Edge(String targetNodeId, double weight) {
            this.targetNodeId = targetNodeId;
            this.weight = weight;
        }

        public String getTargetNodeId() {
            return targetNodeId;
        }

        public double getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return String.format("Edge{to='%s', weight=%.2f}", targetNodeId, weight);
        }
    }

    /**
     * Constructor del mapa de navegación.
     * 
     * @param width  ancho del mapa (número de columnas)
     * @param height alto del mapa (número de filas)
     */
    public NavigationMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.nodes = new LinkedHashMap<>();
        this.adjacencyList = new HashMap<>();
    }

    // ==================== Gestión de nodos ====================

    /**
     * Agrega un nodo al mapa.
     * 
     * @param node nodo a agregar
     */
    public void addNode(Node node) {
        nodes.put(node.getId(), node);
        adjacencyList.putIfAbsent(node.getId(), new ArrayList<>());
    }

    /**
     * Obtiene un nodo por su ID.
     * 
     * @param id identificador del nodo
     * @return el nodo, o null si no existe
     */
    public Node getNode(String id) {
        return nodes.get(id);
    }

    /**
     * Retorna todos los nodos del mapa.
     * 
     * @return colección de nodos
     */
    public Collection<Node> getAllNodes() {
        return Collections.unmodifiableCollection(nodes.values());
    }

    /**
     * Verifica si un nodo existe en el mapa.
     * 
     * @param id identificador del nodo
     * @return true si el nodo existe
     */
    public boolean containsNode(String id) {
        return nodes.containsKey(id);
    }

    /**
     * Elimina un nodo y todas sus aristas asociadas.
     * 
     * @param id identificador del nodo a eliminar
     */
    public void removeNode(String id) {
        nodes.remove(id);
        adjacencyList.remove(id);
        // Eliminar aristas que apuntan a este nodo
        for (List<Edge> edges : adjacencyList.values()) {
            edges.removeIf(edge -> edge.getTargetNodeId().equals(id));
        }
    }

    // ==================== Gestión de aristas ====================

    /**
     * Agrega una arista bidireccional entre dos nodos.
     * 
     * @param fromId ID del nodo origen
     * @param toId   ID del nodo destino
     * @param weight peso de la arista
     */
    public void addEdge(String fromId, String toId, double weight) {
        if (!nodes.containsKey(fromId) || !nodes.containsKey(toId)) {
            throw new IllegalArgumentException(
                    "Ambos nodos deben existir en el mapa para crear una arista.");
        }
        adjacencyList.get(fromId).add(new Edge(toId, weight));
        adjacencyList.get(toId).add(new Edge(fromId, weight));
    }

    /**
     * SOBRECARGA: Agrega una arista con peso calculado automáticamente
     * a partir de la distancia euclidiana y el costo de navegación.
     * 
     * @param fromId ID del nodo origen
     * @param toId   ID del nodo destino
     */
    public void addEdge(String fromId, String toId) {
        Node from = nodes.get(fromId);
        Node to = nodes.get(toId);
        if (from == null || to == null) {
            throw new IllegalArgumentException(
                    "Ambos nodos deben existir en el mapa para crear una arista.");
        }
        double weight = from.distanceTo(to) * to.getNavigationCost();
        addEdge(fromId, toId, weight);
    }

    /**
     * Obtiene los vecinos de un nodo.
     * 
     * @param nodeId ID del nodo
     * @return lista de aristas hacia los vecinos
     */
    public List<Edge> getNeighbors(String nodeId) {
        List<Edge> edges = adjacencyList.get(nodeId);
        return edges != null ? Collections.unmodifiableList(edges) : Collections.emptyList();
    }

    // ==================== Información del mapa ====================

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getNodeCount() {
        return nodes.size();
    }

    public int getEdgeCount() {
        return adjacencyList.values().stream()
                .mapToInt(List::size)
                .sum() / 2; // Dividir por 2 porque es bidireccional
    }

    @Override
    public String toString() {
        return String.format("NavigationMap{size=%dx%d, nodes=%d, edges=%d}",
                width, height, getNodeCount(), getEdgeCount());
    }
}

