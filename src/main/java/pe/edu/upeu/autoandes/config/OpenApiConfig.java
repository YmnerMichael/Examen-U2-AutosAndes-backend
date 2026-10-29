package pe.edu.upeu.autoandes.config;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.security.*;
@org.springframework.context.annotation.Configuration
public class OpenApiConfig {
@org.springframework.context.annotation.Bean public OpenAPI api() { return new OpenAPI().info(new io.swagger.v3.oas.models.info.Info().title("AutoAndes API").version("v1")).components(new Components().addSecuritySchemes("bearerAuth",new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))).addSecurityItem(new SecurityRequirement().addList("bearerAuth")); }
}
