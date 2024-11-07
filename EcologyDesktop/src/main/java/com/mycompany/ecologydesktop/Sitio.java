/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ecologydesktop;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author diego
 */
public class Sitio {

    private String nombre;
    private String descripcion;
    private List<Imagen> imagenes;

    public Sitio(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenes = new ArrayList<>();
    }

    public Sitio(String nombre, List<Imagen> imagenes) {
        this.nombre = nombre;
        this.imagenes = imagenes;
    }

    



    public Sitio(String nombre) {
        this.nombre = nombre;
        this.imagenes = new ArrayList<>();
    }
    
    public String getNombre() {
        return nombre;
    }

    public void addImagen(Imagen imagen) {
        imagenes.add(imagen);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

}
