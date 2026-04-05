package main.java.logic;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Servicio encargado de gestionar el flujo del tiempo
 * y proporcionar la hora formateada a la interfaz gráfica.
 *
 * <p>Centraliza:</p>
 * <ul>
 *     <li>Obtención de la hora del sistema</li>
 *     <li>Formato de presentación</li>
 * </ul>
 * 
 * <ul>
 *     <li>Separación de responsabilidades (no UI aquí)</li>
 *     <li>Uso de API moderna de Java (java.time)</li>
 *     <li>Formato configurable en tiempo real</li>
 * </ul>
 * 
 *
 * @author Diego Alexander Gaviria Jimenez
 * @version 2.0
 */
public class ClockService {

    // ========================= VARIABLES =========================

    /**
     * Formateador encargado de convertir la hora en texto.
     * Ejemplo: HH:mm:ss
     */
    private DateTimeFormatter formatter;


    // ========================= CONSTRUCTOR =========================

    /**
     * Constructor que inicializa el formato del reloj.
     *
     * @param initialFormat formato inicial (ej: "HH:mm:ss")
     */
    public ClockService(String initialFormat) {

        /**
         * 🔥 LÓGICA:
         * Se crea un formateador a partir del patrón recibido.
         *
         * No hay validación explícita aquí, por lo que:
         * - Si el formato es inválido → lanzará excepción automáticamente
         *   (DateTimeParseException / IllegalArgumentException)
         *
         * ✔ Decisión de ingeniería:
         * Delegar validación a la API estándar → menos código, más robusto.
         */
        this.formatter = DateTimeFormatter.ofPattern(initialFormat);
    }


    // ========================= MÉTODOS =========================

    /**
     * Obtiene la hora actual del sistema formateada.
     *
     * @return cadena con la hora actual
     */
    public String getCurrentTime() {

        /**
         * 🔥 LÓGICA PASO A PASO:
         */

        // 1. Obtener la hora actual del sistema (sin zona horaria explícita)
        LocalTime now = LocalTime.now();

        /**
         * ✔ LocalTime:
         * - Representa solo hora (no fecha)
         * - Preciso y liviano
         */

        // 2. Formatear la hora según el patrón configurado
        String formattedTime = now.format(formatter);

        /**
         * 🔥 PROCESO INTERNO:
         * formatter aplica reglas como:
         * HH → horas 24h
         * mm → minutos
         * ss → segundos
         */

        // 3. Retornar resultado listo para UI
        return formattedTime;
    }

    /**
     * Permite cambiar el formato del reloj dinámicamente.
     *
     * @param newFormat nuevo formato de hora
     */
    public void setTimeFormat(String newFormat) {

        /**
         * 🔥 LÓGICA:
         * Se reemplaza completamente el formateador actual.
         *
         * ✔ IMPORTANTE:
         * - No se modifica el existente → se crea uno nuevo
         * - Evita inconsistencias internas
         */

        this.formatter = DateTimeFormatter.ofPattern(newFormat);

        /**
         * ⚠ RIESGO:
         * Si el formato es inválido → excepción en tiempo de ejecución
         *
         * ✔ POSIBLE MEJORA:
         * Encapsular en try-catch si quieres UI más robusta
         */
    }


    // ========================= GETTERS Y SETTERS =========================

    /**
     * Obtiene el formateador actual.
     *
     * @return formateador activo
     */
    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    /**
     * Permite establecer un formateador directamente.
     *
     * @param formatter nuevo formateador
     */
    public void setFormatter(DateTimeFormatter formatter) {

        /**
         * 🔥 VALIDACIÓN DEFENSIVA:
         * Evita asignar null que rompería el sistema.
         */
        if (formatter == null) {
            throw new IllegalArgumentException("El formatter no puede ser null");
        }

        this.formatter = formatter;
    }
}