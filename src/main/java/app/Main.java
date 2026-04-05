package main.java.app;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

import main.java.Controller.UIController;
import main.java.Controller.MainEventHandler;
import main.java.logic.FileHandler;
import main.java.ui.ClockPanel;
import main.java.ui.ConfImagePanel;
import main.java.ui.ConfColorPanel;
import main.java.ui.ConfFontPanel;
import main.java.ui.TimeOverlayPanel;

/**
 * Clase principal de la aplicación.
 *
 * <p>Responsabilidades:</p>
 * <ul>
 *     <li>Inicializar la interfaz gráfica</li>
 *     <li>Construir vistas</li>
 *     <li>Delegar lógica al controlador</li>
 * </ul>
 *
 * @author Diego
 * @version 3.2 (Corrección completa de Javadoc)
 */
public class Main extends JFrame {

    // ========================= VARIABLES =========================

    /** Contenedor principal que usa CardLayout */
    private JPanel mainContainer;

    /** Controlador central de la aplicación */
    private UIController uiController;

    /** Manejador de eventos de la UI */
    private MainEventHandler eventHandler;

    /** Panel de configuración de imágenes */
    private ConfImagePanel panelImage;

    /** Panel de configuración de color */
    private ConfColorPanel panelColor;

    /** Panel de configuración de fuente */
    private ConfFontPanel panelFont;

    /** Panel overlay donde se dibuja el reloj */
    private TimeOverlayPanel panelClock;

    /** Contenedor interno de configuración */
    private JPanel configContent;

    /** Layout para cambiar entre paneles de configuración */
    private CardLayout configLayout;


    // ========================= CONSTRUCTOR =========================

    /**
     * Constructor principal de la aplicación.
     */
    public Main() {
        initComponents();
    }


    // ========================= MÉTODOS =========================

    /**
     * Inicializa la estructura base del JFrame y configura la navegación.
     */
    private void initComponents() {

        setTitle("Reloj Digital Profesional");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setUndecorated(true);
        setLocationRelativeTo(null);

        CardLayout cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        uiController = new UIController(mainContainer, cardLayout);
        eventHandler = new MainEventHandler(this, uiController);

        mainContainer.add(createClockView(), "RELOJ");
        mainContainer.add(createConfigView(), "CONFIG");

        add(mainContainer, BorderLayout.CENTER);
    }


    /**
     * Construye la vista principal del reloj.
     *
     * @return panel completamente configurado con fondo y controles
     */
    private JPanel createClockView() {

        ClockPanel backgroundPanel = new ClockPanel();
        backgroundPanel.setLayout(null);

        uiController.setClockPanel(backgroundPanel);

        panelClock = new TimeOverlayPanel();
        panelClock.setBounds(150, 150, 600, 300);

        backgroundPanel.add(panelClock);
        uiController.setClockOverlay(panelClock);

        loadInitialConfig(backgroundPanel);

        JButton btnConfig = createCustomButton("/resources/icons/Config1.png");
        addPressEffect(btnConfig);
        btnConfig.setPressedIcon(new ImageIcon(getClass().getResource("/resources/icons/Config2.png")));
        btnConfig.setBounds(750, 10, 40, 40);

        btnConfig.addActionListener(_ -> eventHandler.openConfig());

        JButton btnClose = createCustomButton("/resources/icons/Close1.png");
        addPressEffect(btnClose);
        btnClose.setPressedIcon(new ImageIcon(getClass().getResource("/resources/icons/Close2.png")));
        btnClose.setBounds(810, 10, 40, 40);

        btnClose.addActionListener(_ -> eventHandler.closeApp());

        backgroundPanel.add(btnConfig);
        backgroundPanel.add(btnClose);

        return backgroundPanel;
    }


