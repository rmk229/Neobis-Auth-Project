package neo.neobis_auth_project.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Neo-Auth-Project",
                        email = "nurmukhamedalymbaiuulu064@gmail.com",
                        url = "https://neobis-front-auth-mu.vercel.app"
                ),
                title = "Lorby API",
                description = "OpenApi documentation for Lorby Auth Project",
                version = "0.0.1"
        ),
        servers = {
                @Server(
                        description = "Railway App",
                        url = "https://neobis-auth-project.up.railway.app"
                )
        }
)
public class SwaggerConfig {
}