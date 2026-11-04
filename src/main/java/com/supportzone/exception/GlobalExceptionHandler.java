package com.supportzone.exception;

import com.supportzone.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejo centralizado de excepciones. Todas las respuestas de error
 * tienen la misma estructura ({@link ErrorResponse}).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 400: errores de @Valid (campos vacíos, formato incorrecto, etc.). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarErroresValidacion(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> detalles = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                detalles.putIfAbsent(error.getField(), error.getDefaultMessage()));

        return construir(HttpStatus.BAD_REQUEST,
                "Los datos enviados no son válidos", request, detalles);
    }

    /** 400: JSON mal escrito o valor no permitido en prioridad/estado. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarJsonInvalido(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        return construir(HttpStatus.BAD_REQUEST,
                "El cuerpo de la petición no es un JSON válido o contiene valores no permitidos. "
                        + "Prioridad: ALTA, MEDIA, BAJA. Estado: PENDIENTE, EN_PROCESO, RESUELTA",
                request, Map.of());
    }

    /** 400: parámetro de ruta con tipo incorrecto, por ejemplo /api/solicitudes/abc. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarTipoIncorrecto(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String mensaje = "El valor '" + ex.getValue() + "' no es válido para el parámetro '"
                + ex.getName() + "'";
        return construir(HttpStatus.BAD_REQUEST, mensaje, request, Map.of());
    }

    /** 404: la solicitud, cliente o técnico no existe. */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrado(
            RecursoNoEncontradoException ex, HttpServletRequest request) {

        return construir(HttpStatus.NOT_FOUND, ex.getMessage(), request, Map.of());
    }

    /** 404: la ruta no existe, por ejemplo /api/xyz. */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> manejarRutaInexistente(
            NoResourceFoundException ex, HttpServletRequest request) {

        return construir(HttpStatus.NOT_FOUND, "La ruta solicitada no existe", request, Map.of());
    }

    /** 405: método HTTP no permitido en la ruta. */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> manejarMetodoNoPermitido(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {

        return construir(HttpStatus.METHOD_NOT_ALLOWED,
                "El método " + ex.getMethod() + " no está permitido en esta ruta", request, Map.of());
    }

    /** 415: el cuerpo no se envió como JSON. */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> manejarTipoContenido(
            HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {

        return construir(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "El cuerpo debe enviarse como application/json", request, Map.of());
    }

    /** 409: la operación incumple una regla del negocio. */
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponse> manejarReglaNegocio(
            ReglaNegocioException ex, HttpServletRequest request) {

        return construir(HttpStatus.CONFLICT, ex.getMessage(), request, Map.of());
    }

    /** 500: cualquier error no previsto. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErrorGeneral(
            Exception ex, HttpServletRequest request) {

        return construir(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ha ocurrido un error interno en el servidor", request, Map.of());
    }

    private ResponseEntity<ErrorResponse> construir(HttpStatus status, String mensaje,
                                                    HttpServletRequest request,
                                                    Map<String, String> detalles) {
        ErrorResponse cuerpo = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensaje,
                request.getRequestURI(),
                detalles);
        return ResponseEntity.status(status).body(cuerpo);
    }
}
