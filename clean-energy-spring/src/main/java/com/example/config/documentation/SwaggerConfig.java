package com.example.config.documentation;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .addOpenApiCustomiser(this::customizeOpenApi)
                .build();
    }

    private void customizeOpenApi(OpenAPI openApi) {
        openApi.info(new Info().title("API Documentation")
                               .version("v1")
                               .description("Documentation of API endpoints"));
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
        openApi.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
               .components(new io.swagger.v3.oas.models.Components()
               .addSecuritySchemes("bearerAuth", securityScheme));
    }

}
