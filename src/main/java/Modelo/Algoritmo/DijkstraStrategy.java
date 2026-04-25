/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.Algoritmo;

import Modelo.NavigationMap;
import Modelo.NavigationMap.Edge;
import Modelo.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Implementación del algoritmo de Dijkstra para navegación ponderada.
 * 
 * Dijkstra encuentra la ruta con el menor COSTO TOTAL, considerando
 * los pesos de las aristas y los costos de navegación de cada nodo.
 * Esto significa que evita obstáculos costosos (escaleras, puertas cerradas)
 * si existe una ruta alternativa más eficiente.
 * 
 * POLIMORFISMO: Implementa NavigationStrategy junto con BFSStrategy,
 * demostrando cómo la misma interfaz puede tener comportamientos distintos.
 * 
 * Uso ideal: cuando los caminos tienen costos diferentes (escaleras más
 * costosas que pasillos, puertas cerradas vs abiertas, etc.)
 * @author felip
 */
public class DijkstraStrategy implements NavigationStrategy {

    /**
     * Nodo interno para la cola de prioridad de Dijkstra.
     * Almacena el ID del nodo y la distancia acumulada.
     */
    private static class DijkstraNode implements Comparable<DijkstraNode> {
        final String nodeId;
        final double distance;

        DijkstraNode(String nodeId, double distance) {
            this.nodeId = nodeId;
            this.distance = distance;
        }

        /**
         * Comparación por distancia para la cola de prioridad.
         * El nodo con menor distancia tiene mayor prioridad.
         */
        @Override
        public int compareTo(DijkstraNode other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    /**
     * Encuentra la ruta de menor costo total usando Dijkstra.
     * 
     * Complejidad: O((V + E) * log V) con cola de prioridad.
     * 
     * @param map    mapa de navegación (grafo ponderado)
     * @param start  nodo de inicio
     * @param target nodo de destino
     * @return lista ordenada de nodos con la ruta de menor costo, o vacía si no hay ruta
     */
    @Override
    public List<Node> findPath(NavigationMap map, Node start, Node target) {
        if (start == null || target == null) {
            return List.of();
        }
        if (start.equals(target)) {
            return List.of(start);
        }

        String startId = start.getId();
        String targetId = target.getId();

        // Distancias mínimas conocidas desde el inicio
        Map<String, Double> distances = new HashMap<>();
        // Mapa de padres para reconstruir la ruta
        Map<String, String> parentMap = new HashMap<>();
        // Nodos ya procesados
        Set<String> visited = new HashSet<>();
        // Cola de prioridad (menor distancia primero)
        PriorityQueue<DijkstraNode> pq = new PriorityQueue<>();

        // Inicializar distancias a infinito
        for (Node node : map.getAllNodes()) {
            distances.put(node.getId(), Double.MAX_VALUE);
        }

        // El nodo de inicio tiene distancia 0
        distances.put(startId, 0.0);
        pq.add(new DijkstraNode(startId, 0.0));

        while (!pq.isEmpty()) {
            DijkstraNode current = pq.poll();
            String currentId = current.nodeId;

            // Si ya fue procesado, ignorar
            if (visited.contains(currentId)) {
                continue;
            }
            visited.add(currentId);

            // ¿Llegamos al destino?
            if (currentId.equals(targetId)) {
                return reconstructPath(map, parentMap, startId, targetId);
            }

            // Explorar vecinos
            List<Edge> neighbors = map.getNeighbors(currentId);
            for (Edge edge : neighbors) {
                String neighborId = edge.getTargetNodeId();
                Node neighborNode = map.getNode(neighborId);

                // Solo considerar nodos transitables no visitados
                if (neighborNode == null
                        || !neighborNode.isTraversable()
                        || visited.contains(neighborId)) {
                    continue;
                }

                // Calcular nueva distancia:
                // distancia_actual + peso_arista + costo_navegación_del_nodo
                double newDistance = distances.get(currentId)
                        + edge.getWeight()
                        + neighborNode.getNavigationCost();

                // ¿Es mejor que la distancia conocida?
                if (newDistance < distances.get(neighborId)) {
                    distances.put(neighborId, newDistance);
                    parentMap.put(neighborId, currentId);
                    pq.add(new DijkstraNode(neighborId, newDistance));
                }
            }
        }

        // No se encontró ruta
        return List.of();
    }

    /**
     * Reconstruye la ruta óptima desde el destino hasta el origen.
     * 
     * @param map       mapa de navegación
     * @param parentMap mapa de nodo -> padre
     * @param startId   ID del nodo de inicio
     * @param targetId  ID del nodo de destino
     * @return lista de nodos en orden desde start hasta target
     */
    private List<Node> reconstructPath(NavigationMap map,
                                       Map<String, String> parentMap,
                                       String startId, String targetId) {
        List<Node> path = new ArrayList<>();
        String currentId = targetId;

        while (currentId != null) {
            Node node = map.getNode(currentId);
            if (node != null) {
                path.add(node);
            }
            currentId = parentMap.get(currentId);
        }

        Collections.reverse(path);
        return path;
    }

    @Override
    public String getAlgorithmName() {
        return "Dijkstra";
    }

    @Override
    public String getDescription() {
        return "Algoritmo de Dijkstra: encuentra la ruta con el menor costo total, "
                + "considerando pesos de aristas y costos de navegación de cada nodo. "
                + "Ideal para entornos con costos variables (escaleras, puertas, etc.)";
    }

    @Override
    public String toString() {
        return getAlgorithmName();
    }
}

