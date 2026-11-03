package com.supportzone.service;

import com.supportzone.dto.ClienteDTO;
import com.supportzone.model.Cliente;

import java.util.List;

/**
 * Operaciones de negocio sobre clientes.
 */
public interface ClienteService {

    List<Cliente> listarTodos();

    /** @throws com.supportzone.exception.RecursoNoEncontradoException si no existe */
    Cliente buscarPorId(Long id);

    /** @throws com.supportzone.exception.ReglaNegocioException si el correo ya está registrado */
    Cliente registrar(ClienteDTO dto);
}
