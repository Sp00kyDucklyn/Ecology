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
public class Proyecto {
    private String nombre;
    private List<Sitio> sitios;

    public Proyecto() {
        sitios= new ArrayList<>();
    }

    public List<Sitio> getSitios() {
        return sitios;
    }
    public void addSitio(Sitio sitio){
         sitios.add(sitio);
    }
    public void setSitios(List<Sitio> sitios) {
        this.sitios = sitios;
    }

    public Proyecto(String nombre) {
        sitios= new ArrayList<>();
        this.nombre = nombre;
    }

    public Proyecto(String nombre, List<Sitio> sitios) {
     
        this.nombre = nombre;
        this.sitios = sitios;
    }
    
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
