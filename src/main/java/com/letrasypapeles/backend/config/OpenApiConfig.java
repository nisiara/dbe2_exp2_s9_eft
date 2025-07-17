package com.letrasypapeles.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI openAPIConfig(){
    return new OpenAPI().info(new Info()
      .title("Letras y Papeles API")
      .description("Endpoints para la implementación del proyecto Letras y Papeles")
      .version("1.0.0")
    );
  }
}
