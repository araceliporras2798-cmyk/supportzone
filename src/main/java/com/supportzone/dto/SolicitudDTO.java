package com.supportzone.dto;

import com.supportzone.model.EstadoSolicitud;
import com.supportzone.model.Prioridad;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Datos de entrada para registrar (POST) o actualizar (PUT) una solicitud.
 */
@Schema(description = "Datos para registrar o actualizar una solicitud de soporte")
public class SolicitudDTO {

    @Schema(description = "ID del cliente que reporta el problema", example = "1")
    @NotNull(message = "El ID del cliente es obligatorio")
    @Positive(message = "El ID del cliente debe ser mayor que 0")
    private Long clienteId;

    @Schema(description = "ID del técnico asignado (opcional)", example = "1")
    @Positive(message = "El ID del técnico debe ser mayor que 0")
    private Long tecnicoId;

    @Schema(description = "Detalle del problema", example = "La impresora de recepción no imprime")
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
    private String descripcion;

    @Schema(description = "Prioridad de la solicitud", example = "ALTA")
    @NotNull(message = "La prioridad es obligatoria")
    private Prioridad prioridad;

    @Schema(description = "Estado de la solicitud. Si se omite al registrar, queda PENDIENTE", example = "PENDIENTE")
    private EstadoSolicitud estado;

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Long tecnicoId) {
        this.tecnicoId = tecnicoId;
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
}
