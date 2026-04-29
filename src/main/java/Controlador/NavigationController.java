/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Algoritmo.BFSStrategy;
import Modelo.Algoritmo.DijkstraStrategy;
import Modelo.Algoritmo.NavigationStrategy;
import Modelo.MapBuilder;
import Modelo.NavigationDecisionMaker;
import Modelo.NavigationDecisionMaker.NavigationInstruction;
import Modelo.NavigationMap;
import Modelo.Node;
import Vision.VisionProcessor;
import Vista.MainView;
import Voice.VoiceEngine;
import java.util.List;

/**
 **
 * Controlador principal del sistema de navegación.
 * Actúa como intermediario entre la Vista (MainView) y el Modelo
 * (NavigationMap, NavigationStrategy, etc.) siguiendo el patrón MVC.
 * 
 * Responsabilidades:
 *   - Recibir eventos de la vista (iniciar/detener navegación)
 *   - Coordinar el modelo (calcular rutas)
 *   - Actualizar la vista con los resultados
 *   - Orquestar los módulos de visión y voz
 * @author felip
 */
public class NavigationController {

    private MainView view;
    private NavigationMap map;
    private NavigationStrategy strategy;
    private final NavigationDecisionMaker decisionMaker;
    private VisionProcessor visionProcessor;
    private VoiceEngine voiceEngine;

    private boolean navigationActive;
    private List<Node> currentPath;
    private List<NavigationInstruction> currentInstructions;
    private Node currentPosition;
    private int currentInstructionIndex;

    /**
     * Constructor del controlador.
     * Inicializa el DecisionMaker y carga un mapa de ejemplo por defecto.
     */
    public NavigationController() {
        this.decisionMaker = new NavigationDecisionMaker();
        this.navigationActive = false;
        this.currentPath = List.of();
        this.currentInstructions = List.of();
        this.currentInstructionIndex = 0;

        // Cargar mapa de ejemplo y estrategia por defecto
        this.map = MapBuilder.buildSampleFloor();
        this.strategy = new DijkstraStrategy();
    }

    // ==================== Inyección de dependencias ====================

    public void setView(MainView view) {
        this.view = view;
    }

    public void setMap(NavigationMap map) {
        this.map = map;
    }

    public void setStrategy(NavigationStrategy strategy) {
        this.strategy = strategy;
        updateStatus("Estrategia cambiada a: " + strategy.getAlgorithmName());
    }

    public void setVisionProcessor(VisionProcessor visionProcessor) {
        this.visionProcessor = visionProcessor;
    }

    public void setVoiceEngine(VoiceEngine voiceEngine) {
        this.voiceEngine = voiceEngine;
    }

    // ==================== Acciones de navegación ====================

    /**
     * Inicia el proceso de navegación.
     * Se invoca desde la vista cuando el usuario presiona "Iniciar".
     */
    public void startNavigation() {
        if (map == null) {
            updateStatus("Error: No hay mapa cargado.");
            return;
        }
        if (strategy == null) {
            updateStatus("Error: No hay estrategia de navegación seleccionada.");
            return;
        }

        navigationActive = true;
        updateStatus("Navegación iniciada con " + strategy.getAlgorithmName() + ".");
        updateStatus("Mapa cargado: " + map.toString());
        System.out.println("[Controller] Navegación iniciada.");
    }

    /**
     * Detiene el proceso de navegación.
     * Se invoca desde la vista cuando el usuario presiona "Detener".
     */
    public void stopNavigation() {
        navigationActive = false;
        currentPath = List.of();
        currentInstructions = List.of();
        currentInstructionIndex = 0;
        updateStatus("Navegación detenida.");
        showInstruction("Sistema en espera.");
        System.out.println("[Controller] Navegación detenida.");
    }

