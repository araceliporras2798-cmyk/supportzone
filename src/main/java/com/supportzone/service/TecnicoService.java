package com.supportzone.service;

import com.supportzone.dto.TecnicoDTO;
import com.supportzone.model.Tecnico;

import java.util.List;

/**
 * Operaciones de negocio sobre técnicos.
 */
public interface TecnicoService {

    List<Tecnico> listarTodos();

    /** @throws com.supportzone.exception.RecursoNoEncontradoException si no existe */
    Tecnico buscarPorId(Long id);

    Tecnico registrar(TecnicoDTO dto);
}
