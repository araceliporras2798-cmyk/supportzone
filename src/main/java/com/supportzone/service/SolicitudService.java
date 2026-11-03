package com.supportzone.service;

import com.supportzone.model.Solicitud;
import java.util.List;

public interface SolicitudService {

    List<Solicitud> listarTodas();

    Solicitud buscarPorId(Long id);

    Solicitud registrar(Solicitud solicitud);

    Solicitud actualizar(Long id, Solicitud solicitud);

    void eliminar(Long id);
} 