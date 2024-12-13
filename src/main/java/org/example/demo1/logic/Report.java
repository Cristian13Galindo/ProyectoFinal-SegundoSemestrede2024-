package org.example.demo1.logic;

// Clase Reporte (Report)
import java.util.Date;

public class Report {
    private String id;
    private User usuario;
    private String tipoReporte;
    private String contenido;
    private Date fechaGeneracion;

    // Genera el reporte
    public void generarReporte() {
        // Implementación del método
    }

    // Exporta el reporte en el formato especificado
    public void exportarReporte(String formato) {
        // Implementación del método
    }

    // Envía el reporte por email
    public void enviarReportePorEmail() {
        // Implementación del método
    }

    // Getters y setters para los atributos de la clase
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
}