    /**
     * Calcula la ruta entre dos nodos usando la estrategia actual,
     * y genera las instrucciones de navegación correspondientes.
     * 
     * @param startId  ID del nodo de inicio
     * @param targetId ID del nodo de destino
     * @return lista de nodos que forman la ruta
     */
    public List<Node> calculateRoute(String startId, String targetId) {
        if (map == null || strategy == null) {
            return List.of();
        }

        Node start = map.getNode(startId);
        Node target = map.getNode(targetId);

        if (start == null || target == null) {
            updateStatus("Error: Nodos de inicio o destino no encontrados.");
            return List.of();
        }

        currentPath = strategy.findPath(map, start, target);

        if (currentPath.isEmpty()) {
            updateStatus("No se encontró una ruta hacia el destino.");
            showInstruction("No hay ruta disponible.");
        } else {
            updateStatus(String.format("Ruta encontrada: %d pasos usando %s.",
                    currentPath.size(), strategy.getAlgorithmName()));

            // Generar instrucciones de navegación con el DecisionMaker
            currentInstructions = decisionMaker.generateInstructions(currentPath);
            currentInstructionIndex = 0;

            // Mostrar resumen de distancia
            double distance = decisionMaker.calculateTotalDistance(currentPath);
            double weightedDistance = decisionMaker.calculateTotalDistance(currentPath, true);
            updateStatus(String.format("Distancia: %.1f unidades (ponderada: %.1f).",
                    distance, weightedDistance));

            // Mostrar todas las instrucciones en el registro
            for (NavigationInstruction instruction : currentInstructions) {
                updateStatus(instruction.toString());
            }

            // Mostrar primera instrucción en el panel de instrucciones
            if (!currentInstructions.isEmpty()) {
                showInstruction(currentInstructions.get(0).getMessage());
            }
        }

        // Actualizar el mapa en la vista
        if (view != null) {
            view.refreshMap();
        }

        return currentPath;
    }

    /**
     * Avanza a la siguiente instrucción de navegación.
     * Se puede invocar periódicamente o por acción del usuario.
     */
    public void nextInstruction() {
        if (currentInstructions.isEmpty()) {
            return;
        }

        currentInstructionIndex++;
        if (currentInstructionIndex < currentInstructions.size()) {
            NavigationInstruction instruction = currentInstructions.get(currentInstructionIndex);
            showInstruction(instruction.getMessage());

            // Si hay motor de voz disponible, pronunciar la instrucción
            if (voiceEngine != null && !voiceEngine.isSpeaking()) {
                voiceEngine.speak(instruction.getMessage());
            }
        } else {
            showInstruction("Ha llegado a su destino.");
            currentInstructionIndex = currentInstructions.size() - 1;
        }
    }

    /**
     * Cambia la estrategia de navegación entre BFS y Dijkstra.
     * 
     * POLIMORFISMO: Intercambia estrategias en tiempo de ejecución.
     * 
     * @param useDijkstra true para Dijkstra, false para BFS
     */
    public void switchStrategy(boolean useDijkstra) {
        if (useDijkstra) {
            this.strategy = new DijkstraStrategy();
        } else {
            this.strategy = new BFSStrategy();
        }
        updateStatus("Estrategia cambiada a: " + strategy.getAlgorithmName());
    }

    // ==================== Estado del sistema ====================

    public boolean isNavigationActive() {
        return navigationActive;
    }

    public List<Node> getCurrentPath() {
        return currentPath;
    }

    public List<NavigationInstruction> getCurrentInstructions() {
        return currentInstructions;
    }

    public NavigationMap getMap() {
        return map;
    }

    public NavigationStrategy getStrategy() {
        return strategy;
    }

    public NavigationDecisionMaker getDecisionMaker() {
        return decisionMaker;
    }

    public Node getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Node position) {
        this.currentPosition = position;
    }

    // ==================== Comunicación con la vista ====================

    /**
     * Envía un mensaje de estado a la vista.
     * 
     * @param message mensaje a mostrar
     */
    private void updateStatus(String message) {
        if (view != null) {
            view.updateStatus(message);
        }
    }

    /**
     * Muestra una instrucción de navegación en la vista.
     * 
     * @param instruction instrucción a mostrar
     */
    private void showInstruction(String instruction) {
        if (view != null) {
            view.showInstruction(instruction);
        }
    }
}


