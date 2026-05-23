/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vision;
import Modelo.NodeType;
import java.util.Random;
/**
 *
 * @author JuanA
 */

public class SimulatedVisionProcessor implements VisionProcessor {

    private boolean active;
    private final Random random;

    public SimulatedVisionProcessor() { 
        this.random = new Random();
    }

    @Override
    public boolean initialize() {
        active = true;
        System.out.println("Sistema de visión simulado iniciado");
        return true;
    }

    @Override
    public DetectionResult processFrame() {

        if (!active) {
            return DetectionResult.empty();
        }

        int option = random.nextInt(4);

        return switch (option) {
            case 0 -> new DetectionResult(
                    NodeType.OBSTACLE,
                    0.92,
                    120,
                    80,
                    "Obstáculo detectado"
            );

            case 1 -> new DetectionResult(
                    NodeType.DOOR,
                    0.88,
                    200,
                    100,
                    "Puerta detectada"
            );

            case 2 -> new DetectionResult(
                    NodeType.STAIRS,
                    0.85,
                    150,
                    130,
                    "Escaleras detectadas"
            );

            default -> DetectionResult.empty();
        };
    }

    @Override
    public void release() {
        active = false;
        System.out.println("Sistema de visión detenido");
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
