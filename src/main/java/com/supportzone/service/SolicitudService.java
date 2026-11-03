package com.supportzone.service;

import com.supportzone.dto.SolicitudDTO;
import com.supportzone.model.EstadoSolicitud;
import com.supportzone.model.Prioridad;
import com.supportzone.model.Solicitud;

import java.util.List;

/**
 * Operaciones de negocio sobre solicitudes de soporte técnico.
 */
public interface SolicitudService {

    /**
     * Lista las solicitudes ordenadas por ID.
     *
     * @param estado    filtro opcional (null = todos)
     * @param prioridad filtro opcional (null = todas)
     */
    List<Solicitud> listar(EstadoSolicitud estado, Prioridad prioridad);

    /** @throws com.supportzone.exception.RecursoNoEncontradoException si no existe */
    Solicitud buscarPorId(Long id);

    /** Solicitudes de un cliente; lanza 404 si el cliente no existe. */
    List<Solicitud> listarPorCliente(Long clienteId);

    Solicitud registrar(SolicitudDTO dto);

    Solicitud actualizar(Long id, SolicitudDTO dto);

    Solicitud cambiarEstado(Long id, EstadoSolicitud estado);

    void eliminar(Long id);
}
