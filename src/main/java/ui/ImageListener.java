/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.ui;

/**
 * Listener para notificar cuando el usuario selecciona
 * una nueva imagen de fondo.
 * 
 * Permite comunicar el panel de configuración
 * con el contenedor principal del reloj.
 * 
 * @author Diego
 */
public interface ImageListener {

    /**
     * Se ejecuta cuando se selecciona una imagen.
     * 
     * @param path ruta absoluta de la imagen
     */
    void onImageSelected(String path);

}