    /**
     * Carga la configuración persistida del sistema.
     *
     * @param backgroundPanel panel donde se aplica la imagen de fondo
     */
    private void loadInitialConfig(ClockPanel backgroundPanel) {

        String savedColor = FileHandler.getConfig("clock_color");

        if (savedColor != null && savedColor.contains(",")) {
            try {
                String[] rgb = savedColor.split(",");

                Color color = new Color(
                        Integer.parseInt(rgb[0]),
                        Integer.parseInt(rgb[1]),
                        Integer.parseInt(rgb[2])
                );

                uiController.applyColor(color);

            } catch (NumberFormatException e) {
                System.err.println("Error cargando color");
            }
        }

        try {

            String fontName = FileHandler.getConfig("font_name");
            String fontSize = FileHandler.getConfig("font_size");
            String fontStyle = FileHandler.getConfig("font_style");

            if (fontName != null && fontSize != null) {

                Font font = new Font(
                        fontName,
                        fontStyle != null ? Integer.parseInt(fontStyle) : Font.PLAIN,
                        Integer.parseInt(fontSize)
                );

                uiController.applyFont(font);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error cargando fuente");
        }

        String pos = FileHandler.getConfig("clock_position");

        if (pos != null) {
            uiController.applyPosition(pos);
        }

        String savedImage = FileHandler.getConfig("background_path");

        if (savedImage != null && !savedImage.isEmpty()) {

            uiController.updateBackground(savedImage);

        } else {

            URL wallpaperURL = getClass().getResource("/resources/wallpaper/10102.png");

            if (wallpaperURL != null) {
                backgroundPanel.setIcon(new ImageIcon(wallpaperURL));
            }
        }

        panelClock.revalidate();
        panelClock.repaint();
    }


    /**
     * Construye la vista de configuración.
     *
     * @return panel con toolbar y contenido dinámico
     */
    private JPanel createConfigView() {

        JPanel configView = new JPanel(new BorderLayout());

        panelImage = new ConfImagePanel(uiController);
        panelColor = new ConfColorPanel(uiController);
        panelFont = new ConfFontPanel(uiController);

        uiController.setPanelImage(panelImage);
        uiController.setPanelFont(panelFont);
        uiController.setPanelColor(panelColor);

        configLayout = new CardLayout();
        configContent = new JPanel(configLayout);

        configContent.add(panelImage, "IMAGE");
        configContent.add(panelColor, "COLOR");
        configContent.add(panelFont, "FONT");

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));

        JButton btnHome = createCustomButton("/resources/icons/home_icon.png");
        btnHome.setPressedIcon(new ImageIcon(getClass().getResource("/resources/icons/home_icon_1.png")));
        addPressEffect(btnHome);

        JButton btnColor = createCustomButton("/resources/icons/color_icon.png");
        btnColor.setPressedIcon(new ImageIcon(getClass().getResource("/resources/icons/color_icon_1.png")));
        addPressEffect(btnColor);

        JButton btnImage = createCustomButton("/resources/icons/image_icon.png");
        btnImage.setPressedIcon(new ImageIcon(getClass().getResource("/resources/icons/image_icon_1.png")));
        addPressEffect(btnImage);

        JButton btnFont = createCustomButton("/resources/icons/font_icon.png");
        btnFont.setPressedIcon(new ImageIcon(getClass().getResource("/resources/icons/font_icon_1.png")));
        addPressEffect(btnFont);

        btnHome.addActionListener(_ -> eventHandler.goHome());

        btnColor.addActionListener(_ -> {
            configLayout.show(configContent, "COLOR");
            panelColor.updatePreviewBackground();
        });

        btnImage.addActionListener(_ -> configLayout.show(configContent, "IMAGE"));
        btnFont.addActionListener(_ -> configLayout.show(configContent, "FONT"));

        toolbar.add(btnHome);
        toolbar.add(btnColor);
        toolbar.add(btnImage);
        toolbar.add(btnFont);

        configView.add(toolbar, BorderLayout.NORTH);
        configView.add(configContent, BorderLayout.CENTER);

        return configView;
    }


    /**
     * Crea un botón con icono personalizado.
     *
     * @param iconPath ruta del recurso dentro del classpath
     * @return botón configurado con icono escalado
     */
    private JButton createCustomButton(String iconPath) {

        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(40, 40));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);

        try {

            URL iconURL = getClass().getResource(iconPath);

            if (iconURL != null) {

                ImageIcon icon = new ImageIcon(iconURL);

                btn.setIcon(new ImageIcon(
                        icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)
                ));
            }

        } catch (Exception e) {
            System.err.println("Icono no encontrado: " + iconPath);
        }

        return btn;
    }


    /**
     * Aplica efecto visual de presión a un botón.
     *
     * @param button botón al que se le aplica el efecto
     */
    private void addPressEffect(JButton button) {

        button.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                button.setLocation(button.getX() + 1, button.getY() + 1);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                button.setLocation(button.getX() - 1, button.getY() - 1);
            }
        });
    }


    /**
     * Punto de entrada de la aplicación.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {

        FileHandler.loadPalette();
        FileHandler.loadRecentColors();

        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }

}