/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author felip
 */
/**
 * Nodo especializado que representa una puerta en el mapa.
 * 
 * HERENCIA: Extiende Node para agregar propiedades específicas
 * de puertas (estado abierta/cerrada).
 * 
 * SOBREESCRITURA: Sobreescribe getNavigationCost() para reflejar
 * el costo variable según el estado de la puerta.
 */
public class DoorNode extends Node {

    private boolean open;

    /**
     * Constructor de un nodo tipo puerta.
     * 
     * @param id   identificador único
     * @param x    coordenada X
     * @param y    coordenada Y
     * @param open true si la puerta está abierta
     */
    public DoorNode(String id, int x, int y, boolean open) {
        super(id, x, y, NodeType.DOOR);
        this.open = open;
    }

    /**
     * SOBRECARGA: Constructor con puerta cerrada por defecto.
     */
    public DoorNode(String id, int x, int y) {
        this(id, x, y, false);
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * SOBREESCRITURA: El costo de navegación depende del estado de la puerta.
     * Una puerta cerrada tiene un costo mucho mayor.
     */
    @Override
    public double getNavigationCost() {
        return open ? 1.2 : 5.0;
    }

    /**
     * SOBREESCRITURA: Indica si la puerta es transitable.
     * Solo es transitable si está abierta.
     */
    @Override
    public boolean isTraversable() {
        return open;
    }

    @Override
    public String toString() {
        return String.format("DoorNode{id='%s', pos=(%d,%d), open=%b}",
                getId(), getX(), getY(), open);
    }
}

