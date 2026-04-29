/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.Algoritmo;

import Modelo.NavigationMap;
import Modelo.Node;
import java.util.List;

/**
 **
 * Interfaz que define una estrategia de navegación (pathfinding).
 * POLIMORFISMO: Diferentes algoritmos de navegación (BFS, Dijkstra, A*)
 * implementan esta misma interfaz, permitiendo intercambiarlos
 * dinámicamente en tiempo de ejecución (Strategy Pattern).
 * 
 * Se implementará en la Fase 2 con BFS y Dijkstra.
 *
 * @author felip
 */
public interface NavigationStrategy {

    /**
     * Encuentra la ruta óptima entre dos nodos en el mapa.
     * 
     * @param map    mapa de navegación (grafo)
     * @param start  nodo de inicio
     * @param target nodo de destino
     * @return lista ordenada de nodos que forman la ruta, o lista vacía si no hay ruta
     */
    List<Node> findPath(NavigationMap map, Node start, Node target);

    /**
     * Retorna el nombre del algoritmo de navegación.
     * 
     * @return nombre descriptivo del algoritmo
     */
    String getAlgorithmName();

    /**
     * Retorna una descripción del algoritmo.
     * 
     * @return descripción del algoritmo
     */
    String getDescription();
}

