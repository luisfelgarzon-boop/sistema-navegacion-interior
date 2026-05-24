/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Controlador.NavigationController;
import Modelo.Edificio;
import Modelo.RutaHistorial;
import Util.Constants;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import java.awt.image.BufferedImage;
import java.util.List;
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

    // ==================== Componentes ====================
    private JButton btnStart;
    private JButton btnStop;
    private JButton btnVerHistorial;
    private JButton btnVerEdificios;
    private JTextArea txtStatus;
    private JTextArea txtInstructions;
    private JPanel mapPanel;
    private JLabel lblSystemState;
    private JLabel cameraLabel;
    private VideoCapture camera;
    private JTable tblHistorial;
    private JTable tblEdificios;
    private DefaultTableModel modelHistorial;
    private DefaultTableModel modelEdificios;

    public MainView(NavigationController controller) {
        this.controller = controller;
        initializeWindow();
        initializeComponents();
        layoutComponents();
        bindEvents();
       cameraLabel = new JLabel();

       cameraLabel.setPreferredSize(new Dimension(1000, 500));

       mapPanel.add(cameraLabel);
       try {
    startCamera();
} catch (Exception e) {
    e.printStackTrace();
}
        
        setVisible(true);
    }

    // ==================== Inicialización ====================

    private void initializeWindow() {
        setTitle(Constants.WINDOW_TITLE);
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 650));
    }

    private void initializeComponents() {
        // Botones de navegación
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

        // Botones BD
        btnVerHistorial = new JButton("📋 Historial BD");
        btnVerHistorial.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnVerHistorial.setBackground(new Color(21, 101, 192));
        btnVerHistorial.setForeground(Color.WHITE);
        btnVerHistorial.setFocusPainted(false);

        btnVerEdificios = new JButton("🏢 Edificios BD");
        btnVerEdificios.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnVerEdificios.setBackground(new Color(74, 20, 140));
        btnVerEdificios.setForeground(Color.WHITE);
        btnVerEdificios.setFocusPainted(false);

        // Estado del sistema
        txtStatus = new JTextArea(8, 40);
        txtStatus.setEditable(false);
        txtStatus.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtStatus.setBackground(new Color(33, 33, 33));
        txtStatus.setForeground(new Color(0, 230, 118));
        txtStatus.setCaretColor(new Color(0, 230, 118));
        txtStatus.setText("Sistema listo. Presione 'Iniciar Navegación' para comenzar.\n");

        // Instrucciones de voz
        txtInstructions = new JTextArea(5, 40);
        txtInstructions.setEditable(false);
        txtInstructions.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtInstructions.setBackground(new Color(38, 50, 56));
        txtInstructions.setForeground(Color.WHITE);
        txtInstructions.setText("Instrucciones de navegación aparecerán aquí.\n");
        txtInstructions.setLineWrap(true);
        txtInstructions.setWrapStyleWord(true);

        // Panel del mapa
        mapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMap(g);
            }
        };
        mapPanel.setLayout(new BorderLayout());
        mapPanel.setBackground(new Color(48, 48, 48));
        mapPanel.setPreferredSize(new Dimension(500, 400));
        mapPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                "Mapa del Entorno", 0, 0,
                new Font("SansSerif", Font.BOLD, 12), Color.LIGHT_GRAY));

        // Tabla historial
        modelHistorial = new DefaultTableModel(
            new String[]{"ID", "Edificio", "Inicio", "Destino", "Algoritmo", "Fecha"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblHistorial = new JTable(modelHistorial);
        tblHistorial.setBackground(new Color(33, 33, 33));
        tblHistorial.setForeground(Color.WHITE);
        tblHistorial.setGridColor(new Color(80, 80, 80));
        tblHistorial.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tblHistorial.getTableHeader().setBackground(new Color(21, 101, 192));
        tblHistorial.getTableHeader().setForeground(Color.WHITE);
        tblHistorial.setRowHeight(24);

        // Tabla edificios
        modelEdificios = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Tipo", "Pisos", "Descripción"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblEdificios = new JTable(modelEdificios);
        tblEdificios.setBackground(new Color(33, 33, 33));
        tblEdificios.setForeground(Color.WHITE);
        tblEdificios.setGridColor(new Color(80, 80, 80));
        tblEdificios.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tblEdificios.getTableHeader().setBackground(new Color(74, 20, 140));
        tblEdificios.getTableHeader().setForeground(Color.WHITE);
        tblEdificios.setRowHeight(24);

        // Estado del sistema
        lblSystemState = new JLabel("Estado: Inactivo");
        lblSystemState.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblSystemState.setForeground(new Color(255, 193, 7));
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(38, 38, 38));

        // Panel superior
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        topPanel.setBackground(new Color(50, 50, 50));
        topPanel.add(btnStart);
        topPanel.add(btnStop);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(btnVerEdificios);
        topPanel.add(btnVerHistorial);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(lblSystemState);

        // Pestaña navegación
        JPanel panelNavegacion = new JPanel(new BorderLayout(5, 5));
        panelNavegacion.setBackground(new Color(38, 38, 38));
        panelNavegacion.add(mapPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        bottomPanel.setBackground(new Color(38, 38, 38));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JScrollPane statusScroll = new JScrollPane(txtStatus);
        statusScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                "Registro del Sistema", 0, 0,
                new Font("SansSerif", Font.BOLD, 12), Color.LIGHT_GRAY));

        JScrollPane instructionsScroll = new JScrollPane(txtInstructions);
        instructionsScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                "Instrucciones de Voz", 0, 0,
                new Font("SansSerif", Font.BOLD, 12), Color.LIGHT_GRAY));

        bottomPanel.add(statusScroll);
        bottomPanel.add(instructionsScroll);
        panelNavegacion.add(bottomPanel, BorderLayout.SOUTH);

        // Pestaña historial
        JPanel panelHistorial = new JPanel(new BorderLayout());
        panelHistorial.setBackground(new Color(38, 38, 38));
        panelHistorial.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollHistorial = new JScrollPane(tblHistorial);
        scrollHistorial.getViewport().setBackground(new Color(33, 33, 33));
        scrollHistorial.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(21, 101, 192)),
                "Historial de Rutas — BD", 0, 0,
                new Font("SansSerif", Font.BOLD, 13), new Color(21, 101, 192)));
        panelHistorial.add(scrollHistorial, BorderLayout.CENTER);

        // Pestaña edificios
        JPanel panelEdificios = new JPanel(new BorderLayout());
        panelEdificios.setBackground(new Color(38, 38, 38));
        panelEdificios.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollEdificios = new JScrollPane(tblEdificios);
        scrollEdificios.getViewport().setBackground(new Color(33, 33, 33));
        scrollEdificios.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(74, 20, 140)),
                "Edificios Registrados — BD", 0, 0,
                new Font("SansSerif", Font.BOLD, 13), new Color(74, 20, 140)));
        panelEdificios.add(scrollEdificios, BorderLayout.CENTER);

        // JTabbedPane
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(new Color(50, 50, 50));
        tabs.setForeground(Color.WHITE);
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabs.addTab("🗺 Navegación",  panelNavegacion);
        tabs.addTab("📋 Historial BD", panelHistorial);
        tabs.addTab("🏢 Edificios BD", panelEdificios);

        add(topPanel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

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

        btnVerHistorial.addActionListener(e -> cargarHistorial());
        btnVerEdificios.addActionListener(e -> cargarEdificios());
    }

    // ==================== Métodos BD ====================

    private void cargarHistorial() {
        modelHistorial.setRowCount(0);
        List<RutaHistorial> lista = controller.getHistorial();
        if (lista.isEmpty()) {
            updateStatus("No hay rutas en el historial.");
        } else {
            for (RutaHistorial r : lista) {
                modelHistorial.addRow(new Object[]{
                    r.getId(), r.getEdificioId(),
                    r.getNodoInicio(), r.getNodoDestino(),
                    r.getAlgoritmo(), r.getFecha()
                });
            }
            updateStatus("Historial cargado: " + lista.size() + " rutas.");
        }
    }

    private void cargarEdificios() {
        modelEdificios.setRowCount(0);
        List<Edificio> lista = controller.getEdificios();
        if (lista.isEmpty()) {
            updateStatus("No hay edificios registrados.");
        } else {
            for (Edificio ed : lista) {
                modelEdificios.addRow(new Object[]{
                    ed.getId(), ed.getNombre(),
                    ed.getTipo(), ed.getPisos(), ed.getDescripcion()
                });
            }
            updateStatus("Edificios cargados: " + lista.size() + " registros.");
        }
    }

    // ==================== Métodos públicos para el controlador ====================

    public void updateStatus(String message) {
        SwingUtilities.invokeLater(() -> {
            txtStatus.append(message + "\n");
            txtStatus.setCaretPosition(txtStatus.getDocument().getLength());
        });
    }

    public void showInstruction(String instruction) {
        SwingUtilities.invokeLater(() -> txtInstructions.setText(instruction + "\n"));
    }

    public void refreshMap() {
        SwingUtilities.invokeLater(() -> mapPanel.repaint());
    }

    // ==================== Dibujo del mapa ====================

    private void drawMap(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = mapPanel.getWidth();
        int h = mapPanel.getHeight();

        // Cuadrícula
        g2d.setColor(new Color(60, 60, 60));
        int gridSize = 30;
        for (int x = 0; x < w; x += gridSize) g2d.drawLine(x, 0, x, h);
        for (int y = 0; y < h; y += gridSize) g2d.drawLine(0, y, w, y);

        // Placeholder
        g2d.setColor(new Color(150, 150, 150));
        g2d.setFont(new Font("SansSerif", Font.ITALIC, 16));
        String msg = "Mapa del entorno (se renderizará en Fase 4)";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(msg, (w - fm.stringWidth(msg)) / 2, h / 2);
    }
   private void startCamera() {

    try {

        System.out.println("1. Entró a startCamera");

        System.load("C:/Users/JuanA/Downloads/opencv/build/java/x64/opencv_java490.dll");

        System.out.println("2. DLL cargada");

        camera = new VideoCapture(0);

        System.out.println("3. Cámara creada");

        if (!camera.isOpened()) {
            System.out.println("4. NO se pudo abrir cámara");
            return;
        }

        System.out.println("5. Cámara abierta");

        Timer timer = new Timer(30, e -> {

            Mat frame = new Mat();

            if (camera.read(frame)) {
                 Core.flip(frame, frame, 1);

                System.out.println("Leyendo frame");

               BufferedImage bufferedImage =
        matToBufferedImage(frame);

Image scaledImage = bufferedImage.getScaledInstance(
        1000,
        500,
        Image.SCALE_SMOOTH
);

cameraLabel.setIcon(new ImageIcon(scaledImage));
            }
        });

        timer.start();

    } catch (Exception e) {

        e.printStackTrace();
    }
}
    private BufferedImage matToBufferedImage(Mat mat) {

    int type = BufferedImage.TYPE_BYTE_GRAY;

    if (mat.channels() > 1) {
        type = BufferedImage.TYPE_3BYTE_BGR;
    }

    int bufferSize = mat.channels() * mat.cols() * mat.rows();

    byte[] buffer = new byte[bufferSize];

    mat.get(0, 0, buffer);

    BufferedImage image =
            new BufferedImage(mat.cols(), mat.rows(), type);

    final byte[] targetPixels =
            ((java.awt.image.DataBufferByte)
                    image.getRaster().getDataBuffer()).getData();

    System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);

    return image;
}
}