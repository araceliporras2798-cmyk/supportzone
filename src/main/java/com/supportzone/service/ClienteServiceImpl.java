package com.supportzone.service;

import com.supportzone.dto.ClienteDTO;
import com.supportzone.exception.RecursoNoEncontradoException;
import com.supportzone.exception.ReglaNegocioException;
import com.supportzone.model.Cliente;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementación en memoria: los clientes se guardan en un Map (ID → Cliente)
 * que simula una tabla de base de datos mientras la aplicación está en ejecución.
 */
@Service
public class ClienteServiceImpl implements ClienteService {

    private final Map<Long, Cliente> clientes = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(1);

    /** Carga datos de ejemplo al iniciar. */
    public ClienteServiceImpl() {
        guardar(new Cliente(null, "Ferretería El Tornillo", "contacto@eltornillo.pe", "987654321"));
        guardar(new Cliente(null, "Clínica San Andrés", "sistemas@sanandres.pe", "912345678"));
    }

    @Override
    public List<Cliente> listarTodos() {
        return clientes.values().stream()
                .sorted(Comparator.comparing(Cliente::getId))
                .toList();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Cliente cliente = clientes.get(id);
        if (cliente == null) {
            throw new RecursoNoEncontradoException("Cliente", id);
        }
        return cliente;
    }

    @Override
    public Cliente registrar(ClienteDTO dto) {
        boolean correoRepetido = clientes.values().stream()
                .anyMatch(c -> c.getCorreo().equalsIgnoreCase(dto.getCorreo()));
        if (correoRepetido) {
            throw new ReglaNegocioException("Ya existe un cliente con el correo " + dto.getCorreo());
        }
        return guardar(new Cliente(null, dto.getNombre(), dto.getCorreo(), dto.getTelefono()));
    }

    private Cliente guardar(Cliente cliente) {
        cliente.setId(secuencia.getAndIncrement());
        clientes.put(cliente.getId(), cliente);
        return cliente;
    }
}
