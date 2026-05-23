/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
 
/**
 ***
 * Nodo especializado que representa una escalera en el mapa.
 * 
 * HERENCIA: Extiende Node para agregar propiedades específicas
 * de escaleras (número de peldaños, dirección).
 * 
 * SOBREESCRITURA: Sobreescribe getNavigationCost() para calcular
 * el costo basado en la cantidad de peldaños.
 *
 * @author felip
 */
public class StairsNode extends Node {

    /** Dirección de la escalera */
    public enum Direction {
        UP("subiendo"),
        DOWN("bajando");

        private final String description;

        Direction(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private final int numberOfSteps;
    private final Direction direction;

    /**
     * Constructor completo de un nodo tipo escalera.
     * 
     * @param id            identificador único
     * @param x             coordenada X
     * @param y             coordenada Y
     * @param numberOfSteps número de peldaños
     * @param direction     dirección (UP o DOWN)
     */
    public StairsNode(String id, int x, int y, int numberOfSteps, Direction direction) {
        super(id, x, y, NodeType.STAIRS);
        this.numberOfSteps = Math.max(1, numberOfSteps);
        this.direction = direction;
    }

    /**
     * SOBRECARGA: Constructor con dirección predeterminada (UP).
     */
    public StairsNode(String id, int x, int y, int numberOfSteps) {
        this(id, x, y, numberOfSteps, Direction.UP);
    }

    public int getNumberOfSteps() {
        return numberOfSteps;
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * SOBREESCRITURA: El costo de navegación se calcula en función
     * del número de peldaños. Más peldaños = mayor costo.
     */
    @Override
    public double getNavigationCost() {
        return 2.0 + (numberOfSteps * 0.3);
    }

    @Override
    public String toString() {
        return String.format("StairsNode{id='%s', pos=(%d,%d), steps=%d, dir=%s}",
                getId(), getX(), getY(), numberOfSteps, direction.getDescription());
    }
}
