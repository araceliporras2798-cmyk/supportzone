package com.supportzone.controller;

import com.supportzone.dto.SolicitudDTO;
import com.supportzone.model.Solicitud;
import com.supportzone.service.SolicitudService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public ResponseEntity<List<Solicitud>> listarTodas() {
        return ResponseEntity.ok(solicitudService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> buscarPorId(@PathVariable Long id) {
        Solicitud solicitud = solicitudService.buscarPorId(id);

        if (solicitud == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(solicitud);
    }

    @PostMapping
    public ResponseEntity<Solicitud> registrar(
            @Valid @RequestBody SolicitudDTO dto) {

        Solicitud solicitud = convertirADominio(dto);
        Solicitud nueva = solicitudService.registrar(solicitud);

        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Solicitud> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SolicitudDTO dto) {

        Solicitud solicitud = convertirADominio(dto);
        Solicitud actualizada = solicitudService.actualizar(id, solicitud);

        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Solicitud existente = solicitudService.buscarPorId(id);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        solicitudService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    private Solicitud convertirADominio(SolicitudDTO dto) {
        Solicitud solicitud = new Solicitud();

        solicitud.setCliente(dto.getCliente());
        solicitud.setDescripcion(dto.getDescripcion());
        solicitud.setPrioridad(dto.getPrioridad());
        solicitud.setEstado(dto.getEstado());

        return solicitud;
    }
} 