package us.cloud.teachme.forum.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

 @Value("${base}")
  private String BASE;

  @Bean
  OpenAPI customOpenAPI() {
    return new OpenAPI()
        .servers(List.of(new Server().url(BASE)))
        .info(new Info().title("Teachme Forum Service API").version("1.0"));
  }

}
