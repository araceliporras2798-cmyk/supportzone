package com.supportzone.model;

public class Solicitud {

    private Long id;
    private String cliente;
    private String descripcion;
    private String prioridad;
    private String estado;

    public Solicitud() {
    }

    public Solicitud(Long id, String cliente, String descripcion,
                     String prioridad, String estado) {
        this.id = id;
        this.cliente = cliente;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
} 