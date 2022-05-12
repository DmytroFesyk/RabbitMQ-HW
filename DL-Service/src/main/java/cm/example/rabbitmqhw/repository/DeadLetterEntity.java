package cm.example.rabbitmqhw.repository;

import cm.example.rabbitmqhw.MyMessage;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rabbitmq-dead-letter")
@Data
public class DeadLetterEntity {
    private String messageId;
    private MyMessage message;
    private String routingKey;
    @Id
    private String id;
}
