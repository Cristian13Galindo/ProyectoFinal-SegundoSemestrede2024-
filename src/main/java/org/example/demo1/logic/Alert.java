package org.example.demo1.logic;

// Clase Alerta (Alert)
public class Alert {
    private String id;
    private Category categoria;
    private double umbral;
    private String mensaje;
    private boolean activo;

    // Activa la alerta
    public void activarAlerta() {
        this.activo = true;
    }

    // Desactiva la alerta
    public void desactivarAlerta() {
        this.activo = false;
    }

    // Actualiza el umbral de la alerta
    public void actualizarUmbral(double nuevoUmbral) {
        this.umbral = nuevoUmbral;
    }

    // Genera un mensaje de alerta basado en el umbral y la categoría
    public void generarMensajeAlerta() {
        this.mensaje = "Alerta: El umbral de " + categoria.getNombre() + " ha sido superado.";
    }

    // Getters y setters para los atributos de la clase
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Category getCategoria() {
        return categoria;
    }

    public void setCategoria(Category categoria) {
        this.categoria = categoria;
    }

    public double getUmbral() {
        return umbral;
    }

    public void setUmbral(double umbral) {
        this.umbral = umbral;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
