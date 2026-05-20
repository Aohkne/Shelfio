package com.huukhoa.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.List;

@Configuration
public class OpenApiConfig {
    private static final Logger log = LoggerFactory.getLogger(OpenApiConfig.class);
    @Value("${server.port}")
    String port;
    @Value("${server.servlet.context-path}")
    String contextPath;

    @Bean
    public OpenAPI openAPI(@Value("${open.api.title}") String title,
                           @Value("${open.api.version}") String version,
                           @Value("${open.api.description}") String description,
                           @Value("${open.api.server}") String server,
                           @Value("${open.api.serverDes}") String serverDes) {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .version(version)
                        .description(description)
                        .license(new License().name("Aohkne").url("https://github.com/Aohkne")))
                .servers(List.of(
                        new Server()
                                .url(server)
                                .description(serverDes)))
                // Global security requirement — shows Authorize button on all endpoints
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .description("Enter JWT access token")));
    }

    @Bean
    public GroupedOpenApi authGroup() {
        return GroupedOpenApi.builder()
                .group("Authentication")
                .pathsToMatch("/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi bookGroup() {
        return GroupedOpenApi.builder()
                .group("Book")
                .pathsToMatch("/api/books/**")
                .build();
    }

    @Bean
    public GroupedOpenApi memberGroup() {
        return GroupedOpenApi.builder()
                .group("Member")
                .pathsToMatch("/api/members/**")
                .build();
    }

    @Bean
    public GroupedOpenApi borrowingGroup() {
        return GroupedOpenApi.builder()
                .group("Borrowing")
                .pathsToMatch("/api/borrowings/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminGroup() {
        return GroupedOpenApi.builder()
                .group("Admin")
                .pathsToMatch("/api/admin/**")
                .build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        String swaggerUrl = String.format("http://localhost:%s%s/swagger-ui/index.html", port, contextPath);
        log.info("\n----------------------------------------------------------\n\t" +
                "Swagger UI is available at:\n\t" +
                "URL: {}\n" +
                "----------------------------------------------------------", swaggerUrl);
    }
}
