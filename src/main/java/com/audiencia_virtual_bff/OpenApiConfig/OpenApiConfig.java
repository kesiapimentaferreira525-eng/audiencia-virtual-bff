package com.audiencia_virtual_bff.OpenApiConfig;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI audienciaVirtualBffOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BFF Audiência Virtual")
                        .description("Gateway entre o frontend e a API de domínio de audiências virtuais. "
                                + "O BFF encaminha Authorization e X-Correlation-Id para o backend.")
                        .version("v1"))
                .servers(List.of(new Server()
                        .url("http://localhost:8084")
                        .description("Ambiente local")));
    }
}