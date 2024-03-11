package neo.neobis_auth_project.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Neo AuthProject",
                        email = "shmanovermek@gmail.com",
                        url = "https://neobis-front-auth-kappa.vercel.app/"
                ),
                title = "OpenAPI specification - yermek",
                description = "OpenApi documentation Auth Project",
                version = "0.0.1"
        ),
        servers = {
                @Server(
                        description = "Railway App",
                        url = "https://neobis-auth-project-production.up.railway.app" // todo: change
                )
        }
)
public class OpenApiConfig {
}