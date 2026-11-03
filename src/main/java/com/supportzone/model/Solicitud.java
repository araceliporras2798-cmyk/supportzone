package com.supportzone.model;

import java.time.LocalDateTime;

/**
 * Solicitud de soporte técnico registrada por un {@link Cliente}
 * y atendida, opcionalmente, por un {@link Tecnico}.
 */
public class Solicitud {

    private Long id;
    private Cliente cliente;
    private Tecnico tecnico;
    private String descripcion;
    private Prioridad prioridad;
    private EstadoSolicitud estado;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaActualizacion;

    public Solicitud() {
    }

    public Solicitud(Long id, Cliente cliente, Tecnico tecnico, String descripcion,
                     Prioridad prioridad, EstadoSolicitud estado) {
        this.id = id;
        this.cliente = cliente;
        this.tecnico = tecnico;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
