package main.java.ui;

import main.java.logic.ClockService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Panel transparente encargado de mostrar la información temporal
 * del sistema (hora, minutos, segundos y fecha actual).
 *
 * <p>
 * Este componente actúa como una capa superpuesta (overlay) sobre el fondo,
 * permitiendo modificar dinámicamente:</p>
 * <ul>
 *     <li>Color del reloj</li>
 *     <li>Fuente (tipo, tamaño y estilo)</li>
 *     <li>Posición dentro del contenedor padre</li>
 * </ul>
 * 
 *
 * @author Diego
 * @version 6.0
 */
public class TimeOverlayPanel extends JPanel {

    // ========================= VARIABLES =========================

    /**
     * Servicio encargado de obtener la hora actual formateada.
     * Centraliza la lógica de tiempo.
     */
    private final ClockService clockService;

    /**
     * Labels principales que muestran la hora (HH:mm:ss).
     */
    private JLabel lblHora, lblMinutos, lblSegundos;

    /**
     * Labels descriptivos debajo de cada valor.
     */
    private JLabel txtHora, txtMinutos, txtSegundos;

    /**
     * Label que muestra la fecha completa.
     */
    private JLabel lblFecha;

    /**
     * Separadores visuales entre hora, minutos y segundos.
     */
    private JPanel sep1, sep2;

    /**
     * Timer que ejecuta la actualización del reloj cada segundo.
     */
    private Timer timer;

    /**
     * Posición actual del reloj dentro del contenedor padre.
     * Valores posibles: CENTER, BOTTOM, LEFT, RIGHT
     */
    private String currentPosition = "CENTER";

    /**
     * Color actual del reloj (estado persistente).
     * Evita que el color se pierda al cambiar fuente o layout.
     */
    private Color currentColor = Color.WHITE;


    // ========================= CONSTRUCTOR =========================

    /**
     * Constructor principal del panel.
     */
    public TimeOverlayPanel() {
        this.clockService = new ClockService("HH:mm:ss");
        initComponents();
        startTimer();
    }


    // ========================= MÉTODOS =========================

    /**
     * Inicializa todos los componentes visuales del reloj.
     *
     * Se usa layout absoluto (null) para tener control total
     * sobre la posición de cada elemento.
     */
    private void initComponents() {

        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(800, 400));

        Font fuenteReloj = new Font("Arial", Font.BOLD, 90);
        Font fuenteTexto = new Font("Arial", Font.PLAIN, 18);

        // Creación de labels principales con estilo uniforme
        lblHora = createStyledLabel("00", fuenteReloj, currentColor, 50, 20, 150, 100);
        lblMinutos = createStyledLabel("00", fuenteReloj, currentColor, 225, 20, 150, 100);
        lblSegundos = createStyledLabel("00", fuenteReloj, currentColor, 400, 20, 150, 100);

        txtHora = createStyledLabel("Hora", fuenteTexto, currentColor, 50, 110, 150, 30);
        txtMinutos = createStyledLabel("Minutos", fuenteTexto, currentColor, 225, 110, 150, 30);
        txtSegundos = createStyledLabel("Segundos", fuenteTexto, currentColor, 400, 110, 150, 30);

        // Separadores visuales
        sep1 = createSeparator(200, 30);
        sep2 = createSeparator(375, 30);

        // Label de fecha
        lblFecha = createStyledLabel("", fuenteTexto, currentColor, 50, 180, 500, 40);

