package com.supportzone.controller;

import com.supportzone.dto.ErrorResponse;
import com.supportzone.dto.EstadoDTO;
import com.supportzone.dto.SolicitudDTO;
import com.supportzone.model.EstadoSolicitud;
import com.supportzone.model.Prioridad;
import com.supportzone.model.Solicitud;
import com.supportzone.service.SolicitudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Endpoints CRUD de solicitudes de soporte técnico.
 * Los errores (400, 404, 409) los responde {@code GlobalExceptionHandler}.
 */
@Tag(name = "Solicitudes", description = "Registro y atención de solicitudes de soporte técnico")
@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @Operation(summary = "Listar solicitudes", description = "Permite filtrar por estado y/o prioridad")
    @ApiResponse(responseCode = "200", description = "Lista de solicitudes")
    @GetMapping
    public ResponseEntity<List<Solicitud>> listar(
            @Parameter(description = "Filtrar por estado") @RequestParam(required = false) EstadoSolicitud estado,
            @Parameter(description = "Filtrar por prioridad") @RequestParam(required = false) Prioridad prioridad) {
        return ResponseEntity.ok(solicitudService.listar(estado, prioridad));
    }

    @Operation(summary = "Buscar solicitud por ID")
    @ApiResponse(responseCode = "200", description = "Solicitud encontrada")
    @ApiResponse(responseCode = "404", description = "La solicitud no existe",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudService.buscarPorId(id));
    }

    @Operation(summary = "Registrar solicitud")
    @ApiResponse(responseCode = "201", description = "Solicitud registrada")
    @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Cliente o técnico no existe",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Estado EN_PROCESO/RESUELTA sin técnico",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<Solicitud> registrar(@Valid @RequestBody SolicitudDTO dto) {
        Solicitud nueva = solicitudService.registrar(dto);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nueva.getId())
                .toUri();
        return ResponseEntity.created(ubicacion).body(nueva);
    }

    @Operation(summary = "Actualizar solicitud completa")
    @ApiResponse(responseCode = "200", description = "Solicitud actualizada")
    @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Solicitud, cliente o técnico no existe",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<Solicitud> actualizar(@PathVariable Long id,
                                                @Valid @RequestBody SolicitudDTO dto) {
        return ResponseEntity.ok(solicitudService.actualizar(id, dto));
    }

    @Operation(summary = "Cambiar solo el estado de una solicitud")
    @ApiResponse(responseCode = "200", description = "Estado actualizado")
    @ApiResponse(responseCode = "409", description = "Estado EN_PROCESO/RESUELTA sin técnico",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Solicitud> cambiarEstado(@PathVariable Long id,
                                                   @Valid @RequestBody EstadoDTO dto) {
        return ResponseEntity.ok(solicitudService.cambiarEstado(id, dto.getEstado()));
    }

    @Operation(summary = "Eliminar solicitud")
    @ApiResponse(responseCode = "204", description = "Solicitud eliminada")
    @ApiResponse(responseCode = "404", description = "La solicitud no existe",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        solicitudService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
