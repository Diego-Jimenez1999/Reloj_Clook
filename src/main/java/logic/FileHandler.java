/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.logic;

import java.awt.Color;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Clase utilitaria para persistencia de configuración.
 *
 * <p> Responsabilidades:</p>
 * <ul>
 *     <li>Lectura y escritura de configuración</li>
 *     <li>Manejo de cache en memoria</li>
 *     <li>Gestión de archivos del sistema</li>
 * </ul>
 *
 * @author Diego
 * @version 5.0
 */
public class FileHandler {

    // ========================= VARIABLES =========================

    /** Directorio base de configuración */
    private static final String CONFIG_DIR = "config";

    /** Archivo principal de configuración */
    private static final String CONFIG_FILE = CONFIG_DIR + "/config.properties";

    /** Archivo de colores recientes */
    private static final String COLORS_FILE = CONFIG_DIR + "/recent_colors.txt";

    /** Archivo de paleta de colores */
    private static final String PALETTE_FILE = CONFIG_DIR + "/palette.txt";

    /** Cache en memoria (evita lecturas constantes a disco) */
    private static Properties props = new Properties();


    // ========================= CONSTRUCTOR =========================

    /**
     * Constructor privado para evitar instanciación.
     * 
     * 🔥 PATRÓN UTILIDAD:
     * Esta clase solo usa métodos estáticos → no se debe instanciar.
     */
    private FileHandler() {
    }


    // ========================= BLOQUE ESTÁTICO =========================

    /**
     * 🔥 Se ejecuta automáticamente al cargar la clase.
     *
     * LÓGICA:
     * - Inicializa estructura de archivos
     * - Carga configuración en memoria
     */
    static {
        init();
    }


    // ========================= MÉTODOS =========================

    /**
     * Inicializa archivos y carga configuración.
     */
    private static void init() {

        /**
         * 🔥 PASO 1:
         * Asegura que todos los archivos necesarios existan.
         */
        ensureAllFilesExist();

        /**
         * 🔥 PASO 2:
         * Carga propiedades desde disco hacia memoria.
         */
        loadPropertiesFromDisk();
    }

    /**
     * Carga archivo properties a memoria.
     */
    private static void loadPropertiesFromDisk() {

        /**
         * 🔥 TRY-WITH-RESOURCES:
         * - Abre archivo automáticamente
         * - Se cierra solo (evita fugas de memoria)
         */
        try (InputStream in = Files.newInputStream(Paths.get(CONFIG_FILE))) {

            props.load(in);

        } catch (IOException e) {

            /**
             * 🔥 CATCH:
             * Si falla la lectura (archivo corrupto o no accesible)
             * se registra el error pero NO se rompe la app.
             */
            System.err.println("Error cargando config: " + e.getMessage());
        }
    }

    /**
     * Guarda propiedades en disco.
     */
    private static void savePropertiesToDisk() {

        try (OutputStream out = Files.newOutputStream(Paths.get(CONFIG_FILE))) {

            props.store(out, "Updated: " + java.time.Instant.now());

        } catch (IOException e) {
            System.err.println("Error guardando config: " + e.getMessage());
        }
    }

    // ========================= CONFIG =========================

    /**
     * Guarda una clave en memoria y disco.
     * @param key Actualiza en memoria
     * @param value valor corespoondiente a key
     */
    public static void saveConfig(String key, String value) {

        /**
         * 🔥 LÓGICA:
         * 1. Actualiza en memoria (rápido)
         * 2. Persiste en disco (lento)
         */
        props.setProperty(key, value);

        savePropertiesToDisk();
    }

    /**
     * Obtiene valor desde cache en memoria.
     * @param key Obtiene valor desde cache en memoria.
     * @return retorna el valor de la memoria cache
     */
    public static String getConfig(String key) {

        /**
         * 🔥 IMPORTANTE:
         * NO accede a disco → rendimiento óptimo.
         */
        return props.getProperty(key);
    }

    // ========================= CREACIÓN =========================

    /**
     * Asegura existencia de todos los archivos.
     */
    private static void ensureAllFilesExist() {

        ensureConfigDirectoryExists();
        createFileIfNotExists(CONFIG_FILE);
        createFileIfNotExists(COLORS_FILE);
        createPaletteIfNotExists();
    }

    /**
     * Crea el directorio si no existe.
     */
    private static void ensureConfigDirectoryExists() {

        try {

            /**
             * 🔥 Crea directorios recursivamente si no existen.
             */
            Files.createDirectories(Paths.get(CONFIG_DIR));

        } catch (IOException e) {
            System.err.println("Error creando directorio config");
        }
    }

