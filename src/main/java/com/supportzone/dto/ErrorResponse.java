package com.supportzone.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Estructura única para todas las respuestas de error de la API.
 *
 * @param timestamp fecha y hora del error
 * @param status    código HTTP
 * @param error     nombre del código HTTP
 * @param mensaje   explicación del error
 * @param path      ruta solicitada
 * @param detalles  errores por campo (solo en validaciones; vacío en otros casos)
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String mensaje,
        String path,
        Map<String, String> detalles) {
}
