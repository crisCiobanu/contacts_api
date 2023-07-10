package tech.cristianciobanu.contactsapi.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI myOpenApi(){
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8000");
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setEmail("ciobanucristian.cc@gmail.com");
        contact.setName("Cristian Ciobanu");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Contact Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage contacts and skills.")
                .license(mitLicense);
        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}
