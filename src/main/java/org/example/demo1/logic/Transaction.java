package org.example.demo1.logic;

import java.util.Date;

public class Transaction {
    private String id;
    private Category categoria;
    private Subcategory subcategoria;
    private double monto;
    private Date fecha;
    private String descripcion;
    private String ubicacion;
    private String metodoPago;

    // Constructor
    public Transaction(String id, double monto, String fecha, String descripcion, String ubicacion, String metodoPago) {
        this.id = id;
        this.monto = monto;
        this.fecha = new Date(fecha);
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.metodoPago = metodoPago;
    }

    // Registra una nueva transacción
    public void registrarTransaccion() {
        // Implementación del método
    }

    // Edita una transacción existente
    public void editarTransaccion() {
        // Implementación del método
    }

    // Elimina una transacción existente
    public void eliminarTransaccion() {
        // Implementación del método
    }

    // Obtiene los detalles de una transacción
    public void obtenerDetallesTransaccion() {
        // Implementación del método
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

    public Subcategory getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(Subcategory subcategoria) {
        this.subcategoria = subcategoria;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}