    /**
     * Crea archivo si no existe.
     */
    private static void createFileIfNotExists(String path) {

        try {

            Path filePath = Paths.get(path);

            /**
             * 🔥 IF:
             * Solo crea el archivo si NO existe
             * → evita sobrescribir datos existentes
             */
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

        } catch (IOException e) {
            System.err.println("Error creando archivo: " + path);
        }
    }

    /**
     * Crea paleta por defecto si no existe.
     */
    private static void createPaletteIfNotExists() {

        Path path = Paths.get(PALETTE_FILE);

        /**
         * 🔥 IF:
         * Solo inicializa la paleta una vez
         */
        if (!Files.exists(path)) {

            try (PrintWriter writer = new PrintWriter(new FileWriter(path.toFile()))) {

                /**
                 * 🔥 Escritura secuencial de colores base RGB
                 */
                writer.println("255,255,255");
                writer.println("0,0,0");
                writer.println("255,0,0");
                writer.println("0,255,0");
                writer.println("0,0,255");

            } catch (IOException e) {
                System.err.println("Error creando palette");
            }
        }
    }

    // ========================= PALETA =========================

    /**
     * Carga paleta desde archivo.
     * @return carla la lista de  colores
     */
    public static List<Color> loadPalette() {

        List<Color> colors = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(PALETTE_FILE))) {

            String line;

            /**
             * 🔥 WHILE:
             * Lee línea por línea hasta llegar a null (fin del archivo)
             */
            while ((line = reader.readLine()) != null) {

                String[] rgb = line.split(",");

                /**
                 * 🔥 IF:
                 * Valida que tenga formato correcto (R,G,B)
                 */
                if (rgb.length == 3) {

                    colors.add(new Color(
                            Integer.parseInt(rgb[0]),
                            Integer.parseInt(rgb[1]),
                            Integer.parseInt(rgb[2])
                    ));
                }
            }

        } catch (IOException | NumberFormatException e) {

            /**
             * 🔥 CATCH:
             * Maneja errores de:
             * - Lectura (IOException)
             * - Conversión (NumberFormatException)
             */
            System.err.println("Error leyendo paleta");
        }

        return colors;
    }

    // ========================= COLORES =========================

    /**
     * Guarda colores recientes.
     * 
     * @param colors lista de colores recientes seleccionados por el usuario
     */
    public static void saveRecentColors(List<Color> colors) {

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(COLORS_FILE))) {

            /**
             * 🔥 FOR:
             * Recorre cada color y lo guarda como RGB
             */
            for (Color c : colors) {

                writer.write(c.getRed() + "," + c.getGreen() + "," + c.getBlue());
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("Error guardando colores recientes");
        }
    }

    /**
     * Carga colores recientes.
     * @return retorna una lista de colores
     */
    public static List<Color> loadRecentColors() {

        List<Color> colors = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(COLORS_FILE))) {

            String line;

            /**
             * 🔥 WHILE:
             * Itera hasta finalizar archivo
             */
            while ((line = reader.readLine()) != null) {

                String[] rgb = line.split(",");

                if (rgb.length == 3) {
                    colors.add(new Color(
                            Integer.parseInt(rgb[0]),
                            Integer.parseInt(rgb[1]),
                            Integer.parseInt(rgb[2])
                    ));
                }
            }

        } catch (IOException | NumberFormatException e) {
            System.err.println("Error leyendo colores recientes");
        }

        return colors;
    }

    // ========================= IMÁGENES =========================

    /**
     * Copia una imagen al directorio de wallpapers.
     *
     * @param sourcePath ruta origen
     * @return nueva ruta o null si falla
     */
    public static String copyImageToWallpaper(String sourcePath) {

        try {

            Path source = Paths.get(sourcePath);
            Path targetDir = Paths.get("resources/wallpaper");

            /**
             * 🔥 Garantiza que el directorio exista
             */
            Files.createDirectories(targetDir);

            Path target = targetDir.resolve(source.getFileName());

            /**
             * 🔥 Copia archivo:
             * - Si existe → lo reemplaza
             */
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

            return target.toString();

        } catch (IOException e) {

            /**
             * 🔥 CATCH:
             * Maneja errores de acceso o copia
             */
            System.err.println("Error copiando imagen");
            return null;
        }
    }
}