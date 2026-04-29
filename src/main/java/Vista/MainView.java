/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;
import Controlador.NavigationController;
import Util.Constants;
import javax.swing.*;
import java.awt.*;
/**
 * Vista principal del sistema de navegación (interfaz gráfica Swing).
 * Siguiendo el patrón MVC, esta clase SOLO se encarga de:
 * - Mostrar información al usuario
 * - Capturar eventos del usuario
 * - Delegar las acciones al Controlador
 * 
 * NO contiene lógica de negocio ni acceso directo al modelo.
 * @author felip
 */
public class MainView extends JFrame {

    private final NavigationController controller;

    // ==================== Componentes de la interfaz ====================
    private JButton btnStart;
    private JButton btnStop;
    private JTextArea txtStatus;
    private JTextArea txtInstructions;
    private JPanel mapPanel;
    private JLabel lblSystemState;

    /**
     * Constructor de la vista principal.
     * 
     * @param controller controlador al que delegar las acciones
     */
    public MainView(NavigationController controller) {
        this.controller = controller;
        initializeWindow();
        initializeComponents();
        layoutComponents();
        bindEvents();
    }

    // ==================== Inicialización ====================

    /**
     * Configura las propiedades de la ventana principal.
     */
    private void initializeWindow() {
        setTitle(Constants.WINDOW_TITLE);
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla
        setMinimumSize(new Dimension(800, 600));
    }

    /**
     * Crea todos los componentes de la interfaz.
     */
    private void initializeComponents() {
        // Botones de control
        btnStart = new JButton("▶ Iniciar Navegación");
        btnStart.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnStart.setBackground(new Color(46, 125, 50));
        btnStart.setForeground(Color.WHITE);
        btnStart.setFocusPainted(false);

        btnStop = new JButton("⏹ Detener Navegación");
        btnStop.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnStop.setBackground(new Color(198, 40, 40));
        btnStop.setForeground(Color.WHITE);
        btnStop.setFocusPainted(false);
        btnStop.setEnabled(false);

        // Área de estado del sistema
        txtStatus = new JTextArea(8, 40);
        txtStatus.setEditable(false);
        txtStatus.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtStatus.setBackground(new Color(33, 33, 33));
        txtStatus.setForeground(new Color(0, 230, 118));
        txtStatus.setCaretColor(new Color(0, 230, 118));
        txtStatus.setText("Sistema listo. Presione 'Iniciar Navegación' para comenzar.\n");

        // Área de instrucciones de voz
        txtInstructions = new JTextArea(5, 40);
        txtInstructions.setEditable(false);
        txtInstructions.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtInstructions.setBackground(new Color(38, 50, 56));
        txtInstructions.setForeground(Color.WHITE);
        txtInstructions.setText("Instrucciones de navegación aparecerán aquí.\n");
        txtInstructions.setLineWrap(true);
        txtInstructions.setWrapStyleWord(true);

        // Panel del mapa (se dibujará en Fase 4)
        mapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMap(g);
            }
        };
        mapPanel.setBackground(new Color(48, 48, 48));
        mapPanel.setPreferredSize(new Dimension(500, 400));
        mapPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                "Mapa del Entorno",
                0, 0,
                new Font("SansSerif", Font.BOLD, 12),
                Color.LIGHT_GRAY));

        // Estado del sistema
        lblSystemState = new JLabel("Estado: Inactivo");
        lblSystemState.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblSystemState.setForeground(new Color(255, 193, 7));
    }

    /**
     * Organiza los componentes en el layout de la ventana.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(38, 38, 38));

        // Panel superior: controles y estado
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        topPanel.setBackground(new Color(50, 50, 50));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        topPanel.add(btnStart);
        topPanel.add(btnStop);
        topPanel.add(Box.createHorizontalStrut(30));
        topPanel.add(lblSystemState);

        // Panel central: mapa
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(38, 38, 38));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        centerPanel.add(mapPanel, BorderLayout.CENTER);

        // Panel inferior: estado + instrucciones
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        bottomPanel.setBackground(new Color(38, 38, 38));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JScrollPane statusScroll = new JScrollPane(txtStatus);
        statusScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                "Registro del Sistema",
                0, 0,
                new Font("SansSerif", Font.BOLD, 12),
                Color.LIGHT_GRAY));

        JScrollPane instructionsScroll = new JScrollPane(txtInstructions);
        instructionsScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                "Instrucciones de Voz",
                0, 0,
                new Font("SansSerif", Font.BOLD, 12),
                Color.LIGHT_GRAY));

        bottomPanel.add(statusScroll);
        bottomPanel.add(instructionsScroll);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Vincula los eventos de la interfaz con las acciones del controlador.
     * La vista NO ejecuta lógica; solo delega al controlador.
     */
    private void bindEvents() {
        btnStart.addActionListener(e -> {
            controller.startNavigation();
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
            lblSystemState.setText("Estado: Navegando");
            lblSystemState.setForeground(new Color(76, 175, 80));
        });

        btnStop.addActionListener(e -> {
            controller.stopNavigation();
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
            lblSystemState.setText("Estado: Inactivo");
            lblSystemState.setForeground(new Color(255, 193, 7));
        });
    }

    // ==================== Métodos públicos para el controlador
    // ====================

    /**
     * Actualiza el registro de estado del sistema.
     * Invocado por el controlador.
     * 
     * @param message mensaje a agregar al registro
     */
    public void updateStatus(String message) {
        SwingUtilities.invokeLater(() -> {
            txtStatus.append(message + "\n");
            txtStatus.setCaretPosition(txtStatus.getDocument().getLength());
        });
    }

    /**
     * Muestra una instrucción de navegación en el panel de instrucciones.
     * 
     * @param instruction instrucción a mostrar
     */
    public void showInstruction(String instruction) {
        SwingUtilities.invokeLater(() -> {
            txtInstructions.setText(instruction + "\n");
        });
    }

    /**
     * Solicita el repintado del panel del mapa.
     */
    public void refreshMap() {
        SwingUtilities.invokeLater(() -> mapPanel.repaint());
    }

    // ==================== Dibujo del mapa ====================

    /**
     * Dibuja el mapa del entorno en el panel.
     * Implementación básica en Fase 1; se enriquecerá en Fase 4.
     * 
     * @param g contexto gráfico
     */
    private void drawMap(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w = mapPanel.getWidth();
        int h = mapPanel.getHeight();

        // Mensaje placeholder
        g2d.setColor(new Color(150, 150, 150));
        g2d.setFont(new Font("SansSerif", Font.ITALIC, 16));
        String msg = "Mapa del entorno (se renderizará en Fase 4)";
        FontMetrics fm = g2d.getFontMetrics();
        int textX = (w - fm.stringWidth(msg)) / 2;
        int textY = h / 2;
        g2d.drawString(msg, textX, textY);

        // Dibujar cuadrícula básica
        g2d.setColor(new Color(60, 60, 60));
        int gridSize = 30;
        for (int x = 0; x < w; x += gridSize) {
            g2d.drawLine(x, 0, x, h);
        }
        for (int y = 0; y < h; y += gridSize) {
            g2d.drawLine(0, y, w, y);
        }
    }
}