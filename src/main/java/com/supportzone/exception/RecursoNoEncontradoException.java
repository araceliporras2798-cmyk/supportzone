package com.supportzone.exception;

/**
 * Se lanza cuando se busca un recurso (solicitud, cliente o técnico) que no existe.
 * El {@link GlobalExceptionHandler} la convierte en una respuesta 404.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecursoNoEncontradoException(String recurso, Long id) {
        super(recurso + " con ID " + id + " no existe");
    }
}
