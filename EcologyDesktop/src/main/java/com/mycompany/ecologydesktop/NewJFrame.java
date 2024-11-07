/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.ecologydesktop;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.iptc.IptcDirectory;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

/**
 *
 * @author diego
 */
public class NewJFrame extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame
     */
    public Proyecto proyectoActual;
    public Sitio sitioActual;
    public List<Proyecto> proyectos = new ArrayList<>();
    public NewJFrame() {
        initComponents();
        //         List<Proyecto> proyectos = new ArrayList<>();
//
//        Proyecto proyecto1 = new Proyecto("Proyecto 1");
//        proyecto1.addSitio(new Sitio("Sitio 1.1", "Descripción del Sitio 1.1"));
//        proyecto1.addSitio(new Sitio("Sitio 1.2", "Descripción del Sitio 1.2"));
//
//        Proyecto proyecto2 = new Proyecto("Proyecto 2");
//        proyecto2.addSitio(new Sitio("Sitio 2.1", "Descripción del Sitio 2.1"));
//        proyecto2.addSitio(new Sitio("Sitio 2.2", "Descripción del Sitio 2.2"));
//
//        proyectos.add(proyecto1);
//        proyectos.add(proyecto2);
//
        String zipFilePath = "./sad.zip";
        
        try {
            proyectos.add(obtenerProyectoDesdeZip(zipFilePath));
        } catch (IOException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        mostrarProyectos(proyectos);
        try {
            ImageIcon imageIcon = extractFirstImageFromZip("./archivo.zip");

            if (imageIcon != null) {
                System.out.println("Imagen extraída correctamente.");
                ImageIcon resizedImageIcon = resizeImageIcon(imageIcon, panelImagen.getWidth(), panelImagen.getHeight());

                img.setIcon(resizedImageIcon);// Aquí podrías hacer algo con el ImageIcon, como mostrarlo en una interfaz gráfica
            } else {
                System.out.println("Imagen no encontrada en el archivo ZIP.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Proyecto obtenerProyectoDesdeZip(String zipFilePath) throws IOException {
        // Obtiene el nombre del archivo .zip sin extensión
        String nombreProyecto = new File(zipFilePath).getName().replaceFirst("[.][^.]+$", "");

        // Almacena los sitios y sus respectivas imágenes
        Map<String, List<Imagen>> directoriosConImagenes = new HashMap<>();

        // Abre el archivo .zip y obtiene los nombres de carpetas e imágenes
        try (ZipFile zipFile = new ZipFile(new File(zipFilePath))) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();

            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                String entryName = entry.getName();

                // Divide la ruta para identificar carpeta y archivo
                String[] pathParts = entryName.split("/");
                if (pathParts.length > 1) {
                    String folderName = pathParts[0];
                    String fileName = pathParts[pathParts.length - 1];

                    // Inicializa la lista de imágenes para la carpeta si no existe
                    directoriosConImagenes.putIfAbsent(folderName, new ArrayList<>());

                    // Si el archivo es una imagen, agrégala a la lista de imágenes de la carpeta
                    if (esImagen(fileName)) {
                        // Cargar la imagen como ImageIcon
                        try (InputStream is = zipFile.getInputStream(entry)) {
                            byte[] imageBytes = is.readAllBytes();

                            // Crear un InputStream a partir de los bytes leídos para extraer los metadatos
                            try (InputStream metadataStream = new ByteArrayInputStream(imageBytes)) {
                                // Extraer metadatos y crear objeto Imagen
                                Imagen imagen = extractImageTag(metadataStream, fileName);
                                imagen.setImageIcon(new ImageIcon(imageBytes)); // Guarda el ImageIcon

                                // Agrega la imagen al directorio correspondiente
                                directoriosConImagenes.get(folderName).add(imagen);
                            }
                        }
                    }
                }
            }
        }

        // Crear los Sitios con sus listas de imágenes y agregarlos al Proyecto
        List<Sitio> sitios = new ArrayList<>();
        for (Map.Entry<String, List<Imagen>> entry : directoriosConImagenes.entrySet()) {
            Sitio sitio = new Sitio(entry.getKey(), entry.getValue());
            sitios.add(sitio);
        }

        // Retorna el objeto Proyecto con el nombre y la lista de sitios
        return new Proyecto(nombreProyecto, sitios);
    }

    // Método auxiliar para verificar si un archivo es una imagen
    private static boolean esImagen(String fileName) {
        String[] extensionesImagen = {".jpg", ".jpeg", ".png", ".gif", ".bmp"};
        for (String extension : extensionesImagen) {
            if (fileName.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    private ImageIcon resizeImageIcon(ImageIcon originalIcon, int targetWidth, int targetHeight) {
        // Redimensiona la imagen del ImageIcon
        Image img = originalIcon.getImage();
        Image resizedImg = img.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

        // Crea un nuevo ImageIcon con la imagen redimensionada
        return new ImageIcon(resizedImg);
    }

    public void mostrarProyectos(List<Proyecto> proyectos) {
        sitioActual=null;
        limpiarPanel();
        panelView.add(Box.createRigidArea(new Dimension(0, 5)));
        for (Proyecto proyecto : proyectos) {
            JButton proyectoButton = new JButton(proyecto.getNombre());
            proyectoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            proyectoButton.addActionListener(e -> mostrarSitios(proyecto));

            proyectoButton.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            proyectoButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            panelView.add(proyectoButton);
            panelView.add(Box.createRigidArea(new Dimension(0, 5)));
        }
    }

    private void limpiarPanel() {
        panelView.removeAll(); // Limpiar todos los componentes del panel
      
    }

    private void mostrarSitios(Proyecto proyecto) {
        // Limpiar el panel de proyectos
        proyectoActual=proyecto;
        limpiarPanel();
        panelView.add(Box.createRigidArea(new Dimension(0, 5)));
        lblProyecto.setText("Proyecto: " + proyecto.getNombre());

        // Agregar botones de sitios
        for (Sitio sitio : proyecto.getSitios()) {
            JButton sitioButton = new JButton(sitio.getNombre());
            sitioButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            sitioButton.addActionListener(e -> mostrarImagenes(sitio));

            // Ajustar el tamaño máximo del botón y agregar un margen
            sitioButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Anchura máxima, altura fija
            sitioButton.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))); // Margen alrededor del botón

            panelView.add(sitioButton);
            panelView.add(Box.createRigidArea(new Dimension(0, 5))); // Espacio entre botones
        }

        // Actualizar la vista
        panelView.revalidate();
        panelView.repaint();
    }

    private void mostrarImagenes(Sitio sitio) {
        limpiarPanel(); // Limpia el panel de imágenes
        
        panelView.add(Box.createRigidArea(new Dimension(0, 5)));
        lblSitio.setText("Sitio: " + sitio.getNombre());

        // Agregar las imágenes al panel
        for (Imagen imagen : sitio.getImagenes()) {
            // Crear un mini panel para cada imagen
            JPanel miniPanel = new JPanel();
            miniPanel.setLayout(new BorderLayout()); // Usar un layout de BorderLayout
            miniPanel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            // Crear un JLabel para la imagen
            ImageIcon resizedImageIcon = resizeImageIcon(imagen.getImageIcon(), 100, 100);
            JLabel imagenLabel = new JLabel(resizedImageIcon);
            // Ajustar el tamaño de la imagen
            imagenLabel.setPreferredSize(new Dimension(100, 100)); // Ajustar según sea necesario
            imagenLabel.setHorizontalAlignment(JLabel.LEFT);
            miniPanel.add(imagenLabel, BorderLayout.WEST); // Agregar la imagen al lado izquierdo

            // Crear un JLabel para el nombre de la imagen
            JLabel nombreLabel = new JLabel(imagen.getNombre());
            nombreLabel.setHorizontalAlignment(JLabel.LEFT);
            miniPanel.add(nombreLabel, BorderLayout.CENTER); // Agregar el nombre al centro
            miniPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mostrarImagen(imagen);
                }
            });
            // Ajustar el tamaño del mini panel
            miniPanel.setPreferredSize(new Dimension(400, 100)); // Ajustar según sea necesario
            miniPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // Ancho máximo

            // Agregar el mini panel al panel principal
            panelView.add(miniPanel);
            panelView.add(Box.createRigidArea(new Dimension(0, 5))); // Espacio entre mini panels
        }

        // Actualizar la vista
        panelView.revalidate();
        panelView.repaint();
    }

    private void mostrarDetalles(Sitio sitio) {
        JOptionPane.showMessageDialog(this, "Detalles del sitio: " + sitio.getNombre());
    }

    private void mostrarDetalles(Proyecto proyecto) {
        JOptionPane.showMessageDialog(this, "Detalles del proyecto: " + proyecto.getNombre());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        PanelVisual = new javax.swing.JPanel();
        Scroll = new javax.swing.JScrollPane();
        panelView = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        lblProyecto = new javax.swing.JLabel();
        lblSitio = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        panelImagen = new javax.swing.JPanel();
        img = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTipo = new javax.swing.JLabel();
        txtFechaHora = new javax.swing.JLabel();
        txtNombre = new javax.swing.JLabel();
        txtEtiquetas = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCategoria = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1000, 720));
        setResizable(false);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setMinimumSize(new java.awt.Dimension(500, 720));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 720));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));

        jPanel2.setBackground(new java.awt.Color(102, 255, 102));
        jPanel2.setForeground(new java.awt.Color(153, 153, 0));
        jPanel2.setMinimumSize(new java.awt.Dimension(500, 720));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(new java.awt.BorderLayout());

        PanelVisual.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        PanelVisual.setLayout(new javax.swing.BoxLayout(PanelVisual, javax.swing.BoxLayout.Y_AXIS));

        panelView.setLayout(new javax.swing.BoxLayout(panelView, javax.swing.BoxLayout.Y_AXIS));
        Scroll.setViewportView(panelView);

        PanelVisual.add(Scroll);

        jPanel3.add(PanelVisual, java.awt.BorderLayout.CENTER);

        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel9.setLayout(new java.awt.GridLayout(1, 0, 0, 50));

        jButton1.setText("Proyecto");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton1);

        jButton2.setText("Sitios");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton2);

        lblProyecto.setText("Proyecto:");
        jPanel9.add(lblProyecto);

        lblSitio.setText("Sitio:");
        jPanel9.add(lblSitio);

        jPanel3.add(jPanel9, java.awt.BorderLayout.PAGE_START);

        jPanel2.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel5.setBackground(new java.awt.Color(178, 235, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 498, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jPanel6.setBackground(new java.awt.Color(204, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 498, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel6, java.awt.BorderLayout.PAGE_END);

        jPanel1.add(jPanel2);

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setForeground(new java.awt.Color(153, 153, 0));
        jPanel4.setMinimumSize(new java.awt.Dimension(600, 720));
        jPanel4.setPreferredSize(new java.awt.Dimension(1000, 720));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        panelImagen.setMinimumSize(new java.awt.Dimension(500, 500));
        panelImagen.setPreferredSize(new java.awt.Dimension(500, 500));
        panelImagen.setLayout(new java.awt.GridBagLayout());
        panelImagen.add(img, new java.awt.GridBagConstraints());

        jPanel4.add(panelImagen);

        jPanel8.setMinimumSize(new java.awt.Dimension(500, 500));
        jPanel8.setPreferredSize(new java.awt.Dimension(500, 500));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setText("Nombre:");

        jLabel2.setText("Fecha Hora:");

        jLabel3.setText("Tipo:");

        jLabel4.setText("Etiquetas:");

        txtTipo.setText("jLabel5");

        txtFechaHora.setText("jLabel5");

        txtNombre.setText("jLabel5");

        txtEtiquetas.setText("jLabel8");

        jLabel5.setText("Tipo:");

        txtCategoria.setText("jLabel5");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 98, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTipo, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                    .addComponent(txtFechaHora, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtEtiquetas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtFechaHora, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtEtiquetas, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 312, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel8);

        jPanel1.add(jPanel4);

        getContentPane().add(jPanel1);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
proyectoActual=null;
   
    
            mostrarProyectos(proyectos);
            
       
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       if(proyectoActual!=null){
           mostrarSitios(proyectoActual);
           lblSitio.setText("Sitio: ");
       }else{
           JOptionPane.showMessageDialog(this, "Escoge un Sitio");
       }
    }//GEN-LAST:event_jButton2ActionPerformed
    public ImageIcon extractFirstImageFromZip(String zipFilePath) throws IOException {
        File zipFile = new File(zipFilePath);

        // Abre el archivo ZIP
        try (ZipFile zip = new ZipFile(zipFile)) {
            Enumeration<ZipArchiveEntry> entries = zip.getEntries();

            // Iterar sobre las entradas para encontrar la primera imagen
            while (entries.hasMoreElements()) {
                ZipArchiveEntry zipEntry = entries.nextElement();

                // Verificar si es un archivo y tiene una extensión de imagen
                if (!zipEntry.isDirectory() && isImageFile(zipEntry.getName())) {
                    // Obtener el InputStream del archivo de imagen
                    try (InputStream inputStream = zip.getInputStream(zipEntry)) {
                        // Cargar la imagen como BufferedImage
                        byte[] imageBytes = inputStream.readAllBytes();
                        ByteArrayInputStream imageStreamForMetadata = new ByteArrayInputStream(imageBytes);
                        ByteArrayInputStream imageStreamForImage = new ByteArrayInputStream(imageBytes);

                        extractImageTags(imageStreamForMetadata, zipEntry.getName());
                        BufferedImage bufferedImage = ImageIO.read(imageStreamForImage);

                        // Convertir BufferedImage a ImageIcon y retornarla
                        if (bufferedImage != null) {
                            return new ImageIcon(bufferedImage);
                        }
                    }
                }
            }
        }

        // Si no se encuentra ninguna imagen, retorna null
        return null;
    }

    private void extractImageTags(InputStream inputStream, String name) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
            txtNombre.setText(name);
            // Iterar sobre los directorios de metadatos
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    // Imprimir solo los tags relevantes
                    if (tag.getTagName().equalsIgnoreCase("Date/Time")) {
                        txtFechaHora.setText(tag.getDescription());
                    } else if (tag.getTagName().equalsIgnoreCase("Artist")) {
                        txtCategoria.setText(tag.getDescription());
                    } else if (tag.getTagName().equalsIgnoreCase("Image Description")) {
                        txtEtiquetas.setText(tag.getDescription());
                    } else if (tag.getTagName().equalsIgnoreCase("User Comment")) {
                        txtTipo.setText(tag.getDescription());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mostrarImagen(Imagen imagen) {
        ImageIcon resizedImageIcon = resizeImageIcon(imagen.getImageIcon(), panelImagen.getWidth(), panelImagen.getHeight());
        img.setIcon(resizedImageIcon);
        txtNombre.setText(imagen.getNombre());
        txtFechaHora.setText(imagen.getFecha());
        txtCategoria.setText(imagen.getCategoria());
        txtEtiquetas.setText(imagen.getValor());
        txtTipo.setText(imagen.getTipo());

    }

    private Imagen extractImageTag(InputStream inputStream, String name) {
        Imagen imagen = new Imagen("", name); // Inicializa la imagen con el nombre

        try {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
            // Iterar sobre los directorios de metadatos
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    // Imprimir solo los tags relevantes
                    if (tag.getTagName().equalsIgnoreCase("Date/Time")) {
                        imagen.setFecha(tag.getDescription()); // Establecer fecha
                    } else if (tag.getTagName().equalsIgnoreCase("Artist")) {
                        imagen.setCategoria(tag.getDescription()); // Establecer categoría
                    } else if (tag.getTagName().equalsIgnoreCase("Image Description")) {
                        imagen.setValor(tag.getDescription()); // Establecer valor (descripción)
                    } else if (tag.getTagName().equalsIgnoreCase("User Comment")) {
                        imagen.setTipo(tag.getDescription()); // Establecer tipo
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imagen; // Devolver la imagen llena de metadatos
    }

    public static boolean isImageFile(String fileName) {
        System.out.println(fileName);
        return fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")
                || fileName.toLowerCase().endsWith(".png") || fileName.toLowerCase().endsWith(".gif");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelVisual;
    private javax.swing.JScrollPane Scroll;
    private javax.swing.JLabel img;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblProyecto;
    private javax.swing.JLabel lblSitio;
    private javax.swing.JPanel panelImagen;
    private javax.swing.JPanel panelView;
    private javax.swing.JLabel txtCategoria;
    private javax.swing.JLabel txtEtiquetas;
    private javax.swing.JLabel txtFechaHora;
    private javax.swing.JLabel txtNombre;
    private javax.swing.JLabel txtTipo;
    // End of variables declaration//GEN-END:variables
}
