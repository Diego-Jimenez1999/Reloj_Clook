/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

import main.java.logic.FileHandler;
import main.java.Controller.UIController;

/**
 * Panel de configuración de colores del reloj.
 *
 * Este panel permite:
 * - Seleccionar colores desde una paleta
 * - Ingresar valores RGB manualmente
 * - Visualizar cambios en tiempo real
 * - Mantener un historial de colores recientes
 *
 * 🔥 Diseño basado en:
 * - Sincronización con UIController (fuente única de verdad)
 * - Vista previa desacoplada
 * - Persistencia mediante FileHandler
 *
 * @author Diego
 */
public class ConfColorPanel extends JPanel {

    // ========================= VARIABLES =========================

    /** Controlador global que gestiona el estado del sistema */
    private final UIController controller;

    /** Panel donde se renderiza el fondo (imagen del reloj) */
    private ClockPanel previewPanel;

    /** Componente que muestra el reloj en tiempo real */
    private TimeOverlayPanel previewClock;

    /** Contenedor del historial de colores recientes */
    private JPanel recentColorsPanel;

    /** Contenedor de la paleta de colores */
    private JPanel palettePanel;

    /** Campos de entrada RGB */
    private JTextField txtR, txtG, txtB;

    /** Lista de colores recientes (sin duplicados) */
    private final List<Color> recentColors = new ArrayList<>();

    /** Cantidad máxima de colores recientes */
    private static final int MAX_RECENT = 10;

    /**
     * Bandera de control:
     * Evita que se ejecuten eventos durante la inicialización
     */
    private boolean isInitializing = true;


    // ========================= CONSTRUCTOR =========================

    /**
     * Constructor principal del panel.
     *
     * @param controller Controlador global de la aplicación
     */
    public ConfColorPanel(UIController controller) {

        // 🔹 IF: validación obligatoria para evitar errores en tiempo de ejecución
        if (controller == null) {
            throw new NullPointerException("Controller no puede ser null.");
        }

        this.controller = controller;

        initComponents();
        loadRecentColors();

        // 🔥 Sincroniza TODO el estado visual al iniciar
        refreshPreview();

        // 🔹 Desactiva modo inicialización
        isInitializing = false;
    }


    // ========================= UI =========================

    /**
     * Inicializa todos los componentes visuales del panel.
     */
    private void initComponents() {

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(45, 45, 45));

        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        // ===== HISTORIAL =====
        createRecentColorsPanel();

        JLabel lblHist = new JLabel("HISTORIAL RECIENTE");
        lblHist.setForeground(Color.WHITE);

        leftPanel.add(lblHist);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(recentColorsPanel);
        leftPanel.add(Box.createVerticalStrut(20));

        // ===== PALETA =====
        createPalettePanel();

        JLabel lblPal = new JLabel("COLORES RECOMENDADOS");
        lblPal.setForeground(Color.WHITE);

        leftPanel.add(lblPal);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(palettePanel);
        leftPanel.add(Box.createVerticalStrut(25));

        // ===== RGB =====
        createRGBInputPanel(leftPanel);

        add(leftPanel, BorderLayout.WEST);

