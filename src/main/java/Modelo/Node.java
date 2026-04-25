/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import java.util.Objects;
/**
 *
 * @author felip
 */
/**
 * Clase base que representa un nodo en el mapa de navegación.
 * 
 * Un nodo es una posición discreta en el grafo del entorno interior.
 * Contiene coordenadas (x, y), un tipo y un identificador único.
 * 
 * HERENCIA: Esta clase está diseñada para ser extendida por tipos
 * especializados de nodo (DoorNode, StairsNode, ObstacleNode).
 * 
 * SOBREESCRITURA: Los métodos equals(), hashCode() y toString()
 * están sobreescritos. Las subclases pueden sobreescribir
 * getNavigationCost() para modificar el comportamiento de navegación.
 */
public class Node {

    private final String id;
    private final int x;
    private final int y;
    private NodeType type;

    /**
     * Constructor principal del nodo.
     * 
     * @param id   identificador único del nodo
     * @param x    coordenada X en la cuadrícula del mapa
     * @param y    coordenada Y en la cuadrícula del mapa
     * @param type tipo de nodo (FREE_SPACE, DOOR, OBSTACLE, etc.)
     */
    public Node(String id, int x, int y, NodeType type) {
        this.id = Objects.requireNonNull(id, "El ID del nodo no puede ser nulo");
        this.x = x;
        this.y = y;
        this.type = Objects.requireNonNull(type, "El tipo del nodo no puede ser nulo");
    }

    /**
     * SOBRECARGA: Constructor simplificado (tipo FREE_SPACE por defecto).
     * 
     * @param id identificador único del nodo
     * @param x  coordenada X
     * @param y  coordenada Y
     */
    public Node(String id, int x, int y) {
        this(id, x, y, NodeType.FREE_SPACE);
    }

    // ==================== Getters ====================

    public String getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public NodeType getType() {
        return type;
    }

    // ==================== Setters ====================

    public void setType(NodeType type) {
        this.type = Objects.requireNonNull(type);
    }

    // ==================== Lógica de navegación ====================

    /**
     * Retorna el costo de navegación para atravesar este nodo.
     * 
     * POLIMORFISMO: Las subclases pueden sobreescribir este método
     * para definir costos de navegación especializados.
     * 
     * @return costo de navegación
     */
    public double getNavigationCost() {
        return type.getNavigationWeight();
    }

    /**
     * Indica si este nodo es transitable.
     * 
     * @return true si se puede atravesar el nodo
     */
    public boolean isTraversable() {
        return type.isTraversable();
    }

    /**
     * Calcula la distancia euclidiana hacia otro nodo.
     * 
     * @param other el otro nodo
     * @return distancia euclidiana
     */
    public double distanceTo(Node other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * SOBRECARGA: Calcula la distancia euclidiana hacia coordenadas específicas.
     * 
     * @param targetX coordenada X del destino
     * @param targetY coordenada Y del destino
     * @return distancia euclidiana
     */
    public double distanceTo(int targetX, int targetY) {
        int dx = this.x - targetX;
        int dy = this.y - targetY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // ==================== Sobreescrituras de Object ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id.equals(node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Node{id='%s', pos=(%d,%d), type=%s}",
                id, x, y, type.getDescription());
    }
}

