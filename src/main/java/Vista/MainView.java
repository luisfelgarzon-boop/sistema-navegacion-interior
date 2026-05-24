/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Controlador.NavigationController;
import Util.Constants;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
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

    private JButton btnStart;
    private JButton btnStop;
    private JTextArea txtStatus;
    private JTextArea txtInstructions;
    private JPanel mapPanel;
    private JLabel lblSystemState;
    private JLabel cameraLabel;
    private VideoCapture camera;

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
    }

    private void initializeWindow() {
        setTitle(Constants.WINDOW_TITLE);
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
    }

    private void initializeComponents() {
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

        txtStatus = new JTextArea(8, 40);
        txtStatus.setEditable(false);
        txtStatus.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtStatus.setBackground(new Color(33, 33, 33));
        txtStatus.setForeground(new Color(0, 230, 118));
        txtStatus.setCaretColor(new Color(0, 230, 118));
        txtStatus.setText("Sistema listo. Presione 'Iniciar Navegación' para comenzar.\n");

        txtInstructions = new JTextArea(5, 40);
        txtInstructions.setEditable(false);
        txtInstructions.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtInstructions.setBackground(new Color(38, 50, 56));
        txtInstructions.setForeground(Color.WHITE);
        txtInstructions.setText("Instrucciones de navegación aparecerán aquí.\n");
        txtInstructions.setLineWrap(true);
        txtInstructions.setWrapStyleWord(true);

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
                "Cámara / Mapa del Entorno", 0, 0,
                new Font("SansSerif", Font.BOLD, 12), Color.LIGHT_GRAY));

        lblSystemState = new JLabel("Estado: Inactivo");
        lblSystemState.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblSystemState.setForeground(new Color(255, 193, 7));
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(38, 38, 38));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        topPanel.setBackground(new Color(50, 50, 50));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        topPanel.add(btnStart);
        topPanel.add(btnStop);
        topPanel.add(Box.createHorizontalStrut(30));
        topPanel.add(lblSystemState);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(38, 38, 38));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        centerPanel.add(mapPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        bottomPanel.setBackground(new Color(38, 38, 38));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

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

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
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
    }

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

    private void startCamera() {
        try {
            System.out.println("1. Entrando a startCamera");
            System.load("C:\\Users\\FelipeNuevo\\Downloads\\opencv\\build\\java\\x64\\opencv_java4120.dll");
            System.out.println("2. DLL cargada");

            camera = new VideoCapture(0);
            System.out.println("3. Cámara creada");

            if (!camera.isOpened()) {
                System.out.println("4. No se pudo abrir la cámara");
                return;
            }

            System.out.println("5. Cámara abierta correctamente");

            Timer timer = new Timer(30, e -> {
                Mat frame = new Mat();
                if (camera.read(frame)) {
                    Core.flip(frame, frame, 1);
                    BufferedImage img = matToBufferedImage(frame);
                    Image scaled = img.getScaledInstance(
                        mapPanel.getWidth(), mapPanel.getHeight(), Image.SCALE_SMOOTH);
                    cameraLabel.setIcon(new ImageIcon(scaled));
                }
            });
            timer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        int type = mat.channels() > 1
            ? BufferedImage.TYPE_3BYTE_BGR
            : BufferedImage.TYPE_BYTE_GRAY;

        int bufferSize = mat.channels() * mat.cols() * mat.rows();
        byte[] buffer = new byte[bufferSize];
        mat.get(0, 0, buffer);

        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        byte[] targetPixels = ((java.awt.image.DataBufferByte)
            image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
    }

    private void drawMap(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = mapPanel.getWidth();
        int h = mapPanel.getHeight();
        g2d.setColor(new Color(60, 60, 60));
        int gridSize = 30;
        for (int x = 0; x < w; x += gridSize) g2d.drawLine(x, 0, x, h);
        for (int y = 0; y < h; y += gridSize) g2d.drawLine(0, y, w, y);
    }
}