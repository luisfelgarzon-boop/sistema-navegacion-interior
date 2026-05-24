/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Voice;

/**
 * Interfaz que define el contrato para el motor de Text-to-Speech (TTS). 
 * POLIMORFISMO: Permite usar diferentes motores TTS:
 *   - FreeTTSEngine: implementación con FreeTTS
 *   - SystemTTSEngine: implementación con el TTS del sistema operativo
 * 
 * Se implementará en la Fase 5.
 * @author felip
 */
public interface VoiceEngine {

    /**
     * Inicializa el motor de voz.
     * 
     * @return true si la inicialización fue exitosa
     */
    boolean initialize();

    /**
     * Pronuncia un texto.
     * 
     * @param text texto a pronunciar
     */
    void speak(String text);

    /**
     * SOBRECARGA: Pronuncia un texto con una velocidad específica.
     * 
     * @param text texto a pronunciar
     * @param rate velocidad de habla (palabras por minuto)
     */
    void speak(String text, float rate);

    /**
     * Detiene la reproducción actual de voz. 
     */
    void stop();

    /**
     * Indica si el motor está reproduciendo voz actualmente.
     * 
     * @return true si está hablando
     */
    boolean isSpeaking();

    /**
     * Establece el volumen del TTS.
     * 
     * @param volume volumen de 0.0 (silencio) a 1.0 (máximo)
     */
    void setVolume(float volume);

    /**
     * Libera los recursos del motor de voz.
     */
    void release();

    /**
     * Retorna el nombre del motor TTS.
     * 
     * @return nombre descriptivo del motor
     */
    String getEngineName();
}

