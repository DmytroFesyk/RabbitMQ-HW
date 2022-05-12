package cm.example.rabbitmqhw.rabbit;

import cm.example.rabbitmqhw.MyMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import static cm.example.rabbitmqhw.config.Config.CONVERTER_BEAN_NAME;
import static cm.example.rabbitmqhw.config.Config.MAIN_ROUTING_KEY;

@Service
@Slf4j
@RequiredArgsConstructor
public class Listener {

    @RabbitListener(
            queues = MAIN_ROUTING_KEY,
            errorHandler = "retryErrorHandler",
            messageConverter = CONVERTER_BEAN_NAME
    )
    public void listen(
            MyMessage message,
            @Header(AmqpHeaders.MESSAGE_ID) String messageId
    ) throws Exception {
        log.info("Receive message:" + messageId + " user: " + message.getUser() + " message text: " + message.getMessage());
        throw new Exception("Redirect message:" + messageId + " to retry");
    }
}
