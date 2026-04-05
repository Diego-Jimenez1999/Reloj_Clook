/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.app;

/**
 * Listener utilizado para notificar cambios en la configuración de la aplicación.
 * <p>
 * Esta interfaz define un mecanismo de comunicación desacoplado entre los
 * componentes de la interfaz gráfica (paneles de configuración) y el
 * controlador principal de la aplicación.
 * </p>
 *
 * <p>
 * Su objetivo es permitir que los componentes de la UI informen que una
 * configuración ha cambiado sin depender directamente de la lógica de
 * persistencia o del controlador principal.
 * </p>
 *
 * <p><b>Ejemplo de uso:</b></p>
 *
 * <pre>
 * // En un panel de configuración
 * changeListener.onConfigChanged("background_path", imagePath);
 *
 * // En el controlador principal
 * public void onConfigChanged(String key, Object newValue) {
 *     FileHandler.saveConfig(key, String.valueOf(newValue));
 * }
 * </pre>
 *
 * <p>
 * Este enfoque sigue el principio de <b>desacoplamiento</b> y facilita el
 * mantenimiento y la escalabilidad del sistema.
 * </p>
 *
 * @author Diego Alexander Gaviria Jimenez
 * @version 1.0
 */
public interface ConfigChangeListener {

    /**
     * Método invocado cuando un parámetro de configuración es modificado
     * desde algún componente de la interfaz.
     *
     * @param key
     * Identificador de la configuración modificada.
     * Ejemplo: {@code "background_path"}, {@code "clock_format"}.
     *
     * @param newValue
     * Nuevo valor asociado a la configuración modificada.
     * Puede representar rutas, colores, formatos u otros parámetros.
     */
    void onConfigChanged(String key, Object newValue);

}