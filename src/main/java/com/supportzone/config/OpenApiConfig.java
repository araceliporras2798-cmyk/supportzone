package com.supportzone.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Información general que muestra Swagger UI en /swagger-ui.html.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI supportZoneOpenApi() {
        return new OpenAPI().info(new Info()
                .title("SupportZone API")
                .version("1.0.0")
                .description("API REST para registrar, consultar, actualizar y eliminar "
                        + "solicitudes de soporte técnico. Datos almacenados en memoria."));
    }
}
