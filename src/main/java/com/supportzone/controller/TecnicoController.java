package com.supportzone.controller;

import com.supportzone.dto.TecnicoDTO;
import com.supportzone.model.Tecnico;
import com.supportzone.service.TecnicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Endpoints de técnicos de soporte.
 */
@Tag(name = "Técnicos", description = "Técnicos que atienden las solicitudes")
@RestController
@RequestMapping("/api/tecnicos")
public class TecnicoController {

    private final TecnicoService tecnicoService;

    public TecnicoController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    @Operation(summary = "Listar técnicos")
    @GetMapping
    public ResponseEntity<List<Tecnico>> listar() {
        return ResponseEntity.ok(tecnicoService.listarTodos());
    }

    @Operation(summary = "Buscar técnico por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Tecnico> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tecnicoService.buscarPorId(id));
    }

    @Operation(summary = "Registrar técnico")
    @PostMapping
    public ResponseEntity<Tecnico> registrar(@Valid @RequestBody TecnicoDTO dto) {
        Tecnico nuevo = tecnicoService.registrar(dto);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevo.getId())
                .toUri();
        return ResponseEntity.created(ubicacion).body(nuevo);
    }
}
