package ru.user.example.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Класс конфигурации Swagger.
 * Swagger - инструмент для документирования и
 * тестирования API.
 *
 * @info информация о API
 * @title заголовок
 * @description описание
 * @version версия
 *
 */
@OpenAPIDefinition(
        info = @Info(
                title = "User Service API",
                description = "API для управления пользователями",
                version = "1.0.0"
        )
)
@Configuration
@RestController
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                        .servers(List.of(
                                new Server()
                                        .url("http://localhost:8081")
                                        .description("Локальный сервер разработки")
                        ))
                        .info(new io.swagger.v3.oas.models.info.Info()
                                .title("User Service API")
                                .version("1.0.0")
                                .description("REST API для взаимодействия с сервисом пользователей")
                        );
        }

        @GetMapping(value = "/openapi.yaml", produces = "application/yaml")
        public Resource getOpenApiYaml() {
                return new ClassPathResource("static/openapi.yaml");
        }
}