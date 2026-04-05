/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.Controller;

import javax.swing.*;
import main.java.app.Main;

/**
 * Clase encargada de gestionar los eventos de la ventana principal.
 *
 * <p>
 * RESPONSABILIDAD:
 * Centraliza la lógica de navegación de la aplicación para evitar
 * acoplar eventos directamente en la clase {@link Main}.
 * </p>
 *
 * <p>
 * BENEFICIO ARQUITECTÓNICO:
 * - Separación de responsabilidades (UI vs lógica)
 * - Facilita mantenimiento y escalabilidad
 * - Permite reutilizar lógica de navegación
 * </p>
 *
 * @author Diego Alexander Gaviria Jimenez
 * @version 2.0
 */
public class MainEventHandler {

    // ========================= VARIABLES =========================

    /** Controlador principal encargado de gestionar vistas */
    private final UIController uiController;

    /** Referencia a la ventana principal */
    private final Main main;


    // ========================= CONSTRUCTOR =========================

    /**
     * Constructor del manejador de eventos.
     *
     * <p>
     * Inyecta las dependencias necesarias para poder delegar
     * la navegación sin acoplar directamente la lógica en la UI.
     * </p>
     *
     * @param main referencia a la ventana principal
     * @param uiController controlador principal de navegación
     */
    public MainEventHandler(Main main, UIController uiController) {

        // 🔹 Validación básica (buena práctica)
        if (main == null || uiController == null) {
            throw new IllegalArgumentException("Main y UIController no pueden ser null");
        }

        this.main = main;
        this.uiController = uiController;
    }


    // ========================= MÉTODOS =========================

    /**
     * Evento para navegar a la vista principal del reloj.
     *
     * <p>
     * LÓGICA:
     * - Se delega completamente al {@link UIController}
     * - Se evita manipular directamente el CardLayout desde aquí
     * </p>
     *
     * <p>
     * FLUJO:
     * Usuario → Evento UI → MainEventHandler → UIController → Cambio de vista
     * </p>
     */
    public void goHome() {

        // 🔹 No hay condicionales porque la navegación siempre es válida
        // 🔹 Se delega la responsabilidad al controlador central
        uiController.showView("RELOJ");
    }


    /**
     * Evento para abrir la vista de configuración.
     *
     * <p>
     * LÓGICA:
     * - Cambia la vista a CONFIG mediante el controlador
     * - Puede ejecutar lógica adicional como logs o validaciones futuras
     * </p>
     *
     * <p>
     * NOTA DE INGENIERÍA:
     * El uso de System.out.println es temporal.
     * En producción debería usarse un logger (ej: Log4j o java.util.logging)
     * </p>
     */
    public void openConfig() {

        // 🔹 Delega navegación al controlador
        uiController.showConfigView();

        // 🔹 Log de depuración (debug)
        System.out.println("Navegando a configuración...");
    }


    /**
     * Evento para cerrar la aplicación.
     *
     * <p>
     * LÓGICA:
     * - Finaliza la ejecución de la JVM
     * - Libera todos los recursos asociados
     * </p>
     *
     * <p>
     * ⚠ IMPORTANTE:
     * System.exit(0) termina el programa de forma inmediata.
     * En sistemas más complejos se recomienda:
     * - Guardar estado
     * - Cerrar conexiones
     * - Liberar recursos manualmente
     * </p>
     */
    public void closeApp() {

        // 🔹 Código 0 = terminación normal
        System.exit(0);
    }


    // ========================= GETTERS =========================

    /**
     * Obtiene el controlador UI.
     *
     * @return instancia de UIController
     */
    public UIController getUiController() {
        return uiController;
    }

    /**
     * Obtiene la referencia a la ventana principal.
     *
     * @return instancia de Main
     */
    public Main getMain() {
        return main;
    }


}