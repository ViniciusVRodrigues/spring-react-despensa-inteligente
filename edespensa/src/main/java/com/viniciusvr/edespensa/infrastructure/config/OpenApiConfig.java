package com.viniciusvr.edespensa.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do OpenAPI/Swagger para documentação da API.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Despensa Inteligente")
                        .version("1.0.0")
                        .description("API para gerenciamento inteligente de despensa. " +
                                "Controla produtos, validade, quantidade e gera recomendações " +
                                "automáticas de lista de compras.")
                        .contact(new Contact()
                                .name("Vinícius VR")
                                .email("contato@viniciusvr.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
