/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.ecologydesktop;

/**
 *
 * @author diego
 */
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

public class ZipExtractor {

    public static void extractZipFile(String zipFilePath, String outputDir) throws IOException {
        File zipFile = new File(zipFilePath);
        File outputDirectory = new File(outputDir);

        // Crear el directorio de salida si no existe
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        // Abre el archivo ZIP
        try (ZipFile zip = new ZipFile(zipFile)) {
            Enumeration<ZipArchiveEntry> entries = zip.getEntries();
            
            while (entries.hasMoreElements()) {
                ZipArchiveEntry zipEntry = entries.nextElement();
                File outputFile = new File(outputDirectory, zipEntry.getName());

                // Si el archivo es un directorio, créalo
                if (zipEntry.isDirectory()) {
                    outputFile.mkdirs();
                } else {
                    // Extraer archivo
                    try (InputStream inputStream = zip.getInputStream(zipEntry);
                         FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        String zipFilePath = "./archivo.zip";  // Ruta al archivo ZIP
        String outputDir = "./";  // Directorio de salida

        try {
            extractZipFile(zipFilePath, outputDir);
            System.out.println("Extracción completada.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
