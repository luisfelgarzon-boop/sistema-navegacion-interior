/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 **
 * Enumeración que define los tipos de nodo posibles en el mapa de navegación.
 * 
 * Cada tipo tiene características distintas que afectan la navegación:
 * - Transitabilidad: si el nodo es accesible o no.
 * - Peso de navegación: factor que modifica el costo de travesía.
 * - Descripción: texto legible para instrucciones de voz.
 *
 * @author felip
 */
public enum NodeType {

    /** Espacio libre transitable sin obstáculos */
    FREE_SPACE("Espacio libre", true, 1.0),

    /** Puerta (transitable pero requiere interacción) */
    DOOR("Puerta", true, 1.5),

    /** Escalera (transitable con precaución adicional) */
    STAIRS("Escalera", true, 3.0),

    /** Obstáculo fijo (no transitable) */
    OBSTACLE("Obstáculo", false, Double.MAX_VALUE),

    /** Pared (no transitable) */
    WALL("Pared", false, Double.MAX_VALUE),

    /** Punto de inicio de la ruta */
    START("Punto de inicio", true, 1.0),

    /** Punto de destino de la ruta */
    DESTINATION("Destino", true, 1.0);

    private final String description;
    private final boolean traversable;
    private final double navigationWeight;

    NodeType(String description, boolean traversable, double navigationWeight) {
        this.description = description;
        this.traversable = traversable;
        this.navigationWeight = navigationWeight;
    }

    /** Descripción legible del tipo de nodo */
    public String getDescription() {
        return description;
    }

    /** Indica si el nodo es transitable */
    public boolean isTraversable() {
        return traversable;
    }

    /** Peso de navegación asociado al tipo */
    public double getNavigationWeight() {
        return navigationWeight;
    }

    @Override
    public String toString() {
        return description;
    }
}

