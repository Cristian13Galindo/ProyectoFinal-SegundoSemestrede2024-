package org.example.demo1.logic;

import java.util.Date;

// Clase Presupuesto (Budget)
public class Budget {
    private String id;
    private User usuario;
    private double montoTotal;
    private Date fechaInicio;
    private Date fechaFin;
    private String tipoPeriodo;

    // Establece el monto total del presupuesto
    public void establecerMontoTotal(double monto) {
        this.montoTotal = monto;
    }

    // Define el tipo de periodo del presupuesto
    public void definirPeriodo(String tipo) {
        this.tipoPeriodo = tipo;
    }

    // Asigna un monto a una categoría específica dentro del presupuesto
    public void asignarMontoCategoria(Category category, double monto) {
        // Implementación del método
    }

    // Obtiene el presupuesto actual
    public void obtenerPresupuestoActual() {
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

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getTipoPeriodo() {
        return tipoPeriodo;
    }

    public void setTipoPeriodo(String tipoPeriodo) {
        this.tipoPeriodo = tipoPeriodo;
    }
}