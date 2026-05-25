/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Voice;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
/**
 *
 * @author marti
 */
public class FreeTTSEngine implements VoiceEngine {

    private Voice ttsVoice;
    private boolean speaking;

    public FreeTTSEngine() {
        this.speaking = false;
    }

    @Override
    public boolean initialize() {
        try {
            System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

            VoiceManager voiceManager = VoiceManager.getInstance();
            this.ttsVoice = voiceManager.getVoice("kevin16");

            if (this.ttsVoice != null) {
                this.ttsVoice.allocate();
                System.out.println("[Voz] FreeTTS iniciado correctamente.");
                return true;
            } else {
                System.err.println("[Voz] No se encontró la voz 'kevin16'.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("[Voz] Error al inicializar FreeTTS: " + e.getMessage());
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
            float originalRate = this.ttsVoice.getRate();
            this.ttsVoice.setRate(rate);
            speak(text);
            this.ttsVoice.setRate(originalRate);
        }
    }

    @Override
    public void stop() {
        this.speaking = false;
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