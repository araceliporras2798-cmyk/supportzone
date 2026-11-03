package com.supportzone.service;

import com.supportzone.dto.SolicitudDTO;
import com.supportzone.exception.RecursoNoEncontradoException;
import com.supportzone.exception.ReglaNegocioException;
import com.supportzone.model.EstadoSolicitud;
import com.supportzone.model.Prioridad;
import com.supportzone.model.Solicitud;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas unitarias de la lógica de negocio y del almacenamiento en memoria.
 * Parte de 2 solicitudes, 2 clientes y 2 técnicos de ejemplo.
 */
class SolicitudServiceImplTest {

    private SolicitudServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new SolicitudServiceImpl(new ClienteServiceImpl(), new TecnicoServiceImpl());
    }

    private SolicitudDTO dto(Long clienteId, Long tecnicoId, EstadoSolicitud estado) {
        SolicitudDTO dto = new SolicitudDTO();
        dto.setClienteId(clienteId);
        dto.setTecnicoId(tecnicoId);
        dto.setDescripcion("Laptop no enciende después de actualizar");
        dto.setPrioridad(Prioridad.ALTA);
        dto.setEstado(estado);
        return dto;
    }

    @Test
    void registrarAsignaIdYEstadoPendientePorDefecto() {
        Solicitud nueva = service.registrar(dto(1L, null, null));

        assertEquals(3L, nueva.getId().longValue());
        assertEquals(EstadoSolicitud.PENDIENTE, nueva.getEstado());
        assertNotNull(nueva.getFechaRegistro());
        assertEquals(3, service.listar(null, null).size());
    }

    @Test
    void registrarConClienteInexistenteLanza404() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.registrar(dto(99L, null, null)));
    }

    @Test
    void enProcesoSinTecnicoIncumpleReglaDeNegocio() {
        assertThrows(ReglaNegocioException.class,
                () -> service.registrar(dto(1L, null, EstadoSolicitud.EN_PROCESO)));
    }

    @Test
    void actualizarModificaLosDatos() {
        Solicitud actualizada = service.actualizar(1L, dto(2L, 2L, EstadoSolicitud.RESUELTA));

        assertEquals(2L, actualizada.getCliente().getId().longValue());
        assertEquals(EstadoSolicitud.RESUELTA, actualizada.getEstado());
        assertNotNull(actualizada.getFechaActualizacion());
    }

    @Test
    void listarFiltraPorEstado() {
        assertEquals(1, service.listar(EstadoSolicitud.PENDIENTE, null).size());
        assertEquals(1, service.listar(null, Prioridad.ALTA).size());
    }

    @Test
    void eliminarQuitaLaSolicitudYLuegoLanza404() {
        service.eliminar(1L);

        assertThrows(RecursoNoEncontradoException.class, () -> service.buscarPorId(1L));
        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(1L));
    }
}
