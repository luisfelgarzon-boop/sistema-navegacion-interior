/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vision;

import Modelo.NodeType;

/**
 *
 * Resultado de una detección del sistema de visión.
 * Contiene el tipo de objeto detectado y su posición relativa.
 * @author felip
 */
public class DetectionResult {

    private final NodeType detectedType;
    private final double confidence;
    private final int relativeX;
    private final int relativeY;
    private final String description;

    public DetectionResult(NodeType detectedType, double confidence,
                           int relativeX, int relativeY, String description) {
        this.detectedType = detectedType;
        this.confidence = confidence;
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.description = description;
    }

    // Método estático de fábrica — crea resultado vacío
    public static DetectionResult empty() {
        return new DetectionResult(NodeType.FREE_SPACE, 0.0, 0, 0, "Sin detección");
    }

    public NodeType getDetectedType()  { return detectedType; }
    public double getConfidence()      { return confidence; }
    public int getRelativeX()          { return relativeX; }
    public int getRelativeY()          { return relativeY; }
    public String getDescription()     { return description; }

    @Override
    public String toString() {
        return String.format("DetectionResult{type=%s, conf=%.0f%%, pos=(%d,%d), desc='%s'}",
                detectedType, confidence * 100, relativeX, relativeY, description);
    }
}
