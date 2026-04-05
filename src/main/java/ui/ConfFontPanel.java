/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import main.java.Controller.UIController;

/**
 * Panel de configuración de fuente del reloj.
 *
 * RESPONSABILIDADES:
 * - Permitir seleccionar fuente, tamaño y estilo
 * - Permitir seleccionar posición del reloj
 * - Mostrar vista previa en tiempo real
 * - Sincronizar con el controlador global
 *
 * CORRECCIONES APLICADAS:
 * ✔ Vista previa sincronizada (imagen, color, fuente, posición)
 * ✔ NO se resetea la posición al cambiar fuente
 * ✔ Color aplicado correctamente desde el inicio
 * ✔ Layout consistente al cambiar tamaño (evita corte de texto)
 *
 * @author Diego
 * @version 8.0 (Estable + Documentado)
 */
public class ConfFontPanel extends JPanel {

    // ========================= VARIABLES =========================

    /** Controlador global (fuente única de estado) */
    private final UIController controller;

    /** Combo de selección de familia de fuente */
    private JComboBox<String> cbFontFamily;

    /** Check para negrita */
    private JCheckBox chkBold;

    /** Check para cursiva */
    private JCheckBox chkItalic;

    /** Spinner para tamaño de fuente */
    private JSpinner spinSize;

    /** Panel visual de fuentes disponibles */
    private JPanel fontPreviewPanel;

    /** Botones de posición */
    private JRadioButton radCentro, radAbajo, radDerecha, radIzquierda;

    /** Reloj de preview */
    private TimeOverlayPanel previewClock;

    /** Fondo del preview */
    private ClockPanel previewBackground;

    /** Imagen cacheada para evitar recargas */
    private ImageIcon cachedBackground;

    /** Bandera para evitar ejecución de eventos durante inicialización */
    private boolean isInitializing = true;


    // ========================= CONSTRUCTOR =========================
    
    /**
    * Constructor del panel de configuración de letra.
       *
       * @param controller controlador global que gestiona el estado del sistema
       * @throws NullPointerException si el controlador es null
    */
    public ConfFontPanel(UIController controller) {

        // 🔹 IF: Validación crítica para evitar NullPointerException
        if (controller == null) {
            throw new IllegalArgumentException("Controller no puede ser null");
        }

        this.controller = controller;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(45, 45, 45));

        initComponents();       // Construcción UI
        loadInitialState();     // Carga estado inicial
        setupEvents();          // Registro de eventos

        // 🔥 IMPORTANTE: sincronización completa visual
        refreshFromController();

