package net.engineeringdigest.journalApp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration

@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(title = "Journal API", version = "1.0", description = "Journal Application APIs"),
        security = @SecurityRequirement(name = "bearerAuth")
)

//Authorize all apis by jwt
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Authorization header using Bearer scheme",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfiguration {

    @Bean
    public OpenAPI swaggerConfig() {
        OpenAPI openAPI = new OpenAPI();
//        openAPI.info(new Info()
//                .title("Journal App APIs")
//                .description("By Tushar"));
        //openAPI.servers(Arrays.asList(new Server().url("http:localhost:8080/journal").description("localhost")));

        return openAPI;
    }
}
