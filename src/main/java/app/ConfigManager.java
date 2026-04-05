/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.app;

import java.io.*;
import java.util.Properties;

/**
 * Maneja la lectura y escritura del archivo de configuración.
 *
 * <p>Permite persistir parámetros clave del sistema como:</p>
 * 
 * <ul>
 *     <li>Color del reloj</li>
 *     <li>Imagen de fondo</li>
 *     <li>Fuente</li>
 * </ul>
 * 
 *
 * <p>
 * Utiliza la clase {@link Properties} para almacenar pares clave-valor
 * en un archivo de texto plano.
 * </p>
 *
 * @author Diego
 * @version 2.0
 */
public class ConfigManager {

    // ========================= VARIABLES =========================

    /**
     * Ruta del archivo de configuración.
     *
     * <p>
     * Se utiliza un archivo plano tipo .txt donde se guardan
     * las configuraciones en formato clave=valor.
     * </p>
     */
    private static final String CONFIG_FILE = "config.txt";


    // ========================= CONSTRUCTOR =========================

    /**
     * Constructor privado.
     *
     * <p>
     * Se define como privado porque esta clase es utilitaria
     * (solo métodos estáticos) y no debe ser instanciada.
     * </p>
     */
    private ConfigManager() {
    }


    // ========================= MÉTODOS =========================

    /**
     * Guarda una configuración en el archivo.
     *
     * <p>Flujo de ejecución:</p>
     * <ul>
     *     <li>1. Cargar configuraciones existentes (si el archivo existe)</li>
     *     <li>2. Actualizar o agregar la nueva clave</li>
     *     <li>3. Escribir nuevamente el archivo completo</li>
     * </ul>
     * 
     *
     * @param key clave de configuración
     * @param value valor asociado
     */
    public static void saveConfig(String key, String value) {

        try {

            // Objeto que almacena pares clave-valor en memoria
            Properties props = new Properties();

            File file = new File(CONFIG_FILE);

            /**
             * (if)
             *
             * Si el archivo ya existe:
             * → Se cargan las configuraciones previas para no sobrescribirlas.
             *
             * Si NO existe:
             * → Se continúa con un objeto vacío (se creará el archivo luego).
             */
            if (file.exists()) {

                /**
                 * try-with-resources:
                 * Se abre el archivo y se cierra automáticamente.
                 */
                try (FileInputStream fis = new FileInputStream(file)) {

                    // Cargar configuraciones existentes en memoria
                    props.load(fis);
                }
            }

            /**
             * Se agrega o actualiza la clave.
             *
             * Si ya existía → se sobrescribe.
             * Si no existía → se crea.
             */
            props.setProperty(key, value);

            /**
             * Se escribe TODO el contenido nuevamente en el archivo.
             *
             * 🔥 IMPORTANTE:
             * Properties NO guarda cambios parciales,
             * siempre reescribe el archivo completo.
             */
            try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {

                props.store(fos, "Clock configuration");
            }

        } catch (IOException e) {

            /**
             * 🔥 MANEJO DE ERRORES (try-catch)
             *
             * Captura errores de:
             * - Lectura de archivo
             * - Escritura de archivo
             * - Permisos
             *
             * Se imprime el error en consola para diagnóstico.
             */
            e.printStackTrace();
        }
    }


    /**
     * Lee una configuración del archivo.
     *
     * <p>Flujo:</p>
     * <ul>
     *     <li>1. Verifica si el archivo existe</li>
     *     <li>2. Carga propiedades</li>
     *     <li>3. Retorna valor asociado a la clave</li>
     * </ul>
     * 
     *
     * @param key clave a buscar
     * @return valor asociado o null si no existe
     */
    public static String getConfig(String key) {

        try {

            Properties props = new Properties();

            File file = new File(CONFIG_FILE);

            /**
             * 🔥 CONDICIÓN (if)
             *
             * Si el archivo NO existe:
             * → No hay configuraciones guardadas
             * → Se retorna null inmediatamente
             */
            if (!file.exists()) return null;

            /**
             * Se abre el archivo para lectura
             */
            FileInputStream fis = new FileInputStream(file);

            // Se cargan las propiedades en memoria
            props.load(fis);

            // Se cierra manualmente el flujo
            fis.close();

            /**
             * Se retorna el valor asociado a la clave.
             *
             * Si la clave no existe → retorna null.
             */
            return props.getProperty(key);

        } catch (Exception e) {

            /**
             * 🔥 MANEJO DE ERRORES GENERAL
             *
             * Captura cualquier excepción:
             * - IO
             * - formato
             * - acceso a archivo
             */
            e.printStackTrace();
        }

        // Retorno por defecto si ocurre un error
        return null;
    }



}