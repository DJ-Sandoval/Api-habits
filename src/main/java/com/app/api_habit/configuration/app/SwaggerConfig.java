package com.app.api_habit.configuration.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de segumiento de habitos",
                version = "1.0",
                description = "Documentación de la API para la correcta gestion y segumiento de habitos",
                termsOfService = "www.misitio.com/terminos_y_condiciones",
                contact = @Contact(
                        name = "DevSandoval",
                        url = "https://www.misitio.com",
                        email = "contacto@misitio.com"
                ),
                license = @License(
                        name = "Licencia de Uso Estándar",
                        url = "www.misitio.com/licencia"
                )
        ),
        servers = {
                @Server(
                        description = "Servidor de Desarrollo",
                        url = "https://miraculous-tenderness-production.up.railway.app"
                ),
                @Server(
                        description = "Servidor de Producción",
                        url = "https://miraculous-tenderness-production.up.railway.app"
                )
        },
        security = @SecurityRequirement(
                name = "Security Token"
        )
)
@SecurityScheme(
        name = "Security Token",
        description = "Access Token For My API",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}