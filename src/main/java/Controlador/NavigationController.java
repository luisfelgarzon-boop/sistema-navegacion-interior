/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Algoritmo.BFSStrategy;
import Modelo.Algoritmo.DijkstraStrategy;
import Modelo.Algoritmo.NavigationStrategy;
import Modelo.Edificio;
import Modelo.MapBuilder;
import Modelo.NodoDB;
import Modelo.NavigationDecisionMaker;
import Modelo.NavigationDecisionMaker.NavigationInstruction;
import Modelo.NavigationMap;
import Modelo.Node;
import Modelo.RutaHistorial;
import Modelo.modelo.db.DatabaseConnection;
import Modelo.modelo.db.EdificioDAO;
import Modelo.modelo.db.NodoDAO;
import Modelo.modelo.db.RutaHistorialDAO;
import Util.JsonManager;
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
    private int currentEdificioId = 1;

    private final EdificioDAO edificioDAO;
    private final NodoDAO nodoDAO;
    private final RutaHistorialDAO rutaHistorialDAO;

    public NavigationController() {
        this.decisionMaker       = new NavigationDecisionMaker();
        this.navigationActive    = false;
        this.currentPath         = List.of();
        this.currentInstructions = List.of();
        this.currentInstructionIndex = 0;
        this.map      = MapBuilder.buildSampleFloor();
        this.strategy = new DijkstraStrategy();

        this.edificioDAO      = new EdificioDAO();
        this.nodoDAO          = new NodoDAO();
        this.rutaHistorialDAO = new RutaHistorialDAO();

        JsonManager.inicializar();
        inicializarEdificioPorDefecto();
    }

    // ==================== BD: Inicialización ====================

    private void inicializarEdificioPorDefecto() {
        try {
            Edificio existente = edificioDAO.obtenerPorNombre("Edificio Principal");
            if (existente == null) {
                Edificio nuevo = new Edificio(
                    "Edificio Principal",
                    "Edificio cargado por defecto del sistema",
                    "Universitario", 3
                );
                edificioDAO.insertar(nuevo);
                existente = edificioDAO.obtenerPorNombre("Edificio Principal");
                if (existente != null) JsonManager.guardarEdificio(existente);
            }
            if (existente != null) this.currentEdificioId = existente.getId();
        } catch (Exception e) {
            System.err.println("[BD] No se pudo conectar: " + e.getMessage());
        }
    }

    // ==================== CRUD Edificios ====================

    public List<Edificio> getEdificios() {
        return edificioDAO.obtenerTodos();
    }

    public Edificio getEdificio(int id) {
        return edificioDAO.obtenerPorId(id);
    }

    public Edificio getEdificio(String nombre) {
        return edificioDAO.obtenerPorNombre(nombre);
    }

    public boolean agregarEdificio(Edificio e) {
        boolean ok = edificioDAO.insertar(e);
        if (ok) {
            Edificio guardado = edificioDAO.obtenerPorNombre(e.getNombre());
            if (guardado != null) JsonManager.guardarEdificio(guardado);
            updateStatus("Edificio registrado: " + e.getNombre());
        }
        return ok;
    }

    public boolean actualizarEdificio(Edificio e) {
        boolean ok = edificioDAO.actualizar(e);
        if (ok) {
            JsonManager.actualizarEdificio(e);
            updateStatus("Edificio actualizado: " + e.getNombre());
        }
        return ok;
    }

    public boolean eliminarEdificio(int id) {
        boolean ok = edificioDAO.eliminar(id);
        if (ok) {
            JsonManager.eliminarEdificio(id);
            updateStatus("Edificio eliminado ID: " + id);
        }
        return ok;
    }

    // ==================== Historial ====================

    public List<RutaHistorial> getHistorial() {
        return rutaHistorialDAO.obtenerTodos();
    }

    public List<RutaHistorial> getHistorial(int edificioId) {
        return rutaHistorialDAO.obtenerPorEdificio(edificioId);
    }

    // ==================== Nodos ====================

    public List<NodoDB> getNodos(int edificioId) {
        return nodoDAO.obtenerPorEdificio(edificioId);
    }

    public boolean guardarNodo(NodoDB nodo) {
        return nodoDAO.insertar(nodo);
    }

    // ==================== Navegación ====================

    public void startNavigation() {
        if (map == null) {
            updateStatus("Error: No hay mapa cargado.");
            return;
        }
        navigationActive = true;
        updateStatus("Navegación iniciada con " + strategy.getAlgorithmName() + ".");
        updateStatus("Mapa cargado: " + map.toString());
    }

    public void stopNavigation() {
        navigationActive = false;
        currentPath = List.of();
        currentInstructions = List.of();
        currentInstructionIndex = 0;
        updateStatus("Navegación detenida.");
        showInstruction("Sistema en espera.");
        DatabaseConnection.closeConnection();
    }

    public List<Node> calculateRoute(String startId, String targetId) {
        if (map == null || strategy == null) return List.of();

        Node start  = map.getNode(startId);
        Node target = map.getNode(targetId);

        if (start == null || target == null) {
            updateStatus("Error: Nodos no encontrados.");
            return List.of();
        }

        currentPath = strategy.findPath(map, start, target);

        if (currentPath.isEmpty()) {
            updateStatus("No se encontró ruta.");
            showInstruction("No hay ruta disponible.");
        } else {
            updateStatus(String.format("Ruta encontrada: %d pasos con %s.",
                    currentPath.size(), strategy.getAlgorithmName()));

            currentInstructions = decisionMaker.generateInstructions(currentPath);
            currentInstructionIndex = 0;

            double dist  = decisionMaker.calculateTotalDistance(currentPath);
            double wDist = decisionMaker.calculateTotalDistance(currentPath, true);
            updateStatus(String.format("Distancia: %.1f (ponderada: %.1f).", dist, wDist));

            for (NavigationInstruction inst : currentInstructions) {
                updateStatus(inst.toString());
            }

            if (!currentInstructions.isEmpty()) {
                showInstruction(currentInstructions.get(0).getMessage());
            }

            // Guardar en BD y JSON
            try {
                RutaHistorial ruta = new RutaHistorial(
                    currentEdificioId, startId, targetId,
                    strategy.getAlgorithmName()
                );
                rutaHistorialDAO.insertar(ruta);
                JsonManager.guardarRuta(ruta);
                updateStatus("Ruta guardada en BD y JSON.");
            } catch (Exception e) {
                System.err.println("[BD] Error al guardar ruta: " + e.getMessage());
            }
        }

        if (view != null) view.refreshMap();
        return currentPath;
    }

    public void nextInstruction() {
        if (currentInstructions.isEmpty()) return;
        currentInstructionIndex++;
        if (currentInstructionIndex < currentInstructions.size()) {
            NavigationInstruction inst = currentInstructions.get(currentInstructionIndex);
            showInstruction(inst.getMessage());
            if (voiceEngine != null && !voiceEngine.isSpeaking()) {
                voiceEngine.speak(inst.getMessage());
            }
        } else {
            showInstruction("Ha llegado a su destino.");
            currentInstructionIndex = currentInstructions.size() - 1;
        }
    }

    public void switchStrategy(boolean useDijkstra) {
        this.strategy = useDijkstra ? new DijkstraStrategy() : new BFSStrategy();
        updateStatus("Estrategia: " + strategy.getAlgorithmName());
    }

    // ==================== Inyección ====================

    public void setView(MainView view)                  { this.view = view; }
    public void setMap(NavigationMap map)               { this.map = map; }
    public void setStrategy(NavigationStrategy s)       { this.strategy = s; }
    public void setVisionProcessor(VisionProcessor vp)  { this.visionProcessor = vp; }
    public void setVoiceEngine(VoiceEngine ve)          { this.voiceEngine = ve; }
    public void setCurrentEdificioId(int id)            { this.currentEdificioId = id; }

    // ==================== Getters ====================

    public boolean isNavigationActive()                        { return navigationActive; }
    public List<Node> getCurrentPath()                         { return currentPath; }
    public List<NavigationInstruction> getCurrentInstructions(){ return currentInstructions; }
    public NavigationMap getMap()                              { return map; }
    public NavigationStrategy getStrategy()                    { return strategy; }
    public NavigationDecisionMaker getDecisionMaker()          { return decisionMaker; }
    public Node getCurrentPosition()                           { return currentPosition; }
    public void setCurrentPosition(Node p)                     { this.currentPosition = p; }
    public int getCurrentEdificioId()                          { return currentEdificioId; }

    // ==================== Vista ====================

    private void updateStatus(String message) {
        if (view != null) view.updateStatus(message);
        else System.out.println("[Status] " + message);
    }

    private void showInstruction(String instruction) {
        if (view != null) view.showInstruction(instruction);
        else System.out.println("[Instrucción] " + instruction);
    }
}