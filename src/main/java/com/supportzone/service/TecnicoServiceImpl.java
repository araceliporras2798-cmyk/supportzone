package com.supportzone.service;

import com.supportzone.dto.TecnicoDTO;
import com.supportzone.exception.RecursoNoEncontradoException;
import com.supportzone.model.Tecnico;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementación en memoria de {@link TecnicoService} usando un Map (ID → Tecnico).
 */
@Service
public class TecnicoServiceImpl implements TecnicoService {

    private final Map<Long, Tecnico> tecnicos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(1);

    /** Carga datos de ejemplo al iniciar. */
    public TecnicoServiceImpl() {
        guardar(new Tecnico(null, "Luis Ramírez", "Redes", "lramirez@supportzone.pe"));
        guardar(new Tecnico(null, "María Torres", "Hardware", "mtorres@supportzone.pe"));
    }

    @Override
    public List<Tecnico> listarTodos() {
        return tecnicos.values().stream()
                .sorted(Comparator.comparing(Tecnico::getId))
                .toList();
    }

    @Override
    public Tecnico buscarPorId(Long id) {
        Tecnico tecnico = tecnicos.get(id);
        if (tecnico == null) {
            throw new RecursoNoEncontradoException("Técnico", id);
        }
        return tecnico;
    }

    @Override
    public Tecnico registrar(TecnicoDTO dto) {
        return guardar(new Tecnico(null, dto.getNombre(), dto.getEspecialidad(), dto.getCorreo()));
    }

    private Tecnico guardar(Tecnico tecnico) {
        tecnico.setId(secuencia.getAndIncrement());
        tecnicos.put(tecnico.getId(), tecnico);
        return tecnico;
    }
}
