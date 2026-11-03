package com.supportzone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SolicitudDTO {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar 100 caracteres")
    private String cliente;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "La prioridad es obligatoria")
    @Pattern(regexp = "ALTA|MEDIA|BAJA",
            message = "La prioridad debe ser ALTA, MEDIA o BAJA")
    private String prioridad;

    @NotNull(message = "El estado es obligatorio")
    @Pattern(regexp = "PENDIENTE|EN_PROCESO|RESUELTA",
            message = "El estado debe ser PENDIENTE, EN_PROCESO o RESUELTA")
    private String estado;

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