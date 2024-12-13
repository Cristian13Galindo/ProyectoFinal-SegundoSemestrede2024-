package org.example.demo1.logic;

// Clase Subcategoría (Subcategory)
public class Subcategory {
    private String id;
    private String nombre;
    private double montoAsignado;

    // Actualiza el monto asignado a la subcategoría
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
}