/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.Controller;

import javax.swing.*;
import java.awt.*;
import main.java.ui.*;
import main.java.app.*;
import main.java.logic.FileHandler;

/**
 * Controlador central de la aplicación.
 *
 * <p>
 * Gestiona la lógica principal de interacción entre la interfaz gráfica
 * y la lógica del sistema, incluyendo:
 * </p>
 *
 * <ul>
 *     <li>Sincronización entre vistas (MVC)</li>
 *     <li>Persistencia de configuración (colores, fuente, imagen)</li>
 *     <li>Aplicación de cambios en tiempo real</li>
 * </ul>
 *
 * @author Diego
 * @version 4.1 (Corrección de Javadoc profesional)
 */
public class UIController {

    // ========================= VARIABLES =========================

    /** Contenedor principal que maneja las vistas mediante CardLayout */
    private final JPanel mainContainer;

    /** Layout encargado de cambiar entre vistas */
    private final CardLayout cardLayout;

    /** Panel principal donde se muestra el fondo del reloj */
    private ClockPanel clockPanel;

    /** Panel overlay donde se dibuja la hora */
    private TimeOverlayPanel overlayPanel;

    /** Panel de configuración de imágenes */
    private ConfImagePanel panelConfig;

    /** Panel de configuración de fuente */
    private ConfFontPanel panelFont;

    /** Panel de configuración de color */
    private ConfColorPanel panelColor;

    /** Referencia a la ventana principal */
    private Main mainFrame;

    // ===== ESTADO GLOBAL =====

    /** Ruta de la imagen actual */
    private String currentImagePath;

    /** Color actual del reloj */
    private Color currentColor;

    /** Fuente actual del reloj */
    private Font currentFont;

    /** Posición actual del reloj */
    private String currentPosition;


    // ========================= CONSTRUCTORES =========================

    /**
     * Constructor principal usando navegación con CardLayout.
     *
     * @param mainContainer contenedor principal donde se renderizan las vistas
     * @param cardLayout layout que permite cambiar entre vistas
     */
    public UIController(JPanel mainContainer, CardLayout cardLayout) {
        this.mainContainer = mainContainer;
        this.cardLayout = cardLayout;
    }

    /**
     * Constructor alternativo con referencia directa al frame principal.
     *
     * @param mainFrame ventana principal de la aplicación
     */
    public UIController(Main mainFrame) {
        this.mainFrame = mainFrame;
        this.mainContainer = null;
        this.cardLayout = null;
    }


    // ========================= MÉTODOS =========================

    /**
     * Aplica una imagen como fondo del reloj.
     *
     * @param path ruta absoluta de la imagen a aplicar
     */
    public void updateBackground(String path) {

        this.currentImagePath = path;

        if (clockPanel != null && path != null) {
            clockPanel.setIcon(new ImageIcon(path));
            clockPanel.repaint();
        }

        FileHandler.saveConfig("background_path", path);
        refreshAllPreviews();
    }

    /**
     * Aplica un color al reloj.
     *
     * @param color color seleccionado para el texto del reloj
     */
    public void applyColor(Color color) {

        if (color == null) return;

        this.currentColor = color;

        if (overlayPanel != null) {
            overlayPanel.updateClockColor(color);
        }

        String rgb = color.getRed() + "," + color.getGreen() + "," + color.getBlue();
        FileHandler.saveConfig("clock_color", rgb);

        refreshAllPreviews();
    }

    /**
     * Aplica una fuente al reloj.
     *
     * @param font fuente seleccionada (tipo, tamaño, estilo)
     */
    public void applyFont(Font font) {

        if (font == null) {
            throw new IllegalArgumentException("La fuente no puede ser null");
        }

        this.currentFont = font;

        if (overlayPanel != null) {
            overlayPanel.updateClockFont(font);
        }

        FileHandler.saveConfig("font_name", font.getName());
        FileHandler.saveConfig("font_size", String.valueOf(font.getSize()));
        FileHandler.saveConfig("font_style", String.valueOf(font.getStyle()));

        refreshAllPreviews();
    }

    /**
     * Aplica la posición del reloj dentro del panel.
     *
     * @param position posición del reloj (ej: CENTER, TOP, LEFT, etc.)
     */
    public void applyPosition(String position) {

        if (position == null) {
            throw new IllegalArgumentException("La posición no puede ser null");
        }

        this.currentPosition = position;

        if (overlayPanel != null) {
            overlayPanel.updateClockPosition(position);
            overlayPanel.revalidate();
            overlayPanel.repaint();
        }

        FileHandler.saveConfig("clock_position", position);
    }

