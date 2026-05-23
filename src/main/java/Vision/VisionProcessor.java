/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vision;

import Modelo.NodeType;

/**
 *Interfaz que define el contrato para el procesamiento de visión.
 * 
 * POLIMORFISMO: Permite usar diferentes implementaciones:
 *   - CameraVisionProcessor: procesamiento con cámara real + OpenCV
 *   - SimulatedVisionProcessor: procesamiento simulado para pruebas
 * 
 * Se implementará en la Fase 5
 * @author felip
 */
public interface VisionProcessor {

    /**
     * Inicializa el sistema de visión (cámara, modelos, etc.).
     * 
     * @return true si la inicialización fue exitosa 
     */
    boolean initialize();

    /**
     * Captura y procesa un frame del entorno.
     * 
     * @return resultado de la detección
     */
    DetectionResult processFrame();

    /**
     * Libera los recursos del sistema de visión.
     */
    void release();

    /**
     * Indica si el sistema de visión está activo.
     * 
     * @return true si está activo
     */
    boolean isActive();

    /**
     * Resultado de una detección de visión.
     * Contiene el tipo de objeto detectado y su posición relativa.
     */
    record DetectionResult(
            NodeType detectedType,
            double confidence,
            int relativeX,
            int relativeY,
            String description
    ) {
        /**
         * Crea un resultado vacío (sin detección).
         */
        public static DetectionResult empty() {
            return new DetectionResult(NodeType.FREE_SPACE, 0.0, 0, 0,
                    "Sin detección");
        }
    }
}
