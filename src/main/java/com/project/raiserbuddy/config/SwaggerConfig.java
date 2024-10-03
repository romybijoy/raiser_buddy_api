package com.project.raiserbuddy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

@Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI().info(new Info().title("Raiser Buddy Application")
                        .description("Backend APIs for Raiser Buddy app")
                        .version("v1.0.0")
                        .contact(new Contact().name("Romy Rose Jimmy")/*.url("https://sirajuddin135.github.io/")*/.email("romybijoy@gmail.com"))
                        .license(new License().name("License").url("/")))
                .externalDocs(new ExternalDocumentation().description("Raiser Buddy App Documentation")
                        .url("http://localhost:8085/swagger-ui/index.html"));
    }

}