        // ===== PREVIEW =====
        createPreviewPanel();
        add(previewPanel, BorderLayout.CENTER);
    }


    // ========================= PREVIEW =========================

    /**
     * Sincroniza completamente el estado visual del panel.
     *
     * Aplica:
     * - Imagen
     * - Color
     * - Fuente
     * - Posición
     */
    public void refreshPreview() {

        // 🔹 IF: evita errores si controller no está disponible
        if (controller == null) return;

        Color color = controller.getCurrentColor();
        Font font = controller.getCurrentFont();
        String position = controller.getCurrentPosition();
        String path = FileHandler.getConfig("background_path");

        // ===== IMAGEN =====
        if (path != null) {
            previewPanel.setIcon(new ImageIcon(path));
        }

        // ===== COLOR =====
        if (color != null) {
            previewClock.updateClockColor(color);

            // Sincroniza inputs RGB
            txtR.setText(String.valueOf(color.getRed()));
            txtG.setText(String.valueOf(color.getGreen()));
            txtB.setText(String.valueOf(color.getBlue()));
        }

        // ===== FUENTE =====
        if (font != null) {
            previewClock.updateClockFont(font);
        }

        // ===== POSICIÓN =====
        if (position != null) {
            previewClock.updateClockPosition(position);
        }

        centerClock();

        previewClock.revalidate();
        previewClock.repaint();
    }


    /**
     * Crea el panel de preview con el reloj.
     */
    private void createPreviewPanel() {

        previewPanel = new ClockPanel();
        previewPanel.setLayout(null);

        previewClock = new TimeOverlayPanel();
        previewClock.setBounds(0, 0, 400, 150);

        previewPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                centerClock();
            }
        });

        previewPanel.add(previewClock);
    }


    /**
     * Centra el reloj dentro del panel preview.
     */
    private void centerClock() {

        int x = (previewPanel.getWidth() - previewClock.getWidth()) / 2;
        int y = (previewPanel.getHeight() - previewClock.getHeight()) / 2;

        previewClock.setLocation(x, y);
    }


    // ========================= COMPONENTES =========================

    /**
     * Crea el panel de colores recientes.
     *
     * 🔥 Grid dinámico:
     * Se adapta automáticamente al número de elementos.
     */
    private void createRecentColorsPanel() {

        recentColorsPanel = new JPanel(new GridLayout(0, 5, 8, 8));
        recentColorsPanel.setOpaque(false);

        // Tamaño visual controlado
        recentColorsPanel.setPreferredSize(new Dimension(250, 100));
    }


    /**
     * Crea la paleta de colores recomendados.
     */
    private void createPalettePanel() {

        palettePanel = new JPanel(new GridLayout(5, 5, 8, 8));
        palettePanel.setOpaque(false);

        List<Color> palette = FileHandler.loadPalette();

        // 🔹 FOR: recorre cada color de la paleta
        for (Color color : palette) {

            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(40, 40));
            btn.setBackground(color);
            btn.setOpaque(true);

            btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            btn.addActionListener(e -> applyColor(color));

            palettePanel.add(btn);
        }
    }


    /**
     * Crea el panel de entrada RGB.
     *
     * @param parent contenedor padre
     */
    private void createRGBInputPanel(JPanel parent) {

        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnl.setOpaque(false);

        txtR = createStyledField("255");
        txtG = createStyledField("255");
        txtB = createStyledField("255");

        txtR.setPreferredSize(new Dimension(60, 30));
        txtG.setPreferredSize(new Dimension(60, 30));
        txtB.setPreferredSize(new Dimension(60, 30));

        JButton btn = createIconButton("/resources/icons/new_img.png");
        btn.addActionListener(e -> applyManualRGB());

        pnl.add(new JLabel("R:")); pnl.add(txtR);
        pnl.add(new JLabel("G:")); pnl.add(txtG);
        pnl.add(new JLabel("B:")); pnl.add(txtB);
        pnl.add(btn);

        parent.add(Box.createVerticalStrut(10));
        parent.add(pnl);
    }


    private JTextField createStyledField(String value) {

        JTextField txt = new JTextField(value);

        txt.setForeground(Color.WHITE);
        txt.setBackground(new Color(45, 45, 45));
        txt.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));

        return txt;
    }


    private JButton createIconButton(String path) {

        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(50, 50));

        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            btn.setIcon(new ImageIcon(
                    icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)
            ));
        } catch (Exception e) {
            System.err.println("Icono no encontrado");
        }

        return btn;
    }


    // ========================= LÓGICA =========================

    /**
     * Aplica un color seleccionado.
     *
     * @param color color a aplicar
     */
    private void applyColor(Color color) {

        // 🔹 IF: evita errores si el color es null
        if (color == null) return;

        // Aplicar al sistema global
        controller.applyColor(color);

        // Actualizar preview inmediatamente
        previewClock.updateClockColor(color);
        previewClock.repaint();

        // 🔹 IF: evita guardar durante inicialización
        if (!isInitializing) {
            saveRecentColor(color);
        }
    }


    /**
     * Aplica color desde valores RGB ingresados manualmente.
     */
    private void applyManualRGB() {

        try {
            int r = Integer.parseInt(txtR.getText());
            int g = Integer.parseInt(txtG.getText());
            int b = Integer.parseInt(txtB.getText());

            applyColor(new Color(r, g, b));

        } catch (Exception e) {
            // 🔹 TRY-CATCH: controla errores de entrada inválida
            JOptionPane.showMessageDialog(this, "RGB inválido");
        }
    }


    /**
     * Guarda un color en el historial.
     */
    private void saveRecentColor(Color color) {

        // 🔹 IF: elimina duplicados
        if (recentColors.contains(color)) {
            recentColors.remove(color);
        }

        recentColors.add(0, color);

        // 🔹 IF: controla tamaño máximo
        if (recentColors.size() > MAX_RECENT) {
            recentColors.remove(recentColors.size() - 1);
        }

        refreshRecentColorsUI();

        FileHandler.saveRecentColors(recentColors);
    }


    /**
     * Actualiza visualmente el historial.
     */
    private void refreshRecentColorsUI() {

        recentColorsPanel.removeAll();

        // 🔹 FOR: llena el panel dinámicamente
        for (int i = 0; i < MAX_RECENT; i++) {

            if (i < recentColors.size()) {

                Color c = recentColors.get(i);

                JButton btn = new JButton();
                btn.setBackground(c);
                btn.setOpaque(true);

                btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                btn.addActionListener(e -> applyColor(c));

                recentColorsPanel.add(btn);

            } else {
                // Espacio vacío para mantener estructura
                recentColorsPanel.add(new JPanel());
            }
        }

        recentColorsPanel.revalidate();
        recentColorsPanel.repaint();
    }


    /**
     * Carga colores recientes desde persistencia.
     */
    private void loadRecentColors() {

        List<Color> saved = FileHandler.loadRecentColors();

        recentColors.clear();

        // 🔹 FOR: evita duplicados
        for (Color c : saved) {
            if (!recentColors.contains(c)) {
                recentColors.add(c);
            }
        }

        refreshRecentColorsUI();
    }


    /**
     * Actualiza la imagen del preview.
     */
    public void updatePreviewBackground() {

        String path = FileHandler.getConfig("background_path");

        if (path != null) {
            previewPanel.setIcon(new ImageIcon(path));
        }
    }
}