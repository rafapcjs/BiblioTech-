package com.bookLibrary.rafapcjs.commons.utils;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * Configuración de Swagger para la documentación de la API de la biblioteca.
 *
 * <p>
 * Esta configuración muestra el nombre "Rafael Alfonso Corredore Gambin" en la UI de Swagger,
 * incluyendo la información de contacto, términos de servicio, licencia y los servidores de la API.
 * </p>
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Biblioteca API - Rafael Alfonso Corredor Gambin",
                description = "API REST Backend para la gestión de una biblioteca.",
                termsOfService = "rafaelcorredorgambin1@gmail.com",
                version = "1.0.0",
                contact = @Contact(
                        name = "Rafael Alfonso Corredore Gambin",
                        url = "https://www.linkedin.com/in/rafael-alfonso-corredor-gamb%C3%ADn-67a329274/",
                        email = "rafaelcorredorgambin1@gmail.com"
                ),
                license = @License(
                        name = "Unicordoba Análisis y Diseño de Sistemas Informáticos",
                        url = "https://www.linkedin.com/in/rafael-alfonso-corredor-gamb%C3%ADn-67a329274/"
                )
        ),
        servers = {
                @Server(
                        description = "Servidor de Desarrollo",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Servidor de Producción",
                        url = "http://biblioteca.example.com"
                )
        }
)
public class SwaggerConfig {
    // Configuración para Swagger/OpenAPI.
    // No es necesario implementar métodos adicionales.
}