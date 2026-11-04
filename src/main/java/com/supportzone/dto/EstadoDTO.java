package com.supportzone.dto;

import com.supportzone.model.EstadoSolicitud;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Datos de entrada para cambiar solo el estado de una solicitud (PATCH).
 */
@Schema(description = "Nuevo estado de la solicitud")
public class EstadoDTO {

    @Schema(example = "EN_PROCESO")
    @NotNull(message = "El estado es obligatorio")
    private EstadoSolicitud estado;

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }
}
