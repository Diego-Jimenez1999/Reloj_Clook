/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.utils;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Clase utilitaria para gestionar la carga de recursos externos 
 * como imágenes o iconos dentro de la aplicación.
 *
 *
 * @author Diego
 * @version 1.0
 */
public class ResourceLoader {


    // ========================= CONSTRUCTOR =========================

    /**
     * Constructor privado para evitar la instanciación.
     *
     * <p>
     * Este patrón se usa en clases utilitarias donde todos los métodos
     * son estáticos y no se necesita crear objetos.
     * </p>
     *
     * <p>
     * THROW:
     * Se lanza una excepción si alguien intenta crear una instancia
     * de la clase, protegiendo su uso correcto.
     * </p>
     */
    private ResourceLoader() {
        throw new AssertionError("Clase utilitaria, no instanciar.");
    }


    // ========================= MÉTODOS =========================

    /**
     * Carga una imagen desde una ruta del sistema de archivos.
     *
     * @param path Ruta absoluta o relativa donde se encuentra la imagen.
     *
     * @return Objeto Image cargado correctamente o null si ocurre un error.
     */
    public static Image loadImage(String path) {

        /**
         * TRY:
         * Se intenta leer la imagen desde el sistema de archivos.
         * ImageIO.read puede lanzar IOException si:
         * - El archivo no existe
         * - No hay permisos
         * - El formato no es válido
         */
        try {

            // Se crea un objeto File implícito y se intenta leer como imagen
            return ImageIO.read(new java.io.File(path));

        } catch (IOException e) {

            /**
             * CATCH:
             * Si ocurre un error al leer la imagen:
             * - Se captura la excepción
             * - Se muestra un mensaje en consola
             * - Se retorna null para indicar fallo
             */
            System.err.println("Error al cargar la imagen en: " + path);

            return null;
        }
    }


    /**
     * Carga una imagen como ImageIcon para componentes Swing.
     *
     * @param path Ruta del archivo de imagen.
     *
     * @return Objeto ImageIcon si la imagen se carga correctamente,
     *         o null si falla la carga.
     */
    public static ImageIcon loadIcon(String path) {

        /**
         * VARIABLE LOCAL:
         * img almacena la imagen cargada desde el método loadImage.
         */
        Image img = loadImage(path);

        /**
         * OPERADOR TERNARIO (equivalente a un IF):
         *
         * IF (img != null)
         *     return new ImageIcon(img);
         * ELSE
         *     return null;
         *
         * Se usa para evitar crear un ImageIcon con una imagen inválida.
         */
        return (img != null) ? new ImageIcon(img) : null;
    }


  
}