        // Agregar todos los componentes al panel
        add(lblHora);
        add(lblMinutos);
        add(lblSegundos);
        add(txtHora);
        add(txtMinutos);
        add(txtSegundos);
        add(sep1);
        add(sep2);
        add(lblFecha);
    }


    /**
     * Crea un separador visual.
     */
    private JPanel createSeparator(int x, int y) {

        JPanel sep = new JPanel();

        sep.setBackground(new Color(255, 255, 255, 150)); // blanco semi-transparente
        sep.setBounds(x, y, 2, 100);

        return sep;
    }


    /**
     * Crea un JLabel con estilo uniforme.
     */
    private JLabel createStyledLabel(String text, Font font, Color color,
                                     int x, int y, int w, int h) {

        JLabel label = new JLabel(text, SwingConstants.CENTER);

        label.setFont(font);
        label.setForeground(color);
        label.setBounds(x, y, w, h);

        return label;
    }


    /**
     * Inicia el temporizador que actualiza el reloj cada segundo.
     */
    private void startTimer() {

        // Timer ejecuta updateTime() cada 1000 ms
        timer = new Timer(1000, e -> updateTime());

        timer.start();
    }


    /**
     * Actualiza la hora en pantalla.
     */
    private void updateTime() {

        // Se obtiene la hora en formato "HH:mm:ss"
        String[] parts = clockService.getCurrentTime().split(":");

        /**
         * IF:
         * Verifica que el formato sea válido (3 partes: HH, mm, ss)
         * Evita errores si el formato cambia.
         */
        if (parts.length == 3) {

            lblHora.setText(parts[0]);
            lblMinutos.setText(parts[1]);
            lblSegundos.setText(parts[2]);
        }

        updateDate();
    }


    /**
     * Actualiza la fecha actual.
     */
    private void updateDate() {

        DateTimeFormatter formatoFecha =
                DateTimeFormatter.ofPattern(
                        "EEEE, d 'de' MMMM 'de' yyyy",
                        new Locale("es", "ES")
                );

        lblFecha.setText(LocalDateTime.now().format(formatoFecha));
    }


    // ========================= COLOR =========================

    /**
     * Actualiza el color del reloj.
     * @param color Modifica el color del reloj 
     */
    public void updateClockColor(Color color) {

        /**
         * IF:
         * Evita asignar valores nulos que romperían la UI.
         */
        if (color == null) return;

        this.currentColor = color;

        applyColorToComponents();
    }


    /**
     * Aplica el color a todos los componentes.
     */
    private void applyColorToComponents() {

        lblHora.setForeground(currentColor);
        lblMinutos.setForeground(currentColor);
        lblSegundos.setForeground(currentColor);

        txtHora.setForeground(currentColor);
        txtMinutos.setForeground(currentColor);
        txtSegundos.setForeground(currentColor);

        lblFecha.setForeground(currentColor);
    }


    // ========================= FUENTE =========================

    /**
     * Actualiza la fuente del reloj.
     * @param selectedFont Modifica la fuente del reloj
     */
    public void updateClockFont(Font selectedFont) {

        /**
         * IF:
         * Evita errores si se intenta aplicar una fuente nula.
         */
        if (selectedFont == null) return;

        lblHora.setFont(selectedFont);
        lblMinutos.setFont(selectedFont);
        lblSegundos.setFont(selectedFont);

        // Reduce tamaño para etiquetas descriptivas
        txtHora.setFont(selectedFont.deriveFont(Font.PLAIN, selectedFont.getSize() / 3f));
        txtMinutos.setFont(selectedFont.deriveFont(Font.PLAIN, selectedFont.getSize() / 3f));
        txtSegundos.setFont(selectedFont.deriveFont(Font.PLAIN, selectedFont.getSize() / 3f));

        adjustLayout(selectedFont);

        applyColorToComponents(); // mantiene color consistente

        revalidate();
        repaint();
    }


    // ========================= POSICIÓN =========================

    /**
     * Actualiza la posición del reloj dentro del contenedor padre.
     * @param position modifica la posicion del reloj (derecha, izquierda, centro, abajo)
     */
    public void updateClockPosition(String position) {

        this.currentPosition = position;

        SwingUtilities.invokeLater(() -> {

            /**
             * IF:
             * Evita NullPointerException si el panel aún no tiene contenedor padre.
             */
            if (getParent() == null) return;

            int parentWidth = getParent().getWidth();
            int parentHeight = getParent().getHeight();

            int x, y;

            /**
             * SWITCH:
             * Define la posición del reloj dependiendo del valor recibido.
             */
            switch (position) {

                case "BOTTOM" -> {
                    x = (parentWidth - getWidth()) / 2;
                    y = parentHeight - getHeight() - 20;
                }

                case "LEFT" -> {
                    x = 20;
                    y = (parentHeight - getHeight()) / 2;
                }

                case "RIGHT" -> {
                    x = parentWidth - getWidth() - 20;
                    y = (parentHeight - getHeight()) / 2;
                }

                default -> {
                    // CENTER
                    x = (parentWidth - getWidth()) / 2;
                    y = (parentHeight - getHeight()) / 2;
                }
            }

            setLocation(x, y);
        });
    }


    // ========================= LAYOUT =========================

    /**
     * Ajusta el layout dinámicamente según el tamaño de la fuente.
     */
    private void adjustLayout(Font font) {

        int size = font.getSize();

        int width = size * 2;
        int height = size + 20;

        lblHora.setBounds(50, 20, width, height);
        lblMinutos.setBounds(50 + width + 25, 20, width, height);
        lblSegundos.setBounds(50 + (width + 25) * 2, 20, width, height);

        sep1.setBounds(50 + width + 10, 30, 2, height);
        sep2.setBounds(50 + (width + 25) * 2 - 15, 30, 2, height);

        txtHora.setBounds(50, height + 20, width, 30);
        txtMinutos.setBounds(50 + width + 25, height + 20, width, 30);
        txtSegundos.setBounds(50 + (width + 25) * 2, height + 20, width, 30);

        lblFecha.setBounds(50, height + 60, width * 3, 40);

        setSize(width * 3 + 100, height + 120);

        // Mantener posición actual después de redimensionar
       // updateClockPosition(currentPosition);
    }


    // ========================= GETTERS =========================

    /**
     * Obtiene el color actual del reloj.
     * @return obtiene el color actual del reloj 
     */
    public Color getClockColor() {
        return currentColor;
    }
}