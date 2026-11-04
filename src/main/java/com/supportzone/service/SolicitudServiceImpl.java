package com.supportzone.service;

import com.supportzone.dto.SolicitudDTO;
import com.supportzone.exception.RecursoNoEncontradoException;
import com.supportzone.exception.ReglaNegocioException;
import com.supportzone.model.Cliente;
import com.supportzone.model.EstadoSolicitud;
import com.supportzone.model.Prioridad;
import com.supportzone.model.Solicitud;
import com.supportzone.model.Tecnico;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementación en memoria de {@link SolicitudService}.
 * <p>
 * Las solicitudes se guardan en un {@link ConcurrentHashMap} (ID → Solicitud)
 * y los IDs se generan con un {@link AtomicLong}, simulando una tabla con
 * clave autoincremental. Los datos se pierden al detener la aplicación.
 * <p>
 * Reglas de negocio:
 * <ul>
 *     <li>El cliente debe existir; el técnico, si se envía, también.</li>
 *     <li>Una solicitud EN_PROCESO o RESUELTA debe tener técnico asignado.</li>
 *     <li>Si no se envía estado al registrar, queda PENDIENTE.</li>
 * </ul>
 */
@Service
public class SolicitudServiceImpl implements SolicitudService {

    private final Map<Long, Solicitud> solicitudes = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(1);

    private final ClienteService clienteService;
    private final TecnicoService tecnicoService;

    public SolicitudServiceImpl(ClienteService clienteService, TecnicoService tecnicoService) {
        this.clienteService = clienteService;
        this.tecnicoService = tecnicoService;
        cargarDatosIniciales();
    }

    @Override
    public List<Solicitud> listar(EstadoSolicitud estado, Prioridad prioridad) {
        return solicitudes.values().stream()
                .filter(s -> estado == null || s.getEstado() == estado)
                .filter(s -> prioridad == null || s.getPrioridad() == prioridad)
                .sorted(Comparator.comparing(Solicitud::getId))
                .toList();
    }

    @Override
    public Solicitud buscarPorId(Long id) {
        Solicitud solicitud = solicitudes.get(id);
        if (solicitud == null) {
            throw new RecursoNoEncontradoException("Solicitud", id);
        }
        return solicitud;
    }

    @Override
    public List<Solicitud> listarPorCliente(Long clienteId) {
        clienteService.buscarPorId(clienteId); // valida que el cliente exista
        return solicitudes.values().stream()
                .filter(s -> s.getCliente().getId().equals(clienteId))
                .sorted(Comparator.comparing(Solicitud::getId))
                .toList();
    }

    @Override
    public Solicitud registrar(SolicitudDTO dto) {
        return crear(dto);
    }

    private Solicitud crear(SolicitudDTO dto) {
        EstadoSolicitud estado = dto.getEstado() != null ? dto.getEstado() : EstadoSolicitud.PENDIENTE;
        Tecnico tecnico = obtenerTecnico(dto.getTecnicoId());
        validarTecnicoSegunEstado(estado, tecnico);

        Solicitud solicitud = new Solicitud(
                secuencia.getAndIncrement(),
                clienteService.buscarPorId(dto.getClienteId()),
                tecnico,
                dto.getDescripcion().trim(),
                dto.getPrioridad(),
                estado);
        solicitud.setFechaRegistro(LocalDateTime.now());

        solicitudes.put(solicitud.getId(), solicitud);
        return solicitud;
    }

    @Override
    public Solicitud actualizar(Long id, SolicitudDTO dto) {
        Solicitud existente = buscarPorId(id);

        Cliente cliente = clienteService.buscarPorId(dto.getClienteId());
        Tecnico tecnico = obtenerTecnico(dto.getTecnicoId());
        EstadoSolicitud estado = dto.getEstado() != null ? dto.getEstado() : existente.getEstado();
        validarTecnicoSegunEstado(estado, tecnico);

        existente.setCliente(cliente);
        existente.setTecnico(tecnico);
        existente.setDescripcion(dto.getDescripcion().trim());
        existente.setPrioridad(dto.getPrioridad());
        existente.setEstado(estado);
        existente.setFechaActualizacion(LocalDateTime.now());
        return existente;
    }

    @Override
    public Solicitud cambiarEstado(Long id, EstadoSolicitud estado) {
        Solicitud existente = buscarPorId(id);
        validarTecnicoSegunEstado(estado, existente.getTecnico());

        existente.setEstado(estado);
        existente.setFechaActualizacion(LocalDateTime.now());
        return existente;
    }

    @Override
    public void eliminar(Long id) {
        if (solicitudes.remove(id) == null) {
            throw new RecursoNoEncontradoException("Solicitud", id);
        }
    }

    private Tecnico obtenerTecnico(Long tecnicoId) {
        return tecnicoId == null ? null : tecnicoService.buscarPorId(tecnicoId);
    }

    private void validarTecnicoSegunEstado(EstadoSolicitud estado, Tecnico tecnico) {
        if (estado != EstadoSolicitud.PENDIENTE && tecnico == null) {
            throw new ReglaNegocioException(
                    "Una solicitud en estado " + estado + " debe tener un técnico asignado");
        }
    }

    /** Dos solicitudes de ejemplo para que la API no inicie vacía. */
    private void cargarDatosIniciales() {
        SolicitudDTO s1 = new SolicitudDTO();
        s1.setClienteId(1L);
        s1.setDescripcion("La impresora de recepción no imprime");
        s1.setPrioridad(Prioridad.MEDIA);
        crear(s1);

        SolicitudDTO s2 = new SolicitudDTO();
        s2.setClienteId(2L);
        s2.setTecnicoId(1L);
        s2.setDescripcion("Sin conexión a internet en el área de admisión");
        s2.setPrioridad(Prioridad.ALTA);
        s2.setEstado(EstadoSolicitud.EN_PROCESO);
        crear(s2);
    }
}
