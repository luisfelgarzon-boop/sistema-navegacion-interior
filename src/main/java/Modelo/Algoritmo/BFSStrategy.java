/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.Algoritmo;

import Modelo.NavigationMap;
import Modelo.NavigationMap.Edge;
import Modelo.Node; 
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Implementación del algoritmo BFS (Breadth-First Search) para navegación.
 * 
 * BFS encuentra la ruta con el menor número de pasos (nodos) entre
 * el origen y el destino. No considera los pesos de las aristas,
 * solo la cantidad de nodos intermedios.
 * 
 * POLIMORFISMO: Implementa la interfaz NavigationStrategy, permitiendo
 * ser intercambiado dinámicamente con DijkstraStrategy u otra implementación.
 * 
 * Uso ideal: cuando todos los pasillos tienen el mismo costo y se busca
 * la ruta más directa.
 * @author felip
 */
public class BFSStrategy implements NavigationStrategy {

    /**
     * Encuentra la ruta más corta en número de pasos usando BFS.
     * 
     * Complejidad: O(V + E) donde V = nodos, E = aristas.
     * 
     * @param map    mapa de navegación (grafo)
     * @param start  nodo de inicio
     * @param target nodo de destino
     * @return lista ordenada de nodos desde start hasta target, o vacía si no hay ruta
     */
    @Override
    public List<Node> findPath(NavigationMap map, Node start, Node target) {
        if (start == null || target == null) {
            return List.of();
        }
        if (start.equals(target)) {
            return List.of(start);
        }

        // Cola para BFS
        Queue<String> queue = new LinkedList<>();
        // Mapa de nodo visitado -> nodo padre (para reconstruir la ruta)
        Map<String, String> parentMap = new LinkedHashMap<>();

        String startId = start.getId();
        String targetId = target.getId();

        queue.add(startId);
        parentMap.put(startId, null); // El nodo inicial no tiene padre

        while (!queue.isEmpty()) {
            String currentId = queue.poll();

            // ¿Llegamos al destino?
            if (currentId.equals(targetId)) {
                return reconstructPath(map, parentMap, startId, targetId);
            }

            // Explorar vecinos
            List<Edge> neighbors = map.getNeighbors(currentId);
            for (Edge edge : neighbors) {
                String neighborId = edge.getTargetNodeId();
                Node neighborNode = map.getNode(neighborId);

                // Solo visitar nodos transitables y no visitados
                if (neighborNode != null
                        && neighborNode.isTraversable()
                        && !parentMap.containsKey(neighborId)) {

                    parentMap.put(neighborId, currentId);
                    queue.add(neighborId);
                }
            }
        }

        // No se encontró ruta
        return List.of();
    }

    /**
     * Reconstruye la ruta desde el destino hasta el origen usando el mapa de padres.
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
        return "BFS (Breadth-First Search)";
    }

    @Override
    public String getDescription() {
        return "Búsqueda en amplitud: encuentra la ruta con el menor número "
                + "de pasos, sin considerar pesos de las aristas. "
                + "Ideal para entornos con costos uniformes.";
    }

    @Override
    public String toString() {
        return getAlgorithmName();
    }
}