    /**
     * Carga la configuración inicial desde almacenamiento persistente.
     */
    public void loadInitialConfig() {

        String colorStr = FileHandler.getConfig("clock_color");

        if (colorStr != null && colorStr.contains(",")) {
            try {
                String[] rgb = colorStr.split(",");

                Color c = new Color(
                        Integer.parseInt(rgb[0]),
                        Integer.parseInt(rgb[1]),
                        Integer.parseInt(rgb[2])
                );

                applyColor(c);

            } catch (Exception ignored) {}
        }

        try {
            String name = FileHandler.getConfig("font_name");
            String size = FileHandler.getConfig("font_size");
            String style = FileHandler.getConfig("font_style");

            if (name != null && size != null && style != null) {

                Font f = new Font(
                        name,
                        Integer.parseInt(style),
                        Integer.parseInt(size)
                );

                applyFont(f);
            }

        } catch (NumberFormatException ignored) {}

        String pos = FileHandler.getConfig("clock_position");

        if (pos != null) {
            applyPosition(pos);
        }

        String bg = FileHandler.getConfig("background_path");

        if (bg != null) {
            updateBackground(bg);
        }
    }

    /**
     * Registra el panel de configuración de imágenes.
     *
     * @param panelConfig instancia del panel de imágenes
     */
    public void setPanelConfig(ConfImagePanel panelConfig) {
        this.panelConfig = panelConfig;
    }

    /**
     * Asigna el panel principal del reloj.
     *
     * @param clockPanel panel donde se renderiza el fondo
     */
    public void setClockPanel(ClockPanel clockPanel) {
        this.clockPanel = clockPanel;
    }

    /**
     * Asigna el overlay del reloj.
     *
     * @param overlayPanel panel donde se dibuja la hora
     */
    public void setClockOverlay(TimeOverlayPanel overlayPanel) {
        this.overlayPanel = overlayPanel;
    }

    /**
     * Cambia la vista activa.
     *
     * @param viewName nombre de la vista registrada en el CardLayout
     */
    public void showView(String viewName) {

        if (cardLayout != null && mainContainer != null) {
            cardLayout.show(mainContainer, viewName);
        }
    }

    /**
     * Muestra la vista de configuración.
     */
    public void showConfigView() {

        if (panelConfig != null) {
            panelConfig.refreshFromController();
        }

        cardLayout.show(mainContainer, "CONFIG");
    }

    /**
     * Sincroniza todos los paneles con el estado actual del sistema.
     */
    public void refreshAllPreviews() {

        if (panelConfig != null) {
            panelConfig.refreshFromController();
        }

        if (panelFont != null) {
            panelFont.refreshFromController();
        }

        if (panelColor != null) {
            panelColor.refreshPreview();
        }
    }

    // ========================= GETTERS =========================

    /**
     * Obtiene el overlay del reloj.
     *
     * @return panel overlay del reloj
     */
    public TimeOverlayPanel getOverlayPanel() {
        return overlayPanel;
    }

    /**
     * Obtiene el color actual del reloj.
     *
     * @return color actual configurado
     */
    public Color getCurrentColor() {
        return currentColor;
    }

    /**
     * Retorna la fuente actual con fallback seguro.
     *
     * @return fuente configurada o una fuente por defecto si es null
     */
    public Font getCurrentFont() {

        if (currentFont != null) {
            return currentFont;
        }

        return new Font("Arial", Font.BOLD, 48);
    }

    /**
     * Obtiene la ruta de la imagen actual.
     *
     * @return ruta de la imagen de fondo
     */
    public String getCurrentImagePath() {
        return currentImagePath;
    }

    /**
     * Obtiene la posición actual del reloj.
     *
     * @return posición configurada
     */
    public String getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Asigna el panel de fuentes.
     *
     * @param panelFont panel de configuración de fuente
     */
    public void setPanelFont(ConfFontPanel panelFont) {
        this.panelFont = panelFont;
    }

    /**
     * Asigna el panel de colores.
     *
     * @param panelColor panel de configuración de color
     */
    public void setPanelColor(ConfColorPanel panelColor) {
        this.panelColor = panelColor;
    }

    /**
     * Asigna el panel de imágenes.
     *
     * @param panelImage panel de configuración de imágenes
     */
    public void setPanelImage(ConfImagePanel panelImage) {
        this.panelConfig = panelImage;
    }
}