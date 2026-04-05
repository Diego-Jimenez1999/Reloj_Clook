/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;

import javax.swing.filechooser.FileNameExtensionFilter;
import main.java.Controller.UIController;
import main.java.logic.FileHandler;

/**
 * Panel de configuración de imágenes del reloj.
 *
 * RESPONSABILIDADES:
 * - Gestionar galería de imágenes (máx 10)
 * - Evitar duplicados mediante cache
 * - Mostrar preview en tiempo real
 * - Sincronizar cambios con UIController
 *
 * MEJORAS IMPLEMENTADAS:
 * ✔ Preview vertical (mejor estética)
 * ✔ Scroll solo horizontal
 * ✔ Panel transparente (mejor UI)
 * ✔ Tamaño optimizado de miniaturas
 * ✔ Documentación completa (nivel académico)
 *
 * @author Diego
 * @version 7.0
 */
public class ConfImagePanel extends JPanel {

    // ========================= VARIABLES =========================

    /** Controlador global (MVC - capa lógica) */
    private final UIController controller;

    /** Panel que contiene las miniaturas de imágenes */
    private JPanel galleryPanel;

    /** Panel de vista previa del fondo */
    private ClockPanel previewPanel;

    /** Reloj superpuesto en la vista previa */
    private TimeOverlayPanel previewClock;

    /** Ruta donde se almacenan imágenes */
    private final String WALLPAPER_PATH = "src/resources/wallpaper";

    /** Cache para evitar duplicados */
    private final Set<String> imageCache = new HashSet<>();

    /** Bandera para evitar eventos durante inicialización */
    private boolean isInitializing = true;

    /** Límite máximo de imágenes */
    private static final int MAX_IMAGES = 10;


    // ========================= CONSTRUCTOR =========================

    /**
     * Constructor principal del panel.
     *
     * @param controller controlador central de la aplicación
     */
    public ConfImagePanel(UIController controller) {

        // 🔹 IF: Validación crítica
        if (controller == null) {
            throw new IllegalArgumentException("Controller no puede ser null");
        }

        this.controller = controller;

        initComponents();
        loadImagesFromFolder();

        // 🔹 Carga configuración previa
        String current = FileHandler.getConfig("background_path");
        if (current != null) {
            applyImage(current);
        }

        isInitializing = false;
    }


