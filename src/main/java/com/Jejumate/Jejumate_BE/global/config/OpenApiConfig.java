package com.Jejumate.Jejumate_BE.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        servers = {
                @Server(url = "https://jejumate.store", description = "배포 서버 (HTTPS)"),

                @Server(url = "http://localhost:8080", description = "로컬 서버")
        }
)
public class OpenApiConfig {
}
