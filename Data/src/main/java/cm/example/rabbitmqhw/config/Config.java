package cm.example.rabbitmqhw.config;

import lombok.val;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    public static final String MAIN_ROUTING_KEY = "main";
    public static final String DL_ROUTING_KEY = MAIN_ROUTING_KEY+".dlx";
    public static final String FAILED_ROUTING_KEY = MAIN_ROUTING_KEY+".failed";

    public static final String CONVERTER_BEAN_NAME= "jsonConverter";

    @Bean
    public MessageConverter jsonConverter() {
        val converter = new Jackson2JsonMessageConverter();
        converter.setCreateMessageIds(true);
        return converter;
    }

    @Bean
    public Exchange mainExchange() {
        return ExchangeBuilder.directExchange(MAIN_ROUTING_KEY)
                .durable(true)
                .build();
    }

    @Bean
    public Exchange mainDLExchange() {
        return ExchangeBuilder.directExchange(DL_ROUTING_KEY)
                .durable(true)
                .build();
    }

    @Bean
    public Queue dlQueue(){
        return QueueBuilder
                .durable(DL_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding dlqBinding(
            Exchange mainDLExchange
    ) {
        return BindingBuilder
                .bind(dlQueue())
                .to(mainDLExchange)
                .with(DL_ROUTING_KEY)
                .noargs();
    }

    @Bean
    public Queue failedQueue(){
        return QueueBuilder
                .durable(FAILED_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding failedBinding(
            Exchange mainDLExchange
    ) {
        return BindingBuilder
                .bind(failedQueue())
                .to(mainDLExchange)
                .with(FAILED_ROUTING_KEY)
                .noargs();
    }
}
