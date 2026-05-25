/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Voice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FreeTTSEngineTest {

    private FreeTTSEngine engine;

    @BeforeEach
    public void setUp() {
        engine = new FreeTTSEngine();
    }

    @Test
    public void testInitialStateIsNotSpeaking() {
        assertFalse(engine.isSpeaking(), "El motor no debería estar hablando al iniciar.");
    }

    @Test
    public void testInitializeReturnsTrue() {
        boolean result = engine.initialize();
        assertTrue(result, "La inicialización de FreeTTSEngine debería retornar true.");
    }

    @Test
    public void testGetEngineName() {
        String name = engine.getEngineName();
        assertNotNull(name, "El nombre del motor no debe ser nulo.");
        assertEquals("FreeTTS Engine (Kevin16)", name);
    }

    @Test
    public void testSpeakWithValidText() {
        engine.initialize();
        assertDoesNotThrow(() -> engine.speak("Iniciando sistema de navegación"), "speak() no debe lanzar excepciones.");
    }

    @Test
    public void testReleaseResources() {
        engine.initialize();
        assertDoesNotThrow(() -> engine.release(), "La liberación de recursos no debe lanzar errores.");
        assertFalse(engine.isSpeaking());
    }
}
