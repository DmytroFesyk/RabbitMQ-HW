package cm.example.rabbitmqhw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RetryErrorHandler implements RabbitListenerErrorHandler {
    private static String RETRY_COUNT_HEADER = "x-death";

    private final RepublishMessageRecoverer retryMessageRecoverer;

    private final RepublishMessageRecoverer failedMessageRecoverer;

    @Value("${app.rabbitmq.retry-queue.max-attempts}")
    private int maxAttemptsQueueTtl;

    @Override
    public Object handleError(
            Message amqpMessage,
            org.springframework.messaging.Message<?> message,
            ListenerExecutionFailedException exception
    ) {
        var deathHeaders = (List<Map<String, Object>>) amqpMessage.getMessageProperties().getHeader(RETRY_COUNT_HEADER);
        var retryCount = 0L;
        if (deathHeaders != null && deathHeaders.size() > 0) {
            retryCount = (long) deathHeaders.get(0).get("count");
        }
        if (retryCount < maxAttemptsQueueTtl) {
            retryMessageRecoverer.recover(amqpMessage, exception);
            throw new ImmediateAcknowledgeAmqpException("Retrying the message:" + amqpMessage.getMessageProperties().getMessageId()
                    + " attempt: " + ++retryCount);
        }
        failedMessageRecoverer.recover(amqpMessage, exception);
        throw new ImmediateAcknowledgeAmqpException("Failed the message " + amqpMessage.getMessageProperties().getMessageId());
    }
}
