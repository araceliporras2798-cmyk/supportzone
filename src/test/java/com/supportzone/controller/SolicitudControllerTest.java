package com.supportzone.controller;

import com.supportzone.exception.GlobalExceptionHandler;
import com.supportzone.service.ClienteServiceImpl;
import com.supportzone.service.SolicitudServiceImpl;
import com.supportzone.service.TecnicoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas funcionales de los endpoints (equivalentes a las de Postman),
 * incluidos los casos de error del manejador global.
 */
class SolicitudControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        SolicitudServiceImpl service =
                new SolicitudServiceImpl(new ClienteServiceImpl(), new TecnicoServiceImpl());
        mockMvc = MockMvcBuilders.standaloneSetup(new SolicitudController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getListaDevuelve200() throws Exception {
        mockMvc.perform(get("/api/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getPorIdInexistenteDevuelve404ConMensaje() throws Exception {
        mockMvc.perform(get("/api/solicitudes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensaje").value("Solicitud con ID 99 no existe"));
    }

    @Test
    void getConIdNoNumericoDevuelve400() throws Exception {
        mockMvc.perform(get("/api/solicitudes/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postValidoDevuelve201ConLocation() throws Exception {
        String json = """
                {"clienteId": 1, "descripcion": "Correo corporativo no sincroniza", "prioridad": "BAJA"}
                """;
        mockMvc.perform(post("/api/solicitudes").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void postInvalidoDevuelve400ConDetalles() throws Exception {
        String json = """
                {"descripcion": "", "prioridad": "ALTA"}
                """;
        mockMvc.perform(post("/api/solicitudes").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles.clienteId").exists())
                .andExpect(jsonPath("$.detalles.descripcion").exists());
    }

    @Test
    void postConPrioridadNoPermitidaDevuelve400() throws Exception {
        String json = """
                {"clienteId": 1, "descripcion": "Prioridad incorrecta de prueba", "prioridad": "URGENTE"}
                """;
        mockMvc.perform(post("/api/solicitudes").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putActualizaYDevuelve200() throws Exception {
        String json = """
                {"clienteId": 1, "tecnicoId": 2, "descripcion": "Impresora revisada y en reparación",
                 "prioridad": "ALTA", "estado": "EN_PROCESO"}
                """;
        mockMvc.perform(put("/api/solicitudes/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tecnico.id").value(2))
                .andExpect(jsonPath("$.estado").value("EN_PROCESO"));
    }

    @Test
    void patchEnProcesoSinTecnicoDevuelve409() throws Exception {
        mockMvc.perform(patch("/api/solicitudes/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"estado\": \"EN_PROCESO\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteDevuelve204YLuego404() throws Exception {
        mockMvc.perform(delete("/api/solicitudes/1"))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/solicitudes/1"))
                .andExpect(status().isNotFound());
    }
}
