/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.ecologydesktop;
import com.github.junrar.Junrar;
import com.github.junrar.exception.RarException;
import java.io.File;
import java.io.IOException;

public class RarExtractor {

    public static void main(String[] args) throws RarException {
        // Ruta del archivo RAR
        String rarFilePath = "ruta/al/archivo.rar";
        // Carpeta de salida
        String outputDir = "ruta/de/salida/";

        try {
            extractRar(rarFilePath, outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void extractRar(String rarFilePath, String outputDir) throws IOException, RarException {
        File rarFile = new File(rarFilePath);
        File outputDirectory = new File(outputDir);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();  // Crear la carpeta de salida si no existe
        }

        // Utilizando Junrar para extraer el contenido
        Junrar.extract(rarFile, outputDirectory);
        System.out.println("Extracción completada.");
    }
    
}
