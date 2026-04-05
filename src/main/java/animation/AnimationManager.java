
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.animation;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Clase utilitaria encargada de realizar animaciones de desplazamiento
 * de componentes Swing.
 *
 * <p>
 * Utiliza {@link javax.swing.Timer} para ejecutar las animaciones
 * dentro del Event Dispatch Thread (EDT), evitando problemas de
 * concurrencia en la interfaz gráfica.
 * </p>
 *
 * <p>
 * Actualmente soporta animaciones horizontales utilizadas para
 * mostrar u ocultar paneles laterales como el panel de configuración.
 * </p>
 *
 * @author Diego Alexander Gaviria Jimenez
 * @version 3.1
 */
public class AnimationManager {

    // ========================= VARIABLES =========================
    // (Actualmente no hay variables de instancia porque la clase es utilitaria)


    // ========================= CONSTRUCTOR =========================

    /**
     * Constructor por defecto.
     *
     * <p>
     * No realiza ninguna inicialización ya que la clase
     * funciona como utilidad (stateless).
     * </p>
     */
    public AnimationManager() {
    }


    // ========================= MÉTODOS =========================

    /**
     * Realiza una animación horizontal hasta una posición destino.
     *
     * <p>
     * Este método desplaza un panel desde su posición actual (X)
     * hasta una posición objetivo (targetX) de forma progresiva.
     * </p>
     *
     * <p> Lógica interna:</p>
     * <ul>
     *     <li>Se usa un {@link Timer} para ejecutar pasos periódicos</li>
     *     <li>En cada iteración se ajusta la posición X</li>
     *     <li>Se detiene automáticamente al llegar al destino</li>
     * </ul>
     * 
     *
     * @param panel panel que se desea animar
     * @param targetX posición X final
     * @param delay tiempo entre cada paso de animación (ms)
     * @param step cantidad de píxeles que se moverá en cada actualización
     */
    public void animateX(JPanel panel, int targetX, int delay, int step) {

        // Se crea un Timer que ejecutará la animación cada "delay" milisegundos
        Timer timer = new Timer(delay, null);

        // Se agrega la lógica que se ejecuta en cada "tick" del Timer
        timer.addActionListener(e -> {

            // Obtener la posición actual del panel en X
            int currentX = panel.getX();

            /**
             * 🔥 ESTRUCTURA CONDICIONAL (if / else)
             *
             * Se evalúa hacia dónde debe moverse el panel:
             *
             * - Si la posición actual es MENOR que la posición objetivo:
             *   → el panel debe moverse hacia la DERECHA (sumar step)
             *
             * - Si es MAYOR:
             *   → el panel debe moverse hacia la IZQUIERDA (restar step)
             */
            if (currentX < targetX) {

                // Movimiento hacia la derecha
                currentX += step;

                /**
                 * 🔥 CONTROL DE SOBREPASO
                 *
                 * Evita que el panel pase la posición destino.
                 * Si ocurre, se corrige y se fija exactamente en targetX.
                 */
                if (currentX > targetX) {
                    currentX = targetX;
                }

            } else {

                // Movimiento hacia la izquierda
                currentX -= step;

                /**
                 * 🔥 CONTROL DE SOBREPASO (lado izquierdo)
                 *
                 * Si el panel se pasa del objetivo, se corrige.
                 */
                if (currentX < targetX) {
                    currentX = targetX;
                }
            }

            /**
             * Se actualiza la nueva posición del panel.
             * Solo cambia X, la Y se mantiene igual.
             */
            panel.setLocation(currentX, panel.getY());

            /**
             * 🔥 CONDICIÓN DE PARADA
             *
             * Cuando el panel llega exactamente a la posición destino,
             * se detiene el Timer para evitar ejecuciones innecesarias.
             */
            if (currentX == targetX) {
                timer.stop();
            }

        });

        /**
         * Se inicia la animación.
         *
         * A partir de aquí, el Timer ejecutará el bloque anterior
         * cada "delay" milisegundos.
         */
        timer.start();
    }

}