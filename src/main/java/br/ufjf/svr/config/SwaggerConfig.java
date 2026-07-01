package br.ufjf.svr.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API SVR")
                        .description("API do SVR (Sistema de Venda de Roupas), desenvolvida para gerenciar as operações de uma loja de roupas com vendas online e presenciais. O sistema permite que colaboradores registrem vendas realizadas na loja física e cadastrem clientes, enquanto administradores têm acesso completo à gestão do sistema. A API cobre funcionalidades como catálogo de produtos, carrinho de compras, pedidos, estoque, fornecedores e métodos de pagamento.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Diego Pereira Betti e Isaac Nascimento Soares")
                                .url("https://github.com/IsaacNSoares/back-end-svr")
                                .email("diego.pereira@estudante.ufjf.br | isaac.nascimento@estudando.ufjf.br")
                        )
                )

                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                );
    }
}