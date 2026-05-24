/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

/**
 * Constantes globales del sistema de navegación. 
 * Centraliza valores de configuración para evitar números mágicos
 * dispersos en el código.
 * @author felip
 */ 
public final class Constants {

    // ==================== Ventana principal ====================
    /** Ancho predeterminado de la ventana principal */
    public static final int WINDOW_WIDTH = 1024;
    /** Alto predeterminado de la ventana principal */
    public static final int WINDOW_HEIGHT = 768;
    /** Título de la ventana principal */
    public static final String WINDOW_TITLE = "Sistema de Navegación Inteligente en Interiores";

    // ==================== Mapa / Grafo ====================
    /** Peso predeterminado de una arista en el grafo */
    public static final double DEFAULT_EDGE_WEIGHT = 1.0;
    /** Distancia máxima para considerar dos nodos como adyacentes (metros) */
    public static final double MAX_ADJACENCY_DISTANCE = 2.0;

    // ==================== Visión (OpenCV) ====================
    /** Ancho de captura de la cámara (píxeles) */
    public static final int CAMERA_WIDTH = 640;
    /** Alto de captura de la cámara (píxeles) */
    public static final int CAMERA_HEIGHT = 480;
    /** Índice predeterminado de la cámara */
    public static final int DEFAULT_CAMERA_INDEX = 0;

    // ==================== Navegación ====================
    /** Distancia mínima al obstáculo para generar alerta (metros) */
    public static final double OBSTACLE_ALERT_DISTANCE = 1.5;
    /** Intervalo de actualización de la navegación (milisegundos) */
    public static final int NAVIGATION_UPDATE_INTERVAL_MS = 500;

    // ==================== TTS ====================
    /** Velocidad de habla predeterminada (palabras por minuto) */
    public static final float TTS_DEFAULT_RATE = 150.0f;
    /** Volumen predeterminado del TTS (0.0 a 1.0) */
    public static final float TTS_DEFAULT_VOLUME = 1.0f;

    // Constructor privado para evitar instanciación
    private Constants() {
        throw new UnsupportedOperationException("Clase de constantes, no instanciable.");
    }
}

