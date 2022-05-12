package cm.example.rabbitmqhw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static cm.example.rabbitmqhw.config.Config.DL_ROUTING_KEY;
import static cm.example.rabbitmqhw.config.Config.FAILED_ROUTING_KEY;
import static cm.example.rabbitmqhw.config.Config.MAIN_ROUTING_KEY;

@Configuration
@Import(value = Config.class)
@RequiredArgsConstructor
public class ListenerConfig {


    public static final String RETRY_ROUTING_KEY = MAIN_ROUTING_KEY + ".retry";

    @Value("${app.rabbitmq.main-queue.ttl}")
    private int mainQueueTtl;

    @Value("${app.rabbitmq.main-queue.max-length}")
    private int mainQueueMaxLength;

    @Value("${app.rabbitmq.retry-queue.ttl}")
    private int retryQueueTtl;

    @Bean
    public Queue mainQueue(
            Exchange mainDLExchange
    ) {
        return QueueBuilder
                .durable(MAIN_ROUTING_KEY)
                .ttl(mainQueueTtl)
                .maxLength(mainQueueMaxLength)
                .deadLetterExchange(mainDLExchange.getName())
                .deadLetterRoutingKey(DL_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding mainBinding(
            Exchange mainExchange,
            Exchange mainDLExchange
    ) {
        return BindingBuilder
                .bind(mainQueue(mainDLExchange))
                .to(mainExchange)
                .with(MAIN_ROUTING_KEY)
                .noargs();
    }

    @Bean
    public Queue retryQueue(
            Exchange mainExchange
    ) {
        return QueueBuilder.durable(RETRY_ROUTING_KEY)
                .deadLetterExchange(mainExchange.getName())
                .deadLetterRoutingKey(MAIN_ROUTING_KEY)
                .ttl(retryQueueTtl)
                .build();
    }

    @Bean
    public Binding retryBinding(
            Exchange mainExchange,
            Exchange mainDLExchange
    ) {
        return BindingBuilder.bind(retryQueue(mainExchange))
                .to(mainDLExchange)
                .with(RETRY_ROUTING_KEY)
                .noargs();
    }

    @Bean
    public RepublishMessageRecoverer retryMessageRecoverer(
             AmqpTemplate amqpTemplate
    ) {
        return new RepublishMessageRecoverer(amqpTemplate,DL_ROUTING_KEY,RETRY_ROUTING_KEY);
    }

    @Bean
    public RepublishMessageRecoverer failedMessageRecoverer(
            AmqpTemplate amqpTemplate
    ) {
        return new RepublishMessageRecoverer(amqpTemplate,DL_ROUTING_KEY,FAILED_ROUTING_KEY);
    }
}
