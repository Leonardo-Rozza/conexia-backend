package com.conexia.config.filter;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(title = "Conexia",
                description = "API REST desarrollada con Spring Boot para la gestión de usuarios, autenticación y servicios internos de la plataforma Conexia. " +
                "\nIncluye endpoints protegidos con Spring Security y soporte para documentación interactiva mediante Swagger UI.",
                version = "1.0.0",
                termsOfService = "conexia.com/terminos-y-servicios",
                contact = @Contact(
                        name = "Leonardo Rozza",
                        url = "www.leonardo-rozza.com/contact",
                        email = "leonardorozza.dev@hotmail.com"
                ),
                license = @License(
                        name = "Standard Software Use License for Conexia",
                        url = "www.leoanardo-rozza.com/license"
                )
        ),
        servers = {
                @Server(
                        description = "DEV SERVER",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "PROD SERVER",
                        url = "https://conexia.com"
                )
        },
        security = @SecurityRequirement(
                name = "Security Token"
        )
)
@SecurityScheme(
        name = "Security Token",
        description = "Access token for Conexia",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER, // Tambien se pueden guardar en las COOKIES
        scheme = "bearer",
        bearerFormat = "JWT"
)
@Configuration
public class SwaggerConfig {
}