    // ========================= UI =========================

/**
 * Inicializa y construye la interfaz gráfica del panel de imágenes.
 *
 * RESPONSABILIDADES:
 * - Configurar layout principal del panel
 * - Construir el preview vertical del reloj con fondo
 * - Crear la galería de imágenes con scroll horizontal
 * - Configurar botón para agregar nuevas imágenes
 *
 * DECISIONES DE DISEÑO:
 * - Se usa un preview vertical para mantener consistencia con ConfColorPanel
 * - Se elimina scroll vertical para mejorar estética
 * - Las miniaturas son de 70x70, por lo tanto el botón mantiene coherencia visual
 * - Se agrega feedback visual al botón para mejorar UX
 *
 * BUENAS PRÁCTICAS:
 * - Uso de paneles transparentes (setOpaque(false))
 * - Separación visual con padding
 * - Eventos desacoplados (ActionListener / MouseListener)
 */
private void initComponents() {

    // ========================= CONFIGURACIÓN BASE =========================

    setLayout(new BorderLayout(10, 10)); // 🔹 Layout principal con separación entre componentes
    setBackground(new Color(45, 45, 45)); // 🔹 Fondo oscuro consistente con la aplicación
    setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // 🔹 Padding externo


    // ========================= PREVIEW (VERTICAL) =========================

    previewPanel = new ClockPanel(); // 🔹 Panel que renderiza la imagen de fondo
    previewPanel.setLayout(null);    // 🔹 Layout absoluto para posicionar el reloj manualmente

    // 🔹 Tamaño vertical similar a ConfColorPanel
    previewPanel.setPreferredSize(new Dimension(400, 500));

    previewClock = new TimeOverlayPanel(); // 🔹 Componente que dibuja el reloj

    // 🔹 Tamaño del reloj (evita recortes de texto)
    previewClock.setBounds(0, 0, 300, 120);

    // 🔹 EVENTO: al redimensionar el panel se recalcula la posición del reloj
    previewPanel.addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            centerClock(); // 🔹 Centra el reloj dinámicamente
        }
    });

    previewPanel.add(previewClock); // 🔹 Se agrega el reloj al panel de fondo
    add(previewPanel, BorderLayout.CENTER);


    // ========================= GALERÍA =========================

    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.setOpaque(false); // 🔹 Transparente para integrarse con fondo

    // 🔹 Panel que contiene las miniaturas (flujo horizontal)
    galleryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
    galleryPanel.setOpaque(false);

    // 🔹 Scroll configurado SOLO horizontal (evita crecimiento vertical)
    JScrollPane scroll = new JScrollPane(
            galleryPanel,
            JScrollPane.VERTICAL_SCROLLBAR_NEVER,   // 🔹 elimina scroll vertical
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );

    scroll.setPreferredSize(new Dimension(400, 100)); // 🔹 Altura controlada
    scroll.setBorder(null);                           // 🔹 elimina borde visual por defecto
    scroll.setOpaque(false);
    scroll.getViewport().setOpaque(false);


    // ========================= BOTÓN AGREGAR IMAGEN =========================

    JButton btnAdd = createIconButton("/resources/icons/new_img.png");

    // 🔹 Tamaño del botón coherente con miniaturas (70x70 visual + padding)
    
     int galleryHeight = 100; // mismo valor que scroll

     btnAdd.setPreferredSize(new Dimension(galleryHeight, galleryHeight));
    
    

    /**
     * EVENTOS DEL BOTÓN:
     * - mousePressed: simula efecto de presión (feedback visual)
     * - mouseReleased: restaura estado normal
     * - mouseEntered: cambia cursor para indicar interactividad
     */
    btnAdd.addMouseListener(new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
            // 🔹 IF: valida que el botón exista
            if (btnAdd != null) {
                btnAdd.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // 🔹 Restaurar estado visual
            if (btnAdd != null) {
                btnAdd.setBorder(null);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 🔹 feedback UX
        }
    });

    // 🔹 Evento principal: abrir selector de archivos
    btnAdd.addActionListener(e -> openFileChooser());


    // ========================= CONTENEDORES =========================

    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.setOpaque(false);
    rightPanel.add(btnAdd, BorderLayout.CENTER);

    JPanel container = new JPanel(new BorderLayout());
    container.setOpaque(false);
    container.add(scroll, BorderLayout.CENTER);
    container.add(rightPanel, BorderLayout.EAST);

    bottomPanel.add(container, BorderLayout.CENTER);

    add(bottomPanel, BorderLayout.SOUTH);
}


    // ========================= LÓGICA =========================

    /**
     * Centra el reloj dentro del preview.
     */
    private void centerClock() {

        int x = (previewPanel.getWidth() - previewClock.getWidth()) / 2;
        int y = (previewPanel.getHeight() - previewClock.getHeight()) / 2;

        previewClock.setLocation(x, y);
    }


    /**
     * Crea botón con icono.
     */
    private JButton createIconButton(String path) {

        JButton btn = new JButton();

        btn.setPreferredSize(new Dimension(70,70));
        btn.setBorderPainted(false);
               btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            btn.setIcon(new ImageIcon(
                    icon.getImage().getScaledInstance(80,80,Image.SCALE_SMOOTH)
            ));
        } catch (Exception e) {
            System.err.println("Icono no encontrado");
        }

        return btn;
    }


    /**
     * Carga imágenes desde carpeta local.
     */
    private void loadImagesFromFolder() {

        File folder = new File(WALLPAPER_PATH);

        if (!folder.exists()) folder.mkdirs();

        File[] files = folder.listFiles();
        if (files == null) return;

        // 🔹 FOR: recorrer archivos
        for (File file : files) {

            // 🔹 IF: validar extensión
            if (isValidImage(file.getName())) {

                String path = file.getAbsolutePath();

                imageCache.add(path);
                addImageToGallery(path, false);
            }
        }
    }


    /**
     * Abre selector de archivos.
     */
    private void openFileChooser() {

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png", "gif"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            File selected = chooser.getSelectedFile();

            try {

                File dest = new File(WALLPAPER_PATH, selected.getName());

                // 🔹 IF: evitar duplicados
                if (imageCache.contains(dest.getAbsolutePath())) {
                    JOptionPane.showMessageDialog(this, "Imagen ya existe");
                    return;
                }

                Files.copy(selected.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

                addImageToGallery(dest.getAbsolutePath(), true);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error copiando imagen");
            }
        }
    }


    /**
     * Agrega miniatura a la galería.
     */
    private void addImageToGallery(String path, boolean notify) {

        if (galleryPanel.getComponentCount() >= MAX_IMAGES) {
            galleryPanel.remove(0);
        }

        imageCache.add(path);

        ImageIcon icon = new ImageIcon(path);
        Image scaled = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);

        JLabel thumbnail = new JLabel(new ImageIcon(scaled));
        thumbnail.setCursor(new Cursor(Cursor.HAND_CURSOR));

        thumbnail.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                applyImage(path);
            }
        });

        galleryPanel.add(thumbnail);
        galleryPanel.revalidate();
        galleryPanel.repaint();

        if (notify && !isInitializing) {
            applyImage(path);
        }
    }


    /**
     * Aplica imagen seleccionada.
     */
    private void applyImage(String path) {

        if (path == null) return;

        previewPanel.setIcon(new ImageIcon(path));

        Color color = controller.getCurrentColor();
        Font font = controller.getCurrentFont();

        if (color != null) {
            previewClock.updateClockColor(color);
        }

        if (font != null) {
            previewClock.updateClockFont(font);
        }

        centerClock();

        controller.updateBackground(path);
    }


    /**
     * Refresca estado desde el controller.
     */
    public void refreshFromController() {

        if (controller == null) return;

        String path = controller.getCurrentImagePath();
        Color color = controller.getCurrentColor();
        Font font = controller.getCurrentFont();

        if (path != null) {
            previewPanel.setIcon(new ImageIcon(path));
        }

        if (color != null) {
            previewClock.updateClockColor(color);
        }

        if (font != null) {
            previewClock.updateClockFont(font);
        }

        centerClock();
        repaint();
    }


    /**
     * Valida extensión de imagen.
     */
    private boolean isValidImage(String name) {

        String n = name.toLowerCase();

        return n.endsWith(".png") ||
               n.endsWith(".jpg") ||
               n.endsWith(".jpeg") ||
               n.endsWith(".gif");
    }
}