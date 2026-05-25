/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Voice;
import java.io.IOException;
/**
 *
 * @author marti
 */
public class SystemTTSEngine implements VoiceEngine {

    private boolean speaking;
    private float volume;
    private float rate;

    public SystemTTSEngine() {
        this.speaking = false;
        this.volume   = 100;
        this.rate     = 0;
    }

    @Override
    public boolean initialize() {
        this.speaking = false;
        System.out.println("[Voz] Motor del sistema iniciado.");
        return true;
    }

    @Override
    public void speak(String text) {
        if (text == null || text.isEmpty()) return;

        this.speaking = true;
        try {
            String command = String.format(
                "PowerShell -Command \"Add-Type -AssemblyName System.Speech; " +
                "$synth = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                "$synth.SelectVoiceByHints(" +
                    "[System.Speech.Synthesis.VoiceGender]::Female, " +
                    "[System.Speech.Synthesis.VoiceAge]::Adult, 0, " +
                    "[System.Globalization.CultureInfo]::GetCultureInfo('es-ES')" +
                "); " +
                "$synth.Volume = %d; " +
                "$synth.Rate = %d; " +
                "$synth.Speak('%s')\"",
                (int) this.volume,
                (int) this.rate,
                text.replace("'", "")
            );

            Process process = Runtime.getRuntime().exec(new String[]{"cmd", "/c", command});
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("[Voz] Error en SystemTTS: " + e.getMessage());
        } finally {
            this.speaking = false;
        }
    }

    @Override
    public void speak(String text, float rate) {
        float previous = this.rate;
        if (rate > 150)      this.rate =  2;
        else if (rate < 100) this.rate = -2;
        else                 this.rate =  0;
        speak(text);
        this.rate = previous;
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
        if (volume < 0.0f) volume = 0.0f;
        if (volume > 1.0f) volume = 1.0f;
        this.volume = volume * 100;
    }

    @Override
    public void release() {
        this.speaking = false;
        System.out.println("[Voz] Motor del sistema liberado.");
    }

    @Override
    public String getEngineName() {
        return "Nativo del Sistema (Windows PowerShell TTS)";
    }
}