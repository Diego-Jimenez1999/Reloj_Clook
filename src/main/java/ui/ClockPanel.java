
package main.java.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Panel gráfico personalizado utilizado para mostrar el fondo del reloj.
 *
 * <p>Responsabilidades:</p>
 * <ul>
 *     <li>Renderizar fondo (color o imagen)</li>
 *     <li>Aplicar bordes redondeados</li>
 *     <li>Escalar imagen dinámicamente</li>
 * </ul>
 * 
 
 *
 * @author Diego
 * @version 3.0
 */
public class ClockPanel extends JPanel {

    // ========================= VARIABLES =========================

    /** Color de fondo cuando no hay imagen */
    private Color backgroundColor = Color.WHITE;

    /** Radio horizontal de esquinas */
    private int arcWidth = 30;

    /** Radio vertical de esquinas */
    private int arcHeight = 30;

    /** Imagen de fondo */
    private Image image = null;


    // ========================= CONSTRUCTOR =========================

    /**
     * Constructor por defecto.
     */
    public ClockPanel() {

        /**
         * 🔥 LÓGICA:
         * Se define como no opaco para permitir
         * renderizado personalizado completo.
         *
         * ✔ Swing no dibuja fondo automáticamente
         */
        setOpaque(false);
    }


    // ========================= MÉTODOS =========================

    /**
     * Método principal de renderizado del componente.
     *
     * @param g contexto gráfico
     */
    @Override
    protected void paintComponent(Graphics g) {

        /**
         * 🔥 PASO 1:
         * Crear copia del contexto gráfico.
         *
         * ✔ Evita modificar el original (buena práctica)
         */
        Graphics2D g2 = (Graphics2D) g.create();

        /**
         * 🔥 PASO 2:
         * Activar renderizado de alta calidad.
         */
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        /**
         * 🔥 PASO 3:
         * Crear figura con bordes redondeados.
         */
        RoundRectangle2D.Float shape = new RoundRectangle2D.Float(
                0,
                0,
                getWidth(),
                getHeight(),
                arcWidth,
                arcHeight
        );

        /**
         * 🔥 PASO 4:
         * Pintar fondo base.
         */
        g2.setColor(backgroundColor);
        g2.fill(shape);

        /**
         * 🔥 PASO 5:
         * Dibujar imagen SOLO si existe.
         */
        if (image != null) {

            /**
             * 🔥 IF:
             * - Si hay imagen → se dibuja
             * - Si NO → solo queda el color de fondo
             */

            /**
             * 🔥 CLIP:
             * Limita el área de dibujo a la forma redondeada
             */
            g2.clip(shape);

            /**
             * 🔥 ESCALADO:
             * La imagen se ajusta automáticamente al tamaño del panel
             */
            g2.drawImage(
                    image,
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    null
            );
        }

        /**
         * 🔥 PASO 6:
         * Liberar recursos gráficos.
         */
        g2.dispose();

        /**
         * 🔥 PASO 7:
         * Llamar a implementación base.
         */
        super.paintComponent(g);
    }


    // ========================= GETTERS Y SETTERS =========================

    /**
     * Obtiene color de fondo.
     * @return Color
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Cambia color de fondo.
     * 
     * @param backgroundColor  color que se aplicará como fondo
     */
    public void setBackgroundColor(Color backgroundColor) {

        /**
         * 🔥 VALIDACIÓN:
         * Evita asignar null (buena práctica defensiva)
         */
        if (backgroundColor == null) return;

        this.backgroundColor = backgroundColor;

        /**
         * 🔥 repaint():
         * Fuerza redibujado inmediato del panel
         */
        repaint();
    }

    /**
     * Obtiene radio horizontal.
     * @return int
     */
    public int getArcWidth() {
        return arcWidth;
    }

    /**
     * Cambia radio horizontal.
     * @param arcWidth  radio horizontal de las esquinas redondeadas del panel.
     */
    public void setArcWidth(int arcWidth) {

        this.arcWidth = arcWidth;

        /**
         * 🔥 Redibujo necesario porque cambia la forma
         */
        repaint();
    }

    /**
     * Obtiene radio vertical.
     * @return int 
     */
    public int getArcHeight() {
        return arcHeight;
    }

    /**
     * Cambia radio vertical.
     * @param arcHeight radio vertical de las esquinas redondeadas del panel.
     */
    public void setArcHeight(int arcHeight) {

        this.arcHeight = arcHeight;
        repaint();
    }

    /**
     * Obtiene imagen actual.
     * @return tetorna una imagen 
     */
    public Image getImage() {
        return image;
    }

    /**
     * Establece imagen directamente.
     * 
     * @param image Establece imagen directamente.
     */
    public void setImage(Image image) {

        /**
         * 🔥 Puede ser null:
         * - null → se elimina imagen (queda solo color)
         */
        this.image = image;

        repaint();
    }

    /**
     * Establece imagen a partir de un Icon.
     * 
     * @param icon Establece imagen a partir de un Icon.
     */
    public void setIcon(Icon icon) {

        /**
         * 🔥 IF + PATTERN MATCHING:
         * Verifica que el icono sea de tipo ImageIcon
         */
        if (icon instanceof ImageIcon imageIcon) {

            /**
             * Extrae la imagen interna
             */
            this.image = imageIcon.getImage();

            repaint();
        }
    }
}