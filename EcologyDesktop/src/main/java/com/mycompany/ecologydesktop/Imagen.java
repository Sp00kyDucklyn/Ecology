/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ecologydesktop;

import javax.swing.ImageIcon;

/**
 *
 * @author diego
 */
public class Imagen {
    private String directorio;
    private ImageIcon imageIcon;
    private String valor;
    private String nombre;
    private String fecha;
    private String tipo;
    private String categoria;
    
    public Imagen() {
    }

    public Imagen(String directorio, ImageIcon imageIcon, String nombre) {
        this.directorio = directorio;
        this.imageIcon = imageIcon;
        this.nombre = nombre;
    }

    public Imagen(String directorio, ImageIcon imageIcon) {
        this.directorio = directorio;
        this.imageIcon = imageIcon;
    }

    public Imagen(String directorio, String nombre) {
        this.directorio = directorio;
        this.nombre = nombre;
    }
    
    @Override
    public String toString() {
        return "imagen{" + "valor=" + valor + ", nombre=" + nombre + ", fecha=" + fecha + ", tipo=" + tipo + ", categoria=" + categoria + '}';
    }

    public Imagen(String valor, String nombre, String fecha, String tipo, String categoria) {
        this.valor = valor;
        this.nombre = nombre;
        this.fecha = fecha;
        this.tipo = tipo;
        this.categoria = categoria;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDirectorio() {
        return directorio;
    }

    public void setDirectorio(String directorio) {
        this.directorio = directorio;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
}
