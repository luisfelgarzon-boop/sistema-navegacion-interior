/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Vision;

/**
 * * Interfaz que define el contrato para el procesamiento de visión.
 *
 * POLIMORFISMO: Permite usar diferentes implementaciones:
 *   - CameraVisionProcessor: procesamiento con cámara real + OpenCV
 *   - SimulatedVisionProcessor: procesamiento simulado para pruebas
 *
 * El Controlador trabaja con esta interfaz sin importar
 * cuál implementación esté activa en tiempo de ejecución.
 * @author felip
 */
public interface VisionProcessor {

    /**
     * Inicializa el sistema de visión (cámara, modelos, etc.).
     * @return true si la inicialización fue exitosa
     */
    boolean initialize();

    /**
     * Captura y procesa un frame del entorno.
     * @return resultado de la detección
     */
    DetectionResult processFrame();

    /**
     * Libera los recursos del sistema de visión.
     */
    void release();

    /**
     * Indica si el sistema de visión está activo.
     * @return true si está activo
     */
    boolean isActive();
}