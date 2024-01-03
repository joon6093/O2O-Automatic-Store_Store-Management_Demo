package com.SJY.O2O_Automatic_Store_System_Demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi SignApi() {
        return GroupedOpenApi.builder()
                .group("sign")
                .pathsToMatch("/api/sign-up","/api/sign-in","/api/refresh-token")
                .build();
    }

    @Bean
    public GroupedOpenApi MemberApi() {
        return GroupedOpenApi.builder()
                .group("member")
                .pathsToMatch("/api/members/**")
                .build();
    }

    @Bean
    public GroupedOpenApi CategoryApi() {
        return GroupedOpenApi.builder()
                .group("category")
                .pathsToMatch("/api/categories/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("O2O Automatic Store System Demo")
                        .version("1.0")
                        .description("O2O Automatic Store System Demo REST API Documentation"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        ));
    }
}