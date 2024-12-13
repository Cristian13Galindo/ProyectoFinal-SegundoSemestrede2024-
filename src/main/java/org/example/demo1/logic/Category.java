package org.example.demo1.logic;

import java.util.List;

// Clase Categoría (Category)
public class Category {
    private String id;
    private String nombre;
    private double montoAsignado;
    private List<Subcategory> subcategorias;

    // Agrega una subcategoría a la lista de subcategorías
    public void agregarSubcategoria(Subcategory subcategoria) {
        subcategorias.add(subcategoria);
    }

    // Elimina una subcategoría de la lista de subcategorías
    public void eliminarSubcategoria(Subcategory subcategoria) {
        subcategorias.remove(subcategoria);
    }

    // Actualiza el monto asignado a la categoría
    public void actualizarMontoAsignado(double monto) {
        this.montoAsignado = monto;
    }

    // Getters y setters para los atributos de la clase
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getMontoAsignado() {
        return montoAsignado;
    }

    public void setMontoAsignado(double montoAsignado) {
        this.montoAsignado = montoAsignado;
    }

    public List<Subcategory> getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(List<Subcategory> subcategorias) {
        this.subcategorias = subcategorias;
    }
}