        isInitializing = false;
    }


    // ========================= UI =========================

    /**
     * Construye toda la interfaz gráfica.
     */
  private void initComponents() {

    JPanel leftPanel = new JPanel();
    leftPanel.setOpaque(false);

    /**
     * 🔥 BoxLayout vertical real
     */
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

    /**
     * 🔥 ANCHO del ConffontPanel
     */
    leftPanel.setPreferredSize(new Dimension(400, 0));

    // ===== COMPONENTES =====
    leftPanel.add(createControlsPanel());
    leftPanel.add(Box.createVerticalStrut(15));

    leftPanel.add(createFontPreviewPanel());
    leftPanel.add(Box.createVerticalStrut(15));

    leftPanel.add(createPositionPanel());

    JPanel previewPanel = createPreviewPanel();

    /**
     * 🔥 DISTRIBUCIÓN 
     */
    add(leftPanel, BorderLayout.WEST);
    add(previewPanel, BorderLayout.CENTER);
}


    /**
     * Panel de controles (fuente, tamaño, estilo)
     */
    private JPanel createControlsPanel() {

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        

        cbFontFamily = new JComboBox<>(
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .getAvailableFontFamilyNames()
        );

        spinSize = new JSpinner(new SpinnerNumberModel(48, 12, 200, 1));

        chkBold = new JCheckBox("Negrita");
        chkItalic = new JCheckBox("Cursiva");

        JPanel stylePanel = new JPanel(new GridLayout(2, 1));
        stylePanel.add(chkBold);
        stylePanel.add(chkItalic);

        panel.add(new JLabel("Fuente:"));
        panel.add(cbFontFamily);

        panel.add(new JLabel("Tamaño:"));
        panel.add(spinSize);

        panel.add(new JLabel("Estilo:"));
        panel.add(stylePanel);

        return panel;
    }


    /**
     * Panel visual de fuentes (máx 10)
     */
    private JPanel createFontPreviewPanel() {

        fontPreviewPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        fontPreviewPanel.setBorder(BorderFactory.createTitledBorder("Vista de fuentes"));

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();

        int added = 0;

        // 🔹 FOR: recorre todas las fuentes del sistema
        for (String fontName : fonts) {

            Font testFont = new Font(fontName, Font.PLAIN, 20);

            // 🔹 IF:
            // Valida que la fuente pueda renderizar caracteres básicos
            if (!testFont.canDisplay('A') || !testFont.canDisplay('1')) {
                continue; // se descarta
            }

            JLabel lbl = new JLabel("A12", SwingConstants.CENTER);
            lbl.setFont(testFont);
            lbl.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));

            lbl.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    cbFontFamily.setSelectedItem(fontName);
                    applyChanges();
                }
            });

            fontPreviewPanel.add(lbl);

            added++;

            // 🔹 IF: limita a 10 fuentes
            if (added >= 10) break;
        }

        return fontPreviewPanel;
    }


    /**
     * Panel de selección de posición
     */
    private JPanel createPositionPanel() {

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.setBorder(BorderFactory.createTitledBorder("Posición"));

        radCentro = new JRadioButton("Centro", true);
        radAbajo = new JRadioButton("Abajo");
        radDerecha = new JRadioButton("Derecha");
        radIzquierda = new JRadioButton("Izquierda");

        ButtonGroup group = new ButtonGroup();

        // 🔹 Agrupa botones (solo uno activo)
        group.add(radCentro);
        group.add(radAbajo);
        group.add(radDerecha);
        group.add(radIzquierda);
        
        panel.add(radCentro);
        panel.add(radAbajo);
        panel.add(radDerecha);
        panel.add(radIzquierda);

        

        return panel;
    }


    /**
     * Panel de preview
     */
   private JPanel createPreviewPanel() {

    previewBackground = new ClockPanel();
    previewBackground.setLayout(null);

    previewClock = new TimeOverlayPanel();

    /**
     * 🔥 NO usamos tamaño fijo.
     * Se ajusta dinámicamente en función del panel.
     */
    previewBackground.addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {

            int panelW = previewBackground.getWidth();
            int panelH = previewBackground.getHeight();

            /**
             * 🔹 CÁLCULO RESPONSIVE
             * 70% ancho, 30% alto (similar al panel de color)
             */
            int clockW = (int) (panelW * 0.7);
            int clockH = (int) (panelH * 0.35);

            previewClock.setSize(clockW, clockH);

            // 🔥 CENTRADO REAL
            centerClock();
        }
    });

    previewBackground.add(previewClock);

    loadBackgroundFromController();

    JPanel container = new JPanel(new BorderLayout());
    container.setOpaque(false);

    /**
     * 🔥 TAMAÑO BASE (similar al ConfColorPanel)
     */
    container.setPreferredSize(new Dimension(700, 400));

    container.add(previewBackground);

    return container;
}


    // ========================= LÓGICA =========================

    /**
     * Aplica cambios de fuente y posición
     */
    private void applyChanges() {

        // 🔹 IF: evita ejecución durante inicialización
        if (isInitializing) return;

        Font font = new Font(
                (String) cbFontFamily.getSelectedItem(),
                getStyle(),
                (int) spinSize.getValue()
        );

        String position = getSelectedPosition();

        // ===== PREVIEW =====
        previewClock.updateClockFont(font);
        previewClock.updateClockPosition(position);

        // 🔹 Mantiene color actual
        Color c = controller.getCurrentColor();
        if (c != null) {
            previewClock.updateClockColor(c);
        }

        // ===== SISTEMA =====
        controller.applyFont(font);
        controller.applyPosition(position);

        previewClock.repaint();
    }


    /**
     * Sincroniza TODO el estado desde el controller
     */
    public void refreshFromController() {

        if (controller == null) return;

        String image = controller.getCurrentImagePath();
        Color color = controller.getCurrentColor();
        Font font = controller.getCurrentFont();
        String position = controller.getCurrentPosition();

        // 🔹 IF: aplica imagen si existe
        if (image != null) {
            previewBackground.setIcon(new ImageIcon(image));
        }

        // 🔹 IF: aplica color
        if (color != null) {
            previewClock.updateClockColor(color);
        }

        // 🔹 IF: aplica fuente
        if (font != null) {
            previewClock.updateClockFont(font);
        }

        // 🔹 IF: aplica posición
        if (position != null) {
            previewClock.updateClockPosition(position);
        }

        repaint();
    }


    /**
     * Calcula estilo de fuente
     */
    private int getStyle() {

        int style = Font.PLAIN;

        // 🔹 IF: usa OR binario para combinar estilos
        if (chkBold.isSelected()) style |= Font.BOLD;
        if (chkItalic.isSelected()) style |= Font.ITALIC;

        return style;
    }


    /**
     * Retorna posición seleccionada
     */
    private String getSelectedPosition() {

        if (radCentro.isSelected()) return "CENTER";
        if (radAbajo.isSelected()) return "BOTTOM";
        if (radDerecha.isSelected()) return "RIGHT";
        if (radIzquierda.isSelected()) return "LEFT";

        return "CENTER";
    }


    /**
     * Carga estado inicial
     */
    private void loadInitialState() {

        Font currentFont = controller.getCurrentFont();

        if (currentFont != null) {

            cbFontFamily.setSelectedItem(currentFont.getName());
            spinSize.setValue(currentFont.getSize());

            chkBold.setSelected((currentFont.getStyle() & Font.BOLD) != 0);
            chkItalic.setSelected((currentFont.getStyle() & Font.ITALIC) != 0);

            previewClock.updateClockFont(currentFont);
        }
    }


    /**
     * Carga fondo desde configuración
     */
    private void loadBackgroundFromController() {

        String saved = main.java.logic.FileHandler.getConfig("background_path");

        // 🔹 IF: valida que exista ruta
        if (saved != null) {
            cachedBackground = new ImageIcon(saved);
            previewBackground.setIcon(cachedBackground);
        }
    }


    /**
     * Centra el reloj en el preview
     */
    private void centerClock() {

        int x = (previewBackground.getWidth() - previewClock.getWidth()) / 2;
        int y = (previewBackground.getHeight() - previewClock.getHeight()) / 2;

        previewClock.setLocation(x, y);
    }


    /**
     * Registra eventos UI
     */
    private void setupEvents() {

        ActionListener listener = e -> applyChanges();

        cbFontFamily.addActionListener(listener);
        chkBold.addActionListener(listener);
        chkItalic.addActionListener(listener);

        // 🔹 ChangeListener: detecta cambios continuos en tamaño
        spinSize.addChangeListener(e -> applyChanges());

        radCentro.addActionListener(listener);
        radAbajo.addActionListener(listener);
        radDerecha.addActionListener(listener);
        radIzquierda.addActionListener(listener);
    }


    // ========================= GETTERS =========================
    /**
     * retorna la hora actual 
     * @return retorna panel con la hora dia y mes 
    */
    public TimeOverlayPanel getPreviewClock() {
        return previewClock;
    }
}