package financial.assistant;

import javafx.application.Application;
import org.h2.tools.Server;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.sql.SQLException;

@SpringBootApplication
@EnableAutoConfiguration
public class FinancialAssistantSpringEntrypoint {

    public static void main(String [] args) {
        Application.launch(FinancialAssistantApplication.class, args);
    }

    @Bean
    @ConditionalOnProperty(prefix = "environment", name = "dev", havingValue = "true")
    public Server h2Server() throws SQLException {
        return Server.createWebServer().start();
    }
}
