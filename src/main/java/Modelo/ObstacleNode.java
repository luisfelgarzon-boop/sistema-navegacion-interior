/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
 
/**
 ***
 * Nodo especializado que representa un obstáculo detectado en el mapa.
 * 
 * HERENCIA: Extiende Node marcándolo como no transitable.
 * 
 * SOBREESCRITURA: Sobreescribe isTraversable() y getNavigationCost()
 * para garantizar que el obstáculo nunca sea seleccionado como ruta.
 *
 * @author felip
 */
public class ObstacleNode extends Node {

    private final String obstacleDescription;
    private final double detectionConfidence;

    /**
     * Constructor de un nodo tipo obstáculo.
     * 
     * @param id                   identificador único
     * @param x                    coordenada X
     * @param y                    coordenada Y
     * @param obstacleDescription  descripción del obstáculo detectado
     * @param detectionConfidence  confianza de la detección (0.0 a 1.0)
     */
    public ObstacleNode(String id, int x, int y, String obstacleDescription,
                        double detectionConfidence) {
        super(id, x, y, NodeType.OBSTACLE);
        this.obstacleDescription = obstacleDescription;
        this.detectionConfidence = Math.max(0.0, Math.min(1.0, detectionConfidence));
    }

    /**
     * SOBRECARGA: Constructor sin nivel de confianza (100% por defecto).
     */
    public ObstacleNode(String id, int x, int y, String obstacleDescription) {
        this(id, x, y, obstacleDescription, 1.0);
    }

    public String getObstacleDescription() {
        return obstacleDescription;
    }

    public double getDetectionConfidence() {
        return detectionConfidence;
    }

    /**
     * SOBREESCRITURA: Los obstáculos nunca son transitables.
     */
    @Override
    public boolean isTraversable() {
        return false;
    }

    /**
     * SOBREESCRITURA: Costo infinito para evitar la selección en rutas.
     */
    @Override
    public double getNavigationCost() {
        return Double.MAX_VALUE;
    }

    @Override
    public String toString() {
        return String.format("ObstacleNode{id='%s', pos=(%d,%d), desc='%s', conf=%.0f%%}",
                getId(), getX(), getY(), obstacleDescription, detectionConfidence * 100);
    }
}

