package com.supportzone.controller;

import com.supportzone.dto.ClienteDTO;
import com.supportzone.model.Cliente;
import com.supportzone.model.Solicitud;
import com.supportzone.service.ClienteService;
import com.supportzone.service.SolicitudService;
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
 * Endpoints de clientes, incluida la ruta jerárquica
 * {@code /api/clientes/{id}/solicitudes}.
 */
@Tag(name = "Clientes", description = "Clientes que registran solicitudes")
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final SolicitudService solicitudService;

    public ClienteController(ClienteService clienteService, SolicitudService solicitudService) {
        this.clienteService = clienteService;
        this.solicitudService = solicitudService;
    }

    @Operation(summary = "Listar clientes")
    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @Operation(summary = "Buscar cliente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @Operation(summary = "Registrar cliente")
    @PostMapping
    public ResponseEntity<Cliente> registrar(@Valid @RequestBody ClienteDTO dto) {
        Cliente nuevo = clienteService.registrar(dto);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevo.getId())
                .toUri();
        return ResponseEntity.created(ubicacion).body(nuevo);
    }

    @Operation(summary = "Listar las solicitudes de un cliente")
    @GetMapping("/{id}/solicitudes")
    public ResponseEntity<List<Solicitud>> listarSolicitudes(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudService.listarPorCliente(id));
    }
}
