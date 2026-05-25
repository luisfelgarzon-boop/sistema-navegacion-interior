/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Voice;

/**
 *
 * @author marti
 */
package Voice; // Regla fundamental del paquete exigida en la guía

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class FreeTTSEngine implements VoiceEngine {
    
    private Voice ttsVoice;
    private boolean speaking;

    public FreeTTSEngine() {
        this.speaking = false;
    }

    @Override
    public boolean initialize() {
        try {
            VoiceManager voiceManager = VoiceManager.getInstance();
            
            this.ttsVoice = voiceManager.getVoice("kevin16");
            
            if (this.ttsVoice != null) {
                this.ttsVoice.allocate();
                return true; 
            } else {
                System.err.println("Error: No se pudo encontrar la voz 'kevin16'.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error al inicializar FreeTTS: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void speak(String text) {
        if (this.ttsVoice != null && text != null && !text.isEmpty()) {
            this.speaking = true;
            this.ttsVoice.speak(text);
            this.speaking = false;
        }
    }

    @Override
    public void speak(String text, float rate) {
        if (this.ttsVoice != null && text != null && !text.isEmpty()) {
            this.speaking = true;
            float originalRate = this.ttsVoice.getRate();
            this.ttsVoice.setRate(rate); // Ajusta la velocidad en palabras por minuto
            this.ttsVoice.speak(text);
            this.ttsVoice.setRate(originalRate); // Restaura la velocidad original
            this.speaking = false;
        }
    }

    @Override
    public void stop() {
        if (this.ttsVoice != null) {
            this.speaking = false;
        }
    }

    @Override
    public boolean isSpeaking() {
        return this.speaking;
    }

    @Override
    public void setVolume(float volume) {
        if (this.ttsVoice != null) {
            if (volume < 0.0f) volume = 0.0f;
            if (volume > 1.0f) volume = 1.0f;
            this.ttsVoice.setVolume(volume);
        }
    }

    @Override
    public void release() {
        if (this.ttsVoice != null) {
            this.ttsVoice.deallocate();
            this.speaking = false;
        }
    }

    @Override
    public String getEngineName() {
        return "FreeTTS Engine (Kevin16)";
    }
}
