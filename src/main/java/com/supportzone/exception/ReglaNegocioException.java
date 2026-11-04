package com.supportzone.exception;

/**
 * Se lanza cuando una operación incumple una regla del negocio
 * (por ejemplo, poner EN_PROCESO una solicitud sin técnico asignado).
 * El {@link GlobalExceptionHandler} la convierte en una respuesta 409.
 */
public class ReglaNegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
