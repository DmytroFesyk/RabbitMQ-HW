package cm.example.rabbitmqhw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class RabbitMqListenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqListenerApplication.class, args);
    }
}
