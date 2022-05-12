package cm.example.rabbitmqhw.rabbit;

import cm.example.rabbitmqhw.MyMessage;
import cm.example.rabbitmqhw.service.DeadLetterStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import static cm.example.rabbitmqhw.config.Config.CONVERTER_BEAN_NAME;
import static cm.example.rabbitmqhw.config.Config.DL_ROUTING_KEY;
import static cm.example.rabbitmqhw.config.Config.FAILED_ROUTING_KEY;


@Service
@Slf4j
@RequiredArgsConstructor
public class DeadLetterListener {

    private final DeadLetterStorageService deadLetterStorageService;

    @RabbitListener(
            queues = DL_ROUTING_KEY,
            messageConverter = CONVERTER_BEAN_NAME
    )
    public void listenDeadLetterQueue(
            MyMessage message,
            @Header(AmqpHeaders.MESSAGE_ID) String messageId,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        deadLetterStorageService.save(message,messageId,routingKey);
        log.info("Dead message: " + messageId + " saved to storage");
    }

    @RabbitListener(
            queues = FAILED_ROUTING_KEY,
            messageConverter = CONVERTER_BEAN_NAME
    )
    public void listenFailedQueue(
            MyMessage message,
            @Header(AmqpHeaders.MESSAGE_ID) String messageId,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        deadLetterStorageService.save(message,messageId,routingKey);
        log.info("Failed message: " + messageId + " saved to storage");
    }
}
