package com.supportzone.service;

import com.supportzone.model.Solicitud;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class SolicitudServiceImpl implements SolicitudService {

    private final List<Solicitud> solicitudes = new ArrayList<>();
    private final AtomicLong contador = new AtomicLong(1);

    @Override
    public List<Solicitud> listarTodas() {
        return solicitudes;
    }

    @Override
    public Solicitud buscarPorId(Long id) {
        return solicitudes.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Solicitud registrar(Solicitud solicitud) {
        solicitud.setId(contador.getAndIncrement());
        solicitudes.add(solicitud);
        return solicitud;
    }

    @Override
    public Solicitud actualizar(Long id, Solicitud solicitudActualizada) {
        Solicitud existente = buscarPorId(id);

        if (existente == null) {
            return null;
        }

        existente.setCliente(solicitudActualizada.getCliente());
        existente.setDescripcion(solicitudActualizada.getDescripcion());
        existente.setPrioridad(solicitudActualizada.getPrioridad());
        existente.setEstado(solicitudActualizada.getEstado());

        return existente;
    }

    @Override
    public void eliminar(Long id) {
        solicitudes.removeIf(s -> s.getId().equals(id));
    }
} 